package net.alansosa.backendcursojava.services;

import net.alansosa.backendcursojava.models.shared.dto.PostCreationDto;
import net.alansosa.backendcursojava.models.shared.dto.PostDto;

import java.util.List;

public interface PostServiceInterface {

    public PostDto createPost(PostCreationDto post);

    public List<PostDto> getLastPosts();

    public PostDto getPost(String postId);

    public void deletePost(String postId, long userId);

    public PostDto updatePost(String postId, long userId, PostCreationDto updatedPostDto);
}
