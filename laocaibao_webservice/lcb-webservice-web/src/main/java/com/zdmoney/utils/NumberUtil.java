package com.zdmoney.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * ******声明*************
 * 
 * 版权所有：zendaimoney
 * 
 * 项目名称：id5pro 类 名称：NumberUtil 功能描述： 序列号生成器
 * 
 * 创建人员：joe 创建时间：2015年6月23日
 * 
 * @version 修改记录************ 修改人员： 修改时间： 修改描述：
 */
public class NumberUtil {
	/** 最大值 */
	public static final long MAX = 9223372036854775807L; // 36*35*34*33*32*31*30*29

	/** 乘法用的素数 */
	public static final long P = 982451653L;

	/** 加法用的素数 */
	public static final long Q = 9007;

	/** 编码长度 */
	public static final int LEN = 8;

	/** 采用36进制 */
	public static final int RADIX = 36;

	private static Random randGen = null;
	private static char[] numbersAndLetters = null;

	/**
	 * 编码方法。
	 * 
	 * @param number
	 *            序号
	 * @return 8位编码
	 * @throws IllegalArgumentException
	 *             如果序号超过范围
	 */
	public static String encode(long number) {
		if (number <= 0 || number > MAX) {
			throw new IllegalArgumentException();
		}
		long x = (number * P + Q) % MAX;

		return encode0(x);
	}

	private static String encode0(long x) {
		List<Character> list = new LinkedList<Character>();
		for (int i = 0; i < RADIX; i++) {
			list.add(Character.toUpperCase(Character.forDigit(i, RADIX)));
		}

		char[] codes = new char[LEN];
		int radix = RADIX;
		for (int i = LEN; i-- > 0;) {
			int n = (int) Math.abs(x % radix);
			codes[i] = list.remove(n);
			x /= radix;
			radix--;
		}

		return new String(codes);
	}

	public static final String randomString(int length) {
		if(length < 1){
			return null;
		}
		String a = "0123456789abcdefghijklmnopqrstuvwxyz";
		char[] rands = new char[6];
		for (int i = 0; i < rands.length; i++)
		{
			int rand = (int) (Math.random() * a.length());
			rands[i] = a.charAt(rand);
		}
		return new String(rands);
//		if (length < 1) {
//			return null;
//		}
//		if (randGen == null) {
//			randGen = new Random();
//			numbersAndLetters = ("3456789abcdefghjkmnpqrstuvwxy").toCharArray();
//		}
//		char[] randBuffer = new char[length];
//		for (int i = 0; i < randBuffer.length; i++) {
//			randBuffer[i] = numbersAndLetters[randGen.nextInt(31)];
//			// randBuffer[i] = numbersAndLetters[randGen.nextInt(35)];
//		}
//		return new String(randBuffer);
	}
	
	public static final String longStr(int length,Long num)
	{
		return String.format("%0"+length+"d", num);
	}

	public static final String fortmatBigDecimal(BigDecimal number)
	{
		return String.valueOf(number.setScale(2,BigDecimal.ROUND_DOWN));
	}
	public static final String fortmatBigDecimalForOne(BigDecimal number)
	{
		return String.valueOf(number.setScale(1,BigDecimal.ROUND_DOWN));
	}

	public static void main(String[] args) {

		MathContext mc = new MathContext(3, RoundingMode.DOWN);
		System.out.println(NumberUtil.fortmatBigDecimalForOne(new BigDecimal(0.20222222).multiply(new BigDecimal(100))));

	}
}