package com.fernando.rest.springjavarest.service;

import com.fernando.rest.springjavarest.model.Region;
import com.fernando.rest.springjavarest.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegionServiceImpl implements RegionService{

    @Autowired
    private RegionRepository regionRepository;



    @Override
    public Region findById(Long id) {
        return regionRepository.findById(id).orElse(null);
    }
}
