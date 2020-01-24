package com.consultant.model.services;

import com.consultant.model.dto.ClientCompanyDTO;
import com.consultant.model.entities.ClientCompany;
import com.consultant.model.entities.ClientTeam;
import com.consultant.model.exception.EntityAlreadyExists;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.repositories.ClientCompanyRepository;
import com.consultant.model.services.impl.ClientCompanyServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClientCompanyServiceShould {

    @Mock
    private ClientCompanyRepository clientCompanyRepository;

    @Mock
    private ConversionService conversionService;

    private ClientCompanyService clientCompanyService;

    private Long clientId = 1L;

    private ClientCompany clientCompany1 = new ClientCompany();

    private ClientCompany clientCompany2 = new ClientCompany();

    private List<ClientCompany> clientCompanyList = new ArrayList<>();

    private ClientCompanyDTO clientCompanyDTO1 = new ClientCompanyDTO();

    private ClientCompanyDTO clientCompanyDTO2 = new ClientCompanyDTO();

    private ClientTeam clientTeam = new ClientTeam();

    @Before
    public void setUp() {
        clientCompanyService = new ClientCompanyServiceImpl(clientCompanyRepository, conversionService);

        clientCompany1.setId(clientId);

        when(clientCompanyRepository.findAll()).thenReturn(clientCompanyList);
        when(conversionService.convert(clientCompanyDTO1, ClientCompany.class)).thenReturn(clientCompany1);
        when(conversionService.convert(clientCompany1, ClientCompanyDTO.class)).thenReturn(clientCompanyDTO1);
    }

    @Test
    public void returnListOfAllExistingClients() {
        clientCompany1.setClientTeams(Collections.singletonList(clientTeam));
        clientCompanyDTO1.setClientTeams(Collections.singletonList(clientTeam));
        clientCompanyList.add(clientCompany1);

        clientCompanyList.add(clientCompany2);
        when(conversionService.convert(clientCompany2, ClientCompanyDTO.class)).thenReturn(clientCompanyDTO2);

        Set<ClientCompanyDTO> clientCompanyDTOS = clientCompanyService.getAllCompanies();
        Assert.assertThat(clientCompanyDTOS.size(), is(2));
    }

    @Test
    public void returnEmptyListIfThereAreNoClients() {
        when(clientCompanyRepository.findAll()).thenReturn(clientCompanyList);
        Set<ClientCompanyDTO> clientCompanyDTOS = clientCompanyService.getAllCompanies();

        Assert.assertThat(clientCompanyDTOS.isEmpty(), is(true));
    }

    @Test
    public void saveToRepositoryWhenCreatingClient() {
        clientCompanyService.createCompany(clientCompanyDTO1);

        verify(clientCompanyRepository, times(1)).saveAndFlush(clientCompany1);
    }

    @Test(expected = EntityAlreadyExists.class)
    public void throwEntityAlreadyExistWhenCreatingClientWithSameName() {
        String name = "name";
        clientCompany1.setName(name);
        clientCompanyDTO1.setName(name);
        Mockito.when(clientCompanyRepository.findByName(name)).thenReturn(Optional.ofNullable(clientCompany1));
        clientCompanyService.createCompany(clientCompanyDTO1);
    }

    @Test
    public void deleteInRepositoryWhenDeletingExistingClient() throws Exception {
        Mockito.when(clientCompanyRepository.findById(clientId)).thenReturn(Optional.ofNullable(clientCompany1));
        clientCompanyService.deleteCompany(clientId);

        verify(clientCompanyRepository, times(1)).delete(clientCompany1);
    }

    @Test(expected = NoMatchException.class)
    public void throwNoMatchExceptionWhenDeletingNonExistingClient() throws Exception {
        clientCompanyService.deleteCompany(clientId);
    }

    @Test
    public void updateOnEditingExistingClient() throws Exception {
        clientCompanyDTO1.setLastInteractedBy("name");
        clientCompanyDTO1.setId(clientId);
        Mockito.when(clientCompanyRepository.findById(clientId)).thenReturn(Optional.ofNullable(clientCompany1));
        clientCompanyService.editCompany(clientCompanyDTO1);

        verify(clientCompanyRepository, times(1)).saveAndFlush(clientCompany1);
    }

    @Test(expected = NoMatchException.class)
    public void throwNoMatchExceptionWhenUpdatingNonExistingCandidate() throws Exception {
        clientCompanyDTO1.setLastInteractedBy("name");
        clientCompanyDTO1.setId(clientId);
        Mockito.when(clientCompanyRepository.findById(clientId)).thenReturn(Optional.empty());
        clientCompanyService.editCompany(clientCompanyDTO1);
    }
}