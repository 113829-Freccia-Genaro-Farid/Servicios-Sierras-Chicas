package tesis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tesis.entities.AnuncioEntity;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnuncioJpaRepository extends JpaRepository<AnuncioEntity, Long> {
    @Query("SELECT a FROM AnuncioEntity a WHERE a.profesionista.id = :idProfesionista ORDER BY a.anio, a.mes")
    List<AnuncioEntity> findAllByProfesionista_IdOrderByMesAndAnio(Long idProfesionista);
    Optional<AnuncioEntity> findByAnioAndMesAndProfesionista_Id(int anio, int mes, Long idProfesionista);
    AnuncioEntity findByProfesionista_Id(Long id);
}
