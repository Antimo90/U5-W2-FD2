package antimomandorino.u5w2fd2.repositories;

import antimomandorino.u5w2fd2.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, UUID> {

    boolean existsByDipendente_DipendenteIdAndViaggio_Data(UUID dipendenteId, LocalDate dataViaggio);


}
