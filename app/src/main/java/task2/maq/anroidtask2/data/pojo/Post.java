package task2.maq.anroidtask2.data.pojo;

import org.joda.time.DateTime;

public class Post {

    private String author;

    private DateTime createDate;

    private String content;

    private String image;

    private int id;

    public Post(String author, DateTime createDate, String content, int id, String image) {
        this.author = author;
        this.createDate = createDate;
        this.content = content;
        this.id = id;
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public DateTime getCreateDate() {
        return createDate;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Post))
            return false;
        Post p = (Post) o;
        if ((p.getId() == this.id) && (p.getCreateDate().equals(this.createDate)))
            return true;
        return  false;
    }
}
