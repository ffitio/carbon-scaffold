package scaffold.demo;

import org.springframework.stereotype.Component;
import scaffold.demo.converter.DemoConverter;
import scaffold.demo.mapper.DemoMapper;
import scaffold.demo.model.DemoDO;
import scaffold.domain.demo.Demo;
import scaffold.domain.demo.gateway.DemoGateway;
import scaffold.entity.query.DemoQry;
import scaffold.entity.vo.DemoVO;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of {@link DemoGateway}
 *
 * @author Lay
 * @date 2022/9/28
 */
@Component
public class DemoGatewayImpl implements DemoGateway {

    @Resource
    DemoMapper demoMapper;

    @Override
    public void save(Demo demo) {
        DemoDO demoDO = DemoConverter.toDataObject(demo);
        if (demo.hasId()) {
            demoMapper.update(demoDO);
        } else {
            demoMapper.insert(demoDO);
            demo.setId(Demo.Id.of(demoDO.getId()));
        }
    }

    @Override
    public Demo find(Demo.Id id) {
        DemoDO demoDO = demoMapper.selectById(id.getValue());
        return DemoConverter.toDomainObject(demoDO);
    }

    @Override
    public List<DemoVO> query(DemoQry qry) {
        List<DemoDO> list = demoMapper.selectAll(qry.getName());
        if (!list.isEmpty()) {
            final List<DemoVO> res = new ArrayList<>();
            list.forEach(d -> res.add(DemoConverter.toViewObject(d)));
            return res;
        }
        return Collections.emptyList();
    }

    @Override
    public void del(Demo.Id id) {
        demoMapper.delete(id.getValue());
    }
}
