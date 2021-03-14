package net.alansosa.backendcursojava.repositories;

import net.alansosa.backendcursojava.entities.PostEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
//public interface PostRepository extends CrudRepository<PostEntity, Long>{
//CrudRepository allow the repository to perform CRUD operations.
//PagingAndSortingRepository allow to create pagination and sorting data + CRUD operations
//We need this because we want to get the user's posts ordered by the field created_at
public interface PostRepository extends PagingAndSortingRepository<PostEntity, Long> {

    /*
    This is almost like findBy in UserRepository. this allow us to find objects using an specific field
    for query.

    But in this instance if we need to get a collection of data using an specific field as our
    query, we use getBy<FieldName>

    Example:
    getByContent

    NOTE it is needed to have a relation in the field.

    for our case, we will bring all the posts that an user has. The foreign key that has this relation
    in the table post is user_id, so we need a method named getByUserId();

    since we're sorting the collection by the CreatedAt date. we named the method like so:
    getBy<Field>OrderBy<Field><Asc or Desc>
    getByUserIdOrderByCreatedAtDesc()

    This is a feature that JPA handle.
    */

    //This method gets all posts using by an UserId having a descendant order by created_at date.
    List<PostEntity> getByUserIdOrderByCreatedAtDesc(long userId);

    //Here we can create custom querys using @Query annotation.
    //value contiene la query 'p' works the same as lambda expressions. and the @Param() contains the like variables
    //we can use in the query. for example p.expires_at > :now
    @Query(value = "SELECT * FROM posts p WHERE p.exposure_id = :exposure AND p.expired_at > :now ORDER BY created_at DESC LIMIT 20", nativeQuery = true)
    List<PostEntity> getLastPublicPosts(@Param("exposure") long exposureId, @Param("now") Date now);

    //findBypost_id
    PostEntity findByPostId(String postId);
}
