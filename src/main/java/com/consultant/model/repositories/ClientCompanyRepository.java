package com.consultant.model.repositories;

import com.consultant.model.entities.ClientCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientCompanyRepository extends JpaRepository<ClientCompany, Long> {

    Optional<ClientCompany> findByName(String name);
}
