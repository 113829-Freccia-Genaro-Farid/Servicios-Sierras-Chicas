package tesis.repositories.auxiliar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tesis.entities.auxiliar.CategoriaEntity;

import java.util.List;

@Repository
public interface CategoriaJpaRepository extends JpaRepository<CategoriaEntity, Long>{
    void deleteById(Long id);
    boolean existsByDescripcion(String descripcion);
    boolean existsById(Long id);
    List<CategoriaEntity> findAllByOrderByDescripcion();
}
