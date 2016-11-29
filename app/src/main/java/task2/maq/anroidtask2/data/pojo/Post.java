package task2.maq.anroidtask2.data.pojo;

public class Post {

    private String author;

    private String date;

    private String content;

    public Post(String author, String date, String content) {
        this.author = author;
        this.date = date;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }
}
