package post;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Hex;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class crawl {

    public static String sendPost(String url, String param){
        String result = "";
        try{
            URL realUrl = new URL(url);
            //打开和URL之间的连接
            URLConnection conn =  realUrl.openConnection();
            //设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.84 Safari/535.11 SE 2.X MetaSr 1.0");
            //发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //获取URLConnection对象对应的输出流
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            //发送请求参数
            out.print(param);
            //flush输出流的缓冲
            out.flush();
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result += "\n" + line;
            }
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常" + e);
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        newOne();


        //todo: detail for the translate
    }

    public void oldOne(){
        //发送POST请求
        Scanner s = new Scanner(System.in);
        String word = s.nextLine();
        String param = "i=" + word + "&from=AUTO&to=AUTO&martresult=dict&client=fanyideskweb&salt=15616147260797&sign=2fcf94806d5b42374843bead371bf289&ts=1561614726079&bv=e2a78ed30c66e16a857c5b6486a1d326&doctype=json&version= 2.1&keyfrom=fanyi.web&action=FY_BY_REALTlME";
        String s1 = "";//sendPost("http://fanyi.youdao.com/translate?smartresult=dict&smartresult=rule", param);
        //{"type":"EN2ZH_CN","errorCode":0,"elapsedTime":1,"translateResult":[[{"src":"fuck","tgt":"操"}]]}
        System.out.println(s1);
        JSONObject tempJB = JSONObject.parseObject(s1);
        String strTempJB = tempJB.getString("translateResult");

        JSONArray tempJB1 = JSONArray.parseArray(strTempJB);
        String strTempJB2 = tempJB1.getString(0);
        JSONArray tempJB2 = JSONArray.parseArray(strTempJB2);
        String strTempJB3 = tempJB2.getString(0);
        JSONObject finalJB = JSONObject.parseObject(strTempJB3);
        String strfinaljb = finalJB.getString("tgt");
        System.out.println(strfinaljb);
    }

    public static JSONArray newOne(){
        Scanner s = new Scanner(System.in);
        String word = s.nextLine();
        String salt;
        String sign;
        long tsLong = System.currentTimeMillis() / 1000;
        String ts = String.valueOf(tsLong);

        salt = String.valueOf(System.currentTimeMillis());
        sign = calcSign(salt, word,ts);

        Map<String, String> map = new HashMap<>();
        map.put("q", word);
        map.put("from", "AUTO");
        map.put("to","AUTO");
        map.put("appKey","1e7c07678491ecc9");
        map.put("signType","v3");
        map.put("salt", salt);
        map.put("sign",sign);
        map.put("curtime",ts);


        System.out.println(salt + " " + ts);
        System.out.println(calcSign(salt, word, ts));
        System.out.println("done");


        loopMap(map);

        String s1 = sendPost("https://openapi.youdao.com/api", loopMap(map));
        //{"type":"EN2ZH_CN","errorCode":0,"elapsedTime":1,"translateResult":[[{"src":"fuck","tgt":"操"}]]}


        JSONObject jsObj = JSONObject.parseObject(s1);
        String jsObjStr = jsObj.getString("basic");

        JSONObject jsObj_basic = JSONObject.parseObject(jsObjStr);
        String jsObjStr_basic = jsObj_basic.getString("explains");
        System.out.println(jsObjStr_basic);

        return JSONArray.parseArray(jsObjStr_basic);
        /*
        JSONArray jsArr_web = JSONArray.parseArray(jsObjStr);
        String jsObj_web = jsArr_web.get()

        JSONArray tempJB1 = JSONArray.parseArray(strTempJB);
        String strTempJB2 = tempJB1.getString(0);
        JSONArray tempJB2 = JSONArray.parseArray(strTempJB2);
        String strTempJB3 = tempJB2.getString(0);
        JSONObject finalJB = JSONObject.parseObject(strTempJB3);
        String strfinaljb = finalJB.getString("tgt");
        System.out.println(strfinaljb);

        */
    }

    public static String calcSign(String salt, String word, String ts) {
        //sign=sha256(应用ID+input+salt+curtime+应用密钥)
        final String ID = "1e7c07678491ecc9";
        final String key = "X0CcH79hQEjcxG0VCGl8ZoTnu2OjVmMI";
        String input = truncate(word);
        String data;

        data = ID + input + salt + ts + key;

        return String2SHA256(data);
    }

    public static String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        String result;
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }

    public static String loopMap(Map map){
        String param = "";
        Object[] arr = map.keySet().toArray();
        for (int i = 0; i < map.size(); i++) {
            //param += arr[i] + "=" + map.get(arr[i]) + "&";
            System.out.println((i == map.size() - 1));
            if ((i == map.size() - 1)){
                //System.out.println(map.size());
                param += arr[i] + "=" + map.get(arr[i]);
            } else {
                param += arr[i] + "=" + map.get(arr[i]) + "&";
            }

        }
        System.out.println(param);
        return param;
    }

    public static String String2SHA256(String str) {
        MessageDigest messageDigest;
        String encdeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes(StandardCharsets.UTF_8));
            encdeStr = Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encdeStr;
    }
}
