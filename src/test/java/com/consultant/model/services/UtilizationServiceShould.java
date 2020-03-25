package com.consultant.model.services;

import com.consultant.model.dto.ConsultantDTO;
import com.consultant.model.dto.VacationDTO;
import com.consultant.model.entities.Contract;
import com.consultant.model.entities.Utilization;
import com.consultant.model.entities.Vacation;
import com.consultant.model.repositories.UtilizationRepository;
import com.consultant.model.services.impl.ConsultantService;
import com.consultant.model.services.impl.VacationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class UtilizationServiceShould {

    private UtilizationService utilizationService;

    @Mock
    private UtilizationRepository utilizationRepository;

    @Mock
    private ConsultantService consultantService;

    @Mock
    private VacationService vacationService;

    private Set<ConsultantDTO> consultantDTOS = new HashSet<>();

    private List<Utilization> utilizationList = new ArrayList<>();

    private ConsultantDTO consultantDTO = new ConsultantDTO();

    private Utilization utilization = new Utilization();

    @Before
    public void setUp() {
        utilizationService = new UtilizationService(utilizationRepository, consultantService, vacationService);
        Mockito.when(consultantService.getAll()).thenReturn(consultantDTOS);
        Mockito.when(utilizationRepository.findAll()).thenReturn(utilizationList);
    }

    private void initializeContract(Contract contract, LocalDate startDate, ConsultantDTO consultantDTO) {
        contract.setStartedDate(startDate);
        contract.setActive(true);
        contract.setClientName("Client");
        consultantDTO.getContracts().add(contract);
    }

    @Test
    public void returnZeroUtIfThereAreNoContracts() {

        utilizationService.calcUtilPercentOfGivenMonth(LocalDate.now(), utilization);
        Assert.assertEquals(0, utilization.getUt(), 0.0);
    }

    @Test
    public void returnZeroAUtIfThereAreNoContracts() {
        utilizationService.calcUtilPercentOfGivenMonth(LocalDate.now(), utilization);
        Assert.assertEquals(0, utilization.getAidedUt(), 0.0);
    }

    @Test
    public void returnZeroUtIfThereAreNoConsultantsJoinedBeforeGivenDate() {
        createConsultant(LocalDate.now().plusDays(1));
        Contract contract = new Contract();
        initializeContract(contract, LocalDate.now().minusMonths(1), consultantDTO);
        utilizationService.calcUtilPercentOfGivenMonth(LocalDate.now(), utilization);

        Assert.assertEquals(0, utilization.getUt(), 0.0);
    }

    @Test
    public void returnZeroUtilizationIfThereAreNoActiveConsultants() {
        consultantDTO.setDateJoined(LocalDate.now().minusMonths(1));
        consultantDTO.setDeleted(true);
        Contract contract = new Contract();
        initializeContract(contract, LocalDate.now().minusMonths(1), consultantDTO);
        utilizationService.calcUtilPercentOfGivenMonth(LocalDate.now(), utilization);

        Assert.assertEquals(0, utilization.getUt(), 0.0);
    }

    @Test
    public void returnFullUtilizationIfThereAreActiveConsultantsJoinedOnPreviousMonthsThatGivenDate() {
        createConsultant(LocalDate.now().minusMonths(2));
        Contract contract = new Contract();
        initializeContract(contract, LocalDate.now().minusMonths(2), consultantDTO);

        utilizationService.calcUtilPercentOfGivenMonth(LocalDate.now(), utilization);
        Assert.assertEquals(100, utilization.getUt(), 0.0);
    }

    @Test
    public void returnFullUtilizationIfConsultantJoinedOnGivenMonthAndHasActiveContract() {
        createConsultant(LocalDate.now().minusMonths(1));
        Contract contract = new Contract();
        initializeContract(contract, (LocalDate.now().minusMonths(1)), consultantDTO);

        utilizationService.calcUtilPercentOfGivenMonth(LocalDate.now(), utilization);

        Assert.assertEquals(100, utilization.getUt(), 0.0);
    }


    @Test
    public void returnFullAidedUtilizationIfConsultantHasActiveContractOfGivenMonth() {
        createConsultant(LocalDate.now().minusMonths(5));
        Contract contract = new Contract();
        initializeContract(contract, (LocalDate.now().minusMonths(1)), consultantDTO);

        utilizationService.calcUtilPercentOfGivenMonth(LocalDate.now(), utilization);

        Assert.assertEquals(100, utilization.getUt(), 0.0);
    }

    @Test
    public void returnZeroUtilizationIfConsultantHasOfficeContractOfGivenMonth() {
        createConsultant(LocalDate.now().minusMonths(1));
        Contract contract = new Contract();
        initializeContract(contract, (LocalDate.now().minusMonths(1)), consultantDTO);
        contract.setClientName(ContractService.OFFICE_NAME);

        utilizationService.calcUtilPercentOfGivenMonth(LocalDate.now(), utilization);

        Assert.assertEquals(0, utilization.getUt(), 0.0);
    }

    @Test
    public void returnFullUtilizationIfClientContractStartedAndEndedOnGivenMonth() {
        createConsultant(LocalDate.now().minusMonths(2));
        Contract contract = new Contract();
        initializeContract(contract, (LocalDate.now().minusMonths(1)), consultantDTO);
        contract.setEndDate(LocalDate.now().minusMonths(1).plusDays(1));

        utilizationService.calcUtilPercentOfGivenMonth(LocalDate.now(), utilization);

        Assert.assertEquals(100, utilization.getUt(), 0.0);
    }

    @Test
    public void returnZeroUtilizationIfOfficeContractStartedAndEndedOnGivenMonth() {
        createConsultant(LocalDate.now().minusMonths(2));
        Contract contract = new Contract();
        initializeContract(contract, (LocalDate.now().minusMonths(1)), consultantDTO);
        contract.setEndDate(LocalDate.now().minusMonths(1).plusDays(1));
        contract.setClientName(ContractService.OFFICE_NAME);

        utilizationService.calcUtilPercentOfGivenMonth(LocalDate.now(), utilization);

        Assert.assertEquals(0, utilization.getUt(), 0.0);
    }

    @Test
    public void returnFullUtilizationIfClientContractStartedOnPreviousMonthsAndEndsOnGivenMonth() {
        createConsultant(LocalDate.now().minusMonths(2));
        Contract contract = new Contract();
        initializeContract(contract, (LocalDate.now().minusMonths(2)), consultantDTO);
        contract.setEndDate(LocalDate.now().minusMonths(1).plusDays(1));

        utilizationService.calcUtilPercentOfGivenMonth(LocalDate.now(), utilization);

        Assert.assertEquals(100, utilization.getUt(), 0.0);
    }

    @Test
    public void returnZeroUtilizationIfOfficeContractStartedOnPreviousMonthsAndEndsOnGivenMonth() {
        createConsultant(LocalDate.now().minusMonths(2));
        Contract contract = new Contract();
        initializeContract(contract, (LocalDate.now().minusMonths(2)), consultantDTO);
        contract.setEndDate(LocalDate.now().minusMonths(1).plusDays(1));
        contract.setClientName(ContractService.OFFICE_NAME);

        utilizationService.calcUtilPercentOfGivenMonth(LocalDate.now(), utilization);

        Assert.assertEquals(0, utilization.getUt(), 0.0);
    }

    @Test
    public void returnUtilizationIfConsultantHasActiveAndInactiveContractsOnGivenMonth() {
        createConsultant(LocalDate.of(2020, 3, 1));
        Contract contract = new Contract();
        initializeContract(contract, LocalDate.of(2020, 3, 1), consultantDTO);
        contract.setEndDate(LocalDate.of(2020, 3, 10));
        contract.setClientName(ContractService.OFFICE_NAME);
        contract.setActive(false);
        Contract contract2 = new Contract();
        initializeContract(contract2, LocalDate.of(2020, 3, 11), consultantDTO);

        utilizationService.calcUtilPercentOfGivenMonth(LocalDate.of(2020, 4, 1), utilization);

        Assert.assertEquals(67.74, utilization.getUt(), 0.01);
    }

    @Test
    public void returnFullAUtIfConsultantIsFullyAided() {
        createConsultant(LocalDate.now().minusMonths(2));
        Contract contract = new Contract();
        initializeContract(contract, (LocalDate.now().minusMonths(2)), consultantDTO);
        utilizationService.calcUtilPercentOfGivenMonth(LocalDate.now(), utilization);

        Assert.assertEquals(100, utilization.getAidedUt(), 0.0);
    }

    @Test
    public void returnAUtIfConsultantIsPartiallyAided() {
        createConsultant(LocalDate.of(2019, 12, 3));
        Contract contract = new Contract();
        initializeContract(contract, LocalDate.of(2019, 12, 3), consultantDTO);
        contract.setClientName(ContractService.OFFICE_NAME);

        utilizationService.calcUtilPercentOfGivenMonth(LocalDate.of(2020, 4, 1), utilization);

        Assert.assertEquals(6.45, utilization.getAidedUt(), 0.01);
    }

    @Test
    public void returnFullAUtIfConsultantJoinedOnCalculatedMonth() {
        createConsultant(LocalDate.of(2020, 3, 5));
        Contract contract = new Contract();
        initializeContract(contract, LocalDate.of(2019, 12, 3), consultantDTO);
        contract.setClientName(ContractService.OFFICE_NAME);

        utilizationService.calcUtilPercentOfGivenMonth(LocalDate.of(2020, 4, 1), utilization);

        Assert.assertEquals(100, utilization.getAidedUt(), 0.0);
    }

    @Test
    public void returnFullUtilIfPartiallyAidedConsultantHasActiveContractBeforeGivenDate() {
        createConsultant(LocalDate.of(2019, 12, 6));
        Contract contract = new Contract();
        initializeContract(contract, LocalDate.of(2019, 12, 6), consultantDTO);
        contract.setClientName(ContractService.OFFICE_NAME);
        initializeContract(contract, LocalDate.of(2020, 2, 3), consultantDTO);

        utilizationService.calcUtilPercentOfGivenMonth(LocalDate.of(2020, 4, 1), utilization);

        Assert.assertEquals(100, utilization.getAidedUt(), 0.0);
    }

    @Test
    public void returnUtilizationForPartialAidedConsultantWithActiveContract() {
        createConsultant(LocalDate.of(2019, 12, 3));

        Contract officeContract = new Contract();
        initializeContract(officeContract, LocalDate.of(2019, 12, 3), consultantDTO);
        officeContract.setClientName(ContractService.OFFICE_NAME);
        officeContract.setEndDate(LocalDate.of(2020, 3, 8));
        Contract clientContract = new Contract();
        initializeContract(clientContract, LocalDate.of(2020, 3, 9), consultantDTO);

        utilizationService.calcUtilPercentOfGivenMonth(LocalDate.of(2020, 4, 1), utilization);

        Assert.assertEquals(80.64, utilization.getAidedUt(), 0.01);
    }

    @Test
    public void returnFullUtilizationIfPartialAidedConsultantDateOverlapsStartedActiveContract() {
        createConsultant(LocalDate.of(2019, 12, 6));
        Contract officeContract = new Contract();
        initializeContract(officeContract, LocalDate.of(2019, 12, 6), consultantDTO);
        officeContract.setClientName(ContractService.OFFICE_NAME);
        officeContract.setEndDate(LocalDate.of(2020, 3, 2));
        Contract clientContract = new Contract();
        initializeContract(clientContract, LocalDate.of(2020, 3, 3), consultantDTO);

        utilizationService.calcUtilPercentOfGivenMonth(LocalDate.of(2020, 4, 1), utilization);

        Assert.assertEquals(100, utilization.getAidedUt(), 0.01);
    }

    @Test
    public void returnZeroUtOnLongTermVacation() {
        createConsultant((LocalDate.of(2019, 12, 6)));
        Vacation vacation = new Vacation();
        vacation.setIsLongTerm(true);
        vacation.setStartingDate(LocalDate.of(2020, 3, 1));
        Mockito.when(vacationService.getVacationsOfConsultant(1L)).thenReturn(Collections.singletonList(vacation));

        Contract contract = new Contract();
        initializeContract(contract, LocalDate.of(2019, 12, 6), consultantDTO);

        utilizationService.calcUtilPercentOfGivenMonth(LocalDate.of(2020, 4, 1), utilization);

        Assert.assertEquals(0, utilization.getAidedUt(), 0.00);
    }

    @Test
    public void returnZeroUtOnPartiallyAssignedPlusLongTermVacation() {
        consultantDTO.setDateJoined((LocalDate.of(2019, 12, 6)));
        consultantDTO.setDeleted(false);
        consultantDTO.setId(1L);
        Vacation vacation = new Vacation();
        vacation.setIsLongTerm(true);
        vacation.setStartingDate(LocalDate.of(2020, 3, 1));
        vacation.setEndDate(LocalDate.of(2020, 3, 20));
        Mockito.when(vacationService.getVacationsOfConsultant(1L)).thenReturn(Collections.singletonList(vacation));
        consultantDTOS.add(consultantDTO);
        Contract contract = new Contract();
        initializeContract(contract, LocalDate.of(2019, 12, 6), consultantDTO);

        utilizationService.calcUtilPercentOfGivenMonth(LocalDate.of(2020, 4, 1), utilization);

        Assert.assertEquals(0, utilization.getAidedUt(), 0.00);
    }

    @Test
    public void returnUtOnPartiallyAidedAndPartiallyAssignedWithLongTermVacation() {
        createConsultant((LocalDate.of(2019, 12, 6)));
        Vacation vacation = new Vacation();
        vacation.setIsLongTerm(true);
        vacation.setStartingDate(LocalDate.of(2020, 3, 20));
        Mockito.when(vacationService.getVacationsOfConsultant(1L)).thenReturn(Collections.singletonList(vacation));

        Contract officeContract = new Contract();
        initializeContract(officeContract, LocalDate.of(2019, 12, 6), consultantDTO);
        officeContract.setClientName(ContractService.OFFICE_NAME);
        officeContract.setEndDate(LocalDate.of(2020, 3, 10));
        Contract clientContract = new Contract();
        initializeContract(clientContract, LocalDate.of(2020, 3, 11), consultantDTO);

        utilizationService.calcUtilPercentOfGivenMonth(LocalDate.of(2020, 4, 1), utilization);

        Assert.assertEquals(73.68, utilization.getAidedUt(), 0.01);
    }

    @Test
    public void returnsAllSavedUtilization() {
        Utilization utilization = new Utilization();
        utilizationList.add(utilization);
        Assert.assertEquals(1, utilizationService.getAllUtilization().size());
    }

    @Test
    public void calculateAndSaveCurrentUtilizationIfNotSaved() {
        Mockito.when(utilizationRepository.findByDate(Mockito.any())).thenReturn(Optional.empty());

        utilizationService.saveUtToDb();
        Mockito.verify(utilizationRepository, Mockito.times(1)).saveAndFlush(Mockito.any());
    }

    @Test
    public void recalculateAndSaveCurrentUtilizationIfExists() {
        Utilization utilization = new Utilization();
        Mockito.when(utilizationRepository.findByDate(Mockito.any())).thenReturn(Optional.of(utilization));

        utilizationService.saveUtToDb();
        Mockito.verify(utilizationRepository, Mockito.times(1)).saveAndFlush(Mockito.any());
    }

    @Test
    public void recalculateAndSaveAllUtilization() {
        Contract contract = new Contract();
        consultantDTO.setDateJoined((LocalDate.now().minusMonths(2)));
        consultantDTO.setDeleted(false);
        consultantDTOS.add(consultantDTO);
        initializeContract(contract, (LocalDate.now().minusMonths(2)), consultantDTO);

        utilizationService.reCalcAllUtil();
        Mockito.verify(utilizationRepository, Mockito.times(2)).saveAndFlush(Mockito.any());
    }

    private void createConsultant(LocalDate localDate) {
        consultantDTO.setDateJoined(localDate);
        consultantDTO.setDeleted(false);
        consultantDTO.setId(1L);
        consultantDTOS.add(consultantDTO);
    }
}