package tesis.repositories.auxiliar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tesis.entities.auxiliar.CiudadEntity;

import java.util.List;

@Repository
public interface CiudadJpaRepository extends JpaRepository<CiudadEntity, Long> {
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByDescripcionOrCodigoPostal(String descripcion, String codigoPostal);
    List<CiudadEntity> getAllByProvincia_Id(Long id);
    CiudadEntity getByCodigoPostal(String codigoPostal);
}
