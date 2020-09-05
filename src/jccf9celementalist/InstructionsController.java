/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jccf9celementalist;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 *
 * @author James
 */
public class InstructionsController extends Switchable implements Initializable, PropertyChangeListener {
    
    @FXML 
    private AnchorPane anchorPane;
    
    @FXML 
    private Label topLabel;
    
    @FXML
    private Label titleLabel;
    
    @FXML
    private Label bottomLabel;
    
    @FXML
    private Rectangle yellowZone;
    
    private InstructionsModel model;
    
    private boolean enteredYellowZone = false;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new InstructionsModel();
        model.addPropertyChangeListener(this);
        anchorPane.getChildren().add(model.getPlayer());
    }
    
    @FXML
    private void returnToMenuButtonHandler() {
        if (enteredYellowZone) {
            anchorPane.getChildren().add(yellowZone);
            titleLabel.setText("You are a Wizard");
            topLabel.setText("Use W,A,S,D to move");
            bottomLabel.setText("Progress to the yellow zone");
            enteredYellowZone = false;
        }
        model.getPlayer().setX(57);
        model.getPlayer().setY(200);
        model.getPlayer().setVelX(0);
        model.getPlayer().setVelY(0);
        model.resetFlags();
        model.pause();
        clearViewObjects();
        Switchable.switchTo("MainMenu");
    }
    
    @FXML
    private void keyPressHandler(KeyEvent event) {
        KeyCode press = event.getCode();
        model.handlePress(press);
    }
    
    @FXML
    private void keyReleaseHandler(KeyEvent event) {
        KeyCode release = event.getCode();
        model.handleRelease(release);
    }
    
    @FXML
    private void mouseClickHandler(MouseEvent event) {
        model.handleMouse(event);
    }
    
    @FXML
    public void resumeTimer() {
        if (!model.isRunning()) model.play();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("movement")) {
            if (!enteredYellowZone) {
                anchorPane.getChildren().remove(yellowZone);
                titleLabel.setText("You are skilled in fire and ice");
                topLabel.setText("Left Click to shoot fireballs to attack");
                bottomLabel.setText("Right click to raise Ice walls for protection");
                enteredYellowZone = true;
            }
        }
        if (evt.getPropertyName().equals("fireballs")) {
            anchorPane.getChildren().add((Shape)evt.getNewValue());
        }
        if (evt.getPropertyName().equals("fireballoob")) {
            anchorPane.getChildren().remove((Shape)evt.getNewValue());
        }
        if (evt.getPropertyName().equals("icewalls")) {
            anchorPane.getChildren().add((Shape)evt.getNewValue());
        }
        if (evt.getPropertyName().equals("icewalloob")) {
            anchorPane.getChildren().remove((Shape)evt.getNewValue());
        }
        if (evt.getPropertyName().equals("clicked")) {
            titleLabel.setText("Enemies will spawn randomly");
            topLabel.setText("Fight with your fireballs");
            bottomLabel.setText("Block with your ice walls");
        }
        if (evt.getPropertyName().equals("movementX")) {
            model.getPlayer().setTranslateX((double)evt.getNewValue());
        }
        if (evt.getPropertyName().equals("movementY")) {
            model.getPlayer().setTranslateY((double)evt.getNewValue());
        }
        if (evt.getPropertyName().equals("fbmovementX")) {
            model.getFireballs().get((int)evt.getOldValue()).setTranslateX((double)evt.getNewValue());
        }
        if (evt.getPropertyName().equals("fbmovementY")) {
            model.getFireballs().get((int)evt.getOldValue()).setTranslateY((double)evt.getNewValue());
        }
    }
    
    public void clearViewObjects() {
        for (Object fireball : model.getFireballs()) {
            anchorPane.getChildren().remove(fireball);
        }
        for (Object icewall : model.getIcewalls()) {
            anchorPane.getChildren().remove(icewall);
        }
    }
   
}
