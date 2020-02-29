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

public class ControllerDocumentWriter {
    public static void writeControllerClass(List<DBTable> tables, Map<String,String> properties)
            throws IOException, TemplateException {

        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDirectoryForTemplateLoading(new File(properties.get("templateDirectory")));
        configuration.setDefaultEncoding("utf-8");
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
                tables.get(0).getEntityDirPath()+"CrudController.java")));
        BufferedWriter writer1 = new BufferedWriter(new FileWriter(new File(
                tables.get(0).getEntityDirPath()+"API.md")));
        Template controllerTemplate = configuration.getTemplate(properties.get("controllerTemplateName"));
        Template documentTemplate = configuration.getTemplate(properties.get("documentTemplateName"));

        Map<String, Object> dataModel = new HashMap<>();

        dataModel.put("tables",tables);
        dataModel.put("urlPrefix",properties.get("urlPrefix"));

        controllerTemplate.process(dataModel,writer);
        documentTemplate.process(dataModel,writer1);

        writer.close();
        writer1.close();

    }
}
