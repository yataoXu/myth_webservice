package com.zdmoney.service.impl;

import com.zdmoney.mapper.bank.CustomerBankAccountMapper;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.models.bank.CustomerBankAccount;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.service.ICustomerInfoService;
import com.zdmoney.utils.Page;
import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.customer.CustomerBankAccountVO;
import com.zdmoney.webservice.api.dto.customer.CustomerInfoVO;
import com.zdmoney.webservice.api.dto.customer.ExpandedCustomerInfoVO;
import com.zdmoney.webservice.api.facade.IBusiBorrowCertificateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by user on 2017/9/5.
 */
@Service
@Slf4j
public class CustomerInfoServiceImpl implements ICustomerInfoService {

    @Autowired
    private CustomerMainInfoMapper customerMainInfoMapper;

    @Autowired
    private CustomerBankAccountMapper customerBankAccountMapper;

    @Autowired
    private IBusiBorrowCertificateService iBusiBorrowCertificateService;

    @Override
    public PageResultDto<CustomerInfoVO> getCustomerByParams(Map<String, Object> map) {
        return doSearch(map, new Mapper<CustomerInfoVO>() {

            @Override
            public List<CustomerInfoVO> query(Map<String, Object> map) {
                return customerMainInfoMapper.getCustomerByParams(map);
            }
        });
    }

    @Override
    public ResultDto<Integer> updateByCustomerId(CustomerMainInfo model) {
        if (model.getId() == null) {
            return ResultDto.FAIL("ID不能为空");
        }
        try {
            customerMainInfoMapper.updateByCustomerId(model);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResultDto.FAIL("操作失败");
        }
        return ResultDto.SUCCESS();
    }

    interface Mapper<T> {
        List<T> query(Map<String, Object> map);
    }

    static <T, M extends Mapper<T>> PageResultDto<T> doSearch(Map<String, Object> map, M mapper) {
        PageResultDto<T> resultDto = new PageResultDto<>();
        if (map.get("pageNo") == null || map.get("pageSize") == null) {
            resultDto.setCode(PageResultDto.ERROR_CODE);
            resultDto.setMsg("分页参数丢失");
            return resultDto;
        }
        Page<T> page = new Page<>();
        page.setBaseParam(map);
        map.put("page", page);
        try {
            List<T> list = mapper.query(map);

            resultDto.setCode(PageResultDto.SUCCESS_CODE);
            resultDto.setTotalSize(page.getTotalRecord());
            resultDto.setTotalPage(page.getTotalPage());
            resultDto.setPageNo(page.getPageNo());
            resultDto.setDataList(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultDto.setCode(PageResultDto.ERROR_CODE);
            resultDto.setMsg("查询数据异常");
        }
        return resultDto;
    }

    @Override
    public PageResultDto<ExpandedCustomerInfoVO> searchCustomersWithPlannerInfo(Map<String, Object> map) {
        return doSearch(map, new Mapper<ExpandedCustomerInfoVO>() {

            @Override
            public List<ExpandedCustomerInfoVO> query(Map<String, Object> map) {
                return customerMainInfoMapper.selectCustomerWithPlannerInfo(map);
            }
        });
    }

    @Override
    @Transactional
    public ResultDto<Integer> modifyCustomerMemberType(Long id) {
        CustomerMainInfo model = new CustomerMainInfo();
        model.setId(id);
        model.setMemberType(Integer.valueOf(3));
        ResultDto<Integer> resultDto = new ResultDto<>();
        try {
            int num = customerMainInfoMapper.updateByCustomerId(model);
            model = customerMainInfoMapper.selectByCustomerId(id);
            if (StringUtils.isNotBlank(model.getCmInviteCode())) {
                customerMainInfoMapper.modifyInviteeMemberTypeByIntroduceCode(model.getCmInviteCode());
            }
            resultDto.setCode(ResultDto.SUCCESS_CODE);
            resultDto.setData(Integer.valueOf(num));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
        return resultDto;
    }

    @Override
    public PageResultDto<ExpandedCustomerInfoVO> searchCustomersBankAccountInfo(Map<String, Object> map) {
        return doSearch(map, new Mapper<ExpandedCustomerInfoVO>() {

            @Override
            public List<ExpandedCustomerInfoVO> query(Map<String, Object> map) {
                return customerMainInfoMapper.selectCustomersBankAccountInfo(map);
            }
        });
    }

    @Override
    public ResultDto<ExpandedCustomerInfoVO> searchBankAccountDetail(Long id) {
        ResultDto<ExpandedCustomerInfoVO> resultDto = new ResultDto<>();
        try {
            ExpandedCustomerInfoVO detail = customerMainInfoMapper.selectBankAccountDetail(id);
            resultDto.setCode(ResultDto.SUCCESS_CODE);
            resultDto.setData(detail);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultDto.setCode(ResultDto.ERROR_CODE);
            resultDto.setMsg(e.getMessage());
        }
        return resultDto;
    }

    @Override
    public ResultDto<Integer> updateCustomerBankAccount(CustomerBankAccountVO model) {
        CustomerBankAccount bean = new CustomerBankAccount();
        BeanUtils.copyProperties(model, bean);
        ResultDto<Integer> resultDto = new ResultDto<>();
        try {
            int num = customerBankAccountMapper.updateByCustomerId(bean);
            resultDto.setCode(ResultDto.SUCCESS_CODE);
            resultDto.setData(Integer.valueOf(num));
        } catch (Exception e) {
            resultDto.setCode(ResultDto.ERROR_CODE);
            resultDto.setMsg(e.getMessage());
        }
        return resultDto;
    }

    @Override
    public ResultDto<ExpandedCustomerInfoVO> selectOneCustomerBankAccount(Map<String, Object> map) {
        ResultDto<ExpandedCustomerInfoVO> resultDto = new ResultDto<>();
        try {
            ExpandedCustomerInfoVO customerInfoVO = customerMainInfoMapper.selectOneCustomerBankAccount(map);
            resultDto.setCode(ResultDto.SUCCESS_CODE);
            resultDto.setData(customerInfoVO);
        } catch (Exception e) {
            resultDto.setCode(ResultDto.ERROR_CODE);
            resultDto.setMsg(e.getMessage());
        }
        return resultDto;
    }

    @Override
    public ResultDto<CustomerInfoVO> selectOneCustomerInfo(String cmNumber) {
        ResultDto<CustomerInfoVO> resultDto = new ResultDto<>();
        try {
            CustomerMainInfo mainInfo = customerMainInfoMapper.selectBycmNumber(cmNumber);
            CustomerInfoVO vo = new CustomerInfoVO();
            vo.setCmNumber(mainInfo.getCmNumber());
            vo.setAccountType(mainInfo.getAccountType());
            vo.setFuiouLoginId(mainInfo.getFuiouLoginId());
            resultDto.setData(vo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultDto.setCode(ResultDto.ERROR_CODE);
            resultDto.setMsg(e.getMessage());
        }
        return resultDto;
    }

    @Override
    public PageResultDto<CustomerInfoVO> getCustomerByNameAndPhone(Map<String, Object> map) {
        return doSearch(map, new Mapper<CustomerInfoVO>() {

            @Override
            public List<CustomerInfoVO> query(Map<String, Object> map) {
                List<CustomerInfoVO> infoVOS = customerMainInfoMapper.getCustomerByNameAndPhone(map);
                for (CustomerInfoVO infoVO:infoVOS) {
                    //将查询到的在投本金放在该字段中
                    infoVO.setOpenId(iBusiBorrowCertificateService.getHoldAsset(infoVO.getId().toString()).getData());
                }
                return infoVOS;
            }
        });
    }
}
