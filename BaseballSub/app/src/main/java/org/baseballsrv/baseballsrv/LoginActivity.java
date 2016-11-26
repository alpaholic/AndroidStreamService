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

public class LoginActivity extends AppCompatActivity {

    EditText userLoginId;
    EditText userLoginPw;
    int success;
    String userName = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLoginButtonClicked(View v) {
        userLoginId = (EditText) findViewById(R.id.userLoginId);
        userLoginPw = (EditText) findViewById(R.id.userLoginPw);

        if (userLoginId.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "아이디를 입력하세요.", Toast.LENGTH_LONG).show();
            return;
        }
        if (userLoginPw.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "패스워드를 입력하세요.", Toast.LENGTH_LONG).show();
            return;
        }

        Thread thread = new Thread() {
            @Override
            public void run() {
                String url="http://"+Constant.host+":8080/login";
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
                    data.put("id", userLoginId.getText().toString());
                    data.put("pw", userLoginPw.getText().toString());

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
                        userName = jsonObj.get("name").toString();
                        System.out.println(userName);
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
            Intent intent;
            intent = new Intent(getApplicationContext(), ViewActivity.class);
            //System.out.println(userName);
            intent.putExtra("name", userName);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_LONG).show();
        }
    }

}
