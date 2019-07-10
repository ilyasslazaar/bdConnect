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


    @Query(value ="SELECT q FROM  Connexion  c join c.queries q where c.id = q.connexion and q.id = ?1 and c.conxUser.login = ?#{principal.username} ")
    com.nov.domain.Query getConnexionQueryById(Long query_id);



}
