package com.leitingsd.plugins.nlkwhitelist.manager;

public class WhitelistRecord {
    private String player;
    private String guarantor;
    private String train;
    private String description;

    public WhitelistRecord(String player, String guarantor, String train, String description) {
        this.player = player;
        this.guarantor = guarantor;
        this.train = train;
        this.description = description;
    }

    public String getPlayer() {
        return player;
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
}
