package websvc.servant.impl;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.Result;
import com.zdmoney.common.anno.FunctionId;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.euroCup.EuroMatchSchedule;
import com.zdmoney.service.CustInviteLineService;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.service.euroCup.EuroHeroListService;
import com.zdmoney.service.euroCup.EuroHeroVoteService;
import com.zdmoney.service.euroCup.EuroMatchScheduleService;
import com.zdmoney.service.euroCup.EuroMatchStatisticService;
import com.zdmoney.utils.LaocaiUtil;
import com.zdmoney.vo.TopInviteVo;
import com.zdmoney.web.dto.euroCup.EuroHeroListDTO;
import com.zdmoney.web.dto.euroCup.EuroMatchGuessDTO;
import com.zdmoney.web.dto.euroCup.LastGameResultDTO;
import com.zdmoney.web.dto.euroCup.OneDayWinner;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websvc.models.*;
import websvc.req.ReqMain;
import websvc.servant.EuroCupFunctionService;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class EuroCupFunctionServiceImpl implements EuroCupFunctionService {

    @Autowired
    private EuroMatchScheduleService euroMatchScheduleService;
    @Autowired
    private EuroMatchStatisticService euroMatchStatisticService;
    @Autowired
    private CustomerMainInfoService customerMainInfoService;
    @Autowired
    private EuroHeroListService euroHeroListService;
    @Autowired
    private EuroHeroVoteService euroHeroVoteService;
    @Autowired
    private CustInviteLineService custInviteLineService;
    @Autowired
    private ConfigParamBean configParamBean;


    @FunctionId("900101")
    public Result guessMatch(ReqMain reqMain) throws Exception {
        Model_900101 cdtModel = (Model_900101) reqMain.getReqParam();
        EuroMatchGuessDTO result = euroMatchScheduleService.guessMatch(cdtModel.getSessionToken());
        return Result.success(result);
    }

    @FunctionId("900102")
    public Result commitMatch(ReqMain reqMain) throws Exception {
        Model_900102 cdtModel = (Model_900102) reqMain.getReqParam();
        euroMatchScheduleService.commitMatch(cdtModel.getSessionToken(),cdtModel.getGuessRecord());
        return Result.success();
    }

    @Override
    @FunctionId("900103")
    public Result previousGameWinner(ReqMain reqMain) throws Exception {
        //合并分支测试
//        欧洲杯开始时间时间
        DateTime matchStartTime = DateTime.parse("2016-06-12");
        if(matchStartTime.isAfterNow()){
            return  Result.fail("敬请期待！");
        }

        Model_900103 model_900103 = (Model_900103)reqMain.getReqParam();
        String sessionToken = model_900103.getSessionToken();
        String cellPhone = "";
        if (StringUtils.isNotBlank(sessionToken)) {
            CustomerMainInfo mainInfo = customerMainInfoService.findOneBySessionToken(sessionToken);
            cellPhone = mainInfo.getCmCellphone();
        }

        //比赛时间
        Date lastMatchDate = euroMatchScheduleService.findLastMatchDate();

        //比赛场次
        List<EuroMatchSchedule> schedules = euroMatchScheduleService.findLastMatch(lastMatchDate);

        //比赛的中奖者
        List<OneDayWinner> winners = euroMatchStatisticService.previousGameWinner(lastMatchDate,cellPhone);

        LastGameResultDTO dto = new LastGameResultDTO();
        dto.setGameDate(lastMatchDate);
        dto.setSchedules(schedules);
        dto.setWinners(winners);
        return Result.success(dto);
    }

    @FunctionId("901001")
    public Result euroCupHeroList(ReqMain reqMain) throws Exception {
        Model_901001 model_901001 = (Model_901001)reqMain.getReqParam();
        String sessionToken = model_901001.getSessionToken();
        EuroHeroListDTO euroHeroListDTO = euroHeroListService.getEuroHeroList(sessionToken);
        return Result.success(euroHeroListDTO);
    }

    @FunctionId("901002")
    public Result euroCupHeroIsVoted(ReqMain reqMain) throws Exception {
        Model_901002 model_901002 = (Model_901002)reqMain.getReqParam();
        return euroHeroVoteService.euroCupHeroIsVoted(model_901002);
    }

    @FunctionId("901003")
    public Result euroCupHeroVote(ReqMain reqMain) throws Exception {
        return euroHeroVoteService.euroCupHeroVote(reqMain);
    }

    @FunctionId("902001")
    public Result contactsList(ReqMain reqMain) throws Exception {
        Model_902001 cdtModel = (Model_902001)reqMain.getReqParam();
        String sessionToken = StringUtils.trim(cdtModel.getSessionToken());
        Map<String, Object> result = Maps.newHashMap();
        Map<String, Object> map = Maps.newHashMap();
        map.put("startDate", "2016-08-01");
        map.put("endDate", "2016-08-15");
        map.put("type", "81"); // 81活动
        map.put("status", "2"); // 投资
        if (StringUtils.isNotBlank(sessionToken)) {
            try {
                String cmNumber = LaocaiUtil.sessionToken2CmNumber(sessionToken, configParamBean.getUserTokenKey());
                CustomerMainInfo customerMainInfo = customerMainInfoService.findOneByCmNumber(cmNumber);
                if (customerMainInfo != null) {
                    String cellphone = customerMainInfo.getCmCellphone();
                    map.put("cellphone", cellphone);
                    boolean isStaff = custInviteLineService.isStaff(map);
                    TopInviteVo inviteVo = custInviteLineService.getInviteCountByCellphone(map);
                    result.put("isLogin", true);
                    result.put("inviteCount", inviteVo.getInviteCount());
                    if(isStaff){
                        result.put("rank", 0);
                    }else {
                        map.put("inviteCount", inviteVo.getInviteCount());
                        map.put("mTime", inviteVo.getMTime());
                        int rank = custInviteLineService.getRankByCellphone(map);
                        result.put("rank", rank);
                    }
                }
            }catch (Exception e){
                result.put("isLogin", false);
                result.put("desc", "用户信息解析异常：" + e.getMessage());
            }
        }else{
            result.put("isLogin", false);
        }
        PageHelper.startPage(1, 10);
        List<TopInviteVo> custInviteLineList = custInviteLineService.getCustInviteLineList(map);
        result.put("custInviteLineList", custInviteLineList);
        if(custInviteLineList.isEmpty()){
            result.put("rank", 0);
        }
        return Result.success(result);
    }

}
