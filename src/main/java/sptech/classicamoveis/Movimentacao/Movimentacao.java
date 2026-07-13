package sptech.classicamoveis.Movimentacao;

import jakarta.persistence.*;
import lombok.*;
import sptech.classicamoveis.Colaborador.Colaborador;
import sptech.classicamoveis.Movimentacao.TipoMovimentacao.TipoMovimentacao;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimentacao", nullable = false)
    private TipoMovimentacao tipoMovimentacao;

    @Column(length = 100)
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "colaborador_id", nullable = false)
    private Colaborador colaborador;
}
