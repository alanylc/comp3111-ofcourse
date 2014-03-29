package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import ofcourse.Course;
import ofcourse.TimeSlot;
import ofcourse.Timetable;
import ofcourse.courseParse;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TimetableTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		table = new Timetable(5); // TID = 5
		courseParse.parse("COMP"); // the static variable Course.AllCourses should now have the COMP course list
		table.addCourse("COMP2611 ", new String[]{"L1", "T1", "LA1"}); // the table now has olny 1 course
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetTableId() {
		assertEquals(5, table.getTableId());
	}

	@Test
	public void testSetTableId() {
		table.setTableId(2);
		assertEquals(2, table.getTableId());
	}

	@Test
	public void testGetEnrolled() { 
		HashMap<Course, ArrayList<Course.Session>> enrolled = table.getEnrolled();
		if (!enrolled.values().iterator().hasNext()) fail("The enrolled list should have at least 1 course");
		int expected[] = {1, 1, 3}; // 1 course, 1 array list of sessions, 3 sessions 
		int actuals[] = new int[3];
		actuals[0] = enrolled.keySet().size();
		actuals[1] = enrolled.values().size();
		actuals[2] = enrolled.values().iterator().next().size();
		assertArrayEquals(expected, actuals);
	}

	@Test
	public void testSetEnrolled() {
		HashMap<Course, ArrayList<Course.Session>> enrolled = table.getEnrolled();
		Course comp = Course.getCourseByName("COMP2011 ");
		ArrayList<Course.Session> ss = new ArrayList<Course.Session>();
		ss.add(comp.getSessionByString("L1"));
		ss.add(comp.getSessionByString("LA1"));
		enrolled.put(comp, ss);
		table.setEnrolled(enrolled);
		enrolled = table.getEnrolled();
		int expected[] = {2, 2, 3, 2}; // 2 courses, 2 array lists of sessions, 3 sessions for course(COMP2611), 2 sessions for course(COMP2011)
		if (enrolled.values().size()!=2) fail("The new course is not added");
		int actuals[] = new int[4];
		actuals[0] = enrolled.keySet().size();
		actuals[1] = enrolled.values().size();
		actuals[2] = enrolled.get(Course.getCourseByName("COMP2611 ")).size();
		actuals[3] = enrolled.get(Course.getCourseByName("COMP2011 ")).size();
		assertArrayEquals(expected, actuals);
	}

	@Test
	public void testGetOccupied() {
		table.dropCourse(Course.getCourseByName("COMP2611 "));
		table.addCourse("COMP1900 ", new String[]{"T1"});
		ArrayList<TimeSlot> occupied = table.getOccupied();
		TimeSlot actuals[] = new TimeSlot[occupied.size()];  
		occupied.toArray(actuals);
		TimeSlot expected[] = new TimeSlot[2];
		expected[0] = TimeSlot.getTimeSlotByStrings("We", "18:00");
		expected[1] = TimeSlot.getTimeSlotByStrings("We", "18:30");
		assertArrayEquals(expected, actuals);
	}

	@Test
	public void testAddCourseStringStringArray01() { // check course added
		table.addCourse("COMP1001 ", new String[]{"L1"});
		HashMap<Course, ArrayList<Course.Session>> enrolled = table.getEnrolled();
		assertNotNull(enrolled.get(Course.getCourseByName("COMP1001 ")));
	}
	
	@Test
	public void testAddCourseStringStringArray02() { // check only 1 session registered
		table.addCourse("COMP1001 ", new String[]{"L1"});
		HashMap<Course, ArrayList<Course.Session>> enrolled = table.getEnrolled();
		assertEquals(1, enrolled.get(Course.getCourseByName("COMP1001 ")).size());
	}
	
	@Test
	public void testAddCourseStringStringArray03() { // return true
		assertTrue(table.addCourse("COMP1001 ", new String[]{"L1"}));
	}
	
	@Test
	public void testAddCourseStringStringArray04() { // invalid session string
		assertFalse(table.addCourse("COMP1001 ", new String[]{"K1"}));
	}
	
	@Test
	public void testAddCourseStringStringArray05() { // invalid course id
		assertFalse(table.addCourse("COMP1001K", new String[]{"L1"}));
	}

	@Test
	public void testAddCourseCourseSessionArray01() { // check course added
		Course comp = Course.getCourseByName("COMP1001 ");
		table.addCourse(comp, new Course.Session[]{comp.getSessionByString("L1")});
		assertNotNull(table.getEnrolled().get(Course.getCourseByName("COMP1001 ")));
	}
	
	@Test
	public void testAddCourseCourseSessionArray02() { // check only 1 session registered
		Course comp = Course.getCourseByName("COMP1001 ");
		table.addCourse(comp, new Course.Session[]{comp.getSessionByString("L1")});
		assertEquals(1, table.getEnrolled().get(Course.getCourseByName("COMP1001 ")).size());
	}
	
	@Test
	public void testAddCourseCourseSessionArray03() { // return true
		Course comp = Course.getCourseByName("COMP1001 ");
		assertTrue(table.addCourse(comp, new Course.Session[]{comp.getSessionByString("L1")}));
	}
	
	@Test
	public void testAddCourseCourseSessionArray04() { // add course that enrolled
		Course comp = Course.getCourseByName("COMP2611 ");
		assertFalse(table.addCourse(comp, new Course.Session[]{comp.getSessionByString("L1")}));
	}
	
	@Test
	public void testAddCourseCourseSessionArray05() { // not exist session, will still run, but not added, check return true
		Course comp = Course.getCourseByName("COMP1001 ");
		assertTrue(table.addCourse(comp, new Course.Session[]{comp.getSessionByString("L8")}));
	}
	
	@Test
	public void testAddCourseCourseSessionArray06() { // not exist session, will still run, but not added, check no session added
		Course comp = Course.getCourseByName("COMP1001 ");
		table.addCourse(comp, new Course.Session[]{comp.getSessionByString("L8")});
		assertEquals(0, table.getEnrolled().get(comp).size());
	}
	
	@Test
	public void testAddCourseCourseSessionArray07() { // time conflicts between sessions within a course, return false
		Course comp = Course.getCourseByName("COMP2900 ");
		assertFalse(table.addCourse(comp, new Course.Session[]{comp.getSessionByString("T1"), comp.getSessionByString("T2")}));
	}
	
	@Test
	public void testAddCourseCourseSessionArray08() { // time conflicts with enrolled
		Course comp = Course.getCourseByName("COMP3721 ");
		assertFalse(table.addCourse(comp, new Course.Session[]{comp.getSessionByString("L1")}));
	}

	@Test
	public void testDropCourseString() {
		table.dropCourse("COMP2611 ");
		assertEquals(0, table.getEnrolled().size());
	}

	@Test
	public void testDropCourseCourse01() { // drop an enrolled course
		table.dropCourse(Course.getCourseByName("COMP2611 "));
		assertEquals(0, table.getEnrolled().size());
	}
	
	@Test
	public void testDropCourseCourse02() { // null parameter
		assertFalse(table.dropCourse(Course.getCourseByName("COMP1000K")));
	}
	
	@Test
	public void testDropCourseCourse03() { // course not enrolled
		assertFalse(table.dropCourse(Course.getCourseByName("COMP1001 ")));
	}

	@Test
	public void testSwapCourse01() { // Course added
		Course comp = Course.getCourseByName("COMP1001 ");
		table.swapCourse(Course.getCourseByName("COMP2611 "), comp, new Course.Session[]{comp.getSessionByString("L1")});
		assertNotNull(table.getEnrolled().get(Course.getCourseByName("COMP1001 ")));
	}
	
	@Test
	public void testSwapCourse02() { // only 1 session registered
		Course comp = Course.getCourseByName("COMP1001 ");
		table.swapCourse(Course.getCourseByName("COMP2611 "), comp, new Course.Session[]{comp.getSessionByString("L1")});
		assertEquals(1, table.getEnrolled().get(Course.getCourseByName("COMP1001 ")).size());
	}
	
	@Test
	public void testSwapCourse03() { // old course removed
		Course comp = Course.getCourseByName("COMP1001 ");
		table.swapCourse(Course.getCourseByName("COMP2611 "), comp, new Course.Session[]{comp.getSessionByString("L1")});
		assertNull(table.getEnrolled().get(Course.getCourseByName("COMP2611 ")));
	}
	
	@Test
	public void testSwapCourse04() { // return true
		Course comp = Course.getCourseByName("COMP1001 ");
		assertTrue(table.swapCourse(Course.getCourseByName("COMP2611 "), comp, new Course.Session[]{comp.getSessionByString("L1")}));
	}
	
	@Test
	public void testSwapCourse05() { // add one more course which will conflict with course to swap, return false
		table.addCourse("COMP3711 ", new String[]{"L1"});
		Course comp = Course.getCourseByName("COMP1001 ");
		assertFalse(table.swapCourse(Course.getCourseByName("COMP2611 "), comp, new Course.Session[]{comp.getSessionByString("L1")}));
	}
	
	@Test
	public void testSwapCourse06() { // conflict case, old course not removed
		table.addCourse("COMP3711 ", new String[]{"L1"});
		Course comp = Course.getCourseByName("COMP1001 ");
		table.swapCourse(Course.getCourseByName("COMP2611 "), comp, new Course.Session[]{comp.getSessionByString("L1")});
		assertNotNull(table.getEnrolled().get(Course.getCourseByName("COMP2611 ")));
	}
	
	@Test
	public void testSwapCourse07() { // conflict case, new course not added
		table.addCourse("COMP3711 ", new String[]{"L1"});
		Course comp = Course.getCourseByName("COMP1001 ");
		table.swapCourse(Course.getCourseByName("COMP2611 "), comp, new Course.Session[]{comp.getSessionByString("L1")});
		assertNull(table.getEnrolled().get(Course.getCourseByName("COMP1001 ")));
	}
	
	@Test
	public void testSwapCourse08() { // swap with enrolled
		Course comp = Course.getCourseByName("COMP1001 ");
		table.addCourse(comp, new Course.Session[]{comp.getSessionByString("L1")});
		assertFalse(table.swapCourse(Course.getCourseByName("COMP2611 "), comp, new Course.Session[]{comp.getSessionByString("L1")}));
	}

	@Test
	public void testExportString01() {
		String expected = "COMP2611 ;L1,T1,LA1";
		assertEquals(expected, table.exportString());
	}
	
	@Test
	public void testExportString02() {
		table.addCourse("COMP1900 ", new String[]{"T1"});
		table.addCourse("COMP1001 ", new String[]{"L1"});
		String expected = "COMP1001 ;L1;COMP1900 ;T1;COMP2611 ;L1,T1,LA1";
		assertEquals(expected, table.exportString());
	}

	@Test
	public void testImportFrom01() {
		Timetable table2 = new Timetable(9);
		table2.importFrom(table.exportString());
		String expected = "COMP2611 ;L1,T1,LA1";
		assertEquals(expected, table2.exportString());
	}
	
	@Test
	public void testImportFrom02() {
		Timetable table2 = new Timetable(9);
		table2.addCourse("COMP1001 ", new String[]{"L1"});
		table2.importFrom(table.exportString());
		String expected = "COMP1001 ;L1;COMP2611 ;L1,T1,LA1";
		assertEquals(expected, table2.exportString());
	}
	
	private Timetable table;

}
