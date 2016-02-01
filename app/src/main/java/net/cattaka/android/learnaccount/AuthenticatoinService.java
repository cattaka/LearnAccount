package net.cattaka.android.learnaccount;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by takao on 2016/02/01.
 */
public class AuthenticatoinService extends Service {

    private MyAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        mAuthenticator = new MyAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
