package task2.maq.anroidtask2.data.pojo;

public class PostBuilder {

    private String author;

    private String date;

    private String content;

    public PostBuilder withAuthor(String author) {
        this.author = author;
        return this;
    }

    public PostBuilder withDate(String date) {
        this.date = date;
        return this;
    }

    public PostBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public Post build() {
        return new Post(author, date, content);
    }

    private void validate() {

    }
}
