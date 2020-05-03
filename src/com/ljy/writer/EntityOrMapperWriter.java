package com.ljy.writer;

import com.ljy.dbObject.DBTable;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EntityOrMapperWriter {

    public static void write(
            DBTable dbTable,
            String fileName
    ) throws IOException, TemplateException {
        BufferedWriter entityWriter = new BufferedWriter(new FileWriter(new File(fileName)));
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_30);
        configuration.setDirectoryForTemplateLoading(new File(Objects.requireNonNull(EntityOrMapperWriter.class.getClassLoader().getResource("")).getPath()+"/templates"));
        configuration.setDefaultEncoding("utf-8");
        Template entityTemplate = configuration.getTemplate(fileName.contains("Mapper")?"mapperTemplate.ftl":"entityTemplate.ftl");
        Map<String, Object> dataModel = new HashMap<>();

        dataModel.put("table",dbTable);

        entityTemplate.process(dataModel,entityWriter);

        entityWriter.close();
    }
}
