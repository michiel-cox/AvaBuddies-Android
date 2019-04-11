package com.projectsoa.avabuddies.screens.register;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Switch;

import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.base.BaseActivity;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.screens.login.LoginActivity;
import com.projectsoa.avabuddies.screens.login.LoginViewModel;
import com.projectsoa.avabuddies.screens.main.MainActivity;
import com.projectsoa.avabuddies.utils.Utils;

import javax.inject.Inject;

public class RegisterActivity extends BaseActivity {


    @Inject
    protected LoginRepository loginRepository;

    @Inject
    protected Utils utils;

    private LoginViewModel viewModel;
    private String email;
    private String name;

    @BindView(R.id.switch_location)
    protected Switch switchLocation;

    @BindView(R.id.switch_terms)
    protected Switch switchTerms;


    @Override
    protected int layoutRes() {
        return R.layout.activity_register;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        email = getIntent().getStringExtra("email");
        name = getIntent().getStringExtra("name");
        viewModel = getViewModel(LoginViewModel.class);
    }

    @OnClick(R.id.btn_register)
    public void register(){
        if(!switchTerms.isChecked())
        {
            utils.showToastError(getString(R.string.error_terms_and_conditions));
            return;
        }
        loginRepository.register(email, name, switchLocation.isChecked()).subscribe(() ->  {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }, Throwable::printStackTrace);
    }
}
