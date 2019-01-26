package com.example.dell.concession;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context=context;
    }

    //Array for instructions
    public String[] slide_descs={
            "Student have to fill the profile tab only once in a year.After filled once the profile tab fields are been disabled.",
            "Railway concession should be filled monthly or quaterly.",
            "Student will not be allowed to fill any details in the railway concession tab in between the pass start and end date.",
            "Student have to get the printout of the railway concession form from the college staff."
    };

    //Array for images
    public int[] slide_images={
            R.drawable.fillform,
            R.drawable.mnthly,
            R.drawable.notallowed,
            R.drawable.printout
    };

    @Override
    public int getCount() {
        return slide_descs.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide_layout,container,false);

        TextView head=view.findViewById(R.id.heading);
        TextView slidetext=view.findViewById(R.id.slide_desc);
        ImageView slideimageView=view.findViewById(R.id.slide_image);

        slidetext.setText(slide_descs[position]);
        slideimageView.setImageResource(slide_images[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
