package com.ljy;

import com.ljy.dbObject.DBTable;
import com.ljy.writer.CrudControllerWriter;
import com.ljy.writer.EntityMapperWriter;
import com.ljy.writer.MyBatisXMLWriter;
import com.ljy.util.NameRule;
import freemarker.template.TemplateException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class GeneratorController {

    private DatabaseMetaData dbMetaData;
    private String fullyQualifiedEntityPackage;
    private String fullyQualifiedMapperPackage;

    private String driverClass;
    private String connectionURL;
    private String userId;
    private String password;

    private void readGeneratorConfig(String path) throws DocumentException {
        Document document = new SAXReader().read(new File(path));

        Element context = document.getRootElement().element("context");
        Element jdbcConnection = context.element("jdbcConnection");
        this.driverClass  = jdbcConnection.attributeValue("driverClass");
        this.connectionURL = jdbcConnection.attributeValue("connectionURL");
        this.userId = jdbcConnection.attributeValue("userId");
        this.password = jdbcConnection.attributeValue("password");

    }

    private void getDatabaseMetaData(){






        try {
            if (dbMetaData == null) {
                Class.forName(driverClass);
                Connection con = DriverManager.getConnection(connectionURL, userId, password);
                dbMetaData = con.getMetaData();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getAllTableList(String catalogName) throws SQLException, IOException, TemplateException {

        String[] types = { "TABLE" };
        ResultSet rs = dbMetaData.getTables(catalogName, null, "%", types);
        List<DBTable> tables = new ArrayList<>();
        while (rs.next()) {
            String catalog = rs.getString("TABLE_CAT"); //数据库名
            String tableName = rs.getString("TABLE_NAME"); // 表名
            String tableType = rs.getString("TABLE_TYPE"); // 表类型
            String remarks = rs.getString("REMARKS"); // 表备注
            System.out.println(catalog + "\t" + tableName + "\t" + tableType + "\t" + remarks);
            DBTable dbTable = new DBTable(dbMetaData,catalogName,tableName);
            //name table elements.
            dbTable.setTableName(tableName);
            dbTable.setCamelName(NameRule.Underline2Camel(tableName));
            dbTable.setCamelEntityName(NameRule.appendEntity(dbTable.getCamelName()));
            dbTable.setCamelMapperName(NameRule.appendMapper(dbTable.getCamelName()));

            dbTable.setPascalName(NameRule.Underline2Pascal(tableName));
            dbTable.setPascalEntityName(NameRule.appendEntity(dbTable.getPascalName()));
            dbTable.setPascalMapperName(NameRule.appendMapper(dbTable.getPascalName()));

            dbTable.setFullyQualifiedEntityPackage(this.getFullyQualifiedEntityPackage());
            dbTable.setFullyQualifiedMapperPackage(this.getFullyQualifiedMapperPackage());

            dbTable.setEntityDirPath(this.extractPath(dbTable.getFullyQualifiedEntityPackage()));
            dbTable.setMapperDirPath(this.extractPath(dbTable.getFullyQualifiedMapperPackage()));

            tables.add(dbTable);
            MyBatisXMLWriter.writeMyBatisXML(dbTable);
            EntityMapperWriter.writeEntityClass(dbTable);
        }
        CrudControllerWriter.writeControllerClass(tables);
    }

    private String extractPath(String fullyQualifiedPackage){
        StringBuilder sb = new StringBuilder("src\\");
        StringTokenizer st = new StringTokenizer(fullyQualifiedPackage,".");
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
            sb.append(File.separatorChar);
        }

        File f = new File(sb.toString());
        if(!f.exists()){
            f.mkdirs();
        }
        return sb.toString();
    }

    public static void main(String[]args) throws SQLException, IOException, TemplateException, DocumentException {
        GeneratorController rt = new GeneratorController();
        rt.readGeneratorConfig("src\\main\\resources\\generatorConfig.xml");
        rt.getDatabaseMetaData();
        rt.setFullyQualifiedEntityPackage("com.ljy.entity");
        rt.setFullyQualifiedMapperPackage("com.ljy.mapper");

        rt.getAllTableList("classroom");
    }

    public String getFullyQualifiedEntityPackage() {
        return fullyQualifiedEntityPackage;
    }

    public void setFullyQualifiedEntityPackage(String fullyQualifiedEntityPackage) {
        this.fullyQualifiedEntityPackage = fullyQualifiedEntityPackage;
    }

    public String getFullyQualifiedMapperPackage() {
        return fullyQualifiedMapperPackage;
    }

    public void setFullyQualifiedMapperPackage(String fullyQualifiedMapperPackage) {
        this.fullyQualifiedMapperPackage = fullyQualifiedMapperPackage;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getConnectionURL() {
        return connectionURL;
    }

    public void setConnectionURL(String connectionURL) {
        this.connectionURL = connectionURL;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

