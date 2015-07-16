package com.solab.iso8583.parse;

import com.solab.iso8583.IsoType;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Abstract class for date/time parsers.
 *
 * @author Enrique Zamudio
 *         Date: 18/12/13 18:21
 */
public abstract class DateTimeParseInfo extends FieldParseInfo {

    protected static final long FUTURE_TOLERANCE;
    protected TimeZone tz;

   	static {
   		FUTURE_TOLERANCE = Long.parseLong(System.getProperty("j8583.future.tolerance", "900000"));
   	}

    protected DateTimeParseInfo(IsoType type, int length) {
        super(type, length);
    }

    public void setTimeZone(TimeZone value) {
        tz = value;
    }
    public TimeZone getTimeZone() {
        return tz;
    }

    public static void adjustWithFutureTolerance(Calendar cal) {
   		//We need to handle a small tolerance into the future (a couple of minutes)
   		long now = System.currentTimeMillis();
   		long then = cal.getTimeInMillis();
   		if (then > now && then-now > FUTURE_TOLERANCE) {
   			cal.add(Calendar.YEAR, -1);
   		}
   	}

    protected int parseTwoDigits(byte[] buf, int offset) throws UnsupportedEncodingException {
        if (forceStringDecoding) {
            return Integer.parseInt(new String(buf, offset, 2, getCharacterEncoding()), 10);
        } else {
            return ((buf[offset] - 48) * 10) + buf[offset + 1] - 48;
        }
    }
}
