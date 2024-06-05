package tesis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tesis.entities.AnuncioEntity;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnuncioJpaRepository extends JpaRepository<AnuncioEntity, Long> {
    List<AnuncioEntity> findAllByProfesionista_Id(Long idProfesionista);
    Optional<AnuncioEntity> findByFechaAndProfesionista_Id(YearMonth fecha, Long idProfesionista);
    AnuncioEntity findByProfesionista_Id(Long id);
}
