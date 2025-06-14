package org.example;

public enum CacheReplacementPolicy {
    LRU("Least Recently Used"),
    MRU("Most Recently Used"),
    LFU("Least Frequently Used");

    private final String description;

    CacheReplacementPolicy(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }

}
