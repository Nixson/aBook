package ru.nixson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Config {

    private static final String CFG_EXT = ".properties";

    public static final String AK_DB_URL = "dburl";
    public static final String AK_DB_FORNAME = "forName";

    public static final String AK_DB_USER = "dbuser";
    public static final String AK_DB_PASSWORD = "dbpassword";
    public static final String AK_PBWS_URL = "pbwsurl";

    private Map<String, String> properties = new HashMap<String, String>();

    private Config(){

    }


    //for tomcat config-file must be located @ /bin directory
    public static Config getConfig(String configName) {
        Config cfg = new Config();
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader("./" + configName + CFG_EXT);
            br = new BufferedReader(fr);
            String ln = null;
            while ((ln = br.readLine()) != null) {
                int splitterIdx = ln.indexOf("=");
                String key = ln.substring(0, splitterIdx);
                String value = ln.substring(splitterIdx + 1, ln.length());
                cfg.properties.put(key, value);
            }
        } catch (Exception x){
            System.err.println("Error while read config [" + configName + "]");
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch(Exception xx){};
            }
            if(fr != null) {
                try {
                    fr.close();
                } catch(Exception xx){};
            }
        }

        System.out.println("Read config [" + configName + "]\n" + "values = " + cfg.properties);
        return cfg;
    }

    public String getParam(String paramName){
        String val = properties.get(paramName);
        return val == null ? "" : val;
    }
}
