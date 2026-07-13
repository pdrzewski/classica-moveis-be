package sptech.classicamoveis.Cargo;

import jakarta.persistence.*;
import lombok.*;

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
}
