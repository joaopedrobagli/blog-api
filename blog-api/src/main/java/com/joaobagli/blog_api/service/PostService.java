package com.joaobagli.blog_api.service;

import com.joaobagli.blog_api.dto.post.PostRequest;
import com.joaobagli.blog_api.dto.post.PostResponse;
import com.joaobagli.blog_api.entity.Post;
import com.joaobagli.blog_api.entity.User;
import com.joaobagli.blog_api.repository.PostRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private PostResponse toResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getUsername(),
                post.getCreatedAt()
        );
    }

    public PostResponse create(PostRequest request) {
        User user = getCurrentUser();
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthor(user);
        return toResponse(postRepository.save(post));
    }

    public List<PostResponse> findAll() {
        return postRepository.findAll().stream().map(this::toResponse).toList();
    }

    public PostResponse findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));
        return toResponse(post);
    }

    public PostResponse update(Long id, PostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));
        User user = getCurrentUser();
        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new RuntimeException("Sem permissão para editar este post");
        }
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        return toResponse(postRepository.save(post));
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));
        User user = getCurrentUser();
        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new RuntimeException("Sem permissão para deletar este post");
        }
        postRepository.delete(post);
    }
}