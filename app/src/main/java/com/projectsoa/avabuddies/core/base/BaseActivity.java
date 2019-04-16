package com.projectsoa.avabuddies.core.base;

import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;

import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.dagger.utils.ViewModelFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import icepick.Icepick;
import io.reactivex.Completable;
import io.reactivex.subjects.CompletableSubject;

public abstract class BaseActivity extends DaggerAppCompatActivity {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private int nextRequestCode = 0;

    @LayoutRes
    protected abstract int layoutRes();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutRes());
        ButterKnife.bind(this);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    /**
     * Returns an instance of a view model
     */
    public <T extends ViewModel> T getViewModel(Class<T> viewModelClass) {
        return ViewModelProviders.of(this, viewModelFactory).get(viewModelClass);
    }

    /**
     * Required for EventBus (Throws an error when there are no Subscribed events.)
     */
    @Subscribe
    public void dummyEvent(BaseActivity event) {

    }

    public boolean requestPermissions(String[] permissions, String explanation){

        List<String> permissionsNeeded = new ArrayList<>();
        boolean shouldExplain = false;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(permission);
                shouldExplain = shouldExplain || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
            }
        }

        if (permissionsNeeded.isEmpty()) return true;

        if (shouldExplain) {
            new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom))
                    .setTitle(getString(R.string.permission_title))
                    .setMessage(explanation)
                    .setPositiveButton(getString(R.string.positive_button), (dialog, which) -> {
                        ActivityCompat.requestPermissions(this, permissionsNeeded.toArray(new String[0]), nextRequestCode++);
                    })
                    .setNegativeButton(getString(R.string.negative_button), (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create()
                    .show();

        } else {
            ActivityCompat.requestPermissions(this, permissionsNeeded.toArray(new String[0]), nextRequestCode++);
        }

        return false;
    }
}