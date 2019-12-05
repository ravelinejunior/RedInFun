package br.com.raveline.redinfunusers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;

public class VisualizarComentarioActivity extends AppCompatActivity {
    private RecyclerView recyclerViewVisualizarComentario;
    private EditText comentarioVisualizarComentario;
    private Button botaoComentarVisualizarComentario;
    private Toolbar toolbarVisualizarComentario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_comentario);
        carregarElementos();



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
