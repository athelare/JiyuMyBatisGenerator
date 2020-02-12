package com.ljy.pojo;

import java.util.List;

/**
 * reveal and store generatorConfig.xml
 */
public class GeneratorConfig {

    private List<SpecifiedTable> specifiedTables;

    public List<SpecifiedTable> getSpecifiedTables() {
        return specifiedTables;
    }

    public void setSpecifiedTables(List<SpecifiedTable> specifiedTables) {
        this.specifiedTables = specifiedTables;
    }

    static class jdbcConnection{
        private String driverClass;
        private String connectionURL;
        private String userId;
        private String password;

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

    static class javaModelGenerator{
        private String targetPackage;
        private String targetProject;

        public String getTargetPackage() {
            return targetPackage;
        }

        public void setTargetPackage(String targetPackage) {
            this.targetPackage = targetPackage;
        }

        public String getTargetProject() {
            return targetProject;
        }

        public void setTargetProject(String targetProject) {
            this.targetProject = targetProject;
        }
    }

    static class sqlMapGenerator{
        private String targetPackage;
        private String targetProject;

        public String getTargetPackage() {
            return targetPackage;
        }

        public void setTargetPackage(String targetPackage) {
            this.targetPackage = targetPackage;
        }

        public String getTargetProject() {
            return targetProject;
        }

        public void setTargetProject(String targetProject) {
            this.targetProject = targetProject;
        }
    }

    static class javaClientGenerator{
        private String targetPackage;
        private String targetProject;

        public String getTargetPackage() {
            return targetPackage;
        }

        public void setTargetPackage(String targetPackage) {
            this.targetPackage = targetPackage;
        }

        public String getTargetProject() {
            return targetProject;
        }

        public void setTargetProject(String targetProject) {
            this.targetProject = targetProject;
        }
    }

}
