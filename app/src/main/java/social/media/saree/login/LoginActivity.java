package social.media.saree.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import social.media.saree.MainActivity;
import social.media.saree.R;
import social.media.saree.regiser.register;
import social.media.saree.util.Global;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private ScrollView lock_screen;
    private ProgressBar loading;
    public static Integer INVALIDEMAIL = 1;
    private Integer INVLAIDPASS = 2;
    private Integer EMPTYEMAIL = 3;
    private Integer EMPTYPASS = 4;
    private Integer VALID = 0;
    private Button login, register;
    private String TAG = "Login";
    private Handler mHandler;
    private Integer mInterval = 5000;
    private Integer image_index = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        lock_screen = (ScrollView)findViewById(R.id.lock_screen);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        loading = (ProgressBar)findViewById(R.id.loading);
        ProgressBar loadingProgressBar = findViewById(R.id.loading);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(validate()){
                    case 1:
                        Toast.makeText(LoginActivity.this,"Please input Valid Email Address!",Toast.LENGTH_LONG).show();
                        usernameEditText.setText("");
                        break;
                    case 2:
                        Toast.makeText(LoginActivity.this,"Please input Valid Password!",Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        Toast.makeText(LoginActivity.this,"Please input Email Address",Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        Toast.makeText(LoginActivity.this,"Please input Password",Toast.LENGTH_LONG).show();
                        break;
                    case 0:
                        loading.setVisibility(View.VISIBLE);
                        login();
                        break;
                    default:
                        break;
                }

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, social.media.saree.regiser.register.class);
                startActivity(intent);
            }
        });

        mHandler = new Handler();
        startRepeatingTask();

    }

    private void login() {
        String username = String.valueOf(usernameEditText.getText());
        String password = String.valueOf(passwordEditText.getText());
//        Intent intent = new Intent(LoginActivity.this, shop.carate.shopper.MainActivity.class);
//        startActivity(intent);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loading.setVisibility(View.INVISIBLE);
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Global.current_user_email = user.getEmail();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            stopRepeatingTask();
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            loading.setVisibility(View.INVISIBLE);
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

//        mAuth.signInWithEmailAndPassword(username, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            loading.setVisibility(View.INVISIBLE);
//                            Log.d(TAG, "signInWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            Global.current_user_email = user.getEmail();
//                            Intent intent = new Intent(LoginActivity.this, shop.carate.shopper.MainActivity.class);
//                            startActivity(intent);
//                            finish();
//                        } else {
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                            Toast.makeText(LoginActivity.this,"Login Failed, please try again", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });



    }

    private Integer validate() {
        String username = String.valueOf(usernameEditText.getText());
        String password = String.valueOf(passwordEditText.getText());
        if(username.length()==0){
            return EMPTYEMAIL;
        }
        if(password.length()==0){
            return EMPTYPASS;
        }
        if(!username.contains("@")){
            return INVALIDEMAIL;
        }
        if(password.length()<6){
            return INVLAIDPASS;
        }
        return VALID;
    }

    private void startRepeatingTask() {
        mStatusChecker.run();
    }
    public void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {

            if (image_index > 6)
                image_index = 0;
            lock_screen.setBackgroundResource(R.drawable.lockscreen + image_index);
            image_index ++;

            mHandler.postDelayed(mStatusChecker, mInterval);
        }
    };

}
