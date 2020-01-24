package com.consultant.model.services;

import com.consultant.model.dto.ClientDTO;
import com.consultant.model.entities.Client;
import com.consultant.model.exception.EntityAlreadyExists;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.repositories.ClientRepository;
import com.consultant.model.services.impl.ClientServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClientServiceShould {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ConversionService conversionService;

    private ClientService clientService;

    private Long clientId = 1L;

    private Client client1 = new Client();

    private Client client2 = new Client();

    private List<Client> clientList = new ArrayList<>();

    private ClientDTO clientDTO = new ClientDTO();

    @Before
    public void setUp() {
        clientService = new ClientServiceImpl(clientRepository, conversionService);

        client1 = new Client();
        client1.setId(clientId);
        when(clientRepository.findAll()).thenReturn(clientList);

        clientDTO = new ClientDTO();
        when(conversionService.convert(clientDTO, Client.class)).thenReturn(client1);
        when(conversionService.convert(client1, ClientDTO.class)).thenReturn(clientDTO);
    }

    @Test
    public void returnListOfAllExistingClients() throws Exception {
        clientList.add(client1);
        clientList.add(client2);
        Set<ClientDTO> clientDTOS = clientService.getAllClients();
        Assert.assertThat(clientDTOS.size(), is(2));
    }

    @Test
    public void returnEmptyListIfThereAreNoClients() throws Exception {
        when(clientRepository.findAll()).thenReturn(clientList);
        Set<ClientDTO> clientDTOS = clientService.getAllClients();

        Assert.assertThat(clientDTOS.isEmpty(), is(true));
    }

    @Test
    public void saveToRepositoryWhenCreatingClient() throws Exception {
        clientService.createClient(clientDTO);

        verify(clientRepository, times(1)).saveAndFlush(client1);
    }

    @Test(expected = EntityAlreadyExists.class)
    public void throwEntityAlreadyExistWhenCreatingClientWithSameName() throws Exception {
        String name = "name";
        client1.setName(name);
        clientDTO.setName(name);
        Mockito.when(clientRepository.findByName(name)).thenReturn(Optional.ofNullable(client1));
        clientService.createClient(clientDTO);
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
        clientDTO.setLastInteractedBy("name");
        clientDTO.setId(clientId);
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.ofNullable(client1));
        clientService.editClient(clientDTO);

        verify(clientRepository, times(1)).saveAndFlush(client1);
    }

    @Test(expected = NoMatchException.class)
    public void throwNoMatchExceptionWhenUpdatingNonExistingCandidate() throws Exception {
        clientDTO.setLastInteractedBy("name");
        clientDTO.setId(clientId);
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.empty());
        clientService.editClient(clientDTO);
    }
}