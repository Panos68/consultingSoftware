package com.consultant.model.services.deprecated;

import com.consultant.model.dto.ConsultantDTO;
import com.consultant.model.entities.Contract;
import com.consultant.model.entities.Utilization;
import com.consultant.model.repositories.UtilizationRepository;
import com.consultant.model.services.ContractService;
import com.consultant.model.services.impl.ConsultantService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
@Deprecated
public class UtilizationServiceByConsultantShould {

    private UtilizationByConsultantService utilizationService;

    @Mock
    private UtilizationRepository utilizationRepository;

    @Mock
    private ConsultantService consultantService;

    private Set<ConsultantDTO> consultantDTOS = new HashSet<>();

    private List<Utilization> utilizationList = new ArrayList<>();

    private ConsultantDTO consultantDTO = new ConsultantDTO();

    @Before
    public void setUp() {
        utilizationService = new UtilizationByConsultantService(utilizationRepository, consultantService);
        Mockito.when(consultantService.getActiveConsultants()).thenReturn(consultantDTOS);
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
        Assert.assertEquals(0, utilizationService.calcUtilPercentOfGivenMonth(LocalDate.now()), 0.0);
    }

    @Test
    public void returnZeroAUtIfThereAreNoContracts() {
        Assert.assertEquals(0, utilizationService.calculateAidUtilPercentOfGivenMonth(LocalDate.now()), 0.0);
    }

    @Test
    public void returnZeroIfThereAreNoConsultantsJoinedBeforeGivenDate() {
        Contract contract = new Contract();
        consultantDTO.setDateJoined(LocalDate.now().plusDays(1));
        consultantDTO.setDeleted(false);
        consultantDTOS.add(consultantDTO);
        initializeContract(contract, LocalDate.now().minusMonths(1), consultantDTO);

        Assert.assertEquals(0, utilizationService.calcUtilPercentOfGivenMonth(LocalDate.now()), 0.0);
    }

    @Test
    public void returnZeroUtilizationIfThereAreNoActiveConsultants() {
        Contract contract = new Contract();
        consultantDTO.setDateJoined(LocalDate.now().minusMonths(1));
        consultantDTO.setDeleted(true);
        initializeContract(contract, LocalDate.now().minusMonths(1), consultantDTO);

        Assert.assertEquals(0, utilizationService.calcUtilPercentOfGivenMonth(LocalDate.now()), 0.0);
    }

    @Test
    public void returnFullUtilizationIfThereAreActiveConsultantsJoinedOnPreviousMonthsThatGivenDate() {
        Contract contract = new Contract();
        consultantDTO.setDateJoined(LocalDate.now().minusMonths(2));
        consultantDTO.setDeleted(false);
        consultantDTOS.add(consultantDTO);
        initializeContract(contract, LocalDate.now().minusMonths(2), consultantDTO);

        Assert.assertEquals(100, utilizationService.calcUtilPercentOfGivenMonth(LocalDate.now()), 0.0);
    }

    @Test
    public void returnFullUtilizationIfConsultantHasActiveContractOfGivenMonth() {
        Contract contract = new Contract();
        consultantDTO.setDateJoined(LocalDate.now().minusMonths(1));
        consultantDTO.setDeleted(false);
        consultantDTOS.add(consultantDTO);
        initializeContract(contract, (LocalDate.now().minusMonths(1)), consultantDTO);

        Assert.assertEquals(100, utilizationService.calcUtilPercentOfGivenMonth(LocalDate.now()), 0.0);
    }



    @Test
    public void returnFullAidedUtilizationIfConsultantHasActiveContractOfGivenMonth() {
        Contract contract = new Contract();
        consultantDTO.setDateJoined(LocalDate.now().minusMonths(5));
        consultantDTO.setDeleted(false);
        consultantDTOS.add(consultantDTO);
        initializeContract(contract, (LocalDate.now().minusMonths(1)), consultantDTO);

        Assert.assertEquals(100, utilizationService.calculateAidUtilPercentOfGivenMonth(LocalDate.now()), 0.0);
    }

    @Test
    public void returnZeroUtilizationIfConsultantHasOfficeContractOfGivenMonth() {
        Contract contract = new Contract();
        consultantDTO.setDateJoined(LocalDate.now().minusMonths(1));
        consultantDTO.setDeleted(false);
        consultantDTOS.add(consultantDTO);
        initializeContract(contract, (LocalDate.now().minusMonths(1)), consultantDTO);
        contract.setClientName(ContractService.OFFICE_NAME);

        Assert.assertEquals(0, utilizationService.calcUtilPercentOfGivenMonth(LocalDate.now()), 0.0);
    }

    @Test
    public void returnFullUtilizationIfClientContractStartedAndEndedOnGivenMonth() {
        Contract contract = new Contract();
        consultantDTO.setDateJoined(LocalDate.now().minusMonths(2));
        consultantDTO.setDeleted(false);
        consultantDTOS.add(consultantDTO);
        initializeContract(contract, (LocalDate.now().minusMonths(1)), consultantDTO);
        contract.setEndDate(LocalDate.now().minusMonths(1).plusDays(1));

        Assert.assertEquals(100, utilizationService.calcUtilPercentOfGivenMonth(LocalDate.now()), 0.0);
    }

    @Test
    public void returnZeroUtilizationIfOfficeContractStartedAndEndedOnGivenMonth() {
        Contract contract = new Contract();
        consultantDTO.setDateJoined(LocalDate.now().minusMonths(2));
        consultantDTO.setDeleted(false);
        consultantDTOS.add(consultantDTO);
        initializeContract(contract, (LocalDate.now().minusMonths(1)), consultantDTO);
        contract.setEndDate(LocalDate.now().minusMonths(1).plusDays(1));
        contract.setClientName(ContractService.OFFICE_NAME);

        Assert.assertEquals(0, utilizationService.calcUtilPercentOfGivenMonth(LocalDate.now()), 0.0);
    }



    @Test
    public void returnFullUtilizationIfClientContractStartedOnPreviousMonthsAndEndsOnGivenMonth() {
        Contract contract = new Contract();
        consultantDTO.setDateJoined(LocalDate.now().minusMonths(2));
        consultantDTO.setDeleted(false);
        consultantDTOS.add(consultantDTO);
        initializeContract(contract, (LocalDate.now().minusMonths(2)), consultantDTO);
        contract.setEndDate(LocalDate.now().minusMonths(1).plusDays(1));

        Assert.assertEquals(100, utilizationService.calcUtilPercentOfGivenMonth(LocalDate.now()), 0.0);
    }

    @Test
    public void returnZeroUtilizationIfOfficeContractStartedOnPreviousMonthsAndEndsOnGivenMonth() {
        Contract contract = new Contract();
        consultantDTO.setDateJoined(LocalDate.now().minusMonths(2));
        consultantDTO.setDeleted(false);
        consultantDTOS.add(consultantDTO);
        initializeContract(contract, (LocalDate.now().minusMonths(2)), consultantDTO);
        contract.setEndDate(LocalDate.now().minusMonths(1).plusDays(1));
        contract.setClientName(ContractService.OFFICE_NAME);

        Assert.assertEquals(0, utilizationService.calcUtilPercentOfGivenMonth(LocalDate.now()), 0.0);
    }

    @Test
    public void returnUtilizationIfConsultantHasActiveAndInactiveContractsOnGivenMonth() {
        Contract contract = new Contract();
        consultantDTO.setDateJoined(LocalDate.of(2020,3,1));
        consultantDTO.setDeleted(false);
        consultantDTOS.add(consultantDTO);
        initializeContract(contract, LocalDate.of(2020,3,1), consultantDTO);
        contract.setEndDate(LocalDate.of(2020,3,10));
        contract.setClientName(ContractService.OFFICE_NAME);
        contract.setActive(false);
        Contract contract2 = new Contract();
        initializeContract(contract2, LocalDate.of(2020,3,11), consultantDTO);

        Assert.assertEquals(67.74, utilizationService.calcUtilPercentOfGivenMonth(LocalDate.of(2020,4,1)), 0.01);
    }

    @Test
    public void returnFullAUtIfConsultantIsFullyAided() {
        consultantDTO.setDateJoined(LocalDate.now().minusMonths(2));
        consultantDTO.setDeleted(false);
        consultantDTOS.add(consultantDTO);

        Assert.assertEquals(100, utilizationService.calculateAidUtilPercentOfGivenMonth(LocalDate.now()), 0.0);
    }

    @Test
    public void returnAUtIfConsultantIsPartiallyAided() {
        consultantDTO.setDateJoined((LocalDate.of(2019,12,2)));
        consultantDTO.setDeleted(false);
        consultantDTOS.add(consultantDTO);

        Assert.assertEquals(6.45, utilizationService.calculateAidUtilPercentOfGivenMonth((LocalDate.of(2020,4,1))), 0.01);
    }

    @Test
    public void returnFullAUtIfConsultantJoinedOnCalculatedMonth() {
        consultantDTO.setDateJoined((LocalDate.of(2020, 3,5)));
        consultantDTO.setDeleted(false);
        consultantDTOS.add(consultantDTO);

        Assert.assertEquals(100, utilizationService.calculateAidUtilPercentOfGivenMonth((LocalDate.of(2020,4,1))), 0.01);
    }

    @Test
    public void returnFullUtilIfPartiallyAidedConsultantHasActiveContractBeforeGivenDate() {
        Contract contract = new Contract();
        consultantDTO.setDateJoined((LocalDate.of(2019,12,6)));
        consultantDTO.setDeleted(false);
        consultantDTOS.add(consultantDTO);
        initializeContract(contract, LocalDate.of(2020,2,3), consultantDTO);

        Assert.assertEquals(100, utilizationService.calculateAidUtilPercentOfGivenMonth((LocalDate.of(2020,4,1))), 0.01);
    }

    @Test
    public void returnUtilizationForPartialAidedConsultantWithActiveContract() {
        Contract contract = new Contract();
        consultantDTO.setDateJoined((LocalDate.of(2019,12,3)));
        consultantDTO.setDeleted(false);
        consultantDTOS.add(consultantDTO);
        initializeContract(contract, LocalDate.of(2020,3,9), consultantDTO);

        Assert.assertEquals(83.87, utilizationService.calculateAidUtilPercentOfGivenMonth((LocalDate.of(2020,4,1))), 0.01);
    }

    @Test
    public void returnFullUtilizationIfPartialAidedConsultantDateOverlapsStartedActiveContract() {
        Contract contract = new Contract();
        consultantDTO.setDateJoined((LocalDate.of(2019,12,6)));
        consultantDTO.setDeleted(false);
        consultantDTOS.add(consultantDTO);
        initializeContract(contract, LocalDate.of(2020,3,3), consultantDTO);

        Assert.assertEquals(100, utilizationService.calculateAidUtilPercentOfGivenMonth((LocalDate.of(2020,4,1))), 0.01);
    }

    @Test
    public void returnsAllSavedUtilization(){
        Utilization utilization = new Utilization();
        utilizationList.add(utilization);
        Assert.assertEquals(1,utilizationService.getAllUtilization().size());
    }

    @Test
    public void returnsCurrentSavedUtilization(){
        Utilization utilization = new Utilization();
        utilization.setDate(LocalDate.now());
        utilizationList.add(utilization);
        Mockito.when(utilizationRepository.findByDate(Mockito.any())).thenReturn(Optional.of(utilization));

        Assert.assertEquals(utilization.getDate(),utilizationService.getCurrentCompleteUtil().getDate());
    }

    @Test
    public void calculatesAndReturnsCurrentUtilizationIfNotSaved(){
        Mockito.when(utilizationRepository.findByDate(Mockito.any())).thenReturn(Optional.empty());

        Assert.assertEquals(LocalDate.now(),utilizationService.getCurrentCompleteUtil().getDate());
    }

    @Test
    public void calculatesAndReturnsUtilizationForThreeMonthsIfNotSaved(){
        LocalDate firstDayOfPreviousMonth = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().minusMonths(1).getMonthValue(), 1);
        Mockito.when(utilizationRepository.findByDate(firstDayOfPreviousMonth)).thenReturn(Optional.empty());
        Utilization oneMonthAgoUtilization = new Utilization();
        oneMonthAgoUtilization.setUt(90.0);
        oneMonthAgoUtilization.setAidedUt(100.0);
        Utilization twoMonthsAgoUtilization = new Utilization();
        twoMonthsAgoUtilization.setUt(70.0);
        twoMonthsAgoUtilization.setAidedUt(80.0);
        Mockito.when(utilizationRepository.findByDate(firstDayOfPreviousMonth.minusMonths(1))).thenReturn(Optional.of(oneMonthAgoUtilization));
        Mockito.when(utilizationRepository.findByDate(firstDayOfPreviousMonth.minusMonths(2))).thenReturn(Optional.of(twoMonthsAgoUtilization));

        Assert.assertTrue(utilizationService.getCurrentCompleteUtil().getThreeMonthsUt()<=100);
    }

    @Test
    public void calculateAndSaveCurrentUtilizationIfNotSaved(){
        Mockito.when(utilizationRepository.findByDate(Mockito.any())).thenReturn(Optional.empty());

        utilizationService.saveUtToDb();
        Mockito.verify(utilizationRepository,Mockito.times(1)).saveAndFlush(Mockito.any());
    }

    @Test
    public void recalculateAndSaveCurrentUtilizationIfExists(){
        Utilization utilization = new Utilization();
        Mockito.when(utilizationRepository.findByDate(Mockito.any())).thenReturn(Optional.of(utilization));

        utilizationService.saveUtToDb();
        Mockito.verify(utilizationRepository,Mockito.times(1)).saveAndFlush(Mockito.any());
    }

    @Test
    public void recalculateAndSaveAllUtilization(){
        Contract contract = new Contract();
        consultantDTO.setDateJoined((LocalDate.now().minusMonths(2)));
        consultantDTO.setDeleted(false);
        consultantDTOS.add(consultantDTO);
        initializeContract(contract, (LocalDate.now().minusMonths(2)), consultantDTO);

        utilizationService.reCalcAllUtil();
        Mockito.verify(utilizationRepository,Mockito.times(2)).saveAndFlush(Mockito.any());
    }
}