package com.zdmoney.common.handler;

import com.zdmoney.secure.utils.ThreeDesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Author: silence.cheng
 * Date: 2018/1/4 09:45
 * 描述 : 3des 类型转换
 */
@Slf4j
public class SecurityFieldTypeHandler extends BaseTypeHandler<String>{

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String s, JdbcType jdbcType){
        String str = "";
        try {
            str = s;
            if (StringUtils.isNotBlank(str)){
                ps.setString(i, ThreeDesUtil.encryptMode(s));
            }
        } catch (SQLException e) {
            log.error("SecurityFieldTypeHandler setNonNullParameter! parameter is [ " + str + " ]");
        }
    }

    @Override
    public String getNullableResult(ResultSet resultSet, String s){
        String str = "";
        try {
            str = resultSet.getString(s);
            if(StringUtils.isNotBlank(str)){
                return ThreeDesUtil.decryptMode(resultSet.getString(s));
            }
        } catch (SQLException e) {
            log.error("SecurityFieldTypeHandler getNullableResult1! parameter is [ " + str + " ]");
        }
        return "";
    }

    @Override
    public String getNullableResult(ResultSet resultSet, int i){
        String str = "";
        try {
            str = resultSet.getString(i);
            if(StringUtils.isNotBlank(str)){
                return ThreeDesUtil.decryptMode(resultSet.getString(i));
            }
        } catch (SQLException e) {
            log.error("SecurityFieldTypeHandler getNullableResult2! parameter is [ " + str + " ]");
        }
        return "";
    }

    @Override
    public String getNullableResult(CallableStatement callableStatement, int i){
        String str = "";
        try {
            str = callableStatement.getString(i);
            if (StringUtils.isNotBlank(str)){
                return ThreeDesUtil.decryptMode(callableStatement.getString(i));
            }
        } catch (SQLException e) {
            log.error("SecurityFieldTypeHandler getNullableResult3! parameter is [ " + str + " ]");
        }
        return "";
    }

}
