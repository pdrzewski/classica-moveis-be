package sptech.classicamoveis.Fornecedor.model;

import jakarta.persistence.*;
import lombok.*;
import sptech.classicamoveis.Endereco.Endereco;
import sptech.classicamoveis.Representante.model.Representante;

import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "telefone1", length = 20)
    private String telefone1;

    @Column(name = "telefone2", length = 20)
    private String telefone2;

    @Column(name = "whatsapp", length = 20)
    private String whatsapp;

    @Column(name = "email", length = 100)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "endereco_id", nullable = false)
    private Endereco endereco;

    @ManyToMany(mappedBy = "fornecedores")
    @Builder.Default
    private Set<Representante> representantes = new HashSet<>();
}
