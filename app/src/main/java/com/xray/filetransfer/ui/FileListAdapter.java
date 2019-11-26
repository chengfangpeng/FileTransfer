package com.xray.filetransfer.ui;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xray.filetransfer.R;
import com.xray.filetransfer.entity.FileEntity;
import com.xray.filetransfer.ui.widget.FileItemView;
import com.xray.filetransfer.utils.FileSizeUtil;
import com.xray.filetransfer.utils.NetworkUtils;
import com.xray.filetransfer.utils.OpenFileUtil;
import com.xray.filetransfer.utils.QRCodeUtils;

import java.util.List;

/**
 * Created by cfp on 2019-11-25.
 */
public class FileListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "FileListAdapter";

    /*view type header */
    private static final int VIEW_TYPE_HEADER = 0x0;
    /*view type item */
    private static final int VIEW_TYPE_ITEM = 0x1;

    private List<FileEntity> mData;


    public FileListAdapter(){
    }

    public void setData(List<FileEntity> data){
        this.mData = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == VIEW_TYPE_HEADER){
            View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_header, parent, false);
            return new HeaderViewHolder(headerView);
        }else{
            return new FileViewHolder(new FileItemView(parent.getContext()));
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(position == 0){
            return VIEW_TYPE_HEADER;
        }else{
            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {


        if(position == 0 && holder instanceof HeaderViewHolder){
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.tvAddress.setText(NetworkUtils.getIPAddress(holder.itemView.getContext()) + ":9999");
            headerHolder.ivQRCode.setImageBitmap(QRCodeUtils.createQRCode("http://" + headerHolder.tvAddress.getText().toString(), 200));
        }else if(holder instanceof FileViewHolder){
            final FileViewHolder itemHolder = (FileViewHolder) holder;
            final FileEntity entity = mData.get(position);
            itemHolder.itemView.setFileName(entity.fileName);
            itemHolder.itemView.setFileSize(String.valueOf(FileSizeUtil.formatFileSize(entity.fileSize)));
            itemHolder.itemView.setThumbnail(R.drawable.ic_launcher_background);
            itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick filePath = " + entity.filePath);
                    Intent intent = OpenFileUtil.openFile(itemHolder.itemView.getContext(), entity.filePath);
                    holder.itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (mData == null) ? 0 : mData.size();
    }

    class FileViewHolder extends RecyclerView.ViewHolder{

        FileItemView itemView;

        public FileViewHolder(@NonNull FileItemView itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder{

        TextView tvAddress;
        ImageView ivQRCode;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tv_address);
            ivQRCode = itemView.findViewById(R.id.iv_qrcode_address);
        }
    }
}
