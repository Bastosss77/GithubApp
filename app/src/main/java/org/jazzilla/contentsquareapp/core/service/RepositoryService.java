package org.jazzilla.contentsquareapp.core.service;

import org.jazzilla.contentsquareapp.core.githubmodel.CommitCompareGithubModel;
import org.jazzilla.contentsquareapp.core.githubmodel.CommitGithubModel;
import org.jazzilla.contentsquareapp.core.githubmodel.IssueGithubModel;
import org.jazzilla.contentsquareapp.core.githubmodel.PullRequestGithubModel;
import org.jazzilla.contentsquareapp.core.githubmodel.RepositoryBranchGithubModel;
import org.jazzilla.contentsquareapp.core.githubmodel.RepositoryGithubModel;
import org.jazzilla.contentsquareapp.core.githubmodel.SearchRepositoryGithubModel;
import org.jazzilla.contentsquareapp.core.githubmodel.UserGithubModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface RepositoryService {
    String REPOSITORIES_ENDPOINT = "/repositories";
    String REPOSITORY_ENDPOINT = "/repos/{owner}/{repo}";
    String SEARCH_REPOSITORIES_ENDPOINT = "/search/repositories";
    String REPOSITORY_BRANCHES_ENDPOINT = "/repos/{owner}/{repo}/branches";
    String REPOSITORY_CONTRIBUTORS_ENDPOINT = "/repos/{owner}/{repo}/contributors";
    String REPOSITORY_ISSUES_ENDPOINT = "/repos/{owner}/{repo}/issues";
    String REPOSITORY_PULL_REQUEST_ENDPOINT = "/repos/{owner}/{repo}/pulls";
    String REPOSITORY_COMMITS_ENDPOINT = "/repos/{owner}/{repo}/commits";
    String REPOSITORY_COMPARE_ENDPOINT = "/repos/{owner}/{repo}/compare/{base}...{head}";

    @Headers("Accept: application/vnd.github.v3+json")
    @GET(REPOSITORIES_ENDPOINT)
    Call<List<RepositoryGithubModel>> getRepos(@QueryMap Map<String, String> params);

    @Headers("Accept: application/vnd.github.v3+json")
    @GET(SEARCH_REPOSITORIES_ENDPOINT)
    Call<SearchRepositoryGithubModel> searchRepositories(@QueryMap Map<String, String> params);

    @Headers("Accept: application/vnd.github.v3+json")
    @GET(REPOSITORY_BRANCHES_ENDPOINT)
    Call<List<RepositoryBranchGithubModel>> getBranches(@Path("owner") String owner,
                                                        @Path("repo") String repository,
                                                        @QueryMap Map<String, String> params);

    @Headers("Accept: application/vnd.github.v3+json")
    @GET(REPOSITORY_CONTRIBUTORS_ENDPOINT)
    Call<List<UserGithubModel>> getContributors(@Path("owner") String owner,
                                                @Path("repo") String repository,
                                                @QueryMap Map<String, String> params);

    @Headers("Accept: application/vnd.github.v3+json")
    @GET(REPOSITORY_ISSUES_ENDPOINT)
    Call<List<IssueGithubModel>> getIssues(@Path("owner") String owner,
                                           @Path("repo") String repository,
                                           @QueryMap Map<String, String> params);

    @Headers("Accept: application/vnd.github.v3+json")
    @GET(REPOSITORY_PULL_REQUEST_ENDPOINT)
    Call<List<PullRequestGithubModel>> getPullRequests(@Path("owner") String owner,
                                                       @Path("repo") String repository,
                                                       @QueryMap Map<String, String> params);

    @Headers("Accept: application/vnd.github.v3+json")
    @GET(REPOSITORY_COMMITS_ENDPOINT)
    Call<List<CommitGithubModel>> getCommits(@Path("owner") String owner,
                                             @Path("repo") String repository,
                                             @QueryMap Map<String, String> params);

    @Headers("Accept: application/vnd.github.v3+json")
    @GET(REPOSITORY_COMPARE_ENDPOINT)
    Call<CommitCompareGithubModel> compareCommits(@Path("owner") String owner,
                                                  @Path("repo") String repository,
                                                  @Path("base") String baseCommit,
                                                  @Path("head") String headCommit);

    @Headers("Accept: application/vnd.github.v3+json, application/vnd.github.mercy-preview+json")
    @GET(REPOSITORY_ENDPOINT)
    Call<RepositoryGithubModel> getRepository(@Path("owner") String owner,
                                                 @Path("repo") String repository);

}
