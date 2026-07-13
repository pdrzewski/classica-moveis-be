package sptech.classicamoveis.Cliente;

import jakarta.persistence.*;
import lombok.*;
import sptech.classicamoveis.Endereco.Endereco;

@Entity
@Table(name = "cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 45)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "fk_endereco", nullable = false)
    private Endereco endereco;

    @Column(length = 18, unique = true)
    private String documento;

    @Column(length = 20)
    private String telefone1;

    @Column(length = 20)
    private String telefone2;

    @Column(length = 45)
    private String email;

    @Column(length = 45)
    private String observacao;

    @Column(length = 45)
    private String clientecol;

    @Column(length = 45)
    private String ie;
}
