package tesis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tesis.entities.UsuarioEntity;

import java.util.List;

@Repository
public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, String> {
    boolean existsByEmail(String email);
    UsuarioEntity getByEmail(String email);
    boolean existsByEmailAndPassword(String email,String password);
    List<UsuarioEntity> findByEmailStartingWith(String email);
    List<UsuarioEntity> findByRol_Id(Long id);
}