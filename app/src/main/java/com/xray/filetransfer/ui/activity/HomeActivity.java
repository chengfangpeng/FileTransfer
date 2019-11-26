package com.xray.filetransfer.ui.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xray.filetransfer.R;
import com.xray.filetransfer.entity.FileEntity;
import com.xray.filetransfer.server.FileTransferServer;
import com.xray.filetransfer.ui.FileListAdapter;
import com.xray.filetransfer.ui.decoration.FileListItemDecoration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private RecyclerView mRvFileList;

    private FileListAdapter mAdapter;

    private FileTransferServer mServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mRvFileList = findViewById(R.id.rv_file_list);
        initRv();
        fetchData();
        startFileTransferServer();
    }


    private void startFileTransferServer() {
        mServer = new FileTransferServer(this, 9999);
        mServer.setServerCallback(new FileTransferServer.ServerCallback() {
            @Override
            public void uploadSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fetchData();
                    }
                });
            }
        });
        try {
            mServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initRv() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvFileList.setLayoutManager(layoutManager);
        mRvFileList.addItemDecoration(new FileListItemDecoration());
        mAdapter = new FileListAdapter();
        FileEntity headerEntity = new FileEntity();
        List<FileEntity> fileList = new ArrayList<>();
        fileList.add(0, headerEntity);
        mAdapter.setData(fileList);
        mRvFileList.setAdapter(mAdapter);
    }

    private void fetchData() {
        File file = new File(getExternalCacheDir() + "/upload/");
        if (file.exists()) {
            File[] filterFiles = file.listFiles();
            List<FileEntity> fileList = new ArrayList<>();
            for (int i = 0; i < filterFiles.length; i++) {
                FileEntity entity = new FileEntity();
                entity.fileName = filterFiles[i].getName();
                entity.fileThumbnail = filterFiles[i].getPath();
                entity.fileSize = filterFiles[i].length();
                entity.filePath = filterFiles[i].getAbsolutePath();
                Log.d(TAG, "entity = " + entity.toString());
                fileList.add(entity);
            }
            FileEntity headerEntity = new FileEntity();
            fileList.add(0, headerEntity);
            if(mAdapter != null){
                mAdapter.setData(fileList);
            }

        }
    }

    @Override
    protected void onDestroy() {
        if (mServer != null) {
            mServer.stop();
        }
        super.onDestroy();
    }
}
