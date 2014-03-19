package ofcourse;

import java.util.ArrayList;
//TODO: all finished class must be declared final
/**
 * <tt>Course</tt> represents a course. It contains all information that 
 * are normally available in the student center: all sessions, description 
 * code, instructors and quota.
 * 
 * @author Bob
 */
public class Course extends Ratable {
	//TODO: check valid data at all times
	private Code code;
	private boolean matchSession = false;
	private ArrayList<Session> sessions;
	private String description = "";
	//private Course[] prerequsite = null;
	
	public static ArrayList<Course> AllCourses = new ArrayList<Course>();
	
	public static Course getCourseByName(String name) {
		for(Course c : AllCourses) {
			if (name.equals(c.toString())) {
				return c;
			}
		}
		return null;
	}
	
	{
		AllCourses.add(this);
	}
	
	/**
	 * Construct a new course with the provided information. 
	 * 
	 * @param dept
	 * @param number
	 * @param modifier
	 * @param name
	 */
	public Course(String dept, int number, char modifier, String name, boolean matchSession) {
		super(name);
		code = new Code(dept, number, modifier);//TODO: check valid arguments
		setMatchSession(matchSession);
	}
	
	/**
	 * Construct a new course with the provided information. 
	 * @param code
	 * @param name
	 */
	public Course(Code code, String name, boolean matchSession) {
		super(name);
		this.code = (Code) code.clone();//Must be a copy to prevent unexpected modification
		setMatchSession(matchSession);
	}
	
	/**
	 * Gets the description of the course.
	 * @return The string representation of the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the <tt>Code</tt> object that represent the code of the course.
	 * For example, COMP3111H a course code.
	 * @return The <tt>Code</tt> object
	 */
	public Code getCode() {
		return code;
	}

	/**
	 * Gets whether the course require matching session.
	 * @return <tt>True</tt> means matching is required, otherwise <tt>false</tt>
	 */
	public boolean isMatchSession() {
		return matchSession;
	}

	/**
	 * Gets the sessions of the course. Each session is identified by, 
	 * for example, LA2 or T3, a session type and a session number.
	 * @return sessions An array of sessions
	 */
	public ArrayList<Session> getSessions() {
		return sessions;
	}

	/**
	 * Sets the sessions of the course. Each session is identified by, 
	 * for example, LA2 or T3, a session type and a session number.
	 * @param sessions An array of sessions
	 */
	public void setSessions(ArrayList<Session> sessions) {
		this.sessions = sessions;
	}

	/**
	 * Sets whether the course require match session numbers of different sessions.
	 * @param matchSession <tt>True</tt> means matching is required, otherwise <tt>false</tt>
	 */
	public void setMatchSession(boolean matchSession) {
		this.matchSession = matchSession;
	}
	
	/**
	 * Sets the description of the course. 
	 * @param description A <tt>String</tt> of the description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public Session getSessionByString(String session) {
		for(Session s : getSessions()) {
			if (session.equals(s.toString())) {
				return s;
			}
		}
		return null;
	}

	public Session getSessionByClassNumber(int classNo) {
		for(Session s : getSessions()) {
			if (classNo == (s.getClassNo())) {
				return s;
			}
		}
		return null;
	}
	
	public String toString() {
		return code.toString();
	}
	
	private class Code {
		private final String dept;
		private final int number;
		private final char modifier;
		
		public Code(String dept, int number, char modifier) {
			this.dept = new String(dept);//Must be a copy to prevent accidental modification
			this.number = number;//TODO: check valid arguments
			this.modifier = modifier;
		}
		
		public String getDept(){
			return dept.toUpperCase();
		}
		
		public int getNumber() {
			return number;
		}
		
		public char getMod() {
			return Character.toUpperCase(modifier);
		}
		
		@Override
		public String toString() {
			return getDept().toUpperCase() + Integer.toString(getNumber()) + getMod();
		}
	
		public Object clone() {
			return new Code(dept, number, modifier);
		}
	}
	
	/**
	 * <tt>Session</tt> represent a single session of a course. Each 
	 * <tt>Session</tt> is defined to be a lecture, or lab, or 
	 * tutorial, together with a number. For example, ABCD1234 L2 
	 * is a lecture session 2 of course with <tt>Code</tt> ABCD1234. 
	 * Therefore, if L2 has lessons on 2 separate days, they count 
	 * as one session. Each lesson of the <tt>Session</tt> is 
	 * represented as a <tt>TimePeriod</tt> and all lessons were 
	 * put in an array. Each session has an array of <tt>Instructor
	 * </tt>s. Each session is also uniquely identified by a 4-digit 
	 * class number.
	 * 
	 * @author Bob
	 *
	 */
	public class Session {
		//TODO: check valid data at all times
		private SessionType sType = SessionType.Lecture;
		private int sNo = 1;
		
		private ArrayList<TimePeriod> schedule = null;//TODO: Null or length = 0 for TBA
		private int classNo;//usually 4-digit number for course enrollment
		private ArrayList<Instructor> instructors= null;//TODO: Null or length = 0 for TBA
		private int availableQuota = 0;
		private int quota = 0;
			
		
		/**
		 * Construct a new Session with the specified session type: 
		 * Lecture, Laboratory or Tutorial, a session number, an 
		 * array of time periods for the lessons, and the class number 
		 * that uniquely identifies the session.
		 * 
		 * @param sessionType		Type of session: Lecture, Laboratory or Tutorial
		 * @param sessionNumber		e.g LA2, 2 is the session number of this lab session
		 * @param classNo			class number that uniquely identifies the session
		 */
		public Session(SessionType sessionType, int sessionNumber, int classNo) {
			setSType(sessionType);
			setSNo(sessionNumber);
			setClassNo(classNo);
		}
		
		
		/**
		 * Gets the TOTAL quota of the session.
		 * @return The total quota.
		 */
		public int getQuota() {
			return quota;
		}
		
		/** 
		 * Sets the total quota of the session.
		 * 
		 * @param quota The total quota
		 */
		public void setQuota(int quota) {
			this.quota = quota;
		}
		

		/**
		 * Gets the session type of this session.
		 * 
		 * @return A <tt>SessionType</tt> enum. Lecture, Laboratory or Tutorial
		 */
		public SessionType getSType() {
			return sType;
		}
		
		/**
		 * Sets the session type of this session.
		 * 
		 * @param A <tt>SessionType</tt> enum. Lecture, Laboratory or Tutorial
		 */
		public void setSType(SessionType sType) {
			this.sType = sType;
		}
		
		/**
		 * Gets the session number,e.g. for LA2, the session number is 2.
		 * 
		 * @return The session number of this session
		 */
		public int getSNo() {
			return sNo;
		}
		
		/**
		 * Sets the session number,e.g. for LA2, the session number is 2.
		 * 
		 * @param The session number of this session
		 */
		public void setSNo(int sessionNumber) {
			//TODO: check valid arguments
			this.sNo = sessionNumber;
		}
		
		/**
		 * Gets the 4-digit number that identifies this session.
		 * 
		 * @return a 4-digit number
		 */
		public int getClassNo() {
			return classNo;
		}
		
		/**
		 * Sets the 4-digit number that identifies this session.
		 * 
		 * @return a 4-digit number
		 */
		public void setClassNo(int classNo) {
			this.classNo = classNo;
		}
		
		/**
		 * Gets the schedule of this session.
		 * 
		 * @return An array of <tt>TimePeriod</tt>. Each of them represent a continuous period of a lesson of this session.
		 */
		public ArrayList<TimePeriod> getSchedule() {
			return schedule;
		}

		/**
		 * Sets the schedule of this session.
		 * 
		 * @param An array of <tt>TimePeriod</tt>. Each of them represent a continuous period of a lesson of this session.
		 */
		public void setSchedule(ArrayList<TimePeriod> schedule) {
			this.schedule = (ArrayList<TimePeriod>) schedule.clone();//Must be a copy to prevent accidental modification
		}
		
		/**
		 * Gets all instructors teaching the session.
		 * 
		 * @return An array of <tt>Instructor</tt> that are teaching the course.
		 */
		public ArrayList<Instructor> getInstructors() {
			return instructors;
		}

		/**
		 * Sets all instructors teaching the session.
		 * 
		 * @param An array of <tt>Instructor</tt> that are teaching the course.
		 */
		public void setInstructors(ArrayList<Instructor> instructors) {
			this.instructors = (ArrayList<Instructor>) instructors.clone();
		}
		
		@Override
		public String toString() {
			//TODO: sesion type is invalid case
			String type = getSType() == SessionType.Laboratory ? "LA" : getSType().toString().substring(0, 1).toUpperCase();
			return type+ getSNo();
		}

	}
}
