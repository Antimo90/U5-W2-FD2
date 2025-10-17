package antimomandorino.u5w2fd2.services;

import antimomandorino.u5w2fd2.entities.Dipendente;
import antimomandorino.u5w2fd2.exceptions.BadRequestException;
import antimomandorino.u5w2fd2.exceptions.NotFoundException;
import antimomandorino.u5w2fd2.payloads.DipendenteDTO;
import antimomandorino.u5w2fd2.repositories.DipendenteRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class DipendenteService {

    private static final long MAX_SIZE = 5 * 1024 * 1024;

    private static final Set<String> ALLOWED_TYPES = Set.of( //GRAZIE INTERNET
            "image/jpg",
            "image/png",
            "image/jpeg"
    );


    @Autowired
    private DipendenteRepository dipendenteRepository;

    @Autowired
    private Cloudinary imageUploader;

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
    public Dipendente uploadImmagine(UUID dipendenteId, MultipartFile file) {
        Dipendente found = this.findById(dipendenteId);

        if (file.isEmpty()) throw new BadRequestException("File vuoto!");
        if (file.getSize() > MAX_SIZE) throw new BadRequestException("File troppo grande!");
        if (!ALLOWED_TYPES.contains(file.getContentType()))
            throw new BadRequestException("I formati permessi sono png e jpeg!");

        try {
            Map result = imageUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) result.get("url");
            found.setImmagineProfilo(imageUrl);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        this.dipendenteRepository.save(found);
        return this.dipendenteRepository.save(found);
    }
}
