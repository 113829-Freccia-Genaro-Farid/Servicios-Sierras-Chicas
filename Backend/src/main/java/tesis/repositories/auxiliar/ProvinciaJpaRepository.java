package tesis.repositories.auxiliar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tesis.entities.auxiliar.ProvinciaEntity;

@Repository
public interface ProvinciaJpaRepository extends JpaRepository<ProvinciaEntity, Long> {
    void deleteById(Long id);
    boolean existsByDescripcion(String descripcion);
    boolean existsById(Long id);
}
