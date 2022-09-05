package com.nexis.obuluyor.view.dialog

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.nexis.obuluyor.R
import com.nexis.obuluyor.util.Singleton
import kotlinx.android.synthetic.main.splash_dialog.*

class SplashDialog(mContext: Context) : Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
    private val duration: Long = 1000
    private var xScale: Float = 0f
    private var yScale: Float = 0f
    private lateinit var objectScaleAnimX: ObjectAnimator
    private lateinit var objectScaleAnimY: ObjectAnimator
    private lateinit var animatorSet: AnimatorSet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_dialog)

        if (Singleton.themeMode.equals("Dark"))
            splash_dialog_imgAppLogo.imageTintList = ColorStateList.valueOf(Color.WHITE)
        else
            splash_dialog_imgSplashLogo.setImageResource(R.drawable.splash_logo_2)

        startAnim(false)
    }

    private fun startAnim(isStart: Boolean){
        xScale = 0f
        yScale = 0f

        if (isStart){
            yScale = 2.15f
            xScale = 2.15f

            splash_dialog_imgAppLogo.visibility = View.VISIBLE
            splash_dialog_imgSplashLogo.visibility = View.GONE
        } else {
            yScale = 0.25f
            xScale = 0.25f

            splash_dialog_imgSplashLogo.visibility = View.VISIBLE
            splash_dialog_imgAppLogo.visibility = View.GONE
        }

        if (isStart){
            objectScaleAnimX = ObjectAnimator.ofFloat(splash_dialog_imgAppLogo, "scaleX", xScale)
            objectScaleAnimX.duration = duration

            objectScaleAnimY = ObjectAnimator.ofFloat(splash_dialog_imgAppLogo, "scaleY", yScale)
            objectScaleAnimY.duration = duration
        } else {
            objectScaleAnimX = ObjectAnimator.ofFloat(splash_dialog_imgSplashLogo, "scaleX", xScale)
            objectScaleAnimX.duration = duration

            objectScaleAnimY = ObjectAnimator.ofFloat(splash_dialog_imgSplashLogo, "scaleY", yScale)
            objectScaleAnimY.duration = duration
        }

        animatorSet = AnimatorSet()
        animatorSet.playTogether(objectScaleAnimX)
        animatorSet.playTogether(objectScaleAnimY)

        animatorSet.start()
        animatorSet.addListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(p0: Animator?) {}

            override fun onAnimationEnd(p0: Animator?) {
                if (!isStart){
                    startAnim(!isStart)
                }
            }

            override fun onAnimationCancel(p0: Animator?) {}

            override fun onAnimationRepeat(p0: Animator?) {
            }
        })
    }
}