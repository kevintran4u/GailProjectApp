package com.example.tiffanylee.gailprojectapp.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
    Button recordBtn;

    MediaRecorder voicdRecorder;
    String outputFile;
    Boolean record = true;
    private int startFlag = 0;

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 29;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";


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

        record = true;
        recordBtn = v.findViewById(R.id.record_btn);
        recordBtn.setOnClickListener(this);

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
                    voicdRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    voicdRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    voicdRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                    voicdRecorder.setOutputFile(outputFile);
                    startFlag = 1;
                }
                break;
        }
    }


    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.record_btn:
                if(record){
                    record = false;
                    recordBtn.setText("STOP");
                    try {
                        if(startFlag == 1) {
                            voicdRecorder.prepare();
                            voicdRecorder.start();
                            startFlag = 0;
                        }
                        //Toast.makeText(v.getContext(), "Starting Recording", Toast.LENGTH_LONG).show();
                    } catch (IllegalStateException ill) {
                        // error...
                    } catch (IOException io) {
                        // error
                    }
                }else{
                    record = true;
                    recordBtn.setText("START");
                    if(startFlag == 0) {
                        voicdRecorder.stop();
                        voicdRecorder.release();
                        voicdRecorder = null;
                    }
                    //Toast.makeText(v.getContext(), "Stoping Recording", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


}
