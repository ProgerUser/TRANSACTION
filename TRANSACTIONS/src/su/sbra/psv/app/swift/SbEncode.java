package su.sbra.psv.app.swift;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.apache.commons.lang3.StringUtils;

public class SbEncode {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SbEncode.class);
	private static final String OEMTABLE = "?жгЄ?­Јий§екдлў Їа????нпзб?ЁвмЎо???Љ?Ќѓ�????љ???ЂЏђЋ???ќџ??Њ??њЃћьрс";
	private static final String ANSITABLE = "йцукенгшщзхъфывапролджэячсмитьбюЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМ�?ТЬБЮ№Ёё";
	private static final String BASE64TABLE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

	public String oem2ansi(String string) throws Exception {
		try {
			return StringUtils.replaceChars(string, OEMTABLE, ANSITABLE);
		} catch (Exception e) {
			LOGGER.error(String.valueOf(e));
			throw e;
		}
	}

	public String ansi2oem(String string) throws Exception {
		try {
			return StringUtils.replaceChars(string, ((ANSITABLE != null && !"".equals(ANSITABLE)) ? ANSITABLE : "") + " ",
					((OEMTABLE != null && !"".equals(OEMTABLE)) ? OEMTABLE : "") + " ");
		} catch (Exception e) {
			LOGGER.error(String.valueOf(e));
			throw e;
		}
	}

	public String map(Integer i) throws Exception {
		try {
			return Plsqlutils.substr(BASE64TABLE, i + 1, 1);
		} catch (Exception e) {
			LOGGER.error(String.valueOf(e));
			throw e;
		}
	}

	public String ascii2base64(String r) throws Exception {
		try {
			Integer i = null;
			Integer x = null;
			Integer y = null;
			String v = null;

			// For every 3 bytes, split them into 4 6-bit units and map them to
			// the Base64 characters
			i = 1;
			while (new BigDecimal(i + 2).compareTo(new BigDecimal((r != null ? r.length() : -1))) <= 0) {
				x = Plsqlutils.ascii(Plsqlutils.substr(r, i, 1)) * 65536
						+ Plsqlutils.ascii(Plsqlutils.substr(r, i + 1, 1)) * 256
						+ Plsqlutils.ascii(Plsqlutils.substr(r, i + 2, 1));
				y = new Integer(
						(int) (Math.floor(new BigDecimal(x).divide(new BigDecimal("262144"), MathContext.DECIMAL64)
								.setScale(0, RoundingMode.HALF_UP).intValue())));
				v = ((v != null && !"".equals(v)) ? v : "") + map(y);
				x = x - y * 262144;
				y = new Integer(
						(int) (Math.floor(new BigDecimal(x).divide(new BigDecimal("4096"), MathContext.DECIMAL64)
								.setScale(0, RoundingMode.HALF_UP).intValue())));
				v = ((v != null && !"".equals(v)) ? v : "") + map(y);
				x = x - y * 4096;
				y = new Integer((int) (Math.floor(new BigDecimal(x).divide(new BigDecimal("64"), MathContext.DECIMAL64)
						.setScale(0, RoundingMode.HALF_UP).intValue())));
				v = ((v != null && !"".equals(v)) ? v : "") + map(y);
				x = x - y * 64;
				v = ((v != null && !"".equals(v)) ? v : "") + map(x);
				i = i + 3;
			}

			// Process the remaining bytes that has fewer than 3 bytes.
			if (new BigDecimal((r != null ? r.length() : null) - i).compareTo(new BigDecimal("0")) == 0) {
				x = new Integer(Plsqlutils.ascii(Plsqlutils.substr(r, i, 1)));
				y = new Integer((int) (Math.floor(new BigDecimal(x).divide(new BigDecimal("4"), MathContext.DECIMAL64)
						.setScale(0, RoundingMode.HALF_UP).intValue())));
				v = ((v != null && !"".equals(v)) ? v : "") + map(y);
				x = x - y * 4;
				x = x * 16;
				v = ((v != null && !"".equals(v)) ? v : "") + map(x);
				v = ((v != null && !"".equals(v)) ? v : "") + "==";
			} else if (new BigDecimal((r != null ? r.length() : null) - i).compareTo(new BigDecimal("1")) == 0) {
				x = Plsqlutils.ascii(Plsqlutils.substr(r, i, 1)) * 256
						+ Plsqlutils.ascii(Plsqlutils.substr(r, i + 1, 1));
				y = new Integer(
						(int) (Math.floor(new BigDecimal(x).divide(new BigDecimal("1024"), MathContext.DECIMAL64)
								.setScale(0, RoundingMode.HALF_UP).intValue())));
				v = ((v != null && !"".equals(v)) ? v : "") + map(y);
				x = x - y * 1024;
				y = new Integer((int) (Math.floor(new BigDecimal(x).divide(new BigDecimal("16"), MathContext.DECIMAL64)
						.setScale(0, RoundingMode.HALF_UP).intValue())));
				v = ((v != null && !"".equals(v)) ? v : "") + map(y);
				x = x - y * 16;
				x = x * 4;
				v = ((v != null && !"".equals(v)) ? v : "") + map(x);
				v = ((v != null && !"".equals(v)) ? v : "") + "=";
			}
			return v;
		} catch (Exception e) {
			LOGGER.error(String.valueOf(e));
			throw e;
		}
	}
}