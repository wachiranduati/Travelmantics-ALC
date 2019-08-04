package android.example.travelmantics;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private final int RC_SIGN_IN = 201;
    RecyclerView travelManticsRecyclerView;
    ArrayList<TravelPack> mTravelPackData = new ArrayList<>();
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRef = mDatabase.getReference("getaways");
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: activity created");
        travelManticsRecyclerView = findViewById( R.id.travelManticsRecyclerView);
        setData();
    }

    private void setUpRecyclerAdapter() {
        Log.d(TAG, "setUpRecyclerAdapter: recycler adapter created");
        TravelRecyclerAdapter adapter = new TravelRecyclerAdapter(HomeActivity.this, mTravelPackData);
        travelManticsRecyclerView.setAdapter(adapter);
        travelManticsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.home_screen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.create_TP) {
            //call the signin method now to redirect to create new screen
            // check is user signed in
            if (isUsersignedIn() != true) {
                signInsignUpUser();
            } else {
                movetoCreateActivity();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    private boolean isUsersignedIn() {
        if (user != null) {
            return true;
        }
        return false;
    }

    private void movetoCreateActivity() {
        startActivity(new Intent(HomeActivity.this, CreateTrackActivity.class));
    }

    private void signInsignUpUser() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.mountain)
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    private void setData() {
        Log.d(TAG, "setData: data added");
        Toast.makeText(this, "loading data", Toast.LENGTH_SHORT).show();
//        mTravelPackData.add(new TravelPack("image one", "Diani Sandy Reef", "Neptune 5 star hotel in the woods", "5000"));
//        mTravelPackData.add(new TravelPack("image one", "Bamburi white sands", "Neptune 5 star hotel in the woods", "5000"));
//        mTravelPackData.add(new TravelPack("image one", "Tsavo National Park", "Neptune 5 star hotel in the woods", "5000"));
//        mTravelPackData.add(new TravelPack("image one", "Watamu ", "Neptune 5 star hotel in the woods", "5000"));
//        mTravelPackData.add(new TravelPack("image one", "Kilindini Harbour", "Neptune 5 star hotel in the woods", "52000"));
//        mTravelPackData.add(new TravelPack("image one", "Ship Wreck-cove", "Neptune 5 star hotel in the woods", "5000"));
//        mTravelPackData.add(new TravelPack("image one", "Kwetu Oyster Farms", "Neptune 5 star hotel in the woods", "5000"));
//        mTravelPackData.add(new TravelPack("image one", "Mikoko Pamoja Initiative", "Neptune 5 star hotel in the woods", "5000"));
//        mTravelPackData.add(new TravelPack("image one", "Kibandanski", "Neptune 5 star hotel in the woods", "15000"));

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TravelPack travelPack = dataSnapshot.getValue(TravelPack.class);
                Toast.makeText(HomeActivity.this, ""+travelPack.getmDestinationTitle(), Toast.LENGTH_SHORT).show();
                mTravelPackData.add(new TravelPack(travelPack.getmImageLink(), travelPack.getmDestinationTitle(), travelPack.getmDestinationLocation(), travelPack.getmDestinationPrice()));
                setUpRecyclerAdapter();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                user = FirebaseAuth.getInstance().getCurrentUser();
                movetoCreateActivity();
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Toast.makeText(this, "Oops ran into an Error, Please try again later", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
