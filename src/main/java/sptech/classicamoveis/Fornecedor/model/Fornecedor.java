package sptech.classicamoveis.Fornecedor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sptech.classicamoveis.Endereco.Endereco;

@Entity
@Table(name = "fornecedor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", length = 45)
    private String nome;

    @Column(name = "cnpj", length = 45)
    private String cnpj;

    @Column(name = "representante", length = 45)
    private String representante;

    @Column(name = "telefone1", length = 45)
    private String telefone1;

    @Column(name = "telefone2", length = 45)
    private String telefone2;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "endereco_id", nullable = false)
    private Endereco endereco;
}
