package com.joaobagli.blog_api.controller;

import com.joaobagli.blog_api.dto.comment.CommentRequest;
import com.joaobagli.blog_api.dto.comment.CommentResponse;
import com.joaobagli.blog_api.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponse> create(@PathVariable Long postId,
                                                   @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.create(postId, request));
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> findByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.findByPost(postId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long postId, @PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}