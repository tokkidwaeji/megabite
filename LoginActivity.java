package hackathon.viasat.marksdankburgers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import static android.Manifest.permission.READ_CONTACTS;
import android.content.Intent;

import android.view.View.OnTouchListener;
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
public class LoginActivity extends AppCompatActivity implements View.OnClickListener,  View.OnTouchListener{


    //firebase authenticator
    private FirebaseAuth authenticator;
    private FirebaseAuth.AuthStateListener authListener;



    //text entry fields
    private EditText passwordField;
    private EditText emailField;

    private TextView registerLink;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);//must be called
        setContentView(R.layout.activity_login);

        //views
        emailField = (AutoCompleteTextView) findViewById(R.id.email);
        passwordField = (EditText) findViewById(R.id.password);

        registerLink = (TextView) findViewById(R.id.registser_link);
        registerLink.setOnTouchListener(this);
        //login btn
        findViewById(R.id.sign_in_button).setOnClickListener(this);



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



    private void signIn(String email, String password){
        //start signin
        authenticator.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Autnetication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    //open up the registration form
    private void goToRegisterActivity(){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.sign_in_button:
                signIn(emailField.getText().toString(), passwordField.getText().toString());
                break;



        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(v.getId()){
            case R.id.registser_link:
                goToRegisterActivity();
                break;
        }
        return false;
    }
}

