package com.example.apigithub.service;
import com.example.apigithub.model.Branch;
import com.example.apigithub.model.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GithubService {
    @Value("${github.api.url}")
    private String githubApiUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    public List<Repository> getNonForkRepositories (String username){
        String reposUrl = String.format("%s/users/%s/repos", githubApiUrl, username);
        try {
            List<Map<String, Object>> repos = restTemplate.getForObject(reposUrl, List.class);
            List<Repository> result = new ArrayList<>();
            if (repos != null){
                for (Map<String,Object> repo : repos){
                    if (!(Boolean)repo.get("fork")){
                        Repository repository = new Repository();
                        repository.setName((String) repo.get("name"));
                        repository.setOwnerLogin(((Map<String, String>) repo.get("owner")).get("login"));
                        repository.setBranches(getBranches(username, repository.getName()));
                        result.add(repository);
                    }
                }
            }
            return result;
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("User not found");
        }
        }
    private List<Branch> getBranches(String username, String repoName) {
        String branchesUrl = String.format("%s/repos/%s/%s/branches", githubApiUrl, username, repoName);
        List<Map<String, Object>> branches = restTemplate.getForObject(branchesUrl, List.class);
        List<Branch> result = new ArrayList<>();

        if (branches != null) {
            for (Map<String, Object> branch : branches) {
                Branch branchObj = new Branch();
                branchObj.setName((String) branch.get("name"));
                branchObj.setLastCommitSha((String) ((Map<String, Object>) branch.get("commit")).get("sha"));
                result.add(branchObj);
            }
        }

        return result;
    }
    }

