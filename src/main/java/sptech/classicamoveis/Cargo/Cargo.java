package sptech.classicamoveis.Cargo;

import jakarta.persistence.*;
import lombok.*;
import sptech.classicamoveis.Permissao.Permissao;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cargo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 45)
    private String cargo;

    @ManyToMany
    @JoinTable(
            name = "cargo_has_permissao",
            joinColumns = @JoinColumn(name = "cargo_id"),
            inverseJoinColumns = @JoinColumn(name = "permissao_id")
    )
    private Set<Permissao> permissoes = new HashSet<>();
}
