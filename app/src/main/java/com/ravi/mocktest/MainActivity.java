package com.ravi.mocktest;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends Activity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            openHome();
        } else {
            showLogin();
        }
    }

    void showLogin() {

        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(50, 80, 50, 50);

        EditText email = new EditText(this);
        email.setHint("Email");

        EditText password = new EditText(this);
        password.setHint("Password");

        Button login = new Button(this);
        login.setText("Login");

        Button register = new Button(this);
        register.setText("Create New Account");

        box.addView(email);
        box.addView(password);
        box.addView(login);
        box.addView(register);

        setContentView(box);

        login.setOnClickListener(v -> {

            String e = email.getText().toString().trim();
            String p = password.getText().toString();

            if (e.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Email और Password भरें", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(e, p)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Login सफल", Toast.LENGTH_SHORT).show();
                            openHome();
                        } else {
                            Toast.makeText(this, "Login असफल", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        register.setOnClickListener(v -> {

            String e = email.getText().toString().trim();
            String p = password.getText().toString();

            if (e.isEmpty() || p.length() < 6) {
                Toast.makeText(this, "Valid Email और कम से कम 6 अक्षर का Password डालें", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(e, p)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Account बन गया", Toast.LENGTH_SHORT).show();
                            openHome();
                        } else {
                            Toast.makeText(this, "Registration असफल", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    void openHome() {

        WebView w = new WebView(this);

        WebSettings s = w.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);

        w.loadUrl("file:///android_asset/index.html");

        setContentView(w);
    }
}
