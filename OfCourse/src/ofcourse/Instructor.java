package ofcourse;

import java.math.BigDecimal;

public class Instructor extends Ratable {
	public Instructor(String name) {
		super(name);
	}
	/*	DOES NOT WORK
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
	    if (ob.getClass() != Instructor.class) return false;
	    Instructor r = (Instructor)ob;
	    if (!eq(r.recordId, recordId)) return false;
	    if (!eq(r.recSubNum, recSubNum)) return false;
	    if (!eq(r.FileId, FileId)) return false;
	    if (!eq(r.category, category)) return false;
	    if (!eq(r.status, status)) return false;
	    if (!eq(r.errorCode, errorCode)) return false;
	    return true;
	  }

	  private static boolean eq(Object ob1, Object ob2) {
	    return ob1 == null ? ob2 == null : ob1.equals(ob2);
	  }

	  private static int hc(int hc, Object field) {
	    return field == null ? hc : 43 + hc * field.hashCode();
	  }
	  */
	  /*
	@Override
	public boolean equals(Object obj) {
		System.out.println("x");
		/*
	    if (this == obj)
	        return true;
	    if (obj == null)
	        return false;
	    if (getClass() != obj.getClass())
	        return false;
	    Instructor other = (Instructor) obj;
	    if (this.getName() == null) {
	        if (other.getName() != null)
	            return false;
	    } else if (!this.getName().equals(other.getName()))
	        return false;
	    return true;
	    */
	  /*
		try{
		    Instructor other = (Instructor) obj;			
			if (this.getName().equals(other.getName())){
				if(this.getName().equals("LIN, Sze Wai"))System.out.println(this.getName()+other.getName());
		        return true;
			}
		}catch(Exception e){
			return false;
		}
		return false;
	 }
	 */
}
