package sptech.classicamoveis.Cliente.Mapper;

import sptech.classicamoveis.Cliente.Cliente;
import sptech.classicamoveis.Cliente.dto.ClienteResponseDto;

import java.util.ArrayList;
import java.util.List;

public class ClienteMapper {

    public ClienteResponseDto toResponseDto(Cliente cliente) {
        return new ClienteResponseDto(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEnderecoId(),
                cliente.getDocumento(),
                cliente.getTelefone1(),
                cliente.getTelefone2(),
                cliente.getEmail(),
                cliente.getObservacao(),
                cliente.getIe()
        );
    }

    public List<ClienteResponseDto> toResponseDtoList(List<Cliente> clientes) {
      List<ClienteResponseDto> responseDtos = new ArrayList<>();
      for (Cliente cliente : clientes) {
          responseDtos.add(toResponseDto(cliente));
      }
      return responseDtos;
    }

    public Cliente toEntity(ClienteResponseDto responseDto) {
        Cliente cliente = new Cliente();
        cliente.setId(responseDto.getId());
        cliente.setNome(responseDto.getNome());
        cliente.setEnderecoId(responseDto.getEnderecoId());
        cliente.setDocumento(responseDto.getDocumento());
        cliente.setTelefone1(responseDto.getTelefone1());
        cliente.setTelefone2(responseDto.getTelefone2());
        cliente.setEmail(responseDto.getEmail());
        cliente.setObservacao(responseDto.getObservacao());
        cliente.setIe(responseDto.getIe());
        return cliente;
    }

}
