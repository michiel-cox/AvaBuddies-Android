package com.projectsoa.avabuddies.screens.main.profile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.base.BaseFragment;
import com.projectsoa.avabuddies.data.models.Tag;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.data.repositories.UserRepository;
import com.projectsoa.avabuddies.screens.login.LoginActivity;
import com.projectsoa.avabuddies.screens.main.MainActivity;
import com.projectsoa.avabuddies.screens.main.tag.TagsFragment;
import com.projectsoa.avabuddies.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.FileProvider;
import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends BaseFragment {

    protected User user;
    @Inject
    protected LoginRepository loginRepository;
    @Inject
    protected UserRepository userRepository;
    @Inject
    protected Utils utils;

    @BindView(R.id.chip_group)
    protected ChipGroup chipGroup;
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
    @BindView(R.id.privacy)
    protected Switch privacy;

    private static final int SELECT_IMAGE = 1000;
    private static final int CAMERA = 2000;

    private String currentPhotoPath;
    private CharSequence options[];



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
        if(Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        }
        profile.setOnClickListener((View v) -> {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, SELECT_IMAGE);
            } else {
                showDialog();
            }
        });

        // Set values of user account
        location.setChecked(user.isSharelocation());
        location.setClickable(false);
        privacy.setChecked(user.isShareprofile());
        privacy.setClickable(false);
        name.setText(user.getName());
        fullName.setText(user.getName());
        email.setText(user.getEmail());
        info.setText(user.getAboutme());

        for (Tag tag : user.getTags()) {
            Chip chip = new Chip(getContext());
            chip.setText(tag.getName());
            chip.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark, null));
            chip.setChipIcon(null);
            chipGroup.addView(chip);
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @OnClick(R.id.tags)
    public void goToTags() {
        ((MainActivity) getActivity()).loadFragment(new TagsFragment());
    }

    private void showDialog() {
        options = new CharSequence[]{"Gallery", "Camera"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setTitle("Select your option:");
        builder.setItems(options, (dialog, which) -> {
            // the user clicked on options[which]
            if (options[which] == "Gallery") {
                openImageChooser();
            }
            if (options[which] == "Camera") {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA);
                } else {
                    dispatchPictureTakerAction();
                }
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_IMAGE);
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

    private void dispatchPictureTakerAction() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (getActivity() != null && takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("mylog", "Excep: " + ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.projectsoa.avabuddies.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        //create an image file name
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PNG_" + timestamp + "_";
        File storageDir = null;
        if (getActivity() != null) {
            storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }
        File image = File.createTempFile(imageFileName, ".png", storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void showSettingsAlert() {
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
        if (context != null) {
            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), SELECT_IMAGE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        new Thread(() -> {
            if (resultCode == RESULT_OK) {
                if (requestCode == SELECT_IMAGE) {
                    final Uri selectedImageUri = data.getData();
                    if (null != selectedImageUri) {
                        profile.post(() -> {
                            Bitmap bitmap = null;
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (bitmap != null) {
                                profile.setImageBitmap(saveData(bitmap));
                            }
                        });
                    }
                }
                if (requestCode == CAMERA) {
                    profile.post(() -> {
                        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);

                        ExifInterface ei;
                        try {
                            ei = new ExifInterface(currentPhotoPath);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            return;
                        }
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                bitmap = rotateImage(bitmap, 90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                bitmap = rotateImage(bitmap, 180);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                bitmap = rotateImage(bitmap, 270);
                                break;
                            default:
                                break;
                        }
                        profile.setImageBitmap(saveData(bitmap));
                    });
                }
            }
        }).start();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Bitmap saveData(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm = Bitmap.createScaledBitmap(bm, 300, 300, false);
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encoded = Base64.encodeToString(b, Base64.DEFAULT);
        user.setImage(encoded);
        userRepository.updateProfilePicture(user).subscribe(() -> {
                },
                throwable -> getActivity().runOnUiThread(() -> utils.showToastError(getString(R.string.something_went_wrong))));

        return bm;
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
    }

}
