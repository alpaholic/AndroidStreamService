package org.baseballsrv.baseballsrv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.red5pro.streaming.R5Connection;
import com.red5pro.streaming.R5Stream;
import com.red5pro.streaming.R5StreamProtocol;
import com.red5pro.streaming.config.R5Configuration;
import com.red5pro.streaming.view.R5VideoView;

public class R5SubscribeActivity extends AppCompatActivity {

    public R5Configuration configuration;
    public R5Stream stream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        Intent intent = getIntent();
        String streamName = intent.getStringExtra("stream");

        configuration = new R5Configuration(R5StreamProtocol.RTSP, Constant.host,  8554, "live", 1.0f);
        R5Connection connection = new R5Connection(configuration);
        stream = new R5Stream(connection);
        stream.setLogLevel(R5Stream.LOG_LEVEL_DEBUG);

        R5VideoView r5VideoView = (R5VideoView) findViewById(R.id.video);
        r5VideoView.attachStream(stream);
        stream.play(streamName);
    }

}
