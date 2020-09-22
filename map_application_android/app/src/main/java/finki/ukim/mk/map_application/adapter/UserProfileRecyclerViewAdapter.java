package finki.ukim.mk.map_application.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import finki.ukim.mk.map_application.R;
import finki.ukim.mk.map_application.adapter.listener.onMapItemClickListener;
import finki.ukim.mk.map_application.adapter.listener.onMapItemDeleteBtnClickListener;
import finki.ukim.mk.map_application.adapter.listener.onMapItemEditBtnClickListener;
import finki.ukim.mk.map_application.model.Map;
import finki.ukim.mk.map_application.model.Visibility;
import finki.ukim.mk.map_application.viewmodel.UserProfileViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class UserProfileRecyclerViewAdapter extends RecyclerView.Adapter<UserProfileRecyclerViewAdapter.MapHolder> {
    private List<Map> maps = new ArrayList<>();
    private onMapItemClickListener rvItemClickListener;
    private onMapItemDeleteBtnClickListener rvItemDeleteBtnClickListener;
    private onMapItemEditBtnClickListener rvItemEditBtnClickListener;

    public UserProfileRecyclerViewAdapter(onMapItemClickListener rvItemClickListener
            , onMapItemDeleteBtnClickListener rvItemDeleteBtnClickListener
            , onMapItemEditBtnClickListener rvItemEditBtnClickListener) {

        this.rvItemClickListener = rvItemClickListener;
        this.rvItemDeleteBtnClickListener = rvItemDeleteBtnClickListener;
        this.rvItemEditBtnClickListener = rvItemEditBtnClickListener;
    }

    class MapHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mediaImageView;
        private TextView titleTextView;
        private TextView secondaryTextTextView;
        private TextView descriptionTextView;
        private onMapItemClickListener itemClickListener;
        private onMapItemDeleteBtnClickListener itemDeleteBtnClickListener;
        private onMapItemEditBtnClickListener itemEditBtnClickListener;
        private Button editBtn;
        private Button deleteBtn;

        public MapHolder(@NonNull View itemView, onMapItemClickListener itemClickListener
                , onMapItemDeleteBtnClickListener itemDeleteBtnClickListener
                , onMapItemEditBtnClickListener itemEditBtnClickListener) {
            super(itemView);
            this.mediaImageView = itemView.findViewById(R.id.media_image_view);
            this.titleTextView = itemView.findViewById(R.id.title_text_view);
            this.secondaryTextTextView = itemView.findViewById(R.id.secondary_text_text_view);
            this.descriptionTextView = itemView.findViewById(R.id.description_text_view);
            this.itemClickListener = itemClickListener;
            this.itemDeleteBtnClickListener = itemDeleteBtnClickListener;
            this.itemEditBtnClickListener = itemEditBtnClickListener;
            this.editBtn = itemView.findViewById(R.id.btn_edit_card);
            this.deleteBtn = itemView.findViewById(R.id.btn_delete_card);


            itemView.setOnClickListener(this);

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemDeleteBtnClickListener.onMapItemDeleteBtnClick(getAdapterPosition());
                }
            });

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemEditBtnClickListener.onMapItemEditBtnClick(getAdapterPosition());
                }
            });

        }

        @Override
        public void onClick(View v) {
            itemClickListener.onMapItemClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public UserProfileRecyclerViewAdapter.MapHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mapItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.map_card_item, parent, false);
        return new UserProfileRecyclerViewAdapter.MapHolder(mapItemView, rvItemClickListener, rvItemDeleteBtnClickListener, rvItemEditBtnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserProfileRecyclerViewAdapter.MapHolder holder, int position) {
        Map currentMap = maps.get(position);

        if(currentMap.getBackground().startsWith("http")) {
            Picasso.with(holder.itemView.getContext()).load(currentMap.getBackground()).into(holder.mediaImageView);
            holder.titleTextView.setText(currentMap.getName());
        }else if(currentMap.getBackground().startsWith("/images/")){
            String imgUrl = "http://192.168.1.101:3000/"+currentMap.getBackground();
            Picasso.with(holder.itemView.getContext()).load(imgUrl).into(holder.mediaImageView);
            holder.titleTextView.setText(currentMap.getName());

        }

        String secondary = "By " + currentMap.getOwner().getName();
        holder.secondaryTextTextView.setText(secondary);

//        String description = currentMap.getDescription();
//        if (description.length() > 30) {
//            description = description.subSequence(0, 30) + " ...";
//        }
//        holder.descriptionTextView.setText(description);

        String status = "";
        int color = 0;
        if(currentMap.getVisibility() == Visibility.PRIVATE){
            status = "PRIVATE";
            color = Color.GRAY;
        }else if(currentMap.getVisibility() == Visibility.PUBLIC && currentMap.getApproved() == 0){
            status = "PENDING";
            color = Color.GRAY;
        }else if(currentMap.getVisibility() == Visibility.PUBLIC && currentMap.getApproved() == 1){
            status = "APPROVED";
            color = Color.rgb(0,133,119);
        }else if(currentMap.getVisibility() == Visibility.PUBLIC && currentMap.getApproved() == 2){
            status = "DISAPPROVED";
            color = Color.RED;
        }

        holder.descriptionTextView.setText(status);
        holder.descriptionTextView.setTextColor(color);
//        holder.textViewMapId.setText(currentMap.getId());
    }

    @Override
    public int getItemCount() {
        return maps.size();
    }

    public void setMaps(List<Map> maps) {
        this.maps = maps;
        notifyDataSetChanged();
    }
}

