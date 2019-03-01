package com.example.dell.concession;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IntroPage extends AppCompatActivity {

    private ViewPager slideViewPager;
    private LinearLayout dotLayout;

    private TextView[] mDots;

    private SliderAdapter sliderAdapter;

    private Button NextBtn;
    private Button BackBtn;

    private int CurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_page);

        slideViewPager=findViewById(R.id.slideViewPage);
        dotLayout=findViewById(R.id.dots);

        BackBtn=findViewById(R.id.prevbutton);
        NextBtn=findViewById(R.id.nextbutton);

        sliderAdapter=new SliderAdapter(this);
        slideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);
        slideViewPager.addOnPageChangeListener(viewListener);

        //OnClickListener to buttons
        NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideViewPager.setCurrentItem(CurrentPage+1);
            }
        });

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideViewPager.setCurrentItem(CurrentPage-1);
            }
        });

    }

    public void addDotsIndicator(int position){
        mDots=new TextView[4];
        dotLayout.removeAllViews();
        for(int i=0;i<mDots.length;i++){
            mDots[i]=new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

            dotLayout.addView(mDots[i]);

        }
        if(mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    ViewPager.OnPageChangeListener viewListener= new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            CurrentPage=i;

            if(i==0){
                NextBtn.setEnabled(true);
                BackBtn.setEnabled(false);
                BackBtn.setVisibility(View.INVISIBLE);

                NextBtn.setText("Skip");
                NextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        Intent intent=new Intent(IntroPage.this,MainActivity.class);
                        startActivity(intent);
                    }
                });

                BackBtn.setText("");
            }else if(i==mDots.length-1){
                NextBtn.setEnabled(true);
                BackBtn.setEnabled(true);
                BackBtn.setVisibility(View.VISIBLE);

                NextBtn.setText("Finish");
                NextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        Intent intent=new Intent(IntroPage.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
                BackBtn.setText("Skip");
            }else{
                NextBtn.setEnabled(true);
                BackBtn.setEnabled(true);
                BackBtn.setVisibility(View.VISIBLE);

                NextBtn.setText("Skip");
                NextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        Intent intent=new Intent(IntroPage.this,MainActivity.class);
                        startActivity(intent);
                    }
                });

                BackBtn.setText("Back");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
