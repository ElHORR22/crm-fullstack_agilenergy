package com.example.bstagepfe.repos;
import com.example.bstagepfe.entities.Role;
import com.example.bstagepfe.entities.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleType name);
}

