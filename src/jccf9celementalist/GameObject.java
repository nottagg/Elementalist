/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jccf9celementalist;

/**
 *
 * @author James
 * GameObjects are objects that will update with a timer found in the model.
 * setX and setY are used to plant the starting position
 * isOutOfBounds is the boolean to know when to remove the object from the scene. For the player, it prevents the player from moving out of the scene.
*/
public interface GameObject { 
    void Update();
    void setX(double x);
    void setY(double y);
    double getX();
    double getY();
    boolean isOutOfBounds();
}
