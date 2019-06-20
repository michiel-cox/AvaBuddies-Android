package com.projectsoa.avabuddies.screens.main.qrread;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.base.BaseFragment;
import com.projectsoa.avabuddies.data.models.LoggedInUser;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.repositories.FriendRepository;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.data.repositories.UserRepository;
import com.projectsoa.avabuddies.screens.main.MainActivity;
import com.projectsoa.avabuddies.screens.main.publicprofile.PublicProfileFragment;
import com.projectsoa.avabuddies.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class QRReadFragment extends BaseFragment {


    @BindView(R.id.scanner_view)
    protected SurfaceView scannerView;

    @Inject
    protected Utils utils;

    @Inject
    protected LoginRepository loginRepository;

    @Inject
    protected UserRepository userRepository;

    @Inject
    protected FriendRepository friendRepository;

    protected BarcodeDetector barcodeDetector;
    protected CameraSource cameraSource;

    private User user;
    private boolean cameraEnabled;

    public QRReadFragment() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    protected int layoutRes() {
        return R.layout.fragment_qrread;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        barcodeDetector = new BarcodeDetector.Builder(getContext()).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                .setAutoFocusEnabled(true)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(15)
                .build();


        scannerView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @SuppressLint("MissingPermission")
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(getBaseActivity().requestPermissions(new String[]{Manifest.permission.CAMERA}, "The camera is needed to scan QR codes.")) {
                    try {
                        cameraSource.start(holder);
                        cameraEnabled = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                if(!cameraEnabled) return;
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                for (int i = 0; i < barcodes.size(); i++) {
                    boolean success = tryQRCode(loginRepository.getLoggedInUser().getUser(),barcodes.valueAt(0));
                    if (success || !cameraEnabled) return;
                }
            }
        });
    }


    private boolean tryQRCode(User loggedInUser,Barcode barcode) {
        try {
            JSONObject jsonObject = new JSONObject(barcode.rawValue);
            String dateTimeString = jsonObject.getString("dateTime");
            String friendId = jsonObject.getString("id");
            Date dateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dateTimeString);
            validate(loggedInUser,friendId, dateTime);
            return true;
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void validate(User loggedInUser, String friendId, Date dateTime){
        cameraEnabled = false;

        // Receive ConnectionStatus with friend, after that validate
        friendRepository.getConnectionStatus(friendId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                connectionStatus -> {
                    if(validConnectionStatus(connectionStatus)){
                        validateAndReturnUser(loggedInUser, friendId, dateTime).subscribe(user -> {
                            showSuccess(user);
                        }, throwable -> {
                            cameraEnabled = true;
                            utils.showToastError(getString(R.string.qr_invalid));
                        });
                    }else{
                        cameraEnabled = true;
                    }
                },
                throwable -> {
                    cameraEnabled = true;
                    utils.showToastError(getString(R.string.something_went_wrong));
                });
    }

    // Check if the user is allowed to read this code.
    private boolean validConnectionStatus(FriendRepository.ConnectionStatus connectionStatus){
        switch(connectionStatus){
            case SEND:
                return true;
            case RECEIVED:
                utils.showToastError(getString(R.string.qr_error_received));
                break;
            case ACCEPTED:
                utils.showToastError(getString(R.string.qr_error_accepted));
                break;
            case VALIDATED:
                utils.showToastError(getString(R.string.qr_error_validated));
                break;
            case UNKNOWN:
                utils.showToastError(getString(R.string.qr_error_unknown));
                break;
        }
        return false;
    }

    // Tries to validate the request,
    // on success: showSuccess,
    // else it will show an error.
    private Single<User> validateAndReturnUser(User loggedInUser, String friendId, Date dateTime){
        return friendRepository
            .isValidRequest(loggedInUser, friendId, dateTime, new Date())
            .andThen(friendRepository.validateRequest(friendId))
            .andThen(userRepository.getUser(friendId))
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread());
    }

    private void showSuccess(User friend){
        new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom))
                .setTitle(getString(R.string.success))
                .setMessage(String.format(getString(R.string.request_qr_validated), friend.getName()))
                .setPositiveButton(getString(R.string.positive_button), (dialog, which) -> {
                    dialog.dismiss();
                })
                .create()
                .show();
    }

}
