package com.ljy.util;

import java.util.HashMap;
import java.util.Map;

public class NameRule {

    static Map<String,String> converter;

    static {
        converter = new HashMap<String, String>();
        converter.put("CHAR", "String");
        converter.put("VARCHAR", "String");
        converter.put("LONGVARCHAR", "String");
        converter.put("NUMERIC", "java.math.BigDecimal");
        converter.put("DECIMAL", "java.math.BigDecimal");
        converter.put("BIT", "boolean");
        converter.put("BOOLEAN", "boolean");
        converter.put("TINYINT", "byte");
        converter.put("SMALLINT", "short");
        converter.put("INTEGER", "Integer");
        converter.put("INT", "Integer");
        converter.put("BIGINT", "long");
        converter.put("REAL", "float");
        converter.put("FLOAT", "double");
        converter.put("DOUBLE", "double");
        converter.put("BINARY", "byte[]");
        converter.put("VARBINARY", "byte[]");
        converter.put("LONGVARBINARY", "byte[]");
        converter.put("DATE", "java.sql.Date");
        converter.put("TIME", "java.sql.Time");
        converter.put("TIMESTAMP", "java.sql.Timestamp");
        converter.put("CLOB", "Clob");
        converter.put("BLOB", "Blob");
        converter.put("ARRAY", "Array");

    }

    public static String Underline2Camel(String dbName){
        String[]words = dbName.toLowerCase().split("_");

        StringBuilder camelName = new StringBuilder(words[0]);
        for (int i = 1; i < words.length; i++) {
            camelName.append(words[i].substring(0,1).toUpperCase()+words[i].substring(1));
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

    public static String jdbcType2JavaType(String jdbcType){
        return converter.getOrDefault(jdbcType,"Object");
    }


}
