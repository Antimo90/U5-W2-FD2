package antimomandorino.u5w2fd2.services;

import antimomandorino.u5w2fd2.entities.Dipendente;
import antimomandorino.u5w2fd2.exceptions.BadRequestException;
import antimomandorino.u5w2fd2.exceptions.NotFoundException;
import antimomandorino.u5w2fd2.payloads.DipendenteDTO;
import antimomandorino.u5w2fd2.repositories.DipendenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DipendenteService {

    @Autowired
    private DipendenteRepository dipendenteRepository;

    public Dipendente saveDipendente(DipendenteDTO payload) {
        dipendenteRepository.findByUsername(payload.username()).ifPresent(dipendente -> {
            throw new BadRequestException("L'username " + payload.username() + " è già in uso!");
        });

        dipendenteRepository.findByEmail(payload.email()).ifPresent(dipendente -> {
            throw new BadRequestException("La email " + payload.email() + " è già in uso!");
        });


        Dipendente nuovoDipendente = new Dipendente(
                payload.username(),
                payload.nome(),
                payload.cognome(),
                payload.email()
        );
        return this.dipendenteRepository.save(nuovoDipendente);
    }

    public Page<Dipendente> findAll(Integer pageNumber, Integer pageSize, String sortBy) {
        if (pageSize > 20) pageSize = 20;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.dipendenteRepository.findAll(pageable);
    }

    public Dipendente findById(UUID dipendenteId) {
        return this.dipendenteRepository.findById(dipendenteId).orElseThrow(() -> new NotFoundException(dipendenteId));
    }

    public Dipendente findByIdAndUpdate(UUID dipendeteId, DipendenteDTO payload) {
        Dipendente found = this.findById(dipendeteId);

        if (!found.getEmail().equals(payload.email())) {

            this.dipendenteRepository.findByEmail(payload.email()).ifPresent(dipendente -> {
                        throw new BadRequestException("L'email " + dipendente.getEmail() + " è già in uso!");
                    }
            );
        }

        if (!found.getUsername().equals(payload.username())) {

            this.dipendenteRepository.findByUsername(payload.username()).ifPresent(dipendente -> {
                        throw new BadRequestException("L'username " + dipendente.getUsername() + " è già in uso!");
                    }
            );
        }
        found.setUsername(payload.username());
        found.setNome(payload.nome());
        found.setCognome(payload.cognome());
        found.setEmail(payload.email());

        return this.dipendenteRepository.save(found);

    }

    public void findByIdAndDelete(UUID id) {
        Dipendente found = this.findById(id);
        this.dipendenteRepository.delete(found);
    }

    //cambio immagine
    public Dipendente uploadImmagine(UUID dipendenteId, String imageUrl) {
        Dipendente found = this.findById(dipendenteId);
        found.setImmagineProfilo(imageUrl); // O gestisci l'upload di MultipartFile qui
        return this.dipendenteRepository.save(found);
    }
}
