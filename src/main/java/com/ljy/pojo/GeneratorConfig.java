package com.ljy.pojo;

import java.util.List;

/**
 * reveal and store generatorConfig.xml
 */
public class GeneratorConfig {

    private List<SpecifiedTable> specifiedTables;
    private JDBCConnection jdbcConnection;
    private JavaClientGenerator javaClientGenerator;
    private JavaModelGenerator javaModelGenerator;
    private SQLMapGenerator sqlMapGenerator;

    public GeneratorConfig(){
        jdbcConnection = new JDBCConnection();
        javaClientGenerator = new JavaClientGenerator();
        javaModelGenerator = new JavaModelGenerator();
        sqlMapGenerator = new SQLMapGenerator();
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
}


