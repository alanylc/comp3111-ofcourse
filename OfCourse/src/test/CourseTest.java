package test;

import static org.junit.Assert.*;

import ofcourse.Course;
import ofcourse.Timetable;
import ofcourse.courseParse;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CourseTest {
	
	private Course c;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		courseParse.parse("COMP"); // the static variable Course.AllCourses should now have the COMP course list
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		c=Course.getCourseByName("COMP3111 ");
	}

	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void testGetCourseByName() {
		//assertEquals(5, table.getTableId()); seriously, you want me to compare the entire class?
	}
/*
	@Test
	public void testCourseStringIntCharStringBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testCourseCodeStringBoolean() {
		fail("Not yet implemented");
	}
*/
	@Test
	public void testCourseElement() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetDescription() {
		String expected="Methods and tools for planning, designing, implementing, validating, and maintaining large software systems. Project work to build a software system as a team, using appropriate software engineering tools and techniques.";
		c=Course.getCourseByName("COMP3111 ");
		assertEquals(expected, c.getDescription());
	}

	@Test
	public void testGetAttributes() {
		String expected="Common Core (S&T) for 2010 & 2011 3Y programs Common Core (S&T) for 2012 3Y programs Common Core (S&T) for 4Y programs";
		c=Course.getCourseByName("COMP1001 ");
		assertEquals(expected, c.getAttributes());
	}

	@Test
	public void testGetPreRequisite() {
		String expected="COMP 1021 OR COMP 1022P OR COMP 1022Q";
		c=Course.getCourseByName("COMP2011 ");
		assertEquals(expected, c.getPreRequisite());
	}

	@Test
	public void testGetCoRequisite() {
		String expected="(For students without prerequisites) MATH 1013 OR MATH 1014 OR MATH 1018 OR MATH 1020 OR MATH 1023 OR MATH 1024";
		c=Course.getCourseByName("COMP2711 ");
		assertEquals(expected, c.getCoRequisite());
	}

	@Test
	public void testGetExclusion() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPreviousCode() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCoList() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCode() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsMatchSession() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSessions() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMaxWaitList() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetSessions() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetMatchSession() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetDescription() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSessionByString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSessionByClassNumber() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

}
