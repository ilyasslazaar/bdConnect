package com.nov.service;


import com.nov.repository.QueryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class QueryService {

    @Autowired
    private  QueryRepository queryRepository;
    private final Logger log = LoggerFactory.getLogger(QueryService.class);

    public void deleteListOfConnctions(List<Long> ids){
        queryRepository.deleteListQueries(ids);
    }




}
