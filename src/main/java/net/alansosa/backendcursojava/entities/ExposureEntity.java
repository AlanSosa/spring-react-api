package net.alansosa.backendcursojava.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "exposures")
public class ExposureEntity implements Serializable {

    private static long serialversionUID = 1l;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 50)
    private String type;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "exposure")
    private List<PostEntity> posts = new ArrayList<>();

    public List<PostEntity> getPosts() {
        return posts;
    }

    public void setPosts(List<PostEntity> posts) {
        this.posts = posts;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
