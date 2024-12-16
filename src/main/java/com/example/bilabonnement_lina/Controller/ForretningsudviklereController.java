package com.example.bilabonnement_lina.Controller;

import com.example.bilabonnement_lina.Model.MonthlyIncome;
import com.example.bilabonnement_lina.Model.TopBil;
import com.example.bilabonnement_lina.Service.BilService;
import com.example.bilabonnement_lina.Service.LejeKontraktService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ForretningsudviklereController {

    @Autowired
    LejeKontraktService lejeKontraktService;

    @Autowired
    BilService bilService;



    @GetMapping("/samletIndtægt")
    public String samletIndtægt() {
        return "homeForretningsUdvikler/samletIndtægt";
    }


    @PostMapping("/samletIndtægt")
    public String samletIndtægt(@RequestParam("fraDato")LocalDate fraDato,@RequestParam("tilDato")LocalDate tilDato,  Model model)throws SQLException{

        Double samletIndtægt = bilService.getSamletIndtægt(fraDato, tilDato); //method getSamletIndtægter skal laves i repo og service

        if (samletIndtægt == null || samletIndtægt == 0.0){
            model.addAttribute("ingenIndtægt", "Der er ikke nogen indtægt indenfor denne dato.");
        } else {
            model.addAttribute("fraDato", fraDato);
            model.addAttribute("tilDato", tilDato);
            model.addAttribute("samletIndtægt", samletIndtægt);
        }


        model.addAttribute("fraDato", fraDato);
        model.addAttribute("tilDato", tilDato);

        return "homeForretningsUdvikler/samletIndtægt";
    }

    @GetMapping("/topLejedeModeller")
    public String topLejedeModeller(){
        return "homeForretningsUdvikler/topLejedeModeller";
    }

    @PostMapping("/topLejedeModeller")
    public String topLejedeModeller(@RequestParam("fraDato") LocalDate fraDato,
                                    @RequestParam("tilDato")LocalDate tilDato,  Model model)throws SQLException {

        try {
            TopBil topLejedeModel = bilService.getTopLejedeModeller(fraDato, tilDato);
            //System.out.println(topBil);
            String bilModel = topLejedeModel.getModel();
            String maerke = topLejedeModel.getMaerke();
            int antalLånt = topLejedeModel.getAntal();
            model.addAttribute("fraDato", fraDato);
            model.addAttribute("tilDato", tilDato);
            model.addAttribute("model", bilModel);
            model.addAttribute("maerke", maerke);
            model.addAttribute("antalLånt", antalLånt);

            return "homeForretningsUdvikler/topLejedeModeller";
        } catch (EmptyResultDataAccessException e){
            model.addAttribute("ingenTopBil", "Der er ikke udlånt nogle biler i denne periode");
            return "homeForretningsUdvikler/topLejedeModeller";
        }
    }


    @GetMapping("/homeForretningsUdvikler")
    public String homeForretningsUdvikler(){
        return "homeForretningsUdvikler/forretningsUdvikler";
    }



    @GetMapping("/antalLejedeBiler")
    public String visAntalLejedeBilerForm(Model model) throws SQLException {
        List<String> maerker = lejeKontraktService.getBilMaerker();
        model.addAttribute("maerker", maerker);
        model.addAttribute("lejedeBiler", 0); // Sætter til 0 så view ikke breaker
        model.addAttribute("startdato", LocalDate.now()); // sætter en default start dato
        model.addAttribute("slutdato", LocalDate.now()); //og slut dato
        return "homeForretningsUdvikler/antalLejedeBiler";
    }



    @PostMapping("/antalLejedeBiler")
    public String antalLejedeBiler(
            @RequestParam("startdato") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startdato,
            @RequestParam("slutdato") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate slutdato,
            @RequestParam(value = "maerke", required = false) String selectedMaerke,
            Model model
    ) throws SQLException {



        // liste maerker
        List<String> maerker = lejeKontraktService.getBilMaerker();


        int lejedeBiler;
        if (selectedMaerke == null || selectedMaerke.isEmpty()) {
            lejedeBiler = lejeKontraktService.getAntalBiler(startdato, slutdato);
        } else {
            lejedeBiler = lejeKontraktService.getAntalBilerMaerke(startdato, slutdato, selectedMaerke);
        }

        model.addAttribute("lejedeBiler", lejedeBiler);
        model.addAttribute("startdato", startdato);
        model.addAttribute("slutdato", slutdato);
        model.addAttribute("maerker", maerker); //liste mærker
        model.addAttribute("selectedMaerke", selectedMaerke); //udvalgte mærke
        return "homeForretningsUdvikler/antalLejedeBiler";
    }

    @PostMapping("/monthlyIncome")
    public String monthlyIncome(@RequestParam("year") int year, Model model) {
        List<MonthlyIncome> incomeList = lejeKontraktService.monthlyIncomeList(year);
        // Debug: Log the fetched data
        for (MonthlyIncome income : incomeList) {
            System.out.println("Month: " + income.getMåned() + ", Income: " + income.getIndtjening());
        }
        model.addAttribute("indtjening", incomeList);
        model.addAttribute("year", year);
        return "homeForretningsUdvikler/samletIndtægt";
    }



}
