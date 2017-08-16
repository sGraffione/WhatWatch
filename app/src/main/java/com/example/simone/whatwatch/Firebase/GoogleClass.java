package com.example.simone.whatwatch.Firebase;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

//import com.google.android.gms.auth.GoogleAuthUtil;


public class GoogleClass {

    Context mContext;
    String accountName;


    public GoogleClass(Context context){
        mContext = context;
    }

    public String getIdToken(){

        accountName = getAccount();

        // Initialize the scope using the client ID you got from the Console.
        final String scope = "audience:server:client_id:" + "1262xxx48712-9qs6n32447mcj9dirtnkyrejt82saa52.apps.googleusercontent.com";
        String idToken = null;
        try {
    //        idToken = GoogleAuthUtil.getToken(mContext, accountName, scope);
        } catch (Exception e) {
            Log.d("GoogleClass", "exception while getting idToken: " + e);
        }

        return idToken;
    }


    // This snippet takes the simple approach of using the first returned Google account,
    // but you can pick any Google account on the device.
    public String getAccount() {
    Account[] accounts = AccountManager.get(mContext).getAccountsByType("com.google");
    if (accounts.length == 0) {
        return null;
    }
    return accounts[0].name;
    }
}
