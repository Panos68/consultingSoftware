package com.consultant.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DayAssignStatusDTO {
    private boolean longTermLeave;

    private boolean aided;

    private boolean office;

    private boolean assigned;
}
