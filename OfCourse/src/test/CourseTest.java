package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import ofcourse.Course;
import ofcourse.Course.Session;
import ofcourse.Timetable;
import ofcourse.courseParse;

import org.jsoup.select.Elements;
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
		int expected=3111;
		assertEquals(expected, Course.getCourseByName("COMP3111 ").getCode().getNumber());
	}
	@Test
	public void testGetCourseByName2() {
		assertNull(Course.getCourseByName("COMP6666 "));
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

	@Test
	public void testCourseElement() {
		//fail("Not yet implemented");
	}
*/
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
	public void testGetAttributes2() {
		c=Course.getCourseByName("COMP2011 ");
		assertNull(c.getAttributes());
	}

	@Test
	public void testGetPreRequisite() {
		String expected="COMP 1021 OR COMP 1022P OR COMP 1022Q";
		c=Course.getCourseByName("COMP2011 ");
		assertEquals(expected, c.getPreRequisite());
	}
	@Test
	public void testGetPreRequisite2() {
		c=Course.getCourseByName("COMP1999 ");
		assertNull(c.getPreRequisite());
	}

	@Test
	public void testGetCoRequisite() {
		String expected="(For students without prerequisites) MATH 1013 OR MATH 1014 OR MATH 1018 OR MATH 1020 OR MATH 1023 OR MATH 1024";
		c=Course.getCourseByName("COMP2711 ");
		assertEquals(expected, c.getCoRequisite());
	}
	
	@Test
	public void testGetCoRequisite2() {
		c=Course.getCourseByName("COMP2012 ");
		assertNull(c.getCoRequisite());
	}

	@Test
	public void testGetExclusion() {
		String expected="COMP 2711H, MATH 2343";
		c=Course.getCourseByName("COMP2711 ");
		assertEquals(expected, c.getExclusion());
	}
	
	@Test
	public void testGetExclusion2() {
		c=Course.getCourseByName("COMP3071 ");
		assertNull(c.getExclusion());
	}

	@Test
	public void testGetPreviousCode() {
		String expected="COMP 170";
		c=Course.getCourseByName("COMP2711 ");
		assertEquals(expected, c.getPreviousCode());
	}
	
	@Test
	public void testGetPreviousCode2() {
		c=Course.getCourseByName("COMP2011 ");
		assertNull(c.getPreviousCode());
	}

	@Test
	public void testGetCoList() {
		String expected="RMBI 4310";
		c=Course.getCourseByName("COMP4332 ");
		assertEquals(expected, c.getCoList());
	}
	
	@Test
	public void testGetCoList2() {
		c=Course.getCourseByName("COMP2012 ");
		assertNull(c.getCoList());
	}

	@Test
	public void testGetCode() {
		String expectedDept="COMP";
		int expectedNum=1022;
		char expectedMod='Q';
		c=Course.getCourseByName("COMP1022Q");
		assertEquals(expectedDept, c.getCode().getDept());
		assertEquals(expectedNum, c.getCode().getNumber());
		assertEquals(expectedMod, c.getCode().getMod());
	}
	

	@Test
	public void testIsMatchSession() {
		boolean expected=true;
		c=Course.getCourseByName("COMP2012 ");
		assertEquals(expected, c.isMatchSession());
	}

	public void testIsMatchSession2() {
		boolean expected=false;
		c=Course.getCourseByName("COMP2011 ");
		assertEquals(expected, c.isMatchSession());
	}
	@Test
	public void testGetSessions() {
		c=Course.getCourseByName("COMP3111 ");
		ArrayList<Session> sA=c.getSessions();
		int[] classList={1897,1899,1905,1907,1908,1901,1903,1904};
		for(int i=0;i<sA.size();i++){
			assertEquals(classList[i],sA.get(i).getClassNo());
		}
	}

	@Test
	public void testGetMaxWaitList() {
		int expected=0;
		c=Course.getCourseByName("COMP3111 ");
		assertEquals(expected, c.getMaxWaitList());
	}

	@Test
	public void testSetSessions() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetMatchSession() {
		boolean expected=false;
		c=Course.getCourseByName("COMP2012 ");
		c.setMatchSession(expected);
		assertEquals(expected, c.isMatchSession());
	}
	
	@Test
	public void testSetMatchSession2() {
		boolean expected=true;
		c=Course.getCourseByName("COMP2011 ");
		c.setMatchSession(expected);
		assertEquals(expected, c.isMatchSession());
	}

	@Test
	public void testSetDescription() {
		String expected="This course has 100% guarantee of getting A-grade";
		c=Course.getCourseByName("COMP3111 ");
		c.setDescription(expected);
		assertEquals(expected, c.getDescription());
	}

	@Test
	public void testGetSessionByString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSessionByClassNumber() {
		c=Course.getCourseByName("COMP3111 ");
		int[] classList={1897,1899,1905,1907,1908,1901,1903,1904};
		for(int i=0;i<classList.length;i++){
			Session s=c.getSessionByClassNumber(classList[i]);
			assertEquals(classList[i],s.getClassNo());
		}
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

}
