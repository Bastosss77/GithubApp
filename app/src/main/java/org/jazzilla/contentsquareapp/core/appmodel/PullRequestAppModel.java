package org.jazzilla.contentsquareapp.core.appmodel;

import org.jazzilla.contentsquareapp.core.githubmodel.PullRequestGithubModel;

public class PullRequestAppModel extends BaseAppModel<PullRequestGithubModel> {
    public String title;
    public State state;

    @Override
    public PullRequestAppModel from(PullRequestGithubModel githubModel) {
        title = githubModel.title;
        state = State.fromString(githubModel.state);

        return this;
    }

    enum State {
        OPEN, CLOSED;

        public static State fromString(String state) {
            if(state == null) {
                return null;
            }

            switch (state) {
                case "open":
                    return OPEN;
                    case "closed":
                        return CLOSED;
                    default:
                        return null;
            }
        }
    }
}
