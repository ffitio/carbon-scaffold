package scaffold.demo.converter;

import scaffold.demo.model.DemoDO;
import scaffold.domain.demo.Demo;
import scaffold.entity.vo.DemoVO;

/**
 * Demo Converter
 *
 * @author Lay
 * @date 2022/9/28
 */
public class DemoConverter {
    /**
     * convert domain object to view object
     * @param demo Domain Object
     * @return View Object
     */
    public static DemoVO toViewObject(Demo demo) {
        if (demo == null) {
            return null;
        }

        DemoVO demoVO = new DemoVO();
        demoVO.setId(demo.getId().getValue());
        demoVO.setName(demo.getName());
        return demoVO;
    }

    /**
     * convert data object to view object
     * @param demoDO Data Object
     * @return View Object
     */
    public static DemoVO toViewObject(DemoDO demoDO) {
        if (demoDO == null) {
            return null;
        }

        DemoVO demoVO = new DemoVO();
        demoVO.setId(demoDO.getId());
        demoVO.setName(demoDO.getName());
        return demoVO;
    }

    /**
     * convert domain object to data object
     * @param demo Domain Object
     * @return Data Object
     */
    public static DemoDO toDataObject(Demo demo) {
        if (demo == null) {
            return null;
        }

        DemoDO demoDO = new DemoDO();
        demoDO.setId(demo.getId().getValue());
        demoDO.setName(demo.getName());
        return demoDO;
    }

    /**
     * convert data object to domain object
     * @param demoDO Data Object
     * @return Domain Object
     */
    public static Demo toDomainObject(DemoDO demoDO) {
        if (demoDO == null) {
            return null;
        }

        Demo demo = new Demo();
        demo.setId(Demo.Id.of(demoDO.getId()));
        demo.setName(demoDO.getName());
        return demo;
    }
}
