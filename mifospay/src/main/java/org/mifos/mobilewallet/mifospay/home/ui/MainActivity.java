package org.mifos.mobilewallet.mifospay.home.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.jetbrains.annotations.NotNull;
import org.mifos.mobilewallet.core.domain.model.client.Client;
import org.mifos.mobilewallet.mifospay.R;
import org.mifos.mobilewallet.mifospay.base.BaseActivity;
import org.mifos.mobilewallet.mifospay.data.local.LocalRepository;
import org.mifos.mobilewallet.mifospay.faq.ui.FAQActivity;
import org.mifos.mobilewallet.mifospay.home.BaseHomeContract;
import org.mifos.mobilewallet.mifospay.home.presenter.MainPresenter;
import org.mifos.mobilewallet.mifospay.settings.ui.SettingsActivity;
import org.mifos.mobilewallet.mifospay.utils.Constants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.rocket.android.authentication.RocketChat;
import chat.rocket.android.authentication.presentation.RocketChatView;
import chat.rocket.android.helper.ActivityKtKt;
import okhttp3.HttpUrl;

import static chat.rocket.android.authentication.RocketChatKt.STATE_ERROR;
import static chat.rocket.android.authentication.RocketChatKt.STATE_LOADING;

/**
 * Created by naman on 17/6/17.
 */

public class MainActivity extends BaseActivity implements
        BaseHomeContract.BaseHomeView, RocketChatView {

    @Inject
    MainPresenter mPresenter;

    @Inject
    LocalRepository localRepository;

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    BaseHomeContract.BaseHomePresenter mHomePresenter;
    RocketChat<MainActivity> rocketChat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        mPresenter.attachView(this);
        mHomePresenter.fetchClientDetails();
        initializeRocketChatSession();
        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    navigateFragment(item.getItemId(), false);
                    return true;
                });
        bottomNavigationView.setSelectedItemId(R.id.action_home);
        setToolbarTitle(Constants.HOME);
    }

    private void initializeRocketChatSession() {
        String userName = "Naman_Wallet";
        //String roomName = "mobile_wallet_" + localRepository.getClientDetails().getClientId();
        String roomName = "general";
        String userEmail = localRepository.getPreferencesHelper().getEmail();
        String userPassword = "password";
        String name = localRepository.getClientDetails().getName();
        String protocol = localRepository.getPreferencesHelper().getRocketChatServerProtocol();
        String serverDomain = localRepository.getPreferencesHelper().getRocketChatServerDomain();
        //Log.e("details ", userName + " " + userEmail + " " + name);
        rocketChat = new RocketChat<>(this,
                protocol,
                serverDomain,
                name,
                userName,
                userEmail,
                userPassword,
                roomName
        );

        rocketChat.loadCredentials();
        Log.d("ROCKETCHAT", rocketChat.getMessage());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overflow, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_faq:
                startActivity(new Intent(getApplicationContext(), FAQActivity.class));
                break;
            case R.id.item_profile_setting:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            case R.id.item_customer_care:
                startCustomerService();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void startCustomerService() {
        if (rocketChat.getState().getValue().equals(STATE_LOADING)) {
            //showProgress();
            rocketChat.getState().observe(this,
                    s -> {
                        //hideProgress();
                        if (s.equals(STATE_ERROR)) {
                            Log.e("RC", "LOADING + ERROR " + rocketChat.getMessage());
//                                Log.e(HomeActivity.class.getSimpleName(), rocketChat.getMessage());
//                                Snackbar snackbar = Snackbar
//                                        .make(findViewById(R.id.container),
//                                                rocketChat.getMessage(), Snackbar.LENGTH_LONG)
//                                        .setAction("RETRY", new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//                                                rocketChat.loadCredentials();
//                                                moveToSupportChannel();
//                                            }
//                                        });
//                                snackbar.show();
                        } else {
                            Log.e("RC", "LOADING + SUCCESS" + rocketChat.getMessage());
                            rocketChat.loadChatRoom();
                        }
                    });
        } else if (rocketChat.getState().getValue().equals(STATE_ERROR)) {
            Log.e("RC", "ERROR + ERROR" + rocketChat.getMessage());
//            Log.e(HomeActivity.class.getSimpleName(), rocketChat.getMessage());
//            Snackbar snackbar = Snackbar
//                    .make(findViewById(R.id.container),
//                            rocketChat.getMessage(), Snackbar.LENGTH_LONG)
//                    .setAction("RETRY", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            rocketChat.loadCredentials();
//                            moveToSupportChannel();
//                        }
//                    });
//            snackbar.show();
        } else {
            Log.e("RC", "ERROR + SUCCESS" + rocketChat.getMessage());
            rocketChat.loadChatRoom();
        }
    }

    @Override
    public void setPresenter(BaseHomeContract.BaseHomePresenter presenter) {
        mHomePresenter = presenter;
    }

    @Override
    public void showClientDetails(Client client) {
//        tvUserName.setText(client.getName());
//        TextDrawable drawable = TextDrawable.builder()
//                .buildRound(client.getName().substring(0, 1), R.color.colorPrimary);
//        ivUserImage.setImageDrawable(drawable);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.bottom_navigation_fragment_container);
        if (fragment != null && !(fragment instanceof HomeFragment) && fragment.isVisible()) {
            navigateFragment(R.id.action_home, true);
            return;
        }
        super.onBackPressed();
    }

    private void navigateFragment(int id, boolean shouldSelect) {
        if (shouldSelect) {
            bottomNavigationView.setSelectedItemId(id);
        } else {
            switch (id) {
                case R.id.action_home:
                    replaceFragment(HomeFragment.newInstance(localRepository.getClientDetails()
                                    .getClientId()), false,
                            R.id.bottom_navigation_fragment_container);
                    break;

                case R.id.action_payments:
                    replaceFragment(PaymentsFragment.newInstance(), false,
                            R.id.bottom_navigation_fragment_container);
                    break;

                case R.id.action_finance:
                    replaceFragment(FinanceFragment.newInstance(), false,
                            R.id.bottom_navigation_fragment_container);
                    break;

                case R.id.action_profile:
                    replaceFragment(new ProfileFragment(), false,
                            R.id.bottom_navigation_fragment_container);
                    break;

            }
        }
    }

    @Override
    public void saveSmartLockCredentials(@NotNull String s, @NotNull String s1) {
        ActivityKtKt.saveCredentials(this, s, s1);
    }

    @Override
    public void alertNotRecommendedVersion() {

    }

    @Override
    public void blockAndAlertNotRequiredVersion() {

    }

    @Override
    public void errorCheckingServerVersion() {

    }

    @Override
    public void errorInvalidProtocol() {

    }

    @Override
    public void updateServerUrl(@NotNull HttpUrl httpUrl) {

    }

    @Override
    public void versionOk() {

    }
}
