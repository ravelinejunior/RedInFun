package helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {
    private static DatabaseReference referenciaDatabase;
    private static FirebaseAuth referenciaAuth;
    public ConfiguracaoFirebase() {
    }

    public static FirebaseAuth getFirebaseAutenticacao(){
        //caso a autenticação esteja nula
        if (referenciaAuth == null){
            referenciaAuth = FirebaseAuth.getInstance();
        }
        return referenciaAuth;
    }

    public static DatabaseReference getReferenciaDatabase(){
        if (referenciaDatabase == null){
            referenciaDatabase = FirebaseDatabase.getInstance().getReference();
        }

        return referenciaDatabase;
    }
}
