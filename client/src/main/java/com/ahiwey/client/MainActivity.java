package com.ahiwey.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.ahiwey.aidltest.Book;
import com.ahiwey.aidltest.BookManagerImpl;
import com.ahiwey.aidltest.IBookManager;
import com.ahiwey.aidltest.IMyAidlInterface;
import com.ahiwey.aidltest.Person;

import java.util.List;
public class MainActivity extends AppCompatActivity {

    private IMyAidlInterface mService;
    private IBookManager mService1;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IMyAidlInterface.Stub.asInterface(service);
            Log.d("MainActivity", "onServiceConnected:");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };
    ServiceConnection connection1 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService1 = BookManagerImpl.asInterface(service);
            Log.d("AidlTest", "onServiceConnected1:");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("AidlTest", "onServiceDisconnected1:");
            mService1 = null;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "onClick1:");
                Intent intent = new Intent();
                intent.setAction("com.ahiwey.aidltest.action.MyService");
                intent.setPackage("com.ahiwey.aidltest");
                bindService(createExplicitFromImplicitIntent(MainActivity.this,intent), connection, BIND_AUTO_CREATE);
                Intent intent1 = new Intent();
//                Bundle bundle=new Bundle();
//                intent1.putExtras(bundle);
                intent1.setAction("com.ahiwey.aidltest.action.BookManagerService");
                intent1.setPackage("com.ahiwey.aidltest");
                bindService(intent1, connection1, BIND_AUTO_CREATE);
            }
        });
        findViewById(R.id.btn_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String n = mService.getName();
                    Person person=mService.getPerson();
                    Log.d("MainActivity", "name1:" + n);
                    Log.d("MainActivity", "Person:" + person);
                    Log.d("AidlTest", "getBookList1:" + mService1.getBookList());
                    mService1.addBook(new Book(4,
                            "岛上书店"));
                    Log.d("AidlTest", "getBookList2:" + mService1.getBookList());
                    Log.d("MainActivity", "name:" + n);
                    Log.d("MainActivity", "Person:" + person);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(connection);
        }
    }
    /***
     * Android L (lollipop, API 21) introduced a new problem when trying to invoke implicit intent,
     * "java.lang.IllegalArgumentException: Service Intent must be explicit"
     *
     * If you are using an implicit intent, and know only 1 target would answer this intent,
     * This method will help you turn the implicit intent into the explicit form.
     *
     * Inspired from SO answer: http://stackoverflow.com/a/26318757/1446466
     * @param context
     * @param implicitIntent - The original implicit intent
     * @return Explicit Intent created from the implicit original intent
     */
    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }
}
