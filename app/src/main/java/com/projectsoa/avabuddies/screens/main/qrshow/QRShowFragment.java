package com.projectsoa.avabuddies.screens.main.qrshow;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.projectsoa.avabuddies.Constants;
import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.base.BaseFragment;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.models.responses.friend.FriendResponse;
import com.projectsoa.avabuddies.data.repositories.FriendRepository;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.data.repositories.UserRepository;
import com.projectsoa.avabuddies.screens.main.MainActivity;
import com.projectsoa.avabuddies.screens.main.publicprofile.PublicProfileFragment;
import com.projectsoa.avabuddies.utils.Utils;

import org.parceler.Parcels;
import org.reactivestreams.Subscription;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class QRShowFragment extends BaseFragment {

    private static final String ARG_USER = "user";
    private User user;
    private User currentUser;

    @Inject
    protected LoginRepository loginRepository;
    @Inject
    protected UserRepository userRepository;
    @Inject
    protected FriendRepository friendRepository;
    @Inject
    protected Utils utils;

    @BindView(R.id.textTitle)
    protected TextView textTitle;
    @BindView(R.id.textSubtitle)
    protected TextView textSubtitle;
    @BindView(R.id.imageQR)
    protected ImageView imageQR;

    protected Disposable qrInterval;
    protected Disposable statusInterval;

    public QRShowFragment() {

    }


    @Override
    protected int layoutRes() {
        return R.layout.fragment_qrshow;
    }


    public static QRShowFragment newInstance(User user) {
        QRShowFragment fragment = new QRShowFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, Parcels.wrap(user));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = Parcels.unwrap(getArguments().getParcelable(ARG_USER));
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentUser = loginRepository.getLoggedInUser().getUser();
        textTitle.setText(String.format("Friend request from %s", user.getName()));
        textSubtitle.setText(String.format("Let %s scan this QR code.", user.getName()));
        qrInterval = Observable.interval(Constants.QR_VALID_SECONDS / 2, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
                .map(l -> createQRValue(new Date(), currentUser))
                .map(this::createQRImage)
                .subscribe(bitmap -> {
                    imageQR.setImageBitmap(bitmap);
                },throwable -> {
                    utils.showToastError(getString(R.string.something_went_wrong));
                });

        imageQR.setImageBitmap(createQRImage(createQRValue(new Date(), currentUser)));

        statusInterval =  Observable.interval(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapSingle(l -> friendRepository.getConnectionStatus(user.getId()))
                .subscribe(this::pollConnectionStatus, throwable -> {
                    utils.showToastError(getString(R.string.something_went_wrong));
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(qrInterval != null) {
            qrInterval.dispose();
        }
        if(statusInterval != null) {
            statusInterval.dispose();
        }
    }


    private void pollConnectionStatus(FriendRepository.ConnectionStatus connectionStatus){
        switch(connectionStatus){
            case VALIDATED:
                statusInterval.dispose();
                accept();
                break;
        }
    }
    private void accept(){
        friendRepository.acceptRequest(user.getId())
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(() -> {
            utils.showToastMessage(getString(R.string.friend_request_accept));
            onBackPressed();
        }, throwable -> utils.showToastError(getString(R.string.something_went_wrong)));
    }

    public static String createQRValue(Date now, User friend){
        String dateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(now);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("dateTime", dateTime);
        jsonObject.addProperty("id", friend.getId());
        return jsonObject.toString();
    }

    private Bitmap createQRImage(String rawValue){
        QRGEncoder qrgEncoder = new QRGEncoder(rawValue, null, QRGContents.Type.TEXT,500);

        try {
            return qrgEncoder.encodeAsBitmap();
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public boolean onBackPressed() {
        ((MainActivity) getActivity()).loadFragment(PublicProfileFragment.newInstance(user));
        return true;
    }

    public User getUser() {
        return user;
    }
}
