package antimomandorino.u5w2fd2.controllers;

import antimomandorino.u5w2fd2.entities.Dipendente;
import antimomandorino.u5w2fd2.exceptions.ValidationException;
import antimomandorino.u5w2fd2.payloads.DipendenteDTO;
import antimomandorino.u5w2fd2.services.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/dipendenti")
public class DipendenteController {

    @Autowired
    private DipendenteService dipendenteService;

    @GetMapping
    public Page<Dipendente> findAllDipendenti(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "dipendenteId") String sortBy) {
        return this.dipendenteService.findAll(page, size, sortBy);
    }

    @GetMapping("/{dipendenteId}")
    public Dipendente findDipendenteById(@PathVariable UUID dipendenteId) {
        return this.dipendenteService.findById(dipendenteId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Dipendente createDipendente(@RequestBody @Validated DipendenteDTO payload, BindingResult validationResult) {

        if (validationResult.hasErrors()) {

            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return this.dipendenteService.saveDipendente(payload);
    }

    @PutMapping("/{dipendenteId}")
    public Dipendente findByIdAndUpdate(@PathVariable UUID dipendenteId, @RequestBody @Validated DipendenteDTO dipendentePayload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }

        return this.dipendenteService.findByIdAndUpdate(dipendenteId, dipendentePayload);
    }

    @DeleteMapping("/{dipendenteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID dipendenteId) {
        this.dipendenteService.findByIdAndDelete(dipendenteId);
    }


    @PostMapping("/{dipendenteId}/upload")
    public Dipendente uploadImmagine(@PathVariable UUID dipendenteId, @RequestParam("immagine") MultipartFile file) throws IOException {


        return this.dipendenteService.uploadImmagine(dipendenteId, file);
    }


}
