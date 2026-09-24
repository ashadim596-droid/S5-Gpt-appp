package com.example.s5gpt;

import android.app.*;import android.os.*;import android.content.*;import android.graphics.*;import android.graphics.drawable.GradientDrawable;import android.net.Uri;import android.provider.MediaStore;import android.view.*;import android.widget.*;import java.io.*;import java.util.*;

public class MainActivity extends Activity {
 static final int PICK=10; LinearLayout list; EditText input; Button send,attach; SharedPreferences db; Uri picked; ImageView preview;
 int bg=Color.rgb(16,17,20), bubble=Color.rgb(34,36,42), accent=Color.rgb(138,180,248);
 @Override public void onCreate(Bundle b){super.onCreate(b);db=getSharedPreferences("history",0);build();loadHistory();}
 TextView tv(String s,int size){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(Color.rgb(242,243,245));t.setPadding(16,12,16,12);return t;}
 void build(){
  LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setBackgroundColor(bg);
  TextView head=tv("S5GPT  •  Android 6",20);head.setGravity(Gravity.CENTER_VERTICAL);head.setTypeface(null,1);root.addView(head,new LinearLayout.LayoutParams(-1,60));
  ScrollView sv=new ScrollView(this);list=new LinearLayout(this);list.setOrientation(LinearLayout.VERTICAL);list.setPadding(8,8,8,8);sv.addView(list);root.addView(sv,new LinearLayout.LayoutParams(-1,0,1));
  LinearLayout bar=new LinearLayout(this);bar.setPadding(8,6,8,8);attach=new Button(this);attach.setText("+");input=new EditText(this);input.setHint("پیامت رو بنویس...");input.setTextColor(Color.WHITE);input.setHintTextColor(Color.GRAY);input.setGravity(Gravity.TOP);input.setMinLines(1);input.setMaxLines(5);send=new Button(this);send.setText("➤");bar.addView(attach,new LinearLayout.LayoutParams(52,58));bar.addView(input,new LinearLayout.LayoutParams(0,58,1));bar.addView(send,new LinearLayout.LayoutParams(58,58));root.addView(bar);setContentView(root);
  attach.setOnClickListener(v->pickImage());send.setOnClickListener(v->submit(sv));
 }
 void pickImage(){Intent i=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);startActivityForResult(i,PICK);}
 @Override protected void onActivityResult(int r,int c,Intent d){super.onActivityResult(r,c,d);if(r==PICK&&c==RESULT_OK&&d!=null){picked=d.getData();Toast.makeText(this,"عکس انتخاب شد ✓",Toast.LENGTH_SHORT).show();}}
 void submit(ScrollView sv){final String msg=input.getText().toString().trim();if(msg.length()==0&&picked==null)return;final Uri img=picked;input.setText("");picked=null;addBubble("شما",msg,true);if(img!=null)addBubble("📷 تصویر پیوست شد", "",true);send.setEnabled(false);new AsyncTask<Void,Void,String>(){Exception err;protected String doInBackground(Void...x){try{return Api.send(msg,img==null?null:imageData(img));}catch(Exception e){err=e;return null;}}protected void onPostExecute(String r){send.setEnabled(true);if(err!=null)Toast.makeText(MainActivity.this,"خطا: "+err.getMessage(),Toast.LENGTH_LONG).show();else {addBubble("GPT",r,false);save(msg,r);}}}.execute();}
 void addBubble(String who,String text,boolean me){TextView t=tv(who+"\n"+markdown(text),16);GradientDrawable g=new GradientDrawable();g.setColor(me?Color.rgb(29,52,48):bubble);g.setCornerRadius(22);t.setBackground(g);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.setMargins(4,6,4,6);list.addView(t,p);}
 String markdown(String s){if(s==null)return "";return s.replace("**","" ).replace("__","").replace("`","" );}
 void save(String u,String a){int n=db.getInt("n",0);db.edit().putInt("n",n+1).putString("u"+n,u).putString("a"+n,a).apply();}
 void loadHistory(){int n=db.getInt("n",0);for(int i=0;i<n;i++){addBubble("شما",db.getString("u"+i,""),true);addBubble("GPT",db.getString("a"+i,""),false);}}
 String imageData(Uri u)throws Exception{InputStream in=getContentResolver().openInputStream(u);Bitmap b=BitmapFactory.decodeStream(in);in.close();if(b==null)throw new IOException("تصویر خوانده نشد");int max=1280,w=b.getWidth(),h=b.getHeight();if(w>max||h>max){float f=Math.min((float)max/w,(float)max/h);b=Bitmap.createScaledBitmap(b,Math.round(w*f),Math.round(h*f),true);}ByteArrayOutputStream out=new ByteArrayOutputStream();b.compress(Bitmap.CompressFormat.JPEG,70,out);return "data:image/jpeg;base64,"+android.util.Base64.encodeToString(out.toByteArray(),android.util.Base64.NO_WRAP);}
}
