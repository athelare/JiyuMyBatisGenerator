package com.ljy.dbObject;

import com.ljy.util.NameRule;

public class DBColumn {

    private String columnName;      //列明
    private String camelName;       //驼峰式命名
    private String pascalName;      //大写的命名
    private int columnSize;         //列定义的长度
    private boolean nullable;       //是否可以为NULL值
    private String remark;          //备注
    private String columnDefault;   //默认值
    private String autoincrement;   //自动增长
    private boolean primaryKey;     //是否为主键
    private int jdbcTypeIndex;      //jdbc类型的整数值
    private String jdbcTypeName;    //jdbc类型名
    private int jdbcLength;
    private String javaTypeName;    //java类型名

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
        this.camelName = NameRule.Underline2Camel(columnName);
        this.pascalName = NameRule.Underline2Pascal(columnName);
    }

    public int getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getColumnDefault() {
        return columnDefault;
    }

    public void setColumnDefault(String columnDefault) {
        this.columnDefault = columnDefault;
    }

    public String getAutoincrement() {
        return autoincrement;
    }

    public void setAutoincrement(String autoincrement) {
        this.autoincrement = autoincrement;
    }


    @Override
    public String toString() {
        return "DBColumn{" +
                "columnName='" + columnName + '\'' +
                ", columnSize=" + columnSize +
                ", nullable=" + nullable +
                ", remarks='" + remark + '\'' +
                ", columnDefault='" + columnDefault + '\'' +
                ", autoincrement=" + autoincrement +
                '}';
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public int getJdbcTypeIndex() {
        return jdbcTypeIndex;
    }

    public void setJdbcTypeIndex(int jdbcTypeIndex) {
        this.jdbcTypeIndex = jdbcTypeIndex;
    }

    public String getJdbcTypeName() {
        return jdbcTypeName;
    }

    public void setJdbcTypeName(String jdbcTypeName) {
        this.jdbcTypeName = jdbcTypeName;
    }

    public int getJdbcLength() {
        return jdbcLength;
    }

    public void setJdbcLength(int jdbcLength) {
        this.jdbcLength = jdbcLength;
    }

    public String getJavaTypeName() {
        return javaTypeName;
    }

    public void setJavaTypeName(String javaTypeName) {
        this.javaTypeName = javaTypeName;
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
}
