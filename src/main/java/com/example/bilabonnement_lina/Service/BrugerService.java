package com.example.bilabonnement_lina.Service;

import com.example.bilabonnement_lina.Model.Bruger;
import com.example.bilabonnement_lina.Repository.BrugerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BrugerService {


    @Autowired
    BrugerRepo brugerRepo;


    public List<Bruger> getAllUsers(){
        return brugerRepo.getAllUsers();
    }
}
