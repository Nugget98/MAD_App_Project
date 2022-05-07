package dk.au.mad22spring.app.project.liarsdice.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import dk.au.mad22spring.app.project.liarsdice.Models.StaticUser;
import dk.au.mad22spring.app.project.liarsdice.R;
import dk.au.mad22spring.app.project.liarsdice.Utilities.FirestoreUtil;
import dk.au.mad22spring.app.project.liarsdice.Utilities.GoogleAuthenticationUtil;
import dk.au.mad22spring.app.project.liarsdice.ViewModels.ProfileActivityViewModel;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "FirestoreUtil: ";

    private TextView txtUsername, txtWins, txtLoses, txtTotalGames;
    private Button btnSave, btnCancel;

    private FirestoreUtil firestoreUtil;
    private GoogleAuthenticationUtil googleAuthenticationUtil;
    private ProfileActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        setUserDetails();
    }

    private void setUserDetails() {
        viewModel.getLiveData().observe(this, user -> {
            txtUsername.setText(viewModel.getDisplayName());
        });
        Log.d("CRASH", "StaticUser.Wins: " + StaticUser.staticUser.Wins);
        txtWins.setText(String.valueOf(Integer.parseInt(StaticUser.staticUser.TotalGames) - Integer.parseInt(StaticUser.staticUser.Loses)));
        txtLoses.setText(StaticUser.staticUser.Loses);
        txtTotalGames.setText(StaticUser.staticUser.TotalGames);

        txtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                viewModel.setDisplayName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void init() {
        txtWins = findViewById(R.id.txtDisplayWins);
        txtLoses = findViewById(R.id.txtDisplayLoses);
        txtTotalGames = findViewById(R.id.txtDisplayTotalGames);
        txtUsername = findViewById(R.id.editTextUsername);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(view -> save());
        btnCancel.setOnClickListener(view -> cancel());

        firestoreUtil = FirestoreUtil.getFirestore();
        googleAuthenticationUtil = GoogleAuthenticationUtil.getInstance();

        viewModel = new ViewModelProvider(this).get(ProfileActivityViewModel.class);
        viewModel.getUser();
    }

    private void save() {
        firestoreUtil.updateUser(googleAuthenticationUtil.getSignedInUserUID(), viewModel.getDisplayName(), Integer.parseInt(txtWins.getText().toString()), Integer.parseInt(txtLoses.getText().toString()));
        finish();
    }

    private void cancel() {
        finish();
    }
}