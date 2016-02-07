package nazianoorani.popularmoviesapp.dto;

/**
 * Created by nazianoorani on 02/02/16.
 */
public class MovieReviewDto {
    private String content;
    private String author;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
