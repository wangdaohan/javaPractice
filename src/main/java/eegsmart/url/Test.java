package eegsmart.url;

import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by patrick on 2016/3/31.
 */
public class Test {

    public static void main(String[] args)throws Exception{
        String urlStr  = "http://7xp15a.com2.z0.glb.qiniucdn.com/meditation%2FInto the blue, out of the blue.mp3";
        String en1= URLDecoder.decode(urlStr,"utf8");
        URL url = new URL(en1);
        URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        url = uri.toURL();
        System.out.println(url);
    }
}
