package com.ljy.pojo;

import java.util.*;

/**
 * reveal and store generatorConfig.xml
 */
public class GeneratorConfig {

    private List<SpecifiedTable> specifiedTables;
    private JDBCConnection jdbcConnection;
    private JavaClientGenerator javaClientGenerator;
    private JavaModelGenerator javaModelGenerator;
    private SQLMapGenerator sqlMapGenerator;
    private Map<String, String> tableName2DomainObjectMap;

    public GeneratorConfig(){
        jdbcConnection = new JDBCConnection();
        javaClientGenerator = new JavaClientGenerator();
        javaModelGenerator = new JavaModelGenerator();
        sqlMapGenerator = new SQLMapGenerator();
        tableName2DomainObjectMap = new HashMap<>();
    }

    public List<SpecifiedTable> getSpecifiedTables() {
        return specifiedTables;
    }

    public void setSpecifiedTables(List<SpecifiedTable> specifiedTables) {
        this.specifiedTables = specifiedTables;
    }

    public JDBCConnection getJdbcConnection() {
        return jdbcConnection;
    }

    public void setJdbcConnection(JDBCConnection jdbcConnection) {
        this.jdbcConnection = jdbcConnection;
    }

    public JavaClientGenerator getJavaClientGenerator() {
        return javaClientGenerator;
    }

    public void setJavaClientGenerator(JavaClientGenerator javaClientGenerator) {
        this.javaClientGenerator = javaClientGenerator;
    }

    public JavaModelGenerator getJavaModelGenerator() {
        return javaModelGenerator;
    }

    public void setJavaModelGenerator(JavaModelGenerator javaModelGenerator) {
        this.javaModelGenerator = javaModelGenerator;
    }

    public SQLMapGenerator getSqlMapGenerator() {
        return sqlMapGenerator;
    }

    public void setSqlMapGenerator(SQLMapGenerator sqlMapGenerator) {
        this.sqlMapGenerator = sqlMapGenerator;
    }

    public Map<String, String> getTableName2DomainObjectMap() {
        return tableName2DomainObjectMap;
    }

    public void setTableName2DomainObjectMap(Map<String, String> tableName2DomainObjectMap) {
        this.tableName2DomainObjectMap = tableName2DomainObjectMap;
    }
}


