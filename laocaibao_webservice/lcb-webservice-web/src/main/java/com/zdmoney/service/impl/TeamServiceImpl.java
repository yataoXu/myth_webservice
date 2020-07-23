package com.zdmoney.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.zdmoney.common.Result;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.integral.api.common.dto.ResultDto;
import com.zdmoney.integral.api.dto.IntegralCommissionDto;
import com.zdmoney.integral.api.facade.IIntegralAccountFacadeService;
import com.zdmoney.mapper.CustInviteLineMapper;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.mapper.team.TeamMainInfoMapper;
import com.zdmoney.mapper.team.TeamMemberApplyInfoMapper;
import com.zdmoney.models.BusiAddupCustomerOrder;
import com.zdmoney.models.InvestFriendsInfo;
import com.zdmoney.models.customer.AnnualBill;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.sys.SysParameter;
import com.zdmoney.models.team.TeamMainInfo;
import com.zdmoney.models.team.TeamMemberApplyInfo;
import com.zdmoney.models.team.TeamMemberInfo;
import com.zdmoney.secure.utils.ThreeDesUtil;
import com.zdmoney.service.BusiAddupCustomerOrderService;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.service.SysParameterService;
import com.zdmoney.service.TeamService;
import com.zdmoney.service.team.TeamMainInfoService;
import com.zdmoney.service.team.TeamMemberApplyInfoService;
import com.zdmoney.service.team.TeamMemberInfoService;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.utils.IdcardValidatorUtil;
import com.zdmoney.utils.LaocaiUtil;
import com.zdmoney.utils.Page;
import com.zdmoney.web.dto.InviteIntegralDTO;
import com.zdmoney.web.dto.team.TeamApplyDTO;
import com.zdmoney.web.dto.team.TeamCreateDTO;
import com.zdmoney.web.dto.team.TeamListDTO;
import com.zendaimoney.laocaibao.wx.api.dto.WeiChantDto;
import com.zendaimoney.laocaibao.wx.api.facade.IWechatFacadeService;
import com.zendaimoney.laocaibao.wx.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import websvc.models.*;
import websvc.req.ReqMain;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class TeamServiceImpl implements TeamService {

    @Autowired
    private IIntegralAccountFacadeService integralAccountFacadeService;
    @Autowired
    private CustInviteLineMapper custInviteLineMapper;

    @Autowired
    private CustomerMainInfoMapper customerMainInfoMapper;

    @Autowired
    private TeamMainInfoService teamMainInfoService;

    @Autowired
    private TeamMemberInfoService teamMemberInfoService;
    @Autowired
    private TeamMemberApplyInfoService teamMemberApplyInfoService;

    @Autowired
    private SysParameterService sysParameterService;

    @Autowired
    private TeamMemberApplyInfoMapper teamMemberApplyInfoMapper;

    @Autowired
    private TeamMainInfoMapper teamMainInfoMapper;

    @Autowired
    private IWechatFacadeService wechatFacadeService;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private BusiAddupCustomerOrderService busiAddupCustomerOrderService;

    public Result inviteFriendsIntegralRecord(Model_820001 cdtModel) {
        CustomerMainInfo mainInfo = customerMainInfoMapper.selectByPrimaryKey(cdtModel.getCustomerId());
        if (mainInfo == null) {
            throw new BusinessException("customer.not.exist");
        }
        InviteIntegralDTO resDto = new InviteIntegralDTO();
        //查询累计邀请和当天邀请积分
        ResultDto<IntegralCommissionDto> resultDto = integralAccountFacadeService.searchCommissionByAccountNo(mainInfo.getCmNumber());
        if (resultDto.isSuccess()) {
            IntegralCommissionDto dto = resultDto.getData();
            if (dto != null) {
                resDto.setInviteAllIntegral(dto.getAllCommission());
                resDto.setInviteTodayIntegral(dto.getTodayCommission());
            }
        } else {
            throw new BusinessException("查询累计邀请和当日邀请积分失败");
        }
        //查询累计邀请人数
        int inviteNum = custInviteLineMapper.selectInviteNum(mainInfo.getCmCellphone());
        resDto.setInviteNum(inviteNum);
        resDto.setCmInviteCode(mainInfo.getCmInviteCode());
        int curMonthInviteNum = 0;
        Map map = Maps.newTreeMap();
        map.put("customerNo", mainInfo.getCmNumber());
        map.put("yearMonth", DateUtil.getDateFormatString(new Date(), "yyyyMM"));
        List<BusiAddupCustomerOrder> listOrders = busiAddupCustomerOrderService.selctBusiAddupCustomerOrder(map);
        if(!CollectionUtils.isEmpty(listOrders)){
            BusiAddupCustomerOrder listOrder =  listOrders.get(0);
            curMonthInviteNum = listOrder.getInviteFirstInvest();
        }
        String rate = "0.5%";
        if (curMonthInviteNum > 0 && curMonthInviteNum < 3) {
            rate = "0.8%";
        }
        if (curMonthInviteNum >= 3) {
            rate = "1%";
        }
        String tips = "本月您已成功邀请" + curMonthInviteNum + "人注资，下月将享受" + rate + "返利利率";
        resDto.setTips(tips);
        resDto.setCurMonthInviteNum(curMonthInviteNum);
        return Result.success(resDto);
    }

    public Result goCreateTeam() {
        //分配一个未被使用的teamID
        String teamId = "";
        while (true) {
            teamId = LaocaiUtil.getFixLenthString(5);
            //判断随机码是否使用过
            TeamMainInfo teamMainInfo = new TeamMainInfo();
            teamMainInfo.setTeamName(teamId);
            List<TeamMainInfo> list = teamMainInfoService.findByEntity(teamMainInfo);
            if (list.size() == 0) {
                break;
            }
        }

        //随机获取一个口号
        List<SysParameter> parameters = sysParameterService.findByPrType("teamSlogan");
        int parametNum = parameters.size();
        Random random = new Random();
        int index = random.nextInt(parametNum);
        String slogan = parameters.get(index).getPrValue();

        TeamCreateDTO teamCreateDTO = new TeamCreateDTO();
        teamCreateDTO.setTeamId(teamId);
        teamCreateDTO.setSlogan(slogan);

        return Result.success(teamCreateDTO);
    }

    public Result createTeam(Model_820003 model_820003) {
        Long custId = model_820003.getCustomerId();
        CustomerMainInfo customerMainInfo = customerMainInfoMapper.selectByPrimaryKey(custId);
        if (customerMainInfo != null) {
//			if (3 != customerMainInfo.getCmStatus()) {
//				throw new BusinessException("用户没有实名认证！");
//			}
            //用户是否已加入过队伍
            Map map = new HashMap();
            map.put("memberId", customerMainInfo.getId());
            map.put("memberStatus", AppConstants.TeamMemberStatus.TEAM_MEMBER_NORMAL);
            List<TeamMemberInfo> memberInfos = teamMemberInfoService.findTeamMemberInfo(map);
            int memberNum = memberInfos.size();
            if (memberNum != 0) {
                throw new BusinessException("您已加入队伍！");
            }
            //判断队伍编号是否已使用
            map.put("teamName", model_820003.getTeamId());
            List<TeamMainInfo> list = teamMainInfoMapper.findTeamMainInfo(map);
            if (list.size() != 0) {
                throw new BusinessException("小队编号已被使用，请重新创建！");
            }
            //创建队伍
            TeamMainInfo teamMainInfo = new TeamMainInfo();
            teamMainInfo.setTeamName(model_820003.getTeamId());
            teamMainInfo.setSlogan(model_820003.getSlogan());
            teamMainInfo.setCaptainId(customerMainInfo.getId());
            teamMainInfo.setCaptainCellphone(customerMainInfo.getCmCellphone());
            teamMainInfo.setCaptainName(customerMainInfo.getCmRealName());
            teamMainInfo.setTeamNum(1L); //队长一个人
            teamMainInfo.setTeamStatus((short) 1);
            teamMainInfo.setCreateTime(new Date());
            teamMainInfoService.save(teamMainInfo);
            //队长加入此队伍
            TeamMemberInfo captain = new TeamMemberInfo();
            captain.setMemberId(custId);
            captain.setMemberStatus(AppConstants.TeamMemberStatus.TEAM_MEMBER_NORMAL);
            captain.setTeamId(teamMainInfo.getId());
            captain.setMemberCellphone(customerMainInfo.getCmCellphone());
            captain.setMemberName(customerMainInfo.getCmRealName());
            captain.setMemberNumber(customerMainInfo.getCmNumber());
            captain.setEnqueueTime(new Date());
            teamMemberInfoService.save(captain);

        } else {
            throw new BusinessException("没有查询到用户信息！");
        }
        return Result.success("创建队伍成功！", null);
    }

    @Override
    public Result joinTeam(Model_820004 model_820004) {
        Long custId = model_820004.getCustomerId();
        CustomerMainInfo customerMainInfo = customerMainInfoMapper.selectByPrimaryKey(custId);
        if (customerMainInfo != null) {
//			if (3 != customerMainInfo.getCmStatus()) {
//				throw new BusinessException("用户没有实名认证！");
//			}
            //用户是否已加入过队伍
            Map paramsMap = new HashMap();
            paramsMap.put("memberId", customerMainInfo.getId());
            paramsMap.put("memberStatus", AppConstants.TeamMemberStatus.TEAM_MEMBER_NORMAL);
            List<TeamMemberInfo> memberInfos = teamMemberInfoService.findTeamMemberInfo(paramsMap);
            int memberNum = memberInfos.size();
            if (memberNum != 0) {
                throw new BusinessException("您已加入队伍！");
            }

            //所加入队伍人数是否已达上限
            TeamMainInfo teamMainInfo = teamMainInfoService.findOne(model_820004.getTeamId());
            if (teamMainInfo == null) {
                throw new BusinessException("队伍不存在！");
            }
            SysParameter sysParameter = sysParameterService.findOneByPrType("teamUpperLimit");
            int teamUpperLimit = Integer.valueOf(sysParameter.getPrValue());
            if (teamMainInfo.getTeamNum() >= teamUpperLimit) {
                throw new BusinessException("队伍人数已满！");
            }

            //是否向本队伍发起过申请
            TeamMemberApplyInfo applySearch = new TeamMemberApplyInfo();
            applySearch.setTeamId(teamMainInfo.getId());
            applySearch.setMemberId(customerMainInfo.getId());
            applySearch.setMemberStatus(AppConstants.TeamApplyStatus.TEAM_APPLY_AUDITING);
            List<TeamMemberApplyInfo> searchRes = teamMemberApplyInfoService.findByEntity(applySearch);
            if (searchRes.size() != 0) {
                throw new BusinessException("已申请过该队伍，在审核中！");
            }
            //检查邀请人id是否合法
            Long inviteId = model_820004.getInviteId();
            if (!org.springframework.util.StringUtils.isEmpty(inviteId)) {
                CustomerMainInfo iniviter = customerMainInfoMapper.selectByPrimaryKey(inviteId);
                if (iniviter == null) {
                    throw new BusinessException("邀请人不存在！");
                }
            }

            //发起加入申请
            TeamMemberApplyInfo applyInfo = new TeamMemberApplyInfo();
            applyInfo.setTeamId(teamMainInfo.getId());
            applyInfo.setApplyTime(new Date());
            applyInfo.setInviteId(model_820004.getInviteId());
            applyInfo.setMemberCellphone(customerMainInfo.getCmCellphone());
            applyInfo.setMemberName(customerMainInfo.getCmRealName());
            applyInfo.setMemberId(customerMainInfo.getId());
            applyInfo.setMemberStatus(AppConstants.TeamApplyStatus.TEAM_APPLY_AUDITING);
            teamMemberApplyInfoService.save(applyInfo);

            //如果队长已绑微信，向其推送微信消息
            CustomerMainInfo captainCustomerMainInfo = customerMainInfoMapper.selectByPrimaryKey(teamMainInfo.getCaptainId());
            if (captainCustomerMainInfo != null && StringUtils.isNotEmpty(captainCustomerMainInfo.getOpenId())) {
                Map<String, String> map = Maps.newTreeMap();
                map.put("keyword1", teamMainInfo.getTeamName());
                map.put("keyword2", customerMainInfo.getCmCellphone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                map.put("keyword3", "组队购买");
                map.put("keyword4", DateUtil.getDateFormatString(applyInfo.getApplyTime(), DateUtil.fullFormat));
                sendWxTemplateMsg(captainCustomerMainInfo.getOpenId(), Constants.MSG_TPL_JOIN_TEAM_REQUEST, map);
            }

            return Result.success("入队邀请发送成功，请耐心等待队长审核！", null);

        } else {
            throw new BusinessException("没有查询到用户信息！");
        }
    }

    @Override
    public Result checkTeamApply(Model_820005 model_820005) {
        Long custId = model_820005.getCustomerId();
        CustomerMainInfo customerMainInfo = customerMainInfoMapper.selectByPrimaryKey(custId);
        if (customerMainInfo != null) {
            //查询所在队伍
            TeamMainInfo teamSearch = new TeamMainInfo();
            teamSearch.setCaptainId(custId);
            List<TeamMainInfo> list = teamMainInfoService.findByEntity(teamSearch);
            if (list.size() == 0) {
                throw new BusinessException("用户不是队长，不能查看申请列表！");
            }
            TeamMainInfo teamMainInfo = list.get(0);
            //判断队伍人数是否已满
            SysParameter sysParameter = sysParameterService.findOneByPrType("teamUpperLimit");
            int teamUpperLimit = Integer.valueOf(sysParameter.getPrValue());
            int isOver = 0;
            if (teamMainInfo.getTeamNum() >= teamUpperLimit) {
                isOver = 1;
            }
            //返回结果集
            Map result = Maps.newTreeMap();
            result.put("isOver", isOver);


            //队伍申请列表，分页显示
            PageHelper.startPage(model_820005.getPageNo(), model_820005.getPageSize());
            List<TeamApplyDTO> resList = teamMemberApplyInfoMapper.getApplyList(teamMainInfo.getId());
            PageInfo<TeamApplyDTO> page = new PageInfo<TeamApplyDTO>(resList);
            //分页显示
            Page<TeamApplyDTO> pageDto = new Page<TeamApplyDTO>();
            pageDto.setResults(resList);
            pageDto.setPageNo(page.getPageNum());
            pageDto.setPageSize(page.getPageSize());
            pageDto.setTotalRecord((int) page.getTotal());
            pageDto.setTotalPage(page.getPages());

            result.put("pageDto", pageDto);
            return Result.success(result);

        } else {
            throw new BusinessException("没有查询到用户信息！");
        }
    }

    @Override
    public Result tuHaoList(Model_820011 model_820011) {
        SysParameter sysParameter = sysParameterService.findOneByPrType("teamUpperLimit");
        int teamUpperLimit = Integer.valueOf(sysParameter.getPrValue());
        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("teamUpperLimit", teamUpperLimit);
        searchMap.put("searchType", model_820011.getSeachType());
        searchMap.put("searchStr", model_820011.getSeachStr());
        searchMap.put("sDate", new SimpleDateFormat("yyyyMM").format(new Date()));

        PageHelper.startPage(model_820011.getPageNo(), model_820011.getPageSize());
        List<TeamListDTO> resList = teamMainInfoMapper.getTeamList(searchMap);
        PageInfo<TeamListDTO> page = new PageInfo<TeamListDTO>(resList);
        //分页显示
        Page<TeamListDTO> pageDto = new Page<TeamListDTO>();
        pageDto.setResults(resList);
        pageDto.setPageNo(page.getPageNum());
        pageDto.setPageSize(page.getPageSize());
        pageDto.setTotalRecord((int) page.getTotal());
        pageDto.setTotalPage(page.getPages());

//		//查询用户已加入队伍编号
//		TeamMemberInfo searchTeamMemberInfo = new TeamMemberInfo();
//		searchTeamMemberInfo.setMemberId(model_820011.getCustomerId());
//		searchTeamMemberInfo.setMemberStatus(AppConstants.TeamMemberStatus.TEAM_MEMBER_NORMAL);
//		List<TeamMemberInfo> memberInfos = teamMemberInfoService.findByEntity(searchTeamMemberInfo);
//		int memberNum = memberInfos.size();
//		Long teamId;
//		if(memberNum==0){
//			teamId=null;
//		}else{
//			teamId=teamMemberInfoService.findByEntity(searchTeamMemberInfo).get(0).getTeamId();
//		}
//
//		//查询用户的已申请队伍，返回一个数组
//		TeamMemberApplyInfo applyInfoSearch = new TeamMemberApplyInfo();
//		applyInfoSearch.setMemberId(model_820011.getCustomerId());
//		applyInfoSearch.setMemberStatus(AppConstants.TeamApplyStatus.TEAM_APPLY_AUDITING);
//		List<TeamMemberApplyInfo> applyList = teamMemberApplyInfoService.findByEntity(applyInfoSearch);
//		Long[] applyArray = new Long[applyList.size()];
//		for (int i = 0;i<applyList.size();i++){
//			applyArray[i] = applyList.get(i).getTeamId();
//		}
//
//		Map result = Maps.newHashMap();
//		result.put("teamId",teamId);
//		result.put("applyTeamArray",applyArray);
//		result.put("results",pageDto);

        return Result.success(pageDto);
    }

    @Override
    public Result inviteFriends(Model_820006 model_820006) {
        Long custId = model_820006.getCustomerId();
        CustomerMainInfo customerMainInfo = customerMainInfoMapper.selectByPrimaryKey(custId);
        if (customerMainInfo != null) {
            //判断被邀请人手机号
            String cellphone = model_820006.getCellphone();
            Pattern p = Pattern.compile("^1[0-9]{10}$");
            Matcher m = p.matcher(cellphone);
            if (!m.matches()) {
                throw new BusinessException("手机号格式不正确！");
            }
            CustomerMainInfo mainInfo = customerMainInfoMapper.selectByPhone(cellphone);
            if (mainInfo == null) {
                return Result.success("0");
            } else {
                return Result.success("1");
            }

        } else {
            throw new BusinessException("没有查询到用户信息！");
        }
    }

    @Override
    public Result toLoginApply(Model_820012 model_820012) throws Exception {
        Long custId = model_820012.getCustomerId();
        CustomerMainInfo iniviter = customerMainInfoMapper.selectByPrimaryKey(custId);
        String cellphone = model_820012.getCellphone();
        CustomerMainInfo invitee = customerMainInfoMapper.selectByPhone(cellphone);

        //验证被邀请人密码
        customerMainInfoService.checkLoginPassword(model_820012.getPassword(), invitee.getCmSalt(), invitee.getCmLoginPassword());

        TeamMemberInfo searchTeamMemberInfo = new TeamMemberInfo();
        searchTeamMemberInfo.setMemberId(iniviter.getId());
        searchTeamMemberInfo.setMemberStatus(AppConstants.TeamMemberStatus.TEAM_MEMBER_NORMAL);
        TeamMemberInfo teamMemberInfo = teamMemberInfoService.findByEntity(searchTeamMemberInfo).get(0);
        //申请加入
        if (invitee != null) {
            //用户是否已加入过队伍
            TeamMemberInfo memberInfo = new TeamMemberInfo();
            memberInfo.setMemberId(invitee.getId());
            memberInfo.setMemberStatus(AppConstants.TeamMemberStatus.TEAM_MEMBER_NORMAL);
            List<TeamMemberInfo> memberInfos = teamMemberInfoService.findByEntity(memberInfo);
            int memberNum = memberInfos.size();
            if (memberNum != 0) {
                throw new BusinessException("您已加入队伍！");
            }

            //所加入队伍人数是否已达上限
            TeamMainInfo teamMainInfo = teamMainInfoService.findOne(teamMemberInfo.getTeamId());
            if (teamMainInfo == null) {
                throw new BusinessException("队伍不存在！");
            }
            SysParameter sysParameter = sysParameterService.findOneByPrType("teamUpperLimit");
            int teamUpperLimit = Integer.valueOf(sysParameter.getPrValue());
            if (teamMainInfo.getTeamNum() >= teamUpperLimit) {
                throw new BusinessException("登录成功，队伍已满员，请到个人中心-组队投资中进行申请！");
            }

            //是否向本队伍发起过申请
            TeamMemberApplyInfo applySearch = new TeamMemberApplyInfo();
            applySearch.setTeamId(teamMainInfo.getId());
            applySearch.setMemberId(invitee.getId());
            applySearch.setMemberStatus(AppConstants.TeamApplyStatus.TEAM_APPLY_AUDITING);
            List<TeamMemberApplyInfo> searchRes = teamMemberApplyInfoService.findByEntity(applySearch);
            if (searchRes.size() != 0) {
                throw new BusinessException("已申请过该队伍，在审核中！");
            }

            //发起加入申请
            TeamMemberApplyInfo applyInfo = new TeamMemberApplyInfo();
            applyInfo.setTeamId(teamMainInfo.getId());
            applyInfo.setApplyTime(new Date());
            applyInfo.setInviteId(iniviter.getId());
            applyInfo.setMemberCellphone(invitee.getCmCellphone());
            applyInfo.setMemberName(invitee.getCmRealName());
            applyInfo.setMemberId(invitee.getId());
            applyInfo.setMemberStatus(AppConstants.TeamApplyStatus.TEAM_APPLY_AUDITING);
            teamMemberApplyInfoService.save(applyInfo);
            return Result.success("登录成功，您已向该队伍发起申请！", null);

        } else {
            throw new BusinessException("没有查询到用户信息！");
        }

    }

    @Override
    public Result toRegisterApply(ReqMain reqMain) throws Exception {
        Model_820013 model_820013 = (Model_820013) reqMain.getReqParam();
        Long custId = model_820013.getCustomerId();
        String ip = model_820013.getIp();
        CustomerMainInfo iniviter = customerMainInfoMapper.selectByPrimaryKey(custId);
        String cellphone = model_820013.getCellphone();

        //邀请人注册
        CustomerMainInfo invitee = customerMainInfoService.registerWithValidateCode(cellphone, model_820013.getPassword(), model_820013.getVerificationCode(), "", "", "", reqMain.getReqHeadParam(), ip,null);

        TeamMemberInfo searchTeamMemberInfo = new TeamMemberInfo();
        searchTeamMemberInfo.setMemberId(iniviter.getId());
        searchTeamMemberInfo.setMemberStatus(AppConstants.TeamMemberStatus.TEAM_MEMBER_NORMAL);
        TeamMemberInfo teamMemberInfo = teamMemberInfoService.findByEntity(searchTeamMemberInfo).get(0);
        //申请加入
        if (invitee != null) {
            //用户是否已加入过队伍
            TeamMemberInfo memberInfo = new TeamMemberInfo();
            memberInfo.setMemberId(invitee.getId());
            memberInfo.setMemberStatus(AppConstants.TeamMemberStatus.TEAM_MEMBER_NORMAL);
            List<TeamMemberInfo> memberInfos = teamMemberInfoService.findByEntity(memberInfo);
            int memberNum = memberInfos.size();
            if (memberNum != 0) {
                throw new BusinessException("您已加入队伍！");
            }

            //所加入队伍人数是否已达上限
            TeamMainInfo teamMainInfo = teamMainInfoService.findOne(teamMemberInfo.getTeamId());
            if (teamMainInfo == null) {
                throw new BusinessException("队伍不存在！");
            }
            SysParameter sysParameter = sysParameterService.findOneByPrType("teamUpperLimit");
            int teamUpperLimit = Integer.valueOf(sysParameter.getPrValue());
            if (teamMainInfo.getTeamNum() >= teamUpperLimit) {
                throw new BusinessException("奖励已发放，队伍已满员，请到个人中心-组队购买中进行申请！");
            }

            //是否向本队伍发起过申请
            TeamMemberApplyInfo applySearch = new TeamMemberApplyInfo();
            applySearch.setTeamId(teamMainInfo.getId());
            applySearch.setMemberId(invitee.getId());
            applySearch.setMemberStatus(AppConstants.TeamApplyStatus.TEAM_APPLY_AUDITING);
            List<TeamMemberApplyInfo> searchRes = teamMemberApplyInfoService.findByEntity(applySearch);
            if (searchRes.size() != 0) {
                throw new BusinessException("已申请过该队伍，在审核中！");
            }

            //发起加入申请
            TeamMemberApplyInfo applyInfo = new TeamMemberApplyInfo();
            applyInfo.setTeamId(teamMainInfo.getId());
            applyInfo.setApplyTime(new Date());
            applyInfo.setInviteId(iniviter.getId());
            applyInfo.setMemberCellphone(invitee.getCmCellphone());
            applyInfo.setMemberName(invitee.getCmRealName());
            applyInfo.setMemberId(invitee.getId());
            applyInfo.setMemberStatus(AppConstants.TeamApplyStatus.TEAM_APPLY_AUDITING);
            teamMemberApplyInfoService.save(applyInfo);
            return Result.success("奖励已发放，您已向该队伍发起申请！", null);

        } else {
            throw new BusinessException("没有查询到用户信息！");
        }
    }

    private void sendWxTemplateMsg(String openId, String type, Map<String, String> map) {
        try {
            WeiChantDto weiChantDto = new WeiChantDto();
            weiChantDto.setOpenId(openId);
            weiChantDto.setParam(map);
            weiChantDto.setTmlShortId(type);
            com.zendaimoney.laocaibao.wx.api.dto.ResultDto resultDto = wechatFacadeService.sendTemplateMsg(weiChantDto);
            if (resultDto.isSuccess()) {
                log.info(resultDto.getMsg());
            } else {
                log.error("微信推送失败：{} | openId: {} | map: {}", new Object[]{resultDto.getMsg(), openId, JSON.toJSONString(map)});
            }
        } catch (Exception e) {
            log.error("微信推送异常：{} | openId: {} | map: {}", new Object[]{e.getMessage(), openId, JSON.toJSONString(map)});
        }
    }

    @Override
    public Result teamRank(Model_820015 model_820015) {
        SysParameter sysParameter = sysParameterService.findOneByPrType("teamUpperLimit");
        int teamUpperLimit = Integer.valueOf(sysParameter.getPrValue());
        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("teamUpperLimit", teamUpperLimit);
        searchMap.put("searchStr", model_820015.getSearchStr());
        searchMap.put("curMonth",DateUtil.getDateFormatString(new Date(),"yyyyMM"));
        PageHelper.startPage(model_820015.getPageNo(), model_820015.getPageSize());
        List<TeamListDTO> resList = teamMainInfoMapper.getTeamRankList(searchMap);
        PageInfo<TeamListDTO> page = new PageInfo<>(resList);
        //分页显示
        Page<TeamListDTO> pageDto = new Page<>();
        pageDto.setResults(resList);
        pageDto.setPageNo(page.getPageNum());
        pageDto.setPageSize(page.getPageSize());
        pageDto.setTotalRecord((int) page.getTotal());
        pageDto.setTotalPage(page.getPages());
        return Result.success(pageDto);
    }

    @Override
    public Result investFriendsInfo(Model_820016 model_820016) {
        CustomerMainInfo mainInfo = customerMainInfoMapper.selectBycmNumber(model_820016.getCmNumber());
        if (mainInfo == null) throw new BusinessException("customer.not.exist");
        // 邀请人性别
        String mainGender = null;
        if(StringUtils.isNotBlank(mainInfo.getCmIdnum())){
            mainGender = IdcardValidatorUtil.getGenderByIdCard(mainInfo.getCmIdnum());
        }

        Map<String, Object> resMap = new HashMap<>();
        List<String> staffList = custInviteLineMapper.queryInsideStaffInfo();
        if(staffList != null && staffList.size() > 0){
            for(String str : staffList){
                if(mainInfo.getCmCellphone().equals(ThreeDesUtil.decryptMode(str))){
                    resMap.put("count", 0);
                    resMap.put("friendsList", null);
                    return Result.success(resMap);
                }
            }
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("cellPhone", mainInfo.getCmCellphone());
        paramMap.put("startDate", model_820016.getStartDate());
        paramMap.put("endDate", model_820016.getEndDate());
        List<InvestFriendsInfo> infoList = new ArrayList<>();
        int inviteCount = 0;
        if(2 == model_820016.getType()){
            infoList = custInviteLineMapper.isomerismFriendInvest(paramMap);
            List<InvestFriendsInfo> tempList = new ArrayList<>();
            for (int i = 0; i < infoList.size(); i++) {
                CustomerMainInfo custInfo = customerMainInfoMapper.selectByPhone(infoList.get(i).getInvitedCellPhone());
                if(custInfo == null || custInfo.getCmIdnum() == null) continue;
                String invitedGender = IdcardValidatorUtil.getGenderByIdCard(custInfo.getCmIdnum());
                if(invitedGender.equals(mainGender)) tempList.add(infoList.get(i));
            }
            infoList.removeAll(tempList);
        } else if ("3".equals(String.valueOf(model_820016.getType())) || 3 == model_820016.getType()) {
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.fullFormat);
            if (mainInfo.getRealNameTime() != null){
                int c1 = DateUtil.compareDate(sdf.format(mainInfo.getRealNameTime()), model_820016.getStartDate());
                int c2 = DateUtil.compareDate(sdf.format(mainInfo.getRealNameTime()), model_820016.getEndDate());
                // 用户状态为3(已实名) 认证时间大于活动开始时间, 且认证时间小于活动结束时间
                if (3 == mainInfo.getCmStatus() & c1 == 1 & c2 == -1) {
                    inviteCount += 1;
                    resMap.put("isNew", 0); // 老用户
                }
            } else {
                resMap.put("isNew", 1); // 新用户
            }
            int c3 = DateUtil.compareDate(sdf.format(mainInfo.getCmInputDate()), model_820016.getStartDate());
            int c4 = DateUtil.compareDate(sdf.format(mainInfo.getCmInputDate()), model_820016.getEndDate());
            if (1 == mainInfo.getCmStatus() & c3 == -1){// 在活动前仅注册未实名
                resMap.put("regNoAuth", true);
            } else if (1 == mainInfo.getCmStatus() & c3 == 1 & c4 == -1){ // 在活动期间注册为实名
                resMap.put("regNoAuth", false);
            }
            infoList = custInviteLineMapper.queryLotteryCount(paramMap);

            int total = (inviteCount == 0) ? infoList.size() : infoList.size() + inviteCount;
            resMap.put("count", total);
            resMap.put("friendsList", infoList);
            return Result.success(resMap);
        } else if ("4".equals(String.valueOf(model_820016.getType())) || 4 == model_820016.getType()){
            infoList = custInviteLineMapper.queryHelpFriendsKillMonster(paramMap);
        } else {
            List<String> list = custInviteLineMapper.investFriendsPhone(paramMap);
            if (CollectionUtils.isEmpty(list)) {
                resMap.put("count", 0);
                resMap.put("friendsList", null);
                return Result.success(resMap);
            }
            List<String> tempList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                paramMap.put("invitedCellphone", list.get(i));
                int count = custInviteLineMapper.isInvest(paramMap);
                if (count != 3) tempList.add(list.get(i));
            }
            list.removeAll(tempList);

            paramMap.put("phone", listToString(list));

            if (list != null && list.size() > 0) {
                if (0 == model_820016.getType()) {
                    infoList = custInviteLineMapper.investFriendsDetail(paramMap);
                } else if (1 == model_820016.getType()) {
                    infoList = custInviteLineMapper.investFriendsInfo(paramMap);
                }
            }
        }
        if (CollectionUtils.isEmpty(infoList)) {
            resMap.put("count", 0);
            resMap.put("friendsList", null);
            return Result.success(resMap);
        }

        int total = (inviteCount == 0) ? infoList.size() : infoList.size() + inviteCount;
        resMap.put("count", total);
        resMap.put("friendsList", infoList);
        return Result.success(resMap);
    }

    @Override
    public Result isInsideStaff(Model_820017 model_820017) {
        CustomerMainInfo mainInfo = customerMainInfoService.findOneBySessionToken(model_820017.getSessionToken());
        if (mainInfo == null) throw new BusinessException("查询不到用户信息！");

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("realName", mainInfo.getCmRealName());
        paramMap.put("idNum", mainInfo.getCmIdnum());
        int res = custInviteLineMapper.isInsideStaff(paramMap);
        return Result.success(res);
    }

    @Override
    public Result queryAnnualBill2016(Model_820018 model_820018) {
        CustomerMainInfo customerInfo = customerMainInfoService.findOneBySessionToken(model_820018.getSessionToken());
        if(customerInfo == null) throw new BusinessException("customer.not.exist");

        AnnualBill annualBill = teamMainInfoMapper.queryAnnualBill2016(customerInfo.getId());
        return Result.success(annualBill);
    }

    public String listToString(List list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append("'" + list.get(i) + "',");
                } else {
                    sb.append("'" + list.get(i) + "'");
                }
            }
        }
        return sb.toString();
    }

}
