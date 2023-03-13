package com.java.milushreearts;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignupFragment extends Fragment {


    private TextView alreadyRegister;
    private EditText fName, email, password, rePassword;
    private Button registerBtn;

    private ProgressBar progressBar;
    private FirebaseAuth fAuthReg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        fName = view.findViewById(R.id.etFullName);
        email = view.findViewById(R.id.etEmail);
        password = view.findViewById(R.id.etPwd);
        rePassword = view.findViewById(R.id.etPwdRe);

        fAuthReg = FirebaseAuth.getInstance();
        progressBar = view.findViewById(R.id.progressBarReg);

        registerBtn = view.findViewById(R.id.btnRegister);
        alreadyRegister = view.findViewById(R.id.tvAlreadyReg);



        alreadyRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getFragmentManager().beginTransaction()
                        .replace(R.id.loginFragContainer, new LoginFragment());
                transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                registerUser();

            }
        });


        return view;
    }

    private void alreadyHaveLoginId() {
        if (fAuthReg.getCurrentUser() != null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction()
                    .replace(R.id.loginFragContainer, new LoginFragment());
            transaction.commit();

        }
    }


    private void registerUser() {


        if (TextUtils.isEmpty(fName.getText().toString())) {
            fName.setError("Please Enter Your Name");
        } else if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError("Please Enter Valid Email ID");
        } else if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("Please Enter Password");
        } else if (password.getText().toString().length() < 6) {
            password.setError("Password Should be Minimum of 6 Characters");
        } else if (TextUtils.isEmpty(rePassword.getText().toString())) {
            rePassword.setError("Please Re-enter Your Password");
        } else if (!rePassword.getText().toString().equals(password.getText().toString())) {
            rePassword.setError("Password Doesn't Matched");
        } else {
            progressBar.setVisibility(View.VISIBLE);

            fAuthReg.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        fAuthReg.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Registered Successfully. Please Check Your Email To Verify Your Account", Toast.LENGTH_LONG).show();
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction()
                                            .replace(R.id.loginFragContainer, new LoginFragment());
                                    transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                                } else {
                                    Toast.makeText(getContext(), "!!ERROR!! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "!!ERROR!! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });


        }


    }

}