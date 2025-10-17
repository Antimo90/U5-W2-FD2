package antimomandorino.u5w2fd2.payloads;

import java.time.LocalDateTime;

public record ErrorsDTO(
        String message,
        LocalDateTime timestamp
) {


}
