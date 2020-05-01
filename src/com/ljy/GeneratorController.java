package com.ljy;

import com.ljy.dbObject.DBTable;
import com.ljy.pojo.GeneratorConfig;
import com.ljy.pojo.SpecifiedTable;
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
    private String catalogName;
    private GeneratorConfig config;

    /**
     * 在generatorConfig.xml文件中读取配置信息
     * @param path 配置文件路径
     * @throws DocumentException xxx
     */
    private void readGeneratorConfig(String path) throws DocumentException {
        Document document = new SAXReader().read(new File(path));

        config = new GeneratorConfig();
        Iterator<Element> extraProperties;
        Iterator<Element> specifiedTable;
        Map<String,String> propertyPairs;

        Element context = document.getRootElement().element("context");
        Element jdbcConnection = context.element("jdbcConnection");
        Element javaModelGenerator = context.element("javaModelGenerator");
        Element javaClientGenerator = context.element("javaClientGenerator");
        Element sqlMapGenerator = context.element("sqlMapGenerator");
        specifiedTable = context.elementIterator("table");


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

        while(specifiedTable.hasNext()){
            Element tb = specifiedTable.next();
            config.getTableName2DomainObjectMap()
                    .put(tb.attributeValue("tableName"),tb.attributeValue("domainObjectName"));

        }


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

    /**
     * 根据config文件的信息连接数据库，并且生成“DatabaseMetadata"变量供
     */
    private void getDatabaseMetaData(){

        try {
            Class.forName(config.getJdbcConnection().getDriverClass());
            Connection con = DriverManager.getConnection(config.getJdbcConnection().getConnectionURL(), config.getJdbcConnection().getUserId(), config.getJdbcConnection().getPassword());
            dbMetaData = con.getMetaData();
            catalogName = con.getCatalog();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据DatabaseMetadata以及catalogName(成员变量）获取指定数据库所有的表格信息
     * @throws SQLException xxx
     * @throws IOException xxx
     * @throws TemplateException xxx
     */
    public void getAllTableList() throws SQLException, IOException, TemplateException {

        String[] types = { "TABLE" };
        ResultSet rs = dbMetaData.getTables(catalogName, null, "%", types);
        List<DBTable> tables = new ArrayList<>();
        while (rs.next()) {
            String tableName = rs.getString("TABLE_NAME"); // 表名
            String remarks = rs.getString("REMARKS"); // 表备注

            DBTable dbTable = new DBTable(config, dbMetaData,catalogName,tableName, remarks);


            tables.add(dbTable);
            //写入MyBatis的XML映射文件
            MyBatisXMLWriter.writeMyBatisXML(dbTable);
            //根据模板写入Entity文件
            EntityOrMapperWriter.write(
                    dbTable,
                    dbTable.getEntityDirPath()+dbTable.getPascalEntityName()+".java",
                    config.getJavaModelGenerator().getProperties());
            //根据模板写入DAO文件
            EntityOrMapperWriter.write(
                    dbTable,
                    dbTable.getDaoDirPath()+dbTable.getPascalMapperName()+".java",
                    config.getJavaClientGenerator().getProperties());
            /*EntityOrMapperWriter.write(
                    dbTable,
                    config.getJavaClientGenerator().getProperties().get("templateDirectory"),
                    config.getJavaModelGenerator().getProperties().get("templateName"),
                    config.getJavaClientGenerator().getProperties().get("templateName")
            );*/
            //处理多主键的情况，按照其他生成软件的方式，将这些复合主键生成一个class
            if(dbTable.hasComposeKey()){
                EntityOrMapperWriter.write(
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

    /**
     * 程序入口
     * @param args xxx
     * @throws SQLException xxx
     * @throws IOException xxx
     * @throws TemplateException xxx
     * @throws DocumentException xxx
     */
    public static void main(String[]args) throws SQLException, IOException, TemplateException, DocumentException {
        GeneratorController rt = new GeneratorController();
        rt.readGeneratorConfig("src\\main\\resources\\generatorConfig.xml");
        rt.getDatabaseMetaData();
        rt.getAllTableList();
    }

    public static void GenerateFromFile(String configFile){
        GeneratorController rt = new GeneratorController();
        try {
            rt.readGeneratorConfig(configFile);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        rt.getDatabaseMetaData();

        try {
            rt.getAllTableList();
        } catch (SQLException | IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

}

