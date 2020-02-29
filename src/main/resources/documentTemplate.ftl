<#-- @ftlvariable name="tables" type="java.util.Collection<com.ljy.dbObject.DBTable>" -->
# API Document for classroom



<#list tables as table>

## TABLE ${table.tableName}
表格描述
### ${table.tableName}表 新增
#### 请求地址
`${urlPrefix}/${table.camelName}/add`
#### 请求方式
POST RequestBody
#### 请求参数
|参数名称|参数类型|必填|说明|
|---|---|---|---|
<#list table.columns as column>
|${column.camelName}|${column.javaTypeName}|${column.nullable?string("No","Yes")}|${column.remark}|
</#list>
#### 返回参数
XXX对象
------------------------------------

## TABLE ${table.tableName}
表格描述
### ${table.tableName}表 删除
#### 请求地址
`${urlPrefix}/${table.camelName}/delete`
#### 请求方式
POST RequestBody
#### 请求参数
|参数名称|参数类型|必填|说明|
|---|---|---|---|
<#list table.primaryKey as column>
|${column.camelName}|${column.javaTypeName}|${column.nullable?string("No","Yes")}|${column.remark}|
</#list>
#### 返回参数
XXX对象
------------------------------------

## TABLE ${table.tableName}
表格描述
### ${table.tableName}表 修改
#### 请求地址
`${urlPrefix}/${table.camelName}/update`
#### 请求方式
POST RequestBody
#### 请求参数
|参数名称|参数类型|必填|说明|
|---|---|---|---|
<#list table.columns as column>
|${column.camelName}|${column.javaTypeName}|No|${column.remark}|
</#list>
#### 返回参数
XXX对象
------------------------------------

## TABLE ${table.tableName}
表格描述
### ${table.tableName}表 查询（主键）
#### 请求地址
`${urlPrefix}/${table.camelName}/selectByKey`
#### 请求方式
POST RequestBody
#### 请求参数
|参数名称|参数类型|必填|说明|
|---|---|---|---|
<#list table.primaryKey as column>
|${column.camelName}|${column.javaTypeName}|${column.nullable?string("No","Yes")}|${column.remark}|
</#list>
#### 返回参数
XXX对象
------------------------------------

## TABLE ${table.tableName}
表格描述
### ${table.tableName}表 查询（通过对象）
#### 请求地址
`${urlPrefix}/${table.camelName}/selectBy`
#### 请求方式
POST RequestBody
#### 请求参数
|参数名称|参数类型|必填|说明|
|---|---|---|---|
<#list table.columns as column>
|${column.camelName}|${column.javaTypeName}|No|${column.remark}|
</#list>
#### 返回参数
XXX对象
------------------------------------

</#list>
