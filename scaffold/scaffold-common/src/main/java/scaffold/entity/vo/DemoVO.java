package scaffold.entity.vo;

import io.ffit.carbon.dto.ClientObject;
import lombok.Data;

/**
 * Demo View Object
 *
 * @author Lay
 * @date 2022/9/28
 */
@Data
public class DemoVO extends ClientObject {
    /**
     * identity
     */
    private long id;

    /**
     * name
     */
    private String name;
}
