package com.zdmoney.common;

import com.zdmoney.models.sys.SysParameter;
import com.zdmoney.service.SysParameterService;
import lombok.extern.slf4j.Slf4j;
import websvc.utils.SpringContextHelper;

import java.util.List;

/**
 * Created by user on 2018/9/22.
 */
@Slf4j
public class CompanyAccounts {

    private static final String PR_TYPE_ACCOUNT_SY = "account_jgsy";//机构收益户
    private static final String PR_TYPE_ACCOUNT_SY_FUIOU_ID = "account_jgsy_fuiou_id";//机构收益户

    private static final String PR_TYPE_ACCOUNT_HB = "account_gshb";//公司红包户
    private static final String PR_TYPE_ACCOUNT_HB_FUIOU_ID = "account_gshb_fuiou_id";//公司红包户富友ID


    private static final String PR_TYPE_ACCOUNT_HZJG = "account_hzjg";//合作机构收益户
    private static final String PR_TYPE_ACCOUNT_HZJG_FUIOU_ID = "account_hzjg_fuiou_id";//合作机构手续户富友ID

    private String jgsyAccount;

    private String gshbAccount;

    private String gshbAccountFuiouId;

    private String jgsyAccountFuiouId;

    private String hzjgsyAccount;

    private String hzjgsyAccountFuiouId;

    public String getJgsyAccount() {
        return jgsyAccount;
    }

    public String getGshbAccount() {
        return gshbAccount;
    }

    public String getGshbAccountFuiouId() {
        return gshbAccountFuiouId;
    }

    public String getJgsyAccountFuiouId() {
        return jgsyAccountFuiouId;
    }


    public String getHzjgsyAccount() {
        return hzjgsyAccount;
    }

    public String getHzjgsyAccountFuiouId() {
        return hzjgsyAccountFuiouId;
    }

    private CompanyAccounts(){}

    private SysParameterService parameterService;

    private void init(){
        parameterService = SpringContextHelper.getBean(SysParameterService.class);
        this.gshbAccount = findOneParam(PR_TYPE_ACCOUNT_HB);
        this.gshbAccountFuiouId = findOneParam(PR_TYPE_ACCOUNT_HB_FUIOU_ID);
        this.jgsyAccount = findOneParam(PR_TYPE_ACCOUNT_SY);
        this.jgsyAccountFuiouId = findOneParam(PR_TYPE_ACCOUNT_SY_FUIOU_ID);
        this.hzjgsyAccount = findOneParam(PR_TYPE_ACCOUNT_HZJG);
        this.hzjgsyAccountFuiouId = findOneParam(PR_TYPE_ACCOUNT_HZJG_FUIOU_ID);
    }

    private String findOneParam(String prType){
        List<SysParameter> orgs = parameterService.findByPrType(prType);
        String param = null;
        if(orgs.isEmpty()){
            log.error("未查询到所需参数：" + prType);
        }else{
            param = orgs.get(0).getPrValue();
        }
        return param;
    }

    private volatile boolean initiated = false;
    private static CompanyAccounts instance = new CompanyAccounts();
    public static CompanyAccounts getCompanyAccounts(){
        if(!instance.initiated){
            synchronized (CompanyAccounts.class){
                if(!instance.initiated){
                    instance.init();
                    instance.initiated = true;
                }
            }
        }
        return instance;
    }
}
