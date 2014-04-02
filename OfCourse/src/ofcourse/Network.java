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
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class Network {
	private String username;
	private String password; //store user password, always encrypted
	private final String URL = "http://vtnetwork.synology.me/3111/";
	private final String USER_AGENT = "Mozilla/5.0";
	private static Network ourNetwork=new Network();
	
	private Network() { // empty constructor
		username = "";
		password = "";
	}
	public static Network getOurNetwork(){
		return ourNetwork;
	}
	public static Network login(String username, String password){
		ourNetwork.username=username;
		ourNetwork.password=encryptPW(password);
		return ourNetwork;
	}
	public static void logout(){
		ourNetwork.username="";
		ourNetwork.password="";
	}

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

	public void printArray(String matrix[][]) { // Print 2d array
		for (int row = 0; row < matrix.length; row++) {
			for (int column = 0; column < matrix[row].length; column++) {
				System.out.print(matrix[row][column] + " ");
			}
			System.out.println();
		}
	}

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

	public String register(String username) { // input name for register. Will send an email. Output 100 if ok.
		String[][] empty = { { "", "" } };
		ourNetwork.username = username;
		return this.POST("insert.php", empty);
	}

	public String registerB(String username) { // Same as above, but no email sent(debug only)
		String[][] empty = { { "", "" } };
		ourNetwork.username = username;
		return this.POST("insertB.php", empty);
	}

	public String getMyFav() { // My Favorites as output
		String[][] empty = { { "", "" } };
		if (ourNetwork.username.equals(""))return "200";
		return this.POST("select.php", empty);
	}

	public String getFriendList() { // Get my friend list, output as friend1!friend2!...!
		String[][] empty = { { "", "" } };
		if (ourNetwork.username.equals(""))return "200";
		return this.POST("getfriend.php", empty);
	}

	public String getReqFriendList() { // Get ppl who have sent friend request to you, output as reqfriend1!reqfriend2!...!
		String[][] empty = { { "", "" } };
		if (ourNetwork.username.equals(""))return "200";
		return this.POST("getreqfriend.php", empty);
	}

	public String firstNewPW(String ppw, String npw) { // Change Password FOR FIRST TIME, output 100 if ok
		if (ourNetwork.username.equals(""))return "200";
		npw = encryptPW(npw);
		String[][] newpwA = { { "ppw", ppw }, { "npw", npw } };
		String newpw = this.POST("newpw.php", newpwA);
		if (newpw.equals("100"))
			ourNetwork.password = npw;
		return newpw;
	}

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
	
	public Boolean chkFirstPW() {
		String[][] empty = { { "", "" } };
		if (this.POST("chkfpw.php", empty).equals("Y"))
			return true;// true means password has NOT changed (it is the first pw)
		return false;
	}

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

	public String[][] getSummary(String Course) { // Input course (COMP), get Courses and their avg ratings
		String[][] getsummaryA = { { "Course", Course } };
		String line = this.POST("getsummary.php", getsummaryA);
		return dataCut(line, 2);
	}

	public String[][] getCourse(String Course) { // Get comments for that course, output as 2D string array
		String[][] getcourseA = { { "Course", Course } };
		String line = this.POST("getcourse.php", getcourseA);
		return dataCut(line, 4);
	}

	public String friendReq(String friendB) { // Send a friend request
		if (ourNetwork.username.equals(""))return "200";
		String[][] reqfriendA = { { "Nameb", friendB } };
		return this.POST("reqfriend.php", reqfriendA);
	}

	public String friendSet(String friendB) { // Confirm a friend request
		if (ourNetwork.username.equals(""))return "200";
		String[][] setfriendA = { { "Nameb", friendB } };
		return this.POST("setfriend.php", setfriendA);
	}

	public String setMyFav(String MyFav) { // Change your MyFav and upload (NOT WORKING)
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
		} catch (Exception e) {
			return e.toString();
		}
		return returned;
	}

	public String testPassword(String a) {
		String[][] newpwA = { { "ppw", a }, { "npw", a } };
		String newpw = this.POST("newpw.php", newpwA);
		if (newpw.equals("100"))
			ourNetwork.password = a;
		return newpw;
	}
	/**
	 * test run:
	 * 1. Go to server and remove testaaa user 
	 * 2. remove A and B's entry on friend.
	 * @param args
	 */
	public static void main(String[] args) {
		// 000 char error
		// 001 Name registered
		// 002 wrong Name/pw
		// 003 email not sent
		// 004 duplicate entry when insert 
		// 005 entry not exist when update
		// 100 ok
		// 200 not loggined but doing activities that need to login(friend get/set, myfav,etc)
		// 404 Query return false: SQL server connection failed
		// TODO Auto-generated method stub

		System.out.println("New Network");
		Network x = getOurNetwork();
		String ITSC = "testaaa";
		System.out.println("Registering " + ITSC);
		System.out.println(x.registerB(ITSC));// if 100 then ok (insertB is used
												// since I don't want to send
												// email)

		
		
		
		
		
		//ITSC = "xxx";
		//System.out.println("Another New Network " + ITSC + "pwd=aaa");
		
		System.out.print("Recieved Pasword: ");
		String s = new Scanner(System.in).nextLine();
		/*
		System.out.println("Test password");
		System.out.println(x.testPassword(s));// if 100 then ok
		
*/
		System.out.println("Set pwd first time " + "pwd=(original) to pwd=666" );
		System.out.println(x.firstNewPW(s, "666"));// if 100 then ok

		System.out.println("Set new pwd "+ "pwd=666 to pwd=aaa");
		System.out.println(x.newPW("666", "aaa"));// if 100 then ok

		
		
		System.out.println("Login with new pwd");
		x = login(ITSC, "aaa");

		System.out.println("Set A's favourite");
		String MyFav = "COMP3111";// Still not decided how to do this
		
		
		System.out.println(x.setMyFav(MyFav));// if 100 then ok

		System.out.println("Get A's favourite");
		System.out.println(x.getMyFav());// myfav

		System.out.println("Get A's firends");
		System.out.println(x.getFriendList());// friend1!friend2!....!

		System.out.println("Get A's requests");
		System.out.println(x.getReqFriendList());// reqfriend1!reqfriend2!....!

		System.out.println("Post a comment");
		String Course = "COMP3111";
		String Grade = "4";
		String Comment = "上載日期";

		System.out.println(x.comment(Course, Grade, Comment));// if 100 then ok


		System.out.println("Get a comment");
		x.printArray(x.getCourse(Course));// get course comments(2d array)

		Course = "COMP";
		System.out.println("Get summary for all course in one department: " + Course);
		x.printArray(x.getSummary(Course));// show all comp course with their
												// avg rating
		Course = "COMP3111";
		System.out.println("Get avg rating in one course: " + Course);
		x.printArray(x.getSummary(Course));// show all comp course with their
												// avg rating

		System.out.println("Send a friend request to B");
		String friendB = "testaab";
		System.out.println(x.friendReq(friendB));// if 100 then ok


		System.out.println("Login as B");
		ITSC = "testaab";
		x = login(ITSC, "bbb");
/*
		System.out.println("Set friendB pwd for first time");
		System.out.println(x.firstNewPW("bbb", "bbb"));// if 100 then ok
*/
		System.out.println("Get B s friend request");
		System.out.println(x.getReqFriendList());// reqfriend1!reqfriend2!....!

		System.out.println("Accept friend request");
		friendB = "testaaa";
		System.out.println(x.friendSet(friendB));// if 100 then ok


		System.out.println("Get B's friend list");
		System.out.println(x.getFriendList());// friend1!friend2!....!
		
		System.out.println("Login back as A");
		ITSC = "testaaa";
		x = login(ITSC, "aaa");
		
		System.out.println("Get A's friend list");
		System.out.println(x.getFriendList());// friend1!friend2!....!
		
		System.out.println("Behavior when logged out: All the following should return 200");
		logout();

		System.out.println("Set nobody's favourite");
		MyFav = "COMP3111";// Still not decided how to do this
		
		
		System.out.println(x.setMyFav(MyFav));// if 100 then ok

		System.out.println("Get nobody's favourite");
		System.out.println(x.getMyFav());// myfav

		System.out.println("Get nobody's firends");
		System.out.println(x.getFriendList());// friend1!friend2!....!

		System.out.println("Get nobody's requests");
		System.out.println(x.getReqFriendList());// reqfriend1!reqfriend2!....!

		System.out.println("Post nobody's comment");
		Course = "COMP3111";
		Grade = "4";
		Comment = "上載日期";

		System.out.println(x.comment(Course, Grade, Comment));// if 100 then ok
		
		System.out.println("Send a friend request to B");
		friendB = "testaab";
		System.out.println(x.friendReq(friendB));// if 100 then ok

		System.out.println("All the following does not need to login, should return appropriate value");
		System.out.println("Get a comment");
		x.printArray(x.getCourse(Course));// get course comments(2d array)

		Course = "COMP";
		System.out.println("Get summary for all course in one department: " + Course);
		x.printArray(x.getSummary(Course));// show all comp course with their
												// avg ratings
		Course = "COMP3111";
		System.out.println("Get avg rating in one course: " + Course);
		x.printArray(x.getSummary(Course));// show one comp course with its
												// avg rating


	}

}
