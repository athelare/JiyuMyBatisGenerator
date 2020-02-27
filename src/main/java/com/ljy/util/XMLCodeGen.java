package com.ljy.util;

import com.ljy.dbObject.DBColumn;
import com.ljy.dbObject.DBTable;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XMLCodeGen {

    public static Element generateBaseResultMap(DBTable dbTable){
        Element resultMap = DocumentHelper.createElement("resultMap")
                .addAttribute("id","BaseResultMap")
                .addAttribute("type",dbTable.getFullyQualifiedEntityClass());

        for(DBColumn column:dbTable.getColumns()){
            String type;
            if(column.isPrimaryKey()){
                type="id";
            }else {
                type="result";
            }
            resultMap.add(
                    DocumentHelper.createElement(type)
                            .addAttribute("column",column.getColumnName())
                            .addAttribute("property",column.getCamelName())
                            .addAttribute("jdbcType",column.getJdbcTypeName())
            );
        }
        return resultMap;
    }

    public static Element generateBaseColumnList(DBTable dbTable){
        boolean first;
        Element baseColumnList = DocumentHelper.createElement("sql")
                .addAttribute("id","Base_Column_List");
        StringBuilder sb = new StringBuilder();

        first = true;
        for(DBColumn column:dbTable.getColumns()){
            if(!first){
                sb.append(", ");
            }else {
                first=false;
            }
            sb.append(column.getColumnName());
        }
        baseColumnList.setText(sb.toString());
        return baseColumnList;
    }

    public static Element generateSelectByPrimaryKey(DBTable dbTable){

        Element selectByPrimaryKeyCode = DocumentHelper.createElement("select")
                .addAttribute("id","selectByPrimaryKey")
                .addAttribute("resultMap","BaseResultMap")
                .addAttribute(
                        "parameterType",
                        dbTable.hasComposeKey()?
                                dbTable.getFullyQualifiedEntityClass()+"PrimaryKey":
                                dbTable.getPrimaryKey().get(0).getJavaTypeName()
                );


        StringBuilder sb;
        boolean first;

        sb = new StringBuilder();
        sb.append("\n")
                .append("    SELECT\n")
                .append("      <include refid=\"Base_Column_List\" />\n")
                .append("    FROM ")
                .append(dbTable.getTableName()).append("\n")
                .append("    WHERE\n      ");
        first=true;
        for(DBColumn column:dbTable.getPrimaryKey()){
            if(!first){
                sb.append("      AND ");
            }else {
                first=false;
            }
            sb.append(column.getColumnName()+" = #{"+column.getCamelName()+",jdbcType="+column.getJdbcTypeName()+"}\n");
        }
        sb.append("  ");





        selectByPrimaryKeyCode.setText(sb.toString());

        return selectByPrimaryKeyCode;
    }

    public static Element generateSelectBy(DBTable dbTable){

        Element selectByCode = DocumentHelper.createElement("select")
                .addAttribute("id","selectBy")
                .addAttribute("resultMap","BaseResultMap")
                .addAttribute(
                        "parameterType",
                        dbTable.getFullyQualifiedEntityClass()
                );

        StringBuilder sb = new StringBuilder();
        sb.append("\n    SELECT\n" +
                "      <include refid=\"Base_Column_List\" />\n"+
                "    FROM "+dbTable.getTableName() + "\n"+
                "    <where>\n");
        for(DBColumn column:dbTable.getColumns()){
            sb.append("      <if test=\""+column.getCamelName()+" != null\" >\n");
            sb.append("        AND "+column.getColumnName()+" = #{"+column.getCamelName()+",jdbcType="+column.getJdbcTypeName()+"}\n");
            sb.append("      </if>\n");
        }
        sb.append("    </where>\n  ");




        selectByCode.setText(sb.toString());
        return selectByCode;
    }

    public static Element generateInsert(DBTable dbTable){
        StringBuilder sb = new StringBuilder();
        boolean first;
        Element insertCode = DocumentHelper.createElement("insert")
                .addAttribute("id","insert")
                .addAttribute(
                        "parameterType",
                        dbTable.getFullyQualifiedEntityClass()
                );
        sb.append("\n    INSERT INTO "+dbTable.getTableName()+"(");

        first=true;
        for(DBColumn column:dbTable.getColumns()){
            if(!first){
                sb.append(", ");
            }else {
                first=false;
            }
            sb.append(column.getColumnName());
        }
        sb.append(") VALUES (\n");

        first=true;
        for(DBColumn column:dbTable.getColumns()){
            if(!first){
                sb.append(", \n");
            }else {
                first=false;
            }
            sb.append("      #{"+column.getCamelName()+",jdbcType="+column.getJdbcTypeName()+"}");
        }
        sb.append("\n    )\n  ");
        insertCode.setText(sb.toString());

        return insertCode;
    }

    public static Element generateInsertSelective(DBTable dbTable){
        StringBuilder sb = new StringBuilder();
        Element insertSelectiveCode = DocumentHelper.createElement("insert")
                .addAttribute("id","insertSelective")
                .addAttribute(
                        "parameterType",
                        dbTable.getFullyQualifiedEntityClass()
                );
        sb.append("\n    INSERT INTO "+dbTable.getTableName()+"\n");
        sb.append("    <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >\n");
        for(DBColumn column:dbTable.getColumns()){
            sb.append("      <if test=\""+column.getCamelName()+" != null\" >\n");
            sb.append("        "+column.getColumnName()+", \n");
            sb.append("      </if>\n");
        }
        sb.append("    </trim>\n");
        sb.append("    <trim prefix=\"VALUES (\" suffix=\")\" suffixOverrides=\",\" >\n");
        for(DBColumn column:dbTable.getColumns()){
            sb.append("      <if test=\""+column.getCamelName()+" != null\" >\n");
            sb.append("        #{"+column.getCamelName()+",jdbcType="+column.getJdbcTypeName()+"}, \n");
            sb.append("      </if>\n");
        }
        sb.append("    </trim>\n  ");

        insertSelectiveCode.setText(sb.toString());


        return insertSelectiveCode;
    }

    public static Element generateDeleteByPrimaryKey(DBTable dbTable){

        Element deleteByPrimaryKeyCode = DocumentHelper.createElement("delete")
                .addAttribute("id","deleteByPrimaryKey")
                .addAttribute(
                        "parameterType",
                        dbTable.hasComposeKey()?
                                dbTable.getFullyQualifiedEntityClass()+"PrimaryKey":
                                dbTable.getPrimaryKey().get(0).getJavaTypeName()
                );

        StringBuilder sb = new StringBuilder();
        boolean first;
        sb.append("\n    DELETE FROM ").append(dbTable.getTableName()).append(" \n")
                .append("    WHERE\n      ");

        first=true;
        for(DBColumn column:dbTable.getPrimaryKey()){
            if(!first){
                sb.append("      AND ");
            }else {
                first=false;
            }
            sb.append(column.getColumnName()+" = #{"+column.getCamelName()+",jdbcType="+column.getJdbcTypeName()+"}\n");
        }
        sb.append("  ");

        deleteByPrimaryKeyCode.setText(sb.toString());

        return deleteByPrimaryKeyCode;
    }

    public static Element generateUpdateByPrimaryKey(DBTable dbTable){
        Element updateByPrimaryKeyCode = DocumentHelper.createElement("update")
                .addAttribute("id","updateByPrimaryKey")
                .addAttribute(
                        "parameterType",
                        dbTable.hasComposeKey()?
                                dbTable.getFullyQualifiedEntityClass()+"PrimaryKey":
                                dbTable.getPrimaryKey().get(0).getJavaTypeName()
                );

        boolean first;
        StringBuilder sb = new StringBuilder();
        sb.append("\n    UPDATE ").append(dbTable.getTableName()).append("\n    SET\n");

        first=true;
        for(DBColumn column:dbTable.getPrimaryKey()){
            if(!first){
                sb.append(",\n");
            }else {
                first=false;
            }
            sb.append("      "+column.getColumnName()+" = #{"+column.getCamelName()+",jdbcType="+column.getJdbcTypeName()+"}");
        }
        sb.append("\n    WHERE\n      ");

        first=true;
        for(DBColumn column:dbTable.getPrimaryKey()){
            if(!first){
                sb.append("      AND ");
            }else {
                first=false;
            }
            sb.append(column.getColumnName()+" = #{"+column.getCamelName()+",jdbcType="+column.getJdbcTypeName()+"}\n");
        }
        sb.append("  ");


        updateByPrimaryKeyCode.setText(sb.toString());
        return updateByPrimaryKeyCode;
    }

    public static Element generateUpdateByPrimaryKeySelective(DBTable dbTable){
        Element updateByPrimaryKeySelectiveCode = DocumentHelper.createElement("update")
                .addAttribute("id","updateByPrimaryKeySelective")
                .addAttribute(
                        "parameterType",
                        dbTable.hasComposeKey()?
                                dbTable.getFullyQualifiedEntityClass()+"PrimaryKey":
                                dbTable.getPrimaryKey().get(0).getJavaTypeName()
                );

        boolean first;
        StringBuilder sb = new StringBuilder();
        sb.append("\n    UPDATE ").append(dbTable.getTableName()).append("\n    <set>\n");

        for(DBColumn column:dbTable.getColumns()){
            sb.append("      <if test=\""+column.getCamelName()+" != null\" >\n");
            sb.append("        "+column.getColumnName()+" = #{"+column.getCamelName()+",jdbcType="+column.getJdbcTypeName()+"},\n");
            sb.append("      </if>\n");
        }
        sb.append("    </set>\n    WHERE\n      ");

        first=true;
        for(DBColumn column:dbTable.getPrimaryKey()){
            if(!first){
                sb.append("      AND ");
            }else {
                first=false;
            }
            sb.append(column.getColumnName()+" = #{"+column.getCamelName()+",jdbcType="+column.getJdbcTypeName()+"}\n");
        }
        sb.append("  ");
        updateByPrimaryKeySelectiveCode.setText(sb.toString());
        return updateByPrimaryKeySelectiveCode;
    }
}
