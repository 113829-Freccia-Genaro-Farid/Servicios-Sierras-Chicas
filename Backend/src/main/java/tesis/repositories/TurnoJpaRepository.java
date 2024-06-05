package tesis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tesis.entities.TurnoEntity;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TurnoJpaRepository extends JpaRepository<TurnoEntity, Long> {
    List<TurnoEntity> findAllByCliente_Id(Long id);
    List<TurnoEntity> findAllByProfesionista_Id(Long id);
    List<TurnoEntity> findAllByFechaTurno(LocalDate fecha);
    boolean existsByCliente_IdAndProfesionista_Id(Long idCli, Long idProf);
}
