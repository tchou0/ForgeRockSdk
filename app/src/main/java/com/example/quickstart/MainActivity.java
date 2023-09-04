package com.example.quickstart;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.forgerock.android.auth.FRAuth;
import org.forgerock.android.auth.FRUser;
import org.forgerock.android.auth.Logger;
import org.forgerock.android.auth.NodeListener;
import org.forgerock.android.auth.Node;

public class MainActivity extends AppCompatActivity implements NodeListener<FRUser> {
    private TextView status;
    private Button loginButton;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.set(Logger.Level.DEBUG);
        FRAuth.start(this);
        // Add references to status view elements
        status = findViewById(R.id.textViewUserStatus);
        loginButton = findViewById(R.id.buttonLogin);
        logoutButton = findViewById(R.id.buttonLogout);
        updateStatus();

        // Attach `FRUser.login()` to `loginButton`
        loginButton.setOnClickListener(view -> FRUser.login(getApplicationContext(), this));

        // Attach `FRUser.getCurrentUser().logout()` to `logoutButton`
        logoutButton.setOnClickListener(view -> {
            FRUser.getCurrentUser().logout();
            updateStatus();
        });
    }


    private void updateStatus() {
        runOnUiThread(() -> {
            if (FRUser.getCurrentUser() == null) {
                status.setText("User is not authenticated.");
                loginButton.setEnabled(true);
                logoutButton.setEnabled(false);
            } else {
                status.setText("User is authenticated.");
                loginButton.setEnabled(false);
                logoutButton.setEnabled(true);
            }
        });
    }

    @Override
    public void onSuccess(FRUser result) {
        updateStatus();
    }

    @Override
    public void onCallbackReceived(Node node) {
        // Display appropriate UI to handle callbacks
    }

    @Override
    public void onException(Exception e) {
        Logger.error(TAG, e.getMessage(), e);
    }

}
