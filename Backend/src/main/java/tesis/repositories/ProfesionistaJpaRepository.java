package tesis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tesis.entities.ProfesionistaEntity;

import java.util.List;

@Repository
public interface ProfesionistaJpaRepository extends JpaRepository<ProfesionistaEntity, Long> {
    boolean existsByPersona_Id(Long id);
    ProfesionistaEntity getByPersona_Id(Long id);
}
