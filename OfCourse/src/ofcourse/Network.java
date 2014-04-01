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
	String username;
	String password; //store user password, always encrypted
	private final String URL = "http://vtnetwork.synology.me/3111/";
	private final String USER_AGENT = "Mozilla/5.0";

	public Network(String username, String password) { // Constructor
		this.username = username;
		this.password = encryptPW(password);
	}

	public Network() { // empty constructor
		username = "";
		password = "";
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
		this.username = username;
		return this.POST("insert.php", empty);
	}

	public String registerB(String username) { // Same as above, but no email sent(debug only)
		String[][] empty = { { "", "" } };
		this.username = username;
		return this.POST("insertB.php", empty);
	}

	public String getMyFav() { // My Favorites as output
		String[][] empty = { { "", "" } };
		return this.POST("select.php", empty);
	}

	public String getFriendList() { // Get my friend list, output as friend1!friend2!...!
		String[][] empty = { { "", "" } };
		return this.POST("getfriend.php", empty);
	}

	public String getReqFriendList() { // Get ppl who have sent friend request to you, output as reqfriend1!reqfriend2!...!
		String[][] empty = { { "", "" } };
		return this.POST("getreqfriend.php", empty);
	}

	public String firstNewPW(String ppw, String npw) { // Change Password FOR FIRST TIME, output 100 if ok
		npw = encryptPW(npw);
		String[][] newpwA = { { "ppw", ppw }, { "npw", npw } };
		String newpw = this.POST("newpw.php", newpwA);
		if (newpw.equals("100"))
			password = npw;
		return newpw;
	}

	public String newPW(String opw, String npw) { // Change Password, output 100 if ok
		opw = encryptPW(opw);
		npw = encryptPW(npw);
		String[][] newpwA = { { "opw", opw }, { "npw", npw } };
		String newpw = this.POST("newpw.php", newpwA);
		if (newpw.equals("100"))
			password = npw;
		return newpw;
	}
	
	public Boolean chkFirstPW() {
		String[][] empty = { { "", "" } };
		if (this.POST("chkfpw.php", empty).equals("Y"))
			return true;// true means password has NOT changed (it is the first pw)
		return false;
	}

	public String comment(String Course, String Grade, String Comment) {// Leave a comment, output 100 if ok
		byte[] bb = Comment.getBytes();
		try {
			bb = Comment.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[][] commentA = { { "Course", Course }, { "Grade", Grade },
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
		String[][] reqfriendA = { { "Nameb", friendB } };
		return this.POST("reqfriend.php", reqfriendA);
	}

	public String friendSet(String friendB) { // Confirm a friend request
		String[][] setfriendA = { { "Nameb", friendB } };
		return this.POST("setfriend.php", setfriendA);
	}

	public String setMyFav(String MyFav) { // Change your MyFav and upload
		String[][] setfavA = { { "Myfav", MyFav } };
		return this.POST("setfav.php", setfavA);
	}

//	void SetPW(String p) { // Unused
//		password = encryptPW(p);
//	}

	private String convertToHex(byte[] data) { // byte to hash string(0-F)
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

	private String encryptPW(String pw) { // One way function for password
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
			password = a;
		return newpw;
	}
	
	public static void main(String[] args) {
		// 000 char error
		// 001 Name registered
		// 002 wrong Name/pw
		// 003 email not sent
		// 004 duplicate entry when insert 
		// 005 entry not exist when update
		// 100 ok
		// 404 Query return false: SQL server connection failed
		// TODO Auto-generated method stub

		System.out.println("New Network");
		Network x = new Network();
		String ITSC = "hlleeab";
		System.out.println("Registering " + ITSC);
		System.out.println(x.registerB(ITSC));// if 100 then ok (insertB is used
												// since I don't want to send
												// email)

		
		
		
		
		
		//ITSC = "xxx";
		//System.out.println("Another New Network " + ITSC + "pwd=aaa");
		Network a = new Network(ITSC, "aaa");
		
		System.out.print("Recieved Pasword: ");
		String s = new Scanner(System.in).nextLine();
		
		System.out.println("Test password");
		System.out.println(a.testPassword(s));// if 100 then ok
		
		
		System.out.println("Set pwd first time " + "pwd=aaa to pwd=aab" );
		System.out.println(a.firstNewPW(s, s + "6"));// if 100 then ok

		System.out.println("Set new pwd "+ "pwd=aab to pwd=aaa");
		System.out.println(a.newPW(s, "aaa"));// if 100 then ok


		
		
		
		
		System.out.println("Another New Network to login with new pwd");
		Network a2 = new Network(ITSC, "aaa");

		System.out.println("Set my favourite");
		String MyFav = "COMP3111";// Still not decided how to do this
		
		
		System.out.println(a2.setMyFav(MyFav));// if 100 then ok

		System.out.println("Get my favourite");
		System.out.println(a2.getMyFav());// myfav

		System.out.println("Get my firends");
		System.out.println(a2.getFriendList());// friend1!friend2!....!

		System.out.println("Get firend requests");
		System.out.println(a2.getReqFriendList());// reqfriend1!reqfriend2!....!

		System.out.println("Post a comment");
		String Course = "COMP6666";
		String Grade = "4";
		String Comment = "¤W¸ü¤é´Á";

		System.out.println(a2.comment(Course, Grade, Comment));// if 100 then ok


		System.out.println("Get a comment");
		a2.printArray(a2.getCourse(Course));// get course comments(2d array)

		Course = "ABCD";
		System.out.println("Get summary for all course in one department: " + Course);
		a2.printArray(a2.getSummary(Course));// show all comp course with their
												// avg rating

		System.out.println("Send a friend request to friendB");
		String friendB = "ylchungaa";
		System.out.println(a2.friendReq(friendB));// if 100 then ok


		System.out.println("Another Network, login as friendB");
		ITSC = "ylchungaa";
		Network b = new Network(ITSC, "bbb");

		System.out.println("Set friendB pwd for first time");
		System.out.println(b.firstNewPW("bbb", "bbb"));// if 100 then ok

		System.out.println("Get friendB s friend request");
		System.out.println(b.getReqFriendList());// reqfriend1!reqfriend2!....!

		System.out.println("Accept friend request");
		friendB = "thkong";
		System.out.println(b.friendSet(friendB));// if 100 then ok


		System.out.println("Get friend list");
		System.out.println(b.getFriendList());// friend1!friend2!....!

	}

}
