package com.example.tiffanylee.gailprojectapp.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.tiffanylee.gailprojectapp.R;

import java.io.IOException;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";
    private static final int MSG_START_TIMER = 0;
    private static final int MSG_UPDATE_TIMER = 0;
    private static final int MSG_STOP_TIMER = 0;

    Button recordBtn;
    Button stopBtn;

    MediaRecorder voicdRecorder;
    String outputFile;
    Boolean recordingStopped = true;
    Boolean isRecording = false;
    private int startFlag = 0;

    //Stopwatch timer = new Stopwatch();
    private long startTime = 0;
    private long endTime = 0;

    /*Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_START_TIMER:
                    timer.start(); //start timer
                    mHandler.sendEmptyMessage(MSG_UPDATE_TIMER);
                    break;

                case MSG_UPDATE_TIMER:
                    tvTextView.setText(""+ timer.getElapsedTime());
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIMER,REFRESH_RATE); //text view is updated every second,
                    break;                                  //though the timer is still running
                case MSG_STOP_TIMER:
                    mHandler.removeMessages(MSG_UPDATE_TIMER); // no more updates.
                    timer.stop();//stop timer
                    tvTextView.setText(""+ timer.getElapsedTime());
                    break;

                default:
                    break;
            }
        }
    };*/


    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 29;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.mp4";
        Log.d(TAG, outputFile);

        voicdRecorder = new MediaRecorder();
        if (Build.VERSION.SDK_INT >= 23) {
            if (v.getContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && v.getContext().checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                //return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1);
                //return false;
            }
        }

        //record = true;
        recordBtn = v.findViewById(R.id.record_btn);
        stopBtn = v.findViewById(R.id.stop_btn);
        recordBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        return v;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0:
                boolean isPerpermissionForAllGranted = false;
                if (grantResults.length > 0 && permissions.length==grantResults.length) {
                    for (int i = 0; i < permissions.length; i++){
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                            isPerpermissionForAllGranted=true;
                        }else{
                            isPerpermissionForAllGranted=false;
                        }
                    }

                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    isPerpermissionForAllGranted=true;
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }


                if(isPerpermissionForAllGranted){
                    try{
                        voicdRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        voicdRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                        voicdRecorder.setAudioEncoder(MediaRecorder.OutputFormat.MPEG_4);
                        voicdRecorder.setOutputFile(outputFile);
                        startFlag = 1;
                        Log.i(TAG, "====> PERMISSION GRANTED");
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
        }
    }


    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.record_btn:
                if(recordingStopped){
                    startTime = System.currentTimeMillis();
                    //recordBtn.setText("STOP");
                    try {
                        voicdRecorder = new MediaRecorder();
                        voicdRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        voicdRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                        voicdRecorder.setAudioEncoder(MediaRecorder.OutputFormat.MPEG_4);
                        voicdRecorder.setOutputFile(outputFile);
                        voicdRecorder.prepare();
                        voicdRecorder.start();
                        isRecording = true;
                        startFlag = 0;
                        Toast.makeText(v.getContext(), "Starting Recording", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.stop_btn:
                //recordBtn.setText("START");
                Log.d(TAG, "====> STOPING RECORDINGGGGGGG.......");
                if(isRecording) {
                    endTime = System.currentTimeMillis();
                    voicdRecorder.stop();
                    voicdRecorder.reset();
                    voicdRecorder.release();
                    recordingStopped = true;
                    isRecording = false;

                    MediaPlayer mediaPlayer = new MediaPlayer();

                    try{
                        mediaPlayer.setDataSource(outputFile);
                        mediaPlayer.prepare();
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    mediaPlayer.start();


                    Toast.makeText(v.getContext(), "Stoping Recording", Toast.LENGTH_LONG).show();
                }

        }
    }


}
