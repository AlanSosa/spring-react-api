package net.alansosa.backendcursojava.repositories;

import net.alansosa.backendcursojava.entities.UserEntity;
import net.alansosa.backendcursojava.models.shared.dto.UserDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/*
Repositories let us add CRUD functionality to an @Entity
To do this it is necessary to extend from the CrudRepository

Once extending form crudRepository the 2 values sent are:
The @Entity model, and the data type that the @Id is.
in our UserEntity model our @Id field is Long

CrudRepository uses JPA
* */
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    /*
    We can created what whatever we need in the DB. in order to create a method that finds by an
    specific attribute. It's needed to create a method with the following conventions:

    findBy<Parameter>
    findBy -> Obligatory, the library understands this as it will find the data using this as key.

    <Parameter> This can be any value that's in the UserEntity. Crud Repository returns this objects.
    so, it has to be named in camelcase.

    example:
    findByFirstName()
    Looks for an UserEntity by it's firstName.

    The values HAVE TO EXIST in the UserEntity model.
    * */
    UserEntity findByEmail(String email);
}
