package scaffold.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import scaffold.demo.model.DemoDO;

import java.util.List;

/**
 * Demo Mapper
 *
 * @author Lay
 * @date 2022/9/28
 */
@Mapper
public interface DemoMapper {
    DemoDO selectById(long id);

    int insert(DemoDO demoDO);

    int update(DemoDO demoDO);

    int delete(long id);

    List<DemoDO> selectAll(@Param("name") String name);
}
