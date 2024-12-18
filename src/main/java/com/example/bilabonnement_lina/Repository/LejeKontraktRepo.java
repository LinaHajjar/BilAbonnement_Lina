package com.example.bilabonnement_lina.Repository;

import com.example.bilabonnement_lina.Model.Kunde;
import com.example.bilabonnement_lina.Model.LejeKontrakt;
import com.example.bilabonnement_lina.Model.MonthlyIncome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class LejeKontraktRepo {

    @Autowired
    JdbcTemplate template;

    public List<LejeKontrakt> getAllLejeKontrakt() throws SQLException {
        String sql = "select * from lejekontrakt";
        RowMapper<LejeKontrakt> rowMapper=new BeanPropertyRowMapper<>(LejeKontrakt.class);
        return template.query(sql, rowMapper);
    }

    public LejeKontrakt getKontraktById(int lejekontrakt_id) throws SQLException {
        String sql = "select * from lejekontrakt where lejekontrakt_id = ?";
        RowMapper<LejeKontrakt> rowMapper = new BeanPropertyRowMapper<>(LejeKontrakt.class);
        LejeKontrakt lejeKontrakt = template.queryForObject(sql, rowMapper, lejekontrakt_id);
        return lejeKontrakt;
    }

    public LejeKontrakt getKontraktByTelefon(String telefonnumer) throws SQLException {
        String sql = "select * from lejekontrakt where telefonnumer = ?";
        RowMapper<LejeKontrakt> rowMapper = new BeanPropertyRowMapper<>(LejeKontrakt.class);
        LejeKontrakt lejeKontrakt = template.queryForObject(sql, rowMapper, telefonnumer);
        return lejeKontrakt;
    }

    public void sletLejeKontract(int lejekontrakt_id) throws SQLException {
        String delete1 = "DELETE FROM skader WHERE lejekontrakt_id = ?";
        String delete2 = "DELETE FROM BilHaandtering WHERE lejekontrakt_id = ?";
        String delete3 = "DELETE FROM lejekontrakt WHERE lejekontrakt_id = ?";
        template.update(delete1, lejekontrakt_id);
        template.update(delete2, lejekontrakt_id);
        template.update(delete3, lejekontrakt_id);
    }

    public boolean opdaterLejeKontrakt(LejeKontrakt lejeKontrakt) {
        String sql = "UPDATE lejekontrakt " +
                "SET slutdato = ?, maxKm = ?, pris = ? " +
                "WHERE telefonnummer = ? AND nummerplade = ?";
        int rowsUpdated = template.update(sql,
                lejeKontrakt.getSlutdato(),
                lejeKontrakt.getMaxKm(),
                lejeKontrakt.getPris(),
                lejeKontrakt.getTelefonnummer(),
                lejeKontrakt.getNummerplade());

        // Return true if at least one row was updated, otherwise return false
        return rowsUpdated > 0;
    }



    public void addLejekontrakt(LejeKontrakt lejeKontrakt){
        String sql = "INSERT INTO lejekontrakt(telefonnummer, nummerplade, startDato, slutDato, maxKm, pris)\n" +
                "VALUES(?, ?, ?, ?, ?, ?)";
        template.update(sql, lejeKontrakt.getTelefonnummer(), lejeKontrakt.getNummerplade(), lejeKontrakt.getStartdato(), lejeKontrakt.getSlutdato(), lejeKontrakt.getMaxKm(), lejeKontrakt.getPris()); // denne kode adder til databasen ved hjælp af getters
    }


    //  query der tager en kundes telefonnummer og finde alle kundens lejekontrakter: søge efter kunden.
    public List<LejeKontrakt> findKontraktByTelefon(String telefonnummer) throws SQLException {
        String sql = "SELECT * FROM lejekontrakt WHERE telefonnummer = ?";
        RowMapper<LejeKontrakt> rowMapper = new BeanPropertyRowMapper<LejeKontrakt>(LejeKontrakt.class);
        return template.query(sql, rowMapper, telefonnummer);
    }


    public List<LejeKontrakt> getLejeKontraktById(int lejekontrakt_id) throws SQLException {
        String sql = "SELECT * FROM lejekontrakt WHERE lejekontrakt_id = ?";
        RowMapper<LejeKontrakt> rowMapper = new BeanPropertyRowMapper<LejeKontrakt>(LejeKontrakt.class);
        return template.query(sql, rowMapper, lejekontrakt_id);
    }

//metode til mvp
/*public int getAntalBiler(LocalDate startdato, LocalDate slutdato) throws SQLException {
    String sql = "SELECT COUNT(*) FROM lejekontrakt WHERE startdato >= ? AND slutdato <= ?";

    // Convert LocalDate to java.sql.Date
    java.sql.Date sqlStartdato = java.sql.Date.valueOf(startdato);
    java.sql.Date sqlSlutdato = java.sql.Date.valueOf(slutdato);

    // Execute the query and return the count
    return template.queryForObject(sql, Integer.class, sqlStartdato, sqlSlutdato);
}*/



    //public List<String> getBilMaerker() {

    public int getAntalBiler(LocalDate startdato, LocalDate slutdato) throws SQLException {
        String sql = "SELECT COUNT(*) FROM lejekontrakt WHERE startdato >= ? AND slutdato <= ?";

        // Convert LocalDate to java.sql.Date
        java.sql.Date sqlStartdato = java.sql.Date.valueOf(startdato);
        java.sql.Date sqlSlutdato = java.sql.Date.valueOf(slutdato);

        // Execute the query and return the count
        return template.queryForObject(sql, Integer.class, sqlStartdato, sqlSlutdato);
    }




    public List<String> getBilMaerker() {
        String sql = "SELECT DISTINCT maerke FROM bil WHERE maerke IS NOT NULL AND maerke != ''";
        return template.queryForList(sql, String.class);
    }

    public void redigerLejeKontrakt(LejeKontrakt lejeKontrakt){
        String sql = "UPDATE lejeKontrakt SET startdato=?, slutdato=?, maxKm=?, pris=? WHERE lejekontrakt_id= ?";
        template.update(sql, lejeKontrakt.getStartdato(), lejeKontrakt.getSlutdato(), lejeKontrakt.getMaxKm(), lejeKontrakt.getPris(), lejeKontrakt.getLejekontrakt_id());
    }


    public int getAntalBilerforMærke(LocalDate startdato, LocalDate slutdato, String selectedMaerke) {


        java.sql.Date sqlStartdato = java.sql.Date.valueOf(startdato);
        java.sql.Date sqlSlutdato = java.sql.Date.valueOf(slutdato);

        String sql = "SELECT COUNT(*) FROM lejekontrakt l " +
                "JOIN bil b ON l.nummerplade = b.nummerplade " +
                "WHERE l.startdato >= ? " +
                "AND l.slutdato <= ? " +
                "AND b.maerke = ?";


        return template.queryForObject(sql, new Object[]{sqlStartdato, sqlSlutdato, selectedMaerke}, Integer.class);
    }




    public List<MonthlyIncome> monthlyIncomeList(int year){
        String sql = "SELECT \n" +
                "    MONTH(slutdato) AS måned, \n" +
                "    SUM(pris) AS indtjening\n" +
                "FROM lejekontrakt\n" +
                "WHERE YEAR(slutdato) = ?\n" +
                "GROUP BY MONTH(slutdato)\n" +
                "ORDER BY MONTH(slutdato);\n";
        RowMapper<MonthlyIncome> rowMapper = new BeanPropertyRowMapper<MonthlyIncome>(MonthlyIncome.class);
        return template.query(sql, rowMapper, year);
    }

}
