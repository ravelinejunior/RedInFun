package br.com.raveline.redinfunusers.activities.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import br.com.raveline.redinfunusers.R;
import br.com.raveline.redinfunusers.activities.usuario.LoginActivity;

public class HomeLauncher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_launcher);
        new Handler().postDelayed(() -> {
            startActivity(new Intent(HomeLauncher.this, LoginActivity.class));
            finish();
        },500);
    }
}
