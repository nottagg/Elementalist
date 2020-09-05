/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jccf9celementalist;

/**
 *
 * @author James
 */
public class MainMenuModel {
    private String aboutInformation;
    
    public MainMenuModel() {
        aboutInformation = "This code serves as a base for a game. The base game is playable and features the ability to save and load your total score and current wave. " +
                "\nThe application uses firePropertyChange to follow proper MVC architecture. A sample file is provided in the project called \"sampleData\". " +
                "\nA UML diagram of the code is also provided within the project titled \"Jccf9cUML\". " +
                "\nLastly, a document named \"ProjectDocumentation\" is provided to review the code along with the rubric.";
    }
    
    public String getInformation() {
        return aboutInformation;
    }
}
