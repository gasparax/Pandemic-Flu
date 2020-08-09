/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pandemy;

import pandemy.model.Node;
import com.google.gson.Gson;
import java.awt.Point;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import pandemy.persistance.DAONode;

/**
 *
 * @author nunzi
 */
public class UtilityTools {
    
    private static Random rand = new Random();
    private static Gson gson = new Gson();
    
    
    public static double compute_distance(Point p1, Point p2) {
        double distance = Point.distance((double) p1.x, (double) p1.y, (double) p2.x, (double) p2.y);
        return distance;
    }
    
    public static Point generate_position(int x_min, int x_max, int y_min, int y_max) {
        //System.out.println("x_min: " + x_min + " x_max:" + x_max + " y_min: " + y_min + " y_max:" + y_max);
        int x = (int) (Math.random() * ((x_max - x_min) + 1) + x_min);
        //System.out.println("x: " + x);
        int y = (int) (Math.random() * ((y_max - y_min) + 1) + y_min);
        //System.out.println("y: " + y);
        return new Point(x, y);
    }
    

    
    public static boolean nearDestination(Node n, Point p) {
        return UtilityTools.compute_distance(p, n.getLocation()) < Settings.NODE_SPEED;
    }

    public static boolean probability_infected(){
        double p = rand.nextDouble();
        return p < Settings.INFECTION_PROBABILITY;
    }

    public static ArrayList<Integer> meanInfected_per_step(ArrayList<Integer> per_step){
        ArrayList<Integer> inf_per_step = new ArrayList<>();
         for (int i = 0; i < per_step.size() - 1; i = i+10) {
            ArrayList<Integer> sum  = new ArrayList<>();
            for (int j = i; j < i + 10; j++) {
                sum.add(per_step.get(j));
            }
            inf_per_step.add(mean(sum));
        }
        return inf_per_step;
    }

    private static int mean(ArrayList<Integer> list){
        int sum = 0;
        for (Integer integer : list) {
             sum = sum + integer;
        }
        return sum/list.size();
    }
    
    public static void saveTrend(ArrayList<Integer> trend, String filenameNoEx){
        Writer writer = null;
        try {
            Integer i = 1;
            String filename = filenameNoEx.concat(i.toString()).concat(".json");
            while (new File(filename).exists()) {
                i++;
                filename = filenameNoEx.concat(i.toString()).concat(".json");
            }
            writer = new FileWriter(filename);
            gson.toJson(trend, writer);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(DAONode.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(DAONode.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void saveSettings(String fileName){
        HashMap<String, Object> settings = Settings.getSettings();
        Writer writer = null;
        try {
            writer = new FileWriter(fileName);
            gson.toJson(settings, writer);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(DAONode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static HashMap<String, Object> loadSettings(String fileName){
        HashMap<String, Object> settings = new HashMap<>();
        Reader reader = null;
        try {
            reader = new FileReader(fileName);
            settings = gson.fromJson(reader, HashMap.class);
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(DAONode.class.getName()).log(Level.SEVERE, null, ex);
        }
        return settings;
    }

        
}
