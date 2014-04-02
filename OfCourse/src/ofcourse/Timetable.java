package ofcourse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
	}
	
	public Timetable(int tid) {
		table_id = tid;
	}
	
	public Timetable(int tid, JTabbedPane targetTabbedPage) {
		table_id = tid;
		targetTabbedPage.add(Integer.toString(tid), gui);
	}
	
	public int getTableId() {
		return table_id;
	}
	
	public void setTableId(int id) {
		table_id = id;
	}
		
	public JPanel getGUI() {
		return gui;
	}
	
	public HashMap<Course, ArrayList<Course.Session>> getEnrolled() {
		return enrolled;
	}

	public void setEnrolled(HashMap<Course, ArrayList<Course.Session>> enrolled) {
		this.enrolled = enrolled;
	}

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
	
	public boolean addCourse(String course_id, String[] sessions_id) {
		ArrayList<Course.Session> sessions = new ArrayList<Course.Session>();
		Course c = Course.getCourseByName(course_id);
		if (c == null) return false;
		for (Course.Session s : c.getSessions()) {
			for(String sessionid : sessions_id) {
				//DEBUG System.out.println(s.toString());
				if (s.toString().equals(sessionid)) {
					sessions.add(s);
				}
			}
		}
		if (sessions == null || sessions.size() == 0) 
			return false;
		return addCourse(c, sessions.toArray(new Course.Session[sessions.size()]));
	}
	
	public boolean addCourse(Course course, Course.Session[] sessions) {
		// if the course has already been enrolled, fails
		if (getEnrolled().containsKey(course)) {
			return false;
		}
		// ensure all provided sessions belong to the course
		ArrayList<Course.Session> trueSessions = new ArrayList<Course.Session>();
		for (Course.Session s : sessions) {
			if(course.getSessions().contains(s)) trueSessions.add(s);
		}
		
		// advanced checking for if all session types (lecture/lab/tutorial) are included
		ArrayList<SessionType> stype = new ArrayList<SessionType>(), ctype = new ArrayList<SessionType>();
		for (Course.Session s : course.getSessions()) {
			if (!stype.contains(s.getSType())) {
				stype.add(s.getSType());
			}
		}
		for (Course.Session s : trueSessions) {
			if (!ctype.contains(s.getSType())) {
				ctype.add(s.getSType());
			}
		}
		if (!ctype.containsAll(stype)) {
			return false;
		}
		
		// TODO: check whether matching between lecture, tutorial, and lab is needed
		
		//List all time slots to be used by this course registration
		ArrayList<TimeSlot> timeSlots = sessionsToSlots(trueSessions.toArray(new Course.Session[trueSessions.size()]));
		
		// check time conflict within itself
		// efficiency is not a matter: the array tends to be small
		for (int i = 0; i < timeSlots.size(); i++) {
            for (int j = 0; j < timeSlots.size(); j++) {
                if (timeSlots.get(i).equals(timeSlots.get(j)) && i != j) {
                    return false;
                }
            }
        }
		// check time conflict with enrolled
		ArrayList<TimeSlot> occupied = getOccupied();
		boolean conflict = false;
		for (int i = 0; i < timeSlots.size() && !conflict; i++) {
			if (occupied.contains(timeSlots.get(i))) {
				conflict = true;
			}
		}
		if (conflict) {
			return false;
		}
		else {
			//put it to enrolled
			ArrayList<Course.Session> ss = new ArrayList<Course.Session>();
			ss.addAll(trueSessions);
			//duplicate courses are automatically overwritten
			enrolled.put(course, ss);
		}

		//GUI call
		for (Course.Session s : trueSessions) {
			for (TimePeriod tp : s.getSchedule()) {
				try {
					gui.fillSlots(
							tp.getStartSlot().getID(), 
							tp.getEndSlot().getID(), 
							TimeTableGUI.getRandomBgColor(), 
							new String[] { course.toString(), s.toString() }, 
							course.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return true;
	}
	
	public boolean dropCourse(String course_id) {
		return dropCourse(Course.getCourseByName(course_id));
	}
	
	public boolean dropCourse(Course course) {
		if (course == null) 
			return false;
		if (!getEnrolled().containsKey(course)) {
			return false;
		}
		else {
			getEnrolled().remove(course);
		}
		//GUI call
		gui.unfillSlots(course.toString());
		return true;
	}
	
	public boolean swapCourse(Course origin, Course target, Course.Session[] sessions) {
		boolean conflict = false;
		
		// if target course has already been enrolled, fails
		if (getEnrolled().containsKey(target)) {
			return false;
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
			return false;
		}
		else {
			ArrayList<Course.Session> temp = getEnrolled().get(origin);
			if(dropCourse(origin) == false) return false;
			if(addCourse(target, sessions) == false) {
				addCourse(origin, temp.toArray(new Course.Session[temp.size()]));
			}
		}

		return true;
	}
	
	/** Export the attributes of the timetable (TID, Enrolled). Enrolled courses are exported as a list of class numbers.
	 * 
	 * @return A String which can be split for import
	 */
	public String exportTable() {
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
	
	/** Import timetable from String, replacing the original one, unchanged if failed
	 * 
	 * @param instr (should consist of integers only)
	 * @return True if the timetable is replaced by the import one, False means import failure and the timetable is unchanged
	 */
	public boolean importTable(String instr) {
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
				addSuccess = this.addCourse(acourse, sessions);
			}
		}
		if (!consistent || !addSuccess) {
			this.setTableId(old_tid);
			this.setEnrolled(old_enrolled);
			return false;
		}
		return true;
	}
	
//	public String exportString() {
//		String returnStr = new String();
//		HashMap<Course, ArrayList<Course.Session>> map = getEnrolled();
//		Comparator<Course> comparator = new Comparator<Course>() {
//			public int compare(Course c1, Course c2) {
//				return (c1.toString().compareTo(c2.toString()));
//			}
//		};
//		SortedSet<Course> keys = new TreeSet<Course>(comparator);
//		keys.addAll(map.keySet());
//		int count = 0, size = map.size();
//		for (Course key : keys) {
//			count++;
//			ArrayList<Course.Session> value = map.get(key);
//			returnStr += key.toString() + delim;
//			int count2 = 0, size2 = value.size();
//			for (Course.Session s : value) {
//				count2++;
//				returnStr += s.toString();
//				if (count2 != size2) returnStr += innerDelim;
//			}
//			if (count != size) returnStr += delim;
//		}
//		return returnStr;
//	}
//	
//	public boolean importFrom(String inStr) {
//		String tmp[] = inStr.split(delim);
//		String tmpSs[] = null;
//		if (tmp.length % 2 != 0) return false;
//		for (int i=0; i<tmp.length; i+=2) {
//			tmpSs = tmp[i+1].split(innerDelim);
//			addCourse(tmp[i], tmpSs);
//		}
//		return true;
//	}
	
	public static String delim = ";", innerDelim = ",";

	
	
	
	
	
	
	
	
	
	
	private Course selectedCourse = null;
	
	
	@Override
	public void courseSelected(String courseCode) {
		for(Course c : this.enrolled.keySet()) {
			if (c.getCode().toString().equals(courseCode)) {
				selectedCourse = c;
				//DEBUG: JOptionPane.showMessageDialog(null, courseCode);
				return;
			}
			else throw new java.util.NoSuchElementException("Cannot find the course for selection.");
		}
		
	}

	@Override
	public void courseUnselected(String courseCode) {
		selectedCourse = null;
	}

}
