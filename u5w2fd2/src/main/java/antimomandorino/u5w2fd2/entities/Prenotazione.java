package antimomandorino.u5w2fd2.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "prenotazioni")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Prenotazione {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "prenotazione_id")
    private UUID prenotazioneId;
    @Column(name = "data_richiesta")
    private LocalDate dataRichiesta;
    @Column(name = "note_preferenze")
    private String notePreferenze;

    @ManyToOne
    @JoinColumn(name = "viaggio_id", nullable = false)
    private Viaggio viaggio;

    @ManyToOne
    @JoinColumn(name = "dipendente_id", nullable = false)
    private Dipendente dipendente;

    public Prenotazione(LocalDate dataRichiesta, String notePreferenze, Viaggio viaggio, Dipendente dipendente) {
        this.dataRichiesta = dataRichiesta;
        this.notePreferenze = notePreferenze;
        this.viaggio = viaggio;
        this.dipendente = dipendente;
    }
}
