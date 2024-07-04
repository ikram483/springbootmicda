package ma.micda.journal.services;

import java.util.Collection;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.micda.journal.dto.JournalDTO;
import ma.micda.journal.models.JournalEntity;
import ma.micda.journal.repository.JournalRepository;

@Service

public class JournalService {

    @Autowired
    private final JournalRepository journalRepository;
    @Autowired
    private final ModelMapper modelMapper;

    public JournalService(JournalRepository journalRepository, ModelMapper modelMapper) {
        this.journalRepository = journalRepository;
        this.modelMapper = modelMapper;
    }

    public Collection<JournalEntity> get(Long userId) {
        return journalRepository.findByUserId(userId);

    }

    public JournalEntity add(JournalDTO journalDTO) {
        return this.journalRepository.save(getJournalEntity(journalDTO));
    }

    public JournalEntity update(JournalEntity newJournal) {

        JournalEntity oldEntity = journalRepository.findById(newJournal.getId()).orElse(null);
        if (oldEntity != null) {
            oldEntity.setBody(newJournal.getBody());
            oldEntity.setTitle(newJournal.getTitle());
            return this.journalRepository.save(oldEntity);
        } else {

            return oldEntity;
        }

    }

    public void delete(Long id) {

        this.journalRepository.deleteById(id);
    }

    private JournalEntity getJournalEntity(JournalDTO journalDTO) {
        return modelMapper.map(journalDTO, JournalEntity.class);
    }
}
