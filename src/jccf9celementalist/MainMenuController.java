/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jccf9celementalist;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

/**
 * aboutButtonHandler comes from lecture notes
 * @author James
 */
public class MainMenuController extends Switchable implements Initializable {
    private MainMenuModel model;    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new MainMenuModel();
    } 
    
    @FXML
    private void instructionsButtonHandler() {
        Switchable.switchTo("Instructions");
        InstructionsController instructionsController = (InstructionsController)getControllerByName("Instructions");
        if (instructionsController != null) {
            instructionsController.resumeTimer();
        }
    }
    
    @FXML
    private void newGameButtonHandler() {
        Switchable.switchTo("Game");
        GameController gameController = (GameController)getControllerByName("Game");
        if (gameController != null) {
            gameController.resumeTimer();
        }
    }
    
    @FXML
    private void quitButtonHandler() {
        Platform.exit();
        System.exit(0);
    }
    
    @FXML
    private void aboutButtonHandler() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Java FXML Game");
        alert.setContentText("This application was developed by James Congdon at the University of Missouri");
        
        TextArea textArea = new TextArea(model.getInformation());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);
        alert.getDialogPane().setExpandableContent(expContent);
        
        alert.showAndWait();
    }
}
