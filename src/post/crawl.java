package post;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xml.internal.utils.StringToStringTable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

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
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
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
        //发送POST请求
        Scanner s = new Scanner(System.in);
        String word = s.nextLine();
        String param = "i=" + word + "&from=AUTO&to=AUTO&martresult=dict&client=fanyideskweb&salt=15616147260797&sign=2fcf94806d5b42374843bead371bf289&ts=1561614726079&bv=e2a78ed30c66e16a857c5b6486a1d326&doctype=json&version= 2.1&keyfrom=fanyi.web&action=FY_BY_REALTlME";
        String s1 = crawl.sendPost("http://fanyi.youdao.com/translate?smartresult=dict&smartresult=rule", param);
        //{"type":"EN2ZH_CN","errorCode":0,"elapsedTime":1,"translateResult":[[{"src":"fuck","tgt":"操"}]]}
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
}
