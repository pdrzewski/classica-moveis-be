package sptech.classicamoveis.Estabelecimento;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.classicamoveis.Colaborador.model.Colaborador;
import sptech.classicamoveis.Colaborador.repository.ColaboradorRepository;
import sptech.classicamoveis.Endereco.Endereco;
import sptech.classicamoveis.Endereco.repository.EnderecoRepository;
import sptech.classicamoveis.Estabelecimento.dto.EstabelecimentoComEnderecoRequestDto;
import sptech.classicamoveis.Estabelecimento.dto.EstabelecimentoRequestDto;
import sptech.classicamoveis.Estabelecimento.dto.EstabelecimentoResponseDto;
import sptech.classicamoveis.Estabelecimento.mapper.EstabelecimentoMapper;
import sptech.classicamoveis.Estabelecimento.Estabelecimento;
import sptech.classicamoveis.Estabelecimento.repository.EstabelecimentoRepository;
import sptech.classicamoveis.Estabelecimento.service.EstabelecimentoService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstabelecimentoServiceTest {

 @Mock
 EstabelecimentoRepository repository;

 @Mock
 EnderecoRepository enderecoRepository;

 @Mock
 ColaboradorRepository colaboradorRepository;

 @Mock
 EstabelecimentoMapper mapper;

 @InjectMocks
 EstabelecimentoService service;

 private Estabelecimento criarEstabelecimento() {
  Estabelecimento estabelecimento = new Estabelecimento();

  estabelecimento.setId(1);
  estabelecimento.setNome("Loja Centro");

  return estabelecimento;
 }

 // =========================================================
 // LISTAR
 // =========================================================

 @Test
 void listar() {

  Estabelecimento estabelecimento = criarEstabelecimento();

  EstabelecimentoResponseDto response =
          new EstabelecimentoResponseDto(
                  1,
                  "Loja Centro",
                  null,
                  null,
                  null,
                  null,
                  null,
                  null,
                  null,
                  null,
                  null,
                  null,
                  null
          );

  when(repository.findAll())
          .thenReturn(List.of(estabelecimento));

  when(mapper.toResponseDTO(any()))
          .thenReturn(response);

  List<EstabelecimentoResponseDto> resultado =
          service.listarTodos();

  assertNotNull(resultado);
  assertEquals(1, resultado.size());
  assertEquals("Loja Centro", resultado.get(0).getNome());

  verify(repository).findAll();
  verify(mapper).toResponseDTO(estabelecimento);
 }

 // =========================================================
 // BUSCAR POR ID
 // =========================================================

 @Test
 void buscar() {

  Estabelecimento estabelecimento = criarEstabelecimento();

  EstabelecimentoResponseDto response =
          new EstabelecimentoResponseDto(
                  1,
                  "Loja",
                  null,
                  null,
                  null,
                  null,
                  null,
                  null,
                  null,
                  null,
                  null,
                  null,
                  null
          );

  when(repository.findById(1))
          .thenReturn(Optional.of(estabelecimento));

  when(mapper.toResponseDTO(estabelecimento))
          .thenReturn(response);

  EstabelecimentoResponseDto resultado =
          service.buscarPorId(1);

  assertNotNull(resultado);
  assertEquals(1, resultado.getId());
  assertEquals("Loja", resultado.getNome());

  verify(repository).findById(1);
  verify(mapper).toResponseDTO(estabelecimento);
 }

 // =========================================================
 // BUSCAR - ID INEXISTENTE
 // =========================================================

 @Test
 void buscarInexistente() {

  when(repository.findById(9))
          .thenReturn(Optional.empty());

  assertThrows(
          EntityNotFoundException.class,
          () -> service.buscarPorId(9)
  );

  verify(repository).findById(9);
  verify(mapper, never()).toResponseDTO(any());
 }

 // =========================================================
 // CRIAR
 // =========================================================

 @Test
 void criar() {

  EstabelecimentoComEnderecoRequestDto dto =
          new EstabelecimentoComEnderecoRequestDto(
                  "Loja",
                  "123",
                  "119",
                  5,
                  "01000",
                  "Rua",
                  "Centro",
                  "SP",
                  "10",
                  null,
                  "SP"
          );

  Endereco endereco = new Endereco();

  Colaborador colaborador = new Colaborador();
  colaborador.setId(5);

  Estabelecimento saved = criarEstabelecimento();

  EstabelecimentoResponseDto response =
          new EstabelecimentoResponseDto(
                  1,
                  "Loja",
                  null,
                  null,
                  5,
                  null,
                  null,
                  null,
                  null,
                  null,
                  null,
                  null,
                  null
          );

  when(enderecoRepository.save(any(Endereco.class)))
          .thenReturn(endereco);

  when(colaboradorRepository.findById(5))
          .thenReturn(Optional.of(colaborador));

  when(repository.save(any(Estabelecimento.class)))
          .thenReturn(saved);

  when(mapper.toResponseDTO(saved))
          .thenReturn(response);

  EstabelecimentoResponseDto resultado =
          service.criar(dto);

  assertNotNull(resultado);
  assertEquals("Loja", resultado.getNome());

  verify(enderecoRepository).save(any(Endereco.class));
  verify(colaboradorRepository).findById(5);
  verify(repository).save(any(Estabelecimento.class));
  verify(mapper).toResponseDTO(saved);
 }

 // =========================================================
 // CRIAR - RESPONSÁVEL INEXISTENTE
 // =========================================================

 @Test
 void criarResponsavelInexistente() {

  EstabelecimentoComEnderecoRequestDto dto =
          new EstabelecimentoComEnderecoRequestDto(
                  "Loja",
                  "123",
                  "119",
                  5,
                  null,
                  null,
                  null,
                  null,
                  null,
                  null,
                  null
          );

  when(enderecoRepository.save(any(Endereco.class)))
          .thenReturn(new Endereco());

  when(colaboradorRepository.findById(5))
          .thenReturn(Optional.empty());

  assertThrows(
          EntityNotFoundException.class,
          () -> service.criar(dto)
  );

  verify(colaboradorRepository).findById(5);
  verify(repository, never()).save(any());
 }

 // =========================================================
 // ATUALIZAR
 // =========================================================

 @Test
 void atualizar() {

  Estabelecimento estabelecimento =
          criarEstabelecimento();

  Endereco endereco = new Endereco();

  Colaborador colaborador = new Colaborador();

  EstabelecimentoResponseDto response =
          new EstabelecimentoResponseDto(
                  1,
                  "Nova",
                  null,
                  null,
                  3,
                  2,
                  null,
                  null,
                  null,
                  null,
                  null,
                  null,
                  null
          );

  EstabelecimentoRequestDto dto =
          new EstabelecimentoRequestDto(
                  "Nova",
                  2,
                  "cnpj",
                  "tel",
                  3
          );

  when(repository.findById(1))
          .thenReturn(Optional.of(estabelecimento));

  when(enderecoRepository.findById(2))
          .thenReturn(Optional.of(endereco));

  when(colaboradorRepository.findById(3))
          .thenReturn(Optional.of(colaborador));

  when(repository.save(estabelecimento))
          .thenReturn(estabelecimento);

  when(mapper.toResponseDTO(estabelecimento))
          .thenReturn(response);

  EstabelecimentoResponseDto resultado =
          service.atualizar(1, dto);

  assertNotNull(resultado);
  assertEquals("Nova", resultado.getNome());
  assertEquals(3, resultado.getResponsavelId());
  assertEquals(2, resultado.getEnderecoId());

  verify(repository).findById(1);
  verify(enderecoRepository).findById(2);
  verify(colaboradorRepository).findById(3);
  verify(repository).save(estabelecimento);
  verify(mapper).toResponseDTO(estabelecimento);
 }

 // =========================================================
 // ATUALIZAR - DTO NULO
 // =========================================================

 @Test
 void atualizarDtoNulo() {

  when(repository.findById(1))
          .thenReturn(Optional.of(criarEstabelecimento()));

  assertThrows(
          IllegalArgumentException.class,
          () -> service.atualizar(1, null)
  );
 }

 // =========================================================
 // ATUALIZAR - ESTABELECIMENTO INEXISTENTE
 // =========================================================

 @Test
 void atualizarEstabelecimentoInexistente() {

  when(repository.findById(99))
          .thenReturn(Optional.empty());

  EstabelecimentoRequestDto dto =
          new EstabelecimentoRequestDto(
                  "Nova",
                  2,
                  "cnpj",
                  "tel",
                  3
          );

  assertThrows(
          EntityNotFoundException.class,
          () -> service.atualizar(99, dto)
  );

  verify(repository).findById(99);
  verify(repository, never()).save(any());
 }

 // =========================================================
 // ATUALIZAR - ENDEREÇO INEXISTENTE
 // =========================================================

 @Test
 void atualizarEnderecoInexistente() {

  when(repository.findById(1))
          .thenReturn(Optional.of(criarEstabelecimento()));

  when(enderecoRepository.findById(2))
          .thenReturn(Optional.empty());

  EstabelecimentoRequestDto dto =
          new EstabelecimentoRequestDto(
                  "Nova",
                  2,
                  "c",
                  "t",
                  3
          );

  assertThrows(
          EntityNotFoundException.class,
          () -> service.atualizar(1, dto)
  );

  verify(enderecoRepository).findById(2);
  verify(repository, never()).save(any());
 }

 // =========================================================
 // ATUALIZAR - RESPONSÁVEL INEXISTENTE
 // =========================================================

 @Test
 void atualizarResponsavelInexistente() {

  when(repository.findById(1))
          .thenReturn(Optional.of(criarEstabelecimento()));

  when(enderecoRepository.findById(2))
          .thenReturn(Optional.of(new Endereco()));

  when(colaboradorRepository.findById(3))
          .thenReturn(Optional.empty());

  EstabelecimentoRequestDto dto =
          new EstabelecimentoRequestDto(
                  "Nova",
                  2,
                  "c",
                  "t",
                  3
          );

  assertThrows(
          EntityNotFoundException.class,
          () -> service.atualizar(1, dto)
  );

  verify(colaboradorRepository).findById(3);
  verify(repository, never()).save(any());
 }

 // =========================================================
 // DELETAR
 // =========================================================

 @Test
 void deletar() {

  Estabelecimento estabelecimento =
          criarEstabelecimento();

  when(repository.findById(1))
          .thenReturn(Optional.of(estabelecimento));

  service.deletar(1);

  verify(repository).findById(1);
  verify(repository).delete(estabelecimento);
 }

 // =========================================================
 // DELETAR - INEXISTENTE
 // =========================================================

 @Test
 void deletarInexistente() {

  when(repository.findById(99))
          .thenReturn(Optional.empty());

  assertThrows(
          EntityNotFoundException.class,
          () -> service.deletar(99)
  );

  verify(repository).findById(99);
  verify(repository, never()).delete(any());
 }
}