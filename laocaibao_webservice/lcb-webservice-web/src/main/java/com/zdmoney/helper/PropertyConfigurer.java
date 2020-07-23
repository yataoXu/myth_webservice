package com.zdmoney.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PropertyConfigurer extends PropertyPlaceholderConfigurer {
	
	private static final String encryKey = "5ZIcoaQqMj0=";

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {

		String userName = props.getProperty("jdbc.username");
		String password = props.getProperty("jdbc.password");
		if (StringUtils.isNotBlank(userName)) {
			props.setProperty("jdbc.username", decodeBuffer(userName));
		}
		if (StringUtils.isNotBlank(password)) {
			props.setProperty("jdbc.password", decodeBuffer(password));
		}
		super.processProperties(beanFactoryToProcess, props);
	}

	public static String decodeBuffer(String plainText) {
		try {
			return new String(DESCoder.decrypt(plainText, encryKey));
		} catch (Exception e) {
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		String a = DESCoder.encryptBASE64(DESCoder.encrypt("laocaibao".getBytes(), encryKey));
		System.out.println(a);
		System.out.println(new String(DESCoder.decrypt(a,encryKey)));

		Calendar startCal = new GregorianCalendar();
		Calendar endCal  = new GregorianCalendar();
		startCal.setTime(new Date());
		startCal.set(Calendar.YEAR, startCal.get(Calendar.YEAR)-1);
		startCal.set(Calendar.DAY_OF_YEAR,235);
		endCal.setTime(new Date());
		int y1 = startCal.get(Calendar.YEAR);
		int y2 = endCal.get(Calendar.YEAR);
		int days  = (y2-y1)*365 + endCal.get(Calendar.DAY_OF_YEAR) - startCal.get(Calendar.DAY_OF_YEAR);
		System.out.println( Math.abs(days));
	}

}