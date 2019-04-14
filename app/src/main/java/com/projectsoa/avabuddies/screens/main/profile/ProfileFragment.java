package com.projectsoa.avabuddies.screens.main.profile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.base.BaseFragment;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.data.repositories.UserRepository;
import com.projectsoa.avabuddies.screens.login.LoginActivity;
import com.projectsoa.avabuddies.screens.main.MainActivity;
import com.projectsoa.avabuddies.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends BaseFragment implements View.OnClickListener {

    protected User user;
    protected ProfileViewModel viewModel;
    @Inject
    protected LoginRepository loginRepository;
    @Inject
    protected UserRepository userRepository;
    @Inject
    protected Utils utils;

    @BindView(R.id.name)
    protected TextView name;
    @BindView(R.id.email)
    protected TextView email;
    @BindView(R.id.info)
    protected TextView info;
    @BindView(R.id.full_name)
    protected TextView fullName;
    @BindView(R.id.profile)
    protected ImageView profile;
    @BindView(R.id.location)
    protected Switch location;

    private static final int SELECT_IMAGE = 1000;
    private static final int CAMERA = 2000;
    private CharSequence options[];
    private Bitmap bitmap;


    public ProfileFragment() {
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_profile;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.user = loginRepository.getLoggedInUser().getUser();

        profile = view.findViewById(R.id.profile);

        if (!user.getImage().isEmpty()) {
            byte[] imageByteArray = Base64.decode(user.getImage(), Base64.DEFAULT);
            profile.setImageBitmap(BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length));
        }

        profile.setOnClickListener(this);

        // Set values of user account
        location.setChecked(user.isSharelocation());
        location.setClickable(false);
        name.setText(user.getName());
        fullName.setText(user.getName());
        email.setText(user.getEmail());
        info.setText(user.getAboutme());

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = getViewModel(ProfileViewModel.class);
    }


    @OnClick(R.id.btn_logout)
    public void logout() {
        Intent intent = new Intent(getBaseActivity(), LoginActivity.class);
        intent.putExtra("logout", true);
        startActivity(intent);
    }


    @OnClick(R.id.updateThisUser)
    public void goToUpdate() {
        ((MainActivity) getActivity()).loadFragment(new ProfileChangeFragment());
    }

    public void showDialog() {
        options = new CharSequence[]{"Gallery", "Camera"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setTitle("Select your option:");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on options[which]
                if (options[which] == "Gallery") {
                    openImageChooser();
                }
                if (options[which] == "Camera") {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA);
                    } else {
                        openCamera();
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //the user clicked on Cancel
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs access to the storage");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DON'T ALLOW", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //user will be redirected to settings on phone
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                openAppSettings(getActivity());
            }
        });
        alertDialog.show();
    }

    private void openAppSettings(final Activity context) {
        if (context == null) {
            return;
        }
        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), SELECT_IMAGE);
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_IMAGE);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onClick(View v) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, SELECT_IMAGE);
        } else {
            showDialog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == SELECT_IMAGE || requestCode == CAMERA) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission);
                    if (showRationale) {
                        //show your own message
                    } else {
                        //User tapped never ask again
                        showSettingsAlert();
                    }
                }
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (resultCode == RESULT_OK) {
                    if (requestCode == SELECT_IMAGE) {
                        final Uri selectedImageUri = data.getData();
                        if (null != selectedImageUri) {
                            profile.post(new Runnable() {
                                @Override
                                public void run() {
                                    Bitmap bitmapLocal = null;
                                    try {
                                        bitmapLocal = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    saveData(bitmapLocal);
                                    bitmap = bitmapLocal;
                                    profile.setImageBitmap(bitmapLocal);
                                }
                            });
                        }
                    }
                    if (requestCode == CAMERA) {
                        profile.post(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap bitmap1 = (Bitmap) data.getExtras().get("data");
                                profile.setImageBitmap(bitmap1);
                                saveData(bitmap1);
                                bitmap = bitmap1;
                            }

                        });
                    }
                }
            }
        }).start();
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void saveData(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encoded = Base64.encodeToString(b, Base64.DEFAULT);

        user.setImage(encoded);
        userRepository.updateProfilePicture(user).subscribe(() -> {
                },
                throwable -> getActivity().runOnUiThread(() -> utils.showToastError(getString(R.string.something_went_wrong))));
    }
}
