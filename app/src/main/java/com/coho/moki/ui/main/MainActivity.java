package com.coho.moki.ui.main;

import android.Manifest;
import android.animation.Animator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.coho.moki.BaseApp;
import com.coho.moki.R;
import com.coho.moki.adapter.customadapter.NotificationAdapter;
import com.coho.moki.adapter.customadapter.SideMenuAdapter;
import com.coho.moki.callback.OnClickSellListener;
import com.coho.moki.callback.OnClickSideMenuItemListener;
import com.coho.moki.data.constant.AppConstant;
import com.coho.moki.data.constant.SideMenuItem;
import com.coho.moki.service.LogoutService;
import com.coho.moki.service.LogoutServiceImpl;
import com.coho.moki.service.ResponseListener;
import com.coho.moki.ui.base.BaseActivity;
import com.coho.moki.ui.fragment.IntroTutFragment;
import com.coho.moki.ui.fragment.MessageFragment;
import com.coho.moki.ui.fragment.NewsPager.BuyFragment;
import com.coho.moki.ui.fragment.NewsPager.CharityFragment;
import com.coho.moki.ui.fragment.NewsPager.InviteFragment;
import com.coho.moki.ui.fragment.NewsPager.MyLikeFragment;
import com.coho.moki.ui.fragment.NewsPager.NewsPagerFragment;
import com.coho.moki.ui.fragment.NewsPager.SellFragment;
import com.coho.moki.ui.fragment.NewsPager.SettingsFragment;
import com.coho.moki.ui.fragment.NewsPager.SupportFragment;
import com.coho.moki.ui.fragment.NotificationFragment;
import com.coho.moki.ui.login.LoginActivity;
import com.coho.moki.ui.main_search.MainSearchActivity;
import com.coho.moki.ui.product.add.CameraActivity;
import com.coho.moki.ui.start_tutorial.Frame;
import com.coho.moki.ui.user.UserInfoActivity;
import com.coho.moki.util.AccountUntil;
import com.coho.moki.util.DialogUtil;
import com.coho.moki.util.Utils;
import com.coho.moki.util.network.LoadImageUtils;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.coho.moki.ui.fragment.ProductPager.ProductPagerFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainView{

    private static final String TAG = "MainActivity";

    @Inject
    MainPresenter mMainPresenter;

    private Fragment currentDisplayFragment;
    private ProductPagerFragment productPagerFragment;
    private NewsPagerFragment mNewsPagerFragment;
    private SettingsFragment mSettingsFragment;
    private SupportFragment mSupportFragment;
    private InviteFragment mInviteFragment;
    private SellFragment mSellFragment;
    private BuyFragment mBuyFragment;
    private CharityFragment mCharityFragment;
    private MyLikeFragment mMyLikeFragment;

    private int mCurrentMenuIndex = AppConstant.Home_MENU_INDEX;

    public int mViewType = AppConstant.GRID_VIEW_PRODUCT;

//    @BindView(R.id.imgAvatar)
    CircularImageView mImgAvatar;

//    @BindView(R.id.user_name)
    TextView mTxtUserName;

//    @BindView(R.id.side_menu_list)
    RecyclerView mRVSideMenu;

    @BindView(R.id.txtHeader)
    TextView mTxtHeader;

    @BindView(R.id.btnSearch)
    ImageButton mBtnSearch;

    @BindView(R.id.btnAlert)
    View mLLNotiCount;

    @BindView(R.id.layout_message_count)
    View mLLMessageCount;

    @BindView(R.id.btnSwitch)
    public ImageButton mBtnSwitch;

    @BindView(R.id.btnMenu)
    ImageButton mBtnMenu;

    @BindView(R.id.iconAppName)
    View mIconAppName;

    @BindView(R.id.topbar)
    RelativeLayout mTopBar;

    @BindView(R.id.main_content)
    FrameLayout mMainContent;

    @BindView(R.id.main_layout_container)
    FrameLayout mMainLayoutContainer;

    @BindView(R.id.layout_message)
    RelativeLayout mLayoutMessage;

    @BindView(R.id.layout_notification)
    RelativeLayout mLayoutNotification;

    @BindView(R.id.message_fragment)
    FrameLayout mLayoutMessageFragment;

    @BindView(R.id.notification_fragment)
    FrameLayout mLayoutNotificationFragment;

    @BindView(R.id.intro_tut_layout)
    FrameLayout mIntroTutLayout;

    MessageFragment mMsgFragment;
    NotificationFragment mNotificationFragment;

    public IntroTutFragment mIntroTutFragment;

    static final String MESSAGE_FRAGMENT_TAG = "message_fragment";

    int devHeight;
    int heightTopBar;
    int statusBarHeight;

    @OnClick(R.id.btnMenu)
    public void onClickButtonMenu(){
        mSlidingMenu.toggle();
    }

    private static final int PERMISSIONS_REQUEST_CAMERA = 1001;

    @OnClick(R.id.btnSearch)
    public void onClickButtonSearch(){
        Intent intent = new Intent(BaseApp.getContext(), MainSearchActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnChat)
    public void onClickButtonChat(){

        if (AccountUntil.getUserToken() != null){
            showMessageFragment();
        }
        else {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            this.finish();
        }

    }

    @OnClick(R.id.btnAlert)
    public void onClickButtonAlert(){

        if (AccountUntil.getUserToken() != null){
            showNotificationFragment();
        }
        else {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            this.finish();
        }
    }

    @OnClick(R.id.layout_message)
    public void onClickLayoutMessage(){
        hideMessageFragment();
    }

    @OnClick(R.id.layout_notification)
    public void onClickLayoutNotification(){
        hideNotificationFragment();
    }

    @OnClick(R.id.btnSwitch)
    public void onClickBtnSwitch(){

        if (mViewType == AppConstant.GRID_VIEW_PRODUCT){
            mViewType = AppConstant.TIMELINE_VIEW_PRODUCT;
            mBtnSwitch.setImageResource(R.drawable.icon_grid);
        }
        else {
            mViewType = AppConstant.GRID_VIEW_PRODUCT;
            mBtnSwitch.setImageResource(R.drawable.icon_timeline);
        }

        productPagerFragment.changeListProductLayout();
    }

    SlidingMenu mSlidingMenu;

    @Override
    public int setContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        DialogUtil.showProgress(this);
        this.productPagerFragment = new ProductPagerFragment();
        this.mSettingsFragment = new SettingsFragment();
        this.mInviteFragment = new InviteFragment();
        this.mSupportFragment = new SupportFragment();
        this.mSellFragment = new SellFragment();
        this.mBuyFragment = new BuyFragment();
        this.mCharityFragment = new CharityFragment();
        this.mMyLikeFragment = new MyLikeFragment();
        this.mNewsPagerFragment = new NewsPagerFragment();
        addMessageFragment();
        addNotificationFragment();
        productPagerFragment.setSellListener(new OnClickSellListener() {
            @Override
            public void onClick() {
                if (AccountUntil.getUserToken() != null) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        openCamera();
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                PERMISSIONS_REQUEST_CAMERA);
                    }
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        initSlidingMenu();
        onMenuHomeSelect();

        // check neu dang load thi co hien tutorial k
        addIntroTutFragment();

        initDeviceNumber();

        if (!Utils.checkInternetAvailable()) {
            DialogUtil.showPopup(this, BaseApp.getContext().getString(R.string.error_msg_internet_not_connect));
        }
    }

    @Override
    public void initData() {
    }

    public void initSlidingMenu(){
        mSlidingMenu = new SlidingMenu(this);
        mSlidingMenu.setBehindOffset(240);
        mSlidingMenu.setMenu(R.layout.side_menu);
        mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        initMenu();
    }

    private void showUserName(){
        mTxtUserName.setText(AccountUntil.getUsername());
    }

    private void initMenu(){

        LinearLayout llAvatar = (LinearLayout) findViewById(R.id.ll_avatar);
        llAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccountUntil.getUserToken() == null){
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                else {
                    Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                    DialogUtil.showProgress(MainActivity.this);
                    intent.putExtra("userId", AccountUntil.getAccountId());
                    intent.putExtra("numProduct", 0);
                    intent.putExtra("score", 0);
                    startActivity(intent);
                    mSlidingMenu.toggle();
                }
            }
        });

        mTxtUserName = (TextView) findViewById(R.id.user_name);
        mImgAvatar = (CircularImageView) findViewById(R.id.imgAvatar) ;
        if (AccountUntil.getAccountId() != null){
            showUserName();
            LoadImageUtils.loadImageFromUrl(AccountUntil.getAvatarUrl(), R.drawable.unknown_user, mImgAvatar, null);
        }

        mRVSideMenu = (RecyclerView) findViewById(R.id.side_menu_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRVSideMenu.setLayoutManager(linearLayoutManager);

        List<SideMenuItem> sideMenuItems = SideMenuItem.getListSideMenuItem();

        SideMenuAdapter adapter = new SideMenuAdapter(sideMenuItems, this);
        mRVSideMenu.setAdapter(adapter);

        adapter.addListener(new OnClickSideMenuItemListener() {
            @Override
            public void onClick(int index) {
                setViewItemMenuSelect(index);
            }
        });
    }

    public void onMenuHomeSelect(){
        showTopButton();
        showFragment(productPagerFragment, "home");
    }

    private void showFragment(Fragment showFragment, String tag) {
        if (showFragment != this.currentDisplayFragment) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
                ft.add(R.id.main_content, showFragment, tag).addToBackStack(null);
            } else {
                ft.show(showFragment);
            }
            if (this.currentDisplayFragment != null) {
                ft.hide(this.currentDisplayFragment);
            }
            ft.commit();
            this.currentDisplayFragment = showFragment;
        }


//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.main_content, showFragment, tag).commit();
    }

    public void addMessageFragment() {
        mMsgFragment = new MessageFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.message_fragment, mMsgFragment, MESSAGE_FRAGMENT_TAG).commit();
    }

    public void hideMessageFragment() {
        mLayoutMessage.setVisibility(View.GONE);
    }

    public void showMessageFragment() {
        mLayoutMessage.setVisibility(View.VISIBLE);
        mMsgFragment.loadConversations();
    }

    public void addNotificationFragment() {
        mNotificationFragment = new NotificationFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.notification_fragment, mNotificationFragment, AppConstant.NOTIFICATION_FRAGMENT_TAG).commit();
    }

    public void hideNotificationFragment() {
        mLayoutNotification.setVisibility(View.GONE);
    }

    public void showNotificationFragment() {
        mLayoutNotification.setVisibility(View.VISIBLE);
    }

    public void addIntroTutFragment() {

        if (!AccountUntil.isPassIntroTutFragment()){
            mIntroTutLayout.setVisibility(View.VISIBLE);
            mIntroTutFragment = new IntroTutFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.intro_tut_layout, mIntroTutFragment, AppConstant.INTROTUT_FRAGMENT_TAG).commit();
        }
    }


    private void showTopButton() {
        this.mBtnSearch.setVisibility(View.VISIBLE);
        this.mLLNotiCount.setVisibility(View.VISIBLE);
        this.mLLMessageCount.setVisibility(View.VISIBLE);
        this.mBtnSwitch.setVisibility(View.VISIBLE);
        this.mBtnMenu.setVisibility(View.VISIBLE);
        this.mIconAppName.setVisibility(View.VISIBLE);
        this.mTxtHeader.setVisibility(View.GONE);
    }

    private void hideTopButton(){
        this.mTxtHeader.setVisibility(View.VISIBLE);
        this.mBtnSearch.setVisibility(View.GONE);
        this.mLLNotiCount.setVisibility(View.GONE);
        this.mLLMessageCount.setVisibility(View.GONE);
        this.mBtnSwitch.setVisibility(View.GONE);
        this.mIconAppName.setVisibility(View.GONE);
    }

    private void setViewItemMenuSelect(int index){

        if (index >= 2 && index <=6){
            if (AccountUntil.getUserToken() == null){
//                View v = mRVSideMenu.getLayoutManager().findViewByPosition(mCurrentMenuIndex);
//                TextView textView = (TextView) v.findViewById(R.id.item_title);
//                textView.setTextColor(Color.BLACK);
//                mCurrentMenuIndex = index;
                Intent loginIntent = new Intent(this, LoginActivity.class);
//                loginIntent.putExtra(AppConstant.LOGIN_TYPE_TAG, AppConstant.LOGIN_TO_ACTION);
                startActivity(loginIntent);
//                closeSlidingMenu(index);
                this.finish();
                return;
            }
        }

        switch (index) {
            case 0:
                showFragment(productPagerFragment, "home");
                break;
            case 1:
                mTxtHeader.setText(getResources().getText(R.string.sidemenu_title_news).toString());
                showFragment(mNewsPagerFragment, AppConstant.NEWS_FRAGMENT_TAG);
                break;
            case 2:
                mTxtHeader.setText(getResources().getText(R.string.sidemenu_title_like).toString());
                showFragment(mMyLikeFragment, AppConstant.MYLIKE_FRAGMENT_TAG);
                break;
            case 3:
                mTxtHeader.setText(getResources().getText(R.string.sidemenu_title_exhibit).toString());
                showFragment(mSellFragment, AppConstant.SELL_FRAGMENT_TAG);
                break;
            case 4:
                mTxtHeader.setText(getResources().getText(R.string.sidemenu_title_buy).toString());
                showFragment(mBuyFragment, AppConstant.BUY_FRAGMENT_TAG);
                break;
            case 5:
                mTxtHeader.setText(getResources().getText(R.string.sidemenu_title_charity).toString());
                showFragment(mCharityFragment, AppConstant.CHARITY_FRAGMENT_TAG);
                break;
            case 6:
                mTxtHeader.setText(getResources().getText(R.string.sidemenu_title_setting).toString());
                showFragment(mSettingsFragment, AppConstant.SETTINGS_FRAGMENT_TAG);
                break;
            case 7:
                mTxtHeader.setText(getResources().getText(R.string.sidemenu_title_contact).toString());
                showFragment(mSupportFragment, AppConstant.SUPPORT_FRAGMENT_TAG);
                break;
            case 8:
                mTxtHeader.setText(getResources().getText(R.string.sidemenu_title_invite).toString());
                showFragment(mInviteFragment, AppConstant.INVITE_FRAGMENT_TAG);
                break;
            case 9:
                if (AccountUntil.getUserToken() == null){
                    Intent intent = new Intent(BaseApp.getContext(), LoginActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
                else {
                    logout();
                }

                break;
            default:
                closeSlidingMenu(index);
                break;
        }

        if (index >= 0 && index <= 8){
            closeSlidingMenu(index);
        }

        if (index == 0){
            showTopButton();
        }
        else {
            hideTopButton();
        }

    }

    private void closeSlidingMenu(int index) {
        mSlidingMenu.toggle();
        View v = mRVSideMenu.getLayoutManager().findViewByPosition(mCurrentMenuIndex);
        TextView textView = (TextView) v.findViewById(R.id.item_title);
        textView.setTextColor(Color.BLACK);
        mCurrentMenuIndex = index;
    }

    public void setVisibleTopBar(boolean visible, final View btnCamera) {

        if (visible) {
            mMainLayoutContainer.animate()
                    .translationY(0)
                    .setInterpolator(new LinearInterpolator())
                    .setDuration(500);

//            LinearLayout.LayoutParams layoutParams =  new LinearLayout.LayoutParams(CaCamera.getWidth(), btnCamera.getHeight());
//            layoutParams.setMargins(0,0,0,100);
//            btnCamera.setLayoutParams(layoutParams);

            btnCamera.animate()
                    .translationY(-btnCamera.getHeight() + heightTopBar - statusBarHeight)
                    .setInterpolator(new LinearInterpolator())
                    .setDuration(500);


        } else {
            mMainLayoutContainer.animate()
                    .translationY(-mTopBar.getHeight())
                    .setInterpolator(new LinearInterpolator())
                    .setDuration(500)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            mMainLayoutContainer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, devHeight + heightTopBar));
//                            RelativeLayout.LayoutParams layoutParams =  new RelativeLayout.LayoutParams(btnCamera.getWidth(), btnCamera.getHeight());
//                            layoutParams.setMargin;
//                            btnCamera.setLayoutParams(layoutParams);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
//                            mMainLayoutContainer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });

            btnCamera.animate()
                    .translationY(btnCamera.getHeight())
                    .setInterpolator(new LinearInterpolator())
                    .setDuration(500);
        }
    }

    public void logout() {
        String token = AccountUntil.getUserToken();
        LogoutService logoutService = new LogoutServiceImpl();
        DialogUtil.showProgress(this);
        logoutService.logout(token, new ResponseListener() {
            @Override
            public void onSuccess(Object dataResponse) {
                DialogUtil.hideProgress();
                AccountUntil.removeInfoAccount();
                Intent intent = new Intent(BaseApp.getContext(), LoginActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }

            @Override
            public void onFailure(String errorMessage) {
                DialogUtil.hideProgress();
                DialogUtil.showPopup(MainActivity.this, errorMessage);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mLayoutMessage.getVisibility() == View.VISIBLE){
            mLayoutMessage.setVisibility(View.GONE);
        }
        else {

            String appExitText = AppConstant.APP_EXIT_TEXT;
            String appExitTitle = AppConstant.WARNING_TEXT;
            String conFirmText = AppConstant.CONFIRM_TEXT;
            String cancelText = AppConstant.CANCEL_TEXT;

            DialogUtil.showPopUpWarning(this, appExitText, appExitTitle, conFirmText, cancelText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MainActivity.this.finish();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult");

        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
        }
    }

    public void openCamera() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    private void initDeviceNumber(){
        DisplayMetrics metrics = this.getApplication().getResources().getDisplayMetrics();
        this.devHeight = metrics.heightPixels;

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        mTopBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                heightTopBar = mTopBar.getHeight();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTxtUserName = (TextView) findViewById(R.id.user_name);
        mImgAvatar = (CircularImageView) findViewById(R.id.imgAvatar) ;
        if (AccountUntil.getAccountId() != null){
            showUserName();
            LoadImageUtils.loadImageFromUrl(AccountUntil.getAvatarUrl(), R.drawable.unknown_user, mImgAvatar, null);
            mRVSideMenu.getAdapter().notifyDataSetChanged();
        }

//        if (mCurrentMenuIndex == 2){
//            mTxtHeader.setText(getResources().getText(R.string.sidemenu_title_like).toString());
//            showFragment(mMyLikeFragment, AppConstant.MYLIKE_FRAGMENT_TAG);
//        }

//        View v = mRVSideMenu.getLayoutManager().findViewByPosition(mCurrentMenuIndex);
//        TextView textView = (TextView) v.findViewById(R.id.item_title);
//        textView.setTextColor(Color.RED);
//
//        View vHome = mRVSideMenu.getLayoutManager().findViewByPosition(0);
//        TextView textViewHome = (TextView) v.findViewById(R.id.item_title);
//        textViewHome.setTextColor(Color.BLACK);
    }
}
