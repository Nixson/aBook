package ru.nixson;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "setting")
@RequestScoped
public class Front {
    private static Config conf;
    private String host;
    static {
        conf = Config.getConfig("connection-config");
    }

    public String getHost() {
        if(host==null){
            host = conf.getParam(Config.AK_REST_URL);
        }
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
