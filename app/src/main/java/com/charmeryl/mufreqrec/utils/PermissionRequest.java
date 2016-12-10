package com.charmeryl.mufreqrec.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.charmeryl.mufreqrec.MainActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.jar.Manifest;

/**
 * Created by Jiaqiu on 2016/12/10.
 */

public class PermissionRequest {
    private List<String> permissions;
    private List<String> permissionsRequestList;
    private Activity activity;

    private static final int REQUEST_CODE = 1;

    public PermissionRequest(Activity activity){
        this.permissions = new ArrayList<String>();
        this.permissionsRequestList = new ArrayList<String>();
        this.activity = activity;
    }

    public PermissionRequest addPermission(String permission){
        this.permissions.add(permission);
        return this;
    }

    public void request(){
        this.checkPermissions();
        ActivityCompat.requestPermissions(this.activity,
                this.permissionsRequestList.toArray(new String[this.permissionsRequestList.size()]),
                this.REQUEST_CODE);

    }

    private void checkPermissions(){
        for(String permission : this.permissions){
            if(ContextCompat.checkSelfPermission(this.activity,permission)!= PackageManager.PERMISSION_GRANTED){
                this.permissionsRequestList.add(permission);
            }
        }
    }

}
