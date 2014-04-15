package test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import ofcourse.Course;
import ofcourse.CourseParse;
import ofcourse.CourseParseThreaded;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CourseParseThreadedTest {
	static ArrayList<CourseParse> cpA = new ArrayList<CourseParse>();
	static CourseParse cp = new CourseParse();
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
    	try{
      		 
    		File file = new File("COMP.txt");
 
    		file.delete();
 
    	}catch(Exception e){
 
    	}
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
	public void testParse() {
		Course.AllCourses = new ArrayList<Course>();
		cp=CourseParseThreaded.parse("COMP"); // the static variable Course.AllCourses should now have the COMP course list
		String expected="COMP";
		assertEquals(expected,cp.getSubject());
	}

	@Test
	public void testFullparse() {
		cpA = CourseParseThreaded.fullparse();											//This line parse everything into the arraylist of Majors
		String expected="COMP2012 ";
		assertEquals(expected,CourseParse.search(cpA, "COMP2012 ").toString());		
	}

}
