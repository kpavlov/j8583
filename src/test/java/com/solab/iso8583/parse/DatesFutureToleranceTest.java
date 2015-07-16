package com.solab.iso8583.parse;

import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;
import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test that the dates are formatted and parsed correctly.
 *
 * @author Enrique Zamudio
 */
public class DatesFutureToleranceTest {

    private static final TimeZone GMT_MINUS_SIX = TimeZone.getTimeZone("GMT-0600");

    private static TimeZone systemTimezone;
    private Date soon;
    private Date now;

    @BeforeClass
    public static void beforeClass() {
        systemTimezone = TimeZone.getDefault();
        TimeZone.setDefault(GMT_MINUS_SIX);
    }

    @AfterClass
    public static void afterClass() {
        TimeZone.setDefault(systemTimezone);
    }

    @Before
    public void beforeMethod() {
        now = new Date();
    }

    @Test
    public void testDate4FutureTolerance() throws ParseException, IOException {
        GregorianCalendar today = new GregorianCalendar();
        Date soon = new Date(today.getTime().getTime() + 50000);
        today.set(GregorianCalendar.HOUR, 0);
        today.set(GregorianCalendar.MINUTE, 0);
        today.set(GregorianCalendar.SECOND, 0);
        today.set(GregorianCalendar.MILLISECOND, 0);
        byte[] buf = IsoType.DATE4.format(soon, null).getBytes();
        IsoValue<Date> comp = new Date4ParseInfo().parse(0, buf, 0, null);
        Assert.assertEquals(comp.getValue(), today.getTime());
        //Now with the binary
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        comp.write(bout, true, false);
        IsoValue<Date> bin = new Date4ParseInfo().parseBinary(0, bout.toByteArray(), 0, null);
        Assert.assertEquals(comp.getValue().getTime(), bin.getValue().getTime());
    }

    @Test
    public void testDate10FutureTolerance() throws ParseException, IOException {
        GregorianCalendar today = new GregorianCalendar();
        today.set(GregorianCalendar.MILLISECOND, 0);
        Date soon = new Date(today.getTime().getTime() + 50000);
        byte[] buf = IsoType.DATE10.format(soon, null).getBytes();
        IsoValue<Date> comp = new Date10ParseInfo().parse(0, buf, 0, null);
        assertTrue("parsed date is in the future", comp.getValue().after(now));
        assertEquals("parsed date equals original", soon, comp.getValue());
        //Now with the binary
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        comp.write(bout, true, false);
        IsoValue<Date> bin = new Date10ParseInfo().parseBinary(0, bout.toByteArray(), 0, null);
        Assert.assertEquals(comp.getValue().getTime(), bin.getValue().getTime());
    }

    @Test
    public void testDate12FutureTolerance() throws ParseException, IOException {

        GregorianCalendar today = new GregorianCalendar();
        today.set(GregorianCalendar.MILLISECOND, 0);
        Date soon = new Date(today.getTime().getTime() + 50000);
        byte[] buf = IsoType.DATE12.format(soon, null).getBytes();
        IsoValue<Date> comp = new Date12ParseInfo().parse(0, buf, 0, null);
        assertTrue("parsed date is in the future", comp.getValue().after(now));
        assertEquals("parsed date equals original", soon, comp.getValue());
        //Now with the binary
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        comp.write(bout, true, false);
        IsoValue<Date> bin = new Date12ParseInfo().parseBinary(0, bout.toByteArray(), 0, null);
        Assert.assertEquals(comp.getValue().getTime(), bin.getValue().getTime());
    }

}
