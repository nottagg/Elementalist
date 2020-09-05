/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jccf9celementalist;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 *
 * @author James
 */
public class Crawler extends Circle implements GameObject, java.io.Serializable{

    private final Player PLAYER;
    private double x = 0;
    private double y = 0;
    private final double R = 12;
    private double velocity = .9;
    private double velX = 0;
    private double velY = 0;
    private double angle;
    
    public Crawler(Player player, double x, double y) {
        this.PLAYER = player;
        this.x = x;
        this.y = y;
        this.setRadius(R);
        this.setFill(Color.GREENYELLOW);
    }
    
    @Override
    public void Update() {
        angle = Math.atan2(PLAYER.getX() - this.x,PLAYER.getY() - this.y);
        setTrajectory();
    }
    
    private void setTrajectory() {
        this.x += Math.sin(angle) * velocity;
        this.y += Math.cos(angle) * velocity;
    }

    @Override
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public boolean isOutOfBounds() {
        if (this.x <= 0+R) {
            this.x = R+2;
            return true;
        }
        else if (this.x >= 800-R){
            this.x = 800-R-2;
            return true;
        }
        else if (this.y <= 0+R) {
            this.y = R;
            return true;
        }
        else if (this.y >= 600-R) {
            this.y = 600-R;
            return true;
        }
        else return false;
    }
    
    public boolean checkCollision(Shape shape) {
        return this.getBoundsInParent().intersects(shape.getBoundsInParent());
    }

    @Override
    public double getX() {
        return this.x;
    }

    @Override
    public double getY() {
        return this.y;
    }
    
    public void setVelocity(double vel) {
        this.velocity = vel;
    }
    
}
