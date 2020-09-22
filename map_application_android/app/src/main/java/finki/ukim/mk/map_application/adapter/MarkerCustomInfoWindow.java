package finki.ukim.mk.map_application.adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;
import finki.ukim.mk.map_application.R;
import org.w3c.dom.Text;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MarkerCustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    private final View infoWindow;
    private Context context;


    public MarkerCustomInfoWindow(Context context){
        this.context = context;
        this.infoWindow = LayoutInflater.from(context).inflate(R.layout.marker_custom_info_window, null);
    }

    private void renderInfoWindow(Marker marker, View infoWindow){
        ImageView markerImage = infoWindow.findViewById(R.id.marker_imageView);
        TextView markerName = infoWindow.findViewById(R.id.markerName_textView);

        //Getting pinData

        if(!marker.getTitle().equals("")){
            markerName.setText(marker.getTitle());
        }

        if(!marker.getSnippet().equals("")){
            Picasso.with(context).load(marker.getSnippet()).into(markerImage);

            Log.d(TAG, "renderInfoWindow: snipet: "+marker.getSnippet());
            if(marker.getSnippet().startsWith("http")){
                Picasso.with(context).load(marker.getSnippet()).into(markerImage);
            }else if(marker.getSnippet().startsWith("/images/")){
                String imgUrl = "http://192.168.1.101:3000/"+marker.getSnippet();
            Log.d(TAG, "onBindViewHolder: imgUrl: "+imgUrl);
                Picasso.with(context).load(imgUrl).into(markerImage);
            }
        }

    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderInfoWindow(marker, infoWindow);
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderInfoWindow(marker, infoWindow);
        return infoWindow;
    }
}
