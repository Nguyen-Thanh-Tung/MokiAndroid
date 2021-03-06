package com.coho.moki.ui.splash;

import android.content.Intent;
import android.util.Log;

import com.coho.moki.BaseApp;
import com.coho.moki.R;
import com.coho.moki.ui.base.BaseActivity;
import com.coho.moki.ui.login.LoginActivity;
import com.coho.moki.ui.main.MainActivity;
import com.coho.moki.ui.start_tutorial.StartTutorialActivity2;
import com.coho.moki.util.AccountUntil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import javax.inject.Inject;

/**
 * Created by trung on 22/09/2017.
 */

public class SplashActivity extends BaseActivity implements SplashView {

    @Inject
    SplashPresenter mSplashPresenter;

    @Override
    public int setContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        if (AccountUntil.isPassTutorialScreen()){
            BaseApp.getActivityComponent().inject(this);
            mSplashPresenter.onAttach(this);
            mSplashPresenter.checkUserLogin();
        } else {
            Intent intent = new Intent(BaseApp.getContext(), StartTutorialActivity2.class);
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public void initData() {
    }

    @Override
    public void openLoginActivity() {
        Intent intent = new Intent(BaseApp.getContext(), LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void openMainActivity() {
        Intent intent = new Intent(BaseApp.getContext(), MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
