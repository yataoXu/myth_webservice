package com.zdmoney.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class CoreUtil {

	static String[] nums = new String[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };

	static String HanDiviStr[] = new String[] { "", "拾", "佰", "仟" };
	static String ts[] = new String[] { "", "万", "亿" };
	static String[] mus = new String[] { "元", "角", "分" };
	static String[] RMBBigWord = new String[]{"亿","千万","百万","十万","万","千","百","十","元"};
	public final static String ZHEN = "整";
	static DecimalFormat ndf = new DecimalFormat();
	static BigDecimal HUNDRED = new BigDecimal("100");
	static {
		ndf.setRoundingMode(RoundingMode.HALF_UP);
	}

	/**
	 * 舍去小数点后两位数据
	 * 
	 * @param v
	 * @return
	 */
	public static Double DoubleAccurate(Double v) {
		if (v == null) {
			return 0D;
		}
		DecimalFormat df = new DecimalFormat("####.#########################");
		String vs = df.format(v);
		String[] varr = vs.split("[.]");

		int l = 0;
		if (varr.length == 2) {
			l = varr[1].length() >= 2 ? 2 : varr[1].length();
			vs = varr[1].substring(0, l);

			return Double.valueOf(varr[0] + "." + vs);
		} else {
			return v;
		}
	}
	
	/**
	 * 舍去小数点后两位数据
	 * 
	 * @param v
	 * @return
	 */
	public static String BigDecimalAccurate(BigDecimal v) {
	   DecimalFormat myformat=new DecimalFormat("####.#########################");
	   String str = myformat.format(v);  
	   String[] varr = str.split("[.]");
	   int l = 0;
		if (varr.length == 2) {
			l = varr[1].length() >= 2 ? 2 : varr[1].length();
			str = varr[1].substring(0, l);
			return varr[0] + "." + str;
		} else {
			return str;
		}
	}
	
	/**
	 * 舍去小数点后L位数据
	 * 
	 * @param v
	 * @return
	 */
	public static Double DoubleAccurate(Double v, int L) {
		if (v == null) {
			return 0D;
		}
		DecimalFormat df = new DecimalFormat("####.#########################");
		String vs = df.format(v);
		String[] varr = vs.split("[.]");

		int l = 0;
		if (varr.length == 2) {
			l = varr[1].length() >= L ? L : varr[1].length();
			vs = varr[1].substring(0, l);

			return Double.valueOf(varr[0] + "." + vs);
		} else {
			return v;
		}
	}
	
	public static String BigDecimalAccurate(BigDecimal v, int L) {
		if (v == null) {
			return "";
		}
		DecimalFormat df = new DecimalFormat("####.#######");
		String vs = df.format(v);
		String[] varr = vs.split("[.]");
		int l = 0;
		if (varr.length == 1){
			vs += ".";
			for(int i=0;i<L;i++){
				vs += "0";
			}
			return vs;
		}else if (varr.length == 2) {
			l = varr[1].length() >= L ? L : varr[1].length();
			vs = varr[1].substring(0, l);
			String str = varr[0] + "." + vs;
			if(vs.length() < L){
				for(int i=0;i<L-1;i++){
					str += "0";
				}
			}
			return str;
		} else {
			return vs;
		}
	}
	
	public static String DoubleVString(Double v) {
		if (v == null) {
			return "0";
		}
		DecimalFormat df = new DecimalFormat("####.#########################");
		String vs = df.format(v);
		String[] varr = vs.split("[.]");

		int l = 0;
		if (varr.length == 2) {
			l = varr[1].length() >= 2 ? 2 : varr[1].length();
			vs = varr[1].substring(0, l);

			return Double.valueOf(varr[0] + "." + vs).toString();
		} else {
			return v.toString();
		}
	}
	
	public static String Double2String(Double v) {
		if (v == null) {
			return "0";
		}
		String vs = v.toString();
		String[] varr = vs.split("[.]");

		int l = 0;
		if (varr.length == 2) {
			l = varr[1].length() >= 2 ? 2 : varr[1].length();
			vs = varr[1].substring(0, l);
			if (vs.length() == 1) {
				vs += "0";
			}
			return varr[0] + "." + vs;
		} else {
			return v.toString();
		}
	}

	/**
	 * 如果传入的str长度小于14（为null时直接返回14个0）则在前面补0补到14位，若长度大于等于14则返回传入的数值
	 */
	private static String complete(String s) {
		if (s == null)
			return "00000000000000";
		int len = s.length();
		int tmp = 14 - len;
		for (int i = 0; i < tmp; i++) {
			s = "0" + s;
		}
		return s;
	}

	public static String format(double d, int num) {
		ndf.setMaximumFractionDigits(num);
		ndf.setMinimumFractionDigits(num);
		return ndf.format(d);
	}

	/**
	 * 将传入的double表示成大写金额，所接受参数整数位数不得超过12 位，程序自动四舍五入保留两位小数后开始翻译。
	 *
	 * @throws RuntimeException
	 */
	public static String getAmtInWords(double d) throws RuntimeException {
		try {
			String symbol = "";
			if (d < 0)
				symbol = "-";
			String amtStr = complete(String.valueOf((long) (Math.abs(new BigDecimal(format(d, 2).replaceAll(",", ""))
					.multiply(HUNDRED).doubleValue()))));
			if (amtStr.length() > 14)
				throw new RuntimeException("金额转换为大写金额时最大单位不得超过千亿");
			String yuanStr = amtStr.substring(0, 12);
			char jiaoChar = amtStr.charAt(12);
			char fenChar = amtStr.charAt(13);

			String head = "";
			String tail = "";
			if (jiaoChar + fenChar - '0' - '0' == 0)
				tail = ZHEN;
			else if (jiaoChar - '0' == 0) {
				tail = nums[0] + nums[fenChar - '0'] + mus[2];
			} else if (fenChar - '0' == 0) {
				tail = nums[jiaoChar - '0'] + mus[1] + ZHEN;
			} else {
				tail = nums[jiaoChar - '0'] + mus[1] + nums[fenChar - '0'] + mus[2];
			}

			for (int i = 2; i >= 0; i--) {
				String s = "";
				for (int j = 3; j >= 0; j--) {
					int tmp = yuanStr.charAt(11 - i * 4 - j) - '0';
					if (tmp != 0) {
						s = s + nums[tmp] + HanDiviStr[j];
					} else {
						s = s + nums[0];
					}
				}
				s = s.replaceAll("零+", "零");
				s = s.replaceAll("零+\\b", "");
				if (!s.equals("")) {
					head += s + ts[i];
				}
			}

			head = head.replaceAll("\\b零+", "");
			head = head.replaceAll("零+\\b", "");
			head = head.replaceAll("零+", "零");
			if (head.equals(""))
				head = "零";

			return symbol + head + mus[0] + tail;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String[][] getAmtPerWord(double d){
		String dd = String.valueOf(d);
		if(dd.indexOf(".") > 0){
			String[] dds = dd.split("\\.");
			String[][] ddss = new String[dds[0].length()+2][2];
			for(int i=0;i<dds[0].length();i++){
				char c = dds[0].charAt(i);
				String s = getBigWord(c);
				String rmbbigWord = RMBBigWord[RMBBigWord.length-dds[0].length()+i];
				ddss[i][0]=rmbbigWord;
				ddss[i][1]=s;
			}
			ddss[ddss.length-2][0] = "角";
			if(dds[1].length()>=1){
				char jiao = dds[1].charAt(0);
				ddss[ddss.length-2][1] = getBigWord(jiao);
			}else{
				ddss[ddss.length - 1][1] = getBigWord("0".toCharArray()[0]);
			}

			ddss[ddss.length - 1][0] = "分";
			if(dds[1].length()>=2) {
				char fen = dds[1].charAt(1);
				ddss[ddss.length - 1][1] = getBigWord(fen);
			}else{
				ddss[ddss.length - 1][1] = getBigWord("0".toCharArray()[0]);
			}
			return ddss;
		}
		return new String[][]{};
	}

	private static String getBigWord(char c){
		String s = "";
		for (int j = 3; j >= 0; j--) {
			int tmp = c - '0';
			if (tmp != 0) {
				s = s + nums[tmp];
			} else {
				s = s + nums[0];
			}
			return s;
		}
		return "";
	}

	public static String subZeroAndDot(String s){
		if(s.indexOf(".") > 0){
			s = s.replaceAll("0+?$", "");//去掉多余的0
			s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
		}
		return s;
	}

	/**
	 * 替换中间替换为*, 保留字符串前6后4, 不改变字符串长度
	 * @param str
	 * @return
	 */
	public static String strReplace(String str) {
		return str.replaceAll("(?<=\\d{6})\\d(?=\\d{4})", "*");
	}

	/**
	 * 判断一个字符串是否都为数字
	 * @param strNum
	 * @return
	 */
	public static boolean isDigit(String strNum) {
		return strNum.matches("[0-9]{1,}");
	}

	public static void main(String[] args) {
		String[][] ddss = getAmtPerWord(5000000.5d);
		StringBuffer sbf = new StringBuffer();
		for(int i=0;i<ddss.length;i++){
			sbf.append(ddss[i][1]+ddss[i][0]);
		}
		System.out.println(sbf.toString());
	}
}
