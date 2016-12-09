package com.charmeryl.mufreqrec.utils;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Jiaqiu on 2016/12/9.
 */

public class WaveRecorder {
    private int samp_rate = 48000;
    private String path_prefix;
    private String save_path = "/rec/record.pcm";

    private boolean isRecording = false;
    private int buf_size;
    private AudioRecord audioRecord;

    public WaveRecorder(){
        this.path_prefix = Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public WaveRecorder setSampRate(int samp_rate){
        if(samp_rate>0){
            this.samp_rate = samp_rate;
            return this;
        }
        this.samp_rate = 48000;
        return this;
    }

    public WaveRecorder setSavePath(String save_path){
        if(!save_path.isEmpty()){
            this.save_path = save_path;
            return this;
        }
        this.save_path = "/rec/record.pcm";
        return this;
    }

    public String getSavePath(){
        return this.save_path;
    }

    public WaveRecorder build(){
        // build objects
        this.buf_size = AudioRecord.getMinBufferSize(this.samp_rate, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        this.audioRecord = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, this.samp_rate,
                AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT, this.buf_size);
        // create file
        File file = new File(this.path_prefix+this.save_path);
        if(file.exists()){
            file.delete();
        }
        try {
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }catch (Exception e){
            e.printStackTrace();
        }
        return this;
    }

    public void record(){
        try {
            this.audioRecord.startRecording();
            this.isRecording = true;
        }catch (Exception e){
            e.printStackTrace();
            Log.e("DOPPLER", "start recording error");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                write();
            }
        }).start();

    }

    public void stop(){
        this.isRecording = false;
        this.audioRecord.stop();
        this.audioRecord.release();
    }

    private void write(){
        short sData[] = new short[this.buf_size];

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(this.path_prefix+this.save_path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (this.isRecording) {
            // gets the voice output from microphone to byte format

            this.audioRecord.read(sData, 0, this.buf_size);
            //System.out.println("Short wirting to file" + sData.toString());
            try {
                // writes the data to file from buffer
                // stores the voice buffer
                byte bData[] = short2byte(sData);
                os.write(bData, 0, this.buf_size * 2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;

    }

}
