package ma.micda.journal.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import ma.micda.journal.constants.Mappings;
import ma.micda.journal.dto.JournalDTO;
import ma.micda.journal.models.JournalEntity;
import ma.micda.journal.services.JournalService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = Mappings.JOURNAL)
public class JournalController {

    @Autowired
    private final JournalService journalService;
    @Autowired
    private final ModelMapper modelMapper;

    public JournalController(JournalService journalService, ModelMapper modelMapper) {
        this.journalService = journalService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/journal/get/{userId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(this.journalService.get(userId));
    }

    @PostMapping("/journal/add")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> add(@RequestBody JournalDTO journalDTO) {
        return ResponseEntity.ok(getJournalEntity(this.journalService.add(journalDTO)));
    }

    @PutMapping("/journal/update")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<JournalEntity> update(@RequestBody JournalEntity journal) {
        JournalEntity newJournal = this.journalService.update(journal);
        return new ResponseEntity<>(newJournal, HttpStatus.OK);
    }

    @DeleteMapping("/journal/delete/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<JournalEntity> delete(@PathVariable("id") Long id) {
        this.journalService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private JournalDTO getJournalEntity(JournalEntity Journal) {
        return modelMapper.map(Journal, JournalDTO.class);
    }
}
