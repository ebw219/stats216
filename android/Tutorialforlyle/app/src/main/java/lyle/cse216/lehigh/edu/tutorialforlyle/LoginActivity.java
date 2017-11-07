package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import static com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.getStatusCodeString;

/**
 * Created by Kelli on 10/8/17.
 * Phase 3
 */


public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{
    String username;
    String url = "https://lyle-buzz.herokuapp.com/";
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LoginActivity";
    private TextView mStatusTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        findViewById(R.id.badLogin).setVisibility(View.INVISIBLE);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect(); //added to try to make login successful, didn't work so not really necessary

//        AccountManager am = AccountManager.get(this);
//        Bundle options = new Bundle();
//
//        am.getAuthToken(Log.d("lyle", "SHARED PREF: " + SaveSharedPreference.getUserName(LoginActivity.this));
//        myAccount_,                     // Account retrieved using getAccountsByType()
//                "Manage your tasks",            // Auth scopeif(SaveSharedPreference.getUserName(LoginActivity.this).length() == 0) {
//                options,                        // Authenticator-specific options    Intent input = new Intent(getApplicationContext(), MainActivity.class);
//                this,                           // Your activity    startActivityForResult(input, 789);
//                new OnTokenAcquired(),          // Callback called when a token is successfully acquired}
//                new Handler(new OnError()));    // Callback called if an error occurselse {
//        // Stay at the current activity.
//    }

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();

        findViewById(R.id.sign_in_button).setOnClickListener(this);

//    findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Log.d("lyle", "CLICKED LOGIN");
//
//
//
//            username = ((TextView) findViewById(R.id.username)).getText().toString();
//            String password = ((TextView) findViewById(R.id.password)).getText().toString();
//            Intent loginButton = new Intent(getApplicationContext(), MainActivity.class);
//            startActivityForResult(loginButton, 123);
//
////                StringRequest login = new StringRequest(Request.Method.POST, url + "login/" + username + "/" + password,
////                        new Response.Listener<String>() {
////                            @Override
////                            public void onResponse(String response) {
////                                Log.d("lyle", "LOGIN ATTEMPT");
////                                Log.d("lyle", response);
////                                if(loginSuccess(response)) {
////                                    Intent input = new Intent(getApplicationContext(), MainActivity.class);
////                                    input.putExtra("label_contents", "Login");
////                                    input.putExtra("username", username);
////                                    input.putExtra("rand_val", randVal(response));
////                                    SaveSharedPreference.setUserName(getApplicationContext(), username);
////                                    SaveSharedPreference.setRandVal(getApplicationContext(), randVal(response));
////                                    startActivityForResult(input, 123);
////                                } else {
////                                    findViewById(R.id.badLoginCreds).setVisibility(View.VISIBLE);
////                                }
////                            }
////                        }, new Response.ErrorListener() {
////                    @Override
////                    public void onErrorResponse(VolleyError error) { //if mstatus is error
////                        findViewById(R.id.badLoginCreds).setVisibility(View.VISIBLE);
////                        Log.e("lyle", "Bad user creds???");
////                        Log.e("lyle", "That POST didn't work");
////                    }
////                });
////                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(login);
//        }
//            }
//    });


//    findViewById(R.id.createUserButton).setOnClickListener(new View.OnClickListener() {
//
//        @Override
//        public void onClick(View view) {
//            Intent input = new Intent(getApplicationContext(), CreateUserActivity.class);
//            input.putExtra("label_contents", "Create user");
//            startActivityForResult(input, 789);
//        }
//    });


//    findViewById(R.id.badLoginCreds).setVisibility(View.INVISIBLE);

        Intent i = getIntent();
        Log.d("lyle", "label_contents");
        if (i.getStringExtra("label_contents") != null) {
            if (i.getStringExtra("label_contents").equals("Make new user"))
                findViewById(R.id.successCreate).setVisibility(View.VISIBLE);
        }
    }

    String randVal(String response){
        String randVal = new String();
        try{
            JSONObject jsonObj = new JSONObject(response);
            randVal = jsonObj.get("mMessage").toString();
        } catch (final JSONException e) {
            Log.e("lyle", e.getMessage());
        }
        return randVal;
    }

    boolean loginSuccess(String response){
        try {
            Log.d("lyle", "Checking: " + response); // for whatever reason, this (or some log statement) is necessary for the messages to appear
            JSONObject jsonObj = new JSONObject(response);
//            JSONArray json = jsonObj.getJSONArray("mStatus");
//            JSONObject jsonObj = new JSONObject("mStatus");
            if(jsonObj.get("mStatus").equals("ok"))
                return true;
        } catch (final JSONException e) {
            Log.d("lyle", "Error parsing JSON file: " + e.getMessage());
            if(e.getMessage().equals("No value for mData")) {
                Log.d("lyle", "NO DATA");
            }
        }
        return false;
    }

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.d("lyle", "RC_SIGN_IN: " + RC_SIGN_IN);
//        startActivityForResult(signInIntent, MainActivity.class);

    }
    // [END signIn]

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("lyle", "requestCode: " + requestCode);
        Log.d("lyle", "resultCode: " + resultCode);
        Log.d("lyle", "data: " + data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            Log.d("lyle", "onActivityResult method");
            int statusCode = result.getStatus().getStatusCode();
            String status = getStatusCodeString(statusCode);
            Log.d("lyle", "i hate android");
            Log.d("lyle", "status for resultalkdhc: " + status);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        //if (result.isSuccess()) {
        boolean check = true;
        if (check == true) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Intent success = new Intent(getApplicationContext(), MainActivity.class);
            startActivityForResult(success, 123);
            //showing name on page?
//            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true);
        } else {
            findViewById(R.id.badLogin).setVisibility(View.VISIBLE);
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    // [END handleSignInResult]


    private void updateUI(boolean signedIn) {
//        if (signedIn) {
//            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
//        } else {
//            mStatusTextView.setText(R.string.signed_out);
//
//            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
//            case R.id.sign_out_button:
//                signOut();
//                break;
//            case R.id.disconnect_button:
//                revokeAccess();
//                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}

//class OnTokenAcquired implements AccountManagerCallback<Bundle> {
//    @Override
//    public void run(AccountManagerFuture<Bundle> result) {
//        // Get the result of the operation from the AccountManagerFuture.
//        Bundle bundle = null;
//        try {
//            bundle = result.getResult();
//        } catch (OperationCanceledException e) {
//            e.printStackTrace();
//        } catch (AuthenticatorException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // The token is a named value in the bundle. The name of the value
//        // is stored in the constant AccountManager.KEY_AUTHTOKEN.
//        token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
//    }
//}
//
//class OnTokenAcquired implements AccountManagerCallback<Bundle> {
//    @Override
//    public void run(AccountManagerFuture<Bundle> result) {
//        Intent launch = (Intent) result.getResult().get(AccountManager.KEY_INTENT);
//        if (launch != null) {
//            startActivityForResult(launch, 0);
//            return;
//        }
//    }
//}