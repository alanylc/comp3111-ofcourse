package ofcourse;

public abstract class Ratable {
	private String name;
	private int rating = 0;
	private String review = "";
	
	public Ratable(String name) {
		this.name = name;//TODO: check invalid characters
	}
	
	public int getRating() {
		return rating;
	}
	
	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;//TODO: check for invalid characters
	}

	public void setRating(int rating) {
		this.rating = rating;//TODO: check invalid rating
	}
	
	public String getName() {
		return name;
	}
}
