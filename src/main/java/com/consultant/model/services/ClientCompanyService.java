package com.consultant.model.services;

import com.consultant.model.dto.ClientCompanyDTO;
import com.consultant.model.entities.ClientCompany;
import com.consultant.model.entities.ClientTeam;
import com.consultant.model.exception.NoMatchException;

import java.util.Optional;
import java.util.Set;

public interface ClientCompanyService {
    Set<ClientCompanyDTO> getAllCompanies();

    void createCompany(ClientCompanyDTO clientCompanyDTO);

    void editCompany(ClientCompanyDTO clientCompanyDTO) throws NoMatchException;

    void deleteCompany(Long id) throws NoMatchException;

    void assignTeamToCompany(ClientTeam clientTeam,Long companyId) throws NoMatchException;

    /** Gets the id of a team and returns his company if he is assigned to one and empty optional if he isn't
     * @param teamId the id of the team
     * @return client company that he is assigned or optional empty if he is not
     */
    Optional<ClientCompany> getCompanyOfTeam(Long teamId);
}
