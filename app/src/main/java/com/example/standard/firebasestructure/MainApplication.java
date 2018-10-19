package com.example.standard.firebasestructure;

import android.app.Application;
import android.content.Context;

import com.example.standard.firebasestructure.model.User;
import com.squareup.leakcanary.*;

public class MainApplication extends Application {

    private RefWatcher refWatcher;
    private static User currentUser;

    @Override
    public void onCreate() {
        super.onCreate();
        if(LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        refWatcher = LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context){
        MainApplication application = (MainApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        MainApplication.currentUser = currentUser;
    }
}
