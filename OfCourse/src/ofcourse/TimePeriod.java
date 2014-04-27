package ofcourse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class TimePeriod {
	//private WeekDay day;
	private TimeSlot startSlot;
	private TimeSlot endSlot;
	//therefore must be continuous
	/*
	public TimePeriod(TimeSlot startSlot, TimeSlot endSlot) throws IllegalArgumentException {
		//check to only allow time slot in same day
		if (startSlot.getDayID() != endSlot.getDayID()) 
			throw new IllegalArgumentException("A time period cannot run across two or more days.");
		//check for start time is earlier than end time
		if (startSlot.getTimeID() > endSlot.getTimeID()) 
			throw new IllegalArgumentException("A time period start time must be earlier than end time.");
		this.startSlot = startSlot;
		this.endSlot = endSlot;
	}
	*/
	public TimePeriod(String sub, String time){				//construct from "We", "03:30PM - 06:20PM" to obj
		String StartTime=time.substring(0,time.indexOf(" - "));
		String EndTime=time.substring(time.indexOf(" - ")+3);
		//TimeSlot sTS=new TimeSlot(sub,StartTime);
		TimeSlot sTS = TimeSlot.getTimeSlotByStrings(sub, StartTime);
		//TimeSlot eTS=new TimeSlot(sub,EndTime);
		//TODO: Wrong implementation?
		TimeSlot eTS = TimeSlot.getTimeSlotByStrings(sub, EndTime);
		this.startSlot=sTS;
		this.endSlot=eTS;
	}

	public TimeSlot getStartSlot() {
		return startSlot;
	}

	public TimeSlot getEndSlot() {
		return endSlot;
	}
	
	public TimeSlot[] getAllSlots() {
		ArrayList<TimeSlot> slots = new ArrayList<TimeSlot>();
		TimeSlot t = startSlot;
		//TODO: error case
		if (t == null) return null;
		while(t.getID() != endSlot.getID()) {
			slots.add(t);
			t = t.nextSlot();
		}
		slots.add(endSlot);
		return slots.toArray(new TimeSlot[slots.size()]);
	}
	
	public int[] getAllSlotsID() {
		TimeSlot[] slots = getAllSlots();
		int[] l = new int[slots.length];
		for (int i = 0; i < slots.length; i++) {
			l[i] = slots[i].getID();
		}
		return l;
	}
	public int[] getStartEndID(){
		int[] i=new int[2];
		i[0]=startSlot.ID;
		i[1]=endSlot.ID;
		return i;
	}
	
	@Override
	public String toString() {
		try {
			String start = startSlot.getStartTime().substring(0, 2) + ":"
					+ startSlot.getStartTime().substring(2, 4);
			String end = endSlot.getEndTime().substring(0, 2) + ":"
					+ endSlot.getEndTime().substring(2, 4);
			return startSlot.getDay().toString() + " " + start + "-" + end;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	//Problem on Same Time Period solved
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endSlot == null) ? 0 : endSlot.hashCode());
		result = prime * result
				+ ((startSlot == null) ? 0 : startSlot.hashCode());
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
		TimePeriod other = (TimePeriod) obj;
		if (endSlot == null) {
			if (other.endSlot != null)
				return false;
		} else if (!endSlot.equals(other.endSlot))
			return false;
		if (startSlot == null) {
			if (other.startSlot != null)
				return false;
		} else if (!startSlot.equals(other.startSlot))
			return false;
		return true;
	}

	public static String getDistinctStr(Set<TimePeriod> schedule) {
		TreeMap<String, String> arr =  new TreeMap<String, String>();
		for (TimePeriod tp : schedule) {
			String timeStr = null;
			try {
				String tmp = tp.toString();
				String tmp_day = tp.getStartSlot().getDay().toString();
				timeStr = tmp.substring(tmp.indexOf(tmp_day)+tmp_day.length()+1); // one space
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String data = null;
			try {
				data = arr.get(timeStr)==null ? "" : arr.get(timeStr);
				data = data + tp.getStartSlot().getDay().toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			arr.put(timeStr, data);
		}
		String schStr = "";
		Iterator<Entry<String, String>> it = arr.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			schStr += entry.getValue() + " " + entry.getKey();
			if (it.hasNext()) schStr += "\n";
		}
		if (arr.entrySet().size()==0) {
			schStr = "TBA";
		}
		return schStr;
	}
	
	/*DOES NOT WORK
	private BigDecimal recordId = null;
	private BigDecimal recSubNum = null;
	private BigDecimal FileId = null;
	private String category = null;
	private BigDecimal status = null;
	private BigDecimal errorCode = null;

	  @Override
	  public int hashCode() {
	    int ret = 41;
	    ret = hc(ret, recordId);
	    ret = hc(ret, recSubNum);
	    ret = hc(ret, FileId);
	    ret = hc(ret, category);
	    ret = hc(ret, status);
	    ret = hc(ret, errorCode);
	    return ret;
	  }

	  @Override
	  public boolean equals(Object ob) {
		//System.out.println("x");
	    if (ob == null) return false;
	    if (ob.getClass() != TimePeriod.class) return false;
	    TimePeriod r = (TimePeriod)ob;
	    //if (!eq(r.recordId, recordId)) return false;
	    //if (!eq(r.recSubNum, recSubNum)) return false;
	    if (!eq(r.FileId, FileId)) return false;
	    //if (!eq(r.category, category)) return false;
	    //if (!eq(r.status, status)) return false;
	    //if (!eq(r.errorCode, errorCode)) return false;
	    return true;
	  }

	  private static boolean eq(Object ob1, Object ob2) {
	    return ob1 == null ? ob2 == null : ob1.equals(ob2);
	  }

	  private static int hc(int hc, Object field) {
	    return field == null ? hc : 43 + hc * field.hashCode();
	  }
	  */
}
