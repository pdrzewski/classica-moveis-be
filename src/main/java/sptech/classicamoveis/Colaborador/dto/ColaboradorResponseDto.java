package sptech.classicamoveis.Colaborador.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import sptech.classicamoveis.Endereco.dto.EnderecoResponseDTO;

@Schema(description = "Dados de colaborador retornados pela API")
public class ColaboradorResponseDto {

    @Schema(example = "5")
    private Integer id;
    @Schema(example = "João Pedro Lima")
    private String nome;
    @Schema(example = "2")
    private Integer cargoId;
    @Schema(example = "Vendedor")
    private String cargoNome;
    @Schema(example = "5")
    private Integer usuarioId;
    @Schema(example = "false")
    private Boolean emFerias;
    @Schema(example = "2026-12-01")
    private LocalDate feriasDataInicio;
    @Schema(example = "2026-12-30")
    private LocalDate feriasDataFim;
    @Schema(example = "2023-03-01")
    private LocalDate dataAdmissao;
    @Schema(example = "1995-07-22")
    private LocalDate dataNascimento;
    @Schema(example = "2200.00")
    private Double salario;
    @Schema(example = "1234567")
    private String carteiraTrabalho;
    @Schema(example = "3")
    private Integer comissao;
    @Schema(example = "1")
    private Integer estabelecimentoId;
    @Schema(example = "38912233045")
    private String cpf;
    private String telefone1;
    private String telefone2;
    private EnderecoResponseDTO endereco;

    public ColaboradorResponseDto() {
    }

    public ColaboradorResponseDto(Integer id, String nome, Integer cargoId, String cargoNome, Integer usuarioId,
                                  Boolean emFerias, LocalDate feriasDataInicio, LocalDate feriasDataFim,
                                  LocalDate dataAdmissao, LocalDate dataNascimento, Double salario,
                                  String carteiraTrabalho, Integer comissao, Integer estabelecimentoId, String cpf,
                                  String telefone1, String telefone2, EnderecoResponseDTO endereco) {
        this.id = id;
        this.nome = nome;
        this.cargoId = cargoId;
        this.cargoNome = cargoNome;
        this.usuarioId = usuarioId;
        this.emFerias = emFerias;
        this.feriasDataInicio = feriasDataInicio;
        this.feriasDataFim = feriasDataFim;
        this.dataAdmissao = dataAdmissao;
        this.dataNascimento = dataNascimento;
        this.salario = salario;
        this.carteiraTrabalho = carteiraTrabalho;
        this.comissao = comissao;
        this.estabelecimentoId = estabelecimentoId;
        this.cpf = cpf;
        this.telefone1 = telefone1;
        this.telefone2 = telefone2;
        this.endereco = endereco;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Integer getCargoId() { return cargoId; }
    public void setCargoId(Integer cargoId) { this.cargoId = cargoId; }

    public String getCargoNome() { return cargoNome; }
    public void setCargoNome(String cargoNome) { this.cargoNome = cargoNome; }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public Boolean getEmFerias() { return emFerias; }
    public void setEmFerias(Boolean emFerias) { this.emFerias = emFerias; }

    public LocalDate getFeriasDataInicio() { return feriasDataInicio; }
    public void setFeriasDataInicio(LocalDate feriasDataInicio) { this.feriasDataInicio = feriasDataInicio; }

    public LocalDate getFeriasDataFim() { return feriasDataFim; }
    public void setFeriasDataFim(LocalDate feriasDataFim) { this.feriasDataFim = feriasDataFim; }

    public LocalDate getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(LocalDate dataAdmissao) { this.dataAdmissao = dataAdmissao; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public Double getSalario() { return salario; }
    public void setSalario(Double salario) { this.salario = salario; }

    public String getCarteiraTrabalho() { return carteiraTrabalho; }
    public void setCarteiraTrabalho(String carteiraTrabalho) { this.carteiraTrabalho = carteiraTrabalho; }

    public Integer getComissao() { return comissao; }
    public void setComissao(Integer comissao) { this.comissao = comissao; }

    public Integer getEstabelecimentoId() {
        return estabelecimentoId;
    }

    public void setEstabelecimentoId(Integer estabelecimentoId) {
        this.estabelecimentoId = estabelecimentoId;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone1() {
        return telefone1;
    }

    public void setTelefone1(String telefone1) {
        this.telefone1 = telefone1;
    }

    public String getTelefone2() {
        return telefone2;
    }

    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
    }

    public EnderecoResponseDTO getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoResponseDTO endereco) {
        this.endereco = endereco;
    }
}
