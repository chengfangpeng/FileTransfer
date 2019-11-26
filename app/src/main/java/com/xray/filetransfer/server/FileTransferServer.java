package com.xray.filetransfer.server;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Created by cfp on 2019-11-20.
 */
public class FileTransferServer extends NanoHTTPD {

    private static final String TAG = "FileTransferServer";

    private Context mContext;

    private AssetManager mAssetManager;

    private ServerCallback mServerCallback;

    public FileTransferServer(Context context, int port) {
        super(port);
        this.mContext = context;
        this.mAssetManager = context.getAssets();
    }

    @Override
    public Response serve(IHTTPSession session) {

        //解决中文乱码问题
        ContentType ct = new ContentType(session.getHeaders().get("content-type")).tryUTF8();
        session.getHeaders().put("content-type", ct.getContentTypeHeader());

        HashMap<String, String> values = new HashMap<>();
        values.put("title", "chengfangpeng");
        values.put("header_logo", "/image/icon.png");
        values.put("header", "chengfangpeng");

        Log.d(TAG, "save path = " + System.getProperty("java.io.tmpdir"));

        Map<String, String> files = new HashMap<>();
        Method method = session.getMethod();

        if (Method.PUT.equals(method) || Method.POST.equals(method)) {
            try {
                session.parseBody(files);
                for (Map.Entry<String, String> entry : session.getParms().entrySet()) {
                    Log.d(TAG, "paramsKey = " + entry.getKey() + " paramsValue = " + entry.getValue());
                }
            } catch (IOException ioe) {
                return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
            } catch (ResponseException re) {
                return newFixedLengthResponse(re.getStatus(), NanoHTTPD.MIME_PLAINTEXT, re.getMessage());
            }
        }

        boolean uploadFile = false;
        for (Map.Entry<String, String> entry : files.entrySet()) {
            final String fileKey = entry.getKey();
            Log.d(TAG, "file key = " + fileKey + " file value = " + files.get(fileKey));
            if (fileKey.startsWith("upload")) {
                uploadFile = true;
                final String tmpFilePath = files.get(fileKey);
                Log.d(TAG, "tmpFilePath = " + tmpFilePath);
                final File tmpFile = new File(tmpFilePath);
                final File targetDir = new File(mContext.getExternalCacheDir() + "/upload/");
                if(!targetDir.exists()){
                    targetDir.mkdir();
                }
                final File targetFile = new File(targetDir, session.getParms().get(fileKey));
                copyFile(tmpFile, targetFile);
            }
        }

        if (uploadFile) {
            if (mServerCallback != null) {
                mServerCallback.uploadSuccess();
            }
            return newFixedLengthResponse("Success");
        }
        return newFixedLengthResponse(applyPattern(getFieldPattern(), readHomeFile("home.html"), values));
    }

    /**
     * 读取首页html
     *
     * @param fileName
     * @return
     */
    private String readHomeFile(String fileName) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            InputStream inputStream = mAssetManager.open(fileName);
            int len;
            while ((len = inputStream.read()) != -1) {
                stream.write(len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stream.toString();
    }

    private static Pattern getFieldPattern() {
        return Pattern.compile("\\$\\{([a-zA-Z_]+)\\}");
    }

    private static String applyPattern(Pattern pattern, String template, Map<String, String> values) {

        StringBuilder builder = new StringBuilder();
        Matcher matcher = pattern.matcher(template);
        int previousLocation = 0;

        while (matcher.find()) {
            builder.append(template, previousLocation, matcher.start());
            builder.append(values.get(matcher.group(1)));
            previousLocation = matcher.end();
            Log.d(TAG, "buider = " + builder.toString());
        }

        if (previousLocation > -1 && previousLocation < template.length())
            builder.append(template, previousLocation, template.length());

        return builder.toString();
    }

    private void copyFile(File source, File dest) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 设置service callback
     *
     * @param callback
     */
    public void setServerCallback(ServerCallback callback) {
        this.mServerCallback = callback;
    }


    public interface ServerCallback {

        /**
         * 上传文件成功
         */
        void uploadSuccess();
    }
}
