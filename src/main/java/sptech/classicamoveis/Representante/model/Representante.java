package sptech.classicamoveis.Representante.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import sptech.classicamoveis.Fornecedor.model.Fornecedor;

@Entity
@Table(name = "representante")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Representante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 45)
    private String nome;

    @Column(name = "telefone1", length = 20)
    private String telefone1;

    @Column(name = "telefone2", length = 20)
    private String telefone2;

    @ManyToMany
    @JoinTable(
            name = "representante_fornecedor",
            joinColumns = @JoinColumn(name = "representante_id"),
            inverseJoinColumns = @JoinColumn(name = "fornecedor_id")
    )
    @Builder.Default
    private Set<Fornecedor> fornecedores = new HashSet<>();
}
