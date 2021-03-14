package net.alansosa.backendcursojava.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
This Entity is the Java Representation of the USER table in the Database
and contains the fields that the table has.

The @Entity annotation let us to that.
*/
@Entity(name = "users")
/*
This one is used to create indexes. So far we only have one but we want also the email and public userId
in order to create indexes it is needed to create a collection with the parameters
*/
@Table(
        indexes = {
        @Index(columnList = "userId", name = "index_userId", unique = true),
        //'name' is an optional. Because if a name is not assigned to the index a
        //random one will be created. But this time we will assign a name to it.
        //if you check the table in the DB's. There will be 2 new indexes with then names given
        @Index(columnList = "email", name = "index_email", unique = true)
        }
)
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1l;

    /*
    @Id = assign as primary key
    @GeneratedValue = Auto-increment the value once a new User is created
    * */
    @Id
    @GeneratedValue
    private long id;

    //@Column = Declare the properties of the field
    @Column(nullable = false)
    private String userId; //public id

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false)
    private String encryptedPassword;

    /*
    Since an user can have multiple POSTS it's needed to have a @OneToMany relation.
    cascade describes what happens with the posts once an USER is deleted. In this case
    we're deleting all POSTS related to the same user id.
    mappedBy = "user" is the field name that has the relation in PostEntity.
    * */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }
}
