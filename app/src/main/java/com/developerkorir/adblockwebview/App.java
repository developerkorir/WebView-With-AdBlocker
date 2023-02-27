package com.developerkorir.adblockwebview;

import android.app.Application;

import com.anythink.core.api.ATSDK;
import com.google.android.gms.ads.MobileAds;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialization Of Admob SDK
        MobileAds.initialize(this);
        //Initialization Of TopOn/Anythink SDK
        ATSDK.init(this,"My_App_ID","My_App_Key");

    }
}
