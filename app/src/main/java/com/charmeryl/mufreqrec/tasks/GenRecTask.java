package com.charmeryl.mufreqrec.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.charmeryl.mufreqrec.utils.WaveGenerator;
import com.charmeryl.mufreqrec.utils.WaveRecorder;

/**
 * Created by Jiaqiu on 2016/12/9.
 */

public class GenRecTask extends AsyncTask<Integer, Integer, String> {

    private Context context;
    private WaveGenerator waveGenerator;
    private WaveRecorder waveRecorder;

    private int freq_start,freq_intvl,samp_rate,num_freq,out_mode;
    private int samp_rate_rec;
    private String save_path;

    private ProgressDialog dialog;

    public GenRecTask(Context context){
        this.context = context;
        dialog = new ProgressDialog(this.context);
        dialog.setCancelable(false);
        dialog.setMessage("Generating sound wave...");
    }

    /**
     * Wave Generator
     * @param waveGen
     * @return
     */
    public GenRecTask setWaveGen(WaveGenerator waveGen){
        this.waveGenerator = waveGen;
        return this;
    }

    public GenRecTask setFreq(int freq_start){
        this.freq_start = freq_start;
        return this;
    }

    public GenRecTask setFreqIntvl(int freq_intvl){
        this.freq_intvl = freq_intvl;
        return this;
    }

    public GenRecTask setSampRate(int samp_rate){
        this.samp_rate = samp_rate;
        return this;
    }

    public GenRecTask setNumFreq(int num_freq){
        this.num_freq = num_freq;
        return this;
    }
    public GenRecTask setOutMode(int out_mode){
        this.out_mode = out_mode;
        return this;
    }

    /**
     * WaveRecorder
     * @param waveRec
     * @return
     */
    public GenRecTask setWaveRec(WaveRecorder waveRec){
        this.waveRecorder = waveRec;
        return this;
    }
    public GenRecTask setSampRateRec(int samp_rate_rec){
        this.samp_rate_rec = samp_rate_rec;
        return this;
    }
    public GenRecTask setSavePath(String save_path){
        this.save_path = save_path;
        return this;
    }


    /**
     * AsyncTask
     */
    @Override
    protected void onPreExecute(){
        dialog.show();
    }

    @Override
    protected String doInBackground(Integer... param) {
        this.waveGenerator.setFreq(freq_start)
                .setFreqIntvl(freq_intvl)
                .setSampRate(samp_rate)
                .setNumFreq(num_freq)
                .setOutMode(out_mode)
                .build();
        this.waveRecorder.setSampRate(samp_rate_rec)
                .setSavePath(save_path)
                .build();
        this.waveGenerator.play();
        this.waveRecorder.record();
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... progresses) {
        //dialog.dismiss();
    }

    @Override
    protected void onPostExecute(String result) {
        dialog.dismiss();
    }
}
