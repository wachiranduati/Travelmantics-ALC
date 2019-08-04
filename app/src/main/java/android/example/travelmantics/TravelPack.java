package android.example.travelmantics;

import android.os.Parcel;
import android.os.Parcelable;

public class TravelPack implements Parcelable {
    private String mImageLink;
    private String mDestinationTitle;
    private String mDestinationLocation;
    private String mDestinationPrice;

    public TravelPack(String mImageLink, String mDestinationTitle, String mDestinationLocation, String mDestinationPrice) {
        this.mImageLink = mImageLink;
        this.mDestinationTitle = mDestinationTitle;
        this.mDestinationLocation = mDestinationLocation;
        this.mDestinationPrice = mDestinationPrice;
    }

    public TravelPack() {
    }

    protected TravelPack(Parcel in) {
        mImageLink = in.readString();
        mDestinationTitle = in.readString();
        mDestinationLocation = in.readString();
        mDestinationPrice = in.readString();
    }

    public static final Creator<TravelPack> CREATOR = new Creator<TravelPack>() {
        @Override
        public TravelPack createFromParcel(Parcel in) {
            return new TravelPack(in);
        }

        @Override
        public TravelPack[] newArray(int size) {
            return new TravelPack[size];
        }
    };

    public String getmImageLink() {
        return mImageLink;
    }

    public String getmDestinationTitle() {
        return mDestinationTitle;
    }

    public String getmDestinationLocation() {
        return mDestinationLocation;
    }

    public String getmDestinationPrice() {
        return mDestinationPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mImageLink);
        dest.writeString(mDestinationTitle);
        dest.writeString(mDestinationLocation);
        dest.writeString(mDestinationPrice);
    }
}
