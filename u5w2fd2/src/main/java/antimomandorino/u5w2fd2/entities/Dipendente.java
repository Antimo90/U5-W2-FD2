package antimomandorino.u5w2fd2.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "dipendenti")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Dipendente {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "dipendente_id")
    private UUID dipendenteId;

    @Column(unique = true)
    private String username;
    private String nome;
    private String cognome;
    private String email;
    @Column(name = "immagine_profilo")
    private String immagineProfilo;

    public Dipendente(String username, String nome, String cognome, String email) {
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        String nomecompleto = this.nome + "+" + this.cognome;
        this.immagineProfilo = "https://ui-avatars.com/api/?name=" + nomecompleto;
    }
}
