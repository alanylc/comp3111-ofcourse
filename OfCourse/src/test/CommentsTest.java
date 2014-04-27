package test;

import static org.junit.Assert.assertEquals;

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

public class CommentsTest {

	private Comments cmt;

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
		Ratable obj = Course.getCourseByName("COMP1001 ");
		cmt = obj.new Comments("name", 0.0f, "no comment", "date");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetCommentorName() {
		assertEquals("name", cmt.getCommentorName());
	}

	@Test
	public void testGetRating() {
		assertEquals(new Float(0.0), new Float(cmt.getRating()));
	}

	@Test
	public void testGetComments() {
		assertEquals("no comment", cmt.getComments());
	}

	@Test
	public void testGetDate() {
		assertEquals("date", cmt.getDate());
	}

}
