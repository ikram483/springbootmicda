package ma.micda.journal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import ma.micda.journal.models.JournalEntity;
import java.util.List;

@Repository
public interface JournalRepository extends JpaRepository<JournalEntity, Long> {

    List<JournalEntity> findByUserId(Long userId);
}
