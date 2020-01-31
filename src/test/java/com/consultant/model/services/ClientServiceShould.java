package com.consultant.model.services;

import com.consultant.model.dto.ClientDTO;
import com.consultant.model.entities.Client;
import com.consultant.model.entities.ClientTeam;
import com.consultant.model.exception.EntityAlreadyExists;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.repositories.ClientRepository;
import com.consultant.model.services.impl.ClientServiceImpl;
import com.consultant.model.services.impl.ClientTeamServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClientServiceShould {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ConversionService conversionService;

    @Mock
    private ClientTeamServiceImpl clientTeamService;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Long clientId = 1L;

    private Client client1 = new Client();

    private Client client2 = new Client();

    private List<Client> clientList = new ArrayList<>();

    private ClientDTO clientDTO1 = new ClientDTO();

    private ClientDTO clientDTO2 = new ClientDTO();

    private ClientTeam clientTeam = new ClientTeam();

    @Before
    public void setUp() {
        client1.setId(clientId);

        when(clientRepository.findAll()).thenReturn(clientList);
        when(conversionService.convert(clientDTO1, Client.class)).thenReturn(client1);
        when(conversionService.convert(client1, ClientDTO.class)).thenReturn(clientDTO1);
    }

    @Test
    public void returnListOfAllExistingClients() {
        client1.setClientTeams(Collections.singletonList(clientTeam));
        clientDTO1.setClientTeams(Collections.singletonList(clientTeam));
        clientList.add(client1);

        clientList.add(client2);
        when(conversionService.convert(client2, ClientDTO.class)).thenReturn(clientDTO2);

        Set<ClientDTO> clientDTOS = clientService.getAllClients();
        Assert.assertThat(clientDTOS.size(), is(2));
    }

    @Test
    public void returnEmptyListIfThereAreNoClients() {
        when(clientRepository.findAll()).thenReturn(clientList);
        Set<ClientDTO> clientDTOS = clientService.getAllClients();

        Assert.assertThat(clientDTOS.isEmpty(), is(true));
    }

    @Test
    public void saveToRepositoryWhenCreatingClient() {
        clientService.createClient(clientDTO1);

        verify(clientRepository, times(1)).saveAndFlush(client1);
    }

    @Test(expected = EntityAlreadyExists.class)
    public void throwEntityAlreadyExistWhenCreatingClientWithSameName() {
        String name = "name";
        client1.setName(name);
        clientDTO1.setName(name);
        Mockito.when(clientRepository.findByName(name)).thenReturn(Optional.ofNullable(client1));
        clientService.createClient(clientDTO1);
    }

    @Test
    public void deleteInRepositoryWhenDeletingExistingClient() throws Exception {
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.ofNullable(client1));
        clientService.deleteClient(clientId);

        verify(clientRepository, times(1)).delete(client1);
    }

    @Test(expected = NoMatchException.class)
    public void throwNoMatchExceptionWhenDeletingNonExistingClient() throws Exception {
        clientService.deleteClient(clientId);
    }

    @Test
    public void updateOnEditingExistingClient() throws Exception {
        clientDTO1.setLastInteractedBy("name");
        clientDTO1.setId(clientId);
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.ofNullable(client1));
        clientService.editClient(clientDTO1);

        verify(clientRepository, times(1)).saveAndFlush(client1);
    }

    @Test(expected = NoMatchException.class)
    public void throwNoMatchExceptionWhenUpdatingNonExistingCandidate() throws Exception {
        clientDTO1.setLastInteractedBy("name");
        clientDTO1.setId(clientId);
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.empty());
        clientService.editClient(clientDTO1);
    }
}