package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;

import ofcourse.Course;
import ofcourse.Course.Code;
import ofcourse.CourseParseThreaded;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CodeTest {

	private Code c;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// this test will not change contents of course
		Course.AllCourses = new ArrayList<Course>();
		CourseParseThreaded.parse("COMP"); 
		// the static variable Course.AllCourses should now have the COMP course list
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		c=Course.getCourseByName("COMP3111 ").getCode();
	}

	@After
	public void tearDown() throws Exception {
	}
	/*
	@Test
	public void testCode() {
		fail("Not yet implemented");
	}
*/
	@Test
	public void testGetDept() {
		String expected="COMP";
		assertEquals(expected, c.getDept());
	}
	
	

	@Test
	public void testGetNumber() {
		int expected=3111;
		assertEquals(expected, c.getNumber());
	}

	@Test
	public void testGetMod() {
		char expected=' ';
		assertEquals(expected, c.getMod());
	}
	
	@Test
	public void testGetMod2() {
		char expected='Q';
		Code cc=Course.getCourseByName("COMP1022Q").getCode();
		assertEquals(expected, cc.getMod());
	}

	@Test
	public void testToString() {
		String expected="COMP3111 ";
		assertEquals(expected, c.toString());
	}
	
	@Test
	public void testToString2() {
		String expected="COMP1022Q";
		Code cc=Course.getCourseByName("COMP1022Q").getCode();
		assertEquals(expected, cc.toString());
	}

	@Test
	public void testClone() {
		Object cc;
		cc=c.clone();
		assertNotSame(c, cc);

	}

}
