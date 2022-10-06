package scaffold.domain.demo.gateway;

import scaffold.domain.demo.Demo;
import scaffold.entity.query.DemoQry;
import scaffold.entity.vo.DemoVO;

import java.util.List;

/**
 * Demo Gateway
 *
 * @author Lay
 * @date 2022/9/28
 */
public interface DemoGateway {

    /**
     * save demo
     * @param demo {@link Demo}
     */
    void save(Demo demo);

    /**
     * find demo by identity
     * @param id {@link Demo.Id}
     * @return {@link Demo}
     */
    Demo find(Demo.Id id);

    /**
     * query demos
     * @param qry {@link DemoQry}
     * @return Query Results
     */
    List<DemoVO> query(DemoQry qry);

    /**
     * delete demo
     * @param id {@link Demo.Id}
     */
    void del(Demo.Id id);
}
