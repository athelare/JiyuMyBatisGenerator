<#-- @ftlvariable name="tables" type="java.util.Collection<com.ljy.dbObject.DBTable>" -->
// todo package
<#list tables as table>
import ${table.fullyQualifiedEntityPackage}.${table.pascalEntityName};
import ${table.fullyQualifiedDaoPackage}.${table.pascalMapperName};
</#list>
public class CrudController{
<#list tables as table>
    private ${table.pascalMapperName} ${table.camelMapperName};
</#list>


<#list tables as table>
    //@PostMapping("${urlPrefix}/${table.camelName}/add")
    public int add${table.pascalName}(${table.pascalEntityName} ${table.camelEntityName}){
        int res = 0;
        try{
            res =  ${table.camelMapperName}.insert(${table.camelEntityName});
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }finally {
            return res;
        }
    }

</#list>
}