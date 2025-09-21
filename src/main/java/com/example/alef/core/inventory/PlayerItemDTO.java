package com.example.alef.core.inventory;


public class PlayerItemDTO {
    private Long id;
    private Long playerId;
    private Long itemId;
    private String itemName;
    private Integer count;

    public PlayerItemDTO() {}

    public PlayerItemDTO(Long id, Long playerId, Long itemId, String itemName, Integer count) {
        this.id = id;
        this.playerId = playerId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.count = count;
    }

    public static PlayerItemDTO from(PlayerItem playerItem) {
        if (playerItem == null) {
            return null;
        }

        Long itemId = playerItem.getItem() != null ? playerItem.getItem().getId() : null;
        String itemName = playerItem.getItem() != null ? playerItem.getItem().getName() : null;
        Long playerId = playerItem.getPlayer() != null ? playerItem.getPlayer().getId() : null;

        return new PlayerItemDTO(
                playerItem.getId(),
                playerId,
                itemId,
                itemName,
                playerItem.getCount()
        );
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}

