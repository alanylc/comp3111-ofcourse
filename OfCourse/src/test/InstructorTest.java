package test;

import static org.junit.Assert.*;
import ofcourse.Instructor;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class InstructorTest {
	
	private Instructor ins = null;
	private double tolerance = 0.000001;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		ins = new Instructor("Sb");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testToString() {
		assertEquals("Sb", ins.toString());
	}

	@Test
	public void testEqualsObject01() {
		assertTrue(ins.equals(ins));
	}
	
	@Test
	public void testEqualsObject02() {
		assertFalse(ins.equals(null));
	}
	
	@Test
	public void testEqualsObject03() {
		assertFalse(ins.equals(new Object()));
	}
	
	@Test
	public void testEqualsObject04() {
		assertFalse(ins.equals(new Instructor("Another")));
	}
	
	@Test
	public void testEqualsObject05() {
		assertTrue(ins.equals(new Instructor("Sb")));
	}

	@Test
	public void testGetAvgRating() {
		assertEquals(0.0, ins.getAvgRating(), tolerance);
	}

	@Test
	public void testSetAvgRating() {
		ins.setAvgRating(new Float(5.5));
		assertEquals(5.5, ins.getAvgRating(), tolerance);
	}

	@Test
	public void testGetName() {
		assertEquals("Sb", ins.getName());
	}

	@Test
	public void testGetComments() {
		assertTrue(ins.getComments().isEmpty());
	}

	// TODO: Class Comments is package visible, not able to test
//	@Test
//	public void testAddComments() {
//		ins.addComments(new Comments());
//	}

}
