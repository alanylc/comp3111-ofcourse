package ofcourse;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;

public class TimeSlot {
	public int ID;
	
	private static final int min = 0;
	private static final int max = 27;
	
	private final int dayStart = 830; //day start at 09:00 am , 
	
	private WeekDay day = WeekDay.Invalid;
	
	public static java.util.ArrayList<TimeSlot> instances = new java.util.ArrayList<TimeSlot>();
	
	public static TimeSlot getTimeSlotByID(int ID) {
		if (ID % 100 > max) throw new java.lang.IllegalArgumentException("Hour is too large");
		for (TimeSlot ts : instances) {
			if (ts.getID() == ID) return ts;
		}
		return new TimeSlot(ID);
		
	}
	
	//can return null if provided strings cannot be parsed
	public static TimeSlot getTimeSlotByStrings(String wd,String time) {
		SimpleDateFormat ft = new SimpleDateFormat ("hh:mm"); //it cannot parse hh:mma :(
		String map [] []=				//cannot use switch since 1.7 blahblahblah
			{
				{ "Su", "700" },
				{ "Mo", "100" },
				{ "Tu", "200" },
				{ "We", "300" },
				{ "Th", "400" },
				{ "Fr", "500" },
				{ "Sa", "600" }
			};
		for(String[] mapS:map){
			if(wd.equals(mapS[0]))wd=mapS[1];
		}
		Date t; 
		int identitiy;
		try { 
		    t = ft.parse(time); 
		    //System.out.println(t); 
		    //Any provided time at :20 and at :50 are round down to :00 and :30 respectively, 
		    //They are mostly end time. The rounded down result is now the start time of the correct slot.
		    //TODO: Might need to check some end time that end at :30 and :00?
		    identitiy=(int) (Integer.parseInt(wd)+(t.getHours()-9)*2+Math.floor(t.getMinutes()/30.0));
		    if(time.substring(5).equals("PM"))identitiy+=24;//if pm, add 12 hours
		    //System.out.println(ID);
		} catch (Exception e) { 
			System.out.println("Unparseable using " + ft+time); 
			return null;
		}
		//System.out.println(identitiy);
		return getTimeSlotByID(identitiy);
	}
	
	private TimeSlot(int id) {
		if (id % 100 > max) throw new java.lang.IllegalArgumentException("Hour is too large");
		ID = id;
	}
	
	private TimeSlot(WeekDay day, int hour, boolean min) { //time must be between 0900 and 1900
		//TODO: missing implementation
		//ID=day.getID()+(hour-9)*2+Math.ceil(min/30);
	}
	private TimeSlot(String wd,String time) { //wd is weekday
		SimpleDateFormat ft = new SimpleDateFormat ("hh:mm"); //it cannot parse hh:mma :(
		String map [] []=				//cannot use switch since 1.7 blahblahblah
			{
				{ "Su", "700" },
				{ "Mo", "100" },
				{ "Tu", "200" },
				{ "We", "300" },
				{ "Th", "400" },
				{ "Fr", "500" },
				{ "Sa", "600" }
			};
		for(String[] mapS:map){
			if(wd.equals(mapS[0]))wd=mapS[1];
		}
		Date t; 
		try { 
		    t = ft.parse(time); 
		    //System.out.println(t); 
		    ID=(int) (Integer.parseInt(wd)+(t.getHours()-9)*2+Math.floor(t.getMinutes()/30.0));
		    if(time.substring(5).equals("PM"))ID+=24;//if pm, add 12 hours
		    //System.out.println(ID);
		} catch (Exception e) { 
			System.out.println("Unparseable using " + ft+time); 
		}
     
	}
	
	public TimeSlot nextSlot() {
		int temp = ID + 1;
		if(temp % 100 > max) {
			temp = temp / 100 * 100 + 100;
		}
		return new TimeSlot(temp);
	}
	
	public int getID() {
		return ID;
	}
	
	public int getDayID() {
		return ID / 100;
	}
	
	public WeekDay getDay() throws Exception {
		switch (getDayID()) {
		case 1:
			return WeekDay.Mon;
		case 2:
			return WeekDay.Tue;
		case 3:
			return WeekDay.Wed;
		case 4:
			return WeekDay.Thu;
		case 5:
			return WeekDay.Fri;
		case 6:
			return WeekDay.Sat;
		case 7:
			return WeekDay.Sun;
		default:
			throw new Exception("Wrong Day ID");
		}
	}
	
	public int getTimeID() {
		return ID % 100;
	}
	
	private int getIntTime() {
		int a = dayStart; 
		//check for 60 mins, which is 1 hour
		a -= 30;
		for (int i = 0; i < this.getTimeID(); i ++) {
			a += 30;
			if (a - a / 100 * 100 == 60)
				a = a - 60 + 100;
		}
		return a;
	}
	
	public String getStartTime() {
		java.text.DecimalFormat df = new java.text.DecimalFormat("0000");
		return df.format(getIntTime());
	}
	
	public String getEndTime() {
		int b = getIntTime() + 30;
		if (b - b / 100 * 100 == 60)
			b = b - 60 + 100;
		b += 30;
		java.text.DecimalFormat df = new java.text.DecimalFormat("0000");
		return df.format(b);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		result = prime * result + ((day == null) ? 0 : day.hashCode());
		result = prime * result + dayStart;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeSlot other = (TimeSlot) obj;
		if (ID != other.ID)
			return false;
		if (day != other.day)
			return false;
		if (dayStart != other.dayStart)
			return false;
		return true;
	}
	
}






//public enum TimeSlot__ {
//	Mon0900(100), Mon0930(101), Mon1000(102), Mon1030(103), Mon1100(104), Mon1130(105), 
//	Mon1200(106), Mon1230(107), Mon1300(108), Mon1330(109), Mon1400(110), Mon1430(111), 
//	Mon1500(112), Mon1530(113), Mon1600(114), Mon1630(115), Mon1700(116), Mon1730(117), 
//	Mon1800(118), Mon1830(119), Mon1900(120), Mon1930(121), Mon2000(122), Mon2030(123),
//  Mon2100(124), Mon2130(125), Mon2200(126)
//	Tue0900(200), Tue0930(201), Tue1000(202), Tue1030(203), Tue1100(204), Tue1130(205), 
//	Tue1200(206), Tue1230(207), Tue1300(208), Tue1330(209), Tue1400(210), Tue1430(211), 
//	Tue1500(212), Tue1530(213), Tue1600(214), Tue1630(215), Tue1700(216), Tue1730(217), 
//	Tue1800(218), Tue1830(219), Tue1900(220),
//	Wed0900(300), Wed0930(301), Wed1000(302), Wed1030(303), Wed1100(304), Wed1130(305), 
//	Wed1200(306), Wed1230(307), Wed1300(308), Wed1330(309), Wed1400(310), Wed1430(311), 
//	Wed1500(312), Wed1530(313), Wed1600(314), Wed1630(315), Wed1700(316), Wed1730(317), 
//	Wed1800(318), Wed1830(319), Wed1900(320),
//	Thu0900(400), Thu0930(401), Thu1000(402), Thu1030(403), Thu1100(404), Thu1130(405), 
//	Thu1200(406), Thu1230(407), Thu1300(408), Thu1330(409), Thu1400(410), Thu1430(411), 
//	Thu1500(412), Thu1530(413), Thu1600(414), Thu1630(415), Thu1700(416), Thu1730(417), 
//	Thu1800(418), Thu1830(419), Thu1900(420),
//	Fri0900(500), Fri0930(501), Fri1000(502), Fri1030(503), Fri1100(504), Fri1130(505), 
//	Fri1200(506), Fri1230(507), Fri1300(508), Fri1330(509), Fri1400(510), Fri1430(511), 
//	Fri1500(512), Fri1530(513), Fri1600(514), Fri1630(515), Fri1700(516), Fri1730(517), 
//	Fri1800(518), Fri1830(519), Fri1900(520),
//	Sat0900(600), Sat0930(601), Sat1000(602), Sat1030(603), Sat1100(604), Sat1130(605), 
//	Sat1200(606), Sat1230(607), Sat1300(608), Sat1330(609), Sat1400(610), Sat1430(611), 
//	Sat1500(612), Sat1530(613), Sat1600(614), Sat1630(615), Sat1700(616), Sat1730(617), 
//	Sat1800(618), Sat1830(619), Sat1900(620),
//	Sun0900(700), Sun0930(701), Sun1000(702), Sun1030(703), Sun1100(704), Sun1130(705), 
//	Sun1200(706), Sun1230(707), Sun1300(708), Sun1330(709), Sun1400(710), Sun1430(711), 
//	Sun1500(712), Sun1530(713), Sun1600(714), Sun1630(715), Sun1700(716), Sun1730(717), 
//	Sun1800(718), Sun1830(719), Sun1900(720),
//	Invalid(0);
//	
//	private int id = 0;
//	
//	private TimeSlot(int id){
//		this.id = id;
//	}
//	
//	public int getID() {
//		return id;
//	}
//	
//	public int getDayID() {
//		return id / 100;
//	}
//	
//	public int getTimeID() {
//		return id % 100;
//	}
//	
//}
