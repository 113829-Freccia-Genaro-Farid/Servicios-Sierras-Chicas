package tesis.repositories.auxiliar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tesis.entities.auxiliar.RecuperacionEntity;

import java.util.Optional;

@Repository
public interface RecuperacionJpaRepository extends JpaRepository<RecuperacionEntity, String> {
    Optional<RecuperacionEntity> findFirstByEmailOrderByFechaSolicitudDesc(String email);
}
