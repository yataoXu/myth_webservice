package com.zdmoney;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.zdmoney.utils.PoiReadExcel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 读取excel文件,生成sql语句并写入txt文件
 * Created by gaol on 2017/2/10
 **/
public class ImportCoinTest {

    public static final String REGEX = "\r\n";// 换行

    // excel文件根路径
    private static final String  excelFileUrl = "E:\\importCoin";

    // 需要生成的txt文件名称
    private static final String txtFileName = excelFileUrl + "\\sql\\天数≥360，金额≥1w_送10000捞财币.txt";

    private static StringBuilder resSql = new StringBuilder();

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    // 捞财币数量
    private static Long coin = 5000L;

    /**
     * 读取excel文件,生成sql语句
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入环境ip加端口加项目路径，按回车结束（如172.17.34.3:8087/zdpay_cashier）：");
        String ip = scanner.nextLine().trim();
        System.out.println("请输入手机号码");
        String mobile_no = scanner.nextLine().trim();
        System.out.println("请输入富友流水号（收银台流水号，如D20181010154803300001266）：");
        String mchnt_txn_ssn = scanner.nextLine().trim();
        System.out.println("请输入提现的金额(如1000)：");
        String amt = scanner.nextLine().trim();

//        String oriSrc = "localhost:8080/cashier/notify/drawerror?mchnt_cd=0002900F0352200&mobile_no=13267076571&";
        String oriSrc = ip + "/notify/drawerror?mchnt_cd=0002900F0352200&";

        String remark = "";

        String mchnt_txn_dt = "20180925";

        String signature = "DsG3dGfUR6YiH71wMAhmW8QYEdLMK3q4i3ccQPJeLSBgbXLbXMHTL6dLwlfcMAbchEsHvbXc2SYzZ3YM/zEUK8tV/7hAqvule8HQs0HKRRtx2SGZl57Niw1V1HQOXusXj+t5O4K3InSN0IGwpDmbiClwqIp3jbWiYAvba+RJw+8=";

        oriSrc += "mchnt_txn_ssn=" + mchnt_txn_ssn + "&";
        oriSrc += "amt=" + amt + "&";
        oriSrc += "signature=" + signature + "&";
        oriSrc += "remark=" + remark + "&";
        oriSrc += "mchnt_txn_dt=" + mchnt_txn_dt + "&";
        oriSrc += "mobile_no=" + mobile_no + "&";

        System.out.println(oriSrc.substring(0, oriSrc.length() - 1));

    }


    private static BigDecimal getRandomFolat(float scopeMin, float scopeMax){
        BigDecimal bd = new BigDecimal(Math.random() * (scopeMax - scopeMin) + scopeMin);
        return bd.setScale(3, BigDecimal.ROUND_HALF_UP);
    }

    public static void readExcel(){
        Long start = System.currentTimeMillis();
        List<Map<String, Object>> dataList = new ArrayList<>();
        try {
            // 读取excel文件内容
            dataList = PoiReadExcel.readExcel(new File(excelFileUrl + "\\excel\\20170203(天数≥360，金额≥1w).xls"));
            // 删除Excel表头
            dataList.remove(0);

            for (int i = 0; i<dataList.size(); i++) {
                Map<String, Object> map = dataList.get(i);
                Object idNum = map.get("c5");// 用户编号
                String seq =  "X7000"+i;
                String insert = "insert into passport.t_coin_record (ID, ACCOUNT_NO, RECORD_NUM, TIPS, DETAILS, START_COIN, COIN, " +
                        "END_COIN, DIRECTION, CREATE_DATE, ORDER_NUM, RED_RECORD_NUM, DEL_FLAG, COIN_TYPE, SHOW_FLAG) values" +
                        "('bcd2778f-b612-46cd-b8a8-"+seq+"', '"+idNum+"', '"
                        +sdf.format(new Date())+seq+"', '完成任务: 补送奖励', null, (select coin from passport.t_coin_account where account_no = "
                        +idNum+") , "+coin+", (select coin+"+coin+" from passport.t_coin_account where account_no = "
                        +idNum+"), 'IN', sysdate, null, null, 'NORMAL', 'CURRENT','SHOW');" + REGEX;
                String update = "update passport.t_coin_account set coin = coin+"+coin+" where account_no = "+idNum+";" + REGEX;
                resSql.append(insert).append(update).append(REGEX);
            }
            writerTxtContent(txtFileName, resSql.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("共生成sql:" + dataList.size() + "条,耗时:" +(System.currentTimeMillis() - start));
    }

    /**
     * 将sql语句写入txt文件
     * @param fileName
     * @param content
     */
    public static void writerTxtContent(String fileName, String content) {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(fileName, false);
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
