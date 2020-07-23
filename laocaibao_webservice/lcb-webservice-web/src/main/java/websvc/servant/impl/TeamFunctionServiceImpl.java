package websvc.servant.impl;

import com.google.common.collect.Maps;
import com.zdmoney.common.Result;
import com.zdmoney.common.anno.FunctionId;
import com.zdmoney.service.BusiMallService;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.service.TeamService;
import com.zdmoney.service.team.TeamMemberApplyInfoService;
import com.zdmoney.service.team.TeamMemberInfoService;
import com.zdmoney.service.team.TeamTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websvc.models.*;
import websvc.req.ReqMain;
import websvc.servant.TeamFunctionService;

import java.util.Map;

@Service
public class TeamFunctionServiceImpl implements TeamFunctionService {

    @Autowired
    private BusiMallService busiMallService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private TeamMemberApplyInfoService teamMemberApplyInfoService;
    @Autowired
    private TeamTaskService teamTaskService;
    @Autowired
    private TeamMemberInfoService teamMemberInfoService;
    @Autowired
    private CustomerMainInfoService customerMainInfoService;


    @FunctionId("820001")
    public Result inviteFriendsIntegralRecord(ReqMain reqMain) throws Exception {
        Model_820001 cdtModel = (Model_820001) reqMain.getReqParam();
        return teamService.inviteFriendsIntegralRecord(cdtModel);
    }

    @FunctionId("820002")
    public Result lcCoin2GuaGuaKa(ReqMain reqMain) throws Exception {
        Model_820002 cdtModel = (Model_820002) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        busiMallService.lcCoin2GuaGuaKa(customerId);
        return Result.success();
    }

    @FunctionId("820003")
    public Result createTeam(ReqMain reqMain) throws Exception {
        Model_820003 cdtModel = (Model_820003) reqMain.getReqParam();
        return teamService.createTeam(cdtModel);
    }

    @FunctionId("820004")
    public Result joinTeam(ReqMain reqMain) throws Exception {
        Model_820004 cdtModel = (Model_820004) reqMain.getReqParam();
        return teamService.joinTeam(cdtModel);
    }

    @FunctionId("820005")
    public Result checkTeamApply(ReqMain reqMain) throws Exception {
        Model_820005 cdtModel = (Model_820005) reqMain.getReqParam();
        return teamService.checkTeamApply(cdtModel);
    }

    @FunctionId("820006")
    public Result inviteFriends(ReqMain reqMain) throws Exception {
        Model_820006 cdtModel = (Model_820006) reqMain.getReqParam();
        return teamService.inviteFriends(cdtModel);
    }

    @FunctionId("820007")
    public Result receiveTeamAward(ReqMain reqMain) throws Exception {
        Model_820007 cdtModel = (Model_820007) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        Long flowId = cdtModel.getFlowId();
        Map<String, String> resultMap = teamTaskService.receiveTaskReward(flowId,customerId);
        return Result.success(resultMap);
    }

    @FunctionId("820008")
    public Result checkTeamTask(ReqMain reqMain) throws Exception {
        Model_820008 cdtModel = (Model_820008) reqMain.getReqParam();
        return Result.success(teamTaskService.getTaskList(cdtModel.getCustomerId()));
    }

    @FunctionId("820009")
    public Result captainAudit(ReqMain reqMain) throws Exception {
        Model_820009 cdtModel = (Model_820009) reqMain.getReqParam();
        teamMemberApplyInfoService.captainAudit(cdtModel.getCustomerId(),cdtModel.getApplyId(),cdtModel.getType());
        return Result.success("审核完成",null);
    }

    @FunctionId("820010")
    public Result goCreateTeam(ReqMain reqMain) throws Exception {
        return teamService.goCreateTeam();
    }

    @FunctionId("820011")
    public Result tuHaoList(ReqMain reqMain) throws Exception {
        Model_820011 cdtModel = (Model_820011) reqMain.getReqParam();
        return teamService.tuHaoList(cdtModel);
    }

    @FunctionId("820012")
    public Result toLoginApply(ReqMain reqMain) throws Exception {
        Model_820012 cdtModel = (Model_820012) reqMain.getReqParam();
        return teamService.toLoginApply(cdtModel);

    }

    @FunctionId("820013")
    public Result toRegisterApply(ReqMain reqMain) throws Exception {
        return teamService.toRegisterApply(reqMain);
    }

    @FunctionId("820014")
    public Result checkIsMember(ReqMain reqMain) throws Exception {
        Model_820014 model_820014 = (Model_820014)(reqMain.getReqParam());
        Long customerId = model_820014.getCustomerId();
        customerMainInfoService.checkCustomerId(customerId);
        boolean isAdd = teamMemberInfoService.checkIsTeamMember(customerId);
        Map<String,String> map = Maps.newTreeMap();
        if(isAdd){
            map.put("isAdd","1");
        }else{
            map.put("isAdd","0");
        }
        return Result.success(map);
    }

    @FunctionId("820015")
    public Result getRankList(ReqMain reqMain) throws Exception {
        Model_820015 cdtModel = (Model_820015) reqMain.getReqParam();
        return teamService.teamRank(cdtModel);
    }

    @FunctionId("820016")
    public Result investFriendsInfo(ReqMain reqMain) throws Exception {
        Model_820016 model_820016 = (Model_820016) reqMain.getReqParam();
        return teamService.investFriendsInfo(model_820016);
    }

    @FunctionId("820017")
    public Result isInsideStaff(ReqMain reqMain) throws Exception {
        Model_820017 model_820017 = (Model_820017) reqMain.getReqParam();
        return teamService.isInsideStaff(model_820017);
    }

    @FunctionId("820018")
    @Override
    public Result queryAnnualBill2016(ReqMain reqMain) throws Exception {
        Model_820018 model_820018 = (Model_820018) reqMain.getReqParam();
        return teamService.queryAnnualBill2016(model_820018);
    }

}
