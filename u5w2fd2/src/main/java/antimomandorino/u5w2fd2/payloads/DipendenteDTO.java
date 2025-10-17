package antimomandorino.u5w2fd2.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record DipendenteDTO(
        @NotBlank(message = "Username è obbligatorio")
        @Size(min = 2, max = 15, message = "L'username deve avere una lunghezza compresa tra 2 e 15 caratteri")
        String username,
        @NotBlank(message = "Il nome è obbligatorio!")
        @Size(min = 2, max = 30, message = "Il nome deve avere una lunghezza compresa tra 2 e 30 caratteri")
        String nome,
        @NotBlank(message = "Il cognome è obbligatorio!")
        @Size(min = 2, max = 30, message = "Il cognome deve avere una lunghezza compresa tra 2 e 30 caratteri")
        String cognome,
        @NotBlank(message = "L'email è obbligatoria!")
        @Email(message = "L'indirizzo email inserito non è nel formato corretto!")
        String email


) {
}
