package com.zdmoney.utils;

import com.zdmoney.life.api.utils.IdCardUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zdmoney.life.api.utils.IdCardUtils.conver15CardTo18;

/**
 * <p>
 * 类说明:身份证合法性校验
 * </p>
 * <p>
 * --15位身份证号码：第7、8位为出生年份(两位数)，第9、10位为出生月份，第11、12位代表出生日期，第15位代表性别，奇数为男，偶数为女。
 * --18位身份证号码
 * ：第7、8、9、10位为出生年份(四位数)，第11、第12位为出生月份，第13、14位代表出生日期，第17位代表性别，奇数为男，偶数为女。
 * </p>
 */

/**
 * Created by silence.cheng on 2016/7/7.
 */
public class IdcardValidatorUtil {

    protected static String codeAndCity[][] = { { "11", "北京" }, { "12", "天津" },
            { "13", "河北" }, { "14", "山西" }, { "15", "内蒙古" }, { "21", "辽宁" },
            { "22", "吉林" }, { "23", "黑龙江" }, { "31", "上海" }, { "32", "江苏" },
            { "33", "浙江" }, { "34", "安徽" }, { "35", "福建" }, { "36", "江西" },
            { "37", "山东" }, { "41", "河南" }, { "42", "湖北" }, { "43", "湖南" },
            { "44", "广东" }, { "45", "广西" }, { "46", "海南" }, { "50", "重庆" },
            { "51", "四川" }, { "52", "贵州" }, { "53", "云南" }, { "54", "西藏" },
            { "61", "陕西" }, { "62", "甘肃" }, { "63", "青海" }, { "64", "宁夏" },
            { "65", "新疆" }, { "71", "台湾" }, { "81", "香港" }, { "82", "澳门" },
            { "91", "国外" } };

    private static  String cityCode[] = { "11", "12", "13", "14", "15", "21", "22",
            "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43",
            "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63",
            "64", "65", "71", "81", "82", "91" };

    // 每位加权因子
    private static int power[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };

    // 第18位校检码
    private static String verifyCode[] = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };


    /**
     * 验证所有的身份证的合法性
     *
     * @param idcard
     * @return
     */
    public static boolean isValidatedAllIdcard(String idcard) {
//        if (idcard.length() == 15) {
//            idcard = convertIdcarBy15bit(idcard);
//        }
        return isValidate18Idcard(idcard);
    }
    /**
     * <p>
     * 判断18位身份证的合法性
     * @param idcard
     * @return
     */
    public static boolean isValidate18Idcard(String idcard) {
        // 非18位为假
        if (idcard.length() != 18) {
            return false;
        }
        // 获取前17位
        String idcard17 = idcard.substring(0, 17);
        // 获取第18位
        String idcard18Code = idcard.substring(17, 18);
        char c[] = null;
        String checkCode = "";
        // 是否都为数字
        if (isDigital(idcard17)) {
            c = idcard17.toCharArray();
        } else {
            return false;
        }

        if (null != c) {
            int bit[] = new int[idcard17.length()];

            bit = converCharToInt(c);

            int sum17 = 0;

            sum17 = getPowerSum(bit);

            // 将和值与11取模得到余数进行校验码判断
            checkCode = getCheckCodeBySum(sum17);
            if (null == checkCode) {
                return false;
            }
            // 将身份证的第18位与算出来的校码进行匹配，不相等就为假
            if (!idcard18Code.equalsIgnoreCase(checkCode)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 验证15位身份证的合法性,该方法验证不准确，最好是将15转为18位后再判断，该类中已提供。
     *
     * @param idcard
     * @return
     */
    public static boolean isValidate15Idcard(String idcard) {
        // 非15位为假
        if (idcard.length() != 15) {
            return false;
        }

        // 是否全都为数字
        if (isDigital(idcard)) {
            String provinceid = idcard.substring(0, 2);
            String birthday = idcard.substring(6, 12);
            int year = Integer.parseInt(idcard.substring(6, 8));
            int month = Integer.parseInt(idcard.substring(8, 10));
            int day = Integer.parseInt(idcard.substring(10, 12));

            // 判断是否为合法的省份
            boolean flag = false;
            for (String id : cityCode) {
                if (id.equals(provinceid)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return false;
            }
            // 该身份证生出日期在当前日期之后时为假
            Date birthdate = null;
            try {
                birthdate = new SimpleDateFormat("yyMMdd").parse(birthday);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (birthdate == null || new Date().before(birthdate)) {
                return false;
            }

            // 判断是否为合法的年份
            GregorianCalendar curDay = new GregorianCalendar();
            int curYear = curDay.get(Calendar.YEAR);
            int year2bit = Integer.parseInt(String.valueOf(curYear)
                    .substring(2));

            // 判断该年份的两位表示法，小于50的和大于当前年份的，为假
            if ((year < 50 && year > year2bit)) {
                return false;
            }

            // 判断是否为合法的月份
            if (month < 1 || month > 12) {
                return false;
            }

            // 判断是否为合法的日期
            boolean mflag = false;
            curDay.setTime(birthdate); // 将该身份证的出生日期赋于对象curDay
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    mflag = (day >= 1 && day <= 31);
                    break;
                case 2: // 公历的2月非闰年有28天,闰年的2月是29天。
                    if (curDay.isLeapYear(curDay.get(Calendar.YEAR))) {
                        mflag = (day >= 1 && day <= 29);
                    } else {
                        mflag = (day >= 1 && day <= 28);
                    }
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    mflag = (day >= 1 && day <= 30);
                    break;
            }
            if (!mflag) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * 将15位的身份证转成18位身份证
     *
     * @param idcard
     * @return
     */
    public static String convertIdcarBy15bit(String idcard) {
        String idcard17 = null;
        // 非15位身份证
        if (idcard.length() != 15) {
            return null;
        }

        if (isDigital(idcard)) {
            // 获取出生年月日
            String birthday = idcard.substring(6, 12);
            Date birthdate = null;
            try {
                birthdate = new SimpleDateFormat("yyMMdd").parse(birthday);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cday = Calendar.getInstance();
            cday.setTime(birthdate);
            String year = String.valueOf(cday.get(Calendar.YEAR));

            idcard17 = idcard.substring(0, 6) + year + idcard.substring(8);

            char c[] = idcard17.toCharArray();
            String checkCode = "";

            if (null != c) {
                int bit[] = new int[idcard17.length()];

                // 将字符数组转为整型数组
                bit = converCharToInt(c);
                int sum17 = 0;
                sum17 = getPowerSum(bit);

                // 获取和值与11取模得到余数进行校验码
                checkCode = getCheckCodeBySum(sum17);
                // 获取不到校验位
                if (null == checkCode) {
                    return null;
                }

                // 将前17位与第18位校验码拼接
                idcard17 += checkCode;
            }
        } else { // 身份证包含数字
            return null;
        }
        return idcard17;
    }

    /**
     * 15位和18位身份证号码的基本数字和位数验校
     *
     * @param idcard
     * @return
     */
    public static boolean isIdcard(String idcard) {
        return idcard == null || "".equals(idcard) ? false : Pattern.matches(
                "(^\\d{15}$)|(\\d{17}(?:\\d|x|X)$)", idcard);
    }

    /**
     * 15位身份证号码的基本数字和位数验校
     *
     * @param idcard
     * @return
     */
    public static boolean is15Idcard(String idcard) {
        return idcard == null || "".equals(idcard) ? false : Pattern.matches(
                "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$",
                idcard);
    }

    /**
     * 18位身份证号码的基本数字和位数验校
     *
     * @param idcard
     * @return
     */
    public static boolean is18Idcard(String idcard) {
        return Pattern
                .matches(
                        "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X]{1})$",
                        idcard);
    }

    /**
     * 数字验证
     *
     * @param str
     * @return
     */
    public static boolean isDigital(String str) {
        return str == null || "".equals(str) ? false : str.matches("^[0-9]*$");
    }

    /**
     * 验证是否是合法的身份证号码
     * @param idCard
     * @return
     */
    public static boolean is18ByteIdCard(String idCard) {
        Pattern pattern = Pattern.compile("^(\\d{6})(19|20)(\\d{2})(1[0-2]|0[1-9])(0[1-9]|[1-2][0-9]|3[0-1])(\\d{3})(\\d|X|x)?$"); //粗略的校验
        Matcher matcher = pattern.matcher(idCard);
        return matcher.matches() ? true : false;
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
     *
     * @param bit
     * @return
     */
    public static int getPowerSum(int[] bit) {

        int sum = 0;

        if (power.length != bit.length) {
            return sum;
        }

        for (int i = 0; i < bit.length; i++) {
            for (int j = 0; j < power.length; j++) {
                if (i == j) {
                    sum = sum + bit[i] * power[j];
                }
            }
        }
        return sum;
    }

    /**
     * 将和值与11取模得到余数进行校验码判断
     *
     * @param sum17
     * @return 校验位
     */
    public static String getCheckCodeBySum(int sum17) {
        String checkCode = null;
        switch (sum17 % 11) {
            case 10:
                checkCode = "2";
                break;
            case 9:
                checkCode = "3";
                break;
            case 8:
                checkCode = "4";
                break;
            case 7:
                checkCode = "5";
                break;
            case 6:
                checkCode = "6";
                break;
            case 5:
                checkCode = "7";
                break;
            case 4:
                checkCode = "8";
                break;
            case 3:
                checkCode = "9";
                break;
            case 2:
                checkCode = "x";
                break;
            case 1:
                checkCode = "0";
                break;
            case 0:
                checkCode = "1";
                break;
        }
        return checkCode;
    }

    /**
     * 将字符数组转为整型数组
     *
     * @param c
     * @return
     * @throws NumberFormatException
     */
    public static int[] converCharToInt(char[] c) throws NumberFormatException {
        int[] a = new int[c.length];
        int k = 0;
        for (char temp : c) {
            a[k++] = Integer.parseInt(String.valueOf(temp));
        }
        return a;
    }
    /**
     * 获得出生日期
     *
     * @param idcard
     * @return
     * @throws ParseException
     */
    public static Date getBirthDate(String idcard) throws ParseException {
        String year;
        String month;
        String day;
        if (idcard.length() == 18) { // 处理18位身份证
            year = idcard.substring(6, 10);
            month = idcard.substring(10, 12);
            day = idcard.substring(12, 14);
        } else { // 处理非18位身份证
            year = idcard.substring(6, 8);
            month = idcard.substring(8, 10);
            day = idcard.substring(10, 12);
            year = "19" + year;
        }
        return new SimpleDateFormat("yyyy-MM-dd").parse(year + "-" + month
                + "-" + day);

    }
    /**
     * 根据身份证计算周岁
     * @throws ParseException
     */
    public static int getAge(String idcard) throws ParseException {
        Calendar calBirth = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        Date birthDate = getBirthDate(idcard);
        calBirth.setTime(birthDate);
        return getYearDiff(today, calBirth);
    }

    public static int getYearDiff(Calendar cal, Calendar cal1) {
        int m = (cal.get(cal.MONTH)) - (cal1.get(cal1.MONTH));
        int y = (cal.get(cal.YEAR)) - (cal1.get(cal1.YEAR));
        return (y * 12 + m) / 12;
    }
    public static void main(String[] args) throws Exception {

        String idcard15 = "142431199001145";//15位 第7、8位为出生年份(两位数)，第9、10位为出生月份，第11、12位代表出生日期
        String idcard18 = "432503197505028819";//"121212121212121212";//18位
        /*System.out.println(IdcardValidatorUtil.isValidatedAllIdcard(idcard15));
        System.out.println(IdcardValidatorUtil.isValidatedAllIdcard(idcard18));

        System.out.println("15位转18位:" + IdcardValidatorUtil.convertIdcarBy15bit(idcard15));//142431202690011457
        System.out.println("15转18是否正确:" + IdcardValidatorUtil.isValidatedAllIdcard("142431202690011457"));

        System.out.println(IdcardValidatorUtil.getAge(idcard18));*/
        System.out.println(IdcardValidatorUtil.check18IdCard("32050419001131471X"));

    }

    /**
     * 根据身份编号获取性别
     * @param idCard 身份编号
     * @return 性别(B-男，G-女，N-未知)
     */
    public static String getGenderByIdCard(String idCard) {
        String sGender = "N";
        if (idCard.length() == IdCardUtils.CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        String sCardNum = idCard.substring(16, 17);
        return (Integer.parseInt(sCardNum) % 2 != 0) ? "B" : "G";
    }

    /**
     * 验证18位身份证号码是否合法
     * @param idcard
     * @return
     */
    public static boolean check18IdCard(String idcard) {
        // 校验身份证合法性
        if (idcard.length() != 18 || !is18ByteIdCard(idcard)) return false;

        String provinceid = idcard.substring(0, 2);
        String birthday = idcard.substring(6, 14);
        int year = Integer.parseInt(idcard.substring(6, 10));
        int month = Integer.parseInt(idcard.substring(10, 12));
        int day = Integer.parseInt(idcard.substring(12, 14));

        // 判断是否为合法的省份
        boolean flag = false;
        for (String id : cityCode) {
            if (id.equals(provinceid)) {
                flag = true;
                break;
            }
        }
        if (!flag)  return false;
        Date birthdate = null;
        try {
            birthdate = new SimpleDateFormat("yyyyMMdd").parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 该身份证生出日期在当前日期之后时为假
        if (birthdate == null || new Date().before(birthdate)) {
            return false;
        }

        // 判断是否为合法的年份
        GregorianCalendar curDay = new GregorianCalendar();
        int curYear = curDay.get(Calendar.YEAR);
        int year2bit = Integer.parseInt(String.valueOf(curYear).substring(2));

        // 判断该年份的两位表示法，小于50的和大于当前年份的，为假
        if ((year < 50 && year > year2bit)) return false;
        // 判断是否为合法的月份
        if (month < 1 || month > 12) return false;

        // 判断是否为合法的日期
        boolean mflag = false;
        curDay.setTime(birthdate); // 将该身份证的出生日期赋于对象curDay
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                mflag = (day >= 1 && day <= 31);
                break;
            case 2: // 公历的2月非闰年有28天,闰年的2月是29天。
                if (curDay.isLeapYear(curDay.get(Calendar.YEAR))) {
                    mflag = (day >= 1 && day <= 29);
                } else {
                    mflag = (day >= 1 && day <= 28);
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                mflag = (day >= 1 && day <= 30);
                break;
        }
        if (!mflag) return false;
        return true;
    }
}
