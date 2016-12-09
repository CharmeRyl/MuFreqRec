package com.charmeryl.mufreqrec.listener;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.charmeryl.mufreqrec.R;

/**
 * Created by Jiaqiu on 2016/12/8.
 */

public class OnSaveFabClickListener implements View.OnClickListener {
    private EditText freq_start,freq_intvl,samp_rate,num_freq,samp_rate_rec,save_path;
    private RadioGroup out_mode;

    public OnSaveFabClickListener(EditText freq_start, EditText freq_intvl, EditText samp_rate,
            EditText num_freq, RadioGroup out_mode, EditText samp_rate_rec, EditText save_path){
        this.freq_start = freq_start;
        this.freq_intvl = freq_intvl;
        this.samp_rate = samp_rate;
        this.num_freq = num_freq;
        this.out_mode = out_mode;
        this.samp_rate_rec = samp_rate_rec;
        this.save_path = save_path;
    }

    @Override
    public void onClick(View view) {
        SharedPreferences preferences = view.getContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        String out_mode="";
        switch (this.out_mode.getCheckedRadioButtonId()){
            case R.id.mode_handset:
                out_mode = "handset";
                break;
            case R.id.mode_speaker:
                out_mode = "speaker";
                break;
        }
        preferences.edit()
                .putInt("freq_start",Integer.parseInt(this.freq_start.getText().toString()))
                .putInt("freq_intvl",Integer.parseInt(this.freq_intvl.getText().toString()))
                .putInt("samp_rate",Integer.parseInt(this.samp_rate.getText().toString()))
                .putInt("num_freq",Integer.parseInt(this.num_freq.getText().toString()))
                .putString("out_mode",out_mode)
                .putInt("samp_rate_rec",Integer.parseInt(this.samp_rate_rec.getText().toString()))
                .putString("save_path",this.save_path.getText().toString())
                .putBoolean("initialized",true)
                .commit();
        Snackbar.make(view, "Settings Saved", Snackbar.LENGTH_SHORT).show();
    }
}
