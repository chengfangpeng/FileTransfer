package com.xray.filetransfer.ui.fragment;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.encoder.QRCode;
import com.xray.filetransfer.R;
import com.xray.filetransfer.utils.NetworkUtils;

import java.util.EnumMap;
import java.util.Map;

import static android.graphics.Color.BLACK;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private static final String TAG = "HomeFragment";
    private TextView mTvAddress;
    private ImageView mIvQRCode;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mTvAddress = view.findViewById(R.id.tv_address);
        mIvQRCode = view.findViewById(R.id.iv_qrcode_address);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateIpAddress();
        generateQRCode();
    }

    /**
     * 通过地址生产二维码
     */
    private void generateQRCode() {
        try {
            Bitmap bitmap = createQRCode("http://" + mTvAddress.getText().toString(), 300);
            mIvQRCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
            Log.e(TAG, "generate qrcode fail");
        }

    }

    private void updateIpAddress() {
        mTvAddress.setText(NetworkUtils.getIPAddress(getContext()) + ":9999");
    }

    public static Bitmap createQRCode(String str, int widthAndHeight) throws WriterException {
        String contentsToEncode = str;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        //hints.put(EncodeHintType.CHARACTER_SET, encoding);
        hints.put(EncodeHintType.MARGIN, 0); /* default = 4 */
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, BarcodeFormat.QR_CODE , widthAndHeight, widthAndHeight, hints);
        } catch (Exception e) {
            // Unsupported format
            e.printStackTrace();
            return null;
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : Color.WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
