package com.example.alef.core.object;

import java.util.List;

public class LootDTO {
    private String sourceObjectName;
    private List<String> itemNames;
    private String lootMessage;

    public String getLootMessage() {
        return lootMessage;
    }

    public void setLootMessage(String lootMessage) {
        this.lootMessage = lootMessage;
    }

    public LootDTO(String sourceObjectName, List<Item> items, String lootMessage) {
        this.sourceObjectName = sourceObjectName;
        this.itemNames = items.stream().map(Item::getName).toList();
        this.lootMessage = lootMessage;
    }

    public String getSourceObjectName() {
        return sourceObjectName;
    }

    public void setSourceObjectName(String sourceObjectName) {
        this.sourceObjectName = sourceObjectName;
    }

    public List<String> getItemNames() {
        return itemNames;
    }

    public void setItemNames(List<String> itemNames) {
        this.itemNames = itemNames;
    }
}
