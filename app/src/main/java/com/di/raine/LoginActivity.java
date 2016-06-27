package com.di.raine;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.di.raine.services.user.LoginService;
import com.di.raine.services.user.LogoutService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AppCompatActivity {


    protected Button loginButton;
    protected TextView textView3 =null;
    protected ProgressBar progb = null;

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
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                new LoginService().attemptLogin(getApplicationContext());

            }
        });
    }

    public String readIt(InputStream stream, int len) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    @Override
    protected void onStop() {
        new LogoutService().attemptLogout(getApplicationContext());
        super.onStop();
    }

    protected void checkInternetConnectivity() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            //display error missing internet connectivity
            Toast.makeText(getApplicationContext(), "Enable Internet Traffic", Toast.LENGTH_LONG).show();
        }
    }





// To be removed
    private class  LoginTask extends AsyncTask<Void, String, String> {

        protected String loginUrl = (String) getString(R.string.login_url);

        @Override
        protected String doInBackground(Void... voids) {
            return sendLoginRequest();
        }


        @Override
        protected void onProgressUpdate(String... layout){
            Log.d("DEBUG ", layout[0]);
            textView3.setText(layout[0]);
            progb.setProgress(Integer.valueOf(layout[1]));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        protected String sendLoginRequest() {
            String response = "";
            try {
                URL url = new URL(loginUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                HashMap<String, String> postDataParams = new HashMap<>();
                postDataParams.put("username","dimitris");
                postDataParams.put("password", "123456");
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                os.close();
                publishProgress("Attempting Login...", "30");
                conn.connect();
                int responseCode=conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    publishProgress("Connected!", "70");

                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                }
                else {
                    response="";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            publishProgress("Retrieving Account Information...", "100");
            return response;
        }

        @Override
        protected void onPostExecute(String response){
            Log.d("DEBUG ", response);
            JSONObject jObject = null;;
            try {
                jObject = new JSONObject(response);
                String user = (String) jObject.getJSONObject("data").getJSONObject("credentials").get("username");
                Toast.makeText(getApplicationContext(), "Welcome " + user, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            textView3.setVisibility(View.INVISIBLE);
            progb.setVisibility(View.INVISIBLE);
        }

        @NonNull
        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String> entry : params.entrySet()){
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            return result.toString();
        }
    }
}
