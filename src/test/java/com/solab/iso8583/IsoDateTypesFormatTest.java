package com.solab.iso8583;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class IsoDateTypesFormatTest {

    private static final Date DATE = new Date(96867296000L);

    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
    private static final TimeZone GMT_PLUS_ONE = TimeZone.getTimeZone("GMT+0100");
    private static final TimeZone GMT_MINUS_SIX = TimeZone.getTimeZone("GMT-0600");

    private static TimeZone systemTimezone;

    @Parameterized.Parameters(name = "{index}: {0}: \"{1}\" @{2} -> {3}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {IsoType.DATE12, DATE, null, "730125213456"},
                {IsoType.DATE10, DATE, null, "0125213456"},
                {IsoType.DATE4, DATE, null, "0125"},
                {IsoType.DATE_EXP, DATE, null, "7301"},
                {IsoType.TIME, DATE, null, "213456"},
                // GMT
                {IsoType.DATE12, DATE, GMT, "730126033456"},
                {IsoType.DATE10, DATE, GMT, "0126033456"},
                {IsoType.DATE4, DATE, GMT, "0126"},
                {IsoType.DATE_EXP, DATE, GMT, "7301"},
                {IsoType.TIME, DATE, GMT, "033456"},
                // GMT+1
                {IsoType.DATE12, DATE, GMT_PLUS_ONE, "730126043456"},
                {IsoType.DATE10, DATE, GMT_PLUS_ONE, "0126043456"},
                {IsoType.DATE4, DATE, GMT_PLUS_ONE, "0126"},
                {IsoType.DATE_EXP, DATE, GMT_PLUS_ONE, "7301"},
                {IsoType.TIME, DATE, GMT_PLUS_ONE, "043456"},
        });
    }

    private final IsoType type;
    private final String formettedValue;
    private final Date date;
    private final TimeZone timeZone;

    public IsoDateTypesFormatTest(IsoType type, Date date, TimeZone timeZone, String formettedValue) {
        this.type = type;
        this.formettedValue = formettedValue;
        this.date = date;
        this.timeZone = timeZone;
    }

    @BeforeClass
    public static void beforeClass() {
        systemTimezone = TimeZone.getDefault();
        TimeZone.setDefault(GMT_MINUS_SIX);
    }

    @AfterClass
    public static void afterClass() {
        TimeZone.setDefault(systemTimezone);
    }

    @Test
    public void testFormat() {
        Assert.assertThat(date.toString(), type.format(date, timeZone), equalTo(formettedValue));
    }
}
