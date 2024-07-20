package com.leitingsd.plugins.nlkwhitelist.manager;

import java.time.Instant;

public class WhitelistRecord {
    private final long id;
    private final Instant time;
    private final String player;
    private final String operator;
    private final String guarantor;
    private final String train;
    private final String description;
    private final String deleteAt;
    private final String deleteReason;
    private final String deleteOperator;

    public WhitelistRecord(long id, Instant time, String player, String operator, String guarantor, String train, String description, String deleteAt, String deleteReason, String deleteOperator) {
        this.id = id;
        this.time = time;
        this.player = player;
        this.operator = operator;
        this.guarantor = guarantor;
        this.train = train;
        this.description = description;
        this.deleteAt = deleteAt;
        this.deleteReason = deleteReason;
        this.deleteOperator = deleteOperator;
    }

    public long getId() {
        return id;
    }

    public Instant getTime() {
        return time;
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

    public String getDeleteAt() {
        return deleteAt;
    }

    public String getDeleteReason() {
        return deleteReason;
    }

    public String getDeleteOperator() {
        return deleteOperator;
    }
}
