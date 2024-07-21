package com.leitingsd.plugins.nlkwhitelist.manager;

import java.sql.Timestamp;

public class WhitelistRecord {
    private final long id;
    private final String player;
    private final String operator;
    private final String guarantor;
    private final String train;
    private final String description;
    private final Timestamp time;
    private final long deleteAt;
    private final String deleteOperator;
    private final String deleteReason;

    public WhitelistRecord(long id, String player, String operator, String guarantor, String train, String description, Timestamp time, long deleteAt, String deleteOperator, String deleteReason) {
        this.id = id;
        this.player = player;
        this.operator = operator;
        this.guarantor = guarantor;
        this.train = train;
        this.description = description;
        this.time = time;
        this.deleteAt = deleteAt;
        this.deleteOperator = deleteOperator;
        this.deleteReason = deleteReason;
    }

    // Getters for the fields
    public long getId() {
        return id;
    }

    public String getPlayer() {
        return player;
    }

    public String getOperator() {
        return operator;
    }

    public String getGuarantor() {
        return guarantor;
    }

    public String getTrain() {
        return train;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getTime() {
        return time;
    }

    public long getDeleteAt() {
        return deleteAt;
    }

    public String getDeleteOperator() {
        return deleteOperator;
    }

    public String getDeleteReason() {
        return deleteReason;
    }
}
