package ofcourse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
/**
 * Handling all communication between client and server containing username, password, comments, timetables, etc
 * List of error codes:
 * 100: OK: When a request is done successfully.
 * 000: Character error: Name contains invalid characters
 * 001: Username exists: The username that user inputs has been registered.
 * 002: Username/Password incorrect: The username/password that user inputs are incorrect.
 * 003: Email not sent: The email containing the password cannot be sent due to server side error.
 * 004: Duplicate entry when insert: Sending a friend request repeatedly.
 * 005: Entry not exist when update: No known method can produce this error code.
 * 404: Network error: Then there is no network or server is down.
 * @author hin
 *
 */
@SuppressWarnings("deprecation")
public class Network {//chk courseParse.out() for implementation of 
	private String username;
	private String password; //store user password, always encrypted
	private final String URL = "http://vtnetwork.synology.me/3111/";
	private final String USER_AGENT = "Mozilla/5.0";
	private static Network ourNetwork=new Network();
	
	private Network() { // empty constructor
		username = "";
		password = "";
	}
	/**
	 * 
	 * @return	The username/password set that the network class currently using
	 */
	public static Network getOurNetwork(){
		return ourNetwork;
	}
	/**
	 * 
	 * @return	The username that the network class currently using
	 */
	public static String getOurNetworkUserName(){
		return ourNetwork.username;
	}
	/**
	 * 
	 * @return	The password that the network class currently using
	 */
	public static String getOurNetworkPassword(){
		return ourNetwork.password;
	}
	/**
	 * 
	 * @param username ITSC name of user
	 * @param password Password of user
	 * @return Network object containing the username and password that the user inputs. Note that there is no authentication in this method, use getTimetable instead. 
	 */
	public static Network login(String username, String password){
		ourNetwork.username=username;
		ourNetwork.password=encryptPW(password);
		return ourNetwork;
	}
	/**
	 * Sets the username and password to null.
	 */
	public static void logout(){
		ourNetwork.username="";
		ourNetwork.password="";
	}
	/**
	 * 
	 * @param line String which uses ASCII HEX 11-14 (DC1-DC4) to separate array elements.
	 * @param b Number of elements in one array
	 * @return 2D array parsed from line.
	 */
	public String[][] dataCut(String line, int b) { // use ASCII HEX 11-14 (DC1-DC4), don't tell me anyone is typing them.
		int a = StringUtils.countMatches(line, ""); // change string with DC1-DC4 as separator to 2D array
		String[][] data = new String[a][b]; // line is input string and b is number of parameters in one row(1-4)
		int currentRow = -1;
		int cuthead = 0;
		int cuttail = 0;
		for (cuttail = 0; cuttail < line.length(); cuttail++) {
			if (line.charAt(cuttail) == '') {
				currentRow++;
				data[currentRow][0] = line.substring(cuthead, cuttail);
				cuthead = cuttail + 1;
			}
			if (line.charAt(cuttail) == '') {
				data[currentRow][1] = line.substring(cuthead, cuttail);
				cuthead = cuttail + 1;
			}
			if (line.charAt(cuttail) == '') { // third element is hax
				data[currentRow][2] = fromHexString(line.substring(cuthead,
						cuttail));
				cuthead = cuttail + 1;
			}
			if (line.charAt(cuttail) == '') {
				data[currentRow][3] = line.substring(cuthead, cuttail);
				cuthead = cuttail + 1;
			}
		}
		return data;
	}
	/**
	 * Print 2D array (unused)
	 * @param matrix 2Darray.
	 */
	public void printArray(String matrix[][]) { // Print 2d array
		for (int row = 0; row < matrix.length; row++) {
			for (int column = 0; column < matrix[row].length; column++) {
				System.out.print(matrix[row][column] + " ");
			}
			System.out.println();
		}
	}
	/**
	 * Changes from UTF-8 hex string to string
	 * @param hex A hex string which contains 0-9,A-F only
	 * @return String parsed from hex.
	 */
	public String fromHexString(String hex) { // UTF-8 hex string to string
		ByteBuffer buff = ByteBuffer.allocate(hex.length() / 2);
		for (int i = 0; i < hex.length(); i += 2) {
			buff.put((byte) Integer.parseInt(hex.substring(i, i + 2), 16));
		}
		buff.rewind();
		Charset cs = Charset.forName("UTF-8"); // BBB
		CharBuffer cb = cs.decode(buff); // BBB
		return cb.toString();
	}
	/**
	 * Register account on database, and will send an email to user's ITSC account.
	 * @param username ITSC account name the user inputs.
	 * @return Reply from server.
	 */
	public String register(String username) { // input name for register. Will send an email. Output 100 if ok.
		String[][] empty = { { "", "" } };
		ourNetwork.username = username;
		return this.POST("insert.php", empty);
	}
	/**
	 * Register account on database, but will NOT send an email to user's ITSC account. (debug only, you have to manually go to server to look for password)
	 * @param username ITSC account name the user inputs.
	 * @return Reply from server.
	 */
	public String registerB(String username) { // Same as above, but no email sent(debug only)
		String[][] empty = { { "", "" } };
		ourNetwork.username = username;
		return this.POST("insertB.php", empty);
	}
	/**
	 * Get the user's favourite courses from server
	 * @return Reply from server.
	 */
	public String getMyFav() { // My Favorites as output
		String[][] empty = { { "", "" } };
		if (ourNetwork.username.equals(""))return "200";
		return this.POST("select.php", empty);
	}
	/**
	 * Get the user's friend list from server, output as 2Darray
	 * @return Reply from server.
	 */
	public String[][] getFriendListAndTimeTable() { // Get my friend list and timetable
		String[][] empty = { { "", "" } };
		if (ourNetwork.username.equals(""))return new String[][]{{"200"}};
		String line = this.POST("getfriendandtimetable.php", empty);
		return dataCut(line, 2);
	}
	/**
	 * Get the list of people who have sent friend request to you from server, output as reqfriend1!reqfriend2!...!
	 * @return Reply from server.
	 */
	public String getFriendList() { // Get ppl who have sent friend request to you, output as reqfriend1!reqfriend2!...!
		String[][] empty = { { "", "" } };
		if (ourNetwork.username.equals(""))return "200";
		return this.POST("getfriend.php", empty);
	}
	/**
	 * Get the list of people who have sent friend request to you from server, output as reqfriend1!reqfriend2!...!
	 * @return Reply from server.
	 */
	public String getReqFriendList() { // Get ppl who have sent friend request to you, output as reqfriend1!reqfriend2!...!
		String[][] empty = { { "", "" } };
		if (ourNetwork.username.equals(""))return "200";
		return this.POST("getreqfriend.php", empty);
	}
	/**
	 * Get the user's time table string
	 * @return Reply from server.
	 */
	public String getTimeTable() { 
		String[][] empty = { { "", "" } };
		if (ourNetwork.username.equals(""))return "200";
		return this.POST("getTimeTable.php", empty);
	}
	/**
	 * Get the user's password (for debug only, if the account is newly created, the received string is plaintext, if the password has been modified by 'firstNewPW()', it would be hashed)
	 * @return Reply from server.
	 */
	public String getPW() { 
		String[][] empty = { { "", "" } };
		if (ourNetwork.username.equals(""))return "200";
		return this.POST("getPW.php", empty);
	}
	/**
	 * Upload your timetable to server.
	 * @param TimeTable TimeTable String
	 * @return Reply from server.
	 */
	public String setTimeTable(String TimeTable) {
		if (ourNetwork.username.equals(""))return "200";
		String[][] setTimeTableA = { { "TimeTable", TimeTable } };
		return this.POST("setTimeTable.php", setTimeTableA);
	}
	/**
	 * Remove an account (debug only)
	 * @param ITSC ITSC account that have to be removed.
	 * @return Reply from server.
	 */
	public String removeAccount(String ITSC) { 
		String[][] removeAccountA = { { "ITSC", ITSC } };
		return this.POST("delAC.php", removeAccountA);
	}
	/**
	 * Change your password FOR FIRST TIME
	 * @param ppw Original password
	 * @param npw New password
	 * @return Reply from server, 100 if ok.
	 */
	public String firstNewPW(String ppw, String npw) { // Change Password FOR FIRST TIME, output 100 if ok
		if (ourNetwork.username.equals(""))return "200";
		npw = encryptPW(npw);
		String[][] newpwA = { { "ppw", ppw }, { "npw", npw } };
		String newpw = this.POST("newpw.php", newpwA);
		if (newpw.equals("100"))
			ourNetwork.password = npw;
		return newpw;
	}
	/**
	 * Change your password if not at your first time.
	 * @param opw Original password
	 * @param npw New password
	 * @return Reply from server, 100 if ok.
	 */
	public String newPW(String opw, String npw) { // Change Password, output 100 if ok
		if (ourNetwork.username.equals(""))return "200";
		opw = encryptPW(opw);
		npw = encryptPW(npw);
		String[][] newpwA = { { "opw", opw }, { "npw", npw } };
		String newpw = this.POST("newpw.php", newpwA);
		if (newpw.equals("100"))
			ourNetwork.password = npw;
		return newpw;
	}
	/**
	 * Check if the password has not been changed from the one sent via email.
	 * @return True is password has NOT changed, false otherwise.
	 */
	public Boolean chkFirstPW() {
		String[][] empty = { { "", "" } };
		if (this.POST("chkfpw.php", empty).equals("Y"))
			return true;// true means password has NOT changed (it is the first pw)
		return false;
	}
	/**
	 * User gives a comment on a course, which is sent to server.
	 * @param Course	String of the course that the user comments.
	 * @param Grade		rating that the user gives (String.valueOf(int i), "" if none)
	 * @param Comment	Comment that the user gives, "" if none.
	 * @return Reply from server, 100 if ok.
	 */
	public String comment(String Course, String Grade, String Comment) {// Leave a comment, output 100 if ok
		if (ourNetwork.username.equals(""))return "200";
		byte[] bb = Comment.getBytes();
		try {
			bb = Comment.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[][] commentA = { { "Course", Course }, { "Grade", Grade },	//TODO only allow grade of 1/2/3/4/5
				{ "Comment", convertToHex(bb) } };
		return this.POST("comment.php", commentA);
	}
	/**
	 * Input course (COMP), get Courses and their avg ratings
	 * @param Course "COMP" or other subjects
	 * @return	2D array containing the courses and their avg ratings
	 */
	public String[][] getSummary(String Course) { // Input course (COMP), get Courses and their avg ratings
		String[][] getsummaryA = { { "Course", Course } };
		String line = this.POST("getsummary.php", getsummaryA);
		return dataCut(line, 2);
	}
	/**
	 * Get comments for that course, output as 2D string array
	 * @param Course
	 * @return 2D string array containing the comments, refer to ratable.parseComments()
	 */
	public String[][] getCourse(String Course) { // Get comments for that course, output as 2D string array
		String[][] getcourseA = { { "Course", Course } };
		String line = this.POST("getcourse.php", getcourseA);
		return dataCut(line, 4);
	}
	/**
	 * Sends a friend request to another user.
	 * @param friendB another friend's ITSC name
	 * @return	Reply from server, 100 if ok.
	 */
	public String friendReq(String friendB) { // Send a friend request
		if (ourNetwork.username.equals(""))return "200";
		String[][] reqfriendA = { { "Nameb", friendB } };
		return this.POST("reqfriend.php", reqfriendA);
	}
	/**
	 * Confirms a friend request.
	 * @param friendB another friend's ITSC name.
	 * @return Reply from server, 100 if ok.
	 */
	public String friendSet(String friendB) { // Confirm a friend request
		if (ourNetwork.username.equals(""))return "200";
		String[][] setfriendA = { { "Nameb", friendB } };
		return this.POST("setfriend.php", setfriendA);
	}
	/**
	 * Change your MyFav and upload to server
	 * @param MyFav String containing my favourite courses.
	 * @return Reply from server, 100 if ok.
	 */
	public String setMyFav(String MyFav) { // Change your MyFav and upload
		if (ourNetwork.username.equals(""))return "200";
		String[][] setfavA = { { "Myfav", MyFav } };
		return this.POST("setfav.php", setfavA);
	}

//	void SetPW(String p) { // Unused
//		password = encryptPW(p);
//	}

	private static String convertToHex(byte[] data) { // byte to hash string(0-F)
		StringBuilder buf = new StringBuilder();
		for (byte b : data) {
			int halfbyte = (b >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte)
						: (char) ('a' + (halfbyte - 10)));
				halfbyte = b & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	private static String encryptPW(String pw) { // One way function for password
		String salt = "5oD1uM Chl0RiD3";// do not change
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] sha1hash = (pw + salt).getBytes("UTF-8");
			for (int i = 0; i < 10032; i++) { // Just... don't... change... it
				md.update(sha1hash);
				sha1hash = md.digest();
			}
			pw = convertToHex(sha1hash);

		} catch (Exception e) {
			return "";
		}
		return pw;
	}

	private String POST(String url, String[][] para) { // input url and parameters, output string received from url

		String returned;
		try {
			String furl = URL + url;

			@SuppressWarnings({ "resource" })
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(furl);

			// add header
			post.setHeader("User-Agent", USER_AGENT);

			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			for (int i = 0; i < para.length; i++) {
				urlParameters
						.add(new BasicNameValuePair(para[i][0], para[i][1]));
			}
			urlParameters.add(new BasicNameValuePair("Name", username));
			urlParameters.add(new BasicNameValuePair("Password", password));
			post.setEntity(new UrlEncodedFormEntity(urlParameters));

			HttpResponse response = client.execute(post);//throws IOException, ClientProtocolException
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));//throws IOException, IllegalStateException

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {//throws IOException
				result.append(line);
			}

			returned = result.toString();
		} catch (Exception e) {// If no Internet or other exception related to PHP POST, return 404 error
			return "404";
		}
		return returned;
	}


	/**
	 * Before every test run:
	 * 1. Go to server and remove testaaa user 
	 * 2. remove testaaa and ctestaab's entry on friend.
	 * @param args
	 */
	public static void main(String[] args) {
		
	}
	/**
	 * Use only during tests.
	 */
	public static void initialize() {
		// TODO Auto-generated method stub
		String[][] empty = { { "", "" } };
		Network x = getOurNetwork();
		x.POST("initialize.php", empty);
	}

}
