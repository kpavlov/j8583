/*
j8583 A Java implementation of the ISO8583 protocol
Copyright (C) 2011 Enrique Zamudio Lopez

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
*/
package com.solab.iso8583.parse;

import com.solab.iso8583.CustomField;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * This class is used to parse fields of type {@link IsoType#DATE12}.
 *
 * @author Konstantin Pavlov
 * @since 1.10.3
 */
public class Date12ParseInfo extends DateTimeParseInfo {

    public Date12ParseInfo() {
        super(IsoType.DATE12, 12);
    }

    @Override
    public <T> IsoValue<Date> parse(final int field, final byte[] buf,
                                    final int pos, final CustomField<T> custom)
            throws ParseException, UnsupportedEncodingException {
        if (pos < 0) {
            throw new ParseException(String.format("Invalid DATE12 field %d position %d",
                    field, pos), pos);
        }
        if (pos + 12 > buf.length) {
            throw new ParseException(String.format("Insufficient data for DATE12 field %d, pos %d",
                    field, pos), pos);
        }
        //A SimpleDateFormat in the case of dates won't help because of the missing data
        //we have to use the current date for reference and change what comes in the buffer
        Calendar cal = Calendar.getInstance();
        //Set the month in the date
        int year = parseTwoDigits(buf, pos);
        if (year < 50) {
            year = 2000 + year;
        } else {
            year = 1900 + year;
        }
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, parseTwoDigits(buf, pos + 2) - 1);
        cal.set(Calendar.DATE, parseTwoDigits(buf, pos + 4));
        cal.set(Calendar.HOUR_OF_DAY, parseTwoDigits(buf, pos + 6));
        cal.set(Calendar.MINUTE, parseTwoDigits(buf, pos + 8));
        cal.set(Calendar.SECOND, parseTwoDigits(buf, pos + 10));
        cal.set(Calendar.MILLISECOND, 0);
        if (tz != null) {
            cal.setTimeZone(tz);
        }
        adjustWithFutureTolerance(cal);
        return new IsoValue<>(type, cal.getTime(), null);
    }

    @Override
    public <T> IsoValue<Date> parseBinary(final int field, final byte[] buf,
                                          final int pos, final CustomField<T> custom)
            throws ParseException {
        if (pos < 0) {
            throw new ParseException(String.format("Invalid DATE12 field %d position %d",
                    field, pos), pos);
        }
        if (pos + 6 > buf.length) {
            throw new ParseException(String.format("Insufficient data for DATE12 field %d, pos %d",
                    field, pos), pos);
        }
        int[] tens = new int[6];
        int start = 0;
        for (int i = pos; i < pos + tens.length; i++) {
            tens[start++] = (((buf[i] & 0xf0) >> 4) * 10) + (buf[i] & 0x0f);
        }
        Calendar cal = Calendar.getInstance();
        //A SimpleDateFormat in the case of dates won't help because of the missing data
        //we have to use the current date for reference and change what comes in the buffer
        //Set the month in the date
        int year = tens[0];
        if (year < 50) {
            year = 2000 + year;
        } else {
            year = 1900 + year;
        }
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, tens[1] - 1);
        cal.set(Calendar.DATE, tens[2]);
        cal.set(Calendar.HOUR_OF_DAY, tens[3]);
        cal.set(Calendar.MINUTE, tens[4]);
        cal.set(Calendar.SECOND, tens[5]);
        cal.set(Calendar.MILLISECOND, 0);
        if (tz != null) {
            cal.setTimeZone(tz);
        }
        adjustWithFutureTolerance(cal);
        return new IsoValue<>(type, cal.getTime(), null);
    }

}
