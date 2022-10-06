package scaffold.entity.query;

import io.ffit.carbon.dto.Query;
import lombok.Data;

/**
 * Demo Query Params
 *
 * @author Lay
 * @date 2022/9/28
 */
@Data
public class DemoQry extends Query {
    /**
     * name
     */
    private String name;
}
