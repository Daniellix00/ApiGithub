package com.example.apigithub.controller;
import com.example.apigithub.model.Repository;
import com.example.apigithub.service.GithubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GithubController {
    @Autowired
    private GithubService githubService;
    @GetMapping("/{username}/repos")
    public ResponseEntity<?> listRepos(@PathVariable String username) {
        try {
            List<Repository> repos = githubService.getNonForkRepositories(username);
            return ResponseEntity.ok(repos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(
                    new ErrorResponse(404, "User not found")
            );
        }
    }
    private static class ErrorResponse{
        private final int status;
        private final String message;
        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }

}
