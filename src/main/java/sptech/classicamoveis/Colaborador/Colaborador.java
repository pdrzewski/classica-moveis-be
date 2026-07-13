package sptech.classicamoveis.Colaborador;


import jakarta.persistence.*;
import lombok.*;
import sptech.classicamoveis.Cargo.Cargo;
import sptech.classicamoveis.Usuario.Usuario;

import java.time.LocalDate;

@Entity
@Table(name = "colaborador")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 45)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "cargo_id", nullable = false)
    private Cargo cargo;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "em_ferias")
    private Boolean emFerias;

    @Column(name = "data_admissao")
    private LocalDate dataAdmissao;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    private Double salario;

    @Column(name = "cartera_trabalho", length = 55)
    private String carteiraTrabalho;

    private Integer comissao;

}
