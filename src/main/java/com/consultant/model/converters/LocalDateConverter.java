package com.consultant.model.converters;

import com.opencsv.bean.AbstractBeanField;
import org.apache.commons.validator.GenericValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter extends AbstractBeanField {
    @Override
    protected LocalDate convert(String s) {
        if (GenericValidator.isDate(s, "yyyy-MM-dd", true)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(s, formatter);
        } else return null;
    }
}