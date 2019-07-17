package com.nov.service;

import com.nov.domain.Connexion;
import com.nov.domain.Query;
import com.nov.domain.User;
import com.nov.repository.ConnexionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ConnexionService {

    @Autowired
    ConnexionRepository repository;

    @Autowired
    UserService userService;

    private final Logger log = LoggerFactory.getLogger(ConnexionService.class);

    public Page<Connexion> getConnexionsByUserId(Integer currentPage, Integer pageSize, String search, String orderby){
        log.debug("REST request to get all Connexions Of a User");
        Pageable pageable = PageRequest.of(currentPage-1, pageSize, Sort.by(orderby));

        return repository.findByConxUserIsCurrentUser(search,pageable);
    }
    public Connexion saveConnexion(Connexion connexion){
        User user = userService.getUserWithAuthorities().get();
        connexion.setConxUser(user);
        return repository.save(connexion);

    }

    public Connexion  getConnexionById(Long id){
        return repository.findById(id).get();
    }

    public Query getConnextionQueryById(Long stm_id){
        return repository.getConnexionQueryById(stm_id);
    }

}
