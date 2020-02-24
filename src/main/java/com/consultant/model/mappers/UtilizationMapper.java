package com.consultant.model.mappers;

import com.consultant.model.dto.UtilizationDTO;
import com.consultant.model.entities.Utilization;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UtilizationMapper {
        UtilizationMapper INSTANCE = Mappers.getMapper(UtilizationMapper.class);

        UtilizationDTO utilizationToUtilizationDTO(Utilization utilization);
}
