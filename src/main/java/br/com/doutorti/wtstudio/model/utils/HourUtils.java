package br.com.doutorti.wtstudio.model.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HourUtils {

	public static final Integer SINCE_HOUR = 9;
	public static final Integer UNTIL_HOUR = 21;
	public static final Integer INTERVAL_MIN = 30;

	public static List<String> completeListHours() {
		List<String> completeList = new ArrayList<String>();
		for (int h = SINCE_HOUR; h <= UNTIL_HOUR; h++) {
			for (int m = 0; m < 60; m += INTERVAL_MIN) {
				completeList.add((h > 9 ? h : "0" + h) + ":"
						+ (m > 9 ? m : "0" + m));
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

}
