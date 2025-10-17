package antimomandorino.u5w2fd2.payloads;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorsWithListDTO(
        String message,
        LocalDateTime timestamp,
        List<String> errorsList
) {
}
