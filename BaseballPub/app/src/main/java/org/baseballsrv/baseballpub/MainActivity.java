package org.baseballsrv.baseballpub;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        onBackPressed();
    }

    public static class PlaceholderFragment extends Fragment {

        EditText camId;
        EditText camDef;
        int success;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            rootView.findViewById(R.id.Publish).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    camId = (EditText) getView().findViewById(R.id.camId);
                    camDef = (EditText) getView().findViewById(R.id.camDef);

                    if (camId.getText().toString().equals("")) {
                        Toast.makeText(getActivity().getApplicationContext(), "카메라 아이디를 입력하세요.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (camDef.getText().toString().equals("")) {
                        Toast.makeText(getActivity().getApplicationContext(), "카메라 설명을 입력하세요.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Base.camId = camId.getText().toString();
                    Base.camDef = camDef.getText().toString();

                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            Resources res = getResources();
                            String url="http://"+res.getString(R.string.domain)+":8080/setupCam";
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
                                data.put("id", Base.camId);
                                data.put("def", Base.camDef);

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
                        Toast.makeText(getActivity().getApplicationContext(), "[오류] 다시 시도하시길 바랍니다", Toast.LENGTH_LONG).show();
                        camId.setText("");
                        camDef.setText("");
                    } else {
                        transaction.replace(R.id.container, (Fragment) new Publish(), "publish_frag");
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                }
            });

            return rootView;
        }
    }
}
