package com.consultant.model.utils;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

public class CsvUtils {
    private CsvUtils() {
    }

    public static <T> String writeCsv(List<T> entitiesList, Class<T> beanClazz, Writer writer) throws Exception {

        CustomMappingStrategy<T> mappingStrategy = new CustomMappingStrategy<T>();
        mappingStrategy.setType(beanClazz);

        StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                .withMappingStrategy(mappingStrategy)
                .build();
        beanToCsv.write(entitiesList);

        String csv = writer.toString();
        writer.close();
        return csv;
    }

    public static <T> List<T> getEntitiesFromCsv(Class<T> beanClazz, MultipartFile file, Boolean skipHeader) throws Exception {
        Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

        CustomMappingStrategy<T> mappingStrategy = new CustomMappingStrategy<T>();
        mappingStrategy.setType(beanClazz);
        int skipLines = skipHeader?1:0;

        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                .withMappingStrategy(mappingStrategy)
                .withSkipLines(skipLines)
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        List<T> objects = csvToBean.parse();

        reader.close();
        return objects;
    }

    public static <T> void downloadCsv(List<T> entitiesList, Class<T> beanClazz, HttpServletResponse response, String filename) throws Exception {
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        writeCsv(entitiesList, beanClazz, response.getWriter());
    }
}
