package ofcourse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import ofcourse.Course.Session;
import ofcoursegui.TimeTableGUI;


public class Timetable implements ofcoursegui.CourseSelectListener {
	
	private int table_id = -1;
	private HashMap<Course, ArrayList<Course.Session>> enrolled = new HashMap<Course, ArrayList<Course.Session>>();
	private ofcoursegui.TimeTableGUI gui = new ofcoursegui.TimeTableGUI();
	
	{
		gui.addcourseSelectListener(this);
		gui.addpanelUnselectListener(this);
	}
	
	public Timetable(int tid) {
		table_id = tid;
	}
	
	/**
	 * Constructor, new tab page will be created
	 * @param tid
	 * @param targetTabbedPage
	 */
	public Timetable(int tid, JTabbedPane targetTabbedPage) {
		table_id = tid;
		targetTabbedPage.add(Integer.toString(tid), gui);
	}
	
	/**
	 * Getter of table ID
	 * @return <tt>this.table_id</tt>
	 */
	public int getTableId() {
		return table_id;
	}
	
	/** 
	 * Setter of table ID
	 * @param id New table ID (integer)
	 */
	public void setTableId(int id) {
		table_id = id;
	}
	
	public ofcoursegui.TimeTableGUI getGUI() {
		return gui;
	}

	
	/**
	 * Getter of <tt>this.enrolled</tt>
	 * @return <tt>this.enrolled</tt>
	 */
	public HashMap<Course, ArrayList<Course.Session>> getEnrolled() {
		return enrolled;
	}

	/**
	 * Set <tt>this.enrolled</tt> to <tt>enrolled</tt>
	 * @param enrolled
	 */
	public void setEnrolled(HashMap<Course, ArrayList<Course.Session>> enrolled) {
		this.enrolled = enrolled;
	}

	/** 
	 * Get all occupied time slots of the time table
	 * @return ArrayList of TimeSlots occupied by courses in the time table
	 */
	public ArrayList<TimeSlot> getOccupied() { 
		ArrayList<TimeSlot> arrList = new ArrayList<TimeSlot>();
		for (java.util.Map.Entry<Course, ArrayList<Course.Session>> enrolledCourse : getEnrolled().entrySet()) {
		    for (Course.Session session : enrolledCourse.getValue()) {
		    	Set<TimePeriod> periods = session.getSchedule();
		    	for (TimePeriod p : periods) {
		    		arrList.addAll(java.util.Arrays.asList(p.getAllSlots()));
		    	}
			}
		}
		return arrList;
	}
	
	/** 
	 * Convert a list of sessions to a list of time slots occupied by <tt>sessions</tt>
	 * @param sessions Array of course sessions
	 * @return Array List of <tt>TimeSlot</tt> of those occupied by <tt>sessions</tt>
	 */
	private ArrayList<TimeSlot> sessionsToSlots(Course.Session[] sessions) {
		//List all time slots to be used by this course registration
		ArrayList<TimeSlot> timeSlots = new ArrayList<TimeSlot>();
		for (Course.Session s : sessions) {
			ArrayList<TimeSlot> temp =  new ArrayList<TimeSlot>();
			for (TimePeriod tp : s.getSchedule()) {
				temp.addAll(Arrays.asList(tp.getAllSlots()));
			}
			timeSlots.addAll(temp);
		}
		return timeSlots;
	}
	
	/**
	 * Check whether the array list of sessions contain all session types and no duplicate session types
	 * @param course
	 * @param sessions
	 * @return 0 if all sessions are included and distinct, 1 if session type missing, 2 if duplicate session type
	 */
	public int containsAllSessionTypes(Course course, ArrayList<Session> sessions) {
		ArrayList<SessionType> stype = new ArrayList<SessionType>(), ctype = new ArrayList<SessionType>();
		for (Course.Session s : course.getSessions()) {
			if (!stype.contains(s.getSType())) {
				stype.add(s.getSType());
			}
		}
		for (Course.Session s : sessions) {
			if (!ctype.contains(s.getSType())) {
				ctype.add(s.getSType());
			}
			else { // multiple same session type
				return 2;
			}
		}
		if (!ctype.containsAll(stype)) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * Check if <tt>course</tt> is enrolled in this time table
	 * @param course
	 * @return TRUE if <tt>course</tt> enrolled, FALSE if <tt>course</tt> not enrolled
	 */
	public boolean courseEnrolled(Course course) {
		return getEnrolled().containsKey(course);
	}
	
	/**
	 * Get time slots that are conflicted with course enrolled
	 * @param sessions Sessions to be enrolled
	 * @return ArrayList of <tt>TimeSlot</tt> that conflicts with course enrolled, NULL if no conflicts
	 */
	public ArrayList<TimeSlot> getConflictedSlots(ArrayList<Course.Session> sessions) {
		//List all time slots to be used by this course registration
		ArrayList<TimeSlot> timeSlots = sessionsToSlots(sessions.toArray(new Course.Session[sessions.size()]));
		ArrayList<TimeSlot> occupied = getOccupied();
		ArrayList<TimeSlot> conflictSlots = new ArrayList<TimeSlot>();
		boolean conflict = false;
		for (int i = 0; i < timeSlots.size() && !conflict; i++) {
			if (occupied.contains(timeSlots.get(i))) {
				conflict = true;
				conflictSlots.add(timeSlots.get(i));
			}
		}
		if (!conflict) return null;
		return conflictSlots;
	}
	
	/** 
	 * Check whether time conflicts exist between each session in the array list <tt>sessions</tt> 
	 * @param sessions
	 * @return TRUE if no conflicts, FALSE if conflicts exist
	 */
	public boolean noSelfConflicts(ArrayList<Course.Session> sessions) {
		//List all time slots to be used by this course registration
		ArrayList<TimeSlot> timeSlots = sessionsToSlots(sessions.toArray(new Course.Session[sessions.size()]));
		// check time conflict within itself
		// efficiency is not a matter: the array tends to be small
		for (int i = 0; i < timeSlots.size(); i++) {
            for (int j = 0; j < timeSlots.size(); j++) {
                if (timeSlots.get(i).equals(timeSlots.get(j)) && i != j) {
                    return false;
                }
            }
        }
		return true;
	}
	
	
	/**
	 * Check whether lecture are matched with tutorial or lab, TRUE when matching is not required
	 * @param course
	 * @param sessions
	 * @return TRUE when matching is not required or sessions matched, FALSE when sessions unmatched
	 */
	public boolean sessionsMatched(Course course, ArrayList<Course.Session> sessions) {
		if (!course.isMatchSession()) { // no need to match session type
			return true;
		}
		int sNo = sessions.get(0).getSNo();
		for (Course.Session s : sessions) {
			if (sNo != s.getSNo())
				return false;
		}
		return true;
	}
	
	/** 
	 * Add course with <tt>course_id</tt> and specified sessions (<tt>sessions_id</tt>)
	 * @param course_id Course ID of the course
	 * @param sessions Array of String of session ID
	 * @return <tt>TimetableError.NoError</tt> if add success,
	 *  otherwise return the first matched of <tt>TimetableError</tt> in following order:
	 *   <tt>CourseNotExists, InvalidSessions, CourseEnrolled, SessionTypeMissed, DuplicateSessionType, 
	 *    SessionsNotMatched, SelfConflicts, TimeConflicts</tt>
	 */
	public TimetableError addCourse(String course_id, String[] sessions_id) {
		ArrayList<Course.Session> sessions = new ArrayList<Course.Session>();
		Course c = Course.getCourseByName(course_id);
		if (c == null) return TimetableError.CourseNotExists;
		for (Course.Session s : c.getSessions()) {
			for(String sessionid : sessions_id) {
				//Debug System.out.println(sessionid + " " + s.toString());
				if (s.toString().equals(sessionid)) {
					//DEBUG System.out.println("equal");
					sessions.add(s);
				}
			}
		}
		if (sessions.size() == 0 || sessions.size() != sessions_id.length) 
			return TimetableError.InvalidSessions;

		return addCourse(c, sessions.toArray(new Course.Session[sessions.size()]));
	}

	/** 
	 * Add <tt>Course</tt> with specified <tt>sessions</tt>
	 * @param course
	 * @param sessions
	 * @return <tt>TimetableError.NoError</tt> if add success,
	 *  otherwise return the first matched of <tt>TimetableError</tt> in following order:
	 *   <tt>CourseEnrolled, InvalidSessions, SessionTypeMissed, DuplicateSessionType, 
	 *    SessionsNotMatched, SelfConflicts, TimeConflicts</tt>
	 */
	public TimetableError addCourse(Course course, Course.Session[] sessions) {
		// if the course has already been enrolled, fails
		if (courseEnrolled(course)) {
			return TimetableError.CourseEnrolled;
		}
		// ensure all provided sessions belong to the course
		ArrayList<Course.Session> trueSessions = new ArrayList<Course.Session>();
		for (Course.Session s : sessions) {
			if(course.getSessions().contains(s)) trueSessions.add(s);
		}
		if (trueSessions.size() != sessions.length) {
			return TimetableError.InvalidSessions;
		}
				int flag = containsAllSessionTypes(course, trueSessions);
		if (flag==1) { // check if all session types (lecture/lab/tutorial) are included
			return TimetableError.SessionTypeMissed;
		}
		else if (flag==2) {
			return TimetableError.DuplicateSessionType;
		}
		if (!sessionsMatched(course, trueSessions)) { // check matching between lecture, tutorial, and lab is needed
			return TimetableError.SessionsNotMatched;

		}
		if (!noSelfConflicts(trueSessions)) {
			return TimetableError.SelfConflicts;
		}
		if (getConflictedSlots(trueSessions)!=null) {
			return TimetableError.TimeConflicts;
		}
		
		
		// put it to enrolled
		ArrayList<Course.Session> ss = new ArrayList<Course.Session>();
		ss.addAll(trueSessions);
		// duplicate courses are automatically overwritten
		enrolled.put(course, ss);

		// GUI call
		Color c = TimeTableGUI.getRandomBgColor();
		for (Course.Session s : trueSessions) {
			for (TimePeriod tp : s.getSchedule()) {
				try {
					gui.fillSlots(
							tp.getStartSlot().getID(), 
							tp.getEndSlot().getID(), 
							c, 
							new String[] { course.toString(), s.toString() }, 
							course.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return TimetableError.NoError;
	}
	
	/**
	 * Drop <tt>course</tt> from the timetable, and trigger GUI repaint
	 * @param course_id Course ID of course to be dropped
	 * @return <tt>TimetableError.NoError</tt> if drop success, otherwise return first matched of
	 *  <tt>TimetableError</tt> in the following order: <tt>CourseNotExists, CourseNotEnrolled</tt>
	 */
	public TimetableError dropCourse(String course_id) {
		return dropCourse(Course.getCourseByName(course_id));
	}
	
	/**
	 * Drop <tt>course</tt> from the timetable, and trigger GUI repaint
	 * @param course Course to be dropped
	 * @return <tt>TimetableError.NoError</tt> if drop success, otherwise return first matched of
	 *  <tt>TimetableError</tt> in the following order: <tt>CourseNotExists, CourseNotEnrolled</tt>
	 */
	public TimetableError dropCourse(Course course) {
		if (course == null) 
			return TimetableError.CourseNotExists;
		if (!courseEnrolled(course)) {
			return TimetableError.CourseNotEnrolled;
		}
		else {
			getEnrolled().remove(course);
		}
		//GUI call
		gui.unfillSlots(course.toString());
		return TimetableError.NoError;
	}
	
	/**
	 * Swap course <tt>origin</tt> with <tt>target</tt>
	 * @param origin Original course
	 * @param target Target course to replace original course
	 * @param sessions Sessions of target course to be enrolled
	 * @return <tt>TimetableError.NoError</tt> if drop success, otherwise return first matched of
	 *  <tt>TimetableError</tt> in the following order: origin - <tt>CourseEnrolled</tt>;  
	 *  target - <tt>TimeConflicts, CourseNotExists, CourseNotEnrolled,
	 *   CourseEnrolled, InvalidSessions, SessionTypeMissed, DuplicateSessionType, 
	 *    SessionsNotMatched, SelfConflicts, TimeConflicts</tt>
	 */
	public TimetableError swapCourse(Course origin, Course target, Course.Session[] sessions) {
		boolean conflict = false;
		
		// if target course has already been enrolled, fails
		if (courseEnrolled(target)) {
			return TimetableError.CourseEnrolled;
		}
		//List all time slots to if the original course is dropped
		ArrayList<TimeSlot> remainingSlots = new ArrayList<TimeSlot>();
		for (java.util.Map.Entry<Course, ArrayList<Course.Session>> enrolledCourse : getEnrolled().entrySet()) {
			if (enrolledCourse.getKey() == origin) continue;
			else {
			    for (Course.Session session : enrolledCourse.getValue()) {
			    	Set<TimePeriod> periods = session.getSchedule();
			    	for (TimePeriod p : periods) {
			    		remainingSlots.addAll(java.util.Arrays.asList(p.getAllSlots()));
			    	}
				}
			}
		}
		//List all time slots to be used by this course registration
		ArrayList<TimeSlot> timeSlots = sessionsToSlots(sessions);
		
		for (TimeSlot ts : timeSlots) {
			if (remainingSlots.contains(ts)) 
				conflict = true;
		}
		// conflict even after dropping
		if (conflict) {
			return TimetableError.TimeConflicts;
		}
		else {
			ArrayList<Course.Session> temp = getEnrolled().get(origin);
			TimetableError er = dropCourse(origin);
			if(er != TimetableError.NoError) return er;
			er = addCourse(target, sessions);
			if(er != TimetableError.NoError) { 
				// if add course fails after dropping, re-add the dropped course
				addCourse(origin, temp.toArray(new Course.Session[temp.size()])); // * what if this line fails
				return er;
			}
		}

		return TimetableError.NoError;
	}
	
	/** 
	 * Export the attributes of the timetable (TID, Enrolled). Enrolled courses are exported as a list of class numbers.
	 * @return A String which can be split for import
	 */
	public String exportString() {
		String returnStr = new String(this.getTableId()+delim);
		HashMap<Course, ArrayList<Course.Session>> enrolled = this.getEnrolled();
		Iterator<ArrayList<Session>> it = enrolled.values().iterator();
		while (it.hasNext()) {
			ArrayList<Session> sessionsOfOneCourse = it.next();
			for (Session session : sessionsOfOneCourse) {
				returnStr += session.getClassNo() + innerDelim;
			}
			returnStr += delim;
		}
		return returnStr;
	}
	
	/** 
	 * Import timetable from String, replacing the original one, unchanged if failed
	 * @param instr (should consist of integers only)
	 * @return TRUE if the timetable is replaced by the import one, FALSE means import failure and the timetable is unchanged
	 */
	public boolean importString(String instr) {
		String[] tuples = instr.trim().split(delim);
		try {
			Integer.parseInt(tuples[0]);
			for (int i=1; i<tuples.length; i++) {
				String[] ss = tuples[i].split(innerDelim);
				for (String s : ss) { // s is class number of a single session
					int class_num = Integer.parseInt(s);
					Course.getCourseByClassNum(class_num).getSessionByClassNumber(class_num);
				}
			}
		}
		catch (NumberFormatException e1) { // if contains any non-integer, return false
			return false;
		}
		catch (NullPointerException e2) {// non existing session
			return false;
		}
		boolean consistent = true; // used in checking whether a list of class numbers belong to the same course
		boolean addSuccess = true; // check whether all courses are added successfully
		// back up old data
		int old_tid = this.getTableId();
		HashMap<Course, ArrayList<Course.Session>> old_enrolled = this.getEnrolled();
		this.setTableId(Integer.parseInt(tuples[0]));
		this.setEnrolled(new HashMap<Course, ArrayList<Course.Session>>()); // empty the timetable
		for (int i=1; i<tuples.length && consistent && addSuccess; i++) {
			String[] classnums = tuples[i].trim().split(innerDelim);
			Course acourse = Course.getCourseByClassNum(Integer.parseInt(classnums[0]));
			Session[] sessions = new Session[classnums.length];
			for (int j=0; j<classnums.length; j++) {
				if (acourse != Course.getCourseByClassNum(Integer.parseInt(classnums[j]))) {
					consistent = false;
				}
				else {
					sessions[j] = acourse.getSessionByClassNumber(Integer.parseInt(classnums[j]));
				}
			}
			if (consistent) {
				addSuccess = (this.addCourse(acourse, sessions)==TimetableError.NoError);
			}
		}
		if (!consistent || !addSuccess) {
			this.setTableId(old_tid);
			this.setEnrolled(old_enrolled);
			return false;
		}
		return true;
	}
	
	/** 
	 * Write the string from method <tt>exportString()</tt> to file named <tt>filename</tt>
	 * @param filename
	 * @return TRUE if write success, FALSE if any IO exceptions occur 
	 */
	public boolean exportFile(String filename) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filename));
			String line = exportString();
			bw.write(line + "\n");
		}
		catch (FileNotFoundException ex) {
			return false;
		}
		catch (IOException ex) {
			return false;
		}
		
		finally {
			try {
				if (bw!=null) bw.close();
			} catch (IOException e) {
				System.out.println("Fatal error: fail to close file (" + filename + ")");
				return false;
			}
		}
		return true;
	}
	
	/** 
	 * Import timetable from String obtained from file named <tt>filename</tt>, replacing the original one, unchanged if failed. 
	 * Make use of the method <tt>importString(String line)</tt>
	 * @param filename
	 * @return TRUE if the timetable is replaced by the import one, FALSE means import failure and the timetable is unchanged
	 */
	public boolean importFile(String filename) {
		ArrayList<String> strArr = new ArrayList<String>();
		BufferedReader br = null;
		boolean importStatus = false;
		try {
			br = new BufferedReader(new FileReader(filename));
			String line = br.readLine();
			while (line != null) {
				strArr.add(line.trim());
				line = br.readLine();
			}
			importStatus = importString(strArr.get(0));
		}
		catch (FileNotFoundException ex) {
			return false;
		}
		catch (IOException ex) {
			return false;
		}
		finally {
			try {
				if (br!=null) br.close();
			} catch (IOException e) {
				System.out.println("Fatal error: fail to close file (" + filename + ")");
				return false;
			}
		}
		return importStatus;
	}
	
	public static String delim = ";", innerDelim = ",";
	
	private Course selectedCourse = null;
	
	public Course getSelectedCourse() {
		return selectedCourse;
	}
	
	@Override
	public void courseSelected(String courseCode) {
		//DEBUG: JOptionPane.showMessageDialog(null, courseCode);
		for(Course c : this.enrolled.keySet()) {
			if (c.getCode().toString().equals(courseCode)) {
				if (selectedCourse != null) this.gui.unselectSlots(selectedCourse.getCode().toString());
				selectedCourse = c;
				//DEBUG:  JOptionPane.showMessageDialog(null, courseCode);
				this.gui.selectSlots(courseCode);
				return;
			}
		}
		throw new java.util.NoSuchElementException("Cannot find the course for selection: " + courseCode);
	}

	@Override
	public void courseUnselected() {
		if (selectedCourse != null) this.gui.unselectSlots(selectedCourse.getCode().toString());
		selectedCourse = null;
	}
}
