package com.zdmoney.service.gameCenter;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.service.BaseService;
import com.zdmoney.gamecenter.api.common.dto.ResultDto;
import com.zdmoney.gamecenter.api.dto.FragmentRecordDto;
import com.zdmoney.gamecenter.api.dto.GainFragmentDto;
import com.zdmoney.gamecenter.api.facade.IFragmentFacadeService;
import com.zdmoney.mapper.gameCenter.DailyTaskRecordMapper;
import com.zdmoney.models.customer.CustomerSignInfo;
import com.zdmoney.models.gameCenter.DailyTaskRecord;
import com.zdmoney.models.lottey.LotterySign;
import com.zdmoney.service.lottery.LotterySignService;
import com.zdmoney.vo.GainFragmentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 00225181 on 2016/1/5.
 */
@Service
@Slf4j
public class DailyTaskRecordService extends BaseService<DailyTaskRecord, Long> {

    @Autowired
    private IFragmentFacadeService fragmentFacadeService;

    @Autowired
    private LotterySignService lotterySignService;

    @Autowired
    private ConfigParamBean configParamBean;

    private DailyTaskRecordMapper getDailyTaskRecordMapper() {
        return (DailyTaskRecordMapper) baseMapper;
    }


    /**
     *  @Description: 累计签到
     *  @author huangcy
     *  @date 2017/8/2
    */
    @Transient
    public void cumulativeSign(CustomerSignInfo signInfo, String cmNumber, Map<String,Object> resMap){
        //查询用户当月累计签到次数
        Map<String, Object> params = new HashMap<>();
        Date nowDate = new Date();
        params.put("cmNumber",cmNumber);
        params.put("startDate",new Date(DateUtil.beginOfMonth(nowDate).getTime()));
        params.put("endDate",new Date(DateUtil.endOfMonth(nowDate).getTime()));
        int signNum = getDailyTaskRecordMapper().countSameMonthSigns(params);
        resMap.put("signNum",signNum+1);
        resMap.put("fragment",new GainFragmentDto());
        //累计签到记录
        DailyTaskRecord dtr = new DailyTaskRecord();
        dtr.setCmNumber(cmNumber);
        dtr.setActivityType("2");
        dtr.setCreateTime(nowDate);
        int i = getDailyTaskRecordMapper().saveDailyTask(dtr);//每日任务记录
        if(i == 0){
            log.error("保存累计签到记录失败!method=[{}], params=[{}]","cumulativeSign",dtr);return;
        }
        //获取累计签到多少天能送宝箱
        LotterySign byDate = lotterySignService.findByDate();
        if (byDate == null){
            log.error("查询T_LOT_LOTTERY_SIGN失败 !method=[{}], params=[{}]","cumulativeSign","");return;
        }
        Integer signDays = byDate.getSignDays();
        //查询用户当月完成累计签到送的宝箱数量
        int num = 0;
        ResultDto<List<FragmentRecordDto>> listBySourceWithMonth = fragmentFacadeService.getListBySourceWithMonth(cmNumber, "3");
        if(!listBySourceWithMonth.isSuccess()){
            log.error("调用fragmentFacadeService获取签到送宝箱数量失败 !method=[{}], params=[{}],data=[{}]","cumulativeSign",cmNumber,listBySourceWithMonth);return;
        }
        num = listBySourceWithMonth.getData().size();
        if((int)(Math.floor((signNum+1)/signDays)) > num){ //如果累计完成签到数量/后台配置送宝箱天数 取整的整数 大于 用户获得宝箱的数量 则送宝箱
            ResultDto<GainFragmentDto> gfRes = fragmentFacadeService.gainFragment(cmNumber, "3");
            if(!gfRes.isSuccess() || gfRes.getData() == null){
                log.error("调用fragmentFacadeService发放碎片失败 !method=[{}], params=[{}],data=[{}]","cumulativeSign",cmNumber,gfRes);return;
            }
            GainFragmentDto data = gfRes.getData();
            GainFragmentVo gainFragmentVo = JSONObject.parseObject(JSON.toJSONString(data), GainFragmentVo.class);
            gainFragmentVo.setElephantParkUrl(configParamBean.getElephantParkUrl());
            signInfo.setChestStatus(1);
            resMap.put("fragment",gainFragmentVo);
            resMap.put("awardType","4");
        }
        return;
    }

    public static void main(String[] args) {
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //DateTime dateTime = DateUtil.beginOfMonth(new Date());
        //DateTime dateTime1 = DateUtil.endOfMonth(new Date());
        //System.out.println(sdf.format(dateTime1));
        GainFragmentDto gainFragmentDto = new GainFragmentDto();
        GainFragmentVo gainFragmentVo = JSONObject.parseObject(JSON.toJSONString(gainFragmentDto), GainFragmentVo.class);
        System.out.println(gainFragmentVo);
    }
}
