package com.consultant.model.services;

import com.consultant.model.dto.ClientCompanyDTO;
import com.consultant.model.entities.ClientTeam;
import com.consultant.model.exception.NoMatchException;

import java.util.Set;

public interface ClientCompanyService {
    Set<ClientCompanyDTO> getAllCompanies();

    void createCompany(ClientCompanyDTO clientCompanyDTO);

    void editCompany(ClientCompanyDTO clientCompanyDTO) throws NoMatchException;

    void deleteCompany(Long id) throws NoMatchException;

    void assignTeamToCompany(ClientTeam clientTeam,Long companyId) throws NoMatchException;
}
