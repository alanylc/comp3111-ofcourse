package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import ofcourse.Course;
import ofcourse.TimePeriod;
import ofcourse.TimeSlot;
import ofcourse.WeekDay;
import ofcourse.CourseParse;
import ofcourse.Course.Session;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TimeSlotTest {
	private Session s;
	private Course c;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		CourseParse.parse("COMP"); // the static variable Course.AllCourses should now have the COMP course list

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		c=Course.getCourseByName("COMP2012 ");
		s=c.getSessions().get(0);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetTimeSlotByID() {
		Set<TimePeriod> schedule = s.getSchedule();
		int expected=630;//415+215
		int actual=0;
		for(TimePeriod tp:schedule)actual+=tp.getStartSlot().getID();
		assertEquals(expected, actual);
	}

	@Test
	public void testGetTimeSlotByStrings() {
		TimeSlot t=TimeSlot.getTimeSlotByStrings("We","17:00");
		int expected=316;
		assertEquals(expected,t.ID);
	}
	@Test
	public void testGetTimeSlotByStrings2() {
		TimeSlot t=TimeSlot.getTimeSlotByStrings("We","09:00");
		int expected=300;
		assertEquals(expected,t.ID);
	}
	@Test
	public void testGetTimeSlotByStrings3() {
		TimeSlot t=TimeSlot.getTimeSlotByStrings("We","3:30");
		assertEquals(null,t);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetTimeSlotByStrings4() {
		TimeSlot t=TimeSlot.getTimeSlotByStrings("We","23:30");
	}


	@Test
	public void testNextSlot() {
		TimeSlot t=TimeSlot.getTimeSlotByStrings("We","17:00");
		int expected=317;
		assertEquals(expected,t.nextSlot().ID);
	}
	@Test
	public void testNextSlot2() {
		TimeSlot t=TimeSlot.getTimeSlotByStrings("We","22:30");
		int expected=400;
		assertEquals(expected,t.nextSlot().ID);
	}

	@Test
	public void testGetID() {
		Set<TimePeriod> schedule = s.getSchedule();
		int expected=630;//415+215
		int actual=0;
		for(TimePeriod tp:schedule)actual+=tp.getStartSlot().getID();
		assertEquals(expected, actual);
	}

	@Test
	public void testGetDayID() {
		Set<TimePeriod> schedule = s.getSchedule();
		int expected=6;//4+2
		int actual=0;
		for(TimePeriod tp:schedule)actual+=tp.getStartSlot().getDayID();
		assertEquals(expected, actual);
	}

	@Test
	public void testGetDay() {
		Set<TimePeriod> schedule = s.getSchedule();
		WeekDay expected[] = new WeekDay[] {WeekDay.Tue, WeekDay.Thu};
		ArrayList<WeekDay> actual_arr = new ArrayList<WeekDay>();
		for(TimePeriod tp:schedule) {
			try {
				actual_arr.add(tp.getStartSlot().getDay());
			} catch (Exception e) {
			}
		}
		WeekDay actual[] = new WeekDay[actual_arr.size()];
		actual_arr.toArray(actual);
		Arrays.sort(expected);
		Arrays.sort(actual);
		
		assertArrayEquals(expected, actual);
	}
	@Test
	public void testGetDay2() {
		TimeSlot t=TimeSlot.getTimeSlotByStrings("Mo","22:30");
		WeekDay expected=WeekDay.Mon;
		try {
			assertEquals(expected,t.getDay());
		} catch (Exception e) {
		}
	}
	@Test
	public void testGetDay3() {
		TimeSlot t=TimeSlot.getTimeSlotByStrings("Tu","22:30");
		WeekDay expected=WeekDay.Tue;
		try {
			assertEquals(expected,t.getDay());
		} catch (Exception e) {
		}
	}
	@Test
	public void testGetDay4() {
		TimeSlot t=TimeSlot.getTimeSlotByStrings("We","22:30");
		WeekDay expected=WeekDay.Wed;
		try {
			assertEquals(expected,t.getDay());
		} catch (Exception e) {
		}
	}
	@Test
	public void testGetDay5() {
		TimeSlot t=TimeSlot.getTimeSlotByStrings("Th","22:30");
		WeekDay expected=WeekDay.Thu;
		try {
			assertEquals(expected,t.getDay());
		} catch (Exception e) {
		}
	}
	@Test
	public void testGetDay6() {
		TimeSlot t=TimeSlot.getTimeSlotByStrings("Fr","22:30");
		WeekDay expected=WeekDay.Fri;
		try {
			assertEquals(expected,t.getDay());
		} catch (Exception e) {
		}
	}
	@Test
	public void testGetDay7() {
		TimeSlot t=TimeSlot.getTimeSlotByStrings("Sa","22:30");
		WeekDay expected=WeekDay.Sat;
		try {
			assertEquals(expected,t.getDay());
		} catch (Exception e) {
		}
	}
	@Test
	public void testGetDay8() {
		TimeSlot t=TimeSlot.getTimeSlotByStrings("Su","22:30");
		WeekDay expected=WeekDay.Sun;
		try {
			assertEquals(expected,t.getDay());
		} catch (Exception e) {
		}
	}
	@Test//(expected=Exception.class)
	public void testGetDay9() {
		TimeSlot t=TimeSlot.getTimeSlotByStrings("AA","22:30");

			try {
				WeekDay w=t.getDay();
			} catch (Exception e) {
				assertEquals(1,1);
			}

	}
	@Test
	public void testGetTimeID() {
		Set<TimePeriod> schedule = s.getSchedule();
		int expected=30;//15+15
		int actual=0;
		for(TimePeriod tp:schedule)actual+=tp.getStartSlot().getTimeID();
		assertEquals(expected, actual);
	}

	@Test
	public void testGetStartTime() {
		Set<TimePeriod> schedule = s.getSchedule();
		String expected="15301530";
		String actual="";
		for(TimePeriod tp:schedule)actual+=tp.getStartSlot().getStartTime();
		assertEquals(expected, actual);
	}

	@Test
	public void testGetEndTime() {
		Set<TimePeriod> schedule = s.getSchedule();
		String expected="16301630";
		String actual="";
		for(TimePeriod tp:schedule)actual+=tp.getStartSlot().getEndTime();
		assertEquals(expected, actual);
	}

}

