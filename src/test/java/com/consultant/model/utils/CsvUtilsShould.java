package com.consultant.model.utils;

import com.consultant.model.dto.ConsultantDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class CsvUtilsShould {

    private Writer writer = new StringWriter();

    @Before
    public void setUp() {

    }

    @Test
    public void containEntityValues() throws Exception {
        ConsultantDTO consultantDTO1 = new ConsultantDTO();
        consultantDTO1.setFirstName("consultantDTO1");
        ArrayList<ConsultantDTO> list = new ArrayList<>();
        list.add(consultantDTO1);


        String s = CsvUtils.writeCsv(list, ConsultantDTO.class, writer);

        Assert.assertTrue(s.contains("consultantDTO1"));
    }

    @Test
    public void containHeaders() throws Exception {
        ConsultantDTO consultantDTO1 = new ConsultantDTO();
        consultantDTO1.setFirstName("consultantDTO1");
        ArrayList<ConsultantDTO> list = new ArrayList<>();
        list.add(consultantDTO1);
        Writer writer = new StringWriter();

        String s = CsvUtils.writeCsv(list, ConsultantDTO.class, writer);

        System.out.println(s);
        Assert.assertTrue(s.contains("First Name"));
    }
}