package com.example.mugsassesment.Utility;

import android.app.Activity;
import android.app.ProgressDialog;

public class WaitUser {

     ProgressDialog progressDialog;
     Activity activity;

    public WaitUser(Activity context) {
        this.activity = context;
        progressDialog= new ProgressDialog(context);
    }

     public  void start_Dialog(String title,String msg){
        progressDialog.setTitle(title);
        progressDialog.setMessage(msg);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

    }

    public  void  close_Dialog()
    {

        progressDialog.dismiss();

    }


}
