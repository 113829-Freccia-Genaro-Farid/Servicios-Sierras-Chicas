package tesis.repositories.auxiliar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tesis.entities.auxiliar.RolEntity;

@Repository
public interface RolJpaRepository extends JpaRepository<RolEntity, Long> {
    void deleteById(Long id);
    boolean existsByDescripcion(String descripcion);
    boolean existsById(Long id);
    RolEntity getByDescripcion(String descripcion);
}
