package org.jazzilla.contentsquareapp.repository;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.jazzilla.contentsquareapp.R;

public class RepositoryInfoItem extends LinearLayout {
    private TextView mTextView;
    private ImageView mImageView;

    public RepositoryInfoItem(Context context) {
        super(context);
        init(context, null);
    }

    public RepositoryInfoItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RepositoryInfoItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public RepositoryInfoItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.repository_info_item, this);
        mTextView = findViewById(R.id.itemTextView);
        mImageView = findViewById(R.id.itemImageView);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RepositoryInfoItem);

        String text = array.getString(R.styleable.RepositoryInfoItem_text);
        Drawable drawable = array.getDrawable(R.styleable.RepositoryInfoItem_image);

        if(text != null) {
            mTextView.setText(text);
        }

        if(drawable != null) {
            mImageView.setImageDrawable(drawable);
        }

        array.recycle();
    }

    public void setText(String text) {
        mTextView.setText(text);
    }
}
