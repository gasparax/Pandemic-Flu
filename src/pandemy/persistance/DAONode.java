/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pandemy.persistance;

import java.awt.Point;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import pandemy.UtilityTools;
import pandemy.model.Node;
import com.google.gson.Gson;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 *
 * @author Nunzio
 */
public class DAONode {
    
    private static Gson gson = new Gson();
    
    public static void saveNode_rel(HashMap<Integer, Node> nodeList) {
        for (Node node : nodeList.values()) {
            try {
                HashMap<Integer, Integer> map = node.getReletionship();
                //System.out.println(map);
                String filePath = "nodes//nodes_relation//relationNode" + node.getId() + ".json";
                Writer writer = new FileWriter(filePath);
                gson.toJson(map, writer);
                writer.flush();
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(UtilityTools.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void saveNode_pos(HashMap<Integer, Node> nodeList, String filename) {
        HashMap<Integer, Point> nodes_pos = new HashMap<>();
        for (Node node : nodeList.values()) {
            nodes_pos.put(node.getId(), node.getLocation());
        }
        String filePath = "nodes//nodes_pos//" + filename + ".json";
        try {
            try (Writer writer = new FileWriter(filePath)) {
                gson.toJson(nodes_pos, writer);
                writer.flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(UtilityTools.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static void saveNode_history(HashMap<Integer, Node> nodeList){
        for (Node node : nodeList.values()) {
            Writer writer = null;
            try {
                String filePath = "nodes//nodes_pos_history//nodes_history_pos" + node.getId() + ".json";
                writer = new FileWriter(filePath);
                gson.toJson(node.getStorico_pos(), writer);
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
    }
    
    public static void saveNodes_infectionStep(HashMap<Integer, Node> nodeList){
        HashMap<Integer, Integer> nodes_inf_step = new HashMap<>();
        for (Node node : nodeList.values()) {
            nodes_inf_step.put(node.getId(), node.getInfectionStep());
        }
        String filePath = "nodes//nodes_infection_step.json";
        try {
            try (Writer writer = new FileWriter(filePath)) {
                gson.toJson(nodes_inf_step, writer);
                writer.flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(UtilityTools.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

