package finki.ukim.mk.map_application.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import finki.ukim.mk.map_application.R;
import finki.ukim.mk.map_application.adapter.listener.onPinItemClickListener;
import finki.ukim.mk.map_application.model.Pin;

import java.util.ArrayList;
import java.util.List;

public class PinRecyclerViewAdapter extends RecyclerView.Adapter<PinRecyclerViewAdapter.PinHolder> {

    private List<Pin> pins = new ArrayList<>();
    private onPinItemClickListener rvPinItemClickListener;

    public PinRecyclerViewAdapter(onPinItemClickListener rvPinItemClickListener){
        this.rvPinItemClickListener = rvPinItemClickListener;
    }

    @NonNull
    @Override
    public PinHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pin_card_item, parent,false);
        return new PinHolder(itemView, rvPinItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PinHolder holder, int position) {
        Pin currentPin = pins.get(position);
        holder.pinName_tv.setText(currentPin.getName());
        String lat = "latitude: "+currentPin.getLatitude();
        String lng = "longitude: "+currentPin.getLongitude();
        holder.lat_tv.setText(lat);
        holder.lng_tv.setText(lng);
        if(currentPin.getImage().startsWith("http")){
            Picasso.with(holder.itemView.getContext()).load(currentPin.getImage()).into(holder.pinImage_iv);
        }else if(currentPin.getImage().startsWith("/images/")){
            String imgUrl = "http://192.168.1.101:3000/"+currentPin.getImage();
//            Log.d(TAG, "onBindViewHolder: imgUrl: "+imgUrl);
            Picasso.with(holder.itemView.getContext()).load(imgUrl).into(holder.pinImage_iv);
        }
    }

    @Override
    public int getItemCount() {
        return pins.size();
    }

    public void setPins(List<Pin> pins){
        this.pins = pins;
        notifyDataSetChanged();
    }

    class PinHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView pinName_tv;
        private TextView lat_tv;
        private TextView lng_tv;
        private ImageView pinImage_iv;
        private onPinItemClickListener pinItemClickListener;

        public PinHolder(@NonNull View itemView, onPinItemClickListener listener) {
            super(itemView);
            pinName_tv = itemView.findViewById(R.id.textView_map_name);
            lat_tv = itemView.findViewById(R.id.textView_lat);
            lng_tv = itemView.findViewById(R.id.textView_lng);
            pinImage_iv = itemView.findViewById(R.id.pin_photo);
            this.pinItemClickListener = listener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            pinItemClickListener.onPinItemClick(getAdapterPosition());
        }
    }
}
