/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jccf9celementalist;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * Mouse trajectory - https://stackoverflow.com/questions/34122380/shooting-bullet-towards-mouse-position-in-java-slick2d
 * @author James
 */
public class Fireball extends Circle implements GameObject, java.io.Serializable {
    private final Circle WHO;
    private final double R = 7;
    private final double VELOCITY = 6;
    private double angle;
    private double x = 0;
    private double y = 0;
    
    public Fireball(Circle who, double mouseX, double mouseY) {
        this.WHO = who;
        this.x = who.getTranslateX();
        this.y = who.getTranslateY();
        this.setRadius(R);
        this.setFill(Color.ORANGE);
        this.setTrajectory(who, mouseX, mouseY);
    }
        
    public void setTrajectory(Circle who, double mouseX, double mouseY) {
        angle = Math.atan2(mouseX - who.getTranslateX(),mouseY - who.getTranslateY());
    }

    @Override
    public void Update() {
        this.x += Math.sin(angle) * VELOCITY;
        this.y += Math.cos(angle) * VELOCITY;

    }

    @Override
    public boolean isOutOfBounds() {            
        if (this.x <= 0+R) return true;
        else if (this.x >= 800-R) return true;
        else if (this.y <= 0+R) return true;
        else if (this.y >= 600-R) return true;          
        else return false;
    }

    @Override
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public void setY(double y) {
        this.y =y;
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
}
