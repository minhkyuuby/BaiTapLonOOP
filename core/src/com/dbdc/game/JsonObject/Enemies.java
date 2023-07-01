package com.dbdc.game.JsonObject;

import com.badlogic.gdx.math.Vector3;

public class Enemies {
    private String charModelPath;
    private Vector3 position;

    public String getCharModelPath() {
        return charModelPath;
    }

    public void setCharModelPath(String charModelPath) {
        this.charModelPath = charModelPath;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }
}
