package com.xray.filetransfer.ui.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xray.filetransfer.R;
import com.xray.filetransfer.entity.FileEntity;
import com.xray.filetransfer.ui.FileListAdapter;
import com.xray.filetransfer.ui.decoration.FileListItemDecoration;
import com.xray.filetransfer.utils.FileSizeUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FileListFragment extends Fragment {


    private static final String TAG = "FileListFragment";

    private RecyclerView mRvFileList;

    private FileListAdapter mAdapter;


    public FileListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_list, container, false);
        mRvFileList = view.findViewById(R.id.rv_file_list);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRv();
        fetchData();
    }

    private void initRv() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvFileList.setLayoutManager(layoutManager);
        mRvFileList.addItemDecoration(new FileListItemDecoration());
    }

    private void fetchData() {
        getContext().getCacheDir();
        File file = new File(getContext().getCacheDir() + "/upload/");
        if(file.exists()) {
            File[] filterFiles = file.listFiles();
            List<FileEntity> fileList = new ArrayList<>(filterFiles.length);
            for (int i = 0; i < filterFiles.length; i++) {
                FileEntity entity = new FileEntity();
                entity.fileName = filterFiles[i].getName();
                entity.fileThumbnail = filterFiles[i].getPath();
                entity.fileSize = filterFiles[i].length();
                Log.d(TAG, "entity = " + entity.toString());
                fileList.add(entity);
            }
            mAdapter = new FileListAdapter(fileList);
            mRvFileList.setAdapter(mAdapter);
        }
    }
}
