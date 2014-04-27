package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;

import ofcourse.Course;
import ofcourse.CourseParseThreaded;
import ofcourse.Ratable;
import ofcourse.Ratable.Comments;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RatableTest {
	
	private Ratable obj;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		Course.AllCourses = new ArrayList<Course>();
		CourseParseThreaded.parse("COMP"); 
		obj = Course.getCourseByName("COMP1001 ");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetAvgRating() {
		assertFalse(new Float(obj.getAvgRating()).equals(Float.NaN));
	}

	@Test
	public void testSetAvgRating() {
		obj.setAvgRating(0.0f);
		assertEquals(new Float(0.0), new Float(obj.getAvgRating()));
	}

	@Test
	public void testGetName() {
		assertEquals("COMP 1001 - Exploring Multimedia and Internet Computing (3 units)", obj.getName());
	}

	@Test
	public void testGetComments() {
		assertNotNull(obj.getComments());
	}

	@Test
	public void testParseComments() {
		ArrayList<Comments> oldcmt = obj.getComments();
		obj.parseComments();
		ArrayList<Comments> newcmt = obj.getComments();
		assertNotSame(oldcmt, newcmt);
	}

	@Test
	public void testAddComments() {
		ArrayList<Comments> oldcmt = obj.getComments();
		Comments cmt = obj.new Comments("name", 0.0f, "no comment", "date");
		obj.addComments(cmt);
		ArrayList<Comments> newcmt = obj.getComments();
		oldcmt.add(cmt);
		assertEquals(oldcmt, newcmt);
	}

}
