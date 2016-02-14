package com.example.gokuclickgame;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	ImageView goku1;
	int counter = 0;
	float scale = .75f;
	float transL = -85f;
	float transR = 215f;
	MediaPlayer freiza;
	MediaPlayer powerUp;
	MediaPlayer superSaiyan;
	MediaPlayer freiza25;
	MediaPlayer krillin50;
	MediaPlayer goku80;
	TextView tCounter;
	int maxCounter = 100;
	int counterReductionMultiplier;
	Animation shake;
	ImageView glowL;
	ImageView glowR;
	CountDownTimer c;
	TextView cdTest;
	float total;
	float alphaGlow = .10f;
	boolean krillinPlayed;
	boolean freizaPlayed;
	boolean gokuPlayed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		goku1 = (ImageView) findViewById(R.id.goku_1);
		glowL = (ImageView) findViewById(R.id.glow_L);
		glowR = (ImageView) findViewById(R.id.glow_R);
		cdTest = (TextView) findViewById(R.id.cd_test);
		tCounter = (TextView) findViewById(R.id.counter);
		freiza = mediaSettings(this, R.raw.freiza, false);
		powerUp = mediaSettings(this, R.raw.dbz_power_up_2, true);
		superSaiyan = mediaSettings(this, R.raw.dbz_super_saiyan_2, true);
		freiza25 = mediaSettings(this, R.raw.freiza_laugh_25, false);
		krillin50 = mediaSettings(this, R.raw.krillin_blow_up_50, false);
		goku80 = mediaSettings(this, R.raw.goku_80, false);

//		tCounter.setText(R.string.counter_start);
		
		counterReductionMultiplier = 0;

		glowL.setTranslationX(transL);
		glowR.setTranslationX(transR);
		glowL.setAlpha(alphaGlow);
		glowR.setAlpha(alphaGlow);

		shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		shake.setRepeatCount(Animation.INFINITE);

		c = new CountDownTimer(30000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				if (counter > 0 && counter != maxCounter && counter != 1 + maxCounter && counterReductionMultiplier != 0) {
					counter -= (counterReductionMultiplier * 1);
					total += (counterReductionMultiplier * 1);
//					cdTest.setText("counter - " + total);
//					tCounter.setText(counter + "");
					decreaseSize();

				}

			}

			@Override
			public void onFinish() {
				c.start();
			}
		}.start();

		goku1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (counter < 1 && !powerUp.isPlaying()) {
					freiza.start();

					freiza.setOnCompletionListener(new OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer mp) {
							if (freiza.isPlaying()) {
								freiza.stop();
								freiza.release();
							}
							powerUp.start();
						}
					});
				}

				if (counter < maxCounter) {

					counter++;
					vibrate();
					goku1.startAnimation(shake);
//					tCounter.setText(counter + "");
					increaseSize();
					counterCheckPoints();

				} else if (counter == maxCounter) {
					powerUp.stop();
					powerUp.release();
					superSaiyan.start();
					imageFadeAnims();
					counter++;
				}
			}
		});
	}

	public void increaseSize() {
		vibrate();
		transL += (40f / maxCounter);
		transR -= (40f / maxCounter);
		alphaGlow += (.70f / maxCounter);
		scale += ((30f / maxCounter) / 100f);

		glowL.setX(transL);
		glowR.setX(transR);
		glowL.setAlpha(alphaGlow);
		glowR.setAlpha(alphaGlow);
		goku1.setScaleX(scale);
		goku1.setScaleY(scale);

	}

	public void decreaseSize() {
		transL -= (40f / maxCounter * counterReductionMultiplier);
		transR += (40f / maxCounter * counterReductionMultiplier);
		alphaGlow -= (.70f / maxCounter * counterReductionMultiplier);
		scale -= (.30f / maxCounter * counterReductionMultiplier);

		glowL.setX(transL);
		glowR.setX(transR);
		glowL.setAlpha(alphaGlow);
		glowR.setAlpha(alphaGlow);
		goku1.setScaleX(scale);
		goku1.setScaleY(scale);
	}

	public void vibrate() {
		Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(500);
	}

	public void counterCheckPoints() {
		if (counter == .40 * maxCounter && freizaPlayed != true) {
			freiza25.start();
			freizaPlayed = true;
			counterReductionMultiplier  = 1;
			freiza25.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					freiza25.release();

				}
			});
		}

		if (counter == .70 * maxCounter && krillinPlayed != true) {
			krillin50.start();
			krillinPlayed = true;
			counterReductionMultiplier  = 2;
			krillin50.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					krillin50.release();

				}
			});
		}

		if (counter == .85 * maxCounter && gokuPlayed != true) {
			goku80.start();
			gokuPlayed = true;
			counterReductionMultiplier = 3;
			goku80.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					goku80.release();

				}
			});
		}
	}
	
	public MediaPlayer mediaSettings(Context context, int music, boolean isLooping) {
		MediaPlayer media = MediaPlayer.create(context, music);
		media.setAudioStreamType(AudioManager.STREAM_MUSIC);
		if (isLooping) {
			media.setLooping(isLooping);
		}

		return media;

	}

	public void imageFadeAnims() {
		Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		goku1.startAnimation(fadeOut);
		fadeOut.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Animation fadeIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
				goku1.setImageResource(R.drawable.goku_s_4);
				goku1.startAnimation(fadeIn);
			}
		});
	}

}
