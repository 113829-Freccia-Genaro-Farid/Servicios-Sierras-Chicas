package tesis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tesis.entities.ProfesionistaEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfesionistaJpaRepository extends JpaRepository<ProfesionistaEntity, Long> {
    boolean existsByPersona_Id(Long id);
    ProfesionistaEntity getByPersona_Id(Long id);
    Optional<ProfesionistaEntity> findByPersona_Usuario_Email(String email);
    @Modifying
    @Query(value = "DELETE FROM profesionista_profesiones WHERE id_profesionista = :idProfesionista", nativeQuery = true)
    void borrarRelacionesPorProfesionista(@Param("idProfesionista") Long idProfesionista);
}
