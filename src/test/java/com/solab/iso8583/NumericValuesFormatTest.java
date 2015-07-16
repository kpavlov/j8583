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
public class NumericValuesFormatTest {

	@Test
	public void testNumericFormats() {
		assert IsoType.NUMERIC.format(123, 6).equals("000123");
		assert IsoType.AMOUNT.format(12345, 0).equals("000001234500");
		assert IsoType.AMOUNT.format(new BigDecimal("12345.67"), 0).equals("000001234567");
	}

}
