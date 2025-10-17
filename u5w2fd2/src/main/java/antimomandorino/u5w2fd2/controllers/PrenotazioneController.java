package antimomandorino.u5w2fd2.controllers;

import antimomandorino.u5w2fd2.entities.Prenotazione;
import antimomandorino.u5w2fd2.exceptions.ValidationException;
import antimomandorino.u5w2fd2.payloads.PrenotazioneDTO;
import antimomandorino.u5w2fd2.services.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;


    @GetMapping
    public Page<Prenotazione> findAllPrenotazioni(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "prenotazioneId") String sortBy) {
        return this.prenotazioneService.findAll(page, size, sortBy);
    }


    @GetMapping("/{prenotazioneId}")
    public Prenotazione findPrenotazioneById(@PathVariable UUID prenotazioneId) {
        return this.prenotazioneService.findById(prenotazioneId);
    }

    @PutMapping("/{prenotazioneId}")
    public Prenotazione findByIdAndUpdate(@PathVariable UUID prenotazioneId, @RequestBody @Validated PrenotazioneDTO prenotazionePayload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return this.prenotazioneService.findByIdAndUpdate(prenotazioneId, prenotazionePayload);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prenotazione createPrenotazione(@RequestBody @Validated PrenotazioneDTO newPrenotazionePayload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }

        return this.prenotazioneService.savePrenotazione(newPrenotazionePayload);
    }

    @DeleteMapping("/{prenotazioneId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID prenotazioneId) {
        this.prenotazioneService.findByIdAndDelete(prenotazioneId);
    }
}
