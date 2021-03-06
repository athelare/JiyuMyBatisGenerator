package com.ljy.writer;

import com.ljy.dbObject.DBTable;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class EntityOrMapperWriter {
    /*public static void write(
            DBTable dbTable,
            String templateDirectory,
            String entityTemplateName,
            String mapperTemplateName
    ) throws IOException, TemplateException {
        BufferedWriter entityWriter = new BufferedWriter(new FileWriter(new File(dbTable.getEntityDirPath()+dbTable.getPascalEntityName()+".java")));
        BufferedWriter mapperWriter = new BufferedWriter(new FileWriter(new File(dbTable.getDaoDirPath()+dbTable.getPascalMapperName()+".java")));
        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDirectoryForTemplateLoading(new File(templateDirectory));
        configuration.setDefaultEncoding("utf-8");
        Template entityTemplate = configuration.getTemplate(entityTemplateName);
        Template mapperTemplate = configuration.getTemplate(mapperTemplateName);
        Map<String, Object> dataModel = new HashMap<>();

        dataModel.put("table",dbTable);

        entityTemplate.process(dataModel,entityWriter);
        mapperTemplate.process(dataModel,mapperWriter);

        entityWriter.close();
        mapperWriter.close();
    }*/


    public static void write(
            DBTable dbTable,
            String fileName,
            Map<String, String> properties
    ) throws IOException, TemplateException {
        BufferedWriter entityWriter = new BufferedWriter(new FileWriter(new File(fileName)));
        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDirectoryForTemplateLoading(new File(properties.get("templateDirectory")));
        configuration.setDefaultEncoding("utf-8");
        Template entityTemplate = configuration.getTemplate(properties.get("templateName"));
        Map<String, Object> dataModel = new HashMap<>();

        dataModel.put("table",dbTable);

        entityTemplate.process(dataModel,entityWriter);

        entityWriter.close();
    }
}
