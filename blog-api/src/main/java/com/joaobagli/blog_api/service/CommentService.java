package com.joaobagli.blog_api.service;

import com.joaobagli.blog_api.dto.comment.CommentRequest;
import com.joaobagli.blog_api.dto.comment.CommentResponse;
import com.joaobagli.blog_api.entity.Comment;
import com.joaobagli.blog_api.entity.Post;
import com.joaobagli.blog_api.entity.User;
import com.joaobagli.blog_api.repository.CommentRepository;
import com.joaobagli.blog_api.repository.PostRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor().getUsername(),
                comment.getCreatedAt()
        );
    }

    public CommentResponse create(Long postId, CommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));
        User user = getCurrentUser();
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setAuthor(user);
        comment.setPost(post);
        return toResponse(commentRepository.save(comment));
    }

    public List<CommentResponse> findByPost(Long postId) {
        return commentRepository.findByPostId(postId)
                .stream().map(this::toResponse).toList();
    }

    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentário não encontrado"));
        User user = getCurrentUser();
        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new RuntimeException("Sem permissão para deletar este comentário");
        }
        commentRepository.delete(comment);
    }
}