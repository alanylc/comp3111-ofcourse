package test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import ofcourse.Course;
import ofcourse.CourseParse;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CourseParseTest {
	static ArrayList<CourseParse> cpA = new ArrayList<CourseParse>();
	static CourseParse cp = new CourseParse();
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
    	try{
   		 
    		File file = new File("COMP.txt");
 
    		file.delete();
 
    	}catch(Exception e){
 
    	}
		cpA = CourseParse.fullparse();											//This line parse everything into the arraylist of Majors
		Course.AllCourses = new ArrayList<Course>();
		cp=CourseParse.parse("COMP"); // the static variable Course.AllCourses should now have the COMP course list
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCourseParse() {

	}

	@Test
	public void testGetSubject() {
		String expected="COMP";
		assertEquals(expected, cp.getSubject());
	}

	@Test
	public void testSetSubject() {
		String expected="COMP1";
		cp.setSubject(expected);
		assertEquals(expected, cp.getSubject());
	}

	@Test
	public void testGetCourses() {
		ArrayList<Course> ca=cp.getCourses();
		int expected=67;
		assertEquals(expected,ca.size());
		
	}

	@Test
	public void testAdd() {
		//fail("Not yet implemented");
	}

	@Test
	public void testFindByCode() {
		Course c=cp.findByCode("COMP2012 ");
		String expected="COMP2012 ";
		assertEquals(expected,c.toString());
		
	}
	@Test
	public void testFindByCode2() {
		Course c=cp.findByCode("COMP2013");
		assertEquals(null,c);
	}

	@Test						//Heavily tested on other test cases
	public void testParse() {
		//fail("Not yet implemented");
	}

	@Test
	public void testFullparse() {
		//fail("Not yet implemented");
	}

	@Test
	public void testSearchP() {
		CourseParse cp=CourseParse.searchP(cpA, "COMP");
		String expected="COMP";
		assertEquals(expected,cp.getSubject());
	}
	@Test
	public void testSearchP2() {
		CourseParse cp=CourseParse.searchP(cpA, "COMP2");
		assertEquals(null,cp);
	}
	@Test
	public void testSearch() {
		Course c=CourseParse.search(cpA, "COMP2012 ");
		String expected="COMP2012 ";
		assertEquals(expected,c.toString());
	}
	@Test
	public void testSearch2() {
		Course c=CourseParse.search(cpA, "COMP2013");
		assertEquals(null,c);
	}

	@Test
	public void testMain() {
		//fail("Not yet implemented");
	}

}
