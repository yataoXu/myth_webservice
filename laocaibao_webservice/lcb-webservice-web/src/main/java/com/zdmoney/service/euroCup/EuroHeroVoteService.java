package com.zdmoney.service.euroCup;

import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.Result;
import com.zdmoney.common.service.BaseService;
import com.zdmoney.common.util.MD5;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.euroCup.EuroHeroVoteMapper;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.euroCup.EuroHeroVote;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.service.customer.CustomerValidateCodeService;
import com.zdmoney.utils.LaocaiUtil;
import com.zdmoney.web.dto.euroCup.EuroHeroVoteDTO;
import com.zdmoney.web.dto.euroCup.EuroHeroVoteResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import websvc.models.Model_901002;
import websvc.models.Model_901003;
import websvc.req.ReqMain;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by 00231247 on 2016/6/13.
 */
@Service
@Slf4j
public class EuroHeroVoteService extends BaseService<EuroHeroVote, Long> {

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private CustomerValidateCodeService customerValidateCodeService;

    @Autowired
    private EuroHeroListService euroHeroListService;

    private EuroHeroVoteMapper getEuroHeroVoteMapper() {
        return (EuroHeroVoteMapper) baseMapper;
    }

    public Result euroCupHeroIsVoted(Model_901002 model_901002){
        EuroHeroVoteDTO dto = new EuroHeroVoteDTO();
        String heroSessionToken = model_901002.getHeroSessionToken();
        String voterSessionToken = model_901002.getVoterSessionToken();
        //竞选人
        String heroCmNumber = LaocaiUtil.sessionToken2CmNumber(heroSessionToken, configParamBean.getUserTokenKey());
        CustomerMainInfo hero = customerMainInfoService.findOneByCmNumber(heroCmNumber);
        dto.setHeroCellphone(hero.getCmCellphone());

        //投票人
        CustomerMainInfo voter = null;
        if (StringUtils.isNotBlank(voterSessionToken)) {
            String voterCmNumber = LaocaiUtil.sessionToken2CmNumber(voterSessionToken, configParamBean.getUserTokenKey());
            voter = customerMainInfoService.findOneByCmNumber(voterCmNumber);
            if (voter != null) {
                String voterCellphone = voter.getCmCellphone();
                //今日是否已投票
                if(isVoteOneHero(hero.getCmCellphone(),voterCellphone)){
                    dto.setVoted(true);
                }
            }
        }
        return Result.success(dto);
    }

    public Result euroCupHeroVote(ReqMain reqMain) throws Exception{
        //欧洲杯投票截止时间
        DateTime voteEndTime = DateTime.parse("2016-07-26");
        if(voteEndTime.isBeforeNow()){
            return  Result.fail("投票已截止！");
        }
        EuroHeroVoteResultDTO result = new EuroHeroVoteResultDTO();
        Model_901003 model_901003 = (Model_901003)reqMain.getReqParam();
        String heroSessionToken = model_901003.getHeroSessionToken();
        String voterSessionToken = model_901003.getVoterSessionToken();
        String ip = model_901003.getIp();
        //竞选人
        String heroCmNumber = LaocaiUtil.sessionToken2CmNumber(heroSessionToken, configParamBean.getUserTokenKey());
        CustomerMainInfo hero = customerMainInfoService.findOneByCmNumber(heroCmNumber);
        String heroCellphone = hero.getCmCellphone();

        //投票人，如果传投票人sessionToken，则从投票人sessionToken取其信息，否则使用手机号和验证码校验用户
        CustomerMainInfo voter = null;
        String voterPhone = "";
        if (StringUtils.isNotBlank(voterSessionToken)) {
            String voterCmNumber = LaocaiUtil.sessionToken2CmNumber(voterSessionToken, configParamBean.getUserTokenKey());
            voter = customerMainInfoService.findOneByCmNumber(voterCmNumber);
            if (voter != null) {
                voterPhone = voter.getCmCellphone();
            }else{
                throw new BusinessException("投票人sessionToken无效！");
            }
        }else{
            voterPhone = model_901003.getVoterCellPhone();
            String voterValidateCode = model_901003.getVoterValidateCode();
            //判断被邀请人手机号
            Pattern p = Pattern.compile("^1[0-9]{10}$");
            Matcher m = p.matcher(voterPhone);
            if(!m.matches()){
                throw new BusinessException("手机号格式不正确！");
            }
            //校验验证码
            customerValidateCodeService.checkValidateCode(AppConstants.ValidateCode.HEROLIST_VOTE, voterPhone, voterValidateCode);
            voter = customerMainInfoService.findOneByPhone(voterPhone);
            if(voter==null){ //未注册
                //投票人注册
                result.setRegister(false);
                String cmPassword = StringUtils.right(voterPhone, 6);
                cmPassword = MD5.MD5Encode(cmPassword);
                voter = customerMainInfoService.register(voterPhone, cmPassword, null, null, null, AppConstants.RegisterChannel.EURO, reqMain.getReqHeadParam(), ip,null,null,null);
            }

        }

        //是否自己投自己
        if(heroCellphone.equals(voterPhone)){
            throw new BusinessException("用户不能给自己投票！");
        }
        //查询今日是否已投此用户
        if(isVoteOneHero(heroCellphone,voterPhone)){
            throw new BusinessException("今日已投过此用户！");
        }
        //投票
        EuroHeroVote oneVote=new EuroHeroVote();
        oneVote.setVoteDate(new Date());
        oneVote.setGuessMobile(Long.parseLong(heroCellphone));
        oneVote.setVoteMobile(Long.parseLong(voterPhone));
        getEuroHeroVoteMapper().insert(oneVote);

        euroHeroListService.addVote(heroCellphone);
        String sessionToken = LaocaiUtil.makeUserToken(configParamBean.getUserTokenKey(), voter.getCmNumber());
        result.setVoterSessionToken(sessionToken);
        return Result.success("您已经成功帮好友完成投票！",result);
    }

    //今日是否已投过某用户
    private boolean isVoteOneHero(String heroPhone,String voterPhone){
        boolean isVote = false;
        Date currentDate = new Date();
        DateTime currentDateTime = new DateTime(currentDate);
        Example example = new Example(EuroHeroVote.class);
        example.createCriteria().andCondition("to_char(VOTE_DATE,'yyyyMMdd')=", currentDateTime.toString("yyyyMMdd")).andCondition("GUESS_MOBILE=",heroPhone).andCondition("VOTE_MOBILE=",voterPhone);
        List<EuroHeroVote> votes = getEuroHeroVoteMapper().selectByExample(example);
        if(votes.size()>0){
            isVote = true;
        }
        return isVote;
    }

}
