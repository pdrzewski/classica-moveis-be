package sptech.classicamoveis.Colaborador;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;
import sptech.classicamoveis.Cargo.model.Cargo;
import sptech.classicamoveis.Cargo.repository.CargoRepository;
import sptech.classicamoveis.Colaborador.dto.ColaboradorRequestDto;
import sptech.classicamoveis.Colaborador.dto.ColaboradorResponseDto;
import sptech.classicamoveis.Colaborador.model.Colaborador;
import sptech.classicamoveis.Colaborador.repository.ColaboradorRepository;
import sptech.classicamoveis.Colaborador.service.ColaboradorService;
import sptech.classicamoveis.Endereco.Endereco;
import sptech.classicamoveis.Endereco.repository.EnderecoRepository;
import sptech.classicamoveis.Estabelecimento.Estabelecimento;
import sptech.classicamoveis.Estabelecimento.repository.EstabelecimentoRepository;
import sptech.classicamoveis.Usuario.dto.UsuarioRequestDto;
import sptech.classicamoveis.Usuario.model.Usuario;
import sptech.classicamoveis.Usuario.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/** Cobre duplicidade, férias, aniversários e entidades não encontradas do ColaboradorService. */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ColaboradorServiceComplementoTest {

    @Mock ColaboradorRepository colaboradorRepository;
    @Mock CargoRepository cargoRepository;
    @Mock UsuarioRepository usuarioRepository;
    @Mock EstabelecimentoRepository estabelecimentoRepository;
    @Mock EnderecoRepository enderecoRepository;
    @Mock PasswordEncoder passwordEncoder;
    @InjectMocks ColaboradorService service;

    // ---------- helpers ----------

    private Colaborador colaborador(int id, String cpf) {
        Colaborador c = new Colaborador();
        c.setId(id);
        c.setNome("Ana " + id);
        c.setCpf(cpf);
        return c;
    }

    private ColaboradorRequestDto dto(Integer usuarioId, UsuarioRequestDto usuario, String cpf,
                                      Integer estabelecimentoId, Integer enderecoId) {
        return new ColaboradorRequestDto("Ana", 2, usuarioId, usuario, false,
                LocalDate.of(2020, 1, 10), LocalDate.of(1990, 5, 20), 3000.0, "123", 5,
                estabelecimentoId, cpf, "11999999999", null, enderecoId);
    }

    private void prepararAtualizacao() {
        Cargo cargo = new Cargo();
        cargo.setId(2);
        cargo.setCargo("Vendedor");
        when(colaboradorRepository.findById(5)).thenReturn(Optional.of(colaborador(5, "111")));
        when(cargoRepository.findById(2)).thenReturn(Optional.of(cargo));
        when(colaboradorRepository.save(any(Colaborador.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    // ---------- duplicidade ----------

    @Test
    void criarRejeitaUsuarioJaVinculadoAOutroColaborador() {
        when(colaboradorRepository.existsByUsuario_Id(7)).thenReturn(true);
        when(colaboradorRepository.findByUsuario_Id(7)).thenReturn(Optional.of(colaborador(99, null)));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.criar(dto(7, new UsuarioRequestDto("ana", "123"), null, null, null)));

        assertTrue(ex.getMessage().contains("usuário"));
        verify(colaboradorRepository, never()).save(any());
    }

    @Test
    void criarRejeitaCpfJaCadastrado() {
        when(colaboradorRepository.existsByCpf("111")).thenReturn(true);
        when(colaboradorRepository.findAll()).thenReturn(List.of(colaborador(5, "111")));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.criar(dto(null, new UsuarioRequestDto("ana", "123"), "111", null, null)));

        assertTrue(ex.getMessage().contains("CPF"));
    }

    @Test
    void criarIgnoraCpfEmBranco() {
        when(usuarioRepository.findByLogin("ana")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123")).thenReturn("hash");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
        Cargo cargo = new Cargo();
        cargo.setId(2);
        when(cargoRepository.findById(2)).thenReturn(Optional.of(cargo));
        when(colaboradorRepository.save(any(Colaborador.class))).thenAnswer(inv -> inv.getArgument(0));

        ColaboradorResponseDto resp = service.criar(dto(null, new UsuarioRequestDto("ana", "123"), "  ", null, null));

        assertEquals("Ana", resp.getNome());
        verify(colaboradorRepository, never()).existsByCpf(any());
    }

    @Test
    void atualizarPermiteOMesmoColaboradorComMesmoUsuarioECpf() {
        prepararAtualizacao();
        Usuario usuario = new Usuario();
        usuario.setId(7);
        when(usuarioRepository.findById(7)).thenReturn(Optional.of(usuario));
        when(colaboradorRepository.existsByUsuario_Id(7)).thenReturn(true);
        when(colaboradorRepository.findByUsuario_Id(7)).thenReturn(Optional.of(colaborador(5, "111")));
        when(colaboradorRepository.existsByCpf("111")).thenReturn(true);
        when(colaboradorRepository.findAll()).thenReturn(List.of(colaborador(5, "111")));

        ColaboradorResponseDto resp = service.atualizar(5, dto(7, null, "111", null, null));

        assertEquals(7, resp.getUsuarioId());
        assertEquals(2, resp.getCargoId());
    }

    @Test
    void atualizarRejeitaUsuarioDeOutroColaborador() {
        prepararAtualizacao();
        when(colaboradorRepository.existsByUsuario_Id(7)).thenReturn(true);
        when(colaboradorRepository.findByUsuario_Id(7)).thenReturn(Optional.of(colaborador(6, null)));

        assertThrows(IllegalArgumentException.class, () -> service.atualizar(5, dto(7, null, null, null, null)));
    }

    @Test
    void atualizarRejeitaCpfDeOutroColaborador() {
        prepararAtualizacao();
        when(colaboradorRepository.existsByCpf("222")).thenReturn(true);
        when(colaboradorRepository.findAll()).thenReturn(List.of(colaborador(6, "222")));

        assertThrows(IllegalArgumentException.class, () -> service.atualizar(5, dto(null, null, "222", null, null)));
    }

    @Test
    void atualizarComCpfExistenteMasSemColaboradorEncontradoNaoLanca() {
        prepararAtualizacao();
        when(colaboradorRepository.existsByCpf("222")).thenReturn(true);
        when(colaboradorRepository.findAll()).thenReturn(List.of());

        assertDoesNotThrow(() -> service.atualizar(5, dto(null, null, "222", null, null)));
    }

    // ---------- atualizar: relacionamentos ----------

    @Test
    void atualizarComUsuarioInexistente() {
        prepararAtualizacao();
        when(usuarioRepository.findById(7)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.atualizar(5, dto(7, null, null, null, null)));
    }

    @Test
    void atualizarComEstabelecimentoEEnderecoMapeiaNaResposta() {
        prepararAtualizacao();
        Estabelecimento loja = new Estabelecimento();
        loja.setId(3);
        when(estabelecimentoRepository.findById(3)).thenReturn(Optional.of(loja));
        when(enderecoRepository.findById(4)).thenReturn(
                Optional.of(new Endereco(4, "01001000", "Rua A", "Centro", "São Paulo", "10", "Sala 1", "SP")));

        ColaboradorResponseDto resp = service.atualizar(5, dto(null, null, null, 3, 4));

        assertEquals(3, resp.getEstabelecimentoId());
        assertNotNull(resp.getEndereco());
        assertEquals("01001000", resp.getEndereco().cep());
        assertEquals("Rua A", resp.getEndereco().logradouro());
    }

    @Test
    void atualizarComEstabelecimentoInexistente() {
        prepararAtualizacao();
        when(estabelecimentoRepository.findById(3)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.atualizar(5, dto(null, null, null, 3, null)));
    }

    @Test
    void atualizarComEnderecoInexistente() {
        prepararAtualizacao();
        when(enderecoRepository.findById(4)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.atualizar(5, dto(null, null, null, null, 4)));
    }

    // ---------- férias ----------

    private Colaborador emFerias(Boolean flag, LocalDate inicio, LocalDate fim) {
        Colaborador c = colaborador(1, null);
        c.setEmFerias(flag);
        c.setFeriasDataInicio(inicio);
        c.setFeriasDataFim(fim);
        return c;
    }

    @Test
    void colaboradorNuloNaoEstaDeFerias() {
        assertFalse(service.estaDeFerias((Colaborador) null));
    }

    @Test
    void flagNulaOuFalsaNaoEstaDeFerias() {
        assertFalse(service.estaDeFerias(emFerias(null, null, null)));
        assertFalse(service.estaDeFerias(emFerias(false, null, null)));
    }

    @Test
    void emFeriasSemDatasConsideraDeFerias() {
        assertTrue(service.estaDeFerias(emFerias(true, null, null)));
        assertTrue(service.estaDeFerias(emFerias(true, LocalDate.now(), null)));
    }

    @Test
    void feriasSoValemDentroDoPeriodo() {
        LocalDate hoje = LocalDate.now();

        assertTrue(service.estaDeFerias(emFerias(true, hoje.minusDays(1), hoje.plusDays(1))));
        assertTrue(service.estaDeFerias(emFerias(true, hoje, hoje)));
        assertFalse(service.estaDeFerias(emFerias(true, hoje.plusDays(1), hoje.plusDays(5))));
        assertFalse(service.estaDeFerias(emFerias(true, hoje.minusDays(5), hoje.minusDays(1))));
    }

    @Test
    void estaDeFeriasPorId() {
        when(colaboradorRepository.findById(1)).thenReturn(Optional.of(emFerias(true, null, null)));
        when(colaboradorRepository.findById(2)).thenReturn(Optional.empty());

        assertTrue(service.estaDeFerias(1));
        assertFalse(service.estaDeFerias(2));
    }

    @Test
    void listarEmFeriasFiltraQuemNaoEstaDeFerias() {
        Colaborador de = emFerias(true, null, null);
        Colaborador fora = emFerias(false, null, null);
        when(colaboradorRepository.findAll()).thenReturn(List.of(de, fora));

        List<ColaboradorResponseDto> resp = service.listarEmFerias();

        assertEquals(1, resp.size());
    }

    @Test
    void verificarFeriasExpiradasSemDataFimNaoFazNada() {
        Colaborador c = emFerias(true, LocalDate.now().minusDays(3), null);

        service.verificarFeriasExpiradas(c);

        assertTrue(c.getEmFerias());
        verify(colaboradorRepository, never()).save(any());
    }

    @Test
    void verificarFeriasExpiradasMantemFeriasEmAndamento() {
        Colaborador c = emFerias(true, LocalDate.now().minusDays(1), LocalDate.now());

        service.verificarFeriasExpiradas(c);

        assertTrue(c.getEmFerias());
        assertNotNull(c.getFeriasDataFim());
        verify(colaboradorRepository, never()).save(any());
    }

    @Test
    void verificarFeriasExpiradasLimpaEPersisteQuandoJaTerminaram() {
        Colaborador c = emFerias(true, LocalDate.now().minusDays(10), LocalDate.now().minusDays(1));

        service.verificarFeriasExpiradas(c);

        assertFalse(c.getEmFerias());
        assertNull(c.getFeriasDataInicio());
        assertNull(c.getFeriasDataFim());
        verify(colaboradorRepository).save(c);
    }

    // ---------- aniversários ----------

    @Test
    void aniversarioDeHojeTemZeroDias() {
        Colaborador c = colaborador(1, null);
        c.setDataNascimento(LocalDate.now().minusYears(30));
        when(colaboradorRepository.findAll()).thenReturn(List.of(c));

        List<AniversarioColaboradorDto> resp = service.buscarAniversariosProximos(0);

        assertEquals(1, resp.size());
        assertEquals(0, resp.get(0).diasParaAniversario());
    }

    @Test
    void aniversarioEm29DeFevereiroNaoQuebraEmAnoNaoBissexto() {
        Colaborador c = colaborador(1, null);
        c.setDataNascimento(LocalDate.of(2000, 2, 29));
        when(colaboradorRepository.findAll()).thenReturn(List.of(c));

        List<AniversarioColaboradorDto> resp = service.buscarAniversariosProximos(366);

        assertEquals(1, resp.size());
        assertTrue(resp.get(0).diasParaAniversario() >= 0 && resp.get(0).diasParaAniversario() <= 366);
    }
}
