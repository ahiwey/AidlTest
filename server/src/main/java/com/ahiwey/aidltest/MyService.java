package com.ahiwey.aidltest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by ahiwey on 2017/4/22.
 */

public class MyService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return  new MyBinder();
    }

    public class MyBinder extends IMyAidlInterface.Stub{

        @Override
        public String getName() throws RemoteException {
            return "name11";
        }

        @Override
        public Person getPerson() throws RemoteException {
            return new Person("name",11);
        }
    }
}
