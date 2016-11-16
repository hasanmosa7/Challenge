package com.example.hasan.challenge;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Screen4 extends AppCompatActivity implements MicChooseDialog.InterfaceCommunicator{

    private Button recordButton;
    private Button stopButton;
    private Button playButton;
    private MediaRecorder mediaRecorder;
    String voiceStoragePath;
    ArrayList<String> mic_options_list;
    MediaPlayer mediaPlayer;
    boolean headSetOn = false;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("state")){
                if (headSetOn && intent.getIntExtra("state", 0) == 0){
                    headSetOn = false;
                } else if (!headSetOn && intent.getIntExtra("state", 0) == 1){
                    headSetOn = true;
                    mic_options_list = new ArrayList<String> ();
                    mic_options_list.add("Phone Mic");
                    mic_options_list.add("Headset Mic");
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen4);

        voiceStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File audioVoice = new File(voiceStoragePath + File.separator + "voices");
        if(!audioVoice.exists()){
            audioVoice.mkdir();
        }
        String fileName = "challengeRecordingFile";
        voiceStoragePath = voiceStoragePath + File.separator + "voices/" + fileName + ".3gpp";
        System.out.println("Audio path : " + voiceStoragePath);

        recordButton = (Button)findViewById(R.id.recording_button);
        stopButton = (Button)findViewById(R.id.stop_button);
        playButton = (Button)findViewById(R.id.play_button);

        stopButton.setEnabled(false);
        stopButton.setAlpha((float) 0.1);
        playButton.setEnabled(false);
        playButton.setAlpha((float) 0.1);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean on =  v.isPressed();
                v.setPressed(true);
                if (on) {
                    if (mediaRecorder == null){
                        initializeAndStartRecording();
                    }
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAudioRecording();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playLastStoredAudioMusic();
                stopMediaPlayerPlaying();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        getApplicationContext().registerReceiver(mReceiver, filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_audio_recording, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startAudioRecording(){
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(voiceStoragePath);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recordButton.setEnabled(false);
        recordButton.setAlpha((float) 0.1);
        stopButton.setAlpha((float) 1);
        stopButton.setEnabled(true);
    }

    private void stopAudioRecording(){
        if(mediaRecorder != null){
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
        stopButton.setEnabled(false);
        stopButton.setAlpha((float) 0.1);
        playButton.setAlpha((float) 1);
        playButton.setEnabled(true);
    }

    private void playLastStoredAudioMusic(){
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(voiceStoragePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        playButton.setEnabled(false);
        playButton.setAlpha((float) 0.1);
    }

    private void stopAudioPlay(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void stopMediaPlayerPlaying(){
        if(!mediaPlayer.isPlaying()){
            stopAudioPlay();
        }
        recordButton.setAlpha((float) 1);
        recordButton.setEnabled(true);
    }

    private void initializeAndStartRecording(){
        if(mediaRecorder==null) {
            mediaRecorder = new MediaRecorder();
        }
        if(headSetOn) {
            choosemic();
        }
        else
        {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            startAudioRecording();
        }

    }

    @Override
    public void sendRequestCode(int code, int resultCode, Intent data) {
        if(resultCode==200){
            String choosed_mic = data.getStringExtra("result");
            if (choosed_mic.compareTo("Phone Mic") == 0)
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            else if (choosed_mic.compareTo("Headset Mic") == 0)
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            startAudioRecording();
        }
    }

    private void choosemic()
    {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MicChooseDialog fragment = new MicChooseDialog();
        Bundle parameters = new Bundle();
        parameters.putStringArrayList("mic_list", mic_options_list);
        fragment.setArguments(parameters);
        fragment.interfaceCommunicator=this;
        fragment.show(fragmentTransaction, "Mic");
    }

}