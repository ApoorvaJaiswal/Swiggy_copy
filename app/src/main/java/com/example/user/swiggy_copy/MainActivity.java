package com.example.user.swiggy_copy;




import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends Activity{
    LoginButton loginButton;
    CallbackManager callbackManager;
    AccessToken accessToken;
    Profile profile;
    String ProfileName;
    AccessTokenTracker accessTokenTracker;
    Button notNow;
    ProfileTracker profileTracker;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager= CallbackManager.Factory.create();

        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);
       // AppEventsLogger.activateApp();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        notNow= (Button)findViewById(R.id.notNowButton);

        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                accessToken = loginResult.getAccessToken();
                profile = Profile.getCurrentProfile();
                if (profile != null) {
                    ProfileName = profile.getName();
                    Intent i= new Intent(getApplicationContext(),FrontPage.class);
                    i.putExtra("fbLogin",ProfileName);
                    startActivity(i);

                }

            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Login cancelled", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onError(FacebookException e) {

                Toast.makeText(getApplicationContext(), "Please check your network connection!", Toast.LENGTH_SHORT).show();

            }
        });
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken old, AccessToken newToken) {

            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldP, Profile newP){

            }

        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        notNow=(Button)findViewById(R.id.notNowButton);
        notNow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),FrontPage.class);
                startActivity(i);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
    @Override
    public void onResume() {
        super.onResume();

        profile = Profile.getCurrentProfile();
        if (profile != null) {
            ProfileName = profile.getName();
            Intent i= new Intent(getApplicationContext(),FrontPage.class);
            i.putExtra("fbLogin",ProfileName);
            startActivity(i);
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }
}
