package dk.au.mad22spring.app.project.liarsdice;

import android.app.Application;
import android.content.Context;

public class LiarsDiceApplication extends Application {
    private static LiarsDiceApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getAppContext(){
        return instance.getApplicationContext();
    }
}
