package net.alansosa.backendcursojava.services;

import net.alansosa.backendcursojava.entities.ExposureEntity;
import net.alansosa.backendcursojava.entities.PostEntity;
import net.alansosa.backendcursojava.entities.UserEntity;
import net.alansosa.backendcursojava.exceptions.PostNotRelatedException;
import net.alansosa.backendcursojava.models.shared.dto.PostCreationDto;
import net.alansosa.backendcursojava.models.shared.dto.PostDto;
import net.alansosa.backendcursojava.repositories.ExposureRepository;
import net.alansosa.backendcursojava.repositories.PostRepository;
import net.alansosa.backendcursojava.repositories.UserRepository;
import net.alansosa.backendcursojava.utils.Exposures;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PostService implements PostServiceInterface{

    //Now that we created the PostRepository interface. We can use it by autowiring it.
    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ExposureRepository exposureRepository;

    @Autowired
    ModelMapper mapper;

    @Override
    public PostDto createPost(PostCreationDto post) {
        UserEntity userEntity = userRepository.findByEmail(post.getUserEmail());
        ExposureEntity exposureEntity = exposureRepository.findById(post.getExposureId());
        PostEntity postEntity = new PostEntity();
        postEntity.setUser(userEntity);
        postEntity.setExposure(exposureEntity);
        postEntity.setTitle(post.getTitle());
        postEntity.setContent(post.getContent());
        postEntity.setPostId(UUID.randomUUID().toString());
        //Get the current date and add up the milliseconds in the client request.
        //multiply the time from the client from minutes to milliseconds.
        postEntity.setExpiredAt( new Date( System.currentTimeMillis() + (post.getExpirationTime() * 60000)));

        PostEntity createdPost = postRepository.save(postEntity);
        PostDto postToReturn = mapper.map(createdPost, PostDto.class);

        return postToReturn;
    }

    @Override
    public List<PostDto> getLastPosts() {
        List<PostEntity> postEntityList = postRepository.getLastPublicPosts(
                Exposures.PUBLIC, new Date(System.currentTimeMillis()));
        List<PostDto> postDtoList = new ArrayList<>();
        for(PostEntity post: postEntityList){
            PostDto postDto = mapper.map(post, PostDto.class);
            postDtoList.add(postDto);
        }
        return postDtoList;
    }

    @Override
    public PostDto getPost(String postId) {
        PostEntity post = postRepository.findByPostId(postId);
        PostDto postDto = mapper.map(post, PostDto.class);
        return postDto;
    }

    @Override
    public void deletePost(String postId, long userId) {
        //Find post
        PostEntity post = postRepository.findByPostId(postId);

        //Check if the post corresponds to the authenticated user
        if(post.getUser().getId() != userId){
            throw new PostNotRelatedException("Can't perform this operation");
        }
        //Delete the post
        postRepository.delete(post);
    }

    @Override
    public PostDto updatePost(String postId, long userId, PostCreationDto updatedPostDto) {

        //Find post
        PostEntity postEntity = postRepository.findByPostId(postId);

        //Check if the post corresponds to the authenticated user
        if(postEntity.getUser().getId() != userId){
            throw new PostNotRelatedException("Can't perform this operation");
        }

        //Find the exposure
        ExposureEntity exposure = exposureRepository.findById(updatedPostDto.getExposureId());

        //Update the exposure
        postEntity.setExposure(exposure);
        //update the other values
        postEntity.setTitle(updatedPostDto.getTitle());
        postEntity.setContent(updatedPostDto.getContent());
        postEntity.setExpiredAt( new Date( System.currentTimeMillis() + (updatedPostDto.getExpirationTime() * 60000)));

        //Save to the database
        PostEntity updatedPost = postRepository.save(postEntity);
        //Map to postDTO
        PostDto postDto = mapper.map(updatedPost, PostDto.class);

        return postDto;
    }
}
