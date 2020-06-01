package com.consultant.model.services;

import com.consultant.model.dto.ConsultantDTO;
import com.consultant.model.dto.ContractDTO;
import com.consultant.model.dto.DayAssignStatusDTO;
import com.consultant.model.dto.UtilizationDTO;
import com.consultant.model.entities.Utilization;
import com.consultant.model.entities.Vacation;
import com.consultant.model.mappers.UtilizationMapper;
import com.consultant.model.repositories.UtilizationRepository;
import com.consultant.model.services.impl.ConsultantService;
import com.consultant.model.services.impl.VacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@Service
public class UtilizationService {

    private UtilizationRepository utilizationRepository;

    private ConsultantService consultantService;

    private VacationService vacationService;

    @Autowired
    public UtilizationService(UtilizationRepository utilizationRepository, ConsultantService consultantService,
                              VacationService vacationService) {
        this.utilizationRepository = utilizationRepository;
        this.consultantService = consultantService;
        this.vacationService = vacationService;
    }

    /**
     * Calculates every 1st of the month at 9am the utilization and saves it to the db
     */
    @Scheduled(cron = "0 0 9 1 * ?")
    public void saveUtToDb() {
        LocalDate firstDayOfPreviousMonth = getFirstDayOfPreviousMonth(LocalDate.now());
        Utilization utilization = getUtilizationObject(firstDayOfPreviousMonth);
        calcCompleteUtil(firstDayOfPreviousMonth, utilization);
        utilizationRepository.saveAndFlush(utilization);
    }

    private LocalDate getFirstDayOfPreviousMonth(LocalDate date) {
        int monthValue = date.minusMonths(1).getMonthValue();
        int year = monthValue == 12 ? date.getYear() - 1 : date.getYear();
        return LocalDate.of(year, monthValue, 1);
    }

    public Set<UtilizationDTO> getAllUtilization() {
        List<Utilization> utilizationList = utilizationRepository.findAll();
        Set<UtilizationDTO> utilizationDTOS = new LinkedHashSet<>();
        utilizationList.forEach(utilization -> {
            UtilizationDTO utilizationDTO = UtilizationMapper.INSTANCE.utilizationToUtilizationDTO(utilization);
            utilizationDTOS.add(utilizationDTO);
        });
        return utilizationDTOS;
    }

    /**
     * Deletes all ut from the db and calculates again since the day that the first consultant joined until today.
     */
    public void reCalcAllUtil() {
        Set<ConsultantDTO> consultants = consultantService.getAll();
        ConsultantDTO firstConsultantJoinedDTO = consultants.stream().min((Comparator.comparing(ConsultantDTO::getDateJoined))).get();
        LocalDate earliestCalculatingDate = firstConsultantJoinedDTO.getDateJoined().plusMonths(1);
        LocalDate firstDayOfCalculatingMonth = LocalDate.of(earliestCalculatingDate.getYear(), earliestCalculatingDate.getMonthValue(), 1);
        utilizationRepository.deleteAll();
        while (!firstDayOfCalculatingMonth.isAfter(LocalDate.now().plusDays(1))) {
            Utilization utilization = getUtilizationObject(firstDayOfCalculatingMonth);
            utilization.setDate(firstDayOfCalculatingMonth);
            calcCompleteUtil(firstDayOfCalculatingMonth, utilization);
            utilizationRepository.saveAndFlush(utilization);
            firstDayOfCalculatingMonth = firstDayOfCalculatingMonth.plusMonths(1);
        }
    }

    /**
     * Returns the utilization for the given date if it exists on the db or creates a new one if it doesn't
     *
     * @param firstDayOfPreviousMonth given date to search in the db
     * @return utilization object
     */
    private Utilization getUtilizationObject(LocalDate firstDayOfPreviousMonth) {
        Optional<Utilization> optionalUtilization = utilizationRepository.findByDate(firstDayOfPreviousMonth);
        Utilization utilization;
        if (optionalUtilization.isPresent()) {
            utilization = optionalUtilization.get();
        } else {
            utilization = new Utilization();
            utilization.setDate(firstDayOfPreviousMonth);
        }
        return utilization;
    }

    /**
     * Calculates the complete utilization object for given month and adds it to the utilization parameter
     */
    private Utilization calcCompleteUtil(LocalDate firstDayOfGivenMonth, Utilization utilization) {
        calcUtilPercentOfGivenMonth(firstDayOfGivenMonth, utilization);
        calcThreeMonthsUtil(firstDayOfGivenMonth, utilization);
        return utilization;
    }

    void calcUtilPercentOfGivenMonth(LocalDate givenDate, Utilization utilization) {
        Set<ConsultantDTO> consultants = consultantService.getAll();


        int monthMaxDays = getMonthMaxDays(givenDate.minusMonths(1));

        LocalDate firstDayOfGivenMonth = LocalDate.of(givenDate.getYear(), givenDate.getMonthValue(), 1);

        List<ConsultantDTO> activeConsultantsForGivenMonth = consultants.stream()
                .filter(consultantDTO -> consultantDTO.getDateJoined().isBefore(firstDayOfGivenMonth)
                        && hasContractOnGivenMonth(firstDayOfGivenMonth, consultantDTO))
                .collect(Collectors.toList());

        AtomicLong aidedDays = new AtomicLong();
        AtomicLong nonAidedAssignedDays = new AtomicLong();
        AtomicLong assignedDays = new AtomicLong();
        AtomicLong maxAssignedDays = new AtomicLong();

        activeConsultantsForGivenMonth
                .forEach(consultantDTO -> {
                            List<DayAssignStatusDTO> dayAssignStatusList = new ArrayList<>();

                            LocalDate calculatedMonthDate = getFirstDayOfPreviousMonth(firstDayOfGivenMonth);

                            for (int day = 1; day <= monthMaxDays; day++) {
                                DayAssignStatusDTO dayAssignStatusDTO = new DayAssignStatusDTO();

                                Optional<Vacation> longTermAbsenceForGivenDate =
                                        getLongTermAbsenceForGivenDate(calculatedMonthDate, consultantDTO);
                                if (longTermAbsenceForGivenDate.isPresent()) {
                                    dayAssignStatusDTO.setLongTermLeave(true);
                                    continue;
                                }

                                Optional<ContractDTO> contractOnGivenDay = getContractForGivenDate(calculatedMonthDate, consultantDTO);
                                if (contractOnGivenDay.isPresent()) {
                                    if (contractOnGivenDay.get().getClient() == null) {
                                        dayAssignStatusDTO.setOffice(true);
                                    } else {
                                        dayAssignStatusDTO.setAssigned(true);
                                    }
                                    maxAssignedDays.addAndGet(1);
                                    if (consultantDTO.getDateJoined().plusMonths(3).isAfter(calculatedMonthDate)) {
                                        dayAssignStatusDTO.setAided(true);
                                    }
                                }
                                dayAssignStatusList.add(dayAssignStatusDTO);
                                calculatedMonthDate = calculatedMonthDate.plusDays(1);
                            }
                            aidedDays.addAndGet(dayAssignStatusList.stream().filter(DayAssignStatusDTO::isAided).count());
                            assignedDays.addAndGet(dayAssignStatusList.stream().filter(DayAssignStatusDTO::isAssigned).count());
                            nonAidedAssignedDays.addAndGet(dayAssignStatusList.stream()
                                    .filter(d -> !d.isAided() && d.isAssigned())
                                    .count());
                        }
                );

        if (assignedDays.get() > 0 && maxAssignedDays.doubleValue() > 0) {
            utilization.setUt((assignedDays.doubleValue() / maxAssignedDays.doubleValue()) * 100);
        } else {
            utilization.setUt(0.0);
        }

        double assignedAidedDays = aidedDays.doubleValue() + nonAidedAssignedDays.doubleValue();
        if (assignedAidedDays > 0 && maxAssignedDays.doubleValue() > 0) {
            utilization.setAidedUt(assignedAidedDays / maxAssignedDays.doubleValue() * 100);
        } else {
            utilization.setAidedUt(0.0);
        }
    }

    private boolean hasContractOnGivenMonth(LocalDate firstDayOfGivenMonth, ConsultantDTO consultantDTO) {
        return consultantDTO.getContracts().stream().
                anyMatch(contract -> contract.getEndDate() == null ||
                        contract.getEndDate().isAfter(firstDayOfGivenMonth.minusMonths(1)));
    }

    /**
     * calculates three months Aid Ut and three months Ut and adds it to the utilization
     *
     * @param firstDayOfGivenMonth first day of month to calculate for
     * @param utilization          object to add the calculated values
     */
    private void calcThreeMonthsUtil(LocalDate firstDayOfGivenMonth, Utilization utilization) {
        Optional<Utilization> optionalTwoMonthsAgoUtilization = utilizationRepository.findByDate(firstDayOfGivenMonth.minusMonths(1));
        Optional<Utilization> optionalThreeMonthsAgoUtilization = utilizationRepository.findByDate(firstDayOfGivenMonth.minusMonths(2));
        if (optionalTwoMonthsAgoUtilization.isPresent() && optionalThreeMonthsAgoUtilization.isPresent()) {
            int oneMonthAgoMaxDays = getMonthMaxDays(firstDayOfGivenMonth.minusMonths(1));
            int twoMonthsAgoMaxDays = getMonthMaxDays(firstDayOfGivenMonth.minusMonths(2));
            int threeMonthsAgoMaxDays = getMonthMaxDays(firstDayOfGivenMonth.minusMonths(3));
            int lastThreeMonthsTotalDays = oneMonthAgoMaxDays + twoMonthsAgoMaxDays + threeMonthsAgoMaxDays;
            utilization.setThreeMonthsUt((utilization.getUt() * oneMonthAgoMaxDays + optionalTwoMonthsAgoUtilization.get().getUt()
                    * twoMonthsAgoMaxDays + optionalThreeMonthsAgoUtilization.get().getUt() * threeMonthsAgoMaxDays) / lastThreeMonthsTotalDays);
            utilization.setThreeMonthsAidedUt((utilization.getAidedUt() * oneMonthAgoMaxDays +
                    optionalTwoMonthsAgoUtilization.get().getAidedUt() * twoMonthsAgoMaxDays +
                    optionalThreeMonthsAgoUtilization.get().getAidedUt() * threeMonthsAgoMaxDays) / lastThreeMonthsTotalDays);
        }
    }

    private Optional<ContractDTO> getContractForGivenDate(LocalDate calculatedMonthDate, ConsultantDTO consultantDTO) {
        return consultantDTO.getContracts()
                .stream()
                .filter(c -> (c.getStartedDate().isBefore(calculatedMonthDate) ||
                        c.getStartedDate().isEqual(calculatedMonthDate)) &&
                        (c.getEndDate() == null ||
                                (c.getEndDate().isAfter(calculatedMonthDate) ||
                                        c.getEndDate().isEqual(calculatedMonthDate))))
                .findFirst();
    }

    private Optional<Vacation> getLongTermAbsenceForGivenDate(LocalDate calculatedMonthDate, ConsultantDTO consultantDTO) {
        List<Vacation> vacationsOfConsultant = vacationService.getVacationsOfConsultant(consultantDTO.getId());

        return vacationsOfConsultant
                .stream()
                .filter(vacation -> vacation.getIsLongTerm() &&
                        (vacation.getStartingDate().isBefore(calculatedMonthDate) ||
                                vacation.getStartingDate().isEqual(calculatedMonthDate)) &&
                        (vacation.getEndDate() == null ||
                                (vacation.getEndDate().isAfter(calculatedMonthDate) ||
                                        vacation.getEndDate().isEqual(calculatedMonthDate))))
                .findFirst();
    }

    private int getMonthMaxDays(LocalDate givenDate) {
        YearMonth yearMonth = YearMonth.of(givenDate.getYear(), givenDate.getMonth());
        return yearMonth.lengthOfMonth();
    }

}
