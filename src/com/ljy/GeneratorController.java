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
    private String catalogName;

    /**
     * 在generatorConfig.xml文件中读取配置信息
     * @param path 配置文件路径
     * @throws DocumentException xxx
     */
    private GeneratorConfig readGeneratorConfig(String path) throws DocumentException {
        Document document = new SAXReader().read(new File(path));

        GeneratorConfig config = new GeneratorConfig();
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

        return config;

    }

    /**
     * 根据config文件的信息连接数据库，并且生成“DatabaseMetadata"变量供
     */
    private DatabaseMetaData getDatabaseMetaData(GeneratorConfig config){

        try {
            Class.forName(config.getJdbcConnection().getDriverClass());
            Connection con = DriverManager.getConnection(
                    config.getJdbcConnection().getConnectionURL(),
                    config.getJdbcConnection().getUserId(),
                    config.getJdbcConnection().getPassword()
            );
            catalogName = con.getCatalog();
            return con.getMetaData();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据DatabaseMetadata以及catalogName(成员变量）获取指定数据库所有的表格信息
     * @param config 用户配置信息
     * @param metaData 数据库元数据
     * @return 所有数据库表
     * @throws SQLException xxx
     */
    public List<DBTable> retrieveDatabaseMetadataInformation(GeneratorConfig config, DatabaseMetaData metaData) throws SQLException {

        String[] types = { "TABLE" };
        ResultSet rs = metaData.getTables(catalogName, null, "%", types);
        List<DBTable> tables = new ArrayList<>();
        while (rs.next()) {
            String tableName = rs.getString("TABLE_NAME"); // 表名
            String remarks = rs.getString("REMARKS"); // 表备注
            DBTable dbTable = new DBTable(config, metaData,catalogName,tableName, remarks);
            tables.add(dbTable);
        }
        return tables;
    }

    private void outputFilesFromMetaInfo(GeneratorConfig config, List<DBTable> tables) throws IOException, TemplateException {
        for(DBTable table:tables){
            //生成XML配置文件
            MyBatisXMLWriter.writeMyBatisXML(table);
            //生成实体类
            EntityOrMapperWriter.write(
                    table,
                    table.getEntityDirPath()+table.getPascalEntityName()+".java"
            );
            //生成DAO接口
            EntityOrMapperWriter.write(
                    table,
                    table.getDaoDirPath()+table.getPascalMapperName()+".java"
            );
            //处理多主键的情况，按照其他生成软件的方式，将这些复合主键生成一个class
            if(table.hasComposeKey()){
                EntityOrMapperWriter.write(
                        table.getPrimaryKeyClass(),
                        table.getEntityDirPath()+table.getPascalEntityName()+"PrimaryKey.java"
                );
            }
        }
        ControllerDocumentWriter.writeControllerClass(
                tables,
                config.getJavaClientGenerator().getProperties()
        );
    }

    /**
     * 程序测试入口
     * @param args xxx
     */
    public static void main(String[]args) {
        GenerateFromFile("src\\main\\resources\\generatorConfig.xml");
    }

    /**
     * 插件入口
     * @param configFile 配置文件的地址
     */
    public static void GenerateFromFile(String configFile){
        GeneratorConfig config;
        List<DBTable> tables;
        DatabaseMetaData metaData;
        GeneratorController controller;

        try {
            controller = new GeneratorController();
            config = controller.readGeneratorConfig(configFile);
            metaData = controller.getDatabaseMetaData(config);
            assert metaData != null;
            tables = controller.retrieveDatabaseMetadataInformation(config, metaData);
            controller.outputFilesFromMetaInfo(config,tables);
        } catch (DocumentException | SQLException | TemplateException | IOException e) {
            e.printStackTrace();
        }
    }

}

