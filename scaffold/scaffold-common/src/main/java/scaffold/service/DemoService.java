package scaffold.service;

import io.ffit.carbon.response.MultiResponse;
import io.ffit.carbon.response.SimpleResponse;
import io.ffit.carbon.response.SingleResponse;
import org.springframework.validation.annotation.Validated;
import scaffold.entity.cmd.DemoSetCmd;
import scaffold.entity.query.DemoQry;
import scaffold.entity.vo.DemoVO;

import javax.validation.Valid;

/**
 * Demo Service
 *
 * @author Lay
 * @date 2022/9/28
 */
@Validated
public interface DemoService {
    /**
     * create demo
     * @param cmd
     * @return
     */
    SimpleResponse create(@Valid DemoSetCmd cmd);

    /**
     * modify demo
     * @param cmd
     * @return
     */
    SimpleResponse modify(@Valid DemoSetCmd cmd);

    /**
     * get demo by identity
     * @param id
     * @return
     */
    SingleResponse<DemoVO> get(long id);

    /**
     * query demos
     * @param qry
     * @return
     */
    MultiResponse<DemoVO> query(DemoQry qry);

    /**
     * delete demo
     * @param id
     * @return
     */
    SimpleResponse del(long id);
}
