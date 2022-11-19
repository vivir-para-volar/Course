package com.irinalyamina.appnetworkforphotographers

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class ShowMessage {
    companion object{
        private const val duration = Toast.LENGTH_SHORT

        fun toast(context: Context, text: String?){
            Toast.makeText(context, text, duration).show()
        }
    }
}