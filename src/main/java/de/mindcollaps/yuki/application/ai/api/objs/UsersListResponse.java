package de.mindcollaps.yuki.application.ai.api.objs;

import java.util.List;

public class UsersListResponse {
    private List<User> users;
    private String nextToken;

    public UsersListResponse() {
    }

    public UsersListResponse(List<User> users, String nextToken) {
        this.users = users;
        this.nextToken = nextToken;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }
}
