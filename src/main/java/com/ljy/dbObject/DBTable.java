package com.ljy.dbObject;

import com.ljy.util.NameRule;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库表
 * @author Jiyu
 */
public class DBTable {
    private String tableName;
    private String camelName;       //数据库名称转驼峰命名（用于url名称）
    private String pascalName;      //数据库名称转大驼峰命名
    private String pascalEntityName;//数据库名称转大驼峰+Entity命名（用于类名称）
    private String camelEntityName; //数据库名称转驼峰+Entity命名（用于对象名称）
    private String pascalMapperName;//数据库名称转大驼峰+Mapper命名（用于映射类/文件名称）
    private String camelMapperName; //数据库名称转驼峰+Mapper命名（用于映射对象名称）
    private String remark;
    private boolean hasPrimaryKey;//是否有主键，有的话置为真
    private boolean hasComposeKey;//是否复合主键，如果主键只有一个属性就置为假，复合主键就置为真
    private String fullyQualifiedEntityPackage;
    private String fullyQualifiedMapperPackage;
    private String fullyQualifiedDaoPackage;
    private String entityDirPath;
    private String mapperDirPath;
    private String daoDirPath;
    private String primaryKeyType;



    private List<DBColumn> primaryKey;
    private List<DBColumn> columns;

    public DBTable(DatabaseMetaData dbMetaData, String catalogName, String tableName) throws SQLException {
        columns = new ArrayList<>();

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
            column.setJdbcTypeName(dataTypeName);
            column.setJavaTypeName(NameRule.jdbcType2JavaType(dataTypeName));
            column.setColumnSize(columnSize);
            column.setNullable(nullable);
            column.setRemarks(remarks);
            column.setColumnDefault(columnDef);

            columns.add(column);

        }

        primaryKey = new ArrayList<>();
        rs = dbMetaData.getPrimaryKeys(catalogName,null,tableName);
        int primaryKeyCount = 0;
        while(rs.next()){
            this.setHasPrimaryKey(true);
            primaryKeyCount++;
            String columnName = rs.getString("COLUMN_NAME");

            for(DBColumn column:columns){
                if(column.getColumnName().equals(columnName)){
                    column.setPrimaryKey(true);
                    primaryKey.add(column);
                }
            }
        }
        if(primaryKeyCount>1){
            this.setHasComposeKey(true);
            this.setPrimaryKeyType(this.getPascalEntityName()+"PrimaryKey");
        }else {
            this.setPrimaryKeyType(this.getPrimaryKey().get(0).getJavaTypeName());
        }
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
}
