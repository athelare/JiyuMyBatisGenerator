package ${table.fullyQualifiedDaoPackage};

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import ${table.fullyQualifiedEntityPackage}.${table.pascalEntityName};
<#if table.hasComposeKey()>import ${table.fullyQualifiedEntityPackage}.${table.pascalEntityName}PrimaryKey;</#if>

/**
 * ${comment!""}
 * @author ${author!""}
 * @time ${time!""}
 */

@Mapper
@Repository
public interface ${table.pascalMapperName} {

    public int insert(${table.pascalEntityName} ${table.camelEntityName});

    public int insertSelective(${table.pascalEntityName} ${table.camelEntityName});

    public int deleteByPrimaryKey(${table.primaryKeyType} pk);

    public ${table.pascalEntityName} selectByPrimaryKey(${table.primaryKeyType} pk);

    public List<${table.pascalEntityName}> selectBy(${table.pascalEntityName} ${table.camelEntityName});

    public int updateByPrimaryKey(${table.pascalEntityName} ${table.camelEntityName});

    public int updateByPrimaryKeySelective(${table.pascalEntityName} ${table.camelEntityName});

}