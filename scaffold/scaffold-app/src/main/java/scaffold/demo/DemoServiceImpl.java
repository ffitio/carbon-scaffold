package scaffold.demo;

import io.ffit.carbon.response.MultiResponse;
import io.ffit.carbon.response.SimpleResponse;
import io.ffit.carbon.response.SingleResponse;
import org.springframework.stereotype.Service;
import scaffold.demo.converter.DemoConverter;
import scaffold.domain.demo.Demo;
import scaffold.domain.demo.gateway.DemoGateway;
import scaffold.entity.cmd.DemoSetCmd;
import scaffold.entity.query.DemoQry;
import scaffold.entity.vo.DemoVO;
import scaffold.response.Errors;
import scaffold.service.DemoService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Implementation of {@link DemoService}
 *
 * @author Lay
 * @date 2022/9/28
 */
@Service
public class DemoServiceImpl implements DemoService {

    @Resource
    DemoGateway demoGateway;

    @Override
    public SimpleResponse create(DemoSetCmd cmd) {
        // create
        Demo demo = new Demo();
        demo.create(cmd.getName());

        // persist
        demoGateway.save(demo);

        return SimpleResponse.success();
    }

    @Override
    public SimpleResponse modify(DemoSetCmd cmd) {
        Demo.Id id = Demo.Id.of(cmd.getId());

        // find
        Demo demo = demoGateway.find(id);
        if (demo == null) {
            return SimpleResponse.error(Errors.DemoNotFound);
        }

        // modify
        demo.modify(cmd.getName());

        // persist
        demoGateway.save(demo);

        return SimpleResponse.success();
    }

    @Override
    public SingleResponse<DemoVO> get(long id) {
        Demo demo = demoGateway.find(Demo.Id.of(id));
        if (demo == null) {
            return SingleResponse.error(Errors.DemoNotFound);
        }

        DemoVO demoVO = DemoConverter.toViewObject(demo);
        return SingleResponse.of(demoVO);
    }

    @Override
    public MultiResponse<DemoVO> query(DemoQry qry) {
        List<DemoVO> list = demoGateway.query(qry);
        return MultiResponse.of(list);
    }

    @Override
    public SimpleResponse del(long id) {
        demoGateway.del(Demo.Id.of(id));
        return SimpleResponse.success();
    }
}
