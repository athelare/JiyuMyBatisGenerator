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
    public int add${table.pascalName}(${table.pascalEntityName} ${table.camelEntityName}){
        try{
            int ${table.camelMapperName}.insert(${table.camelEntityName});
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }finally {
            return response.success();
        }
    }

</#list>
}