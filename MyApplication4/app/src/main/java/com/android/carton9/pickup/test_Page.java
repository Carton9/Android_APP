package com.android.carton9.pickup;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class test_Page extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    ConnectService connect;
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test__page);
        connect=new ConnectService(this);
        Button start=$(R.id.button);
        Button indata=$(R.id.button2);
        Button act=$(R.id.button3);
        Button signout=$(R.id.button4);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        SignInButton signInButton=$(R.id.SignIn);
        signInButton.setOnClickListener(this);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect.connect();
            }
        });
        indata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(connect.isConnect());
                    connect.getService().sendInfo(0,0," "," ");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                try {
                    connect.getService().action();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect.disconnect();
                connect.stop();
            }
        });
    }
    private <T extends View> T $(int resId) {
        return (T) super.findViewById(resId);
    }

    @Override
    public void onClick(View view) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            StringBuffer output=new StringBuffer();
            output.append(acct.getDisplayName()+"\n");
            output.append(acct.getEmail()+"\n");
            output.append(acct.getId()+"\n");
            output.append(acct.getIdToken()+"\n");
            TextView out=$(R.id.textView2);
            out.setText(output.toString());
        }
    }
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
