/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pandemy.model;

import java.awt.Point;
import java.util.HashMap;
import pandemy.Pandemy;
import pandemy.Settings;
import pandemy.UtilityTools;

/**
 *
 * @author nunzi
 */
public class Hotspot {
    
    private int id;
    private HashMap<Integer, Node> nodes = new HashMap<>();
    private Point hotspot_center;

    public Hotspot(int id, Point hotspot_center) {
        this.id = id;
        this.hotspot_center = hotspot_center;
    }

    public HashMap<Integer, Node> getNodes() {
        return nodes;
    }

    public void setNodes(HashMap<Integer, Node> nodes) {
        this.nodes = nodes;
    }

    public void addNode(Node node){
        nodes.put(node.getId(), node);
    }

    public void removeNode(Node node){
        nodes.remove(node.getId());
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Point getHotspot_center() {
        return hotspot_center;
    }

    public void setHotspot_center(Point hotspot_center) {
        this.hotspot_center = hotspot_center;
    }
    
    
    //Genero una posizione dispoibile per un nodo
    public Point aviableLocaion(){
        //System.out.println("Centro Hotspot: " + hotspot_center);
        int x_min = this.hotspot_center.x - Settings.HOTSPOT_RANGE;
        int x_max = this.hotspot_center.x + Settings.HOTSPOT_RANGE;
        int y_min = this.hotspot_center.y - Settings.HOTSPOT_RANGE;
        int y_max = this.hotspot_center.y + Settings.HOTSPOT_RANGE;
        Point aviableLoc = UtilityTools.generate_position(x_min, x_max, y_min, y_max);
        while (UtilityTools.compute_distance(this.hotspot_center, aviableLoc) > Settings.HOTSPOT_RANGE &&
                Pandemy.posOccupate.contains(aviableLoc)) {            
            aviableLoc = UtilityTools.generate_position(x_min, x_max, y_min, y_max);
        }
        return aviableLoc;
    }
    
    public boolean isInRange(Node node){
        return UtilityTools.compute_distance(hotspot_center, node.getLocation()) < Settings.HOTSPOT_RANGE;
    }

    @Override
    public String toString() {
        return "ID Hotspot: " + this.id + " Centro: " + this.hotspot_center;
    }
    
    
    
}
