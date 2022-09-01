package com.nexis.obuluyor.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.nexis.obuluyor.R
import com.nexis.obuluyor.view.addadvert.AddAdvertCategoriesFragmentDirections
import kotlinx.android.synthetic.main.exit_the_add_advert_dialog.*

class ExitTheAddAdvertDialog(val v: View, val userId: Int) : Dialog(v.context), View.OnClickListener {
    private lateinit var navDirections: NavDirections

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exit_the_add_advert_dialog)

        exit_the_add_advert_dialog_btnYes.setOnClickListener(this)
        exit_the_add_advert_dialog_imgClose.setOnClickListener(this)
        exit_the_add_advert_dialog_btnNo.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.exit_the_add_advert_dialog_btnYes -> backToPage()
                R.id.exit_the_add_advert_dialog_imgClose -> closeThisDialog()
                R.id.exit_the_add_advert_dialog_btnNo -> closeThisDialog()
            }
        }
    }

    private fun backToPage(){
        navDirections = AddAdvertCategoriesFragmentDirections.actionAdvertCategoriesFragmentToMainFragment(userId)
        Navigation.findNavController(v).navigate(navDirections)

        closeThisDialog()
    }

    private fun closeThisDialog(){
        if (this.isShowing)
            this.dismiss()
    }
}