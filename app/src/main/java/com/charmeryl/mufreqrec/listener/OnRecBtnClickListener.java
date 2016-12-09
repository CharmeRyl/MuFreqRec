package com.charmeryl.mufreqrec.listener;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.charmeryl.mufreqrec.R;
import com.charmeryl.mufreqrec.tasks.GenRecTask;
import com.charmeryl.mufreqrec.utils.WaveGenerator;
import com.charmeryl.mufreqrec.utils.WaveRecorder;

/**
 * Created by Jiaqiu on 2016/12/8.
 */

public class OnRecBtnClickListener implements View.OnClickListener {

    // Views
    private EditText freq_start,freq_intvl,samp_rate,num_freq,samp_rate_rec,save_path;
    private FloatingActionButton save_fab;
    private RadioGroup out_mode;

    // Controls
    private WaveGenerator waveGenerator;
    private WaveRecorder waveRecorder;

    // tasks
    private GenRecTask genRecTask;

    public OnRecBtnClickListener(EditText freq_start, EditText freq_intvl, EditText samp_rate,
            EditText num_freq, RadioGroup out_mode, EditText samp_rate_rec, EditText save_path,
            FloatingActionButton save_fab){
        this.freq_start = freq_start;
        this.freq_intvl = freq_intvl;
        this.samp_rate = samp_rate;
        this.num_freq = num_freq;
        this.out_mode = out_mode;
        this.samp_rate_rec = samp_rate_rec;
        this.save_path = save_path;
        this.save_fab = save_fab;
    }

    private void setView(boolean status){
        this.freq_start.setEnabled(status);
        this.freq_intvl.setEnabled(status);
        this.samp_rate.setEnabled(status);
        this.num_freq.setEnabled(status);
        this.out_mode.getChildAt(1).setEnabled(status);
        this.out_mode.getChildAt(2).setEnabled(status);
        this.samp_rate_rec.setEnabled(status);
        this.save_path.setEnabled(status);
        this.save_fab.setVisibility(status==true?View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        ImageButton imgBtn = (ImageButton) v;
        CharSequence status = imgBtn.getContentDescription();
        waveGenerator = waveGenerator == null? new WaveGenerator(v.getContext()):waveGenerator;
        waveRecorder = waveRecorder == null? new WaveRecorder():waveRecorder;

        if(status.equals("stopped")){
            this.setView(false);
            genRecTask = new GenRecTask(v.getContext());
            genRecTask.setWaveGen(waveGenerator)
                    .setFreq(Integer.parseInt(this.freq_start.getText().toString()))
                    .setFreqIntvl(Integer.parseInt(this.freq_intvl.getText().toString()))
                    .setSampRate(Integer.parseInt(this.samp_rate.getText().toString()))
                    .setNumFreq(Integer.parseInt(this.num_freq.getText().toString()))
                    .setOutMode(this.out_mode.getCheckedRadioButtonId()==R.id.mode_handset?1:2);
            genRecTask.setWaveRec(waveRecorder)
                    .setSampRateRec(Integer.parseInt(this.samp_rate_rec.getText().toString()))
                    .setSavePath(this.save_path.getText().toString());
            genRecTask.execute();
            imgBtn.setImageResource(R.drawable.ic_stop_white_96dp);
            imgBtn.setBackgroundResource(R.drawable.oval_button_stop);
            imgBtn.setContentDescription("recording");
        }else if(status.equals("recording")){
            waveRecorder.stop();
            waveGenerator.stop();
            genRecTask.cancel(true);
            Snackbar.make(v,"PCM file saved to "+waveRecorder.getSavePath(),Snackbar.LENGTH_LONG).show();
            imgBtn.setImageResource(R.drawable.ic_mic_white_96dp);
            imgBtn.setBackgroundResource(R.drawable.oval_button_rec);
            imgBtn.setContentDescription("stopped");
            this.setView(true);
        }

    }


}
