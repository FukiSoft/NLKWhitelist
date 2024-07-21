package com.leitingsd.plugins.nlkwhitelist.manager;

import java.sql.Timestamp;

public class WhitelistRecord {
    private long id;
    private String player;
    private String operator;
    private String guarantor;
    private String train;
    private String description;
    private Timestamp time;
    private long deleteAt;
    private String deleteOperator;
    private String deleteReason;

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

    // Getter methods for all fields

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
