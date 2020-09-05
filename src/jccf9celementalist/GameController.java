/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jccf9celementalist;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 * Data serialization is based off of notes in class
 * @author James
 */
public class GameController extends Switchable implements Initializable, PropertyChangeListener {
    
    @FXML 
    private AnchorPane anchorPane;
    
    @FXML
    private StackPane escMenu;
    
    @FXML
    private StackPane gameOver;
    
    @FXML
    private Label waveCounter;
    
    @FXML
    private Label waveCounterGameOver;
        
    @FXML
    private Label killCount;
    
    private GameModel model;
       
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new GameModel();
        model.addPropertyChangeListener(this);     
    } 
    
    @FXML
    private void returnToMenuButtonHandler() {
        model.pause();
        if (escMenu.isVisible()) escMenu.setVisible(false);
            if (gameOver.isVisible()) {
                gameOver.setVisible(false);
                clearViewObjects();
            }
            clearViewObjects();
            model.setWaveNumber(0);
            model.setKillCount(1);
            
        Switchable.switchTo("MainMenu");
    }
    
    public void resumeTimer() {
        if (escMenu.isVisible()) escMenu.setVisible(false);
        anchorPane.requestFocus();
        if (!model.isRunning()) {
            model.play();
        }
    }

    
    @FXML
    private void keyPressHandler(KeyEvent event) {
        KeyCode press = event.getCode();
        if (press == KeyCode.ESCAPE) {
            if (!gameOver.isVisible()) {  
                if (!escMenu.isVisible()) {
                    escMenu.setVisible(true);
                    escMenu.toFront();
                    model.pause();
                }
                else {
                    escMenu.setVisible(false);
                    model.play();
                }
            }
        }

        model.handlePress(press);
    }
    
    @FXML
    private void keyReleaseHandler(KeyEvent event) {
        KeyCode release = event.getCode();
        model.handleRelease(release);
    }
    
    @FXML
    private void mouseClickHandler(MouseEvent event) {
        if (model.isRunning()) model.handleMouse(event);
    }
    
    @FXML
    private void menuButton() {
        handleSave();
        Switchable.switchTo("MainMenu");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("playerMade")) {
            anchorPane.getChildren().add((Shape)evt.getNewValue());
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
        if (evt.getPropertyName().equals("crawlers")) {
            anchorPane.getChildren().add((Shape)evt.getNewValue());
        }
        if (evt.getPropertyName().equals("crawlersoob")) {
            anchorPane.getChildren().remove((Shape)evt.getNewValue());
        }
        if (evt.getPropertyName().equals("wavePlus")) {
            waveCounter.setText("Wave: " + (int)evt.getNewValue());
            waveCounterGameOver.setText("Wave: " + (int)evt.getNewValue());
        }
        if (evt.getPropertyName().equals("killCountPlus")) {
            killCount.setText("Enemies Slain: " + (int)evt.getNewValue());
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
        if (evt.getPropertyName().equals("crmovementX")) {
            model.getCrawlers().get((int)evt.getOldValue()).setTranslateX((double)evt.getNewValue());
        }
        if (evt.getPropertyName().equals("crmovementY")) {
            model.getCrawlers().get((int)evt.getOldValue()).setTranslateY((double)evt.getNewValue());
        }
        if (evt.getPropertyName().equals("gameOver")) {
            gameOver.setVisible((boolean)evt.getNewValue());
                    clearViewObjects();
        }

    }
    
    public void clearViewObjects() {
        for (Object fireball : model.getFireballs()) {
            anchorPane.getChildren().remove(fireball);
        }
        for (Object icewall : model.getIcewalls()) {
            anchorPane.getChildren().remove(icewall);
        }
        for (Object crawler : model.getCrawlers()) {
            anchorPane.getChildren().remove(crawler);
        }
        anchorPane.getChildren().remove(model.getPlayer());
        model.removeAll();
    }
    
    @FXML
    private void handleSave() {
        GameModel saveModel = new GameModel(model.getWaveNumber(), model.getKillCount());
        
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(this.getStage());
        if (file != null) {
            try {
                FileOutputStream fileOut = new FileOutputStream(file);
                ObjectOutputStream out = new ObjectOutputStream(fileOut); 
                
                out.writeObject(saveModel);
                out.close(); 
                fileOut.close(); 
                
            } catch (FileNotFoundException ex) {
                String message = "File not found exception occured while saving to " + file; 
                displayExceptionAlert(message, ex); 
                
            } catch (IOException ex) {
                String message = "IOException occured while saving to " + file;
                displayExceptionAlert(message, ex);
                
            }  
        }
    }
    
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(this.getStage());
        if (file != null) {
            FileInputStream fileIn; 
            try {
                fileIn = new FileInputStream(file.getPath());
                ObjectInputStream in = new ObjectInputStream(fileIn); 
                
                GameModel temp = (GameModel) in.readObject();
                
                in.close(); 
                fileIn.close(); 
                gameLoad(temp);
                
            } catch (FileNotFoundException ex) {
                String message = "File not found exception occured while opening " + file.getPath(); 
                displayExceptionAlert(message, ex); 
                
            } catch (IOException ex) {
                String message = "IO exception occured while opening " + file.getPath(); 
                displayExceptionAlert(message, ex);
                
            } catch (ClassNotFoundException ex) {
                String message = "Class not found exception occured while opening " + file.getPath(); 
                displayExceptionAlert(message, ex); 
            }   
        }
    }
    
    private void displayErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        //alert.setHeaderText("Error!");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void displayExceptionAlert(String message, Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Exception!");
        alert.setContentText(message);

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }      
    
    private void gameLoad(GameModel loadedModel) {
        clearViewObjects();
        model.setWaveNumber(loadedModel.getWaveNumber()-2);
        model.setKillCount(loadedModel.getKillCount() - 1 );
        waveCounter.setText("Wave: " + (model.getWaveNumber() + 1));
        killCount.setText("Enemies Slain: " + (model.getKillCount()));
    }
    
}
