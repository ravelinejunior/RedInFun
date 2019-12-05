package br.com.raveline.redinfunusers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

import helper.UsuarioFirebase;
import model.Comentarios;
import model.Usuario;

public class VisualizarComentarioActivity extends AppCompatActivity {
    private RecyclerView recyclerViewVisualizarComentario;
    private EditText comentarioVisualizarComentario;
    private Button botaoComentarVisualizarComentario;
    private Toolbar toolbarVisualizarComentario;
    private String idFotoPostada;

    //configurações de Usuario
    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_comentario);
        carregarElementos();

        //configurações iniciais
        usuario = UsuarioFirebase.getUsuarioLogado(); //quem está fazendo comentario

        //recuperando dados de acesso
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idFotoPostada = bundle.getString("idFotoPostada");
        }

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
