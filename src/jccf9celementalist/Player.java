/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jccf9celementalist;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 * movement smoothing - https://www.youtube.com/watch?v=L5GJ-i_6uGQ
 * @author James
 */
public class Player extends Circle implements GameObject, java.io.Serializable {
    
    private double x = 0;
    private double y = 0;
    private final double R = 16;
    private final double VELOCITY = 3;
    private double velX = 0;
    private double velY = 0;
    
    public Player(double x, double y) {
        this();
        this.x = x;
        this.y = y;
    }
    
    public Player() {
        this.setRadius(R);
        this.setFill(Color.BLUE);
    }
    
    @Override
    public void Update() {
        this.x += velX;
        this.y += velY;
        
        isOutOfBounds();
    }
    
    public void setVelX(double velX) {
        this.velX = velX;
    }
    
    public void setVelY(double velY) {
        this.velY = velY;
    }
    
    @Override
    public void setX(double x) {
        this.x = x;
    }
    
    @Override
    public void setY(double y) {
        this.y = y;
    }
    
    public void movePlayerPress(KeyCode press) {
        switch (press) {
            case A:
                this.setVelX(-VELOCITY);
                break;
            case D:
                this.setVelX(VELOCITY);
                break;
            case W:
                this.setVelY(-VELOCITY);
                break;
            case S:
                this.setVelY(VELOCITY);
                break;
        }        
    }
    
    public void movePlayerRelease(KeyCode release) {
        switch (release) {
            case A:
                this.setVelX(0);
                break;
            case D:
                this.setVelX(0);
                break;
            case W:
                this.setVelY(0);
                break;
            case S:
                this.setVelY(0);
                break;                
        }        
    }
    
    public Shape clickHandler(MouseEvent event) {
        MouseButton mouseButton = event.getButton();
        double mouseXPosition = event.getX();
        double mouseYPosition = event.getY();
        switch (mouseButton) {
            case PRIMARY:
                Shape fireAttack = primaryAttack(mouseXPosition, mouseYPosition);
                return fireAttack;
            case SECONDARY:
                Shape iceAttack = secondaryAttack(mouseXPosition, mouseYPosition);
                return iceAttack;
        }  
        return null;
    }
    
    public Fireball primaryAttack(double mouseX, double mouseY) {
        Fireball fireball = new Fireball(this, mouseX, mouseY);        
        return fireball;
    }
    
    public IceWall secondaryAttack(double mouseX, double mouseY) {
        IceWall icewall = new IceWall(mouseX,mouseY);
        return icewall;
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
    
    public double getVelX() {
        return this.velX;
    }
    
    public double getVelY() {
        return this.velY;
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
