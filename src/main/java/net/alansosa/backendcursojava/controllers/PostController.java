package net.alansosa.backendcursojava.controllers;

import net.alansosa.backendcursojava.exceptions.PostNotRelatedException;
import net.alansosa.backendcursojava.models.requests.PostCreateRequestModel;
import net.alansosa.backendcursojava.models.responses.OperationStatusModel;
import net.alansosa.backendcursojava.models.responses.PostRest;
import net.alansosa.backendcursojava.models.shared.dto.PostCreationDto;
import net.alansosa.backendcursojava.models.shared.dto.PostDto;
import net.alansosa.backendcursojava.models.shared.dto.UserDto;
import net.alansosa.backendcursojava.services.PostServiceInterface;
import net.alansosa.backendcursojava.services.UserServiceInterface;
import net.alansosa.backendcursojava.utils.Exposures;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/posts") //localhost:8080/posts
public class PostController {
    @Autowired
    PostServiceInterface postService;

    @Autowired
    UserServiceInterface userService;

    @Autowired
    ModelMapper mapper;

    @PostMapping
    public PostRest createPost(@RequestBody @Valid PostCreateRequestModel createRequestModel){
        //Get the Authentication from the Context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //Get the email from the token
        String email = authentication.getPrincipal().toString();
        //Map the request model to PostCreationDto
        PostCreationDto postCreationDto = mapper.map(createRequestModel, PostCreationDto.class);
        //Map the email to the DTO.
        postCreationDto.setUserEmail(email);
        //Create the post
        PostDto post = postService.createPost(postCreationDto);

        PostRest postToReturn = mapper.map(post, PostRest.class);

        return postToReturn;
    }

    @GetMapping("/lasts")//localhost:8080/posts/last
    public List<PostRest> getLastPosts(){
        List<PostDto> postDtoList = postService.getLastPosts();
        List<PostRest> postRestList = new ArrayList<>();

        for(PostDto post : postDtoList ){
            PostRest postRest = mapper.map(post, PostRest.class);
            postRestList.add(postRest);
        }
        //convert from DTO to Rest
        return postRestList;
    }

    //{id} will be a parameter in the url. Will be using the uuid which is the post id -> postId
    @GetMapping(path = "/{postId}") //localhost:8080/posts/uuid
    public PostRest getPost(@PathVariable String postId){

        PostDto post = postService.getPost(postId);
        PostRest postToReturn = mapper.map(post, PostRest.class);

        /*
        Since this is a public endpoint, we need to validate if the post is private.
        The client can get post from other users if they know the UUID.
        This piece of code validate the user only gets their correspondent PUBLIC posts AND that
        are not expired.

        - If user is NOT Authenticated. this endpoint will return it's public post
        - if user is Authenticated. This endpoint will be able to return it's post
        - if user is not authenticated and tries to bring an expired post
            error will be returned:
            {
                "timeStamp": "2021-03-11T15:15:18.449+00:00",
                "message": "anonymousUser"
            }
        - If user tries to get a PRIVATE post from other user an error will return:
        {
            "timeStamp": "2021-03-11T15:22:24.686+00:00",
            "message": "You don't have permissions to perform this action"
        }
        */
        if(postToReturn.getExposure().getId() == Exposures.PRIVATE || postToReturn.isExpired()){

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getPrincipal().toString();

            UserDto user = userService.getUser(email);

            //Validate if the post is from the same user
            if(user.getId() != post.getUser().getId()){
                throw new PostNotRelatedException("You don't have permissions to perform this action");
            }
        }
        return postToReturn;
    }

    @DeleteMapping("/{postId}") //localhost:8080/posts/uuid
    public OperationStatusModel deletePost(@PathVariable String postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        UserDto user = userService.getUser(email);

        //If something fails deletePost will thrown an Exception.
        postService.deletePost(postId, user.getId());

        //If no exception is thrown just create the response
        OperationStatusModel response = new OperationStatusModel("DELETE", "SUCCESS");

        //return the post
        return response;
    }

    @PutMapping("/{postId}") //localhost:8080/posts/uuid
    public PostRest updatePost(@RequestBody @Valid PostCreateRequestModel postCreateRequestModel,
                                           @PathVariable String postId){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        UserDto user = userService.getUser(email);

        PostCreationDto updatedPostDto = mapper.map(postCreateRequestModel, PostCreationDto.class);

        //If something fails updatePost will thrown an Exception.
        PostDto updatePost = postService.updatePost(postId, user.getId(), updatedPostDto);

        //if no exception thrown. Create the response model.
        PostRest postToReturn = mapper.map(updatePost, PostRest.class);

        //return the post
        return postToReturn;
    }
}
