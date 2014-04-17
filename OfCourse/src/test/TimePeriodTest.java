package test;

import static org.junit.Assert.*;
import ofcourse.TimePeriod;
import ofcourse.TimeSlot;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TimePeriodTest {
	
	private TimePeriod tp = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		tp = new TimePeriod("We", "15:30 - 18:20");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetStartSlot() {
		TimeSlot ts = tp.getStartSlot();
		TimeSlot expected = TimeSlot.getTimeSlotByStrings("We", "15:30");
		assertEquals(expected, ts);
	}

	@Test
	public void testGetEndSlot() {
		TimeSlot ts = tp.getEndSlot();
		TimeSlot expected = TimeSlot.getTimeSlotByStrings("We", "18:20");
		assertEquals(expected, ts);
	}

	@Test
	public void testGetAllSlots() {
		TimeSlot[] ts = tp.getAllSlots();
		TimeSlot[] expected = new TimeSlot[]{TimeSlot.getTimeSlotByStrings("We", "15:30"),
				TimeSlot.getTimeSlotByStrings("We", "16:00"), TimeSlot.getTimeSlotByStrings("We", "16:30"), 
				TimeSlot.getTimeSlotByStrings("We", "17:00"), TimeSlot.getTimeSlotByStrings("We", "17:30"),
				TimeSlot.getTimeSlotByStrings("We", "18:00")};
		assertArrayEquals(expected, ts);
	}
	

	@Test
	public void testGetAllSlotsID() {
		int[] sid = tp.getAllSlotsID();
		int[] expected = new int[] {313,314,315,316,317,318};
		assertArrayEquals(expected, sid);
	}

	@Test
	public void testGetStartEndID() {
		int[] seid = tp.getStartEndID();
		int[] expected = new int[] {313,318};
		assertArrayEquals(expected, seid);
	}

	@Test
	public void testToString() {
		assertEquals("Wed 15:30-18:30", tp.toString());
	}

	@Test
	public void testEqualsObject01() {
		TimePeriod newtp = new TimePeriod("We", "15:30 - 18:20");
		assertTrue(newtp.equals(tp));
	}
	
	@Test
	public void testEqualsObject02() {
		assertFalse(tp.equals(null));
	}

	@Test
	public void testEqualsObject03() {
		TimePeriod newtp = new TimePeriod("We", "15:30 - 19:20");
		assertFalse(newtp.equals(tp));
	}
	
	@Test
	public void testEqualsObject04() {
		TimePeriod newtp = new TimePeriod("We", "16:30 - 18:20");
		assertFalse(newtp.equals(tp));
	}
	
	@Test
	public void testEqualsObject05() {
		TimePeriod newtp = new TimePeriod("Fr", "15:30 - 18:20");
		assertFalse(newtp.equals(tp));
	}
	
	@Test
	public void testEqualsObject06() {
		assertTrue(tp.equals(tp));
	}
	
	@Test
	public void testEqualsObject07() {
		assertFalse(tp.equals(new Object()));
	}
}
