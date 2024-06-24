package tesis.repositories.auxiliar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tesis.entities.auxiliar.ProfesionEntity;

import java.util.List;

@Repository
public interface ProfesionJpaRepository extends JpaRepository<ProfesionEntity, Long> {
    void deleteById(Long id);
    boolean existsByDescripcion(String descripcion);
    boolean existsById(Long id);
    List<ProfesionEntity> getAllByCategoria_IdOrderByDescripcion(Long id);
    List<ProfesionEntity> findAllByOrderByDescripcion();
    @Query("SELECT p FROM ProfesionEntity p JOIN ProfesionistaEntity pe ON p.id = pe.id WHERE pe.id = :idProfesionista")
    List<ProfesionEntity> findProfesionesByProfesionistaId(@Param("idProfesionista") Long idProfesionista);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM profesionista_profesiones WHERE id_profesionista = :idProfesionista", nativeQuery = true)
    void deleteProfesionesByProfesionistaId(@Param("idProfesionista") Long idProfesionista);
}
