package ofcourse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class DataCore {
	private boolean onlineMode = false;
	private OfflineDataManager offlineDataManager = new OfflineDataManager();
	private OnlineDataManager onlineDataManager = new OnlineDataManager();
	
	public IDataManager getDataManager() {
		if (onlineMode) return onlineDataManager;
		else return offlineDataManager;
	}
	
	public DataCore() { // empty constructor
		
	}
	
	public DataCore(String username, String password) { // Constructor, directly online
		changeToOnline(username, password);
	}
	
	public boolean changeToOnline(String username, String password) {
		//test if name is used by registering with this username 
		String r = onlineDataManager.register(username, true); //TODO: DEBUGGING
		if (r == "100") {
			//TODO: name is not used, email has been sent
			return false;
		}
		else if (r == "003") {
			//TODO: email has NOT been sent due to email error
			return false;
		}
		else if (r == "001") {
			//name is used, try setting password to the same password
			return false;
		}
		else {
			return false;
		}
		
	}
}
