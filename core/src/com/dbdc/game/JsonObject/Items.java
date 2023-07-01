package com.dbdc.game.JsonObject;

import com.badlogic.gdx.math.Vector3;

public class Items {
    private String itemPath;
    private Vector3 position;

    public String getItemPath() {
        return itemPath;
    }

    public void setItemPath(String itemPath) {
        this.itemPath = itemPath;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }
}
