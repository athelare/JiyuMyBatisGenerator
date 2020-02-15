package com.ljy.writer;

import com.ljy.dbObject.DBColumn;
import com.ljy.dbObject.DBTable;
import com.ljy.util.XMLCodeGen;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;

public class MyBatisXMLWriter {

    public static void writeMyBatisXML(DBTable dbTable) throws IOException {

        String fullyQualifiedEntity = dbTable.getFullyQualifiedEntityPackage()+"."+dbTable.getPascalEntityName();
        String fullyQualifiedMapper = dbTable.getFullyQualifiedMapperPackage()+"."+dbTable.getPascalMapperName();


        //XML文档对象
        Document document = DocumentHelper.createDocument();
        document.addDocType("mapper","-//mybatis.org//DTD Mapper 3.0//EN","http://mybatis.org/dtd/mybatis-3-mapper.dtd");

        Element rootElement = DocumentHelper.createElement("mapper").addAttribute("namespace",fullyQualifiedMapper);
        document.add(rootElement);


        rootElement.add(XMLCodeGen.generateBaseResultMap(dbTable));
        rootElement.add(XMLCodeGen.generateBaseColumnList(dbTable));
        rootElement.add(XMLCodeGen.generateSelectByPrimaryKey(dbTable));
        rootElement.add(XMLCodeGen.generateSelectBy(dbTable));
        rootElement.add(XMLCodeGen.generateInsert(dbTable));
        rootElement.add(XMLCodeGen.generateInsertSelective(dbTable));
        rootElement.add(XMLCodeGen.generateDeleteByPrimaryKey(dbTable));
        rootElement.add(XMLCodeGen.generateUpdateByPrimaryKey(dbTable));
        rootElement.add(XMLCodeGen.generateUpdateByPrimaryKeySelective(dbTable));
        //Output directory



        OutputFormat format = new OutputFormat("  ",true,"UTF-8");
        XMLWriter writer = new XMLWriter(new FileOutputStream(new File(dbTable.getMapperDirPath()+dbTable.getPascalMapperName()+".xml")),format);
        writer.setEscapeText(false);
        writer.write(document);
    }
}
