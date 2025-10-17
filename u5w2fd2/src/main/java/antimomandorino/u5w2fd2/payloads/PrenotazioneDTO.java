package antimomandorino.u5w2fd2.payloads;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PrenotazioneDTO(
        @NotNull(message = "ID del Viaggio è obbligatorio")
        UUID viaggioId,
        @NotNull(message = "ID del dipendente è obbligatorio")
        UUID dipendenteId,
        //  il campo può essere null (ho deciso di lasciarlo opzionale)
        String notePreferenze
) {
}
