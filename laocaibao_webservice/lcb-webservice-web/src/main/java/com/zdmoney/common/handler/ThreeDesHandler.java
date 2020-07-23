package com.zdmoney.common.handler;/**
 * Created by pc05 on 2018/1/5.
 */

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
 * 描述 :
 *
 * @author : huangcy
 * @create : 2018-01-05 9:08
 * @email : huangcy01@zendaimoney.com
 **/
@Slf4j
public class ThreeDesHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, String s, JdbcType jdbcType){
        String str = "";
        try {
            str = s;
            if(StringUtils.isNotBlank(str)){
                preparedStatement.setString(i, ThreeDesUtil.encryptMode(str));
            }
        } catch (SQLException e) {
            log.error("ThreeDesHandler setNonNullParameter! parameter is [ " + str + " ]");
        }
    }

    @Override
    public String getNullableResult(ResultSet resultSet, String s) {
        String str = "";
        try {
            str = resultSet.getString(s);
            if(StringUtils.isNotBlank(str)){
                return ThreeDesUtil.decryptMode(str);
            }
        } catch (SQLException e) {
            log.error("ThreeDesHandler getNullableResult1! parameter is [ " + str + " ]");
        }
        return "";
    }

    @Override
    public String getNullableResult(ResultSet resultSet, int i){
        String str = "";
        try {
            str = resultSet.getString(i);
            if (StringUtils.isNotBlank(str)){
                return ThreeDesUtil.decryptMode(str);
            }
        } catch (SQLException e) {
            log.error("ThreeDesHandler getNullableResult2! parameter is [ " + str + " ]");
        }
        return "";
    }

    @Override
    public String getNullableResult(CallableStatement callableStatement, int i) {
        String str = "";
        try {
            str = callableStatement.getString(i);
            if (StringUtils.isNotBlank(str)){
                return ThreeDesUtil.decryptMode(str);
            }
        } catch (SQLException e) {
            log.error("ThreeDesHandler getNullableResult3! parameter is [ " + str + " ]");
        }
        return "";
    }
}
