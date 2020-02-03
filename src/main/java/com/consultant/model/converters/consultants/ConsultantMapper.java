package com.consultant.model.converters.consultants;

import com.consultant.model.dto.ConsultantDTO;
import com.consultant.model.entities.Consultant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConsultantMapper {
    ConsultantMapper INSTANCE = Mappers.getMapper( ConsultantMapper.class );

    ConsultantDTO consultantToConsultantDTO(Consultant consultant);

    Consultant consultantDTOToConsultant(ConsultantDTO consultantDTO);
}