package sptech.classicamoveis.Fornecedor;

import jakarta.persistence.*;
import lombok.*;
import sptech.classicamoveis.Endereco.Endereco;

@Entity
@Table(name = "fornecedor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 45)
    private String nome;

    @Column(length = 14, nullable = false, unique = true)
    private String cnpj;

    @Column(length = 45)
    private String representante;

    @Column(name = "telefone1", length = 20)
    private String telefone1;

    @Column(name = "telefone2", length = 20)
    private String telefone2;

    @ManyToOne
    @JoinColumn(name = "endereco_id", nullable = false)
    private Endereco endereco;
}
