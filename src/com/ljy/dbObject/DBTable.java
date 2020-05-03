package com.ljy.dbObject;

import com.ljy.pojo.GeneratorConfig;
import com.ljy.util.NameRule;

import java.io.File;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 数据库表
 * @author Jiyu
 */
public class DBTable {
    private String tableName;       //原始名称
    private String camelName;       //数据库名称转驼峰命名（用于url名称）
    private String pascalName;      //数据库名称转大驼峰命名
    private String pascalEntityName;//数据库名称转大驼峰+Entity命名（用于类名称）
    private String camelEntityName; //数据库名称转驼峰+Entity命名（用于对象名称）
    private String pascalMapperName;//数据库名称转大驼峰+Mapper命名（用于映射类/文件名称）
    private String camelMapperName; //数据库名称转驼峰+Mapper命名（用于映射对象名称）
    private String remark;          //描述

    private boolean hasPrimaryKey;  //是否有主键，有的话置为真
    private boolean hasComposeKey;  //是否复合主键，如果主键只有一个属性就置为假，复合主键就置为真
    private String fullyQualifiedEntityPackage; //实体类所在包
    private String fullyQualifiedMapperPackage; //映射文件（XML文件）的包
    private String fullyQualifiedDaoPackage;    //数据访问对象DAO接口
    private String entityDirPath;               //实体类文件夹路径
    private String mapperDirPath;               //XML文件夹路径
    private String daoDirPath;                  //DAO文件夹路径
    private String primaryKeyType;              //主键类型

    private List<DBColumn> primaryKey;
    private List<DBColumn> columns;

    public DBTable(){}
    public DBTable(GeneratorConfig config, DatabaseMetaData dbMetaData, String catalogName, String tableName, String remark) throws SQLException {

        this.setTableName(tableName);
        if (config.getTableName2DomainObjectMap().containsKey(tableName)){
            this.setPascalName(config.getTableName2DomainObjectMap().get(tableName));
            this.setPascalEntityName(pascalName);
            this.setPascalMapperName(NameRule.appendMapper(pascalName));
            this.setCamelName(NameRule.Pascal2Camel(pascalName));
            this.setCamelEntityName(camelName);
            this.setCamelMapperName(NameRule.appendMapper(camelName));

        }else {
            this.setCamelName(NameRule.Underline2Camel(tableName));
            this.setCamelEntityName(NameRule.appendEntity(this.getCamelName()));
            this.setCamelMapperName(NameRule.appendMapper(this.getCamelName()));
            this.setPascalName(NameRule.Camel2Pascal(this.getCamelName()));
            this.setPascalEntityName(NameRule.appendEntity(this.getPascalName()));
            this.setPascalMapperName(NameRule.appendMapper(this.getPascalName()));
        }

        this.setRemark(remark);
        this.setFullyQualifiedEntityPackage(config.getJavaModelGenerator().getTargetPackage());
        this.setFullyQualifiedMapperPackage(config.getSqlMapGenerator().getTargetPackage());
        this.setFullyQualifiedDaoPackage(config.getJavaClientGenerator().getTargetPackage());
        this.setEntityDirPath(this.extractPath(
                null == config.getJavaModelGenerator().getTargetProject()?
                        "src":
                        config.getJavaModelGenerator().getTargetProject(),
                this.getFullyQualifiedEntityPackage()
        ));
        this.setMapperDirPath(this.extractPath(
                null == config.getSqlMapGenerator().getTargetProject()?
                        "src":
                        config.getSqlMapGenerator().getTargetProject(),
                this.getFullyQualifiedMapperPackage()
        ));
        this.setDaoDirPath(this.extractPath(
                null == config.getJavaClientGenerator().getTargetProject()?
                        "src":
                        config.getJavaClientGenerator().getTargetProject(),
                config.getJavaClientGenerator().getTargetPackage()
        ));
        /*if(null == config.getJavaModelGenerator().getTargetProject()){
            this.setEntityDirPath(this.extractPath("src",this.getFullyQualifiedEntityPackage()));
        }else{
            this.setEntityDirPath(this.extractPath(config.getJavaModelGenerator().getTargetProject(),this.getFullyQualifiedEntityPackage()));
        }

        if(null == config.getSqlMapGenerator().getTargetProject()){
            this.setMapperDirPath(this.extractPath("src",this.getFullyQualifiedMapperPackage()));
        }else {
            this.setMapperDirPath(this.extractPath(config.getSqlMapGenerator().getTargetProject(),this.getFullyQualifiedMapperPackage()));
        }

        if(null == config.getJavaClientGenerator().getTargetProject()){
            this.setDaoDirPath(this.extractPath("src",config.getJavaClientGenerator().getTargetPackage()));
        }else {
            this.setDaoDirPath(this.extractPath(config.getJavaClientGenerator().getTargetProject(),config.getJavaClientGenerator().getTargetPackage()));
        }*/


        columns = new ArrayList<>();

        //获取列信息
        ResultSet rs = dbMetaData.getColumns(catalogName,null,tableName,"%");
        while(rs.next()){
            String columnName = rs.getString("COLUMN_NAME");
            int dataType = rs.getInt("DATA_TYPE");
            String dataTypeName = rs.getString("TYPE_NAME");
            int columnSize = rs.getInt("COLUMN_SIZE");
            boolean nullable = rs.getBoolean("NULLABLE");// 是否允许为null
            String remarks = rs.getString("REMARKS");// 列描述
            String columnDef = rs.getString("COLUMN_DEF");// 默认值
            String autoincrement = rs.getString("IS_AUTOINCREMENT");

            DBColumn column = new DBColumn();
            column.setColumnName(columnName);
            column.setAutoincrement(autoincrement);
            column.setJdbcTypeIndex(dataType);
            column.setJavaTypeName(NameRule.jdbcType2JavaType(dataTypeName));
            column.setColumnSize(columnSize);
            column.setNullable(nullable);
            column.setRemark(remarks);
            column.setColumnDefault(columnDef);

            switch (dataTypeName) {
                case "INT":
                    dataTypeName = "INTEGER";
                    break;
                case "ENUM":
                    dataTypeName = "VARCHAR";
                    break;
                case "DATETIME":
                    dataTypeName = "TIMESTAMP";
                    break;
                case "TEXT":
                    dataTypeName = "CLOB";
                    break;
            }
            column.setJdbcTypeName(dataTypeName);

            columns.add(column);

        }

        //获取主键信息
        primaryKey = new ArrayList<>();
        rs = dbMetaData.getPrimaryKeys(catalogName,null,tableName);
        int primaryKeyCount = 0;
        while(rs.next()){

            //设置为主键
            this.setHasPrimaryKey(true);
            primaryKeyCount++;
            String columnName = rs.getString("COLUMN_NAME");

            //在列上添加主键标记
            for(DBColumn column:columns){
                if(column.getColumnName().equals(columnName)){
                    column.setPrimaryKey(true);
                    primaryKey.add(column);
                }
            }
        }

        //
        if(primaryKeyCount>1){
            this.setHasComposeKey(true);
            this.setPrimaryKeyType(this.getPascalEntityName()+"PrimaryKey");
        }else {
            this.setPrimaryKeyType(this.getPrimaryKey().get(0).getJavaTypeName());
        }
    }


    public DBTable getPrimaryKeyClass(){
        DBTable tb = new DBTable();
        tb.setColumns(this.getPrimaryKey());
        tb.setFullyQualifiedEntityPackage(this.getFullyQualifiedEntityPackage());
        tb.setRemark("主键类");
        tb.setPascalEntityName(this.getPascalEntityName()+"PrimaryKey");
        return tb;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean hasPrimaryKey() {
        return hasPrimaryKey;
    }

    public void setHasPrimaryKey(boolean hasPrimaryKey) {
        this.hasPrimaryKey = hasPrimaryKey;
    }

    public boolean hasComposeKey() {
        return hasComposeKey;
    }

    public void setHasComposeKey(boolean hasComposeKey) {
        this.hasComposeKey = hasComposeKey;
    }

    public List<DBColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<DBColumn> columns) {
        this.columns = columns;
    }

    public List<DBColumn> getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(List<DBColumn> primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Override
    public String toString() {
        return "DBTable{" + "\n" +
                "\ttableName='" + tableName + '\'' +
                ", \n\tremark='" + remark + '\'' +
                ", \n\thasPrimaryKey=" + hasPrimaryKey +
                ", \n\thasComposeKey=" + hasComposeKey +
                ", \n\tprimaryKey=" + primaryKey +
                ", \n\tcolumns=" + columns +
                '}';
    }

    public String getCamelName() {
        return camelName;
    }

    public void setCamelName(String camelName) {
        this.camelName = camelName;
    }

    public String getPascalName() {
        return pascalName;
    }

    public void setPascalName(String pascalName) {
        this.pascalName = pascalName;
    }

    public String getPascalEntityName() {
        return pascalEntityName;
    }

    public void setPascalEntityName(String pascalEntityName) {
        this.pascalEntityName = pascalEntityName;
    }

    public String getCamelEntityName() {
        return camelEntityName;
    }

    public void setCamelEntityName(String camelEntityName) {
        this.camelEntityName = camelEntityName;
    }

    public String getPascalMapperName() {
        return pascalMapperName;
    }

    public void setPascalMapperName(String pascalMapperName) {
        this.pascalMapperName = pascalMapperName;
    }

    public String getCamelMapperName() {
        return camelMapperName;
    }

    public void setCamelMapperName(String camelMapperName) {
        this.camelMapperName = camelMapperName;
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

    public String getFullyQualifiedEntityClass(){
        return this.getFullyQualifiedEntityPackage()+"."+this.getPascalEntityName();
    }

    public String getFullyQualifiedMapperClass(){
        return this.getFullyQualifiedMapperPackage()+"."+this.getPascalMapperName();
    }

    public String getEntityDirPath() {
        return entityDirPath;
    }

    public void setEntityDirPath(String entityDirPath) {
        this.entityDirPath = entityDirPath;
    }

    public String getMapperDirPath() {
        return mapperDirPath;
    }

    public void setMapperDirPath(String mapperDirPath) {
        this.mapperDirPath = mapperDirPath;
    }

    public String getPrimaryKeyType() {
        return primaryKeyType;
    }

    public void setPrimaryKeyType(String primaryKeyType) {
        this.primaryKeyType = primaryKeyType;
    }

    public String getFullyQualifiedDaoPackage() {
        return fullyQualifiedDaoPackage;
    }

    public void setFullyQualifiedDaoPackage(String fullyQualifiedDaoPackage) {
        this.fullyQualifiedDaoPackage = fullyQualifiedDaoPackage;
    }

    public String getDaoDirPath() {
        return daoDirPath;
    }

    public void setDaoDirPath(String daoDirPath) {
        this.daoDirPath = daoDirPath;
    }

    public boolean isHasPrimaryKey() {
        return hasPrimaryKey;
    }

    public boolean isHasComposeKey() {
        return hasComposeKey;
    }

    private String extractPath(String targetProject, String fullyQualifiedPackage){
        StringBuilder sb = new StringBuilder(targetProject+"\\");
        StringTokenizer st = new StringTokenizer(fullyQualifiedPackage,".");
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
            sb.append(File.separatorChar);
        }

        File f = new File(sb.toString());
        if(!f.exists() && !f.mkdirs()){
            System.out.println("未能成功创建文件夹");
        }
        return sb.toString();
    }


}
