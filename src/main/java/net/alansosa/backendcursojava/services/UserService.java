package net.alansosa.backendcursojava.services;

import net.alansosa.backendcursojava.entities.PostEntity;
import net.alansosa.backendcursojava.exceptions.EmailExistsException;
import net.alansosa.backendcursojava.models.shared.dto.PostDto;
import net.alansosa.backendcursojava.repositories.PostRepository;
import net.alansosa.backendcursojava.repositories.UserRepository;
import net.alansosa.backendcursojava.entities.UserEntity;
import net.alansosa.backendcursojava.models.shared.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserServiceInterface{

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    ModelMapper mapper;

    @Override
    public UserDto createUser(UserDto user) {

        //First we find if the user exists in the DB.
        if(userRepository.findByEmail(user.getEmail()) != null){
            throw new EmailExistsException("Email already exists");
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        UUID userId = UUID.randomUUID();
        userEntity.setUserId(userId.toString());
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto userToReturn = new UserDto();
        BeanUtils.copyProperties(storedUserDetails, userToReturn);

        return userToReturn;
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null){
            throw new UsernameNotFoundException(email);
        }
        UserDto userToReturn = new UserDto();
        BeanUtils.copyProperties(userEntity, userToReturn);
        return userToReturn;
    }

    @Override
    public List<PostDto> getUserPosts(String email) {
        //find the UserEntity user the email.
        UserEntity userEntity = userRepository.findByEmail(email);
        //Get the list of posts using the User id key
        List<PostEntity> posts = postRepository.getByUserIdOrderByCreatedAtDesc(userEntity.getId());
        List<PostDto> postDtoList = new ArrayList<>();
        //Iterate through the list of entities to parse each one of them to PostDto.
        for(PostEntity post : posts){
            PostDto postDto = mapper.map(post, PostDto.class);
            postDtoList.add(postDto);
        }
        return postDtoList;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null){
            throw new UsernameNotFoundException(email);
        }
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }
}
