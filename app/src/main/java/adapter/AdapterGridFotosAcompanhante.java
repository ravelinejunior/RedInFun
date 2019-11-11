package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.raveline.redinfunusers.R;

public class AdapterGridFotosAcompanhante extends ArrayAdapter<String> {
    private Context context;
    private List<String> urlFotos;
    private int layoutResource;
    private ImageView imagemGridFotos;
    private ProgressBar progressBarAdapterGridFotos;

    public AdapterGridFotosAcompanhante(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResource = resource;
        this.urlFotos = objects;



    }

    //Cria classe View Holder
    public class ViewHolder {
        ImageView imagem;
        ProgressBar progressBar;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       // imagemGridFotos = convertView.findViewById(R.id.imagem_icone_id_grid_acompanhante);
       // progressBarAdapterGridFotos = convertView.findViewById(R.id.progressBar_id_grid_acompanhante);

        //RecyclerView.ViewHolder viewHolder;
        ViewHolder viewHolder;
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
            convertView = layoutInflater.inflate(layoutResource,parent,false);
            viewHolder.imagem = convertView.findViewById(R.id.imagem_icone_id_grid_acompanhante);
            viewHolder.progressBar = convertView.findViewById(R.id.progressBar_id_grid_acompanhante);

            convertView.setTag(viewHolder);

        }else{
           viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
}
