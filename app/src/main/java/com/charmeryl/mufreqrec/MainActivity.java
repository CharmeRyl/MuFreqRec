package com.charmeryl.mufreqrec;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.charmeryl.mufreqrec.listener.*;

public class MainActivity extends AppCompatActivity {

    private EditText freq_start,freq_intvl,samp_rate,num_freq,samp_rate_rec,save_path;
    private RadioGroup out_mode;
    private RadioButton mode_chked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initialView();
        getPref();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("About")
                    .setIcon(R.drawable.ic_info_outline_black_48px)
                    .setView(R.layout.dialog_about)
                    .create();
            dialog.show();
            return true;
        }
        if (id == R.id.action_exit) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initialView(){
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Text Views
        this.freq_start = (EditText)findViewById(R.id.freq_start);
        this.freq_intvl = (EditText)findViewById(R.id.freq_interval);
        this.samp_rate = (EditText)findViewById(R.id.samp_rate);
        this.num_freq = (EditText)findViewById(R.id.num_freq);
        this.out_mode = (RadioGroup)findViewById(R.id.out_mode);
        this.samp_rate_rec = (EditText)findViewById(R.id.samp_rate_rec);
        this.save_path = (EditText)findViewById(R.id.save_path);
        //Save Fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new OnSaveFabClickListener(freq_start,freq_intvl,samp_rate,num_freq,out_mode,samp_rate_rec,save_path));
        //Rec Button
        ImageButton btn_rec = (ImageButton) findViewById(R.id.btn_rec);
        btn_rec.setOnClickListener(new OnRecBtnClickListener(freq_start,freq_intvl,samp_rate,num_freq,out_mode,samp_rate_rec,save_path,fab));

    }

    private void getPref(){
        SharedPreferences preferences = getSharedPreferences("pref",MODE_PRIVATE);
        if(!preferences.getBoolean("initialized",false)){
            preferences.edit()
                    .putInt("freq_start",18000)
                    .putInt("freq_intvl",350)
                    .putInt("samp_rate",48000)
                    .putInt("num_freq",3)
                    .putString("out_mode","handset")
                    .putInt("samp_rate_rec",48000)
                    .putString("save_path","/rec/record.pcm")
                    .putBoolean("initialized",true)
                    .apply();
        }
        this.freq_start.setText(Integer.toString(preferences.getInt("freq_start",0)));
        this.freq_intvl.setText(Integer.toString(preferences.getInt("freq_intvl",0)));
        this.samp_rate.setText(Integer.toString(preferences.getInt("samp_rate",0)));
        this.num_freq.setText(Integer.toString(preferences.getInt("num_freq",0)));
        this.samp_rate_rec.setText(Integer.toString(preferences.getInt("samp_rate_rec",0)));
        this.save_path.setText(preferences.getString("save_path",""));
        switch (preferences.getString("out_mode","handset")){
            case "handset":
                this.mode_chked = (RadioButton)findViewById(R.id.mode_handset);
                break;
            case "speaker":
                this.mode_chked = (RadioButton)findViewById(R.id.mode_speaker);
                break;
        }
        this.mode_chked.setChecked(true);
    }

}
