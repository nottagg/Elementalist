/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jccf9celementalist;

import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

/**
 *
 * @author James
 */
public class InstructionsModel extends FirePropertyChange {
    private Timeline timeline;
    private KeyFrame keyFrame;
    private double tickTimeInSeconds = .01;
    private Player player;
    private ArrayList<Fireball> fireballs;
    private ArrayList<IceWall> icewalls;
    private boolean hasFired = false;
    private boolean hasIced = false;
    private boolean hasYellowed = false;
    private boolean lastTransition = false;

    public InstructionsModel() {
        setupTimer();
        player = new Player(57,200);
        timeline.play();
        fireballs = new ArrayList<>();
        icewalls = new ArrayList<>();        
    }

    public void setupTimer() {
        keyFrame = new KeyFrame(Duration.millis(tickTimeInSeconds*1000), (ActionEvent actionEvent) -> {
            player.Update();
            firePropertyChange("movementX",null,player.getX());
            firePropertyChange("movementY",null,player.getY());
            
            for (int i = 0; i < fireballs.size(); i++) {
                fireballs.get(i).Update();
                firePropertyChange("fbmovementX", i, fireballs.get(i).getX());
                firePropertyChange("fbmovementY", i, fireballs.get(i).getY());
                
                if (fireballs.get(i).isOutOfBounds()){
                    firePropertyChange("fireballoob",null, fireballs.get(i));
                    fireballs.set(i, null);
                    fireballs.remove(i);  
                }
//                see statement in GameModel on why this is not implemented
//                if (icewalls.size() > 0) {
//                    for (int j = 0; j < icewalls.size(); j++) {
//                        if (fireballs.get(i).checkCollision((Shape)icewalls.get(j))) {
//                            firePropertyChange("fireballoob", null, fireballs.get(i));
//                            firePropertyChange("icewalloob", null, icewalls.get(j));
//                            fireballs.set(i, null);
//                            fireballs.remove(i);
//                            icewalls.set(j,null);
//                            icewalls.remove(j);
//                        }
//                    }
//                }
            }
                
            for (int i = 0; i < icewalls.size(); i++) {
                icewalls.get(i).Update();
                if (icewalls.get(i).isOutOfBounds()) {
                    firePropertyChange("icewalloob",null,icewalls.get(i));
                    icewalls.set(i,null);
                    icewalls.remove(i);
                }
            }  

            if (hasFired && hasIced && hasYellowed) {
                lastTransition = true;
            }
            firePropertyChange("clicked",false,lastTransition);
            firePropertyChange("movement",false,enteredYellowZone());
        });

        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    public void handlePress(KeyCode press) {
        player.movePlayerPress(press);
    }

    public void handleRelease(KeyCode release) {
        player.movePlayerRelease(release);
    }

    public void handleMouse(MouseEvent event) {
       Shape attack = player.clickHandler(event);
       if (attack instanceof Fireball) {
           fireballs.add((Fireball)attack);
           firePropertyChange("fireballs",0,fireballs.get(fireballs.size()-1));
           hasFired = true;
       }
       if (attack instanceof IceWall) {
           icewalls.add((IceWall)attack);
           firePropertyChange("icewalls",0,icewalls.get(icewalls.size()-1));
           hasIced = true;
       }
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean enteredYellowZone() {
        if (player.getBoundsInParent().intersects(515,280,80,75)) hasYellowed = true;
        return hasYellowed;
    }

    public void pause() {
        timeline.pause();
    }

    public void play() {
        timeline.play();
    }

    public boolean isRunning() {
    if (timeline != null) {
        if(timeline.getStatus() == Animation.Status.RUNNING) {
            return true;
        }
    }
    return false;
    }
    
    public ArrayList<Fireball> getFireballs() {
        return fireballs;
    }
    
    public ArrayList<IceWall> getIcewalls() {
        return icewalls;
    }

    public void resetFlags() {
        hasFired = false;
        hasIced = false;
        hasYellowed = false;
        lastTransition = false;     
    }
}
