package com.ljy.dbObject;

import com.ljy.util.NameRule;

public class DBColumn {

    private String columnName;
    private String camelName;
    private String pascalName;
    private int columnSize;
    private boolean nullable;
    private String remark;
    private String columnDefault;
    private String autoincrement;
    private boolean primaryKey;
    private int jdbcTypeIndex;
    private String jdbcTypeName;
    private int jdbcLength;
    private String javaTypeName;

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
