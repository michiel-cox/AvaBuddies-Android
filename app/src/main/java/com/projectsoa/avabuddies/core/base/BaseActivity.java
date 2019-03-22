package com.projectsoa.avabuddies.core.base;

import android.os.Bundle;

import com.projectsoa.avabuddies.core.dagger.utils.ViewModelFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import icepick.Icepick;

public abstract class BaseActivity  extends DaggerAppCompatActivity {

    @Inject
    protected ViewModelFactory viewModelFactory;

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
    public <T extends ViewModel> T getViewModel(Class<T> viewModelClass){
        return ViewModelProviders.of(this, viewModelFactory).get(viewModelClass);
    }

    /**
     *   Required for EventBus (Throws an error when there are no Subscribed events.)
     */
    @Subscribe
    public void dummyEvent(BaseActivity event){  }

}