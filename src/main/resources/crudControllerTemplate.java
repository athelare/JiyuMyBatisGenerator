//TODO package
<#list tables as table>
import ${table.fullyQualifiedEntityPackage}.${table.pascalEntityName};
import ${table.fullyQualifiedMapperPackage}.${table.pascalMapperName};
</#list>
public class CrudController{
<#list tables as table>
    private ${table.pascalMapperName} ${table.camelMapperName};
</#list>


<#list tables as table>
    public Response add${table.pascalName}(${table.pascalEntityName} ${table.camelEntityName}){
        try{
            ${table.camelMapperName}.insert(${table.camelEntityName});
        }catch (Exception e){
            e.printStackTrace();
            return Response.error();
        }finally {
            return response.success();
        }
    }

</#list>
}