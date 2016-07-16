package hackathon.viasat.marksdankburgers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;
import static android.Manifest.permission.READ_CONTACTS;
import android.content.Intent;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A login screen that offers login via email/password.
 */
public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    //firebase authenticator
    private FirebaseAuth authenticator;
    private FirebaseAuth.AuthStateListener authListener;



    //text entry fields
    //private EditText nameField;
    private EditText passwordField;
    private EditText emailField;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);//must be called
        setContentView(R.layout.activity_registration);

        //views
        //nameField = (AutoCompleteTextView) findViewById(R.id.name);
        emailField = (AutoCompleteTextView) findViewById(R.id.email);
        passwordField = (EditText) findViewById(R.id.password);

        //register btn
        findViewById(R.id.register_button).setOnClickListener(this);

        //init data base
        FirebaseDatabase.getInstance().getReference();

        //set up authentication state listener
        authListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //user is signed in
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //launchAccount();


                }
                else{
                    //user is sigend out
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                }

                //updateUI(user);
            }

        };
        //end set up auth state listener

        authenticator = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart(){
        super.onStart();
        authenticator.addAuthStateListener(authListener);
    }

    @Override
    public void onStop(){
        super.onStop();

        //clean up listener
        if(authListener != null){
            authenticator.removeAuthStateListener(authListener);
        }
    }

    private void createAccount(String email, String password){

        //start create user with email
        authenticator.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //if sign in fails display message
                        if(!task.isSuccessful()){
                            Toast.makeText(RegistrationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                        //if sign in succeeds auth state listner notified and handles signed in user
                    }
                });
        //end create user iwth email
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.register_button:
                createAccount(emailField.getText().toString(), passwordField.getText().toString());
                break;


        }

    }
}

