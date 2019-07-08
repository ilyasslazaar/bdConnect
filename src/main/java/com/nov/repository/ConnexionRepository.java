package com.nov.repository;

import com.nov.domain.Connexion;
import com.nov.domain.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Connexion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConnexionRepository extends JpaRepository<Connexion, Long> {

    @Query("select connexion from Connexion connexion where connexion.conxUser.login = ?#{principal.username}")
    List<Connexion> findByConxUserIsCurrentUser();



}
