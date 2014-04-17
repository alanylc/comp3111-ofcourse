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

//	@Test
//	public void testGetEndSlot() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetAllSlots() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetAllSlotsID() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetStartEndID() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testToString() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testEqualsObject() {
//		fail("Not yet implemented");
//	}

}
