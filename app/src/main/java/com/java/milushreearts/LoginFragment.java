package com.java.milushreearts;

import android.content.Intent;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private TextView signupLogin, forgotPwd;
    private EditText emailLogin, ePassword;
    private Button login, resetPwd;

    private ProgressBar progressBar;
    private FirebaseAuth fAuthLogin;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailLogin = view.findViewById(R.id.etEmailLogIn);
        ePassword = view.findViewById(R.id.etPwdLogIn);
        login = view.findViewById(R.id.btnLogIn);
        forgotPwd = view.findViewById(R.id.tvForgetPwd);
        resetPwd = view.findViewById(R.id.btnResetPwd);

        progressBar = view.findViewById(R.id.progressBarLogin);
        fAuthLogin = FirebaseAuth.getInstance();

        signupLogin = view.findViewById(R.id.tvSignupLogIn);

        signupLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction()
                        .replace(R.id.loginFragContainer, new SignupFragment());
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUser();

            }
        });

        forgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ePassword.setVisibility(View.GONE);
                login.setVisibility(View.GONE);
                forgotPwd.setVisibility(View.GONE);
                signupLogin.setVisibility(View.GONE);
                resetPwd.setVisibility(View.VISIBLE);
            }
        });

        resetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuthLogin.sendPasswordResetEmail(emailLogin.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Password Reset Link Has Been Sent To Your Email ID", Toast.LENGTH_SHORT).show();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction()
                                    .replace(R.id.loginFragContainer, new LoginFragment());
                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                        } else {
                            Toast.makeText(getContext(), "!ERROR! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return view;
    }

    private void loginUser() {

        if (TextUtils.isEmpty(emailLogin.getText().toString())) {
            emailLogin.setError("Please Enter Valid Email ID");
        } else if (TextUtils.isEmpty(ePassword.getText().toString())) {
            ePassword.setError("Please Enter Your Password");
        } else {
            progressBar.setVisibility(View.VISIBLE);

            fAuthLogin.signInWithEmailAndPassword(emailLogin.getText().toString(), ePassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        if (fAuthLogin.getCurrentUser().isEmailVerified()) {
                            startActivity(new Intent(getContext(), MainActivity.class));
                            Toast.makeText(getContext(), "Welcome", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Please Verify Your Email To Login", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        Toast.makeText(getContext(), "!! ERROR !! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        forgotPwd.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }
}