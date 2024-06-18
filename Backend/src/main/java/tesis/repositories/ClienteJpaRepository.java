package tesis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tesis.entities.ClienteEntity;

import java.util.Optional;

@Repository
public interface ClienteJpaRepository extends JpaRepository<ClienteEntity,Long> {
    boolean existsByPersona_Id(Long id);
    ClienteEntity getByPersona_Id(Long id);
    Optional<ClienteEntity> findByPersona_Usuario_Email(String email);
}
