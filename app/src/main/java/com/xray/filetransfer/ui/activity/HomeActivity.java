package com.xray.filetransfer.ui.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.xray.filetransfer.R;
import com.xray.filetransfer.server.FileTransferServer;
import com.xray.filetransfer.ui.fragment.FileListFragment;
import com.xray.filetransfer.ui.fragment.HomeFragment;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private FragmentTransaction mFragmentTransaction;

    private HomeFragment mHomeFragment;

    private FileListFragment mFileListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initFragment();
        startFileTransferServer();
    }

    private void initFragment() {
        mHomeFragment = new HomeFragment();
        mFileListFragment = new FileListFragment();
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.fl_home, mHomeFragment);
        mFragmentTransaction.commit();


    }

    private void startFileTransferServer() {
        FileTransferServer server = new FileTransferServer(this, 9999);
        server.setServerCallback(new FileTransferServer.ServerCallback() {
            @Override
            public void uploadSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "start FileListFragment ..." );
                        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        mFragmentTransaction.replace(R.id.fl_home, mFileListFragment);
                        mFragmentTransaction.commit();
                    }
                });

            }
        });
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
