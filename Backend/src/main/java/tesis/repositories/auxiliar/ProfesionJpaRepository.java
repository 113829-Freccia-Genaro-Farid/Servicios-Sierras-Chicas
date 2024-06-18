package tesis.repositories.auxiliar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tesis.entities.auxiliar.ProfesionEntity;

import java.util.List;

@Repository
public interface ProfesionJpaRepository extends JpaRepository<ProfesionEntity, Long> {
    void deleteById(Long id);
    boolean existsByDescripcion(String descripcion);
    boolean existsById(Long id);
    List<ProfesionEntity> getAllByCategoria_IdOrderByDescripcion(Long id);
    List<ProfesionEntity> findAllByOrderByDescripcion();
}
