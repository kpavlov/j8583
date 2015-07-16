package com.solab.iso8583;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Tests formatting of certain IsoTypes.
 *
 * @author Enrique Zamudio
 * @author Konstantin Pavlov
 */
@RunWith(Parameterized.class)
public class StringValueFormatTest {


    @Parameterized.Parameters(name = "{index}: {0}[2]: \"{1}\" -> {3}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // numeric types
                {IsoType.NUMERIC, "hola", 6, "00hola"},
                {IsoType.AMOUNT, "1234.56", 6, "000000123456"},
                // string types
                {IsoType.ALPHA, "hola", 3, "hol"},
                {IsoType.ALPHA, "hola", 4, "hola"},
                {IsoType.ALPHA, "hola", 6, "hola  "},
                {IsoType.LLVAR, "hola", 0, "hola"},
                {IsoType.LLLVAR, "hola", 0, "hola"},
                {IsoType.LLLLVAR, "HOLA", 0, "HOLA"},
        });
    }

    private final IsoType type;
    private final String value;
    private final int length;
    private final String expectedValue;

    public StringValueFormatTest(IsoType type, String value, int length, String expectedValue) {
        this.type = type;
        this.value = value;
        this.length = length;
        this.expectedValue = expectedValue;
    }

    @Test
    public void testFormat() {
        assertThat(type.format(value, length), equalTo(expectedValue));
    }

}
