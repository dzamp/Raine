package com.di.raine.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.di.raine.R;
import com.di.raine.services.NetworkService;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private NetworkService networkService;
    private LoginActivity instance = this;
    boolean mBound = false;
    protected Button loginButton;
    private String username = null, password = null;
    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";
    private final static String LOGGED_IN = "loggedIn";
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkInternetConnectivity();
        bar = (ProgressBar) findViewById(R.id.progressBar);
        loginButton = (Button) findViewById(R.id.login_button);
//        if (savedInstanceState != null) {
//            // Restore value of members from saved state
//            if (savedInstanceState.getBoolean(LOGGED_IN)) {
//                username = savedInstanceState.getString(USERNAME);
//                password = savedInstanceState.getString(PASSWORD);
////                networkService.sendLoginRequest(username,password);
//            }
//        } else {
        loginButton.setOnClickListener(this);
    }
    // } end of else


    @Override
    public void onClick(View view) {
        username = ((EditText) findViewById(R.id.username)).getText().toString();
        final EditText usernameEditText = ((EditText) findViewById(R.id.username));
        final EditText passwordEditText = ((EditText) findViewById(R.id.password));
        password = ((EditText) findViewById(R.id.password)).getText().toString();
        bar.setVisibility(View.VISIBLE);
        if (mBound) {
            networkService.sendLoginRequest(username, password, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("SUCCESS", (String) response);
                            Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), GridView_Products.class);
                            startActivity(i);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            bar.setVisibility(View.INVISIBLE);
                            usernameEditText.setError("Invalid");
                            usernameEditText.setText("");
                            passwordEditText.setError("Invalid");
                            passwordEditText.setText("");
                        }
                    }
            );
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, NetworkService.class);
        startService(intent);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            networkService.sendLogoutRequest();
            unbindService(mConnection);
            mBound = false;
        }

        bar.setVisibility(View.INVISIBLE);
    }


//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        // Save the user's current game state
//        savedInstanceState.putBoolean(LOGGED_IN, networkService.loggedIn);
//        savedInstanceState.putString(USERNAME, username);
//        savedInstanceState.putString(PASSWORD, password);
//        // Always call the superclass so it can save the view hierarchy state
//        super.onSaveInstanceState(savedInstanceState);
//    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            NetworkService.NetworkBinder binder = (NetworkService.NetworkBinder) service;
            networkService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    protected void checkInternetConnectivity() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(getApplicationContext(), "Enable Internet Traffic", Toast.LENGTH_LONG).show();
        }
    }


}
