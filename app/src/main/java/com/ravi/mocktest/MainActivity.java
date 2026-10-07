package com.ravi.mocktest;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
public class MainActivity extends Activity {

    FirebaseAuth auth;
FirebaseFirestore db;
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        auth = FirebaseAuth.getInstance();
db = FirebaseFirestore.getInstance();
        if (auth.getCurrentUser() != null) {
            openHome();
        } else {
            showLogin();
        }
    }
void checkAdmin() {
        if (auth.getCurrentUser() == null) {
                return;
                    }

                        String uid = auth.getCurrentUser().getUid();

                            db.collection("admins").document(uid).get()
                                    .addOnSuccessListener(document -> {
                                                if (document.exists()) {
                                                                Toast.makeText(this, "Admin verified", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                                    });
                                                                                    }void
}
    void showLogin() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(50, 80, 50, 50);
        box.setBackgroundColor(Color.WHITE);

        TextView title = new TextView(this);
        title.setText("Ravi Mock Test");
        title.setTextSize(30);
        title.setTextColor(Color.rgb(40, 90, 220));
        title.setGravity(17);

        TextView sub = new TextView(this);
        sub.setText("User Login");
        sub.setTextSize(22);
        sub.setPadding(0, 30, 0, 30);

        EditText email = new EditText(this);
        email.setHint("Email");

        EditText password = new EditText(this);
        password.setHint("Password");
        password.setInputType(129);

        Button login = new Button(this);
        login.setText("Login");

        Button register = new Button(this);
        register.setText("Create New Account");

        box.addView(title);
        box.addView(sub);
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
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Login सफल", Toast.LENGTH_SHORT).show();
                        openHome();
                    } else {
                        Toast.makeText(this, "Login असफल: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        });

        register.setOnClickListener(v -> {
            String e = email.getText().toString().trim();
            String p = password.getText().toString();

            if (e.isEmpty() || p.length() < 6) {
                Toast.makeText(this, "Email भरें और Password कम से कम 6 अक्षर का रखें", Toast.LENGTH_LONG).show();
                return;
            }

            auth.createUserWithEmailAndPassword(e, p)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Account बन गया", Toast.LENGTH_SHORT).show();
                        openHome();
                    } else {
                        Toast.makeText(this, "Registration असफल: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
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


