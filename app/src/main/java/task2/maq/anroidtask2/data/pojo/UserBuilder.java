package task2.maq.anroidtask2.data.pojo;

public class UserBuilder {

    private int id;

    private String name;

    public UserBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public User build() {
        return new User(name, id);
    }

}
