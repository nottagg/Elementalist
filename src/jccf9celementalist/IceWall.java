/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jccf9celementalist;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author James
 */
public class IceWall extends Rectangle implements GameObject, java.io.Serializable {
    
    private final int LIFETIME = 100;
    private int life = 0;
    private final int LENGTH = 60;
    private double x;
    private double y;
    
    public IceWall(double x, double y) {
        this.setTranslateX(x - LENGTH/2);
        this.setTranslateY(y - LENGTH/2);
        this.setFill(Color.AQUA);
        this.setWidth(LENGTH);
        this.setHeight(LENGTH);
    }
        
    @Override
    public void Update() {
        life++;
    }

    @Override
    public boolean isOutOfBounds() {
        return life > LIFETIME;
    }   
}
