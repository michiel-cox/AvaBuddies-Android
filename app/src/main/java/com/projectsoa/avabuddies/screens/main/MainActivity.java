package com.projectsoa.avabuddies.screens.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.gms.tasks.TaskCompletionSource;
import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.base.BaseActivity;
import com.projectsoa.avabuddies.core.base.BaseFragment;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.screens.login.LoginActivity;
import com.projectsoa.avabuddies.screens.main.nearby.NearbyFragment;
import com.projectsoa.avabuddies.screens.main.profile.ProfileFragment;
import com.projectsoa.avabuddies.screens.main.qrread.QRReadFragment;
import com.projectsoa.avabuddies.screens.main.search.SearchFragment;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import butterknife.BindView;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.PublishSubject;

public class MainActivity extends BaseActivity  {

    @Inject
    protected LoginRepository loginRepository;

    protected MainViewModel viewModel;

    @BindView(R.id.frame_container)
    protected FrameLayout frameLayout;
    protected BaseFragment fragment;

    @Override
    protected int layoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = getViewModel(MainViewModel.class);
        if (!loginRepository.isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        loadFragment(new ProfileFragment());
    }

    public void loadFragment(BaseFragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .runOnCommit(() -> this.fragment = fragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (fragment != null) {
            fragment.onBackPressed();
        }
    }

    public void onClickProfile(MenuItem item) {
        loadFragment(new ProfileFragment());
    }

    public void onClickSearch(MenuItem item) {
        loadFragment(new SearchFragment());
    }

    public void onClickNearby(MenuItem item) {
        loadFragment(new NearbyFragment());
    }
    public void onClickQR(MenuItem item) {
        loadFragment(new QRReadFragment());
    }
}
