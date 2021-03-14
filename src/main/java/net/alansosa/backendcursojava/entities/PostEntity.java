package net.alansosa.backendcursojava.entities;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "posts")
//Assign the postId as an index named index_postId
@Table(
        indexes = {
                @Index(columnList = "postId", name = "index_postId", unique = true)
        }
)
@EntityListeners(AuditingEntityListener.class)// This listeners lets the @CreateDate bean
public class PostEntity implements Serializable {

    private static long serialversionUID = 1l;

    @Id //Assign the ID
    @GeneratedValue //Autoincrement
    private long id;

    //public id
    @Column(nullable = false)
    private String postId;

    @Column(nullable = false, length = 255)
    private String title;

    //columnDefinition = "TEXT" let us have this field as TEXT because kind of type
    //has not defined limit size in mysql.
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Date expiredAt;

    //This annotations enables the entity to automatically fill this filed once a new insertion
    //is done into the database.
    @CreatedDate
    private Date createdAt;

    /*
    Many posts can have only one USER, so in in this case it's needed a @ManyToOne relation.
    IT'S IMPORTANT TO NOTE that the field is named 'user' because that's how it's configured in
    UserEntity.posts, that field is mapped to this one by the field name.
    @JoinColumn creates the field that contains the relation with the name 'user_id' in the posts table.
    * */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    //Same for the exposures. Since an Exposure can have multiple posts. This is a @ManyToOne Relation
    @ManyToOne
    @JoinColumn(name = "exposure_id")
    private ExposureEntity exposure;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ExposureEntity getExposure() {
        return exposure;
    }

    public void setExposure(ExposureEntity exposure) {
        this.exposure = exposure;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Date expiredAt) {
        this.expiredAt = expiredAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
