package sptech.classicamoveis.Estabelecimento;


import jakarta.persistence.*;
import lombok.*;
import sptech.classicamoveis.Colaborador.Colaborador;
import sptech.classicamoveis.Endereco.Endereco;

@Entity
@Table(name = "estabelecimento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 45)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "fk_endereco", nullable = false)
    private Endereco endereco;

    @Column(length = 14, nullable = false, unique = true)
    private String cnpj;

    @Column(length = 20)
    private String telefone;

    @ManyToOne
    @JoinColumn(name = "fk_responsavel", nullable = false)
    private Colaborador responsavel;
}

