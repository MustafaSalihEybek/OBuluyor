package com.nexis.obuluyor.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.nexis.obuluyor.R
import com.nexis.obuluyor.util.Singleton
import com.nexis.obuluyor.viewmodel.FavoritesViewModel
import kotlinx.android.synthetic.main.remove_favorite_dialog.*

class RemoveFavoriteDialog(mContext: Context, val userId: Int, val advertId: Int, val favoriteId: Int, val fV: FavoritesViewModel) : Dialog(mContext), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.remove_favorite_dialog)

        remove_favorite_dialog_btnNo.setOnClickListener(this)
        remove_favorite_dialog_imgClose.setOnClickListener(this)
        remove_favorite_dialog_btnYes.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.remove_favorite_dialog_btnNo -> closeThisDialog()
                R.id.remove_favorite_dialog_imgClose -> closeThisDialog()
                R.id.remove_favorite_dialog_btnYes -> fV.removeFavorite(userId, advertId, favoriteId)
            }
        }
    }

    private fun closeThisDialog(){
        Singleton.closeRemoveFavoriteDialog()
    }
}