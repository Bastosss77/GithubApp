package org.jazzilla.contentsquareapp.core.appmodel;

import org.jazzilla.contentsquareapp.core.githubmodel.IssueGithubModel;

public class IssueAppModel extends BaseAppModel<IssueGithubModel> {
    public String title;
    public State state;

    @Override
    public IssueAppModel from(IssueGithubModel githubModel) {
        title = githubModel.title;
        state = State.fromString(githubModel.state);

        return this;
    }

    public enum State {
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
