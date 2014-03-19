package ofcourse;

public enum WeekDay {
	Mon(1), Tue(2), Wed(3), Thu(4), Fri(5), Sat(6), Sun(7), Invalid(0);
	
	private int val = 0;
	
	private WeekDay(int i) {
		val = i;
	}
	
	public int getID() {
		return val * 100;
	}
}
