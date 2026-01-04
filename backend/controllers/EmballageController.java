package com.example.bstagepfe.controllers;
import com.example.bstagepfe.entities.Emballage;
import com.example.bstagepfe.services.ServiceEmballage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/emballages")
public class EmballageController {

    @Autowired
    private ServiceEmballage serviceEmballage;

    @GetMapping
    public List<Emballage> getAllEmballages() {
        return serviceEmballage.getAllEmballages();
    }

    @PostMapping
    public Emballage addEmballage(@RequestBody Emballage emballage) {
        return serviceEmballage.addEmballage(emballage);
    }

    @PutMapping("/{id}")
    public Emballage updateEmballage(@PathVariable Long id, @RequestBody Emballage emballage) {
        return serviceEmballage.updateEmballage(id, emballage);
    }

    @DeleteMapping("/{id}")
    public void deleteEmballage(@PathVariable Long id) {
        serviceEmballage.deleteEmballage(id);
    }
}
