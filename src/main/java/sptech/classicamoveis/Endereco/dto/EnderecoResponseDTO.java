package sptech.classicamoveis.Endereco.dto;

public record EnderecoResponseDTO(
        Integer id,
        String cep,
        String logradouro,
        String bairro,
        String cidade,
        String numero,
        String complemento,
        String estado
) {
}