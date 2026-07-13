package sptech.classicamoveis.Colaborador;

import java.time.LocalDate;

public class ColaboradorResponseDto {

    private Integer id;
    private String nome;
    private Integer cargoId;
    private String cargoNome;
    private Integer usuarioId;
    private Boolean emFerias;
    private LocalDate dataAdmissao;
    private LocalDate dataNascimento;
    private Double salario;
    private String carteiraTrabalho;
    private Integer comissao;

    public ColaboradorResponseDto() {
    }

    public ColaboradorResponseDto(Integer id, String nome, Integer cargoId, String cargoNome, Integer usuarioId, Boolean emFerias, LocalDate dataAdmissao, LocalDate dataNascimento, Double salario, String carteiraTrabalho, Integer comissao) {
        this.id = id;
        this.nome = nome;
        this.cargoId = cargoId;
        this.cargoNome = cargoNome;
        this.usuarioId = usuarioId;
        this.emFerias = emFerias;
        this.dataAdmissao = dataAdmissao;
        this.dataNascimento = dataNascimento;
        this.salario = salario;
        this.carteiraTrabalho = carteiraTrabalho;
        this.comissao = comissao;
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
}