package com.example.bilabonnement_lina.Service;

import com.example.bilabonnement_lina.Model.Kunde;
import com.example.bilabonnement_lina.Model.LejeKontrakt;
import com.example.bilabonnement_lina.Model.MonthlyIncome;
import com.example.bilabonnement_lina.Repository.LejeKontraktRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Service
public class LejeKontraktService {

    @Autowired
    LejeKontraktRepo lejeKontraktRepo;

    public List<LejeKontrakt> getAllLejeKontrakt() throws SQLException {
        return lejeKontraktRepo.getAllLejeKontrakt();
    }

    // Find lejekontrakt baseret på telefonnummer og nummerplade
    public LejeKontrakt getKontraktByTelefon(String telefonnummer) throws SQLException {
        return lejeKontraktRepo.getKontraktByTelefon(telefonnummer);
    }

    public boolean opdaterLejeKontrakt(LejeKontrakt lejeKontrakt) {
        return lejeKontraktRepo.opdaterLejeKontrakt(lejeKontrakt);
    }

    public void sletLejeKontract(int lejekontrakt_id) throws SQLException {
        lejeKontraktRepo.sletLejeKontract(lejekontrakt_id);
    }

    public LejeKontrakt getLejeKontraktById(int lejekontrakt_id) throws SQLException {
        return lejeKontraktRepo.getKontraktById(lejekontrakt_id);
    }

    public List<String> getBilMaerker() {
        return lejeKontraktRepo.getBilMaerker();
    }


    public void addLejekontrakt(LejeKontrakt lejeKontrakt){
        lejeKontraktRepo.addLejekontrakt(lejeKontrakt);
    }

    public List<LejeKontrakt> findKontraktByTelefon(String telenonnumer) throws SQLException {
        return lejeKontraktRepo.findKontraktByTelefon(telenonnumer);
    }


    public int getAntalBiler (LocalDate startdato, LocalDate slutdato) throws SQLException {
        return lejeKontraktRepo.getAntalBiler(startdato, slutdato);
    }


    public List<MonthlyIncome> monthlyIncomeList(int year){
        return lejeKontraktRepo.monthlyIncomeList(year);

    }

    public void redigerLejeKontrakt(LejeKontrakt lejeKontrakt) {
        lejeKontraktRepo.redigerLejeKontrakt(lejeKontrakt);
    }
    // se antal lejet biler ude for maerke over mvp
    public int getAntalBilerMaerke(LocalDate startdato, LocalDate slutdato, String selectedMaerke) throws SQLException {
        return lejeKontraktRepo.getAntalBilerforMærke(startdato, slutdato, selectedMaerke);
    }




}

