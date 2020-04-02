package br.com.raveline.redinfunusers.activities.launcher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

import br.com.raveline.redinfunusers.R;
import br.com.raveline.redinfunusers.activities.usuario.CadastrarUsuario;
import br.com.raveline.redinfunusers.activities.usuario.LoginActivity;

public class SlideHomeLauncher extends IntroActivity {

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //pode ser removido devido ao Slider
        //setContentView(R.layout.activity_slide_home_launcher);
        /*
        setButtonBackVisible(false);
        setButtonNextVisible(false);*/

        addSlide(new FragmentSlide.Builder()
                .background(R.color.primaryDarkColor)
                .fragment(R.layout.launcher_item_1)
                .canGoBackward(false)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(R.color.laranja)
                .fragment(R.layout.launcher_item_2)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(R.color.fui_bgFacebook)
                .fragment(R.layout.launcher_item_3)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(R.color.laranjaClaro)
                .fragment(R.layout.launcher_item_4)
                .canGoForward(false)
                .build()
        );


    }

    public void botaoCadastrarLauncher(View view) {
        Intent intent = new Intent(getContext(), CadastrarUsuario.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public Context getContext() {
        return SlideHomeLauncher.this;
    }

    public void botaoLogarLauncher(View view) {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


     /*
            MODO MANUAL DE COLOCAR
     addSlide(new SimpleSlide.Builder()
                .title("PetGram 2")
                .description("Um mundo animal de fofura e diversão")
                .image(R.drawable.logonovacirc)
                .background(R.color.primaryLightColor)
                .build()
        );

        addSlide(new SimpleSlide.Builder()
                .title("PetGram 3")
                .description("Um mundo animal de fofura e diversão")
                .image(R.drawable.logonovacirc)
                .background(R.color.primaryLightColor)
                .build()
        );

        addSlide(new SimpleSlide.Builder()
                .title("PetGram 4")
                .description("Um mundo animal de fofura e diversão")
                .image(R.drawable.logonovacirc)
                .background(R.color.primaryLightColor)
                .build()
        );*/

}

