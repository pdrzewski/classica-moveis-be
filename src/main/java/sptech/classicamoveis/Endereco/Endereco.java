package sptech.classicamoveis.Endereco;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "endereco")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 8)
    private String cep;

    @Column(length = 100)
    private String logradouro;

    @Column(length = 45)
    private String bairro;

    @Column(length = 45)
    private String cidade;

    @Column(length = 45)
    private String numero;

    @Column(length = 45)
    private String complemento;

    @Column(length = 2)
    private String estado;
}