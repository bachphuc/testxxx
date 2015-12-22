package com.learn.mobile.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import com.alexvasilkov.gestures.Settings;
import com.alexvasilkov.gestures.views.GestureImageView;

/**
 * Created by 09520_000 on 12/22/2015.
 */
public class CustomGestureImageView extends GestureImageView {
    public static final String TAG = CustomGestureImageView.class.getSimpleName();

    protected boolean isMovementAreaSpecified = false;
    protected float movementRatio = 1;
    protected int maxWidth = 80;
    protected boolean isFullMovement = false;

    public CustomGestureImageView(Context context) {
        super(context);
    }

    public CustomGestureImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomGestureImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomGestureImageView setMovementAreaSpecified(boolean b) {
        isMovementAreaSpecified = b;
        return this;
    }

    protected void calculateMovementSize() {
        float w = 0, h = 0;
        if (isFullMovement) {
            w = getWidth();
            h = w / movementRatio;
        } else {
            w = maxWidth * getWidth() / 100;
            h = w / movementRatio;
        }
        Log.i(TAG, "Movement XY: " + w + "," + h);
        getController().getSettings().setMovementArea((int) w, (int) h);
    }

    public CustomGestureImageView setMaxWidthMovement(int i) {
        maxWidth = i;
        calculateMovementSize();
        return this;
    }

    public CustomGestureImageView setMovementRatio(float f) {
        isMovementAreaSpecified = true;
        movementRatio = f;
        calculateMovementSize();
        return this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isMovementAreaSpecified) {
            return;
        }
        RectF rect = new RectF();

        Settings settings = getController().getSettings();
        int width = settings.getMovementAreaW();
        int height = settings.getMovementAreaH();

        float x = (getWidth() + getPaddingLeft() - width) / 2;
        float y = (getHeight() + getPaddingTop() - height) / 2;

        rect.set(x, y, x + width, y + height);
        Paint paint = new Paint();
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        canvas.drawRect(rect, paint);

        int alpha = 200;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.argb(alpha, 0, 0, 0));

        RectF rectF = new RectF();
        rectF.set(0, 0, getWidth(), y);
        canvas.drawRect(rectF, paint);

        rectF.set(0, y + height, getWidth(), getHeight());
        canvas.drawRect(rectF, paint);

        rectF.set(0, y, x, y + height);
        canvas.drawRect(rectF, paint);

        rectF.set(getWidth() - x, y, getWidth(), y + height);
        canvas.drawRect(rectF, paint);
    }
}
