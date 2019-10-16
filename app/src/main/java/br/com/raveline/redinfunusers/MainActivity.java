package br.com.raveline.redinfunusers;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import helper.ConfiguracaoFirebase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //configurando toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_principal_main_activity);
        toolbar.setTitle("RedInFun");
        toolbar.setTitleTextColor(getColor(R.color.branco));
        setSupportActionBar(toolbar);
        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
    }

    //criando menu na tela
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //recuperando valores dos itens
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_main_sair:
                deslogarUsuario();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario(){
        try {
            auth.signOut();
        }catch (Exception e){
            Toast.makeText(this, "Erro." +e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
