package com.xray.filetransfer.ui;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xray.filetransfer.R;
import com.xray.filetransfer.entity.FileEntity;
import com.xray.filetransfer.utils.FileSizeUtil;

import java.util.List;

/**
 * Created by cfp on 2019-11-25.
 */
public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.FileViewHolder> {


    private List<FileEntity> mData;


    public FileListAdapter(List<FileEntity> data){
        this.mData = data;
    }

    public void setData(List<FileEntity> data){
        this.mData = data;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_file, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {

        FileEntity entity = mData.get(position);
        holder.mTvFileName.setText(entity.fileName);
        holder.mTvFileSize.setText(String.valueOf(FileSizeUtil.formatFileSize(entity.fileSize)));
        holder.mIvThumbnail.setImageResource(R.drawable.ic_launcher_background);
    }

    @Override
    public int getItemCount() {
        return (mData == null) ? 0 : mData.size();
    }

    class FileViewHolder extends RecyclerView.ViewHolder{

        ImageView mIvThumbnail;
        TextView mTvFileName;
        TextView mTvFileSize;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvThumbnail = itemView.findViewById(R.id.iv_item_thumbnail);
            mTvFileName = itemView.findViewById(R.id.tv_file_name);
            mTvFileSize = itemView.findViewById(R.id.tv_file_size);
        }
    }
}
