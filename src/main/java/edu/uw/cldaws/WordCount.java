package edu.uw.cldaws;

import java.io.IOException;

import com.google.gson.Gson;

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

    
    public void processQueue() throws IOException {
        List<WordCountMessage> mlist = wcQueue.getMessage();
        if (mlist.isEmpty()) {
            System.out.println("No message ");
        } else {
            WordCountMessage m = mlist.get(0);
            String url  = m.getUrl();
            System.out.println("Processing " + url);
            String result = wcParser.getCountAsJson(url);
            wcCache.putCacheVal(url, result);
            wcQueue.removeMessage(m);
        }
    }
   
    public String returnJsonMessage(String s) {
        HashMap<String,String> map = new HashMap<>();
        map.put("message", s);
        return new Gson().toJson(map);
    }
    
}
