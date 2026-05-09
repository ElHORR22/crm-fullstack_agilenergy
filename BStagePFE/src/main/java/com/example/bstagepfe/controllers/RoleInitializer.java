package com.example.bstagepfe.controllers;
import com.example.bstagepfe.entities.Role;
import com.example.bstagepfe.entities.RoleType;
import com.example.bstagepfe.repos.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void initRoles() {
        if (roleRepository.count() == 0) {
            for (RoleType roleType : RoleType.values()) {
                Role role = new Role();
                role.setName(roleType);
                roleRepository.save(role);
            }
        }
    }
}
