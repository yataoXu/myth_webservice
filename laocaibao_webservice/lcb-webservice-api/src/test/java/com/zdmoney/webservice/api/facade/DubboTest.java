package com.zdmoney.webservice.api.facade;

import com.alibaba.fastjson.JSON;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.app.*;
import com.zdmoney.webservice.api.dto.customer.*;
import com.zdmoney.webservice.api.dto.product.BusiProductListDTO;
import com.zdmoney.webservice.api.dto.product.ProductVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * dubbo接口测试类
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-dubbo-consumer.xml")
public class DubboTest {

    @Autowired
    private IUserFacadeService userService;

    @Autowired
    private IProductFacadeService productService;

    @Autowired
    private IAppFacadeService appFacadeService;


    @Test
    public void userLoginTest(){
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setCmCellphone("17666668888");
        userLoginDTO.setCmPassword("E10ADC3949BA59ABBE56E057F20F883E");
        userLoginDTO.setType(1);
        //userLoginDTO.setAppType("ANDROID");
        userLoginDTO.setSystemType("App");
        //userLoginDTO.setClientId("a15ecf6ed208061a0b38434bcbcae31e");
        //userLoginDTO.setLoginType("app");
        ResultDto<CustomerVO> res = userService.checkPwd(userLoginDTO);
        System.out.println(">>>>>>>" + JSON.toJSONString(res));
    }


    @Test
    public void wechatRegistOrLogin(){
        RegistDTO registDTO = new RegistDTO();
        registDTO.setCellPhone("18900002212");
        registDTO.setPassword("E10ADC3949BA59ABBE56E057F20F883E");
        registDTO.setOpenId("000222");
        registDTO.setValidateCode("6606");
        ResultDto<CustomerVO> res = userService.wechatRegistOrLogin(registDTO);
        System.out.println(">>>>>>>" + JSON.toJSONString(res));
    }

    @Test
    public void getProduct(){
        ProductVO productVO = new ProductVO();
        productVO.setProductType(2);
        productVO.setPageNo(1);
        ResultDto<BusiProductListDTO> res = productService.getProductListByType(productVO);
        System.out.println(">>>>>>>" + JSON.toJSONString(res));
    }

    @Test
    public void getIndexInfo(){
        IndexVO indexVO = new IndexVO();
        indexVO.setCustomerId("840692");
        ResultDto<IndexDTO> res = appFacadeService.getIndexInfo(indexVO);
        System.out.println(">>>>>>>" + JSON.toJSONString(res));
    }

    @Test
    public void getPublicizeInfo(){
        ResultDto<PublicizeDTO> res = appFacadeService.getPublicizeInfo();
        System.out.println(">>>>>>>" + JSON.toJSONString(res));
    }

    @Test
    public void getAccountInfo(){
        AccountVO accountVO = new AccountVO();
        accountVO.setCustomerId("892554");
        ResultDto<AccountInfoDTO> res = appFacadeService.getAccountInfo(accountVO);
        System.out.println(">>>>>>>" + JSON.toJSONString(res));
    }

    @Test
    public void getDiscovery(){
        ResultDto<DiscoveryInfoDTO> res = appFacadeService.discovery();
        System.err.println(">>>>>>>" + JSON.toJSONString(res));
    }

    @Test
    public void ShowMsgDTO(){
        IndexVO indexVO = new IndexVO();
        indexVO.setCustomerId("892554");
        ResultDto<ShowMsgDTO> res = appFacadeService.showMsg(indexVO);
        System.err.println(">>>>>>>" + JSON.toJSONString(res));
    }

}
