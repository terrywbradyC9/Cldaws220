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

public class WordCount {

    public static void main(String[] args) {
        WordCount wc = new WordCount();
        wc.setString("The quick brown fox jumped over the lazy dogs.  That silly fox.");
        wc.report(5);
        try {
            System.out.println(wc.doReport("http://www.textfiles.com/etext/FICTION/warpeace.txt", 10));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public WordCount() {
        setString("The quick brown fox jumped over the lazy dogs.  That silly fox.");
    }

    public void setUrl(String s) throws IOException {
        URL url = new URL(s);
        try(InputStream is = url.openStream()) {
            try(Scanner scan = new Scanner(is)){
                parseVals(scan);
            }
        }
    }
    
    public String doReport(String url, int n) throws IOException {
        setUrl(url);
        Collection<String> results = getTopKeys(n);
        return new Gson().toJson(results);
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
        scan.useDelimiter(Pattern.compile("\\W"));
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
