package com.solab.iso8583;

import java.math.BigDecimal;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

/** Tests formatting of certain IsoTypes.
 *
 * @author Enrique Zamudio
 */
public class TestFormats {

	@Test
	public void testNumericFormats() {
		assert IsoType.NUMERIC.format(123, 6).equals("000123");
		assert IsoType.NUMERIC.format("hola", 6).equals("00hola");
		assert IsoType.AMOUNT.format(12345, 0).equals("000001234500");
		assert IsoType.AMOUNT.format(new BigDecimal("12345.67"), 0).equals("000001234567");
		assert IsoType.AMOUNT.format("1234.56", 0).equals("000000123456");
	}

	@Test
	public void testStringFormats() {
		assert IsoType.ALPHA.format("hola", 3).equals("hol");
		assert IsoType.ALPHA.format("hola", 4).equals("hola");
		assert IsoType.ALPHA.format("hola", 6).equals("hola  ");
		assert IsoType.LLVAR.format("hola", 0).equals("hola");
		assert IsoType.LLLVAR.format("hola", 0).equals("hola");
        assert IsoType.LLLLVAR.format("HOLA", 0).equals("HOLA");
	}

}
