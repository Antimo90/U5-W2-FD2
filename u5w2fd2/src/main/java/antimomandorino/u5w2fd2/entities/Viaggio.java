package antimomandorino.u5w2fd2.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "viaggi")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Viaggio {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "viaggio_id")
    private UUID viaggioId;
    private String destinazione;
    private LocalDate data;
    private ViaggioStato stato;

    public Viaggio(String destinazione, LocalDate data, ViaggioStato stato) {
        this.destinazione = destinazione;
        this.data = data;
        this.stato = stato;
    }
}
