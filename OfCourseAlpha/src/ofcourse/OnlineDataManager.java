package ofcourse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

public class OnlineDataManager implements IDataManager {
	private String username = "";
	private String password = "";
	private static final String URL = "http://vtnetwork.synology.me/3111/";
	private static final String USER_AGENT = "Mozilla/5.0";
	
	private String[][] dataCut(String line, int b) { // use ASCII HEX 11-14 (DC1-DC4), don't tell me anyone is typing them.
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
	
	private String fromHexString(String hex) { // UTF-8 hex string to string
		ByteBuffer buff = ByteBuffer.allocate(hex.length() / 2);
		for (int i = 0; i < hex.length(); i += 2) {
			buff.put((byte) Integer.parseInt(hex.substring(i, i + 2), 16));
		}
		buff.rewind();
		Charset cs = Charset.forName("UTF-8"); // BBB
		CharBuffer cb = cs.decode(buff); // BBB
		return cb.toString();
	}
	
	private void printArray(String matrix[][]) { // Print 2d array
		for (int row = 0; row < matrix.length; row++) {
			for (int column = 0; column < matrix[row].length; column++) {
				System.out.print(matrix[row][column] + " ");
			}
			System.out.println();
		}
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
	
	public String changeFirstPassword(String ppw, String npw) { // Change Password FOR FIRST TIME, output 100 if ok
		npw = encryptPW(npw);
		String[][] newpwA = { { "ppw", ppw }, { "npw", npw } };
		String newpw = this.POST("newpw.php", newpwA);
		if (newpw.equals("100"))
			password = npw;
		return newpw;
	}

	public String changePassword(String opw, String npw) { // Change Password, output 100 if ok
		opw = encryptPW(opw);
		npw = encryptPW(npw);
		String[][] newpwA = { { "opw", opw }, { "npw", npw } };
		String newpw = this.POST("newpw.php", newpwA);
		if (newpw.equals("100"))
			password = npw;
		return newpw;
	}
	
	public String testFirstPassword(String a) {
		String[][] newpwA = { { "ppw", a }, { "npw", a } };
		String newpw = this.POST("newpw.php", newpwA);
		if (newpw.equals("100"))
			password = a;
		return newpw;
	}
	
	public String register(String username, boolean debug) { 
		// input name for register. Will send an email. Return 100 if ok.
		// if debug is true, no email sent(debug only)
		String[][] empty = { { "", "" } };
		this.username = username;
		if(debug) return (this.POST("insertB.php", empty));
		else return (this.POST("insert.php", empty));
	}
	
	
	@Override
	public Response retrieveReview(Ratable item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response sendReview(String review) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response retrieveFriendList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response sendFriendRequest(String friend) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response sendTimeTable(String timetable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response retrieveTimeTable(String friendName) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
