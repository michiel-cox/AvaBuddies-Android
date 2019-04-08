package com.projectsoa.avabuddies.core.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projectsoa.avabuddies.core.dagger.utils.ViewModelFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

public abstract class BaseFragment extends DaggerFragment {

    @Inject
    protected ViewModelFactory viewModelFactory;
    private BaseActivity mActivity;
    private Unbinder mUnbinder;

    @LayoutRes
    protected abstract int layoutRes();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutRes(), container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
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
    public void onDestroyView() {
        super.onDestroyView();
        if(mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
    }

    // Return whether or it has been handled.
    public boolean onBackPressed(){
        return false;
    }

    @Subscribe
    public void dummyEvent(BaseActivity event){  }


    public BaseActivity getBaseActivity() {
        return mActivity;
    }


    public <T extends ViewModel> T getViewModel(Class<T> viewModelClass){
        return ViewModelProviders.of(this, viewModelFactory).get(viewModelClass);
    }

}