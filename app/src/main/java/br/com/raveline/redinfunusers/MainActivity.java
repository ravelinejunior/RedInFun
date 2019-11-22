package br.com.raveline.redinfunusers;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.Objects;

import fragment.PostarFragment;
import fragment.HomeFragment;
import fragment.PerfilFragment;
import fragment.PesquisarFragment;
import fragment.UsuariosFragment;
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
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(),R.color.branco));
        setSupportActionBar(toolbar);

        //habilitando bottomNav
        configurarBottomNav();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_main, new PerfilFragment()).commit();


        //instanciar usuario firebase
        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
    }

    private void configurarBottomNav(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottom_navigation);
        //configurar o bottom navigation
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(true);
        bottomNavigationViewEx.enableShiftingMode(true);
        bottomNavigationViewEx.setTextVisibility(true);

        //habilitando a navegação nas fragments
        habilitarEventosBottomNav(bottomNavigationViewEx);

        //configurar menu inicial quando tela for carrega ou houver algum impacto na rede
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);


    }

    //metodo para habilitar e tratar eventos de clique no bottom navigation
    private void habilitarEventosBottomNav(BottomNavigationViewEx viewEx){
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                //criar objetos para carregar as fragments
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                //recuperando o item de menu que foi selecionado
                switch (menuItem.getItemId()){
                    case R.id.home_bottom:
                        fragmentTransaction.replace(R.id.frame_layout_main, new HomeFragment()).commit();
                        return true;

                    case R.id.perfil_bottom:
                        fragmentTransaction.replace(R.id.frame_layout_main, new PerfilFragment()).commit();
                        //fragmentTransaction.add(R.id.frame_layout_main, new PesquisarFragment().getParentFragment());
                        return true;

                    case R.id.usuarios_bottom:
                        fragmentTransaction.replace(R.id.frame_layout_main, new UsuariosFragment()).commit();
                        return true;

                    case R.id.contato_bottom:
                        fragmentTransaction.replace(R.id.frame_layout_main, new PostarFragment()).commit();
                        return true;

                    case R.id.pesquisar_bottom:
                        fragmentTransaction.replace(R.id.frame_layout_main, new PesquisarFragment()).commit();
                        return true;

                }
                return false;
            }
        });
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
        finish();
        super.onBackPressed();
    }
}
