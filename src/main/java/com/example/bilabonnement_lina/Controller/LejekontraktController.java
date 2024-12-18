package com.example.bilabonnement_lina.Controller;

import com.example.bilabonnement_lina.Model.Kunde;
import com.example.bilabonnement_lina.Model.LejeKontrakt;
import com.example.bilabonnement_lina.Service.BilService;
import com.example.bilabonnement_lina.Service.LejeKontraktService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
@Controller
public class LejekontraktController {

    @Autowired
    BilService bilService;
    @Autowired
    LejeKontraktService lejeKontraktService;

    // oprettelse af getmapping metode for at vise alle lejekontrakter
    @GetMapping("/manageKontrakter")
    public String allLejeKontrakter(Model model) throws SQLException {
        model.addAttribute("lejekontrakter", lejeKontraktService.getAllLejeKontrakt());
        return "homeKontrakt/manageKontrakter";
    }

    //oprettelse af postmaping metode for at sende input fra bruger omkring lejekontrakt info
    @GetMapping("/nyKontrakt")
    public String nyKontrakt(Model model) throws SQLException {
        List<String> nummerpladeList = bilService.alleNummerplader();
        model.addAttribute("nummerplader", nummerpladeList);
        return "homeKontrakt/nyKontrakt";
    }

    @PostMapping("/nyKontrakter")
    public String visLejeKontraktForm(@RequestParam("telefonnummer") String telefonnummer,
                                      @RequestParam("nummerplade") String nummerplade,
                                      @RequestParam("startdato") LocalDate startdato,
                                      @RequestParam("slutdato") LocalDate slutdato,
                                      @RequestParam("maxKm") int maxKm,
                                      @RequestParam("pris") int pris,
                                      Model model) throws SQLException {

        LejeKontrakt kontrakt = new LejeKontrakt(); // der oprettes en lejekontrakt object med info fra brugeren
        kontrakt.setTelefonnummer(telefonnummer);
        kontrakt.setNummerplade(nummerplade);
        kontrakt.setStartdato(startdato);
        kontrakt.setSlutdato(slutdato);
        kontrakt.setMaxKm(maxKm);
        kontrakt.setPris(pris);
        lejeKontraktService.addLejekontrakt(kontrakt); // her sendes den videre til addlejekontrakt metoden
        return "redirect:/manageKontrakter";
    }

    @GetMapping("/nummerplade")
    public String nummerplade(Model model) throws SQLException {
        return "homeKontrakt/nyKontrakt";
    }

    @PostMapping("/tilføjKontrakt")
    public String tilføjKontrakt(@RequestParam("telefonnummer") String telefonnummer,
                                 @RequestParam("nummerplade") String nummerplade,
                                 @RequestParam("startdato") LocalDate startdato,
                                 @RequestParam("slutdato") LocalDate slutdato,
                                 @RequestParam("maxKm") int maxKm,
                                 @RequestParam("pris") Double pris,
                                 RedirectAttributes redirectAttributes) throws SQLException {

        try {

            LejeKontrakt kontrakt = new LejeKontrakt();
            kontrakt.setTelefonnummer(telefonnummer);
            kontrakt.setNummerplade(nummerplade);
            kontrakt.setStartdato(startdato);
            kontrakt.setSlutdato(slutdato);
            kontrakt.setMaxKm(maxKm);
            kontrakt.setPris(pris);
            lejeKontraktService.addLejekontrakt(kontrakt);
            redirectAttributes.addFlashAttribute("oprettet", "Lejekontrakt er blevet oprettet."); // giver en message hvis lejekontrakt bliver oprettet
            return "redirect:/manageKontrakter";
        } catch (DataIntegrityViolationException ex) {
            // Handle the exception and add an error message
            redirectAttributes.addFlashAttribute("error",
                    "Systemet kunne ikke oprette kontrakten. Tjek om telefonnummeret og nummerpladen eksisterer"); // giver en message hvis lejekontrakt ikke bliver oprettet
            redirectAttributes.addFlashAttribute("opretNyKunde", true);
            return "redirect:/manageKontrakter";
        }
    }


    @PostMapping("/sletKontrakt")
    public String sletKontrakt(@RequestParam("lejekontrakt_id") int id) throws SQLException {
        lejeKontraktService.sletLejeKontract(id);
        return "redirect:/manageKontrakter";
    }

    // til at søge alle kontrakter til det samme kunde (telefonnummer)
    @GetMapping("/soegLejekontraktTelefon")
    public String homemanageKontrakt() {
        return ("homeKontrakt/manageKontrakter");
    }


    @GetMapping("/backToKontrakt")
    public String backToKontrakt(Model model) {
        return "redirect:/manageKontrakter";
    }



    @PostMapping("/kundensKontrakter")
    public String kundensKontrakter(@RequestParam("telefonnummer") String telefonnummer, Model model, RedirectAttributes redirectAttributes) throws SQLException {

        List<LejeKontrakt> kontrakter = lejeKontraktService.findKontraktByTelefon(telefonnummer);

        if (kontrakter == null || kontrakter.isEmpty()){

            redirectAttributes.addFlashAttribute("ingenKontrakt", "Ingen lejekontrakter med dette telefonnummer eksisterer.");
            redirectAttributes.addFlashAttribute("nyKunde", true);
            return "redirect:/manageKontrakter";
        } else {
            model.addAttribute("kontrakter", kontrakter);
            return "homeKontrakt/kundensKontrakter";
        }

    }


    @GetMapping("/alleKontrakter")
    public String alleKontrakter(){

        return "redirect:/manageKontrakter";
    }

    @GetMapping("/redigerLejeKontrakt/{lejekontrakt_id}")
    public String redigerLejeKontrakt(@PathVariable("lejekontrakt_id") int lejekontrakt_id, Model model) throws SQLException {
        model.addAttribute("lejekontrakt", lejeKontraktService.getLejeKontraktById(lejekontrakt_id));
        return "homeKontrakt/redigerLejeKontrakt";
    }

    @PostMapping("/redigerLejeKontrakt")
    public String redigerLejeKontrakt(@ModelAttribute LejeKontrakt lejeKontrakt) throws SQLException {
        lejeKontraktService.redigerLejeKontrakt(lejeKontrakt);
        return "redirect:/manageKontrakter";
    }

}


