package nazianoorani.popularmoviesapp.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nazianoorani on 31/01/16.
 */
public class MovieDetailsDto implements Parcelable{
    private String posterpath;
    private String backdroppath;
    private String id;
    private String title;
    private String releaseDate;
    private String plotSynopsis;
    private String voteAverage;


    public void setBackdroppath(String backdroppath) {
        this.backdroppath = backdroppath;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    public void setPosterpath(String posterpath) {
        this.posterpath = posterpath;
    }
    public String getId() {

        return id;
    }


    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getBackdroppath() {return backdroppath;}

    public String getPosterpath() {return posterpath; }

    public static final Parcelable.Creator<MovieDetailsDto> CREATOR = new Creator<MovieDetailsDto>() {
        @Override
        public MovieDetailsDto createFromParcel(Parcel source) {
            MovieDetailsDto movieDetailsDto = new MovieDetailsDto();
            movieDetailsDto.posterpath =source.readString();
            movieDetailsDto.backdroppath = source.readString();
            movieDetailsDto.id = source.readString();
            movieDetailsDto.title = source.readString();
            movieDetailsDto.releaseDate = source.readString();
            movieDetailsDto.plotSynopsis = source.readString();
            movieDetailsDto.voteAverage = source.readString();
            return movieDetailsDto;
        }

        @Override
        public MovieDetailsDto[] newArray(int size) {
            return new MovieDetailsDto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterpath);
        dest.writeString(backdroppath);
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(plotSynopsis);
        dest.writeString(voteAverage);
    }
}
