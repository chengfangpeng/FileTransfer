package com.xray.filetransfer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FileTransferService extends Service {
    public FileTransferService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
