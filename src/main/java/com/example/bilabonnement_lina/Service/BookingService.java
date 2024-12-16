package com.example.bilabonnement_lina.Service;


import com.example.bilabonnement_lina.Model.Bil;
import com.example.bilabonnement_lina.Model.LejeKontrakt;
import com.example.bilabonnement_lina.Repository.BookingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {


    @Autowired
    BookingRepo bookingrepo;


    //skal måske slettes?
    public List<Bil> getTilgaengeliBiler(Date startdato, Date slutdato) {
        return bookingrepo.getTilgaengeligBiler(startdato, slutdato);
    }


    public List<LejeKontrakt> seBiler(LocalDate startdato, LocalDate slutdato) {
        return bookingrepo.seBiler(startdato, slutdato);
    }






}
