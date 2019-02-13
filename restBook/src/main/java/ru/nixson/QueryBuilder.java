package ru.nixson;

import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Iterator;

public class QueryBuilder {
    public static HashMap<String, String> parse(String uri){
        UriComponentsBuilder b = UriComponentsBuilder.newInstance();
        MultiValueMap<String, String> parameters = b.query(uri).build().getQueryParams();
        HashMap<String, String> map = new HashMap<String, String>();
        Iterator<String> iter = parameters.keySet().iterator();
        while(iter.hasNext()){
            String key = iter.next();
            map.put(key,parameters.get(key).get(0));
        }
        return map;
    }
}
