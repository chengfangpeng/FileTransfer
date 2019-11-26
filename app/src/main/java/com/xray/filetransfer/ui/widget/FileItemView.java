package com.xray.filetransfer.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xray.filetransfer.R;

/**
 * Created by cfp on 2019-11-26.
 */
public class FileItemView extends LinearLayout {



    private ImageView mIvThumbnail;
    private TextView mTvFileName;
    private TextView mTvFileSize;


    public FileItemView(@NonNull Context context) {
        super(context);
        initView();
    }

    public FileItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FileItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setFocusable(true);
        setOrientation(HORIZONTAL);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_item_file, this);
        mIvThumbnail = findViewById(R.id.iv_item_thumbnail);
        mTvFileName = findViewById(R.id.tv_file_name);
        mTvFileSize = findViewById(R.id.tv_file_size);
        setClipChildren(false);
        setClipToPadding(false);
        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }


    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if(gainFocus){
            setScaleX(1.1f);
            setScaleY(1.1f);
        }else{
            setScaleX(1.0f);
            setScaleY(1.0f);
        }
    }

    public void setThumbnail(int thumbnail) {
        mIvThumbnail.setImageResource(thumbnail);
    }

    public void setFileName(String fileName) {
        mTvFileName.setText(fileName);
    }

    public void setFileSize(String fileSize) {
        mTvFileSize.setText(fileSize);
    }

}
