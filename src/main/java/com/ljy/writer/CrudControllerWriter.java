package com.ljy.writer;

import com.ljy.dbObject.DBTable;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrudControllerWriter {
    public static void writeControllerClass(List<DBTable> tables) throws IOException, TemplateException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(tables.get(0).getEntityDirPath()+"crudController.java")));
        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDirectoryForTemplateLoading(new File("src\\main\\resources\\"));
        configuration.setDefaultEncoding("utf-8");
        Template entityTemplate = configuration.getTemplate("crudControllerTemplate.java");

        Map dataModel = new HashMap<>();

        dataModel.put("tables",tables);
        dataModel.put("author","Jiyu");
        dataModel.put("comment","hello");
        dataModel.put("time","2020-01-19");

        entityTemplate.process(dataModel,writer);

        writer.close();

    }
}
