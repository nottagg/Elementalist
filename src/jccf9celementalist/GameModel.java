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
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import java.util.Random;

/**
 *
 * @author James
 */
public class GameModel extends FirePropertyChange implements java.io.Serializable {
    
    private transient Timeline timeline;
    private transient KeyFrame keyFrame;
    private double tickTimeInSeconds = .01;
    private Player player;
    private ArrayList<Fireball> fireballs;
    private ArrayList<IceWall> icewalls;
    private ArrayList<Crawler> crawlers;
    private int waveNumber = 0;
    private int killCount = 1;
     
    public GameModel(int waveNumber, int killCount) {
        this();
        this.waveNumber = waveNumber;
        this.killCount = killCount;
    }
    
    public GameModel() {
        fireballs = new ArrayList<>();
        icewalls = new ArrayList<>();
        crawlers = new ArrayList<>();
        setupTimer();
        timeline.play();
    }
    
    public void setupTimer() {
        keyFrame = new KeyFrame(Duration.millis(tickTimeInSeconds*1000), (ActionEvent actionEvent) -> {
            if (player == null) {
                player = new Player(384,284);
                firePropertyChange("playerMade",null,player);
            }
            player.Update();
            
            if (crawlers.size() < 1) {
                Random random = new Random();
                waveNumber++;
                spawnCrawler(waveNumber + random.nextInt(killCount - 0) + 1);
                firePropertyChange("wavePlus",null,waveNumber);
            }
            
            firePropertyChange("movementX",null,player.getX());
            firePropertyChange("movementY",null,player.getY());
            
            for (int i = 0; i < fireballs.size(); i++) {
                fireballs.get(i).Update();
                firePropertyChange("fbmovementX", i, fireballs.get(i).getX());
                firePropertyChange("fbmovementY", i, fireballs.get(i).getY());
                
                if (fireballs.get(i).isOutOfBounds()) {
                    firePropertyChange("fireballoob",null, fireballs.get(i));
                    fireballs.set(i, null);
                    fireballs.remove(i); 
                    continue;
                }
//                will be implemented if enemies are made that shoot fireballs
//                if (icewalls.size() > 0 && fireballs.size() > 0) { 
//                    boolean flag = false;
//                    for (int j = 0; j < icewalls.size(); j++) {                
//                        if (fireballs.get(i).checkCollision(icewalls.get(j))) {
//                            firePropertyChange("fireballoob", null, fireballs.get(i));
//                            firePropertyChange("icewalloob", null, icewalls.get(j));
//                            fireballs.set(i, null);
//                            fireballs.remove(i);
//                            icewalls.set(j,null);
//                            icewalls.remove(j);
//                            flag = true;
//                        }
//                    }
//                    if (flag) {
//                        continue;
//                    }
//                }
                
                    for (int j = 0; j < crawlers.size(); j++) {
                        if (fireballs.get(i).checkCollision(crawlers.get(j))) {
                            firePropertyChange("fireballoob", null, fireballs.get(i));
                            firePropertyChange("crawlersoob", null, crawlers.get(j));
                            fireballs.set(i, null);
                            fireballs.remove(i);
                            crawlers.set(j, null);
                            crawlers.remove(j);
                            killCount++;
                            firePropertyChange("killCountPlus",null,killCount - 1);
                            break;
                        }
                    }
            }
            
            for (int i = 0; i < icewalls.size(); i++) {
                icewalls.get(i).Update();
                if (icewalls.get(i).isOutOfBounds()) {
                    firePropertyChange("icewalloob",null,icewalls.get(i));
                    icewalls.set(i,null);
                    icewalls.remove(i);
                }
                
            }  
            
            for (int i = 0; i < crawlers.size(); i++) {
                crawlers.get(i).Update();
                crawlers.get(i).setVelocity(.9);
                firePropertyChange("crmovementX", i, crawlers.get(i).getX());
                firePropertyChange("crmovementY", i, crawlers.get(i).getY());
                if (crawlers.get(i).checkCollision(player)) {
                firePropertyChange("gameOver", null, true);
                pause();
                }
                for (int j = 0; j < icewalls.size(); j++) {
                    if (crawlers.get(i).checkCollision(icewalls.get(j))) crawlers.get(i).setVelocity(0);
                }
            }
            
        });
        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    public void handlePress(KeyCode press) {
        if (player != null) player.movePlayerPress(press);
    }

    public void handleRelease(KeyCode release) {
        if (player != null) player.movePlayerRelease(release);
    }
    
    public void handleMouse(MouseEvent event) {
       Shape attack = player.clickHandler(event);
       if (attack instanceof Fireball) {
           fireballs.add((Fireball)attack);
           firePropertyChange("fireballs",0,fireballs.get(fireballs.size()-1));
       }
       if (attack instanceof IceWall) {
           icewalls.add((IceWall)attack);
           firePropertyChange("icewalls",0,icewalls.get(icewalls.size()-1));
       }
    }
    
    public Player getPlayer() {
        return this.player;
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
    
    public ArrayList<Crawler> getCrawlers() {
        return crawlers;
    }
        
    public void spawnCrawler(int number) {
        for (int i = 0; i < number; i++) {
            Crawler crawler;
            Random random = new Random();
            switch (random.nextInt((3-0)+1)) {
                case 0:
                    crawler = new Crawler(player, 0, random.nextInt((600-0)+1));
                    break;
                case 1:
                    crawler = new Crawler(player, 800, random.nextInt((600-0)+1));
                    break;  
                case 2:
                    crawler = new Crawler(player, random.nextInt((800-0)+1), 0);
                    break;
                case 3:
                    crawler = new Crawler(player, random.nextInt((800-0)+1), 600);
                    break;
                default:
                    crawler = new Crawler(player, 800,600);
            }
            crawlers.add(crawler);
            firePropertyChange("crawlers",0,crawlers.get(crawlers.size()-1));
        }
    }
       
    public void removeAll() {
        for (int i = 0; i < fireballs.size(); i++) {
            fireballs.set(i, null);
            fireballs.remove(i);
            i=i-1;
        }
        for (int i = 0; i < icewalls.size(); i++) {
            icewalls.set(i, null);
            icewalls.remove(i);
            i=i-1;
        }
        for (int i = 0; i < crawlers.size(); i++) {
            crawlers.set(i, null);
            crawlers.remove(i);
            i=i-1;
        }
        waveNumber = 0;
        player = null;
    }
    
    public void setWaveNumber(int wave) {
        this.waveNumber = wave;
    }
    
    public void setKillCount(int killCount) {
        this.killCount = killCount;
    }
    public int getWaveNumber() {
        return this.waveNumber;
    }
    
    public int getKillCount() {
        return this.killCount;
    }
    
}
