package com.shouman.apps.hawk.ui.auth;


import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.shouman.apps.hawk.databinding.FragmentScanQrBinding;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.io.IOException;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Scan_QR extends Fragment {

    private static final String TAG = "Fragment_Scan_QR";
    private FragmentScanQrBinding mBinding;
    private AuthViewModel authViewModel;
    private boolean detectionCaptured = false;


    public Fragment_Scan_QR() {
        // Required empty public constructor
    }

    public static Fragment_Scan_QR getInstance() {
        return new Fragment_Scan_QR();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentScanQrBinding.inflate(inflater);

        initViewModel();
        createCameraSource();
        return mBinding.getRoot();
    }

    private void initViewModel() {
        authViewModel = new ViewModelProvider(getHostActivity()).get(AuthViewModel.class);
    }

    private StartingActivity getHostActivity() {
        return (StartingActivity) getActivity();
    }


    private void createCameraSource() {
        mBinding.surfaceView.setZOrderMediaOverlay(true);
        BarcodeDetector barcodeDetector = new BarcodeDetector
                .Builder(getContext())
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        final CameraSource cameraSource = new CameraSource.Builder(getHostActivity(), barcodeDetector)
                .setAutoFocusEnabled(true)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(640, 480)
                .build();

        mBinding.surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                if (!detectionCaptured) {
                    SparseArray<Barcode> barCodes = detections.getDetectedItems();
                    if (barCodes.size() > 0) {
                        detectionCaptured = true;
                        Vibrator vibrator = (Vibrator) getHostActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        Objects.requireNonNull(vibrator).vibrate(100);
                        authViewModel.setBarCodesArray(barCodes.valueAt(0));
                        finishScanFragment();
                    }
                }
            }
        });

    }

    private void finishScanFragment() {
        //getHostActivity().fragmentManager.popBackStack("qr_scan", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

}
