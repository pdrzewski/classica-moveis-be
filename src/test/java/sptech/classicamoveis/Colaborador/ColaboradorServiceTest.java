package sptech.classicamoveis.Colaborador;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.classicamoveis.Cargo.model.Cargo;
import sptech.classicamoveis.Cargo.repository.CargoRepository;
import sptech.classicamoveis.Colaborador.FeriasRequestDto;
import sptech.classicamoveis.Colaborador.dto.ColaboradorRequestDto;
import sptech.classicamoveis.Colaborador.dto.ColaboradorResponseDto;
import sptech.classicamoveis.Colaborador.model.Colaborador;
import sptech.classicamoveis.Colaborador.repository.ColaboradorRepository;
import sptech.classicamoveis.Colaborador.service.ColaboradorService;
import sptech.classicamoveis.Estabelecimento.Estabelecimento;
import sptech.classicamoveis.Usuario.model.Usuario;
import sptech.classicamoveis.Usuario.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ColaboradorServiceTest {

 @Mock
 ColaboradorRepository colaboradorRepository;

 @Mock
 CargoRepository cargoRepository;

 @Mock
 UsuarioRepository usuarioRepository;

 @InjectMocks
 ColaboradorService service;

 // =========================================================
 // OBJETOS AUXILIARES
 // =========================================================

 private Cargo cargo() {

  Cargo cargo = new Cargo();

  cargo.setId(2);
  cargo.setCargo("Vendedor");

  return cargo;
 }

 private Usuario usuario() {

  Usuario usuario = new Usuario();

  usuario.setId(3);
  usuario.setLogin("ana");

  return usuario;
 }

 private Estabelecimento estabelecimento() {

  Estabelecimento estabelecimento =
          new Estabelecimento();

  estabelecimento.setId(9);
  estabelecimento.setNome("Loja Centro");

  return estabelecimento;
 }

 private Colaborador colaborador() {

  Colaborador colaborador =
          new Colaborador();

  colaborador.setId(1);
  colaborador.setNome("Ana");

  colaborador.setCargo(cargo());
  colaborador.setUsuario(usuario());
  colaborador.setEstabelecimento(estabelecimento());

  colaborador.setEmFerias(false);

  colaborador.setDataNascimento(
          LocalDate.of(2000, 1, 1)
  );

  colaborador.setDataAdmissao(
          LocalDate.of(2024, 1, 1)
  );

  colaborador.setSalario(2000.0);
  colaborador.setCarteiraTrabalho("123");
  colaborador.setComissao(5);
  colaborador.setCpf("111");

  return colaborador;
 }

 private ColaboradorRequestDto dto(Boolean ferias) {

  return new ColaboradorRequestDto(
          "Ana",
          2,
          3,
          ferias,
          LocalDate.of(2024, 1, 1),
          LocalDate.of(2000, 1, 1),
          2000.0,
          "123",
          5,
          9,
          "111"
  );
 }

 // =========================================================
 // LISTAR TODOS
 // =========================================================

 @Test
 void listar() {

  when(colaboradorRepository.findAll())
          .thenReturn(List.of(colaborador()));

  List<ColaboradorResponseDto> resultado =
          service.listarTodos();

  assertNotNull(resultado);

  assertEquals(1, resultado.size());

  assertEquals(
          "Ana",
          resultado.get(0).getNome()
  );

  assertEquals(
          1,
          resultado.get(0).getId()
  );

  assertEquals(
          2,
          resultado.get(0).getCargoId()
  );

  assertEquals(
          "Vendedor",
          resultado.get(0).getCargoNome()
  );

  assertEquals(
          3,
          resultado.get(0).getUsuarioId()
  );

  verify(colaboradorRepository).findAll();
 }

 // =========================================================
 // BUSCAR POR ID
 // =========================================================

 @Test
 void buscar() {

  Colaborador colaborador = colaborador();

  when(colaboradorRepository.findById(1))
          .thenReturn(Optional.of(colaborador));

  ColaboradorResponseDto resultado =
          service.buscarPorId(1);

  assertNotNull(resultado);

  assertEquals(
          "Ana",
          resultado.getNome()
  );

  assertEquals(
          1,
          resultado.getId()
  );

  verify(colaboradorRepository).findById(1);
 }

 // =========================================================
 // BUSCAR INEXISTENTE
 // =========================================================

 @Test
 void buscarInexistente() {

  when(colaboradorRepository.findById(1))
          .thenReturn(Optional.empty());

  assertThrows(
          EntityNotFoundException.class,
          () -> service.buscarPorId(1)
  );

  verify(colaboradorRepository)
          .findById(1);
 }

 // =========================================================
 // CRIAR
 // =========================================================

 @Test
 void criar() {

  when(cargoRepository.findById(2))
          .thenReturn(Optional.of(cargo()));

  when(usuarioRepository.findById(3))
          .thenReturn(Optional.of(usuario()));

  when(colaboradorRepository.save(any()))
          .thenAnswer(invocation -> {

           Colaborador c =
                   invocation.getArgument(0);

           c.setId(1);
           c.setEstabelecimento(
                   estabelecimento()
           );

           return c;
          });

  ColaboradorResponseDto resultado =
          service.criar(dto(false));

  assertNotNull(resultado);

  assertEquals(
          "Ana",
          resultado.getNome()
  );

  assertEquals(
          1,
          resultado.getId()
  );

  assertFalse(
          resultado.getEmFerias()
  );

  verify(cargoRepository)
          .findById(2);

  verify(usuarioRepository)
          .findById(3);

  verify(colaboradorRepository)
          .save(any(Colaborador.class));
 }

 // =========================================================
 // CRIAR - CARGO INEXISTENTE
 // =========================================================

 @Test
 void criarCargoInexistente() {

  when(cargoRepository.findById(2))
          .thenReturn(Optional.empty());

  assertThrows(
          EntityNotFoundException.class,
          () -> service.criar(dto(false))
  );

  verify(cargoRepository)
          .findById(2);

  verifyNoInteractions(
          usuarioRepository
  );

  verify(
          colaboradorRepository,
          never()
  ).save(any());
 }

 // =========================================================
 // CRIAR - USUÁRIO INEXISTENTE
 // =========================================================

 @Test
 void criarUsuarioInexistente() {

  when(cargoRepository.findById(2))
          .thenReturn(Optional.of(cargo()));

  when(usuarioRepository.findById(3))
          .thenReturn(Optional.empty());

  assertThrows(
          EntityNotFoundException.class,
          () -> service.criar(dto(false))
  );

  verify(cargoRepository)
          .findById(2);

  verify(usuarioRepository)
          .findById(3);

  verify(
          colaboradorRepository,
          never()
  ).save(any());
 }

 // =========================================================
 // ATUALIZAR
 // =========================================================

 @Test
 void atualizar() {

  Colaborador colaborador =
          colaborador();

  when(colaboradorRepository.findById(1))
          .thenReturn(
                  Optional.of(colaborador)
          );

  when(cargoRepository.findById(2))
          .thenReturn(
                  Optional.of(cargo())
          );

  when(usuarioRepository.findById(3))
          .thenReturn(
                  Optional.of(usuario())
          );

  when(colaboradorRepository.save(colaborador))
          .thenReturn(colaborador);

  ColaboradorResponseDto resultado =
          service.atualizar(
                  1,
                  dto(true)
          );

  assertNotNull(resultado);

  assertEquals(
          "Ana",
          resultado.getNome()
  );

  assertTrue(
          resultado.getEmFerias()
  );

  verify(colaboradorRepository)
          .findById(1);

  verify(cargoRepository)
          .findById(2);

  verify(usuarioRepository)
          .findById(3);

  verify(colaboradorRepository)
          .save(colaborador);
 }

 // =========================================================
 // ATUALIZAR - FÉRIAS NULL
 // =========================================================

 @Test
 void atualizarComFeriasNulasDefineFalse() {

  Colaborador colaborador =
          colaborador();

  colaborador.setEmFerias(true);

  when(colaboradorRepository.findById(1))
          .thenReturn(
                  Optional.of(colaborador)
          );

  when(cargoRepository.findById(2))
          .thenReturn(
                  Optional.of(cargo())
          );

  when(usuarioRepository.findById(3))
          .thenReturn(
                  Optional.of(usuario())
          );

  when(colaboradorRepository.save(colaborador))
          .thenReturn(colaborador);

  service.atualizar(
          1,
          dto(null)
  );

  assertFalse(
          colaborador.getEmFerias()
  );
 }

 // =========================================================
 // ATUALIZAR - COLABORADOR INEXISTENTE
 // =========================================================

 @Test
 void atualizarColaboradorInexistente() {

  when(colaboradorRepository.findById(99))
          .thenReturn(Optional.empty());

  assertThrows(
          EntityNotFoundException.class,
          () -> service.atualizar(
                  99,
                  dto(false)
          )
  );

  verify(
          colaboradorRepository
  ).findById(99);

  verify(
          colaboradorRepository,
          never()
  ).save(any());
 }

 // =========================================================
 // DELETAR COM USUÁRIO
 // =========================================================

 @Test
 void deletarComUsuario() {

  Colaborador colaborador =
          colaborador();

  when(colaboradorRepository.findById(1))
          .thenReturn(
                  Optional.of(colaborador)
          );

  service.deletar(1);

  verify(colaboradorRepository)
          .delete(colaborador);

  verify(usuarioRepository)
          .deleteById(3);
 }

 // =========================================================
 // DELETAR SEM USUÁRIO
 // =========================================================

 @Test
 void deletarSemUsuario() {

  Colaborador colaborador =
          colaborador();

  colaborador.setUsuario(null);

  when(colaboradorRepository.findById(1))
          .thenReturn(
                  Optional.of(colaborador)
          );

  service.deletar(1);

  verify(colaboradorRepository)
          .delete(colaborador);

  verify(
          usuarioRepository,
          never()
  ).deleteById(any());
 }

 // =========================================================
 // DELETAR INEXISTENTE
 // =========================================================

 @Test
 void deletarInexistente() {

  when(colaboradorRepository.findById(99))
          .thenReturn(Optional.empty());

  assertThrows(
          EntityNotFoundException.class,
          () -> service.deletar(99)
  );

  verify(
          colaboradorRepository
  ).findById(99);

  verify(
          colaboradorRepository,
          never()
  ).delete(any());
 }

 // =========================================================
 // REGISTRAR FÉRIAS
 // =========================================================

 @Test
 void ferias() {

  Colaborador colaborador =
          colaborador();

  when(colaboradorRepository.findById(1))
          .thenReturn(
                  Optional.of(colaborador)
          );

  when(colaboradorRepository.save(colaborador))
          .thenReturn(colaborador);

  LocalDate inicio =
          LocalDate.of(2026, 9, 1);

  LocalDate fim =
          LocalDate.of(2026, 9, 10);

  FeriasRequestDto dto =
          new FeriasRequestDto(
                  inicio,
                  fim
          );

  ColaboradorResponseDto resultado =
          service.registrarFerias(
                  1,
                  dto
          );

  assertTrue(
          colaborador.getEmFerias()
  );

  assertEquals(
          inicio,
          colaborador.getFeriasDataInicio()
  );

  assertEquals(
          fim,
          colaborador.getFeriasDataFim()
  );

  assertEquals(
          "Ana",
          resultado.getNome()
  );

  verify(colaboradorRepository)
          .save(colaborador);
 }

 // =========================================================
 // FÉRIAS - DATA INICIAL NULL
 // =========================================================

 @Test
 void feriasSemDataInicio() {

  FeriasRequestDto dto =
          new FeriasRequestDto(
                  null,
                  LocalDate.of(2026, 9, 10)
          );

  assertThrows(
          IllegalArgumentException.class,
          () -> service.registrarFerias(
                  1,
                  dto
          )
  );

  verifyNoInteractions(
          colaboradorRepository
  );
 }

 // =========================================================
 // FÉRIAS - DATA FINAL NULL
 // =========================================================

 @Test
 void feriasSemDataFim() {

  FeriasRequestDto dto =
          new FeriasRequestDto(
                  LocalDate.of(2026, 9, 1),
                  null
          );

  assertThrows(
          IllegalArgumentException.class,
          () -> service.registrarFerias(
                  1,
                  dto
          )
  );

  verifyNoInteractions(
          colaboradorRepository
  );
 }

 // =========================================================
 // FÉRIAS - DATA INVERTIDA
 // =========================================================

 @Test
 void feriasDataInvertida() {

  LocalDate inicio =
          LocalDate.of(2026, 9, 10);

  LocalDate fim =
          LocalDate.of(2026, 9, 1);

  FeriasRequestDto dto =
          new FeriasRequestDto(
                  inicio,
                  fim
          );

  assertThrows(
          IllegalArgumentException.class,
          () -> service.registrarFerias(
                  1,
                  dto
          )
  );

  verifyNoInteractions(
          colaboradorRepository
  );
 }

 // =========================================================
 // ENCERRAR FÉRIAS
 // =========================================================

 @Test
 void encerrarFerias() {

  Colaborador colaborador =
          colaborador();

  colaborador.setEmFerias(true);

  when(colaboradorRepository.findById(1))
          .thenReturn(
                  Optional.of(colaborador)
          );

  when(colaboradorRepository.save(colaborador))
          .thenReturn(colaborador);

  ColaboradorResponseDto resultado =
          service.encerrarFerias(1);

  assertFalse(
          colaborador.getEmFerias()
  );

  assertEquals(
          "Ana",
          resultado.getNome()
  );

  verify(colaboradorRepository)
          .save(colaborador);
 }

 // =========================================================
 // ENCERRAR FÉRIAS - INEXISTENTE
 // =========================================================

 @Test
 void encerrarFeriasColaboradorInexistente() {

  when(colaboradorRepository.findById(99))
          .thenReturn(Optional.empty());

  assertThrows(
          EntityNotFoundException.class,
          () -> service.encerrarFerias(99)
  );

  verify(
          colaboradorRepository
  ).findById(99);

  verify(
          colaboradorRepository,
          never()
  ).save(any());
 }

 // =========================================================
 // ANIVERSÁRIOS
 // =========================================================

 @Test
 void aniversariosFiltraEOrdena() {

  Colaborador a =
          colaborador();

  a.setDataNascimento(
          LocalDate.now().plusDays(5)
  );

  Colaborador b =
          colaborador();

  b.setId(2);
  b.setNome("Bia");

  b.setDataNascimento(
          LocalDate.now().plusDays(2)
  );

  Colaborador n =
          colaborador();

  n.setId(3);
  n.setDataNascimento(null);

  when(colaboradorRepository.findAll())
          .thenReturn(
                  List.of(a, b, n)
          );

  var resultado =
          service.buscarAniversariosProximos(10);

  assertEquals(
          2,
          resultado.size()
  );

  assertEquals(
          "Bia",
          resultado.get(0).nome()
  );
 }

 // =========================================================
 // ANIVERSÁRIO PASSADO
 // =========================================================

 @Test
 void aniversarioPassadoVaiParaProximoAno() {

  Colaborador colaborador =
          colaborador();

  colaborador.setDataNascimento(
          LocalDate.now().minusDays(2)
  );

  when(colaboradorRepository.findAll())
          .thenReturn(
                  List.of(colaborador)
          );

  var resultado =
          service.buscarAniversariosProximos(400);

  assertEquals(
          1,
          resultado.size()
  );

  assertTrue(
          resultado
                  .get(0)
                  .diasParaAniversario() > 300
  );
 }

 // =========================================================
 // ANIVERSÁRIO SEM DATA DE NASCIMENTO
 // =========================================================

 @Test
 void aniversarioSemDataNascimento() {

  Colaborador colaborador =
          colaborador();

  colaborador.setDataNascimento(null);

  when(colaboradorRepository.findAll())
          .thenReturn(
                  List.of(colaborador)
          );

  var resultado =
          service.buscarAniversariosProximos(10);

  assertNotNull(resultado);

  assertTrue(
          resultado.isEmpty()
  );
 }

 // =========================================================
 // ANIVERSÁRIO FORA DO PERÍODO
 // =========================================================

 @Test
 void aniversarioForaDoPeriodo() {

  Colaborador colaborador =
          colaborador();

  colaborador.setDataNascimento(
          LocalDate.now().plusDays(20)
  );

  when(colaboradorRepository.findAll())
          .thenReturn(
                  List.of(colaborador)
          );

  var resultado =
          service.buscarAniversariosProximos(10);

  assertTrue(
          resultado.isEmpty()
  );
 }
}