package android.example.travelmantics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class CreateTrackActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CreateTrackActivity";
    public static final int REQUEST_CODE_GALLERY = 1323;
    EditText titleEditText, priceEditText, descriptionEditText;
    Button selectImageBtn;
    ImageView travelImageView;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String imageChosen;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = mDatabase.getReference();
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_track);
        // bind all the necessary views first
        // create a util method to ensure all fields are not empty
        // then put value to database
        Toast.makeText(this, "just got created" + user, Toast.LENGTH_SHORT).show();
        redirectUserIfNotAuthenticated();
        bindCreateTrackViews();
        selectImageBtn.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        redirectUserIfNotAuthenticated();
    }

    private void redirectUserIfNotAuthenticated() {
        Log.d(TAG, "redirectUserIfNotAuthenticated: redirectuser method called");
        if(user == null){
            Toast.makeText(this, "Oops you need to sign in to access this screen", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CreateTrackActivity.this, HomeActivity.class));
            finish();
        }
    }

    private void bindCreateTrackViews() {
        titleEditText = findViewById(R.id.titleEditText);
        priceEditText = findViewById(R.id.priceEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        selectImageBtn = findViewById(R.id.selectImageBtn);
        travelImageView = findViewById(R.id.travelImageViewCreate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.create_track_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.create_TP){
//            Toast.makeText(this, "save button clicked", Toast.LENGTH_SHORT).show();
            if(allFieldsFilled() == true){
                
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean allFieldsFilled() {
        if(!TextUtils.isEmpty(titleEditText.getText())){
            if(!TextUtils.isEmpty(priceEditText.getText())){
                if(!TextUtils.isEmpty(descriptionEditText.getText())){
                    if(imageChosen != null){
                        uploadContentToDatabase();
                    }else{
                        Toast.makeText(this, "Please Select an Image", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Please Provide a Value in the Description Field", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Please Provide a Value in the Price Field", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Please Provide a Value in the Destination Title Field", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void uploadContentToDatabase() {
        // upload image to firebase storage first
        dialog = new ProgressDialog(this);
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        StorageReference storageRef = storage.getReference();
        final StorageReference mRef = storageRef.child("getaways/"+ UUID.randomUUID().toString());
        mRef.putFile(Uri.parse(imageChosen))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageDownloadLink = uri.toString();
                                // upload to firebase database
                                writeToFirebaseDatabase(imageDownloadLink);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Image Upload Failed" +e);
                Toast.makeText(CreateTrackActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                dialog.setMessage("uploading "+ progress + "%");
                if(progress >= 100.0){
                    dialog.dismiss();
                }
            }
        });

    }

    private void writeToFirebaseDatabase(String imageDownloadLink) {
        TravelPack travelPack = new TravelPack(imageDownloadLink, titleEditText.getText().toString(), descriptionEditText.getText().toString(), priceEditText.getText().toString());
        databaseReference.child("getaways").child(UUID.randomUUID().toString()).setValue(travelPack)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.setMessage("New GetAway Added successfully");
                        Toast.makeText(CreateTrackActivity.this, "New GetAway Added Successfully", Toast.LENGTH_SHORT).show();
                        resetAllInputs();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(CreateTrackActivity.this, "Error! Item upload could not be completed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetAllInputs() {
        imageChosen = null;
        titleEditText.setText("");
        priceEditText.setText("");
        descriptionEditText.setText("");
        Glide.with(this).load(R.drawable.mountain).into(travelImageView);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.selectImageBtn){
//            Log.d(TAG, "onClick: select image button clicked");
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_GALLERY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_GALLERY){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
//                Toast.makeText(this, "the image "+imageUri, Toast.LENGTH_SHORT).show();
                imageChosen = imageUri.toString();
                setImageToImageView(imageUri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setImageToImageView(Uri imageUri) {
        Glide.with(this).load(imageUri.toString()).into(travelImageView);
    }
}
