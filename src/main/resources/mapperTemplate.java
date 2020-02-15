package ${table.fullyQualifiedMapperPackage};

import java.util.List;

/**
 * ${comment!""}
 * @author ${author!""}
 * @time ${time!""}
 */

public interface ${table.pascalMapperName} {

    public int insert(${table.pascalEntityName} ${table.camelEntityName});

    public int insertSelective(${table.pascalEntityName} ${table.camelEntityName});

    public int deleteByPrimaryKey(${table.primaryKeyType} pk);

    public ${table.pascalEntityName} selectByPrimaryKey(${table.primaryKeyType} pk);

    public ${table.pascalEntityName} selectBy(${table.pascalEntityName} ${table.camelEntityName});

    public int updateByPrimaryKey(${table.pascalEntityName} ${table.camelEntityName});

    public int updateByPrimaryKeySelective(${table.pascalEntityName} ${table.camelEntityName});

}