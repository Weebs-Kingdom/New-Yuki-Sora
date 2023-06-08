package de.mindcollaps.yuki.api.lib.route;

public class DatabaseId {
    private final String databaseId;

    public DatabaseId() {
        this.databaseId = "";
    }
    public DatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    @Override
    public String toString() {
        return databaseId;
    }
}
