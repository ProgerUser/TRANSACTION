/*
	Copyright (c) 1999-2021, Ispirer Systems Ltd. All rights reserved.
	NOTICE OF LICENSE
	This file is Ispirer Reusable Software ("IRS") and a subject to Ispirer Systems Ltd. End User License Agreement ("License"), 
	which can be found in supplementary NOTICE.txt file.
	The License is entered by Ispirer to govern the usage of Ispirer Reusable Software. By using this Ispirer Reusable Software,
	you acknowledge that you have read the License and agree with its terms as well as with the fact that Ispirer Reusable Software 
	is the property of and belong to Ispirer only.
	GRANT OF LICENSE
	If you were provided with IRS under any professional services or license agreement concluded with the Licensor, you are 
	granted a non-exclusive, worldwide, perpetual, irrevocable and fully paid up license to use, modify, adapt, sublicense, 
	and otherwise exploit this IRS as provided below and solely in order to use it as a part of the deliverables and/or its 
	derivatives, transferred to you by the Licensor under professional services or license agreement between you and the Licensor. 
	Redistributions of this IRS must retain the above copyright notice.
	IF YOU ARE NOT IN A LAWFUL POSSESSION OF ANY DELIVERABLES, TRANSFERRED BY THE LICENSOR TO ITS CUSTOMERS UNDER PROFESSIONAL 
	SERVICES OR LICENSE AGREEMENT, OR ITS DERIVATIVES TO USE THIS IRS WITH, PLEASE, STOP USING THIS IRS IMMEDIATELY!
*/

package swift;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Period;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDateTime;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;
import oracle.jdbc.OracleConnection;

public class Plsqlutils {

	/*
	 * Character Functions
	 * 
	 */

	public static String initcap(String str) {
		if (str == null)
			return null;
		String[] exampleSplit = str.split(" ");

		StringBuffer sb = new StringBuffer();
		for (String word : exampleSplit) {
			if (!word.isEmpty())
				sb.append(word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase());
			sb.append(" ");
		}
		return sb.toString().replaceFirst(".$", "");

	}

	public static Integer instr(String str, String searchStr, Integer offset) {
		if (offset != null && !isNullOrEmpty(str) && !isNullOrEmpty(searchStr)) {
			int tmp = offset.intValue();
			if (tmp < 0) {
				if (Math.abs(tmp) > str.length()) {
					return 0;
				}
				String tmpStr = str.substring(0, str.length() + tmp);
				return tmpStr.length()
						- StringUtils.indexOf(StringUtils.reverse(tmpStr), StringUtils.reverse(searchStr))
						- searchStr.length() + 1;
			}
			return StringUtils.indexOf(str, searchStr, tmp) + 1;
		}
		return null;
	}

	public static Integer instr(String str, String searchStr, Integer offset, Integer count) throws SQLException {
		if (offset != null && count != null && !isNullOrEmpty(str) && !isNullOrEmpty(searchStr)) {
			int tmpOffset = offset.intValue();
			int tmpCount = count.intValue();
			if (tmpCount < 0) {
				throw new SQLException(String.format("ORA-01428: argument %1$2s is out of range", tmpCount), "72000",
						1428);
			}
			String tmpStr = str;
			if (Math.abs(tmpOffset) > tmpStr.length()) {
				return 0;
			}
			if (tmpOffset < 0) {
				tmpStr = StringUtils.reverse(str.substring(0, str.length() + tmpOffset));
				int res = StringUtils.ordinalIndexOf(tmpStr, StringUtils.reverse(searchStr), tmpCount);
				if (res < 0) {
					return 0;
				}
				return tmpStr.length() - (res + 1);
			} else if (tmpOffset > 0) {
				tmpStr = str.substring(tmpOffset - 1);
				return StringUtils.ordinalIndexOf(tmpStr, searchStr, tmpCount) + tmpOffset;
			}
			return StringUtils.ordinalIndexOf(tmpStr, searchStr, tmpCount) + 1;
		}
		return null;
	}

	public static String rpad(String str, Integer size) {
		if (size != null) {
			return StringUtils.rightPad(str, size.intValue());
		}
		return null;
	}

	public static String rpad(String str, Integer size, String padStr) {
		if (size != null) {
			return StringUtils.rightPad(str, size.intValue(), padStr);
		}
		return null;
	}

	public static String lpad(String str, Integer size) {
		if (size != null) {
			return StringUtils.leftPad(str, size.intValue());
		}
		return null;
	}

	public static String lpad(String str, Integer size, String padStr) {
		if (size != null) {
			return StringUtils.leftPad(str, size.intValue(), padStr);
		}
		return null;
	}

	public static String substr(String str, BigDecimal start, BigDecimal number) {
		if (str == null || start == null || number == null)
			return null;
		return substr(str, start.setScale(0, RoundingMode.HALF_UP).intValue(),
				number.setScale(0, RoundingMode.HALF_UP).intValue());
	}

	public static String substr(String str, Integer start, Integer number) {
		if (str == null || start == null || number == null)
			return null;
		int tmpNumber = number.intValue();
		int tmpStart = start.intValue();
		if (tmpNumber < 1) {
			return null;
		}
		if (tmpStart == 0) {
			return StringUtils.substring(str, tmpStart, tmpNumber + tmpStart);
		}
		if (tmpStart < 0) {
			if (tmpStart < str.length() * -1) {
				return null;
			}
			return StringUtils.substring(str, tmpStart, str.length() + tmpStart + tmpNumber);
		}
		return StringUtils.substring(str, tmpStart - 1, tmpNumber + tmpStart - 1);
	}

	public static String substr(String str, BigDecimal start) {
		if (str == null || start == null)
			return null;
		return substr(str, start.setScale(0, RoundingMode.HALF_UP).intValue());
	}

	public static String substr(String str, Integer start) {
		if (str == null || start == null) {
			return null;
		}
		int tmpStart = start.intValue();
		if (tmpStart <= 0) {
			if (tmpStart < str.length() * -1) {
				return null;
			}
			return StringUtils.substring(str, tmpStart);
		}
		return StringUtils.substring(str, tmpStart - 1);
	}

	public static Integer ascii(String str) {
		return Integer.valueOf(str.charAt(0));
	}

	/*
	 * Datetime Functions
	 * 
	 */

	public static Timestamp trunc(Timestamp tmstmp, String frmt) {
		if (tmstmp != null && frmt != null && !frmt.isEmpty()) {
			return new Timestamp(trunc(new LocalDateTime(tmstmp.getTime()), frmt).toDateTime().getMillis());
		}
		return null;
	}

	public static Timestamp trunc(Timestamp tmstmp) {
		if (tmstmp != null)
			return new Timestamp(java.sql.Date.valueOf(new java.sql.Date(tmstmp.getTime()).toString()).getTime());
		return null;
	}

	public static Timestamp trunc(java.util.Date dt, String frmt) {
		if (dt != null && frmt != null && !frmt.isEmpty()) {
			return new Timestamp(trunc(new LocalDateTime(dt.getTime()), frmt).toDateTime().getMillis());
		}
		return null;
	}

	public static Timestamp trunc(java.util.Date dt) {
		if (dt != null)
			return new Timestamp(java.sql.Date.valueOf(new java.sql.Date(dt.getTime()).toString()).getTime());
		return null;
	}

	public static Timestamp lastDay(java.util.Date dt) {
		if (dt != null) {
			return lastDay(new Timestamp(dt.getTime()));
		}
		return null;
	}

	public static Timestamp lastDay(Timestamp ts) {
		if (ts != null) {
			return new Timestamp(new LocalDateTime(ts).dayOfMonth().withMaximumValue().toDateTime().getMillis());
		}
		return null;
	}

	public static Timestamp addMonths(Timestamp partime, int num) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(partime.getTime());
		cal.add(Calendar.MONTH, num);
		return new Timestamp(cal.getTimeInMillis());
	}

	public static BigDecimal monthsBetween(Timestamp ts1, Timestamp ts2) {
		if (ts1 != null && ts2 != null) {
			DateTime dt1 = new DateTime(ts1.getTime());
			DateTime dt2 = new DateTime(ts2.getTime());
			int day1 = dt1.getDayOfMonth();
			int month1 = dt1.getMonthOfYear();
			int year1 = dt1.getYear();
			int day2 = dt2.getDayOfMonth();
			int month2 = dt2.getMonthOfYear();
			int year2 = dt2.getYear();
			if (day1 == day2 || day1 == dt1.dayOfMonth().withMaximumValue().getDayOfMonth()
					&& day2 == dt2.dayOfMonth().withMaximumValue().getDayOfMonth()) {
				return new BigDecimal(Months.monthsBetween(dt2, dt1).getMonths());
			}
			return new BigDecimal(String.valueOf((year1 - year2) * 12 + month1 - month2
					+ ((day1 - day2) * 86400000.0 + (dt1.getMillisOfDay() - dt2.getMillisOfDay())) / 2678400000.0));
		}
		return null;
	}

	/*
	 * Conversion Functions
	 * 
	 */

	public static String toString(Integer num) {
		return num == null ? "" : num.toString();
	}

	public static String toChar(java.util.Date dt) throws SQLException {
		if (dt != null)
			return toChar(new Timestamp(dt.getTime()), "DD-MON-RR", Locale.ENGLISH);
		return "";
	}

	public static String toChar(java.util.Date dt, String frmt) throws SQLException {
		if (dt != null) {
			return toChar(new Timestamp(dt.getTime()), frmt, Locale.ENGLISH);
		}
		return null;
	}

	public static String toChar(java.util.Date dt, String frmt, Locale loc) throws SQLException {
		if (dt != null) {
			return toChar(new Timestamp(dt.getTime()), frmt, loc);
		}
		return "";
	}

	public static String toChar(Timestamp ts) throws SQLException {
		return toChar(ts, "DD-MON-RR", Locale.ENGLISH);
	}

	public static String toChar(Timestamp ts, String frmt) throws SQLException {
		return toChar(ts, frmt, Locale.ENGLISH);
	}

	public static String toChar(Timestamp ts, String frmt, Locale locale) throws SQLException {
		if (ts == null || isNullOrEmpty(frmt)) {
			return null;
		}
		GregorianCalendar cal = new GregorianCalendar(locale);
		cal.setTimeInMillis(ts.getTime());
		StringBuilder output = new StringBuilder();
		int i = 0;
		while (i < frmt.length()) {
			if (storeAt(frmt, i, "DL") != null) {
				output.append(new SimpleDateFormat("EEEE, MMMM d, yyyy", locale).format(ts));
				i += 2;
			} else if (storeAt(frmt, i, "DS") != null) {
				output.append(new SimpleDateFormat("MM/dd/yyyy", locale).format(ts));
				i += 2;
			} else if (storeAt(frmt, i, "DDD") != null) {
				output.append(cal.get(Calendar.DAY_OF_YEAR));
				i += 3;
			} else if (storeAt(frmt, i, "DD") != null) {
				output.append(String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)));
				i += 2;
			} else if (storeAt(frmt, i, "DY") != null) {
				String day = new SimpleDateFormat("EEE", locale).format(ts).toUpperCase();
				output.append(day.toUpperCase());
				i += 2;
			} else if (storeAt(frmt, i, "DAY") != null) {
				String day = new SimpleDateFormat("EEEE", locale).format(ts);
				output.append(day.toUpperCase());
				i += 3;
			} else if (storeAt(frmt, i, "D") != null) {
				output.append(cal.get(Calendar.DAY_OF_WEEK));
				i += 1;
			} else if (storeAt(frmt, i, "IW", "WW") != null) {
				output.append(cal.get(Calendar.WEEK_OF_YEAR));
				i += 2;
			} else if (storeAt(frmt, i, "W") != null) {
				int w = (int) (1 + Math.floor(cal.get(Calendar.DAY_OF_MONTH) / 7.0));
				output.append(w);
				i += 1;
			} else if (storeAt(frmt, i, "HH24") != null) {
				output.append(new DecimalFormat("00").format(cal.get(Calendar.HOUR_OF_DAY)));
				i += 4;
			} else if (storeAt(frmt, i, "HH12") != null) {
				output.append(new DecimalFormat("00").format(cal.get(Calendar.HOUR)));
				i += 4;
			} else if (storeAt(frmt, i, "HH") != null) {
				output.append(new DecimalFormat("00").format(cal.get(Calendar.HOUR)));
				i += 2;
			} else if (storeAt(frmt, i, "MI") != null) {
				output.append(new DecimalFormat("00").format(cal.get(Calendar.MINUTE)));
				i += 2;
			} else if (storeAt(frmt, i, "SSSSS") != null) {
				int seconds = cal.get(Calendar.HOUR_OF_DAY) * 60 * 60;
				seconds += cal.get(Calendar.MINUTE) * 60;
				seconds += cal.get(Calendar.SECOND);
				output.append(seconds);
				i += 5;
			} else if (storeAt(frmt, i, "SS") != null) {
				output.append(new DecimalFormat("00").format(cal.get(Calendar.SECOND)));
				i += 2;
			} else if (storeAt(frmt, i, "Y,YYY") != null) {
				output.append(new DecimalFormat("#,###").format(getYear(cal)));
				i += 5;
			} else if (storeAt(frmt, i, "SYYYY") != null) {
				if (cal.get(Calendar.ERA) == GregorianCalendar.BC) {
					output.append('-');
				}
				output.append(new DecimalFormat("0000").format(getYear(cal)));
				i += 5;
			} else if (storeAt(frmt, i, "YYYY", "IYYY", "RRRR") != null) {
				output.append(new DecimalFormat("0000").format(getYear(cal)));
				i += 4;
			} else if (storeAt(frmt, i, "YYY", "IYY") != null) {
				output.append(new DecimalFormat("000").format(getYear(cal) % 1000));
				i += 3;
			} else if (storeAt(frmt, i, "YY", "IY", "RR") != null) {
				output.append(new DecimalFormat("00").format(getYear(cal) % 100));
				i += 2;
			} else if (storeAt(frmt, i, "I", "Y") != null) {
				output.append(getYear(cal) % 10);
				i += 1;
			} else if (storeAt(frmt, i, "MONTH") != null) {
				String month = new SimpleDateFormat("MMMM", locale).format(ts);
				output.append(month.toUpperCase());
				i += 5;
			} else if (storeAt(frmt, i, "MON") != null) {
				String month = new SimpleDateFormat("MMM", locale).format(ts);
				output.append(month.toUpperCase());
				i += 3;
			} else if (storeAt(frmt, i, "MM") != null) {
				output.append(String.format("%02d", cal.get(Calendar.MONTH) + 1));
				i += 2;
			} else if (storeAt(frmt, i, "AM", "PM") != null) {
				output.append(new SimpleDateFormat("a", locale).format(ts));
				i += 2;
			} else if (storeAt(frmt, i, "A.M.", "P.M.") != null) {
				output.append(new SimpleDateFormat("a", locale).format(ts).replaceAll(".", "$0."));
				i += 4;
			} else if (storeAt(frmt, i, "\"") != null) {
				for (i = i + 1; i < frmt.length(); i++) {
					char c = frmt.charAt(i);
					if (c != '"') {
						output.append(c);
					} else {
						i++;
						break;
					}
				}
			} else {
				switch (frmt.charAt(i)) {
				case '-':
				case '/':
				case ',':
				case '.':
				case ';':
				case ':':
				case ' ':
					output.append(frmt.charAt(i));
					i += 1;
					break;
				default:
					throw new SQLException(
							"Please, contact Ispirer Support in order to have the method extended for the format: "
									+ frmt);
				}
			}
		}
		return output.toString();
	}

	private static int getYear(Calendar cal) {
		int year = cal.get(Calendar.YEAR);
		if (cal.get(Calendar.ERA) == GregorianCalendar.BC) {
			year--;
		}
		return year;
	}

	public static Timestamp toDate(Timestamp tmstamp, String strfmt) {
		if (tmstamp == null || strfmt == null) {
			return null;
		}
		return new Timestamp(java.sql.Date.valueOf(new java.sql.Date(tmstamp.getTime()).toString()).getTime());
	}

	public static Timestamp toDate(Integer inttime, String strfmt) throws SQLException {
		if (inttime == null || strfmt == null) {
			return null;
		}
		return toDate(inttime.toString(), strfmt);
	}

	public static Timestamp toDate(String strtime, String formatMask) throws SQLException {
		if (strtime == null || formatMask == null) {
			return null;
		}
		StringBuilder javaFrmt = new StringBuilder();
		boolean flSetMonths = false;
		int i = 0;
		while (i < formatMask.length()) {
			if (storeAt(formatMask, i, "AD", "BC") != null) {
				javaFrmt.append("G");
				i += 2;
			} else if (storeAt(formatMask, i, "A.D.", "B.C.") != null) {
				javaFrmt.append("G");
				i += 4;
			} else if (storeAt(formatMask, i, "AM", "PM") != null) {
				javaFrmt.append("aa");
				i += 2;
			} else if (storeAt(formatMask, i, "A.M.", "P.M.") != null) {
				javaFrmt.append("aa");
				i += 4;
			} else if (storeAt(formatMask, i, "SCC") != null) {
				javaFrmt.append("CC");
				i += 3;
			} else if (storeAt(formatMask, i, "CC") != null) {
				javaFrmt.append("CC");
				i += 2;
			} else if (storeAt(formatMask, i, "DDD") != null) {
				javaFrmt.append("D");
				i += 3;
			} else if (storeAt(formatMask, i, "DD") != null) {
				javaFrmt.append("d");
				i += 2;
			} else if (storeAt(formatMask, i, "DL") != null) {
				javaFrmt.append("EEEE, MMMM d, yyyy");
				i += 2;
			} else if (storeAt(formatMask, i, "DS") != null) {
				javaFrmt.append("MM/dd/yyyy");
				i += 2;
			} else if (storeAt(formatMask, i, "TS") != null) {
				javaFrmt.append("h:mm:ss aa");
				i += 2;
			} else if (storeAt(formatMask, i, "D") != null) {
				javaFrmt.append("u");
				i += 2;
			} else if (storeAt(formatMask, i, "DY") != null) {
				javaFrmt.append("EEE");
				i += 2;
			} else if (storeAt(formatMask, i, "DAY") != null) {
				javaFrmt.append("EEEE");
				i += 3;
			} else if (storeAt(formatMask, i, "IW", "WW") != null) {
				javaFrmt.append("w");
				i += 2;
			} else if (storeAt(formatMask, i, "W") != null) {
				javaFrmt.append("W");
				i += 1;
			} else if (storeAt(formatMask, i, "HH24") != null) {
				javaFrmt.append("H");
				i += 4;
			} else if (storeAt(formatMask, i, "HH12") != null) {
				javaFrmt.append("h");
				i += 4;
			} else if (storeAt(formatMask, i, "HH") != null) {
				javaFrmt.append("h");
				i += 2;
			} else if (storeAt(formatMask, i, "MI") != null) {
				javaFrmt.append("mm");
				i += 2;
			} else if (storeAt(formatMask, i, "SS") != null) {
				javaFrmt.append("s");
				i += 2;
			} else if (storeAt(formatMask, i, "FF9", "FF8", "FF7", "FF6", "FF5", "FF4", "FF3", "FF2", "FF1") != null) {
				javaFrmt.append("S");
				i += 3;
			} else if (storeAt(formatMask, i, "FF") != null) {
				javaFrmt.append("S");
				i += 2;
			} else if (storeAt(formatMask, i, "YYYY", "IYYY", "RRRR") != null) {
				javaFrmt.append("yyyy");
				i += 4;
			} else if (storeAt(formatMask, i, "YYY", "IYY") != null) {
				javaFrmt.append("yyy");
				i += 3;
			} else if (storeAt(formatMask, i, "YY", "IY", "RR") != null) {
				javaFrmt.append("yy");
				i += 2;
			} else if (storeAt(formatMask, i, "I", "Y") != null) {
				javaFrmt.append("y");
				i += 1;
			} else if (storeAt(formatMask, i, "MONTH") != null) {
				flSetMonths = true;
				javaFrmt.append("MMMM");
				i += 5;
			} else if (storeAt(formatMask, i, "MON") != null) {
				flSetMonths = true;
				javaFrmt.append("MMM");
				i += 3;
			} else if (storeAt(formatMask, i, "MM") != null) {
				flSetMonths = true;
				javaFrmt.append("MM");
				i += 2;
			} else if (storeAt(formatMask, i, "\"") != null) {
				for (i = i + 1; i < formatMask.length(); i++) {
					char c = formatMask.charAt(i);
					if (c != '"') {
						javaFrmt.append(c);
					} else {
						i++;
						break;
					}
				}
			} else if (Arrays.asList(new Character[] { '-', '/', ',', '.', ';', ':', ' ' })
					.contains(formatMask.charAt(i))) {
				javaFrmt.append(formatMask.charAt(i));
				i += 1;
			} else {
				throw new SQLException(
						"Please, contact Ispirer Support in order to have the method extended for the format: "
								+ formatMask);
			}
		}
		LocalDateTime date = null;
		try {
			date = DateTimeFormat.forPattern(javaFrmt.toString()).parseLocalDateTime(strtime);
		} catch (IllegalArgumentException e) {
			if (e.getMessage().split("\"")[2].equals(" is too short")) {
				String tmpJavaFrmt = javaFrmt.toString();
				while (date == null) {
					tmpJavaFrmt = tmpJavaFrmt.substring(0, tmpJavaFrmt.length() - 1);
					try {
						date = DateTimeFormat.forPattern(tmpJavaFrmt.toString()).parseLocalDateTime(strtime);
					} catch (IllegalArgumentException exc) {
					}
				}
			}
		}
		if (!flSetMonths) {
			date = date.withMonthOfYear(new LocalDateTime().getMonthOfYear());
		}
		return new Timestamp(date.toDateTime().getMillis());
	}

	public static BigDecimal toNumber(String stringToNumber, String format) {
		int[] gradesFormat = initGrades(format);
		int[] gradesValue = initGrades(stringToNumber);

		boolean isCorrect = true;
		for (int i = 0; i < gradesFormat.length; i++) {
			if (gradesValue[i] > gradesFormat[i]) {
				isCorrect = false;
			}
		}
		if (!isCorrect) {
			throw new NumberFormatException("Argument " + stringToNumber + " not fit in the format");
		}
		return new BigDecimal(stringToNumber);

	}

	private static int[] initGrades(String stringToNumber) {
		int[] result;
		if (stringToNumber.contains(".")) {
			String[] stringValues = stringToNumber.split("\\.");
			result = new int[] { stringValues[0].length(), stringValues[1].length() };
		} else {
			result = new int[] { stringToNumber.length(), 0 };
		}
		return result;
	}

	/*
	 * Numeric Functions
	 * 
	 */

	public static Integer trunc(Integer num) {
		return num;
	}

	public static BigDecimal trunc(BigDecimal num) {
		return num != null ? num.setScale(0, BigDecimal.ROUND_DOWN) : null;
	}

	public static Long trunc(Long num) {
		return num;
	}

	public static Integer trunc(Integer num, Integer decimalPlace) throws SQLException {
		if (num != null && decimalPlace != null) {
			if (decimalPlace < 0)
				return new BigDecimal(num).setScale(decimalPlace, BigDecimal.ROUND_DOWN).intValue();
			return num;
		}
		return null;
	}

	public BigDecimal trunc(BigDecimal num, BigDecimal decimalPlace) throws SQLException {
		if (num != null && decimalPlace != null) {
			BigDecimal tmpDecimalPlace = decimalPlace.setScale(0, BigDecimal.ROUND_HALF_UP);
			if (tmpDecimalPlace.compareTo(new BigDecimal(Integer.MAX_VALUE)) > 0
					|| tmpDecimalPlace.compareTo(new BigDecimal(Integer.MIN_VALUE)) < 0)
				throw new SQLException("ORA-01426: numeric overflow", "22003", -6502);
			return num.setScale(tmpDecimalPlace.intValue(), BigDecimal.ROUND_DOWN);
		}
		return null;
	}

	/*
	 * General Comparison Functions
	 * 
	 */

	public static Timestamp least(Timestamp... values) {
		if (values == null)
			return null;
		for (Timestamp value : values) {
			if (value == null)
				return null;
		}
		Arrays.sort(values);
		return values[0];
	}

	public static String least(String... values) {
		if (values == null)
			return null;
		for (String value : values) {
			if (value == null || value.isEmpty())
				return null;
		}
		Arrays.sort(values);
		return values[0];
	}

	public static Integer least(Integer... values) {
		if (values == null)
			return null;
		for (Integer value : values) {
			if (value == null)
				return null;
		}
		Arrays.sort(values);
		return values[0];
	}

	public static BigDecimal least(BigDecimal... values) {
		if (values == null)
			return null;
		for (BigDecimal value : values) {
			if (value == null)
				return null;
		}
		Arrays.sort(values);
		return values[0];
	}

	public static Long least(Long... values) {
		if (values == null)
			return null;
		for (Long value : values) {
			if (value == null)
				return null;
		}
		Arrays.sort(values);
		return values[0];
	}

	public static Double least(Double... values) {
		if (values == null)
			return null;
		for (Double value : values) {
			if (value == null)
				return null;
		}
		Arrays.sort(values);
		return values[0];
	}

	public static Timestamp greatest(Timestamp... values) {
		if (values == null)
			return null;
		for (Timestamp value : values) {
			if (value == null)
				return null;
		}
		Arrays.sort(values, Collections.reverseOrder());
		return values[0];
	}

	public static String greatest(String... values) {
		if (values == null)
			return null;
		for (String value : values) {
			if (value == null || value.isEmpty())
				return null;
		}
		Arrays.sort(values, Collections.reverseOrder());
		return values[0];
	}

	public static Integer greatest(Integer... values) {
		if (values == null)
			return null;
		for (Integer value : values) {
			if (value == null)
				return null;
		}
		Arrays.sort(values, Collections.reverseOrder());
		return values[0];
	}

	public static BigDecimal greatest(BigDecimal... values) {
		if (values == null)
			return null;
		for (BigDecimal value : values) {
			if (value == null)
				return null;
		}
		Arrays.sort(values, Collections.reverseOrder());
		return values[0];
	}

	public static Long greatest(Long... values) {
		if (values == null)
			return null;
		for (Long value : values) {
			if (value == null)
				return null;
		}
		Arrays.sort(values, Collections.reverseOrder());
		return values[0];
	}

	public static Double greatest(Double... values) {
		if (values == null)
			return null;
		for (Double value : values) {
			if (value == null)
				return null;
		}
		Arrays.sort(values, Collections.reverseOrder());
		return values[0];
	}

	public static Timestamp greatest(Timestamp parTime1, Integer parTime2) {
		return parTime1.before(new Timestamp(parTime2)) ? parTime1 : new Timestamp(parTime2);
	}

	/*
	 * NULL-Related Functions
	 * 
	 */

	public static Object nvl(Object expr1, Object expr2) {
		return expr1 == null ? expr2 : expr1;
	}

	/*
	 * DBMS_LOB Functions
	 * 
	 */

	public static String copy(String str1, String str2, BigDecimal amount) {
		if (str1 == null || str1.isEmpty() || str2 == null || str2.isEmpty())
			return null;
		if (amount == null)
			return null;
		return copy(str1, str2, amount.setScale(0, RoundingMode.HALF_UP).intValue(), 0, 0);
	}

	public static String copy(String str1, String str2, BigDecimal amount, BigDecimal offset1) {
		if (str1 == null || str1.isEmpty() || str2 == null || str2.isEmpty())
			return null;
		if (amount == null || offset1 == null)
			return null;
		return copy(str1, str2, amount.setScale(0, RoundingMode.HALF_UP).intValue(),
				offset1.setScale(0, RoundingMode.HALF_UP).intValue(), 0);
	}

	public static String copy(String str1, String str2, BigDecimal amount, BigDecimal offset1, BigDecimal offset2) {
		if (str1 == null || str1.isEmpty() || str2 == null || str2.isEmpty())
			return null;
		if (amount == null || offset1 == null || offset2 == null)
			return null;
		return copy(str1, str2, amount.setScale(0, RoundingMode.HALF_UP).intValue(),
				offset1.setScale(0, RoundingMode.HALF_UP).intValue(),
				offset2.setScale(0, RoundingMode.HALF_UP).intValue());

	}

	public static String copy(String str1, String str2, Integer amount) {
		if (str1 == null || str1.isEmpty() || str2 == null || str2.isEmpty())
			return null;
		if (amount == null)
			return null;
		return copy(str1, str2, amount.intValue(), 0, 0);
	}

	public static String copy(String str1, String str2, Integer amount, Integer offset1) {
		if (str1 == null || str1.isEmpty() || str2 == null || str2.isEmpty())
			return null;
		if (amount == null || offset1 == null)
			return null;
		return copy(str1, str2, amount.intValue(), offset1.intValue(), 0);
	}

	public static String copy(String str1, String str2, Integer amount, Integer offset1, Integer offset2) {
		if (str1 == null || str1.isEmpty() || str2 == null || str2.isEmpty())
			return null;
		if (amount == null || offset1 == null || offset2 == null)
			return null;
		return copy(str1, str2, amount.intValue(), offset1.intValue(), offset2.intValue());

	}

	public static String copy(String str1, String str2, int amount, int offset1, int offset2) {
		int tmpOffset1 = (offset1 == 0) ? offset1 : offset1 - 1;
		int tmpOffset2 = (offset2 == 0) ? offset2 : offset2 - 1;
		String substr = str2.substring(tmpOffset2, tmpOffset2 + amount);
		String str1Full;
		if (str1.length() < tmpOffset1 + substr.length())
			str1Full = rpad(str1, tmpOffset1 + substr.length());
		else
			str1Full = str1;
		return str1Full.substring(0, offset1) + substr + str1Full.substring(offset1 + substr.length());
	}

	/*
	 * Gauxiliary Functions
	 * 
	 */

	private static LocalDateTime truncQuarter(LocalDateTime dateTime) {
		int month = dateTime.getMonthOfYear();
		if (month <= 3)
			return new LocalDateTime(DateUtils.truncate(dateTime.toDate(), Calendar.YEAR).getTime()).withMonthOfYear(1);
		if (month >= 4 && month <= 6)
			return new LocalDateTime(DateUtils.truncate(dateTime.toDate(), Calendar.YEAR).getTime()).withMonthOfYear(4);
		if (month >= 7 && month <= 9)
			return new LocalDateTime(DateUtils.truncate(dateTime.toDate(), Calendar.YEAR).getTime()).withMonthOfYear(7);
		return new LocalDateTime(DateUtils.truncate(dateTime.toDate(), Calendar.YEAR).getTime()).withMonthOfYear(10);
	}

	private static LocalDateTime truncWeakFirstDayOfYear(LocalDateTime dateTime) {
		LocalDateTime date = new LocalDateTime(DateUtils.truncate(dateTime.toDate(), Calendar.DAY_OF_MONTH));
		LocalDateTime tmpDt = date.withDayOfWeek(date.withMonthOfYear(1).withDayOfMonth(1).getDayOfWeek());
		if (Days.daysBetween(tmpDt, date).getDays() > 0) {
			return tmpDt.plusDays(-7);
		}
		return tmpDt;
	}

	private static LocalDateTime truncWeakFirstDayOfCalendarWeak(LocalDateTime dateTime) {
		LocalDateTime date = new LocalDateTime(DateUtils.truncate(dateTime.toDate(), Calendar.DAY_OF_MONTH));
		LocalDateTime tmpDt = date.withDayOfWeek(date.withWeekOfWeekyear(1).withDayOfWeek(1).getDayOfWeek());
		if (Days.daysBetween(tmpDt, date).getDays() > 0) {
			return tmpDt.plusDays(-7);
		}
		return tmpDt;
	}

	private static LocalDateTime truncWeakFirstDayOfMonth(LocalDateTime dateTime) {
		LocalDateTime date = new LocalDateTime(DateUtils.truncate(dateTime.toDate(), Calendar.DAY_OF_MONTH));
		LocalDateTime tmpDt = date.withDayOfWeek(date.withDayOfMonth(1).getDayOfWeek());
		if (Days.daysBetween(tmpDt, date).getDays() > 0) {
			return tmpDt.plusDays(-7);
		}
		return tmpDt;
	}

	private static LocalDateTime truncWeak(LocalDateTime dateTime, String frmt) {
		if ("WW".equalsIgnoreCase(frmt)) {
			return truncWeakFirstDayOfYear(dateTime);
		}
		if ("IW".equalsIgnoreCase(frmt)) {
			return truncWeakFirstDayOfCalendarWeak(dateTime);
		}
		if ("W".equalsIgnoreCase(frmt)) {
			return truncWeakFirstDayOfMonth(dateTime);
		}
		return dateTime.withMillisOfDay(0);
	}

	private static LocalDateTime trunc(LocalDateTime dt, String frmt) {
		if (storeAt(frmt.toUpperCase(), 0, "SYYYY", "YYYY", "YEAR", "SYEAR", "YYY", "YY", "Y") != null) {
			return new LocalDateTime(DateUtils.truncate(dt.toDate(), Calendar.YEAR).getTime());
		}
		LocalDateTime roundD = new LocalDateTime(DateUtils.truncate(dt.toDate(), Calendar.DAY_OF_MONTH));
		if ("IYYY".equalsIgnoreCase(frmt) || "IY".equalsIgnoreCase(frmt) || "I".equalsIgnoreCase(frmt)) {
			return roundD.withWeekOfWeekyear(1).withDayOfWeek(1);
		}
		if ("Q".equalsIgnoreCase(frmt)) {
			return truncQuarter(dt);
		}
		if ("MONTH".equalsIgnoreCase(frmt) || "MON".equalsIgnoreCase(frmt) || "MM".equalsIgnoreCase(frmt)
				|| "RM".equalsIgnoreCase(frmt)) {
			return new LocalDateTime(DateUtils.truncate(dt.toDate(), Calendar.MONTH).getTime());
		}
		if ("DDD".equalsIgnoreCase(frmt) || "DD".equalsIgnoreCase(frmt) || "J".equalsIgnoreCase(frmt)) {
			return roundD;
		}
		if ("D".equalsIgnoreCase(frmt) || "DY".equalsIgnoreCase(frmt) || "DAY".equalsIgnoreCase(frmt)) {
			return roundD.withDayOfWeek(1);
		}
		if ("HH".equalsIgnoreCase(frmt) || "HH12".equalsIgnoreCase(frmt) || "HH24".equalsIgnoreCase(frmt)) {
			return new LocalDateTime(DateUtils.truncate(dt.toDate(), Calendar.HOUR).getTime());
		}
		if ("MI".equalsIgnoreCase(frmt)) {
			return new LocalDateTime(DateUtils.truncate(dt.toDate(), Calendar.MINUTE).getTime());
		}
		return truncWeak(dt, frmt);
	}

	private static Cptlztn storeAt(String str, int ind, String... substrings) {
		for (String substring : substrings) {
			if (ind + substring.length() <= str.length()) {
				boolean found = true;
				Boolean up1 = null;
				Boolean up2 = null;
				for (int i = 0; i < substring.length(); i++) {
					char c1 = str.charAt(ind + i);
					char c2 = substring.charAt(i);
					if (c1 != c2 && Character.toUpperCase(c1) != Character.toUpperCase(c2)) {
						found = false;
						break;
					} else if (Character.isLetter(c1)) {
						if (up1 == null) {
							up1 = Character.isUpperCase(c1);
						} else if (up2 == null) {
							up2 = Character.isUpperCase(c1);
						}
					}
				}
				if (found) {
					return Cptlztn.toCptlztn(up1, up2);
				}
			}
		}
		return null;
	}

	private enum Cptlztn {
		UPPERCASE, LOWERCASE, CAPITALIZE;

		public static Cptlztn toCptlztn(Boolean v1, Boolean v2) {
			if (v1 == null) {
				return Cptlztn.CAPITALIZE;
			} else if (v2 == null) {
				return v1 ? Cptlztn.UPPERCASE : Cptlztn.LOWERCASE;
			} else if (v1) {
				return v2 ? Cptlztn.UPPERCASE : Cptlztn.CAPITALIZE;
			} else {
				return Cptlztn.LOWERCASE;
			}
		}
	}

	private static boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}

    public static Array toOracleArray(String record, String array, Object[] collect, Connection mconn)
			throws SQLException {
        Struct[] arrayOfRecords = new Struct[collect.length];
        for (int i = 0; i < arrayOfRecords.length; i++) {
            arrayOfRecords[i] = mconn.createStruct(record, (Object[]) collect[i]);
        }
        return ((OracleConnection) mconn).createOracleArray(array, arrayOfRecords);
    }
    
    public static Array toOracleArray(String array, Object[] collect, Connection mconn)
			throws SQLException {
        return ((OracleConnection) mconn).createOracleArray(array, collect);
    }

	/**
	 * Created a method to convert calendar to timestamp
	 * 
	 * @param timestamp
	 * @return
	 */
	public static Calendar toCalendar(Timestamp timestamp) {
		if (timestamp == null) {
			return null;
		}
		final Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(timestamp.getTime());
		return instance;
	}

	private static final class NumberToBigDecimalConverter {

		private static BigDecimal convert(Number input) {
			if (input instanceof Integer) {
				return new BigDecimal((Integer) input);
			} else if (input instanceof Double) {
				return new BigDecimal((Double) input);
			} else if (input instanceof Long) {
				return new BigDecimal((Long) input);
			} else if (input instanceof BigDecimal) {
				return (BigDecimal) input;
			}

			throw new IllegalArgumentException(
					String.format("NumberToBigDecimalConverter does not support %s to BigDecimal.",
							input.getClass().getSimpleName()));
		}
	}

	public static Period numToYMInterval(Number inputValNum, String intervalUnit) {
		if (inputValNum != null && intervalUnit != null && !intervalUnit.isEmpty()) {
			BigDecimal inputVal = NumberToBigDecimalConverter.convert(inputValNum);
			switch (intervalUnit.toUpperCase()) {
			case "YEAR":
				return Period.ofMonths(inputVal.multiply(new BigDecimal(12)).intValue());
			case "MONTH":
				return Period.ofMonths(inputVal.intValue());
			case "SECOND":
			default:
				throw new IllegalArgumentException(String.format("numToYMInterval does not support %s.", intervalUnit));
			}
		}
		return null;

	}

	public static Duration numToDSInterval(Number inputValNum, String intervalUnit) {
		if (inputValNum != null && intervalUnit != null && !intervalUnit.isEmpty()) {
			BigDecimal inputVal = NumberToBigDecimalConverter.convert(inputValNum);
			switch (intervalUnit.toUpperCase()) {
			case "DAY":
				return Duration.ofSeconds(inputVal.multiply(new BigDecimal(24 * 60 * 60)).intValue());
			case "HOUR":
				return Duration.ofSeconds(inputVal.multiply(new BigDecimal(60 * 60)).intValue());
			case "MINUTE":
				return Duration.ofSeconds(inputVal.multiply(new BigDecimal(60)).intValue());
			case "SECOND":
				return Duration.ofSeconds(inputVal.intValue());
			default:
				throw new IllegalArgumentException(String.format("numToDSInterval does not support %s.", intervalUnit));
			}
		}
		return null;
	}

	public static Period toYMInterval(String interval) {
		@SuppressWarnings("unused")
		String sign = "";
		Integer years = 0;
		Integer months = 0;
		String regex = "^(\\+|-)?([^-]+)-([^$]+)";
		Matcher matcher = Pattern.compile(regex).matcher(interval);
		while (matcher.find()) {
			sign = matcher.group(1);
			try {
				years = Integer.valueOf(matcher.group(2));
				months = Integer.valueOf(matcher.group(3));

			} catch (NumberFormatException nfe) {
				throw new IllegalArgumentException(String.format(
						"numToYMInterval does not support %s. Please, contact Ispirer Support in order to have the method extended.",
						interval));
			}
		}
		return Period.of(years, months, 0);
	}

	public static Duration toDSInterval(String interval) {
		@SuppressWarnings("unused")
		String sign = "";
		Integer days = 0;
		Integer hours = 0;
		Integer minutes = 0;
		Integer seconds = 0;
		BigDecimal fSeconds = BigDecimal.ZERO;
		String regex = "^(\\+|-)?([^\\s]+)\\s([^:]+):([^:]+):([^.|$]+).(\\d*)";
		Matcher matcher = Pattern.compile(regex).matcher(interval);
		while (matcher.find()) {
			sign = matcher.group(1);
			try {
				days = Integer.valueOf(matcher.group(2));
				hours = Integer.valueOf(matcher.group(3));
				minutes = Integer.valueOf(matcher.group(4));
				seconds = Integer.valueOf(matcher.group(5));
				fSeconds = new BigDecimal("0." + matcher.group(6));

			} catch (NumberFormatException nfe) {
				throw new IllegalArgumentException(String.format(
						"toDSInterval does not support %s. Please, contact Ispirer Support in order to have the method extended.",
						interval));
			}
		}
		return Duration.ofDays(days).plusHours(hours).plusMinutes(minutes).plusSeconds(seconds)
				.plusNanos(fSeconds.multiply(new BigDecimal(1000000000)).intValue());
	}

	public static Timestamp addDaysToDate(Timestamp date, Number daysToAdd) {
		if (date != null && daysToAdd != null) {
			BigDecimal daystoAddBigDec = NumberToBigDecimalConverter.convert(daysToAdd);
			return new Timestamp(
					new BigDecimal(date.getTime()).add(daystoAddBigDec.multiply(new BigDecimal(86400000))).longValue());
		}
		return null;
	}

	public static Date addDaysToDate(Date date, Number daysToAdd) {
		if (date != null && daysToAdd != null) {
			BigDecimal daystoAddBigDec = NumberToBigDecimalConverter.convert(daysToAdd);
			return new Timestamp(
					new BigDecimal(date.getTime()).add(daystoAddBigDec.multiply(new BigDecimal(86400000))).longValue());
		}
		return null;
	}

	public static String replaceBindVars(String value) {
		return value.replaceAll("(?<=([^\\w\\'\\\"]))(:\\s*\\w+?)(?=([^\\w]))", "?");
	}

}
