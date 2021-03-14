package net.alansosa.backendcursojava.controllers;

import net.alansosa.backendcursojava.models.requests.UserDetailRequestModel;
import net.alansosa.backendcursojava.models.responses.PostRest;
import net.alansosa.backendcursojava.models.responses.UserRest;
import net.alansosa.backendcursojava.models.shared.dto.PostDto;
import net.alansosa.backendcursojava.models.shared.dto.UserDto;
import net.alansosa.backendcursojava.services.UserServiceInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/users") //localhost:8080/users
public class UserController {

    @Autowired
    UserServiceInterface userService;

    @Autowired
    ModelMapper mapper;

    //in @GetMapping we're adding support for XMl and JSON in case it is needed. So it can return
    //the response either in XML or JSON format
    //this ones can be used after the jar com.fasterxml.jackson.dataformat is added in the pom.xml
    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE} )
    public UserRest getUser(){
        //SecurityHolder helps us get the context from the security and get the user's auth data.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //In line 68 in AuthenticationFilter class, we set up the 'email' as the subject.
        //Here we can retrieve it.
        String email = authentication.getPrincipal().toString();

        //Find the userDto object with the email given
        UserDto user = userService.getUser(email);

        UserRest userToReturn = mapper.map(user, UserRest.class);

        return userToReturn;
    }

    //@Valid makes the end point to check with the constraints added to the model.
    @PostMapping
    public UserRest createUser(@RequestBody @Valid UserDetailRequestModel userDetails){

        UserRest userToReturn = new UserRest();

        //This object will be shared across all logic layers
        UserDto userDto = new UserDto();

        //Copies properties from one object to another
        BeanUtils.copyProperties(userDetails, userDto);

        //Get the created user from the user service using our DTO models
        UserDto createdUser = userService.createUser(userDto);

        //Copy properties from the createdUser DTO to the response object.
        BeanUtils.copyProperties(createdUser, userToReturn);

        return userToReturn;
    }

    //This endpoint get user's posts.
    @GetMapping(path = "/posts") //localhost:8080/users/posts
    public List<PostRest> getPosts(){

        //Get the authenticated member with the securityContentx token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();

        List<PostDto> posts = userService.getUserPosts(email);

        List<PostRest> postRestList = new ArrayList<>();
        for(PostDto post : posts ){
            PostRest postRest = mapper.map(post, PostRest.class);
            postRest.isExpired();
            postRestList.add(postRest);
        }

        return postRestList;
    }
}
