package test;

import static org.junit.Assert.*;

import ofcourse.Course;
import ofcourse.SessionType;
import ofcourse.Course.Session;
import ofcourse.courseParse;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SessionTest {

	private Session s;
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
		c=Course.getCourseByName("COMP2012 ");
	}

	@After
	public void tearDown() throws Exception {
	}
/*
	@Test
	public void testSession() {
		fail("Not yet implemented");
	}
*/
	@Test
	public void testGetSet() {//Case for Set exists
		char expected='A';
		s=c.getSessions().get(2);
		assertEquals(expected, s.getSet());
	}
	
	@Test
	public void testGetSet2() {//Case for Set not exists
		char expected=' ';
		s=c.getSessions().get(0);
		assertEquals(expected, s.getSet());
	}


	@Test
	public void testGetEnrol() {//First element
		int expected=117;
		s=c.getSessions().get(0);
		assertEquals(expected, s.getEnrol());
	}

	@Test
	public void testGetEnrol2() {//Last element
		int expected=65;
		s=c.getSessions().get(4);
		assertEquals(expected, s.getEnrol());
	}

	@Test
	public void testGetavailableQuota() {//First element
		int expected=17;
		s=c.getSessions().get(0);
		assertEquals(expected, s.getavailableQuota());
	}
	
	@Test
	public void testGetavailableQuota2() {//Last element
		int expected=2;
		s=c.getSessions().get(4);
		assertEquals(expected, s.getavailableQuota());
	}

	@Test
	public void testGetQuota() {
		int expected=134;
		s=c.getSessions().get(0);
		assertEquals(expected, s.getQuota());
	}

	@Test
	public void testGetwait() {
		int expected=0;
		s=c.getSessions().get(0);
		assertEquals(expected, s.getwait());
	}

	@Test
	public void testGetRemarks() {//no remarks
		String expected="";
		s=c.getSessions().get(0);
		assertEquals(expected, s.getRemarks());
	}
	
	@Test
	public void testGetRemarks2() {//there are remarks
		String expected="Instructor Consent Required";
		Course cc=Course.getCourseByName("COMP2012H");
		s=cc.getSessions().get(0);
		assertEquals(expected, s.getRemarks());
	}

	@Test
	public void testSetQuota() {
		int expected=666;
		s=c.getSessions().get(0);
		s.setQuota(expected);
		assertEquals(expected, s.getQuota());
	}

	@Test
	public void testGetSType() {
		SessionType expected=SessionType.Lecture;
		s=c.getSessions().get(0);
		assertEquals(expected, s.getSType());
	}
	
	@Test
	public void testGetSType2() {
		SessionType expected=SessionType.Laboratory;
		s=c.getSessions().get(3);
		assertEquals(expected, s.getSType());
	}

	@Test
	public void testSetSType() {
		SessionType expected=SessionType.Research;
		s=c.getSessions().get(3);
		s.setSType(expected);
		assertEquals(expected, s.getSType());
	}

	@Test
	public void testGetSNo() {
		int expected=2;
		s=c.getSessions().get(1);
		assertEquals(expected, s.getSNo());
	}

	@Test
	public void testSetSNo() {
		int expected=666;
		s=c.getSessions().get(1);
		s.setSNo(expected);
		assertEquals(expected, s.getSNo());
	}

	@Test
	public void testGetClassNo() {
		int expected=1833;
		s=c.getSessions().get(1);
		assertEquals(expected, s.getClassNo());
	}

	@Test
	public void testSetClassNo() {
		int expected=6666;
		s=c.getSessions().get(1);
		s.setClassNo(expected);
		assertEquals(expected, s.getClassNo());
	}

	@Test
	public void testGetSchedule() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetSchedule() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRoom() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetInstructors() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetInstructors() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

}
