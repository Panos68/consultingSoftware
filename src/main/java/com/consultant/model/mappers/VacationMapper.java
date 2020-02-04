package com.consultant.model.mappers;

import com.consultant.model.dto.VacationDTO;
import com.consultant.model.entities.Vacation;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VacationMapper {
    VacationMapper INSTANCE = Mappers.getMapper(VacationMapper.class);

    VacationDTO vacationToVacationDTO(Vacation vacation);

    Vacation vacationDTOToVacation(VacationDTO vacationDTO);
}