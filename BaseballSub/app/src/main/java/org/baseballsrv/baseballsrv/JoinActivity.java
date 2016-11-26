package org.baseballsrv.baseballsrv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class JoinActivity extends AppCompatActivity {

    EditText userId;
    EditText userPw;
    EditText userName;
    EditText userGender;
    EditText userAge;
    int success = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
    }

    public void onIdCheckButtonClicked(View v) {
        userId = (EditText) findViewById(R.id.userLoginId);

        if (userId.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "아이디를 입력하세요.", Toast.LENGTH_LONG).show();
            return;
        }

        Thread thread = new Thread() {
            @Override
            public void run() {
                String url="http://"+Constant.host+":8080/idCheck";
                try {
                    URL object=new URL(url);

                    HttpURLConnection con = (HttpURLConnection) object.openConnection();

                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "*/*");
                    con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
                    con.setRequestMethod("POST");

                    JSONObject data = new JSONObject();
                    data.put("id", userId.getText().toString());

                    OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());

                    wr.write(data.toString());
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
                        success = Integer.parseInt(jsonObj.get("success").toString());
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

        if (success == 0) {
            Toast.makeText(getApplicationContext(), "중복되는 아이디가 존재합니다.", Toast.LENGTH_LONG).show();
            userId.setText("");
        }

    }

    public void onRequestButtonClicked(View v) {
        userId = (EditText) findViewById(R.id.userLoginId);
        userPw = (EditText) findViewById(R.id.userLoginPw);
        userName = (EditText) findViewById(R.id.userName);
        userAge = (EditText) findViewById(R.id.userAge);

        if (userId.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "아이디를 입력하세요.", Toast.LENGTH_LONG).show();
            return;
        }

        if (userPw.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "패스워드를 입력하세요.", Toast.LENGTH_LONG).show();
            return;
        }

        if (userName.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "이름을 입력하세요.", Toast.LENGTH_LONG).show();
            return;
        }

        if (userAge.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "나이를 입력하세요.", Toast.LENGTH_LONG).show();
            return;
        }

        Thread thread = new Thread() {
            @Override
            public void run() {
                String url="http://"+Constant.host+":8080/join";
                try {
                    URL object=new URL(url);

                    HttpURLConnection con = (HttpURLConnection) object.openConnection();

                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "*/*");
                    con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
                    con.setRequestMethod("POST");

                    JSONObject data = new JSONObject();
                    data.put("id", userId.getText().toString());
                    data.put("pw", userPw.getText().toString());
                    data.put("name", userName.getText().toString());
                    data.put("age", userAge.getText().toString());

                    OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());

                    wr.write(data.toString());
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
                        success = Integer.parseInt(jsonObj.get("success").toString());
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

        if (success == 1) {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        } else {
            Toast.makeText(getApplicationContext(), "다시 시도하시길 바랍니다 ", Toast.LENGTH_LONG).show();
        }
    }

}
