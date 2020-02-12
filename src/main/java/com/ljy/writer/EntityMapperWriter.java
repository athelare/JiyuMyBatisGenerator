package com.ljy.writer;

import com.ljy.dbObject.DBTable;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class EntityMapperWriter {
    public static void writeEntityClass(DBTable dbTable) throws IOException, TemplateException {
        BufferedWriter entityWriter = new BufferedWriter(new FileWriter(new File(dbTable.getEntityDirPath()+dbTable.getPascalEntityName()+".java")));
        BufferedWriter mapperWriter = new BufferedWriter(new FileWriter(new File(dbTable.getMapperDirPath()+dbTable.getPascalMapperName()+".java")));
        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDirectoryForTemplateLoading(new File("src\\main\\resources\\"));
        configuration.setDefaultEncoding("utf-8");
        Template entityTemplate = configuration.getTemplate("entityTemplate.java");
        Template mapperTemplate = configuration.getTemplate("mapperTemplate.java");
        Map dataModel = new HashMap<>();

        dataModel.put("table",dbTable);
        dataModel.put("author","Jiyu");
        dataModel.put("comment","hello");
        dataModel.put("time","2020-01-19");

        entityTemplate.process(dataModel,entityWriter);
        mapperTemplate.process(dataModel,mapperWriter);

        entityWriter.close();
        mapperWriter.close();
    }
}
