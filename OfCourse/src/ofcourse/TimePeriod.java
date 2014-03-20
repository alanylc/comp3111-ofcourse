package ofcourse;

import java.util.ArrayList;
import java.lang.IllegalArgumentException;

public class TimePeriod {
	//private WeekDay day;
	private TimeSlot startSlot;
	private TimeSlot endSlot;
	//therefore must be continuous
	
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
	public TimePeriod(String sub, String time){				//construct from "We", "03:30PM - 06:20PM" to obj
		String StartTime=time.substring(1,time.indexOf(" - "));
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
}
