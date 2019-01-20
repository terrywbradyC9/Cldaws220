package edu.uw.cldaws;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;
import java.util.Map;

public class WordCount {

    private static int MAX = 10_000;
    private static String URL = "url";
    private static String RESULT = "result";
    private static String CACHE="WordCountCache";
    
    public static void main(String[] args) {
        WordCount wc = new WordCount();
        //wc.setString("The quick brown fox jumped over the lazy dogs.  That silly fox.");
        //wc.report(5);
        try {
            System.out.println(wc.doReport("http://www.textfiles.com/etext/FICTION/warpeace.txt", 20));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public WordCount() {
        setString("The quick brown fox jumped over the lazy dogs.  That silly fox.");
    }

    public void setUrl(String s) throws IOException {
        if (s == null) {
            System.err.println("Url not found");
            setString("");
            return;
        }
        try {
            URL url = new URL(s);
            try(InputStream is = url.openStream()) {
                try(Scanner scan = new Scanner(is)){
                    parseVals(scan);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            setString("");
            return;
        }
    }

    //Week 1 - allow count param, disregard as we test caching
    public String doReport(String url, int n) throws IOException {
        setUrl(url);
        int count = n > MAX ? MAX: n;
        Collection<String> results = getTopKeys(count);
        return new Gson().toJson(results);
    }

    
    private String checkCacheVal(String url) {
        String result;
        HashMap<String,AttributeValue> key_to_get = new HashMap<>();
        
        key_to_get.put(URL, AttributeValue.builder().s(url).build());

        GetItemRequest request = GetItemRequest.builder()
            .tableName(CACHE)
            .key(key_to_get)
            .build();

        try(DynamoDbClient ddb = DynamoDbClient.create()) {
            Map<String,AttributeValue> returned_item = ddb.getItem(request).item();
            result = returned_item.containsKey(RESULT) ? returned_item.get(RESULT).s() : null;
        }
        return result;
    }

    private void putCacheVal(String url, String result) {
        HashMap<String,AttributeValue> map = new HashMap<>();
        map.put(URL, AttributeValue.builder().s(url).build());
        map.put(RESULT, AttributeValue.builder().s(result).build());
        
        PutItemRequest req = PutItemRequest.builder()
            .tableName(CACHE)
            .item(map)
            .build();
        try(DynamoDbClient ddb = DynamoDbClient.create()) {
            ddb.putItem(req);
        }
    }
    
    public String doCacheReport(String url) throws IOException {
        String result = checkCacheVal(url);
        if (result == null) {
            result = doReport(url, 10); 
            putCacheVal(url, result);
        }
        return result;
    }

    
    public void report(int n) {
        for(String s: getTopKeys(n)) {
            System.out.println(String.format("[%-20s%6d]", s, map.get(s)));
        }
        System.out.println();
        
        Gson gson = new Gson();
        System.out.println(gson.toJson(getTopKeys(n)));
        System.out.println();
    }
    
    public void setString(String s) {
        try(Scanner scan = new Scanner(s)){
            parseVals(scan);
        }
    }

    private void parseVals(Scanner scan) {
        map.clear();
        scan.useDelimiter(Pattern.compile("[^\\w']"));
        while(scan.hasNext()) {
            String key = scan.next().toLowerCase();
            if (key.isEmpty()) {
                continue;
            }
            Integer val = map.containsKey(key) ? map.get(key) + 1: 1;
            map.put(key, val);
        }        
    }
    
    public Collection<String> getTopKeys(int n) {
        return map.keySet()
            .stream()
            .sorted((o1, o2)->map.get(o2).compareTo(map.get(o1)))
            .limit(n)
            .collect(Collectors.toList());
    }
    
    
    private TreeMap<String,Integer> map = new TreeMap<>();
    
    
}
