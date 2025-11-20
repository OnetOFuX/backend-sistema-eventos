package pe.edu.upeu.eventos.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "eventos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class EventoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoEventoEnum tipo;

    @Column(nullable = false, length = 200)
    private String ubicacion;

    @Column(name = "cupo_maximo")
    private Integer cupoMaximo;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "es_pago", nullable = false)
    private Boolean esPago = false;

    @Column(precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "metodos_pago")
    @ElementCollection
    private List<String> metodosPago; // JSON: ["EFECTIVO", "YAPE", "PLIN", "TRANSFERENCIA", "TARJETA"]

    @Column(name = "requiere_comprobante")
    private Boolean requiereComprobante = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "carrera_id")
    private CarreraEntity carrera; // Si es null, significa que no está restringido por carrera

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "facultad_id")
    private FacultadEntity facultad; // Si es null, significa que no está restringido por facultad

    @Column(name = "para_todas")
    private Boolean paraTodas = false; // TRUE = visible para todos sin restricción

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creador_id", nullable = false)
    private UsuarioEntity creador;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<FechaEventoEntity> fechas = new HashSet<>();

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<InscripcionEntity> inscripciones = new HashSet<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum TipoEventoEnum {
        ACADEMICO,
        CULTURAL,
        DEPORTIVO,
        ADMINISTRATIVO,
        SOCIAL,
        CONFERENCIA,
        TALLER,
        SEMINARIO
    }
}
