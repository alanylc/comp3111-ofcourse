package ofcourse;

import java.util.ArrayList;

public abstract class Ratable {
	private String name;
	private float avgRating = 0;//get avg rating during initialization
	private ArrayList<Comments> comments=new ArrayList<Comments>();//get comments only when you access the course(currently via out() on courseParse)
	class Comments{
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
		public float getRating() {
			return rating;
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
	public void parseComments() {
		//this.comments = comments;
	}
	public void parseAvgRating() {
		Network a=Network.getOurNetwork();
		try{
			avgRating=Float.parseFloat(a.getSummary(name)[0][1]);
		}catch(Exception e){
			avgRating=0;
		}
	}
	public void addComments(Comments comments) {
		this.comments.add(comments);
	}

}
