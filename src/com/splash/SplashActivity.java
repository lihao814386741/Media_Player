package com.splash;

import com.media.R;
import com.media.MainActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;

public class SplashActivity extends Activity{
	
	private Animation endAnimation = null;
	private Handler endAnimationHandler = null;
	private Runnable endAnimationRunnable = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.splashscreen);
		
		
		endAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		endAnimation.setFillAfter(true);

		endAnimation.setAnimationListener(new AnimationListener(){
			public void onAnimationEnd(Animation animation) {
				MainActivity.launch(SplashActivity.this);
				SplashActivity.this.finish();
			}
			public void onAnimationRepeat(Animation animation) {
			}
			public void onAnimationStart(Animation animation) {
			}
			
		});
		
		endAnimationHandler = new Handler();
		endAnimationRunnable = new Runnable() {
			public void run() {
				findViewById(R.id.splashlayout).startAnimation(endAnimation);
			}
		};
		endAnimationHandler.postDelayed(endAnimationRunnable, 2000);
	}
	

}
