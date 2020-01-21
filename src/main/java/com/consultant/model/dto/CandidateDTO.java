package com.consultant.model.dto;

import lombok.Data;

@Data
public class CandidateDTO {

    public CandidateDTO() {
    }

    private Long id;

    private String linkedinUrl;

    private String role;

    private String company;

    private String location;

    private String consultant;

    private String diverse;

    private String source;

    private String comment;
}
