package com.consultant.model.dto;

import com.consultant.model.enums.TechnologyType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnologyDTO {
    private Long id;

    private String name;

    private TechnologyType type;
}
