package com.nov.repository;

import com.nov.domain.Connexion;
import com.nov.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Connexion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConnexionRepository extends PagingAndSortingRepository<Connexion, Long> {

    @Query("select connexion from Connexion connexion where connexion.name like %?1% and connexion.conxUser.login = ?#{principal.username}")
    Page<Connexion> findByConxUserIsCurrentUser(String search, Pageable pageable);


    @Query(value ="SELECT q FROM  Connexion  c join c.queries q where c.id = q.connexion and q.id = ?1 and c.conxUser.login = ?#{principal.username} ")
    com.nov.domain.Query getConnexionQueryById(Long query_id);



}
