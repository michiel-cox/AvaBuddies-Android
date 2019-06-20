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
import androidx.core.content.FileProvider;

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
import com.projectsoa.avabuddies.screens.main.challenge.ChallengeFragment;
import com.projectsoa.avabuddies.screens.main.friends.FriendsFragment;
import com.projectsoa.avabuddies.screens.main.tag.TagsFragment;
import com.projectsoa.avabuddies.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

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

        if (user.getImage() != null && !user.getImage().isEmpty()) {
            byte[] imageByteArray = Base64.decode(user.getImage(), Base64.DEFAULT);
            profile.setImageBitmap(BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length));
        } else {
            byte[] imageByteArray = Base64.decode("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"
, Base64.DEFAULT);
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

    @OnClick(R.id.showFriends)
    public void goToFriends(){
        ((MainActivity) getActivity()).loadFragment(new FriendsFragment());
    }
    @OnClick(R.id.challenges)
    public void goToChallenges(){
        ((MainActivity) getActivity()).loadFragment(new ChallengeFragment());
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
                                Bitmap resized = cropResizeBitmap(bitmap);
                                saveBitmap(resized);
                                profile.setImageBitmap(resized);
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

                        Bitmap resized = cropResizeBitmap(bitmap);
                        saveBitmap(resized);
                        profile.setImageBitmap(resized);
                    });
                }
            }
        }).start();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Bitmap cropResizeBitmap(Bitmap srcBmp) {
        Bitmap croppedBmp;
        if (srcBmp.getWidth() >= srcBmp.getHeight()){

            croppedBmp = Bitmap.createBitmap(
                    srcBmp,
                    srcBmp.getWidth()/2 - srcBmp.getHeight()/2,
                    0,
                    srcBmp.getHeight(),
                    srcBmp.getHeight()
            );

        }else{

            croppedBmp = Bitmap.createBitmap(
                    srcBmp,
                    0,
                    srcBmp.getHeight()/2 - srcBmp.getWidth()/2,
                    srcBmp.getWidth(),
                    srcBmp.getWidth()
            );
        }

        croppedBmp = Bitmap.createScaledBitmap(croppedBmp, 300, 300, true);
        return croppedBmp;
    }

    private void saveBitmap(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String encoded = Base64.encodeToString(b, Base64.DEFAULT);
        user.setImage(encoded);
        userRepository.updateProfilePicture(user).subscribe(() -> {
                },
                throwable -> getActivity().runOnUiThread(() -> utils.showToastError(getString(R.string.something_went_wrong))));
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
    }

}
