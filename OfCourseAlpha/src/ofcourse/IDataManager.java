package ofcourse;

import java.io.*;

public interface IDataManager {
	public Response retrieveReview(Ratable item);
	public Response sendReview(String review) ;
	public Response retrieveFriendList();
	public Response sendFriendRequest(String friend);
	public Response sendTimeTable(String timetable);
	public Response retrieveTimeTable(String friendName);
}
