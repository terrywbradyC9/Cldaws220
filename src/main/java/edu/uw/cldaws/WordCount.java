package edu.uw.cldaws;

import java.io.IOException;

import com.google.gson.Gson;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WordCount {

    private WordCountParser wcParser = new WordCountParser();
    private WordCountCache wcCache = new WordCountCache();
    private WordCountQueue wcQueue = new WordCountQueue();
    public static void main(String[] args) {
        WordCount wc = new WordCount();
        try {
            while(true) {
                wc.processQueue();
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public WordCount() {
    }
   
    public String checkUrl(String url) throws IOException {
        String result = wcCache.checkCacheVal(url);
        
        if (result == null) {
            if (wcQueue.queueRequest(url)) {
                return returnJsonMessage("Not in cache. Request queued. Try again later.");
            } else {
                return returnJsonMessage("Not in cache. Request could not be queued.");
            }
        }
        return result;
    }

    public long reportStatus(long start, long lastReport) {
        long now = new Date().getTime();
        if ((now - lastReport) > 60_000) {
            long min = (now - start)/60_000;
            System.out.println(String.format("System has run for %d min", min));
        } else {
            System.out.println(now);
        }
        return now;
    }
    
    public void processQueue() throws IOException {
        List<WordCountMessage> mlist = wcQueue.getMessage();
        long start = new Date().getTime();
        long lastReport = start;
        if (mlist.isEmpty()) {
            lastReport = reportStatus(start, lastReport);
        } else {
            WordCountMessage m = mlist.get(0);
            String url  = m.getUrl();
            String result = wcCache.checkCacheVal(url);
            if (result == null) {
                System.out.println("Processing " + url);
                result = wcParser.getCountAsJson(url);
                wcCache.putCacheVal(url, result);
            } else {
                System.out.println("Already processed" + url);
            }
            wcQueue.removeMessage(m);
        }
    }
   
    public static String returnJsonMessage(String s) {
        HashMap<String,String> map = new HashMap<>();
        map.put("message", s);
        return new Gson().toJson(map);
    }
    
}
