package helper;

import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import model.Usuario;

public class UsuarioFirebase {

    public UsuarioFirebase() {

    }

    public static String getIdentificadorUsuario(){
        return getUsuarioAtual().getUid();
    }

    public static FirebaseUser getUsuarioAtual(){
        FirebaseUser firebaseUser = ConfiguracaoFirebase.getFirebaseAutenticacao().getCurrentUser();
        return firebaseUser;
    }

    public static void atualizarNomeUsuario(String nome){

        try {
            //usuario logado
            FirebaseUser firebaseUserLogado = getUsuarioAtual();

            //configurar usuario logado
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.
                    Builder().
                    setDisplayName(nome).
                    build();

            firebaseUserLogado.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Perfil: "+getUsuarioAtual().getDisplayName(),"Erro: "+e.getMessage());
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void atualizarFotoUsuario(Uri caminhoFoto){
        try {
            //usuario logado
            FirebaseUser firebaseUserLogado = getUsuarioAtual();

            //configurar usuario logado
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.
                    Builder().
                    setPhotoUri(caminhoFoto).
                    build();

            firebaseUserLogado.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Perfil: "+getUsuarioAtual().getDisplayName(),"Erro: "+e.getMessage());
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static Usuario getUsuarioLogado(){

        FirebaseUser firebaseUser = getUsuarioAtual();

        Usuario usuario = new Usuario();
        usuario.setEmail(firebaseUser.getEmail());
        usuario.setNome(firebaseUser.getDisplayName());
        usuario.setId(firebaseUser.getUid());
        if (firebaseUser.getPhotoUrl() == null){
            usuario.setCaminhoFoto("");
        }else{
            usuario.setCaminhoFoto(firebaseUser.getPhotoUrl().toString());
        }
        usuario.setIdade(firebaseUser.getProviderId());
        return usuario;
    }
}
