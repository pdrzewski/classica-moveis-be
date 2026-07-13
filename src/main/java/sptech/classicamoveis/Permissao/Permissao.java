package sptech.classicamoveis.Permissao;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "permissao", length = 45)
    private String permissao;
}
