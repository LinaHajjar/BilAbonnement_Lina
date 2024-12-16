package com.example.bilabonnement_lina.Service;

import com.example.bilabonnement_lina.Model.BilHaandtering;
import com.example.bilabonnement_lina.Repository.BilHaandteringRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class BilHaandteringService {
    @Autowired
    BilHaandteringRepo bilHaandteringRepo;




    public List<BilHaandtering> bilHaandteringsList(){
        return bilHaandteringRepo.seAlleHaandteringer();
    }

    public void opretteUdlevering(BilHaandtering bilHaandtering){
        bilHaandteringRepo.opretteUdlevering(bilHaandtering);
    }

    public void opretteReturnering (BilHaandtering bilHaandtering){
        bilHaandteringRepo.opretteReturnering(bilHaandtering);
    }

    public void opdaterStatus(int haanteringId, BilHaandtering.status status){
        bilHaandteringRepo.opdaterStatus(haanteringId, status);
    }
}
