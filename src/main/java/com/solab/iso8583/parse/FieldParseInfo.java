/*
j8583 A Java implementation of the ISO8583 protocol
Copyright (C) 2007 Enrique Zamudio Lopez

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

/**
 * This class is used to parse a field from a message buffer. There are concrete subclasses for each IsoType.
 *
 * @author Enrique Zamudio
 */
public abstract class FieldParseInfo {

    private static final int[] POWERS_OF_TEN = {1, 10, 100, 1000, 10000};

    protected IsoType type;
    protected final int length;
    private String encoding = System.getProperty("file.encoding");
    protected boolean forceStringDecoding;
    private CustomField<?> decoder;

    /**
     * Creates a new instance that parses a value of the specified type, with the specified length.
     * The length is only useful for ALPHA and NUMERIC types.
     *
     * @param t   The ISO type to be parsed.
     * @param len The length of the data to be read (useful only for ALPHA and NUMERIC types).
     */
    public FieldParseInfo(IsoType t, int len) {
        if (t == null) {
            throw new IllegalArgumentException("IsoType cannot be null");
        }
        type = t;
        length = len;
    }

    /**
     * Specified whether length headers for variable-length fields in text mode should
     * be decoded using proper string conversion with the character encoding. Default is false,
     * which means use the old behavior of decoding as ASCII.
     */
    public void setForceStringDecoding(boolean flag) {
        forceStringDecoding = flag;
    }

    public void setCharacterEncoding(String value) {
        encoding = value;
    }

    public String getCharacterEncoding() {
        return encoding;
    }

    /**
     * Returns the specified length for the data to be parsed.
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns the data type for the data to be parsed.
     */
    public IsoType getType() {
        return type;
    }

    public void setDecoder(CustomField<?> value) {
        decoder = value;
    }

    public CustomField<?> getDecoder() {
        return decoder;
    }

    /**
     * Parses the character data from the buffer and returns the
     * IsoValue with the correct data type in it.
     *
     * @param field  The field index, useful for error reporting.
     * @param buf    The full ISO message buffer.
     * @param pos    The starting position for the field data.
     * @param custom A CustomField to decode the field.
     */
    public abstract <T> IsoValue<?> parse(final int field, byte[] buf, int pos,
                                          CustomField<T> custom)
            throws ParseException, UnsupportedEncodingException;

    /**
     * Parses binary data from the buffer, creating and returning an IsoValue of the configured
     * type and length.
     *
     * @param field  The field index, useful for error reporting.
     * @param buf    The full ISO message buffer.
     * @param pos    The starting position for the field data.
     * @param custom A CustomField to decode the field.
     */
    public abstract <T> IsoValue<?> parseBinary(final int field, byte[] buf, int pos,
                                                CustomField<T> custom)
            throws ParseException, UnsupportedEncodingException;

    /**
     * Returns a new FieldParseInfo instance that can parse the specified type.
     */
    public static FieldParseInfo getInstance(IsoType type, int len, String encoding) {
        FieldParseInfo fpi = null;
        switch (type) {
            case ALPHA:
                fpi = new AlphaParseInfo(len);
                break;
            case AMOUNT:
                fpi = new AmountParseInfo();
                break;
            case BINARY:
                fpi = new BinaryParseInfo(len);
                break;
            case DATE10:
                fpi = new Date10ParseInfo();
                break;
            case DATE12:
                fpi = new Date12ParseInfo();
                break;
            case DATE4:
                fpi = new Date4ParseInfo();
                break;
            case DATE_EXP:
                fpi = new DateExpParseInfo();
                break;
            case LLBIN:
                fpi = new LlbinParseInfo();
                break;
            case LLLBIN:
                fpi = new LllbinParseInfo();
                break;
            case LLLVAR:
                fpi = new LllvarParseInfo();
                break;
            case LLVAR:
                fpi = new LlvarParseInfo();
                break;
            case NUMERIC:
                fpi = new NumericParseInfo(len);
                break;
            case TIME:
                fpi = new TimeParseInfo();
                break;
            case LLLLVAR:
                fpi = new LlllvarParseInfo();
                break;
            case LLLLBIN:
                fpi = new LlllbinParseInfo();
                break;
            default:
                throw new IllegalArgumentException(String.format("Cannot parse type %s", type));
        }
        fpi.setCharacterEncoding(encoding);
        return fpi;
    }

    protected int decodeLength(byte[] buf, int pos, int digits) throws UnsupportedEncodingException {
        if (forceStringDecoding) {
            return Integer.parseInt(new String(buf, pos, digits, encoding), 10);
        } else {
            int value = 0;
            for (int i = 0; i < digits; i++) {
                value += (buf[pos + i] - 48) * POWERS_OF_TEN[digits - i - 1];
            }
            return value;
        }
    }

}
