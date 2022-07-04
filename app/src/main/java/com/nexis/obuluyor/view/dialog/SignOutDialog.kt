package com.nexis.obuluyor.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.nexis.obuluyor.R
import com.nexis.obuluyor.util.SharedPreferences
import com.nexis.obuluyor.view.ProfileFragmentDirections
import kotlinx.android.synthetic.main.sign_out_dialog.*

class SignOutDialog(val v: View, val userId: Int) : Dialog(v.context), View.OnClickListener {
    private lateinit var navDirections: NavDirections
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_out_dialog)
        sharedPreferences = SharedPreferences(v.context)

        sign_out_dialog_imgClose.setOnClickListener(this)
        sign_out_dialog_btnNo.setOnClickListener(this)
        sign_out_dialog_btnYes.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.sign_out_dialog_imgClose -> closeThisDialog()
                R.id.sign_out_dialog_btnNo -> closeThisDialog()
                R.id.sign_out_dialog_btnYes -> signOut(userId)
            }
        }
    }

    private fun closeThisDialog(){
        if (this.isShowing)
            this.dismiss()
    }

    private fun signOut(userId: Int){
        closeThisDialog()
        sharedPreferences.saveUserId(-1)

        navDirections = ProfileFragmentDirections.actionProfileFragmentToMainFragment(-1)
        Navigation.findNavController(v).navigate(navDirections)
    }
}