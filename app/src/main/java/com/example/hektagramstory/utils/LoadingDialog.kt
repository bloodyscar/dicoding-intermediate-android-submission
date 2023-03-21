package com.example.hektagramstory.utils

import android.app.Activity
import android.app.AlertDialog
import com.example.hektagramstory.R

class LoadingDialog(private val activity: Activity) {
    private lateinit var alertDialog: AlertDialog

    fun startLoadingDialog() {
        val builder = AlertDialog.Builder(activity)

        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.custom_dialog, null))
        builder.setCancelable(true)

        alertDialog = builder.create()
        alertDialog.show()
    }

    fun dismiss(){
        alertDialog.dismiss()
    }
}