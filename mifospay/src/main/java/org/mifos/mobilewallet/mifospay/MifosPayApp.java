package org.mifos.mobilewallet.mifospay;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.work.Worker;

import com.mifos.mobile.passcode.utils.ForegroundChecker;

import org.jetbrains.annotations.NotNull;
import org.mifos.mobilewallet.mifospay.injection.component.ApplicationComponent;
import org.mifos.mobilewallet.mifospay.injection.component.DaggerApplicationComponent;
import org.mifos.mobilewallet.mifospay.injection.module.ApplicationModule;

import butterknife.ButterKnife;
import chat.rocket.android.app.RocketChatInitializer;
import chat.rocket.android.app.RocketChatInjector;
import dagger.android.AndroidInjector;

/**
 * Created by naman on 17/8/17.
 */

public class MifosPayApp extends Application implements RocketChatInjector {

    private static MifosPayApp instance;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    ApplicationComponent applicationComponent;
    RocketChatInitializer rocketChatInitializer;

    public static MifosPayApp get(Context context) {
        return (MifosPayApp) context.getApplicationContext();
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (MifosPayApp.instance == null) {
            MifosPayApp.instance = this;
        }
        ButterKnife.setDebug(true);

        //Initialize ForegroundChecker
        ForegroundChecker.init(this);
        // For Initializing Rocket.Chat
        rocketChatInitializer = new RocketChatInitializer(this);
        rocketChatInitializer.init();
    }

    public ApplicationComponent component() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return applicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }

    @NotNull
    @Override
    public AndroidInjector<Worker> workerInjector() {
        return rocketChatInitializer.workerInjector();
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return rocketChatInitializer.activityInjector();
    }

    @Override
    public AndroidInjector<BroadcastReceiver> broadcastReceiverInjector() {
        return rocketChatInitializer.broadcastReceiverInjector();
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return rocketChatInitializer.serviceInjector();
    }
}

