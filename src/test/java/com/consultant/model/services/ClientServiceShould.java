package com.consultant.model.services;

import com.consultant.model.dto.ClientDTO;
import com.consultant.model.entities.Client;
import com.consultant.model.entities.ClientTeam;
import com.consultant.model.exception.EntityAlreadyExists;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.repositories.ClientRepository;
import com.consultant.model.services.impl.ClientService;
import com.consultant.model.services.impl.ClientTeamService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClientServiceShould {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientTeamService clientTeamService;

    @InjectMocks
    private ClientService clientService;

    private Long clientId = 1L;

    private Long teamId = 1L;

    private Client client1 = new Client();

    private Client client2 = new Client();

    private List<Client> clientList = new ArrayList<>();

    private ClientDTO clientDTO1 = new ClientDTO();

    private ClientTeam clientTeam = new ClientTeam();

    @Before
    public void setUp() {
        client1.setId(clientId);

        when(clientRepository.findAll()).thenReturn(clientList);
    }

    @Test
    public void returnListOfAllExistingClients() {
        client1.setClientTeams(Collections.singletonList(clientTeam));
        clientDTO1.setClientTeams(Collections.singletonList(clientTeam));
        clientList.add(client1);

        clientList.add(client2);
        Set<ClientDTO> clientDTOS = clientService.getAll();
        Assert.assertThat(clientDTOS.size(), is(2));
    }

    @Test
    public void returnEmptyListIfThereAreNoClients() {
        when(clientRepository.findAll()).thenReturn(clientList);
        Set<ClientDTO> clientDTOS = clientService.getAll();

        Assert.assertThat(clientDTOS.isEmpty(), is(true));
    }

    @Test
    public void saveToRepositoryWhenCreatingClient() throws NoMatchException {
        clientService.create(clientDTO1);

        verify(clientRepository, times(1)).saveAndFlush(client1);
    }

    @Test(expected = EntityAlreadyExists.class)
    public void throwEntityAlreadyExistWhenCreatingClientWithSameName() throws NoMatchException {
        String name = "name";
        client1.setName(name);
        clientDTO1.setName(name);
        Mockito.when(clientRepository.findByName(name)).thenReturn(Optional.ofNullable(client1));
        clientService.create(clientDTO1);
    }

    @Test
    public void saveToRepositoryWhenDeletingExistingClient() throws Exception {
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.ofNullable(client1));
        clientService.delete(clientId);

        verify(clientRepository, times(1)).saveAndFlush(client1);
    }

    @Test(expected = NoMatchException.class)
    public void throwNoMatchExceptionWhenDeletingNonExistingClient() throws Exception {
        clientService.delete(clientId);
    }

    @Test
    public void updateOnEditingExistingClient() throws Exception {
        clientDTO1.setLastInteractedBy("name");
        clientDTO1.setId(clientId);
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.ofNullable(client1));
        clientService.edit(clientDTO1);

        verify(clientRepository, times(1)).saveAndFlush(client1);
    }

    @Test(expected = NoMatchException.class)
    public void throwNoMatchExceptionWhenUpdatingNonExistingClient() throws Exception {
        clientDTO1.setLastInteractedBy("name");
        clientDTO1.setId(clientId);
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.empty());
        clientService.edit(clientDTO1);
    }

    @Test
    public void saveToRepositoryWhenAssigningTeamToExistingClient() throws Exception {
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.ofNullable(client1));
        clientService.assignTeamToClient(clientTeam,clientId);
    }

    @Test(expected = NoMatchException.class)
    public void throwNoMatchExceptionWhenAssigningTeamToNonExistingClient() throws Exception {
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.empty());
        clientService.assignTeamToClient(clientTeam,clientId);
    }

    @Test
    public void returnAssignedClientOfTeam() {
        clientTeam.setId(teamId);
        Mockito.when(clientRepository.findByTeamId(teamId)).thenReturn(Optional.ofNullable(client1));
        Optional<Client> clientOfTeam = clientService.getClientOfTeam(teamId);
        Assert.assertEquals(client1,clientOfTeam.get());
    }
}