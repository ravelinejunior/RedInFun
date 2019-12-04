package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;
import java.util.Objects;

import br.com.raveline.redinfunusers.R;

public class AdapterGridFotosAcompanhante extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> urlFotos;
    private final int layoutResource;
    private ImageView imagemGridFotos;
    private ProgressBar progressBarAdapterGridFotos;

    public AdapterGridFotosAcompanhante(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResource = resource;
        this.urlFotos = objects;



    }

    //Cria classe View Holder
    class ViewHolder {
        ImageView imagem;
        ProgressBar progressBar;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       // imagemGridFotos = convertView.findViewById(R.id.imagem_icone_id_grid_acompanhante);
       // progressBarAdapterGridFotos = convertView.findViewById(R.id.progressBar_id_grid_acompanhante);

        //RecyclerView.ViewHolder viewHolder;
        final ViewHolder viewHolder;
        //se a view nao estiver inflada, é preciso inflá-la
        if (convertView == null){
            viewHolder = new ViewHolder();
          /*  viewHolder = new RecyclerView.ViewHolder(convertView) {
                @Override
                public String toString() {
                    return super.toString();
                }
            };*/
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = Objects.requireNonNull(layoutInflater).inflate(layoutResource,parent,false);
            viewHolder.imagem = convertView.findViewById(R.id.imagem_icone_id_grid_acompanhante);
            viewHolder.progressBar = convertView.findViewById(R.id.progressBar_id_grid_acompanhante);

            convertView.setTag(viewHolder);

        }else{
           viewHolder = (ViewHolder) convertView.getTag();
        }

        //recuperar dados da imagem
        String urlImagem = getItem(position);
        //carregando varias imagens utilizando o ImageHolder
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(urlImagem, viewHolder.imagem, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                viewHolder.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                viewHolder.progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Não foi possivel carregar imagem", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                viewHolder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                viewHolder.progressBar.setVisibility(View.GONE);

            }
        });


        return convertView;
    }
}
