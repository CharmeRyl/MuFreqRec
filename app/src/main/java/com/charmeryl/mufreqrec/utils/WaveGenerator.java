package com.charmeryl.mufreqrec.utils;

import android.Manifest;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Created by Jiaqiu on 2016/12/9.
 */

public class WaveGenerator {
    private final int duration = 100;  // 100 seconds
    private int samp_rate = 48000;
    private int freq_start = 18000;
    private int freq_intvl = 350;
    private int num_freq = 3;
    private int out_mode = 1;

    private int num_samps;
    private double samples[];
    private byte gen_snd[];

    private AudioTrack audioTrack;

    public WaveGenerator(Context context){

    }

    /**
     * frequency setting part.
     */
    public WaveGenerator setFreq(int freq_start){
        if(freq_start>0){
            this.freq_start = freq_start;
            return this;
        }
        this.freq_start = 18000;
        return this;
    }
    public WaveGenerator setFreqIntvl(int freq_intvl){
        if(freq_intvl>0){
            this.freq_intvl = freq_intvl;
            return this;
        }
        this.freq_intvl = 350;
        return this;
    }

    /**
     * sample rate setting part
     */
    public WaveGenerator setSampRate(int samp_rate){
        if(samp_rate>0){
            this.samp_rate = samp_rate;
            return this;
        }
        this.samp_rate = 48000;
        return this;
    }

    /**
     * number of frequencies
     */
    public WaveGenerator setNumFreq(int num_freq){
        if(num_freq>0){
            this.num_freq = num_freq;
            return this;
        }
        this.num_freq = 3;
        return this;
    }

    /**
     * output mode
     */
    public WaveGenerator setOutMode(int out_mode){
        if(out_mode == 1 || out_mode == 2){
            this.out_mode = out_mode;
            return this;
        }
        this.out_mode = 1;
        return this;
    }

    /**
     * build generator
     */
    public WaveGenerator build(){
        //build objects
        this.num_samps = this.samp_rate * this.duration;
        this.samples = new double[this.num_samps];
        this.gen_snd = new byte[2*this.num_samps];

        //build data
        for(int i=0; i<this.num_freq; i++){
            for(int j=0; j<this.num_samps; j++){
                int freq = this.freq_start+this.freq_intvl*i;
                this.samples[j]+=Math.cos(2*Math.PI*freq*j/this.samp_rate)/this.num_freq;
            }
        }

        //convert to 16bit pcm
        int idx = 0;
        for (final double dVal : samples) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            gen_snd[idx++] = (byte) (val & 0x00ff);
            gen_snd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }

        return this;
    }

    /**
     * play sound
     */
    public void play(){
        if(gen_snd.length==0){
            Log.w("WaveGenerator","pls call build before play.");
            this.build();
        }
        int stream_type = (out_mode==1)?AudioManager.STREAM_VOICE_CALL:AudioManager.STREAM_MUSIC;
        audioTrack = new AudioTrack(stream_type,
                this.samp_rate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,gen_snd.length,
                AudioTrack.MODE_STATIC);
        audioTrack.write(gen_snd,0,gen_snd.length);
        audioTrack.play();
    }

    /**
     * stop sound
     */
    public void stop(){
        audioTrack.stop();
    }



}
