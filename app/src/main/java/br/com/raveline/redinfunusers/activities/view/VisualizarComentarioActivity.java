package br.com.raveline.redinfunusers.activities.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import adapter.AdapterVisualizarComentario;
import br.com.raveline.redinfunusers.R;
import helper.ConfiguracaoFirebase;
import helper.UsuarioFirebase;
import model.Comentarios;
import model.Usuario;

public class VisualizarComentarioActivity extends AppCompatActivity {
    private RecyclerView recyclerViewVisualizarComentario;
    private EditText comentarioVisualizarComentario;
    private Button botaoComentarVisualizarComentario;
    private Toolbar toolbarVisualizarComentario;
    private String idFotoPostada;
    private ValueEventListener valueEventListenerComentarios;

    //configurações de Usuario
    private Usuario usuario;

    //CONFIGURAÇÕES FIREBASE
    private DatabaseReference firebaseRef;
    private DatabaseReference comentariosRef;


    //adapter
    private AdapterVisualizarComentario adapterVisualizarComentario;
    private List<Comentarios> comentariosList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_comentario);
        carregarElementos();

        //configurações iniciais
        usuario = UsuarioFirebase.getUsuarioLogado(); //quem está fazendo comentario
        firebaseRef = ConfiguracaoFirebase.getReferenciaDatabase();


        //recuperando dados de acesso
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idFotoPostada = bundle.getString("idFotoPostada");
        }



        adapterVisualizarComentario = new AdapterVisualizarComentario(comentariosList,getApplicationContext());
        //configurar recyclerView
        recyclerViewVisualizarComentario.setHasFixedSize(true);
        recyclerViewVisualizarComentario.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewVisualizarComentario.setAdapter(adapterVisualizarComentario);

    }


    public void recuperarComentarios(){
        comentariosRef = firebaseRef.child("comentarios")
                .child(idFotoPostada);
        valueEventListenerComentarios = comentariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comentariosList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    comentariosList.add(ds.getValue(Comentarios.class));
                    adapterVisualizarComentario.notifyItemChanged(ds.hashCode());
                }
                adapterVisualizarComentario.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        recuperarComentarios();
    }

    @Override
    protected void onStop() {
        super.onStop();
        comentariosRef.removeEventListener(valueEventListenerComentarios);
    }

    public void salvarComentario(View view){

        String comentarioDigitado  = comentarioVisualizarComentario.getText().toString();
        if (comentarioDigitado == null || comentarioDigitado.equals("")){
            Toast.makeText(this, "Insira um comentario", Toast.LENGTH_SHORT).show();
        } else{
            Comentarios comentario = new Comentarios();
            comentario.setIdFotoPostada(idFotoPostada);
            comentario.setIdUsuario(usuario.getId());
            comentario.setNome(usuario.getNome());
            comentario.setCaminhoFoto(usuario.getCaminhoFoto());
            comentario.setComentario(comentarioDigitado);
            if(comentario.salvarComentario()){
                Toast.makeText(this, "Comentario salvo", Toast.LENGTH_SHORT).show();
            }

        }
        comentarioVisualizarComentario.setText("");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    public void carregarElementos(){
        recyclerViewVisualizarComentario = findViewById(R.id.recyclerView_visualizar_comentario);
        comentarioVisualizarComentario = findViewById(R.id.comentario_visualizar_comentario);
        botaoComentarVisualizarComentario = findViewById(R.id.botao_comentar_visualizar_comentario);
        //configurando toolbar
        toolbarVisualizarComentario = findViewById(R.id.toolbar_principal_main_activity);
        toolbarVisualizarComentario.setTitle("Comentarios");
        setSupportActionBar(toolbarVisualizarComentario);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_fechar);

    }
}
