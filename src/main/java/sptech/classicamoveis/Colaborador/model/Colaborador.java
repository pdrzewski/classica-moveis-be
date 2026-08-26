package sptech.classicamoveis.Colaborador.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import sptech.classicamoveis.Cargo.model.Cargo;
import sptech.classicamoveis.Estabelecimento.Estabelecimento;
import sptech.classicamoveis.Usuario.model.Usuario;

import java.time.LocalDate;

@Entity
@Table(name = "colaborador")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //tem
    private Integer id;

    @Column(length = 45) // tem
    private String nome;

    @ManyToOne
    @JoinColumn(name = "cargo_id", nullable = false) //tem
    private Cargo cargo;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false) //tem
    private Usuario usuario;

    @Column(name = "em_ferias")//tem
    private Boolean emFerias;

    @Column(name = "ferias_data_inicio") //tem
    private LocalDate feriasDataInicio;

    @Column(name = "ferias_data_fim") //tem
    private LocalDate feriasDataFim;

    @Column(name = "data_admissao") //tem
    private LocalDate dataAdmissao;

    @Column(name = "data_nascimento") //tem
    private LocalDate dataNascimento;

    private Double salario; //tem

    @Column(name = "carteira_trabalho", length = 55) //tem
    private String carteiraTrabalho;

    private Integer comissao; // tem


    @Length(max = 11)
    private  String cpf;

    @ManyToOne
    @JoinColumn(name = "estabelecimento_id")
    private Estabelecimento estabelecimento;
}