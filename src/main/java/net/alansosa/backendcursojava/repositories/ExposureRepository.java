package net.alansosa.backendcursojava.repositories;

import net.alansosa.backendcursojava.entities.ExposureEntity;
import net.alansosa.backendcursojava.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface ExposureRepository extends CrudRepository<ExposureEntity,Long> {

    //Lets Overwrite the findById
    ExposureEntity findById(long id);
}
