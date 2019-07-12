package org.jazzilla.contentsquareapp.core.provider;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jazzilla.contentsquareapp.common.ConstantsUtils;
import org.jazzilla.contentsquareapp.core.RequestManager;
import org.jazzilla.contentsquareapp.core.appmodel.IssueAppModel;
import org.jazzilla.contentsquareapp.core.appmodel.PullRequestAppModel;
import org.jazzilla.contentsquareapp.core.appmodel.RepositoryAppModel;
import org.jazzilla.contentsquareapp.core.appmodel.RepositoryBranchAppModel;
import org.jazzilla.contentsquareapp.core.appmodel.UserAppModel;
import org.jazzilla.contentsquareapp.core.githubmodel.CommitCompareGithubModel;
import org.jazzilla.contentsquareapp.core.githubmodel.CommitGithubModel;
import org.jazzilla.contentsquareapp.core.githubmodel.IssueGithubModel;
import org.jazzilla.contentsquareapp.core.githubmodel.PullRequestGithubModel;
import org.jazzilla.contentsquareapp.core.githubmodel.RepositoryBranchGithubModel;
import org.jazzilla.contentsquareapp.core.githubmodel.RepositoryGithubModel;
import org.jazzilla.contentsquareapp.core.githubmodel.SearchRepositoryGithubModel;
import org.jazzilla.contentsquareapp.core.githubmodel.UserGithubModel;
import org.jazzilla.contentsquareapp.core.service.RepositoryService;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepositoryProvider {
    public static final int ERROR_NETWORK = 0;
    public static final int ERROR_NO_REPOSITORIES = 1;
    public static final int ERROR_GENERIC = 3;
    public static final int ERROR_NO_BRANCH = 4;
    public static final int ERROR_COMMIT = 5;

    @IntDef({ERROR_NETWORK, ERROR_NO_REPOSITORIES, ERROR_GENERIC, ERROR_NO_BRANCH, ERROR_COMMIT})
    public @interface Error {}

    public static void getAllPublicRepos(int fromRepository, @Nullable final ProviderCallback<List<RepositoryAppModel>> callback) {
        RepositoryService service = RequestManager.getInstance().createService(RepositoryService.class);
        Map<String, String> params = new HashMap<>();
        params.put("visibility", "public");
        params.put("since", Integer.toString(fromRepository));

        if(callback != null) {
            callback.start();
        }

        service.getRepos(params).enqueue(new Callback<List<RepositoryGithubModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<RepositoryGithubModel>> call, @NonNull Response<List<RepositoryGithubModel>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<RepositoryAppModel> repositories = new ArrayList<>();

                    for(RepositoryGithubModel githubRepo : response.body()) {
                        repositories.add(new RepositoryAppModel().from(githubRepo));
                    }

                    if(callback != null) {
                        if(repositories.size() > 0) {
                            callback.success(repositories);
                        } else {
                            callback.error(ERROR_NO_REPOSITORIES);
                        }

                        callback.finish();
                    }
                } else {
                    if(callback != null) {
                        callback.error(ERROR_GENERIC);
                        callback.finish();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<RepositoryGithubModel>> call, @NonNull Throwable t) {
                if(callback != null) {
                    @Error int error = (t instanceof UnknownHostException) ? ERROR_NETWORK : ERROR_GENERIC;

                    callback.error(error);
                    callback.finish();
                }
            }
        });
    }

    public static void searchRepositories(@NonNull String repositoryName, final ProviderCallback<List<RepositoryAppModel>> callback) {
        RepositoryService service = RequestManager.getInstance().createService(RepositoryService.class);
        Map<String, String> params = new HashMap<>();
        params.put("q", repositoryName + "in:name");

        if(callback != null) {
            callback.finish();
        }

        service.searchRepositories(params).enqueue(new Callback<SearchRepositoryGithubModel>() {
            @Override
            public void onResponse(@NonNull Call<SearchRepositoryGithubModel> call, @NonNull Response<SearchRepositoryGithubModel> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<RepositoryAppModel> repositories = new ArrayList<>();
                    List<RepositoryGithubModel> foundRepositories = response.body().items;

                    for(RepositoryGithubModel githubRepo : foundRepositories) {
                        repositories.add(new RepositoryAppModel().from(githubRepo));
                    }

                    if(callback != null) {
                        if(repositories.size() > 0) {
                            callback.success(repositories);
                        } else {
                            callback.error(ERROR_NO_REPOSITORIES);
                        }
                    }
                } else {
                    if(callback != null) {
                        callback.error(ERROR_GENERIC);
                        callback.finish();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchRepositoryGithubModel> call, @NonNull Throwable t) {
                if(callback != null) {
                    @Error int error = (t instanceof UnknownHostException) ? ERROR_NETWORK : ERROR_GENERIC;

                    callback.error(error);
                    callback.finish();
                }
            }
        });
    }

    public static void getBranches(int page, int resultCount, @NonNull String ownerName, @NonNull String repositoryName, final ProviderCallback<List<RepositoryBranchAppModel>> callback) {
        RepositoryService service = RequestManager.getInstance().createService(RepositoryService.class);
        Map<String, String> params = new HashMap<>();
        params.put("page", Integer.toString(page));
        params.put("per_page", Integer.toString(resultCount));


        if(callback != null) {
            callback.start();
        }

        service.getBranches(ownerName, repositoryName, params).enqueue(new Callback<List<RepositoryBranchGithubModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<RepositoryBranchGithubModel>> call, @NonNull Response<List<RepositoryBranchGithubModel>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<RepositoryBranchAppModel> branches = new ArrayList<>();
                    List<RepositoryBranchGithubModel> foundBranches = response.body();

                    for(RepositoryBranchGithubModel githubBranch : foundBranches) {
                        branches.add(new RepositoryBranchAppModel().from(githubBranch));
                    }

                    if(callback != null) {
                        callback.success(branches);
                        callback.finish();
                    }
                } else {
                    if(callback != null) {
                        callback.error(ERROR_GENERIC);
                        callback.finish();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<RepositoryBranchGithubModel>> call, @NonNull Throwable t) {
                if(callback != null) {
                    @Error int error = (t instanceof UnknownHostException) ? ERROR_NETWORK : ERROR_GENERIC;

                    callback.error(error);
                    callback.finish();
                }
            }
        });
    }

    public static void getContributors(int page, int resultCount, @NonNull String ownerName, @NonNull String repositoryName, final ProviderCallback<List<UserAppModel>> callback) {
        RepositoryService service = RequestManager.getInstance().createService(RepositoryService.class);
        Map<String, String> params = new HashMap<>();
        params.put("page", Integer.toString(page));
        params.put("per_page", Integer.toString(resultCount));

        if(callback != null) {
            callback.start();
        }

        service.getContributors(ownerName, repositoryName, params).enqueue(new Callback<List<UserGithubModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserGithubModel>> call, @NonNull Response<List<UserGithubModel>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<UserAppModel> contributors = new ArrayList<>();
                    List<UserGithubModel> foundContributors = response.body();

                    for(UserGithubModel githubContributor : foundContributors) {
                        contributors.add(new UserAppModel().from(githubContributor));
                    }

                    if(callback != null) {
                        callback.success(contributors);
                        callback.finish();
                    }
                } else {
                    if(callback != null) {
                        callback.error(ERROR_GENERIC);
                        callback.finish();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UserGithubModel>> call, @NonNull Throwable t) {
                if(callback != null) {
                    callback.error(ERROR_GENERIC);
                    callback.finish();
                }
            }
        });
    }

    public static void getIssues(int page, int resultCount, @NonNull String ownerName, @NonNull String repositoryName, final ProviderCallback<List<IssueAppModel>> callback) {
        RepositoryService service = RequestManager.getInstance().createService(RepositoryService.class);
        Map<String, String> params = new HashMap<>();
        params.put("page", Integer.toString(page));
        params.put("per_page", Integer.toString(resultCount));

        if(callback != null) {
            callback.start();
        }

        service.getIssues(ownerName, repositoryName, params).enqueue(new Callback<List<IssueGithubModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<IssueGithubModel>> call, @NonNull Response<List<IssueGithubModel>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<IssueAppModel> issues = new ArrayList<>();
                    List<IssueGithubModel> foundIssues = response.body();

                    for(IssueGithubModel githubIssue : foundIssues) {
                        issues.add(new IssueAppModel().from(githubIssue));
                    }

                    if(callback != null) {
                        callback.success(issues);
                        callback.finish();
                    }
                } else {
                    if(callback != null) {
                        callback.error(ERROR_GENERIC);
                        callback.finish();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<IssueGithubModel>> call, @NonNull Throwable t) {
                if(callback != null) {
                    @Error int error = (t instanceof UnknownHostException) ? ERROR_NETWORK : ERROR_GENERIC;

                    callback.error(error);
                    callback.finish();
                }
            }
        });
    }

    public static void getOpenPullRequests(int page, int resultCount, @NonNull String ownerName, @NonNull String repositoryName, final ProviderCallback<List<PullRequestAppModel>> callback) {
        RepositoryService service = RequestManager.getInstance().createService(RepositoryService.class);
        Map<String, String> params = new HashMap<>();
        params.put("state", "open");
        params.put("page", Integer.toString(page));
        params.put("per_page", Integer.toString(resultCount));

        if(callback != null) {
            callback.start();
        }

        service.getPullRequests(ownerName, repositoryName, params).enqueue(new Callback<List<PullRequestGithubModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<PullRequestGithubModel>> call, @NonNull Response<List<PullRequestGithubModel>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<PullRequestAppModel> pullRequests = new ArrayList<>();
                    List<PullRequestGithubModel> foundPullRequests = response.body();

                    for(PullRequestGithubModel githubPullRequest : foundPullRequests) {
                        pullRequests.add(new PullRequestAppModel().from(githubPullRequest));
                    }

                    if(callback != null) {
                        callback.success(pullRequests);
                        callback.finish();
                    }
                } else {
                    if(callback != null) {
                        callback.error(ERROR_GENERIC);
                        callback.finish();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PullRequestGithubModel>> call, @NonNull Throwable t) {
                if(callback != null) {
                    @Error int error = (t instanceof UnknownHostException) ? ERROR_NETWORK : ERROR_GENERIC;

                    callback.error(error);
                    callback.finish();
                }
            }
        });
    }

    public static void getCommitCount(@NonNull final String ownerName, @NonNull final String repositoryName, @NonNull Date repositoryCreationDate, final ProviderCallback<Integer> callback) {
        final RepositoryService service = RequestManager.getInstance().createService(RepositoryService.class);
        final Map<String, String> params = new HashMap<>();
        final SimpleDateFormat format = new SimpleDateFormat(ConstantsUtils.REPOSITORY_DATE_FORMAT);

        params.put("until", format.format(repositoryCreationDate));
        params.put("per_page", "1");

        if(callback != null) {
            callback.start();
        }

        service.getCommits(ownerName, repositoryName, params).enqueue(new Callback<List<CommitGithubModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<CommitGithubModel>> call, @NonNull Response<List<CommitGithubModel>> response) {
                List<CommitGithubModel> commits = response.body();

                if(commits != null && commits.size() > 0) {
                    final String firstCommitSha = commits.get(0).sha;
                    params.put("until", format.format(new Date()));

                    service.getCommits(ownerName, repositoryName, params).enqueue(new Callback<List<CommitGithubModel>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<CommitGithubModel>> call, @NonNull Response<List<CommitGithubModel>> response) {
                            List<CommitGithubModel> commits = response.body();

                            if(commits != null && commits.size() > 0) {
                                String lastCommitSha = commits.get(0).sha;

                                service.compareCommits(ownerName, repositoryName, firstCommitSha, lastCommitSha).enqueue(new Callback<CommitCompareGithubModel>() {
                                    @Override
                                    public void onResponse(@NonNull Call<CommitCompareGithubModel> call, @NonNull Response<CommitCompareGithubModel> response) {
                                        if(response.body() != null) {
                                            if(callback != null) {
                                                callback.success(response.body().totalCommits);
                                            }
                                        } else {
                                            if(callback != null) {
                                                callback.error(ERROR_COMMIT);
                                                callback.finish();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<CommitCompareGithubModel> call, @NonNull Throwable t) {
                                        if(callback != null) {
                                            @Error int error = (t instanceof UnknownHostException) ? ERROR_NETWORK : ERROR_GENERIC;

                                            callback.error(error);
                                            callback.finish();
                                        }
                                    }
                                });

                            } else {
                                if(callback != null) {
                                    callback.error(ERROR_COMMIT);
                                    callback.finish();
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<CommitGithubModel>> call, @NonNull Throwable t) {
                            if(callback != null) {
                                @Error int error = (t instanceof UnknownHostException) ? ERROR_NETWORK : ERROR_GENERIC;

                                callback.error(error);
                                callback.finish();
                            }
                        }
                    });

                } else {
                    if(callback != null) {
                        callback.error(ERROR_COMMIT);
                        callback.finish();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CommitGithubModel>> call, @NonNull Throwable t) {
                if(callback != null) {
                    @Error int error = (t instanceof UnknownHostException) ? ERROR_NETWORK : ERROR_GENERIC;

                    callback.error(error);
                    callback.finish();
                }
            }
        });
    }

    public static void getRepository(@NonNull String owner, @NonNull String repositoryName, final ProviderCallback<RepositoryAppModel> callback) {
        final RepositoryService service = RequestManager.getInstance().createService(RepositoryService.class);

        service.getRepository(owner, repositoryName).enqueue(new Callback<RepositoryGithubModel>() {
            @Override
            public void onResponse(@NonNull Call<RepositoryGithubModel> call, @NonNull Response<RepositoryGithubModel> response) {
                if(response.body() != null) {
                    RepositoryAppModel repo = new RepositoryAppModel().from(response.body());

                    if(callback != null) {
                        callback.success(repo);
                    }
                } else {
                    if(callback != null) {
                        callback.error(ERROR_NO_REPOSITORIES);
                        callback.finish();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RepositoryGithubModel> call, @NonNull Throwable t) {
                if(callback != null) {
                    @Error int error = (t instanceof UnknownHostException) ? ERROR_NETWORK : ERROR_GENERIC;

                    callback.error(error);
                    callback.finish();
                }
            }
        });
    }
}
