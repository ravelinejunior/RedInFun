package fragment;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import br.com.raveline.redinfunusers.R;
import helper.Permissao;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostarFragment extends Fragment {
    private Button botaoAbrirCameraPostarFragment;
    private Button botaoAbrirGaleriaPostarFragment;

    //request code para a ação de onclick de fotos
    private final static int CODIGO_ABRIR_CAMERA = 100;
    private final static int CODIGO_ABRIR_GALERIA = 200;

    //lista de permissões
    private String[] listaPermissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };



    public PostarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_postar, container, false);

        //validando as permissoes
        Permissao.validarPermissoes(listaPermissoesNecessarias,getActivity(),1);

        botaoAbrirCameraPostarFragment = view.findViewById(R.id.botao_abrir_camera_postar_layout);
        botaoAbrirGaleriaPostarFragment = view.findViewById(R.id.botao_abrir_galeria_postar_layout);

        //evento de click para camera
        botaoAbrirCameraPostarFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getActivity().getPackageManager())!= null){
                    startActivityForResult(i,CODIGO_ABRIR_CAMERA);
                }
            }
        });

        //evento de on click para galeria
        botaoAbrirGaleriaPostarFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(getActivity().getPackageManager())!= null){
                    startActivityForResult(i,CODIGO_ABRIR_GALERIA);
                }
            }
        });





        return view;
    }

}
