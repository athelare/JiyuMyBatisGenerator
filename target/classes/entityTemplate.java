package ${table.fullyQualifiedEntityPackage};

import java.util.List;

/**
 * ${comment!""}
 * @author ${author!""}
 * @time ${time!""}
 */

public class ${table.pascalEntityName} {

<#list table.columns as column>
private ${column.javaTypeName} ${column.camelName};

</#list>

<#list table.columns as column>
private ${column.javaTypeName} get${column.pascalName}(){
        return ${column.camelName};
        }

private void set${column.pascalName}(${column.javaTypeName} ${column.camelName}){
        this.${column.camelName} = ${column.camelName};
        }

</#list>
@Override
public String toString() {
        return "${table.pascalEntityName}{"
<#list table.columns as column>
        + "${column.camelName}='" + ${column.camelName} + "'" <#sep> + ", "</#sep>
</#list>
        + "}";
        }


}