package antimomandorino.u5w2fd2.services;

import antimomandorino.u5w2fd2.entities.Dipendente;
import antimomandorino.u5w2fd2.entities.Prenotazione;
import antimomandorino.u5w2fd2.entities.Viaggio;
import antimomandorino.u5w2fd2.exceptions.BadRequestException;
import antimomandorino.u5w2fd2.exceptions.NotFoundException;
import antimomandorino.u5w2fd2.payloads.PrenotazioneDTO;
import antimomandorino.u5w2fd2.repositories.DipendenteRepository;
import antimomandorino.u5w2fd2.repositories.PrenotazioneRepository;
import antimomandorino.u5w2fd2.repositories.ViaggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private ViaggioRepository viaggioRepository;

    @Autowired
    private DipendenteRepository dipendenteRepository;

    public Prenotazione savePrenotazione(PrenotazioneDTO payload) {

        Dipendente dipendente = dipendenteRepository.findById(payload.dipendenteId())
                .orElseThrow(() -> new NotFoundException(payload.dipendenteId()));

        Viaggio viaggio = viaggioRepository.findById(payload.viaggioId())
                .orElseThrow(() -> new NotFoundException(payload.viaggioId()));

        LocalDate dataViaggio = viaggio.getData();

        if (prenotazioneRepository.existsByDipendente_DipendenteIdAndViaggio_Data(dipendente.getDipendenteId(), dataViaggio)) {
            throw new BadRequestException("Il dipendente con ID " + dipendente.getDipendenteId() +
                    " ha già una prenotazione per un viaggio in data " + dataViaggio + ". " +
                    "Non può avere più prenotazioni per lo stesso giorno.");
        }

        Prenotazione nuovaPrenotazione = new Prenotazione(
                LocalDate.now(),
                payload.notePreferenze(),
                viaggio,
                dipendente
        );
        return this.prenotazioneRepository.save(nuovaPrenotazione);
    }

    public Page<Prenotazione> findAll(Integer pageNumber, Integer pageSize, String sortBy) {
        if (pageSize > 20) pageSize = 20;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.prenotazioneRepository.findAll(pageable);
    }

    public Prenotazione findById(UUID id) {
        return this.prenotazioneRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Prenotazione findByIdAndUpdate(UUID prenotazioneId, PrenotazioneDTO payload) {

        Prenotazione found = this.findById(prenotazioneId);

        Dipendente nuovoDipendente = dipendenteRepository.findById(payload.dipendenteId())
                .orElseThrow(() -> new NotFoundException(payload.dipendenteId()));

        Viaggio nuovoViaggio = viaggioRepository.findById(payload.viaggioId())
                .orElseThrow(() -> new NotFoundException(payload.viaggioId()));

        LocalDate nuovaDataViaggio = nuovoViaggio.getData();


        if (prenotazioneRepository.existsByDipendente_DipendenteIdAndViaggio_Data(nuovoDipendente.getDipendenteId(), nuovaDataViaggio)) {

            if (!found.getViaggio().getViaggioId().equals(nuovoViaggio.getViaggioId()) ||
                    !found.getDipendente().getDipendenteId().equals(nuovoDipendente.getDipendenteId())) {

                throw new BadRequestException("Il dipendente con ID " + nuovoDipendente.getDipendenteId() +
                        " ha già un'altra prenotazione per un viaggio in data " + nuovaDataViaggio + ".");
            }
        }

        found.setViaggio(nuovoViaggio);
        found.setDipendente(nuovoDipendente);
        found.setNotePreferenze(payload.notePreferenze());

        return prenotazioneRepository.save(found);
    }


    public void findByIdAndDelete(UUID id) {
        Prenotazione found = this.findById(id);
        this.prenotazioneRepository.delete(found);
    }
}
