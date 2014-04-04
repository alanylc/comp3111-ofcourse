package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import ofcourse.Course;
import ofcourse.TimeSlot;
import ofcourse.Timetable;
import ofcourse.TimetableError;
import ofcourse.courseParse;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TimetableTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		courseParse.parse("COMP"); // the static variable Course.AllCourses should now have the COMP course list

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		table = new Timetable(5); // TID = 5
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
		expected[0] = TimeSlot.getTimeSlotByStrings("We", "06:00PM");
		expected[1] = TimeSlot.getTimeSlotByStrings("We", "06:30PM");
		assertArrayEquals(expected, actuals);
	}

	@Test
	public void testAddCourseStringStringArray01() { // check: course added
		table.addCourse("COMP1001 ", new String[]{"L1", "LA1"});
		HashMap<Course, ArrayList<Course.Session>> enrolled = table.getEnrolled();
		assertNotNull(enrolled.get(Course.getCourseByName("COMP1001 ")));
	}
	
	@Test
	public void testAddCourseStringStringArray02() { // check: 2 session registered
		table.addCourse("COMP1001 ", new String[]{"L1", "LA1"});
		HashMap<Course, ArrayList<Course.Session>> enrolled = table.getEnrolled();
		assertEquals(2, enrolled.get(Course.getCourseByName("COMP1001 ")).size());
	}
	
	@Test
	public void testAddCourseStringStringArray03() { // return true
		assertEquals(TimetableError.NoError, table.addCourse("COMP1001 ", new String[]{"L1", "LA1"}));
	}
	
	@Test
	public void testAddCourseStringStringArray04() { // invalid session string
		assertEquals(TimetableError.InvalidSessions, table.addCourse("COMP1001 ", new String[]{"L8", "LA9"}));
	}
	
	@Test
	public void testAddCourseStringStringArray05() { // invalid course id
		assertEquals(TimetableError.CourseNotExists, table.addCourse("COMP1001K", new String[]{"L1", "LA1"}));
	}

	@Test
	public void testAddCourseCourseSessionArray01() { // check course added
		Course comp = Course.getCourseByName("COMP1001 ");
		table.addCourse(comp, new Course.Session[]{comp.getSessionByString("L1"), comp.getSessionByString("LA1")});
		assertNotNull(table.getEnrolled().get(Course.getCourseByName("COMP1001 ")));
	}
	
	@Test
	public void testAddCourseCourseSessionArray02() { // check: 2 session registered
		Course comp = Course.getCourseByName("COMP1001 ");
		table.addCourse(comp, new Course.Session[]{comp.getSessionByString("L1"), comp.getSessionByString("LA1")});
		assertEquals(2, table.getEnrolled().get(Course.getCourseByName("COMP1001 ")).size());
	}
	
	@Test
	public void testAddCourseCourseSessionArray03() { // return true
		Course comp = Course.getCourseByName("COMP1001 ");
		assertEquals(TimetableError.NoError, table.addCourse(comp, new Course.Session[]{comp.getSessionByString("L1"), comp.getSessionByString("LA1")}));
	}
	
	@Test
	public void testAddCourseCourseSessionArray04() { // add course that enrolled
		Course comp = Course.getCourseByName("COMP2611 ");
		assertEquals(TimetableError.CourseEnrolled, table.addCourse(comp, new Course.Session[]{comp.getSessionByString("L1"), comp.getSessionByString("T2"), comp.getSessionByString("LA1")}));
	}
	
	@Test
	public void testAddCourseCourseSessionArray05() { // not exist session, L8 will not be in trueSessions, thus failing the test that all session type included
		Course comp = Course.getCourseByName("COMP1001 ");
		assertEquals(TimetableError.InvalidSessions, table.addCourse(comp, new Course.Session[]{comp.getSessionByString("L8"), comp.getSessionByString("LA1")}));
	}
	
	@Test
	public void testAddCourseCourseSessionArray06() { // duplicate session type
		Course comp = Course.getCourseByName("COMP2900 ");
		assertEquals(TimetableError.DuplicateSessionType, table.addCourse(comp, new Course.Session[]{comp.getSessionByString("T1"), comp.getSessionByString("T2")}));
	}
	
	@Test
	public void testAddCourseCourseSessionArray07() { // time conflicts with enrolled
		Course comp = Course.getCourseByName("COMP3721 ");
		assertEquals(TimetableError.TimeConflicts, table.addCourse(comp, new Course.Session[]{comp.getSessionByString("L1"), comp.getSessionByString("T1")}));
	}

	@Test
	public void testDropCourseString() { // drop an enrolled course
		table.dropCourse("COMP2611 ");
		assertEquals(0, table.getEnrolled().size());
	}

	@Test
	public void testDropCourseCourse01() { // drop an enrolled course
		table.dropCourse(Course.getCourseByName("COMP2611 "));
		assertEquals(0, table.getEnrolled().size());
	}
	
	@Test
	public void testDropCourseCourse02() { // null parameter due to non-exist course
		assertEquals(TimetableError.CourseNotExists, table.dropCourse(Course.getCourseByName("COMP1000K")));
	}
	
	@Test
	public void testDropCourseCourse03() { // course not enrolled
		assertEquals(TimetableError.CourseNotEnrolled, table.dropCourse(Course.getCourseByName("COMP1001 ")));
	}

	@Test
	public void testSwapCourse01() { // Course added
		Course comp = Course.getCourseByName("COMP1001 ");
		table.swapCourse(Course.getCourseByName("COMP2611 "), comp, new Course.Session[]{comp.getSessionByString("L1"), comp.getSessionByString("LA1")});
		assertNotNull(table.getEnrolled().get(Course.getCourseByName("COMP1001 ")));
	}
	
	@Test
	public void testSwapCourse02() { // 2 session registered
		Course comp = Course.getCourseByName("COMP1001 ");
		table.swapCourse(Course.getCourseByName("COMP2611 "), comp, new Course.Session[]{comp.getSessionByString("L1"), comp.getSessionByString("LA1")});
		assertEquals(2, table.getEnrolled().get(Course.getCourseByName("COMP1001 ")).size());
	}
	
	@Test
	public void testSwapCourse03() { // old course removed
		Course comp = Course.getCourseByName("COMP1001 ");
		table.swapCourse(Course.getCourseByName("COMP2611 "), comp, new Course.Session[]{comp.getSessionByString("L1"), comp.getSessionByString("LA1")});
		assertNull(table.getEnrolled().get(Course.getCourseByName("COMP2611 ")));
	}
	
	@Test
	public void testSwapCourse04() { // return true
		Course comp = Course.getCourseByName("COMP1001 ");
		assertEquals(TimetableError.NoError, table.swapCourse(Course.getCourseByName("COMP2611 "), comp, new Course.Session[]{comp.getSessionByString("L1"), comp.getSessionByString("LA1")}));
	}
	
	@Test
	public void testSwapCourse05() { // add one more course which will conflict with course to swap, return false
		table.addCourse("COMP3711 ", new String[]{"L1", "T1"});
		Course comp = Course.getCourseByName("COMP1001 ");
		assertEquals(TimetableError.TimeConflicts, table.swapCourse(Course.getCourseByName("COMP2611 "), comp, new Course.Session[]{comp.getSessionByString("L1"), comp.getSessionByString("LA1")}));
	}
	
	@Test
	public void testSwapCourse06() { // conflict case, old course not removed
		table.addCourse("COMP3711 ", new String[]{"L1", "T1"});
		Course comp = Course.getCourseByName("COMP1001 ");
		table.swapCourse(Course.getCourseByName("COMP2611 "), comp, new Course.Session[]{comp.getSessionByString("L1"), comp.getSessionByString("LA1")});
		assertNotNull(table.getEnrolled().get(Course.getCourseByName("COMP2611 ")));
	}
	
	@Test
	public void testSwapCourse07() { // conflict case, new course not added
		table.addCourse("COMP3711 ", new String[]{"L1", "T1"});
		Course comp = Course.getCourseByName("COMP1001 ");
		table.swapCourse(Course.getCourseByName("COMP2611 "), comp, new Course.Session[]{comp.getSessionByString("L1"), comp.getSessionByString("L1")});
		assertNull(table.getEnrolled().get(Course.getCourseByName("COMP1001 ")));
	}
	
	@Test
	public void testSwapCourse08() { // swap with enrolled
		Course comp = Course.getCourseByName("COMP1001 ");
		table.addCourse(comp, new Course.Session[]{comp.getSessionByString("L1"), comp.getSessionByString("LA1")});
		assertEquals(TimetableError.CourseEnrolled, table.swapCourse(Course.getCourseByName("COMP2611 "), comp, new Course.Session[]{comp.getSessionByString("L1"), comp.getSessionByString("LA1")}));
	}
	
	@Test
	public void testExportTable01() {
		String[] expected = new String[] {"5", "1851", "1860", "1855"}; // TID, L1, T1, LA1
		String exportLine = table.exportString();
		String[] results = exportLine.split(Timetable.delim);
		int count = 0; // check number of integers in the string
		for (String s : results) {
			count += s.split(Timetable.innerDelim).length;
		}
		String[] actuals = new String[count];
		actuals[0] = new String(results[0]);
		count = 1; // change the use to be index of array actuals
		for (int i=1; i<results.length; i++) {
			String[] ss = results[i].split(Timetable.innerDelim);
			for (String s : ss) {
				actuals[count] = new String(s);
				count++;
			}
		}
		java.util.Arrays.sort(actuals);
		java.util.Arrays.sort(expected);
		assertArrayEquals(expected, actuals);
	}
	
	@Test
	public void testExportTable02() {
		table.addCourse("COMP1900 ", new String[]{"T1"});
		table.addCourse("COMP1001 ", new String[]{"L1", "LA1"});
		String[] expected = new String[] {"5", "1851", "1860", "1855", "1823", "1780", "1784"};
		String exportLine = table.exportString();
		String[] results = exportLine.split(Timetable.delim);
		int count = 0; // check number of integers in the string
		for (String s : results) {
			count += s.split(Timetable.innerDelim).length;
		}
		String[] actuals = new String[count];
		actuals[0] = new String(results[0]);
		count = 1; // change the use to be index of array actuals
		for (int i=1; i<results.length; i++) {
			String[] ss = results[i].split(Timetable.innerDelim);
			for (String s : ss) {
				actuals[count] = new String(s);
				count++;
			}
		}
		java.util.Arrays.sort(actuals);
		java.util.Arrays.sort(expected);
		assertArrayEquals(expected, actuals);
	}
	
	@Test
	public void testImportTable01() {
		Timetable table2 = new Timetable(9);
		String exportLine = table.exportString();
		boolean success = table2.importString(exportLine);
		assertTrue(success);
	}
	
	@Test
	public void testImportTable02() {
		Timetable table2 = new Timetable(9);
		assertTrue(table2.importString(table.exportString()));
		String[] expected = new String[] {"5", "1851", "1860", "1855"};
		String exportLine = table2.exportString();
		String[] results = exportLine.split(Timetable.delim);
		int count = 0; // check number of integers in the string
		for (String s : results) {
			count += s.split(Timetable.innerDelim).length;
		}
		String[] actuals = new String[count];
		actuals[0] = new String(results[0]);
		count = 1; // change the use to be index of array actuals
		for (int i=1; i<results.length; i++) {
			String[] ss = results[i].split(Timetable.innerDelim);
			for (String s : ss) {
				actuals[count] = new String(s);
				count++;
			}
		}
		java.util.Arrays.sort(actuals);
		java.util.Arrays.sort(expected);
		assertArrayEquals(expected, actuals);
	}
	
	@Test
	public void testImportTable03() {
		table.addCourse("COMP1900 ", new String[]{"T1"});
		table.addCourse("COMP1001 ", new String[]{"L1", "LA1"});
		Timetable table2 = new Timetable(9);
		table2.importString(table.exportString());
		String[] expected = new String[] {"5", "1851", "1860", "1855", "1823", "1780", "1784"};
		String exportLine = table2.exportString();
		String[] results = exportLine.split(Timetable.delim);
		int count = 0; // check number of integers in the string
		for (String s : results) {
			count += s.split(Timetable.innerDelim).length;
		}
		String[] actuals = new String[count];
		actuals[0] = new String(results[0]);
		count = 1; // change the use to be index of array actuals
		for (int i=1; i<results.length; i++) {
			String[] ss = results[i].split(Timetable.innerDelim);
			for (String s : ss) {
				actuals[count] = new String(s);
				count++;
			}
		}
		java.util.Arrays.sort(actuals);
		java.util.Arrays.sort(expected);
		assertArrayEquals(expected, actuals);
	}
	
	@Test
	public void testImportTable04() {
		String importLine = "5;1823,;33,1860,1855,;1780,1784,;";  // 33 is invalid class number
		assertFalse(table.importString(importLine));
	}
	
	@Test
	public void testImportTable05() {
		String importLine = "5;1823,;1780,1860,1855,;1780,1784,;";  // 1780 does not belong to the same course 
		assertFalse(table.importString(importLine));
	}
	
	@Test
	public void testImportTable06() {
		String importLine = "5;1823,;178..0,1860,1855,;1780,1784,;";  // non-integer
		assertFalse(table.importString(importLine));
	}
	
	@Test
	public void testImportTable07() {
		String importLine = "5;1823,;1851,1860,1855,;1780,1784,;1823;";  // add duplicated course, to make addCourse fails
		assertFalse(table.importString(importLine));
	}
	
//	@Test
//	public void testExportString01() {
//		String expected = "COMP2611 ;L1,T1,LA1";
//		assertEquals(expected, table.exportString());
//	}
//	
//	@Test
//	public void testExportString02() {
//		table.addCourse("COMP1900 ", new String[]{"T1"});
//		table.addCourse("COMP1001 ", new String[]{"L1", "LA1"});
//		String expected = "COMP1001 ;L1,LA1;COMP1900 ;T1;COMP2611 ;L1,T1,LA1";
//		assertEquals(expected, table.exportString());
//	}
//
//	@Test
//	public void testImportFrom01() {
//		Timetable table2 = new Timetable(9);
//		table2.importFrom(table.exportString());
//		String expected = "COMP2611 ;L1,T1,LA1";
//		assertEquals(expected, table2.exportString());
//	}
//	
//	@Test
//	public void testImportFrom02() {
//		Timetable table2 = new Timetable(9);
//		table2.addCourse("COMP1001 ", new String[]{"L1", "LA1"});
//		table2.importFrom(table.exportString());
//		String expected = "COMP1001 ;L1,LA1;COMP2611 ;L1,T1,LA1";
//		assertEquals(expected, table2.exportString());
//	}
	
	private Timetable table;

}
