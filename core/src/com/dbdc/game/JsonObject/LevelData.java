package com.dbdc.game.JsonObject;

import java.util.ArrayList;

public class LevelData {
    private ArrayList<Items> items;
    private ArrayList<Enemies> enemies;
    private Items interactableItem;
    private String levelScenePath;
    private String levelBodyPath;
    public ArrayList<Items> getItems() {
        return items;
    }
    public  ArrayList<Enemies> getEnemies(){
        return enemies;
    }
    public Items getInteractableItem(){
        return interactableItem;
    }
    public String getLevelScenePath(){
        return levelScenePath;
    }
    public String getLevelBodyPath(){
        return levelBodyPath;
    }
    public void setItems(ArrayList<Items> items) {
        this.items = items;
    }

    public void setEnemies(ArrayList<Enemies> enemies) {
        this.enemies = enemies;
    }

    public void setInteractableItem(Items interactableItem) {
        this.interactableItem = interactableItem;
    }

    public void setLevelBodyPath(String levelBodyPath) {
        this.levelBodyPath = levelBodyPath;
    }

    public void setLevelScenePath(String levelScenePath) {
        this.levelScenePath = levelScenePath;
    }
}