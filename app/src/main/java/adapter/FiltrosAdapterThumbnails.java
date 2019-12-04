package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.List;

import br.com.raveline.redinfunusers.R;

public class FiltrosAdapterThumbnails extends RecyclerView.Adapter<FiltrosAdapterThumbnails.MyViewHolder> {

    private final List<ThumbnailItem> listaFiltros;
    private final Context context;

    public FiltrosAdapterThumbnails(List<ThumbnailItem> listaFiltros, Context context) {
        this.listaFiltros = listaFiltros;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_filtros,parent,false);
        return new FiltrosAdapterThumbnails.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //recuperar item
        ThumbnailItem item = listaFiltros.get(position);

        holder.imagem.setImageBitmap(item.image);
        holder.nomeFiltro.setText(item.filterName);
    }

    @Override
    public int getItemCount() {
        return listaFiltros.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        final ImageView imagem;
        final TextView nomeFiltro;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagem = itemView.findViewById(R.id.imagem_id_filtro_adapter);
            nomeFiltro = itemView.findViewById(R.id.nome_id_filtro_adapter);

        }
    }
}
