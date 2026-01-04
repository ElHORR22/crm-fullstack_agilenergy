package com.example.bstagepfe.services;
import com.example.bstagepfe.entities.Produit;
import com.example.bstagepfe.repos.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServiceProduit {

    @Autowired
    private ProduitRepository produitRepository;

    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    public Produit addProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    public Produit updateProduit(Long id, Produit detailsproduit) {
        return produitRepository.findProduitById(id).map(produit -> {
            produit.setNom(detailsproduit.getNom());
            produit.setCodeProduit(detailsproduit.getCodeProduit());
            produit.setLibelleProduit(detailsproduit.getLibelleProduit());
            produit.setCodeEmballage(detailsproduit.getCodeEmballage());
            produit.setCategorie(detailsproduit.getCategorie());
            produit.setPoidsProduit(detailsproduit.getPoidsProduit());
            produit.setTypeProduit(detailsproduit.getTypeProduit());
            produit.setActif(detailsproduit.getActif());
            produit.setPackageProduit(detailsproduit.getPackageProduit());
            produit.setEcozit(detailsproduit.getEcozit());
            produit.setPrixGros(detailsproduit.getPrixGros());
            produit.setPrixDetail(detailsproduit.getPrixDetail());
            produit.setPrixGerant(detailsproduit.getPrixGerant());
            produit.setDateCreation(detailsproduit.getDateCreation());
            produit.setDateModification(detailsproduit.getDateModification());

            return produitRepository.save(produit);
        }).orElseThrow(() -> new RuntimeException("Emballage non trouvé !"));
    }

    public void deleteProduit(Long id) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID : " + id));
        produit.setIsDeleted(true);
        produitRepository.save(produit);
    }
}
