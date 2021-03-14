package net.alansosa.backendcursojava.services;

import net.alansosa.backendcursojava.models.shared.dto.PostDto;
import net.alansosa.backendcursojava.models.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserServiceInterface extends UserDetailsService {

    public UserDto createUser(UserDto user);
    public UserDto getUser(String email);
    public List<PostDto> getUserPosts(String email);
}
