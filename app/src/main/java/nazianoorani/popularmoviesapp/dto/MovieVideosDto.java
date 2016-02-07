package nazianoorani.popularmoviesapp.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nazianoorani on 03/02/16.
 */
public class MovieVideosDto implements Parcelable {
    private String key;
    private String name;
    private String type;



    public static final Creator<MovieVideosDto> CREATOR = new Creator<MovieVideosDto>() {
        @Override
        public MovieVideosDto createFromParcel(Parcel in) {
            MovieVideosDto movieVideosDto = new MovieVideosDto();
            movieVideosDto.key = in.readString();
            movieVideosDto.name = in.readString();
            movieVideosDto.type = in.readString();
            return movieVideosDto;
        }

        @Override
        public MovieVideosDto[] newArray(int size) {
            return new MovieVideosDto[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(type);
    }
}
