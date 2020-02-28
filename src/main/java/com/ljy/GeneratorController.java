package com.ljy;

import com.ljy.dbObject.DBTable;
import com.ljy.pojo.GeneratorConfig;
import com.ljy.writer.ControllerDocumentWriter;
import com.ljy.writer.EntityOrMapperWriter;
import com.ljy.writer.MyBatisXMLWriter;
import freemarker.template.TemplateException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class GeneratorController {

    private DatabaseMetaData dbMetaData;
    private String catalog_name;
    private GeneratorConfig config;

    private void readGeneratorConfig(String path) throws DocumentException {
        Document document = new SAXReader().read(new File(path));

        config = new GeneratorConfig();
        Iterator<Element> extraProperties;
        Map<String,String> propertyPairs;

        Element context = document.getRootElement().element("context");
        Element jdbcConnection = context.element("jdbcConnection");
        Element javaModelGenerator = context.element("javaModelGenerator");
        Element javaClientGenerator = context.element("javaClientGenerator");
        Element sqlMapGenerator = context.element("sqlMapGenerator");


        config.getJdbcConnection().setDriverClass(jdbcConnection.attributeValue("driverClass"));
        config.getJdbcConnection().setConnectionURL(jdbcConnection.attributeValue("connectionURL"));
        config.getJdbcConnection().setUserId(jdbcConnection.attributeValue("userId"));
        config.getJdbcConnection().setPassword(jdbcConnection.attributeValue("password"));

        config.getJavaModelGenerator().setTargetPackage(javaModelGenerator.attributeValue("targetPackage"));
        config.getJavaModelGenerator().setTargetProject(javaModelGenerator.attributeValue("targetProject"));
        extraProperties = javaModelGenerator.elementIterator("property");
        propertyPairs = new HashMap<>();
        while(extraProperties.hasNext()){
            Element tmp = extraProperties.next();
            propertyPairs.put(tmp.attributeValue("name"),tmp.attributeValue("value"));
        }
        config.getJavaModelGenerator().setProperties(propertyPairs);


        config.getJavaClientGenerator().setTargetPackage(javaClientGenerator.attributeValue("targetPackage"));
        config.getJavaClientGenerator().setTargetProject(javaClientGenerator.attributeValue("targetProject"));
        extraProperties = javaClientGenerator.elementIterator("property");
        propertyPairs = new HashMap<>();
        while(extraProperties.hasNext()){
            Element tmp = extraProperties.next();
            propertyPairs.put(tmp.attributeValue("name"),tmp.attributeValue("value"));
        }
        config.getJavaClientGenerator().setProperties(propertyPairs);


        config.getSqlMapGenerator().setTargetPackage(sqlMapGenerator.attributeValue("targetPackage"));
        config.getSqlMapGenerator().setTargetProject(sqlMapGenerator.attributeValue("targetProject"));

    }

    private void getDatabaseMetaData(){

        try {
            Class.forName(config.getJdbcConnection().getDriverClass());
            Connection con = DriverManager.getConnection(config.getJdbcConnection().getConnectionURL(), config.getJdbcConnection().getUserId(), config.getJdbcConnection().getPassword());
            dbMetaData = con.getMetaData();
            catalog_name = con.getCatalog();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void getAllTableList(String catalogName) throws SQLException, IOException, TemplateException {

        String[] types = { "TABLE" };
        ResultSet rs = dbMetaData.getTables(catalogName, null, "%", types);
        List<DBTable> tables = new ArrayList<>();
        while (rs.next()) {
            String tableName = rs.getString("TABLE_NAME"); // 表名
            //String tableType = rs.getString("TABLE_TYPE"); // 表类型
            String remarks = rs.getString("REMARKS"); // 表备注

            DBTable dbTable = new DBTable(config, dbMetaData,catalogName,tableName, remarks);


            tables.add(dbTable);
            MyBatisXMLWriter.writeMyBatisXML(dbTable);
            EntityOrMapperWriter.write(
                    dbTable,
                    config.getJavaClientGenerator().getProperties().get("templateDirectory"),
                    config.getJavaModelGenerator().getProperties().get("templateName"),
                    config.getJavaClientGenerator().getProperties().get("templateName")
            );

            if(dbTable.hasComposeKey()){
                EntityOrMapperWriter.write1(
                        new DBTable(dbTable),
                        dbTable.getEntityDirPath()+dbTable.getPascalEntityName()+"PrimaryKey.java",
                        config.getJavaModelGenerator().getProperties());
            }


        }
        ControllerDocumentWriter.writeControllerClass(
                tables,
                config.getJavaClientGenerator().getProperties()
                );
    }

    public static void main(String[]args) throws SQLException, IOException, TemplateException, DocumentException {
        GeneratorController rt = new GeneratorController();
        rt.readGeneratorConfig("src\\main\\resources\\generatorConfig.xml");
        rt.getDatabaseMetaData();
        rt.getAllTableList(rt.catalog_name);
    }

}

