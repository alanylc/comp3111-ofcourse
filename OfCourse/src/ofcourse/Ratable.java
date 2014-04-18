package ofcourse;

import java.util.ArrayList;

public abstract class Ratable {
	private String name;
	private float avgRating = 0;//get avg rating during initialization
	private ArrayList<Comments> comments=new ArrayList<Comments>();//get comments only when you access the course(currently via out() on courseParse)
	public class Comments{
		private String commenterName;
		private float rating;
		private String comments="";
		private String date;//use string instead of date because we don't need to further manipulate the date, we just need to show it
		public Comments(String commentorName, float rating, String comments, String date) {
			this.commenterName = commentorName;
			this.rating = rating;
			this.comments = comments;
			this.date=date;
		}
		public String getCommentorName() {
			return commenterName;
		}
		public String getRating() {
			return String.valueOf(rating);
		}
		public String getComments() {
			return comments;
		}
		public String getDate() {
			return date;
		}
		
	}
	
	public Ratable(String name) {
		this.name = name;//TODO: check invalid characters
	}
	
	public float getAvgRating() {
		return avgRating;
	}
	public void setAvgRating(float avgRating) {
		this.avgRating = avgRating;
	}
	public String getName() {
		return name;
	}
	public ArrayList<Comments> getComments() {
		return comments;
	}
	public void parseComments() {	//for course only
		Network a=Network.getOurNetwork();
		this.comments=new ArrayList<Comments>();
		try{
			String[][] commentsForCourse=a.getCourse(name.substring(0, 4)+name.substring(5, 10));
			avgRating=0;
			try{
				avgRating=Float.parseFloat(a.getSummary(name.substring(0, 4)+name.substring(5, 10))[0][1]);
			}catch(Exception e){
				//do nothing
			}
			for(String[] cm:commentsForCourse){
				try{
					Comments c=new Comments(cm[0],Float.parseFloat(cm[1]),cm[2],cm[3]);
					//avgRating+=Float.parseFloat(cm[1]);
					this.comments.add(c);
				}catch(Exception e){
					//do nothing
				}
			}
			//avgRating/=commentsForCourse.length;
			//System.out.println(avgRating+"/"+commentsForCourse.length);
		}catch(Exception e){
			//do nothing
		}
	}

	public void addComments(Comments comments) {
		this.comments.add(comments);
	}

}
