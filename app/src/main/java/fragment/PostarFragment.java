package fragment;


import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import activities.view.FiltrosActivity;
import br.com.raveline.redinfunusers.R;
import helper.Permissao;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostarFragment extends Fragment {

    //request code para a ação de onclick de fotos
    private final static int CODIGO_ABRIR_CAMERA = 100;
    private final static int CODIGO_ABRIR_GALERIA = 200;

    //lista de permissões
    private final String[] listaPermissoesNecessarias = new String[]{
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

        Button botaoAbrirCameraPostarFragment = view.findViewById(R.id.botao_abrir_camera_postar_layout);
        Button botaoAbrirGaleriaPostarFragment = view.findViewById(R.id.botao_abrir_galeria_postar_layout);

        //evento de click para camera
        botaoAbrirCameraPostarFragment.setOnClickListener(v -> {


            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (i.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager())!= null){
                startActivityForResult(i,CODIGO_ABRIR_CAMERA);
            }
        });



        //evento de on click para galeria
        botaoAbrirGaleriaPostarFragment.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (i.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager())!= null){
                startActivityForResult(i,CODIGO_ABRIR_GALERIA);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap imagem = null;

        //valida o tipo de seleção de imagem
       try {

           switch (requestCode){
               case CODIGO_ABRIR_CAMERA:
                   imagem = (Bitmap) Objects.requireNonNull(Objects.requireNonNull(data).getExtras()).get("data");
                   break;
               case CODIGO_ABRIR_GALERIA:
                   Uri localImagemSelecionada = Objects.requireNonNull(data).getData();
                   imagem = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(),localImagemSelecionada);
                   break;

           }

           //validar imagem selecionada
           if (imagem != null){

               //converter imagem em byte array
               ByteArrayOutputStream baos = new ByteArrayOutputStream();
               imagem.compress(Bitmap.CompressFormat.WEBP,80,baos);
               byte[] dadosFoto = baos.toByteArray();

               //enviar imagem para tela de filtros
               Intent i = new Intent(getActivity(), FiltrosActivity.class);
               i.putExtra("fotoSelecionada",dadosFoto);
               startActivity(i);

           }

       }catch (Exception e){
        e.printStackTrace();
       }
    }
}
