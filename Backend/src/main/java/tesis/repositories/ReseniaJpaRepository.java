package tesis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tesis.entities.ReseniaEntity;

import java.util.List;

@Repository
public interface ReseniaJpaRepository extends JpaRepository<ReseniaEntity, Long> {
    List<ReseniaEntity> findAllByCliente_Id(Long id);
    List<ReseniaEntity> findAllByProfesionista_IdOrderByFechaResenia(Long id);
    boolean existsByCliente_IdAndProfesionista_Id(Long idCli, Long idProf);
}

