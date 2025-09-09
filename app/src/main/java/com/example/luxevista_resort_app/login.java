package com.example.luxevista_resort_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.luxevista_resort_app.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        binding.SignUpInst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, sign_up.class);
                startActivity(intent);
            }
        });

        binding.PasswordEditTextLogin.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(event.getRawX() >= (binding.PasswordEditTextLogin.getRight() - binding.PasswordEditTextLogin.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())){
                        togglePasswordVisibility(binding.PasswordEditTextLogin);
                        return true;
                    }
                }

                return false;
            }
        });


    }

    private void togglePasswordVisibility(EditText editText){
        Drawable leftIcon = editText.getCompoundDrawables()[0];

        if(editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)){

            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

            Drawable rightIcon = ContextCompat.getDrawable(this, R.drawable.eye_off);
            editText.setCompoundDrawablesWithIntrinsicBounds(leftIcon, null, rightIcon, null);

        }
        else{
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            Drawable rightIcon = ContextCompat.getDrawable(this, R.drawable.eye);
            editText.setCompoundDrawablesWithIntrinsicBounds(leftIcon, null, rightIcon, null);
        }
        editText.setSelection(editText.getText().length());
    }

    private void loginUser(){
        String email = binding.EmailEditTextLogin.getText().toString().trim();
        String password = binding.PasswordEditTextLogin.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(login.this, HomePage.class);
                startActivity(intent);
                finish();
        }
            else
            {
                Toast.makeText(getApplicationContext(), "Login failed! Please check your credentials.", Toast.LENGTH_LONG).show();
            }
        });
    }
}