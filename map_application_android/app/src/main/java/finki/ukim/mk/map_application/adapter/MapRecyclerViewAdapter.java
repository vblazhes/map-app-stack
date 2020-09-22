package finki.ukim.mk.map_application.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import finki.ukim.mk.map_application.R;
import finki.ukim.mk.map_application.adapter.listener.onMapItemClickListener;
import finki.ukim.mk.map_application.model.Map;

import java.util.ArrayList;
import java.util.List;

public class MapRecyclerViewAdapter extends RecyclerView.Adapter<MapRecyclerViewAdapter.MapHolder> implements Filterable {

    private List<Map> maps = new ArrayList<>();
    private onMapItemClickListener rvItemClickListener;
    private List<Map> allMaps;

    public MapRecyclerViewAdapter(onMapItemClickListener rvItemClickListener) {
        this.rvItemClickListener = rvItemClickListener;
    }

    @Override
    public Filter getFilter() {
        return mapsFilter;
    }

    private Filter mapsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Map> filteredMaps = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredMaps.addAll(allMaps);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Map map : allMaps){
                    if(map.getName().toLowerCase().contains(filterPattern)){
                        filteredMaps.add(map);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredMaps;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            maps.clear();
            maps.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    class MapHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView mediaImageView;
        private TextView titleTextView;
        private TextView secondaryTextTextView;
        private TextView descriptionTextView;
        private onMapItemClickListener itemClickListener;

        public MapHolder(@NonNull View itemView, onMapItemClickListener listener) {
            super(itemView);
            this.mediaImageView = itemView.findViewById(R.id.media_image_view);
            this.titleTextView = itemView.findViewById(R.id.title_text_view);
            this.secondaryTextTextView = itemView.findViewById(R.id.secondary_text_text_view);
            this.descriptionTextView = itemView.findViewById(R.id.description_text_view);
            this.itemClickListener = listener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onMapItemClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MapHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mapItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.explore_map_card_item, parent,false);
        return new MapHolder(mapItemView, rvItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MapHolder holder, int position) {
        Map currentMap = maps.get(position);

        if(currentMap.getBackground().startsWith("http")) {
            Picasso.with(holder.itemView.getContext()).load(currentMap.getBackground()).into(holder.mediaImageView);
        }else if(currentMap.getBackground().startsWith("/images/")){
            String imgUrl = "http://192.168.1.101:3000/"+currentMap.getBackground();
            Picasso.with(holder.itemView.getContext()).load(imgUrl).into(holder.mediaImageView);
        }
        holder.titleTextView.setText(currentMap.getName());

        String secondary = "By "+ currentMap.getOwner().getName();
        holder.secondaryTextTextView.setText(secondary);

        String description = currentMap.getDescription();
        if(description.length() >30){
            description = description.subSequence(0, 30)+" ...";
        }
        holder.descriptionTextView.setText(description);
    }

    @Override
    public int getItemCount() {
        return maps.size();
    }

    public void setMaps(List<Map> maps){
        this.maps = maps;
        this.allMaps = new ArrayList<>(maps);
        notifyDataSetChanged();
    }
}
