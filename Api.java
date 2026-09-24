package com.example.s5gpt;

import java.io.*;import java.net.*;

public final class Api {
 private Api() {}
 public static final String RELAY_URL="https://YOUR-SERVER.example/chat";
 public static String send(String message,String imageDataUrl)throws Exception{
  HttpURLConnection c=(HttpURLConnection)new URL(RELAY_URL).openConnection(); c.setConnectTimeout(15000);c.setReadTimeout(90000);c.setRequestMethod("POST");c.setDoOutput(true);c.setRequestProperty("Content-Type","application/json; charset=UTF-8");
  String json="{\"message\":\""+esc(message)+"\",\"image\":"+(imageDataUrl==null?"null":"\""+esc(imageDataUrl)+"\"")+"}";
  byte[] b=json.getBytes("UTF-8");c.setFixedLengthStreamingMode(b.length);OutputStream o=c.getOutputStream();o.write(b);o.close();int code=c.getResponseCode();InputStream in=code>=200&&code<300?c.getInputStream():c.getErrorStream();BufferedReader r=new BufferedReader(new InputStreamReader(in,"UTF-8"));StringBuilder s=new StringBuilder();String line;while((line=r.readLine())!=null)s.append(line);r.close();c.disconnect();if(code<200||code>=300)throw new Exception("HTTP "+code+": "+s);return extract(s.toString(),"reply");
 }
 static String esc(String s){return s.replace("\\","\\\\").replace("\"","\\\"").replace("\r","\\r").replace("\n","\\n");}
 static String extract(String j,String key){String k="\""+key+"\"",p=j.indexOf(k);if(p<0)return j;int col=j.indexOf(':',p+k.length());int q=j.indexOf('"',col+1);if(q<0)return j;StringBuilder o=new StringBuilder();boolean e=false;for(int i=q+1;i<j.length();i++){char ch=j.charAt(i);if(e){if(ch=='n')o.append('\n');else if(ch=='r')o.append('\r');else if(ch=='t')o.append('\t');else o.append(ch);e=false;}else if(ch=='\\')e=true;else if(ch=='"')break;else o.append(ch);}return o.toString();}
}
