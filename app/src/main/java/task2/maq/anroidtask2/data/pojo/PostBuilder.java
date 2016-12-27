package task2.maq.anroidtask2.data.pojo;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class PostBuilder {

    private String author;

    private DateTime createDate;

    private String content;

    private String image;

    private int id=-1;

    public PostBuilder withImage(String image) {
        this.image = image;
        return this;
    }

    public PostBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public PostBuilder withAuthor(String author) {
        this.author = author;
        return this;
    }

    public PostBuilder withCreateDate(String createDate, String pattern) {
        this.createDate = DateTimeFormat.forPattern(pattern).parseDateTime(createDate);
        return this;
    }

    public PostBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public Post build() throws InvalidDataException {
        if (isValid()) {
            return new Post(author, createDate, content, id, image);
        } else {
            throw new InvalidDataException();
        }
    }

    private boolean isValid() {
        if (content != null)
            return true;
        else
            return false;
    }
}
