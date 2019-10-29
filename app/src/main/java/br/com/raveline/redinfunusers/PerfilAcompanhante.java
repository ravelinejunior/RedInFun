package br.com.raveline.redinfunusers;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import helper.ConfiguracaoFirebase;
import helper.UsuarioFirebase;
import model.Usuario;

public class PerfilAcompanhante extends AppCompatActivity {

    private Usuario usuarioSelecionado;
    private Button botaoSeguirAcompanhante;
    private CircleImageView imagemPerfilAcompanhante;
    private TextView fasPerfilAcompanhante;
    private TextView clientesPerfilAcompanhante;
    private TextView fotosPerfilAcompanhante;

    //firebase
    private DatabaseReference referenceFirebase;
    private DatabaseReference usuariosRef;
    private DatabaseReference usuarioAmigoRef;
    private DatabaseReference seguidoresRef;

    //para eventos
    private ValueEventListener valueEventListenerPerfilAcompanhante;

    //usuarios
    private String idUsuarioLogado;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_acompanhante);

        inicializarComponentes();

        //Configurações iniciais Firebase
        //recuperando o no usuarios
        referenceFirebase = ConfiguracaoFirebase.getReferenciaDatabase();
        usuariosRef = referenceFirebase.child("usuarios");
        seguidoresRef = referenceFirebase.child("seguidores");
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();



        //recuperar usuario selecionado
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            usuarioSelecionado = (Usuario) bundle.getSerializable("usuarioSelecionado");

            //configura nome da toolbar nome do usuario
            getSupportActionBar().setTitle(usuarioSelecionado.getNome());

            //recuperar foto do usuario
            String caminhoFoto = usuarioSelecionado.getCaminhoFoto();
            if( caminhoFoto != null){
                Uri url = Uri.parse(caminhoFoto);
                Glide.with(PerfilAcompanhante.this).load(url)
                        .circleCrop()
                        .centerInside()
                        .into(imagemPerfilAcompanhante);

            } else{
                Toast.makeText(this, "Erro ao recuperar imagem.", Toast.LENGTH_SHORT).show();
            }

        }


    }

    private void verificarSeSegueAcompanhante(){
        DatabaseReference seguidorRef = seguidoresRef.
                child(idUsuarioLogado).
                child(usuarioSelecionado.getId())
                ;
        seguidorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //verificar se existem dados dentro do DataSnapshot
                if (dataSnapshot.exists()){
                    //Já segue usuario
                    habilitarBotaoSeguir(true);
                }
                else{
                    //Ainda não segue usuario
                    habilitarBotaoSeguir(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void habilitarBotaoSeguir(boolean segueUsuario){

        if (segueUsuario){
            botaoSeguirAcompanhante.setText("Seguindo Acompanhante");
        }else{
            botaoSeguirAcompanhante.setText("Seguir Acompanhante");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarDadosAcompanhante();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //remove evento para nao pesar activity
        usuarioAmigoRef.removeEventListener(valueEventListenerPerfilAcompanhante);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void inicializarComponentes(){
        imagemPerfilAcompanhante = findViewById(R.id.perfil_foto_perfil_fragment);
        botaoSeguirAcompanhante = findViewById(R.id.botao_acao_perfil);
        fasPerfilAcompanhante = findViewById(R.id.fas_perfil_fragment);
        clientesPerfilAcompanhante = findViewById(R.id.clientes_perfil_fragment);
        fotosPerfilAcompanhante = findViewById(R.id.fotos_perfil_fragment);
        botaoSeguirAcompanhante.setText("Seguir Acompanhante");

        //configurando toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_principal_main_activity);
        toolbar.setTitle("RedInFun");
        toolbar.setTitleTextColor(getColor(R.color.branco));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_voltar_back);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    private void recuperarDadosAcompanhante(){
        //recuperando dados do usuario mencionado por id
        usuarioAmigoRef = usuariosRef.child(usuarioSelecionado.getId());

        //adicionando evento
        valueEventListenerPerfilAcompanhante = usuarioAmigoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //recuperar e exibir dados do usuario
                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                //configurar valores da activity
                String fotosPostadasAcompanhante = String.valueOf(Objects.requireNonNull(usuario).getFotos());
                String fasAcompanhante = String.valueOf(usuario.getFas());
                String clientesAcomphantes = String.valueOf(usuario.getClientes());

                //configurar caixa de texto
                fotosPerfilAcompanhante.setText(fotosPostadasAcompanhante);
                fasPerfilAcompanhante.setText(fasAcompanhante);
                clientesPerfilAcompanhante.setText(clientesAcomphantes);

                

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
