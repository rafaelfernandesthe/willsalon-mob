package br.com.doutorti.willsalon.model.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateHourUtils {

	public static final Integer SINCE_HOUR = 9;
	public static final Integer UNTIL_HOUR = 21;
	public static final Integer INTERVAL_MIN = 30;
	public static SimpleDateFormat sdfHHmm = new SimpleDateFormat("HH:mm");
	public static SimpleDateFormat ddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");

	public static List<String> completeListHours() {
		List<String> completeList = new ArrayList<String>();
		for (int h = SINCE_HOUR; h <= UNTIL_HOUR; h++) {
			for (int m = 0; m < 60; m += INTERVAL_MIN) {
				completeList.add((h > 9 ? h : "0" + h) + ":" + (m > 9 ? m : "0" + m));
				if (h == UNTIL_HOUR)
					break;
			}
		}
		return completeList;
	}

	public static String getCorrectHourOrMinute(int hourOrMinute) {
		String result = "";

		result = hourOrMinute > 9 ? hourOrMinute + "" : "0" + hourOrMinute;

		return result;
	}

	public static Date zeroMilli(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static String sumMinutes(String hourMinute, int minutes) {
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdfHHmm.parse(hourMinute));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.add(Calendar.MINUTE, minutes);
		return getCorrectHourOrMinute(c.get(Calendar.HOUR_OF_DAY)) + ":"
				+ getCorrectHourOrMinute(c.get(Calendar.MINUTE));
	}

	public static Date getStartOfDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static Date getEndOfDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

}
