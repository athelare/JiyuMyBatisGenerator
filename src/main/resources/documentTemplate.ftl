# API Document for classroom 
<#list tables as table>
## TABLE ${table.tableName}
表格描述
### ${table.tableName}表 新增
#### 请求地址
`asd`
#### 请求方式
POST RequestBody
#### 请求参数
|参数名称|参数类型|说明|
|---|---|---|
<#list table.columns as column>
|${column.camelName}|${column.javaTypeName}|${column.remark}|
</#list>
#### 返回参数
XXX对象
</#list>
