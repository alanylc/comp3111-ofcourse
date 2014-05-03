package ofcourse;

import java.util.ArrayList;
/**
 * Represents an object which users can give a rate and comment to it. 
 * @author hin
 *
 */
public abstract class Ratable {
	private String name;
	private float avgRating = 0;//get avg rating during initialization
	private ArrayList<Comments> comments=new ArrayList<Comments>();//get comments only when you access the course(currently via out() on courseParse)
	/**
	 * Represents the rate and comment to a Ratable object.
	 * @author hin
	 *
	 */
	public class Comments{
		private String commenterName;
		private float rating;
		private String comments="";
		private String date;//use string instead of date because we don't need to further manipulate the date, we just need to show it
		/**
		 * Creates a Comments.
		 * @param commentorName The user name of the one who give this comment
		 * @param rating The rating.
		 * @param comments The comment.
		 * @param date The date when this comment is given.
		 */
		public Comments(String commentorName, float rating, String comments, String date) {
			this.commenterName = commentorName;
			this.rating = rating;
			this.comments = comments;
			this.date=date;
		}
		/**
		 * Gets the name of the commentor.
		 * @return The name of the commentor.
		 */
		public String getCommentorName() {
			return commenterName;
		}
		/**
		 * Gets the rating in string.
		 * @return The rating in string
		 */
		public String getRating() {
			return String.valueOf(rating);
		}
		/**
		 * Gets the comment.
		 * @return The comment.
		 */
		public String getComments() {
			return comments;
		}
		/**
		 * Gets the date when this comment is given, in string.
		 * @return The date when this comment is given.
		 */
		public String getDate() {
			return date;
		}
		
	}
	/**
	 * Creates a Ratable object.
	 * @param name The name of this object.
	 */
	public Ratable(String name) {
		this.name = name;//TODO: check invalid characters
	}
	
	/**
	 * Gets the average rating of all users of this object.
	 * @return The average rating of all users of this object.
	 */
	public float getAvgRating() {
		return avgRating;
	}
	/**
	 * Sets the average rating of all users of this object.
	 * @param avgRating The average rating of all users of this object.
	 */
	public void setAvgRating(float avgRating) {
		this.avgRating = avgRating;
	}
	/**
	 * Gets the name of this Ratable object.
	 * @return The name of this Ratable object.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets all Comments on this Ratable object.
	 * @return An ArrayList of all Comments on this Ratable object. Any change is reflected and stored in this object.
	 */
	public ArrayList<Comments> getComments() {
		return comments;
	}
	/**
	 * Fetch all Comments which are given to the object with the same name from the server.
	 */
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

	/**
	 * Adds a Comments to this object.
	 * @param comments Comments
	 */
	public void addComments(Comments comments) {
		this.comments.add(comments);
	}

}
