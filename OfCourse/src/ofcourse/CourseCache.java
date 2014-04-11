package ofcourse;

import java.io.File;
import java.util.Date;
import java.util.Hashtable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jsoup.nodes.Document;

public class CourseCache {
	private Hashtable<String, Long> lastUpdateTime = new Hashtable<String, Long>();
	public final String mainURL = "https://w5.ab.ust.hk/wcq/cgi-bin/1330/subject/";
	private final File historyFile = new File("history.txt");

	private final ReentrantReadWriteLock historyLock = new ReentrantReadWriteLock();
	private final Hashtable<String, ReentrantReadWriteLock> cacheLocks = new Hashtable<String, ReentrantReadWriteLock>();
	
	//Fast delivered, acquire reader lock on cache if cache is used, connect and return quota page if need update
	public Document getDocument(String url) {
		return null;
	}
	
	private ReentrantReadWriteLock getCacheLock(String key) {
		if (cacheLocks.containsKey(key)) {
			return cacheLocks.get(key);
		}
		else {
			ReentrantReadWriteLock newLock = new ReentrantReadWriteLock();
			cacheLocks.put(key, newLock);
			return newLock;
		}
	}
	
	//Slow written, acquire writer lock on specific cache file
	private void updateCache(String key, Document doc) {
		ReentrantReadWriteLock.WriteLock readerLock = getCacheLock(key).writeLock();
		
		
	}
	//acquire reader lock on history file
	public void readHistory() {
		
	}
	
	//Slow written, acquire lock on history file
	private void writeHistory(String key, Date lastUpdateTime) {
		synchronized(historyFile) {
			
		}
	}
}
