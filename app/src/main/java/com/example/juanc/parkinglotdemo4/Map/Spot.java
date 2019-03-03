package com.example.juanc.parkinglotdemo4.Map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.juanc.parkinglotdemo4.R;

public class Spot extends View {
    private Rect rectangle;
    private Paint paint;
    private float width;
    private float height;


    public Spot(Context context){
        super(context);
        init(null);
    }

    public Spot(Context context, AttributeSet attrs){
        super(context, attrs);
        init(attrs);
    }

    public Spot(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    private void init(@Nullable AttributeSet set){
        rectangle = new Rect();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);

        if(set == null){
            return;
        }

        TypedArray typedArray = getContext().obtainStyledAttributes(set, R.styleable.Spot);
        width = typedArray.getDimension(R.styleable.Spot_width,0);
        height = typedArray.getDimension(R.styleable.Spot_height,0);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        rectangle.left = 0;
        rectangle.top = 0;
        rectangle.right = (int) width;
        rectangle.bottom = (int)height;

        canvas.drawRect(rectangle,paint);
    }
}