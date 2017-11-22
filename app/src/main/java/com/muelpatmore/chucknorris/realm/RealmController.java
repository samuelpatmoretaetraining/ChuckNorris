package com.muelpatmore.chucknorris.realm;

import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.muelpatmore.chucknorris.utils.Constants;

import java.security.SecureRandom;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Samuel on 22/11/2017.
 */

public class RealmController {

    public static final String TAG = "RealmController";

    private static final String REALM_ID = "My Realm";
    private static RealmController sInstance;

    private Realm mRealm;
    private SharedPreferences preferences;


    private RealmController(Realm realm) {
        this.mRealm = realm;
        enableEncryption();
    }

    public static RealmController getInstance() {
        synchronized (RealmController.class) {
            if (sInstance == null) {
                synchronized (RealmController.class) {
                    sInstance = new RealmController(Realm.getDefaultInstance());

                }
            }
        }
        return sInstance;
    }

    public void saveJoke(final RealmJoke joke) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(joke);
            }
        } );
    }


    private void enableEncryption() {
        byte[] key;
        String rawKey = preferences.getString(Constants.PREFS_ENCRYPTION_KEY, null);

        if (rawKey == null) {
            key = new byte[64];
            new SecureRandom().nextBytes(key);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constants.PREFS_ENCRYPTION_KEY, Base64.encodeToString(key, Base64.DEFAULT));
        } else {
            key = Base64.decode(rawKey, Base64.DEFAULT);
        }
        RealmConfiguration config= new RealmConfiguration.Builder()
                .name(REALM_ID) // can be set to any string
                .schemaVersion(1) //update number
                .deleteRealmIfMigrationNeeded() // rebuild DB on version number change to prevent crash
                .encryptionKey(key)
                .build();

        Realm.setDefaultConfiguration(config);

    }
}
