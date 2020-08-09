/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pandemy;

import java.util.HashMap;

/**
 *
 * @author nunzi
 */
public class Settings {

    public static final int WIDTH = 20000;
    public static final int HEIGHT = 15000;
    
    public static final double MAX_INF = 0.8;

    public static final int NODE_NUMBER = 350;
    public static final int NODE_RANGE = 10;
    public static final int NODE_SPEED = 330;
          
    public static final int HOTSPOT_NUMBER = 7;
    public static final int HOTSPOT_RANGE = 80;
    
    public static final double HALTING_PROBABILITY = 0.4;
    public static final double EXPLORING_PROBABILITY = 0.4;
    public static final double TRAVELLING_PROBABILITY = 
           (1 - (HALTING_PROBABILITY + EXPLORING_PROBABILITY));

    public static final double ANDROID_DISTRIBUTION = 0.5;
    public static final double IOS_DISTRIBUTION = 0.4;
    public static final double WINDOWS_DISTRIBUTION = 
            (1 - (ANDROID_DISTRIBUTION + IOS_DISTRIBUTION));
    public static final boolean CROSS_OS = true; //if cross is TRUE the virus can infect devices with diffrent OS

    public static final double UPDATE_PROBABILITY = 0.000025;
    public static final int START_UPDATE = 2500;// (Settings.STEP_NUMBER / 2 +(Settings.STEP_NUMBER / 4));
    public static final double INFECTION_PROBABILITY = 0.06;
    public static final int STEP_NUMBER = 50000;
    
    
    public static void printSettings(){
        StringBuilder sb = new StringBuilder();
        sb.append("width: ").append(WIDTH);
        sb.append("\nheight: ").append(HEIGHT);
        sb.append("\nnodeNumber: ").append(NODE_NUMBER);
        sb.append("\nnodeRange: ").append(NODE_RANGE);
        sb.append("\nnodeSpeed: ").append(NODE_SPEED);
        sb.append("\nhotspotNumber: ").append(HOTSPOT_NUMBER);
        sb.append("\nhotspotRange: ").append(HOTSPOT_RANGE);
        sb.append("\nhaltingProbability: ").append(HALTING_PROBABILITY);
        sb.append("\nexploringProbability: ").append(EXPLORING_PROBABILITY);
        sb.append("\ntravellingProbability: ").append(TRAVELLING_PROBABILITY);
        sb.append("\nupdateProbablity: ").append(UPDATE_PROBABILITY);
        sb.append("\nchance_to_get_infected: ").append(INFECTION_PROBABILITY);
        sb.append("\nCross OS: ").append(CROSS_OS);
        sb.append("\nstepNumber: ").append(STEP_NUMBER);
        System.out.println("Settings: \n" + sb + "\n");
    }

    public static HashMap<String, Object> getSettings(){
        HashMap<String, Object> settings = new HashMap<>();
        settings.put("WIDTH", WIDTH);
        settings.put("HEIGHT", HEIGHT);
        settings.put("NODE_NUMBER", NODE_NUMBER);
        settings.put("NODE_RANGE", NODE_RANGE);
        settings.put("NODE_SPEED", NODE_SPEED);
        settings.put("HOTSPOT_NUMBER", HOTSPOT_NUMBER);
        settings.put("HOTSPOT_RANGE", HOTSPOT_RANGE);
        settings.put("HALTING_PROBABILITY", HALTING_PROBABILITY);
        settings.put("EXPLORING_PROBABILITY", EXPLORING_PROBABILITY);
        settings.put("TRAVELLING_PROBABILITY", TRAVELLING_PROBABILITY);
        settings.put("ANDROID_DISTRIBUTION", ANDROID_DISTRIBUTION);
        settings.put("IOS_DISTRIBUTION", IOS_DISTRIBUTION);
        settings.put("WINDOWS_DISTRIBUTION", WINDOWS_DISTRIBUTION);
        settings.put("CROSS_OS", WIDTH);
        settings.put("UPDATE_PROBABILITY", WIDTH);
        settings.put("width", UPDATE_PROBABILITY);
        settings.put("INFECTION_PROBABILITY", INFECTION_PROBABILITY);
        settings.put("CROSS_OS", CROSS_OS);
        settings.put("START_UPDATE", START_UPDATE);
        settings.put("STEP_NUMBER", STEP_NUMBER);
        settings.put("MAX_INF", MAX_INF);
        return settings;
    }

}
