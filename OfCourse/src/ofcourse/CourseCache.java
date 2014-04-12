package ofcourse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CourseCache {
	private Hashtable<String, Long> lastUpdateTime = new Hashtable<String, Long>();
	public static final String mainURL = "https://w5.ab.ust.hk/wcq/cgi-bin/1330/subject/";
	private final File historyFile = new File("CACHEHISTORY.txt");

	private final ReentrantReadWriteLock historyLock = new ReentrantReadWriteLock();
	private final ReentrantReadWriteLock historyFileLock = new ReentrantReadWriteLock();
	private final Hashtable<String, ReentrantReadWriteLock> cacheLocks = new Hashtable<String, ReentrantReadWriteLock>();
	
	private long expireTime = 10 * 60 * 1000;
	
	public void setExpireTime(long milliS) {
		if (milliS > 0)expireTime = milliS;
	}
	
	public long getExpireTime() {
		return expireTime;
	}
	
	public CourseCache() {
		String historyFileContent = "";
		try {
			historyFileLock.readLock().lock();
			boolean exist = historyFile.exists();
			historyFileLock.readLock().unlock();
			if (exist) {
				historyFileLock.readLock().lock();
				java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(historyFile));
				historyFileContent = br.readLine();
				br.close();
				historyFileLock.readLock().unlock();
			}
			else {
				historyFileLock.writeLock().lock();
				historyFile.createNewFile();
				historyFileLock.writeLock().unlock();
			}
		
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			int start = 0, middle = -1, end = -1;
			while(historyFileContent != null && start < historyFileContent.length()) {
				if (historyFileContent != null && historyFileContent.length() > 0) {
					Date d1 = null;
					
					middle = historyFileContent.indexOf("!", start);
					end = historyFileContent.indexOf("", middle);
					if (middle == -1) //no subject can be find in history
						return;
					if (end == -1) { //parse error
						System.out.println("parse error");
						throw new IOException();
					}
					String subject = historyFileContent.substring(start, middle);
					//in milliseconds 
					d1 = new Date(Long.parseLong(historyFileContent.substring(middle + 1, end)));
					lastUpdateTime.put(subject, d1.getTime());
					start = end + 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	//Fast delivered, acquire reader lock on cache if cache is used, connect and return quota page if need update
	//THIS METHID SHOULD BE THREAD-SAFE.
	public Document getDocument(String subject) {
		Document doc = null;
		boolean updateNeeded = checkHistory(subject);
		
		if (updateNeeded){
			//Get, save in cache
			System.out.println(subject + ": Update is needed.");
			//Get
			doc = tryGetFromInternet(subject);
			//save in cache
			updateCache(subject, doc);
			//Update last update time
			updateHistory(subject, new Date().getTime());
		}
		else {
			//Read cache
			System.out.println(subject + ": Cache is used.");
			doc = tryGetFromCache(subject);
		}
		
		return doc;
	}
	
	private Document tryGetFromInternet(String subject) {
		Document doc = null;
		try {
			doc = Jsoup.connect(mainURL + subject).get();
		} catch (IOException e){
			System.out.println(subject + ": Page cannot be obtained. Cached is used.");
			try {
				doc = readCache(subject);
			} catch (IOException e1) {
				System.out.println(subject + ": Cached cannort be read or opened. This subject will not be shown and is unusable.");
			}
		}
		return doc;
	}
	
	private Document tryGetFromCache(String subject) {
		Document doc = null;
		try {
			doc = readCache(subject);
		} catch (IOException e){
			System.out.println(subject + ": Cached cannort be read or opened. Update is needed.");
			try {
				doc = Jsoup.connect(mainURL + subject).get();
			} catch (IOException e1) {
				System.out.println(subject + ": Page cannot be obtained. This subject will not be shown and is unusable.");
			}
		}
		return doc;
	}
	
	//acquire reader lock on history file
	private boolean checkHistory(String subject) {
		long now = new Date().getTime();
		historyLock.readLock().lock();
		Long past = lastUpdateTime.get(subject);
		historyLock.readLock().unlock();
		if(past == null) return true;
		if(now - past > expireTime) return true;
		if (!new File(subject).exists()) return true;
		return false;
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
	
	//Slow written, acquire writer lock on specific cache file, fork a new thread
	private void updateCache(String key, Document doc) {
		//TODO: check null
		Thread t = new Thread(new CacheWriterRunner(key, doc));
		t.start();
	}
	
	private class CacheWriterRunner implements Runnable {
		private String key = "";
		private Document doc = null;
		public CacheWriterRunner(String key, Document doc){
			this.key = key;
			this.doc = doc;
		}
		@Override
		public void run() {
			ReentrantReadWriteLock.WriteLock writerLock = getCacheLock(key).writeLock();
			writerLock.lock();
			java.io.PrintWriter writer = null;
			try {
				writer = new java.io.PrintWriter(new File(key));
			} catch (FileNotFoundException e) {
				//TODO: Use another type of exception
				System.out.println(key + ": Cache cannot be updated due to File error.");
				//throw new RuntimeException(e.getMessage() == null ? "" : e.getMessage());
			}
			if(writer != null) {
				writer.print(doc.outerHtml());
				writer.close();
			}
			writerLock.unlock();
			
		}
	}

	private Document readCache(String key) throws IOException {
		Document doc = null;
		ReentrantReadWriteLock.ReadLock lock = getCacheLock(key).readLock();
		lock.lock();
		String s = new Scanner(new File(key)).useDelimiter("\\Z").next();
		lock.unlock();
		doc = Jsoup.parse(s, mainURL);
		return doc;
	}
	
	//update last update time in instance array first, then fork a new thread to do IO work
	private void updateHistory(String subject, long time) {
		historyLock.writeLock().lock();
		lastUpdateTime.put(subject, time);
		historyLock.writeLock().unlock();
		Thread t = new Thread(new HistoryWriterRunner());
		t.start();
	}
	private class HistoryWriterRunner implements Runnable {
		@Override
		public void run() {
			String fileContent = "";
			for(Entry<String, Long> e : lastUpdateTime.entrySet()) {
				fileContent += e.getKey() + "!" + e.getValue() + "";
			}
			historyFileLock.writeLock().lock();
			PrintWriter pw;
			try {
				pw = new PrintWriter(historyFile);
			} catch (FileNotFoundException e) {
				//TODO: Use another type of exception
				throw new RuntimeException(e.getMessage() == null ? "" : e.getMessage());
			}
			pw.write(fileContent);
			pw.close();
			historyFileLock.writeLock().unlock();
		}
	}
	
}
