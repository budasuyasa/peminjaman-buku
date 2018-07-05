package budasuyasa.android.simplecrud.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import budasuyasa.android.simplecrud.Models.Peminjaman;
import budasuyasa.android.simplecrud.R;

public class PinjamAdapter extends RecyclerView.Adapter<PinjamAdapter.ViewHolder> {

    /**
     * Create ViewHolder class to bind list item view
     */
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView nama;
        public TextView lamaPinjam;
        public TextView tanggalPinjam;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            nama = (TextView) itemView.findViewById(R.id.nama);
            lamaPinjam = (TextView) itemView.findViewById(R.id.lamaPinjam);
            tanggalPinjam = (TextView) itemView.findViewById(R.id.tglPinjam);

        }
    }

    private List<Peminjaman> mListData;
    private Context mContext;

    public PinjamAdapter(Context context, List<Peminjaman> listData){
        mListData = listData;
        mContext = context;
    }

    private Context getmContext(){return mContext;}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.pinjam_list_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Peminjaman m = mListData.get(position);
        holder.title.setText(m.getTitle());
        holder.nama.setText(m.getNama());
        holder.lamaPinjam.setText("Lama Pinjam: "+m.getLamaPinjam());
        holder.tanggalPinjam.setText("TangalPinjam: "+ m.getTanggalPinjam());
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }


}
