package org.baseballsrv.baseballpub;

import android.content.res.Resources;
import android.hardware.Camera;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Surface;

import com.red5pro.streaming.R5Connection;
import com.red5pro.streaming.R5Stream;
import com.red5pro.streaming.R5StreamProtocol;
import com.red5pro.streaming.config.R5Configuration;
import com.red5pro.streaming.source.R5Camera;
import com.red5pro.streaming.source.R5Microphone;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mindol on 2016. 11. 6..
 */
public class Base extends Fragment {
    protected R5Stream publish;
    protected int cameraOrientation;
    protected Camera cam;
    public static String camId;
    public static String camDef;
    int success;

    public Base(){}

    protected R5Stream getNewStream(int type){
        Resources res = getResources();

        R5Configuration config = new R5Configuration(R5StreamProtocol.RTSP,res.getString(R.string.domain), res.getInteger(R.integer.port), res.getString(R.string.context), 0.5f);
        R5Connection connection = new R5Connection(config);

        R5Stream stream = new R5Stream(connection);
        stream.setLogLevel(R5Stream.LOG_LEVEL_DEBUG);

        if(type == 1) { //publishing

            cam = openFrontFacingCameraGingerbread();
            cam.setDisplayOrientation((cameraOrientation + 180)%360);

            R5Camera camera  = new R5Camera(cam, 320, 240);
            camera.setBitrate(res.getInteger(R.integer.bitrate));
            camera.setOrientation(cameraOrientation);
            R5Microphone mic = new R5Microphone();

            stream.attachMic(mic);
            stream.attachCamera(camera);
        }

        return stream;

    }

    protected Camera openFrontFacingCameraGingerbread() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                    cameraOrientation = cameraInfo.orientation;
                    applyDeviceRotation();
                } catch (RuntimeException e) {
                    Log.e("R5 Test Activity", "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }

        return cam;
    }

    protected void applyDeviceRotation(){
        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        cameraOrientation += degrees;

        cameraOrientation = cameraOrientation%360;
    }


    @Override
    public void onStop(){

        super.onStop();

        if(publish != null)
            publish.stop();

        publish = null;

        if(cam != null) {
            cam.stopPreview();
            cam.release();
            System.out.println("END!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

            // 카메라 데이터 제거
            Thread thread = new Thread() {
                @Override
                public void run() {
                    Resources res = getResources();
                    String url="http://"+res.getString(R.string.domain)+":8080/deleteCam";
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
        }

    }
}
