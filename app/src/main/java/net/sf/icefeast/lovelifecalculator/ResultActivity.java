package net.sf.icefeast.lovelifecalculator;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;

import android.widget.TextView;


import com.github.lzyzsd.circleprogress.CircleProgress;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class ResultActivity extends Activity {
    String loveStatus;
    MediaPlayer waterFlow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        final Animation fadeIn,fadeOut;
        fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
        fadeIn.setDuration(1200);
        fadeIn.setFillAfter(true);

        //requesting for data from MainActivity
        final String name1 = getIntent().getStringExtra("name1");
        final String name2 = getIntent().getStringExtra("name2");
        final String percentage = getIntent().getStringExtra("percentage");
        final String websiteAddress = getResources().getString(R.string.website_address);
        final int per = Integer.parseInt(percentage);
        //animating progressbar
        CircleProgress mProgress = (CircleProgress) findViewById(R.id.circle_progress);
        CircleProgressAnimation anim = new CircleProgressAnimation(mProgress, 0,Integer.parseInt(percentage) );
        anim.setDuration(2000);
        final Button shareButton = (Button) findViewById(R.id.share_button);
        shareButton.setVisibility(View.GONE);

        mProgress.startAnimation(anim);
        //loading water flow sound
        waterFlow = MediaPlayer.create(getApplicationContext(), R.raw.waterflow);
        //doing work after animation of circle progress
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {

                waterFlow.start();
                waterFlow.setLooping(true);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {

                waterFlow.stop();
                shareButton.setAnimation(fadeIn);
                shareButton.setVisibility(View.VISIBLE);

                TextView describeView = (TextView) findViewById(R.id.describeView);


                if(per >= 85){
                    loveStatus = "Match made in heaven";
                    describeView.setText(loveStatus);
                    describeView.setAnimation(fadeIn);
                }
                else if(per >= 75 && per < 85){
                    loveStatus = "Head over heals in love";
                    describeView.setText(loveStatus);
                    describeView.setAnimation(fadeIn);
                }
                else if(per >= 65 && per < 75){
                    loveStatus = "Lovey-dovey" ;
                    describeView.setText(loveStatus);
                    describeView.setAnimation(fadeIn);
                }
                else if(per >= 55 && per < 65){
                    loveStatus = "Soul mates " ;
                    describeView.setText(loveStatus);
                    describeView.setAnimation(fadeIn);
                }
                else if(per >= 45 && per < 55){
                    loveStatus = "Chocolate pair" ;
                    describeView.setText(loveStatus);
                    describeView.setAnimation(fadeIn);
                }
                else if(per >= 35 && per < 45){
                    loveStatus = "Kiss and make up" ;
                    describeView.setText(loveStatus);
                    describeView.setAnimation(fadeIn);
                }
                else if(per >= 25 && per < 35){
                    loveStatus = "Puppy love" ;
                    describeView.setText(loveStatus);
                    describeView.setAnimation(fadeIn);
                }
                else {
                    loveStatus = "On the rocks";
                    describeView.setText(loveStatus);
                    describeView.setAnimation(fadeIn);
                }
            }
        });
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                shareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = "Hey! "+name1+" and "+name2+" are like" +
                                            " "+"'"+loveStatus+"'"+
                                            " and they are "+percentage+"% match. Now check yours --> "
                                            +websiteAddress;
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    }
                });

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });





       // mProgress.setProgress(Integer.parseInt(percentage));
       // Toast.makeText(this,percentage, Toast.LENGTH_SHORT).show();




    }
    public class CircleProgressAnimation extends Animation {
        private CircleProgress mProgress;
        private float from;
        private float  to;

        public CircleProgressAnimation(CircleProgress mProgress, float from, float to) {
            super();
            this.mProgress = mProgress;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            mProgress.setProgress((int) value);

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    protected void onPause() {
        super.onPause();
        waterFlow.pause();
    }
    protected void onResume() {
        super.onResume();
        waterFlow.start();

    }

}
