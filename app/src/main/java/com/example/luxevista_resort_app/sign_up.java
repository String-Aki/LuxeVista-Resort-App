package com.example.luxevista_resort_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.luxevista_resort_app.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class sign_up extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        binding.PasswordEditTextSignUp.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if(event.getAction() == MotionEvent.ACTION_UP){
                if(event.getRawX() >= (binding.PasswordEditTextSignUp.getRight() - binding.PasswordEditTextSignUp.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())){
                    togglePasswordVisibility(binding.PasswordEditTextSignUp);
                    return true;
                }
            }
            return false;
        });

        binding.SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });

        binding.LoginInst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sign_up.this, login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void togglePasswordVisibility(EditText editText){
        Drawable leftIcon = editText.getCompoundDrawables()[0];

        if(editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)){

            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

            Drawable rightIcon = ContextCompat.getDrawable(this, R.drawable.ico_hint_eye_off);
            editText.setCompoundDrawablesWithIntrinsicBounds(leftIcon, null, rightIcon, null);

        }
        else{
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            Drawable rightIcon = ContextCompat.getDrawable(this, R.drawable.ico_hint_eye);
            editText.setCompoundDrawablesWithIntrinsicBounds(leftIcon, null, rightIcon, null);
        }
        editText.setSelection(editText.getText().length());
    }

    private void registerNewUser() {
        String firstName = binding.FirstNameEditText.getText().toString().trim();
        String lastName = binding.LastNameEditText.getText().toString().trim();
        String email = binding.EmailEditTextSignUp.getText().toString();
        String password = binding.PasswordEditTextSignUp.getText().toString();

        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Authentication successful. Saving details...", Toast.LENGTH_SHORT).show();

                    String userId = mAuth.getCurrentUser().getUid();

                    Map<String, Object> user = new HashMap<>();
                    user.put("firstName", firstName);
                    user.put("lastName", lastName);
                    user.put("email", email);

                    db.collection("users").document(userId)
                            .set(user)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(sign_up.this, login.class);
                                startActivity(intent);
                                finish();
                            }).addOnFailureListener(e -> {
                                Toast.makeText(getApplicationContext(), "Error saving user details", Toast.LENGTH_LONG).show();
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}