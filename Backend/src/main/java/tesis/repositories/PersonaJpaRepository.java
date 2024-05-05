package tesis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tesis.entities.PersonaEntity;

import java.util.List;

@Repository
public interface PersonaJpaRepository extends JpaRepository<PersonaEntity,Long> {
    List<PersonaEntity> findByApellidoStartingWith(String apellido);
    boolean existsByNroDocumentoAndTipoDNI_Id(String nroDoc, Long idTipoDNI);
    boolean existsByTelefono1(String numero);
    boolean existsByUsuario_Email(String email);

}
