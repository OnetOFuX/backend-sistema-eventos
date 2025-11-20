package pe.edu.upeu.eventos.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscripciones", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"usuario_id", "evento_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class InscripcionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    private EventoEntity evento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoInscripcionEnum estado;

    @Column(name = "codigo_qr", unique = true)
    private String codigoQr;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", length = 30)
    private MetodoPagoEnum metodoPago;

    @Column(name = "monto_pagado", precision = 10, scale = 2)
    private BigDecimal montoPagado;

    @Column(name = "comprobante_url")
    private String comprobanteUrl;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @Column(name = "verificado_por")
    private Long verificadoPor; // ID del administrador/coordinador que verificó el pago

    @Column(name = "fecha_verificacion")
    private LocalDateTime fechaVerificacion;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum EstadoInscripcionEnum {
        PENDIENTE,
        CONFIRMADA,
        PAGADA,
        CANCELADA,
        RECHAZADA
    }

    public enum MetodoPagoEnum {
        EFECTIVO,
        YAPE,
        PLIN,
        TRANSFERENCIA,
        TARJETA,
        GRATUITO
    }
}
