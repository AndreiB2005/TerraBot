package mission;

import java.util.List;
import java.util.ArrayList;
import entities.Entity;

public class TerraBot {
    private int battery;
    private int rechargeTime;
    private final List<Entity> inventory;
    private int posX;
    private int posY;

    public TerraBot(int energyPoints) {
        battery = energyPoints;
        rechargeTime = 0;
        inventory = new ArrayList<>();
        posX = 0;
        posY = 0;
    }

    public int getBattery() {
        return battery;
    }

    public int getRechargeTime() {
        return rechargeTime;
    }

    public List<Entity> getInventory() {
        return inventory;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public void setRechargeTime(int rechargeTime) {
        this.rechargeTime = Math.max(0, rechargeTime);
    }

    public void changeCoordinates(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }
}
