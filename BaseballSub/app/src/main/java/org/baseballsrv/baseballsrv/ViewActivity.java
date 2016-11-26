package org.baseballsrv.baseballsrv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {

    public ArrayList<String> idList = new ArrayList<String> ();
    public ArrayList<String> defList = new ArrayList<String> ();
    public ArrayList<Button> btnList = new ArrayList<Button> ();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        TextView welcomeMsg = (TextView) findViewById(R.id.welcomeMsg);
        welcomeMsg.setText(name + "님, 안녕하세요! ");
        getCameraList();
    }

    public void onReloadBtnClicked(View v) {
        System.out.println("RELOAD!!!!!!!!!!!!!!!!!");
        getCameraList();
    }

    public void initialize() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.viewLinearLayout);

        if (btnList.size() > 0) {
            for (int i=0; i<btnList.size(); i++) {
                Button btn = (Button) findViewById(i);
                layout.removeView(btn);
            }
        }
        btnList = new ArrayList<Button> ();
        idList = new ArrayList<String> ();
        defList = new ArrayList<String> ();
    }

    public void getCameraList() {
        initialize();
        LinearLayout layout = (LinearLayout) findViewById(R.id.viewLinearLayout);

        Thread thread = new Thread() {
            @Override
            public void run() {
                String url="http://"+Constant.host+":8080/getCamList";
                try {
                    URL object=new URL(url);

                    HttpURLConnection con = (HttpURLConnection) object.openConnection();

                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "*/*");
                    con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
                    con.setRequestMethod("POST");

                    OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());
                    wr.flush();

                    //display what returns the POST request
                    StringBuilder sb = new StringBuilder();

                    int HttpResult = con.getResponseCode();
                    if(HttpResult == HttpURLConnection.HTTP_OK){

                        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));
                        String line = null;

                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }

                        br.close();
                        JSONObject jsonObj = new JSONObject(sb.toString());
                        JSONArray list = jsonObj.getJSONArray("list");

                        for (int i = 0; i < list.length(); i++) {
                            JSONObject c = list.getJSONObject(i);
                            idList.add(c.getString("ID"));
                            defList.add(c.getString("DEF"));
                        }

                    }else{
                        //System.out.println(con.getResponseMessage());
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {

                }
            }
        };


        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }


        for (int i=0; i<idList.size(); i++) {

            Button btn = new Button(this);
            btn.setText(defList.get(i));
            btn.setWidth(500);
            btn.setHeight(130);
            btn.setTextSize(20);
            btn.setId(i);
            btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent;
                    intent = new Intent(getApplicationContext(), R5SubscribeActivity.class);
                    intent.putExtra("stream", idList.get(v.getId()));
                    startActivity(intent);
                }
            });
            btnList.add(btn);
            layout.addView(btn);
        }
    }
}
