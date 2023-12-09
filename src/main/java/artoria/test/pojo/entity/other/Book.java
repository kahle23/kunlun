package artoria.test.pojo.entity.other;

import java.io.Serializable;

/**
 * The book (test bean).
 * @author Kahle
 */
public class Book implements Serializable {
    private Long   id;
    private String name;
    private String author;
    private String publisher;

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getAuthor() {

        return author;
    }

    public void setAuthor(String author) {

        this.author = author;
    }

    public String getPublisher() {

        return publisher;
    }

    public void setPublisher(String publisher) {

        this.publisher = publisher;
    }

}
