package fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import br.com.raveline.redinfunusers.AlterarDados;
import br.com.raveline.redinfunusers.R;
import de.hdodenhof.circleimageview.CircleImageView;
import helper.ConfiguracaoFirebase;
import helper.UsuarioFirebase;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {
    private Button botaoEditarPerfil;
    private GridView gridViewPerfil;
    private TextView fotosPostadasPerfil;
    private TextView clientesPerfil;
    private TextView visualizacoesPerfil;
    private CircleImageView fotoPerfil;

    //Firebse
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;

    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_perfil, container, false);

         //inicializando componentes
        botaoEditarPerfil = view.findViewById(R.id.botao_editar_perfil);
        gridViewPerfil = view.findViewById(R.id.grid_perfil_layout_fragment);
        fotosPostadasPerfil = view.findViewById(R.id.fotos_perfil_fragment);
        clientesPerfil = view.findViewById(R.id.clientes_perfil_fragment);
        visualizacoesPerfil = view.findViewById(R.id.visualizacoes_perfil_fragment);
        fotoPerfil = view.findViewById(R.id.alterar_foto_perfil);
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        firebaseUser = UsuarioFirebase.getUsuarioAtual();

        botaoEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AlterarDados.class));
                try {
                    PerfilFragment.this.finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });

         return view;
    }



}
