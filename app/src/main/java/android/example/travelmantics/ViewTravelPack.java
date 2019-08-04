package android.example.travelmantics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ViewTravelPack extends AppCompatActivity {

    TextView travelViewTextView;
    ImageView travelDestImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_travel_pack);
        travelViewTextView = findViewById(R.id.viewDtextView);
        TravelPack travelDetails = getIntent().getParcelableExtra("travelPack");
        travelViewTextView.setText("Destination : "+travelDetails.getmDestinationTitle()+" \n"+
                "Directions : "+travelDetails.getmDestinationLocation()+" \n"+
                "Price : "+travelDetails.getmDestinationPrice());
        travelDestImageView = findViewById(R.id.travelDImageView);
        Glide
                .with(this)
                .load(travelDetails.getmImageLink())
                .centerCrop()
                .placeholder(R.drawable.mountain)
                .into(travelDestImageView);

    }
}
