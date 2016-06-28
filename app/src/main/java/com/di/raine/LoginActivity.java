package com.di.raine;

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
import android.widget.TextView;
import android.widget.Toast;

import com.di.raine.services.NetworkManager;
import com.di.raine.services.NetworkService;

public class LoginActivity extends AppCompatActivity {

    private NetworkService networkService;
    boolean mBound = false;
    protected Button loginButton;
    protected TextView textView3 = null;
    protected ProgressBar progb = null;
    private String username = null, password = null;
    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";
    private final static String LOGGED_IN = "loggedIn";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkInternetConnectivity();
        loginButton = (Button) findViewById(R.id.login_button);
        textView3 = (TextView) findViewById(R.id.textView3);
        progb = (ProgressBar) findViewById(R.id.progressBar);
        progb.setMax(100);
        progb.setProgress(0);

        Intent startServiceIntent = new Intent(getApplicationContext(),
                NetworkService.class);
        startService(startServiceIntent);
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            if (savedInstanceState.getBoolean(LOGGED_IN)) {
                username = savedInstanceState.getString(USERNAME);
                password = savedInstanceState.getString(PASSWORD);
                networkService.sendLoginRequest(username,password);
            }
        } else {

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    username = ((EditText) findViewById(R.id.username)).getText().toString();
                    password = ((EditText) findViewById(R.id.password)).getText().toString();
                    if (mBound) {
                        System.out.println("adawddadaw");
                        networkService.sendLoginRequest(username, password);
                    }
                }
            });
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, NetworkService.class);
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
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putBoolean(LOGGED_IN, networkService.loggedIn);
        savedInstanceState.putString(USERNAME, username);
        savedInstanceState.putString(PASSWORD, password);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

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


//    // To be removed
//    private class LoginTask extends AsyncTask<Void, String, String> {
//
//        protected String loginUrl = (String) getString(R.string.login_url);
//
//        @Override
//        protected String doInBackground(Void... voids) {
//            return sendLoginRequest();
//        }
//
//
//        @Override
//        protected void onProgressUpdate(String... layout) {
//            Log.d("DEBUG ", layout[0]);
//            textView3.setText(layout[0]);
//            progb.setProgress(Integer.valueOf(layout[1]));
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        protected String sendLoginRequest() {
//            String response = "";
//            try {
//                URL url = new URL(loginUrl);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                conn.setReadTimeout(10000);
//                conn.setConnectTimeout(15000);
//                conn.setRequestMethod("POST");
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
//                HashMap<String, String> postDataParams = new HashMap<>();
//                postDataParams.put("username", "dimitris");
//                postDataParams.put("password", "123456");
//                OutputStream os = conn.getOutputStream();
//                BufferedWriter writer = new BufferedWriter(
//                        new OutputStreamWriter(os, "UTF-8"));
//                writer.write(getPostDataString(postDataParams));
//                writer.flush();
//                writer.close();
//                os.close();
//                publishProgress("Attempting Login...", "30");
//                conn.connect();
//                int responseCode = conn.getResponseCode();
//                if (responseCode == HttpsURLConnection.HTTP_OK) {
//                    publishProgress("Connected!", "70");
//
//                    String line;
//                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                    while ((line = br.readLine()) != null) {
//                        response += line;
//                    }
//                } else {
//                    response = "";
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            publishProgress("Retrieving Account Information...", "100");
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(String response) {
//            Log.d("DEBUG ", response);
//            JSONObject jObject = null;
//            ;
//            try {
//                jObject = new JSONObject(response);
//                String user = (String) jObject.getJSONObject("data").getJSONObject("credentials").get("username");
//                Toast.makeText(getApplicationContext(), "Welcome " + user, Toast.LENGTH_LONG).show();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            textView3.setVisibility(View.INVISIBLE);
//            progb.setVisibility(View.INVISIBLE);
//        }
//
//        @NonNull
//        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
//            StringBuilder result = new StringBuilder();
//            boolean first = true;
//            for (Map.Entry<String, String> entry : params.entrySet()) {
//                if (first)
//                    first = false;
//                else
//                    result.append("&");
//
//                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
//                result.append("=");
//                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
//            }
//            return result.toString();
//        }
//    }
//
//    public String readIt(InputStream stream, int len) throws IOException {
//        Reader reader = null;
//        reader = new InputStreamReader(stream, "UTF-8");
//        char[] buffer = new char[len];
//        reader.read(buffer);
//        return new String(buffer);
//    }
}
