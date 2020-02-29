package com.ljy.util;

import java.util.HashMap;
import java.util.Map;

public class NameRule {

    static Map<String,String> converter;

    static {
        converter = new HashMap<>();
        converter.put("CHAR", "String");
        converter.put("VARCHAR", "String");
        converter.put("LONGVARCHAR", "String");
        converter.put("NUMERIC", "java.math.BigDecimal");
        converter.put("DECIMAL", "java.math.BigDecimal");
        converter.put("BIT", "Boolean");
        converter.put("BOOLEAN", "Boolean");
        converter.put("TINYINT", "Byte");
        converter.put("SMALLINT", "Short");
        converter.put("INTEGER", "Integer");
        converter.put("INT", "Integer");
        converter.put("BIGINT", "Long");
        converter.put("REAL", "Float");
        converter.put("FLOAT", "Double");
        converter.put("DOUBLE", "Double");
        converter.put("BINARY", "Byte[]");
        converter.put("VARBINARY", "Byte[]");
        converter.put("LONGVARBINARY", "Byte[]");
        converter.put("DATE", "java.sql.Date");
        converter.put("TIME", "java.sql.Time");
        converter.put("TIMESTAMP", "java.sql.Timestamp");
        converter.put("CLOB", "String");
        converter.put("BLOB", "Blob");
        converter.put("ARRAY", "Array");

    }

    public static String Underline2Camel(String dbName){
        String[]words = dbName.toLowerCase().split("_");

        StringBuilder camelName = new StringBuilder(words[0]);
        for (int i = 1; i < words.length; i++) {
            camelName.append(words[i].substring(0, 1).toUpperCase()).append(words[i].substring(1));
        }
        return camelName.toString();
    }

    public static String Underline2Pascal(String dbName){
        String camelName = Underline2Camel(dbName);
        return camelName.substring(0,1).toUpperCase()+camelName.substring(1);
    }

    public static String appendMapper(String name){
        return name+"Mapper";
    }

    public static String appendEntity(String name){
        return name+"Entity";
    }

    /**
     * 根据数据库元数据中查询到的数据库类型找出对应的Java数据类型，如果没有合适的转化，就暂且转化为Object对象
     * @param jdbcType 数据库类型名称
     * @return java数据类型名称
     */
    public static String jdbcType2JavaType(String jdbcType){
        return converter.getOrDefault(jdbcType,"Object");
    }


}
