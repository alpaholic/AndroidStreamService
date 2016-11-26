package org.baseballsrv.baseballpub;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.red5pro.streaming.R5Connection;
import com.red5pro.streaming.R5Stream;
import com.red5pro.streaming.R5StreamProtocol;
import com.red5pro.streaming.config.R5Configuration;
import com.red5pro.streaming.source.R5Camera;
import com.red5pro.streaming.source.R5Microphone;
import com.red5pro.streaming.view.R5VideoView;

/**
 * Created by mindol on 2016. 11. 6..
 */
public class Publish extends Base {

    public Publish() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publish, container, false);

        Resources res = getResources();

        R5Configuration config = new R5Configuration(R5StreamProtocol.RTSP,res.getString(R.string.domain), res.getInteger(R.integer.port), res.getString(R.string.context), 0.5f);
        R5Connection connection = new R5Connection(config);

        publish = new R5Stream(connection);

        publish.setLogLevel(R5Stream.LOG_LEVEL_DEBUG);

        cam = openFrontFacingCameraGingerbread();
        cam.setDisplayOrientation((cameraOrientation + 180)%360);

        R5Camera camera  = new R5Camera(cam, 640, 360);
        camera.setBitrate(res.getInteger(R.integer.bitrate));
        camera.setOrientation(cameraOrientation);

        R5Microphone mic = new R5Microphone();

        publish.attachMic(mic);

        R5VideoView r5VideoView =(R5VideoView) view.findViewById(R.id.video);
        r5VideoView.attachStream(publish);

        publish.attachCamera(camera);
        publish.publish(Base.camId, R5Stream.RecordType.Live);
        cam.startPreview();

        return view;
    }

}
