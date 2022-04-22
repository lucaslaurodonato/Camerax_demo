package com.example.camerax.view.splash

import android.animation.Animator
import android.os.Bundle
import android.os.Handler
import com.example.camerax.R
import com.example.camerax.databinding.ActivitySplashBinding
import com.example.camerax.mechanism.Constants.SPLASH_DISPLAY_TIME
import com.example.camerax.view.base.BaseActivity
import com.example.camerax.view.onboarding.OnboardingActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        controlAnimation()
    }

    private fun controlAnimation() {
        binding.lavSplash.addAnimatorListener(object :
            Animator.AnimatorListener {
            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
            override fun onAnimationStart(animator: Animator) {}
            override fun onAnimationEnd(animator: Animator) {
                startWelcomeScreen()
            }
        })
    }

    private fun startWelcomeScreen() {
        startActivity(OnboardingActivity.getStartIntent(this))
        finish()
    }

}