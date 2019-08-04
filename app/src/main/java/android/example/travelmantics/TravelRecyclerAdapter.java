package android.example.travelmantics;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TravelRecyclerAdapter extends RecyclerView.Adapter<TravelRecyclerAdapter.viewHolder>{

    private static final String TAG = "TravelRecyclerAdapter";
    private Context context;
    private ArrayList<TravelPack> mTravelDestinations;

    public TravelRecyclerAdapter(Context context, ArrayList<TravelPack> mTravelDestinations) {
        this.context = context;
        this.mTravelDestinations = mTravelDestinations;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_items, viewGroup, false);
        viewHolder holder = new viewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        // use glide to set imageview
        viewHolder.destinationtextView.setText(mTravelDestinations.get(i).getmDestinationTitle());
        viewHolder.dLocationTextView.setText(mTravelDestinations.get(i).getmDestinationLocation());
        viewHolder.vacationPrice.setText(mTravelDestinations.get(i).getmDestinationPrice());
        Glide
                .with(viewHolder.travelImageVew.getContext())
                .load(mTravelDestinations.get(i).getmImageLink())
                .centerCrop()
                .placeholder(R.drawable.mountain)
                .into(viewHolder.travelImageVew);
        final int position = i;
        viewHolder.ListingConstrainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+mTravelDestinations.get(position).getmDestinationTitle(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), ViewTravelPack.class);
                intent.putExtra("travelPack", mTravelDestinations.get(position));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTravelDestinations.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

            ImageView travelImageVew;
            TextView destinationtextView;
            TextView dLocationTextView;
            TextView vacationPrice;
            ConstraintLayout ListingConstrainLayout;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            travelImageVew = itemView.findViewById(R.id.recyclerImageView);
            destinationtextView = itemView.findViewById(R.id.destinationtextView);
            dLocationTextView = itemView.findViewById(R.id.dLocationTextView);
            vacationPrice = itemView.findViewById(R.id.vacationPrice);
            ListingConstrainLayout = itemView.findViewById(R.id.ListingConstrainLayout);
        }
    }
}
