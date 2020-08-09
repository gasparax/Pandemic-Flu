/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pandemy;

import pandemy.model.Node;
import pandemy.model.Hotspot;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import pandemy.persistance.DAOHotspot;
import pandemy.persistance.DAONode;


/**
 *
 * @author nunzi
 */
public class Pandemy {
    
    
    private static HashMap<Integer,Node> nodes = new HashMap<>();
    public static Set<Point> posOccupate = new HashSet<>();
    private static HashMap<Integer, Hotspot> hotspots = new HashMap<>();
    private static ArrayList<Integer> infected_per_step = new ArrayList<>();
    private static ArrayList<Integer> patched_per_step = new ArrayList<>();
    private static ArrayList<Integer> healthy_per_step = new ArrayList<>();
    private static Random rand = new Random();
    private static int actualStep = 0;
    private static int totalPatched = 0;
    public static int actual_infected = 0;
    public static int contagi = 0;
    public static int contagi_hotspot = 0;
    public static int max_infected = 0;
    public static int incontri = 0;
    public static int actual_patched = 0;
    


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Settings.printSettings();
        UtilityTools.saveSettings("settings.json");
        long startTime = System.currentTimeMillis();
        generateNodes();
        generateHotspots();
        int stoppend = 0;   
        while (actualStep < Settings.STEP_NUMBER) {
            if (actual_infected <= max_infected && actual_patched < 350) {
                step_exection();
                infected_per_step.add(actual_infected);
                actualStep++;
                healthy_per_step.add(Settings.NODE_NUMBER - actual_infected);
                patched_per_step.add(actual_patched);
            } else {
                stoppend++;
                if (stoppend == 1) {
                   System.err.println("Max reached step: " + actualStep); 
                }
                infected_per_step.add(actual_infected);
                healthy_per_step.add(Settings.NODE_NUMBER - actual_infected);
                patched_per_step.add(actual_patched);                
                actualStep++;
            }
            //System.out.println("Esecuzione step: " + actualStep);
        }
        saveNodeData();
        int infected_nodes = 0;
        int healthy_nodes = 0;
        for (Node node : nodes.values()) {
            if(node.isInfected()){
                infected_nodes++;
            } else {
                healthy_nodes++;
            }
        }
        double percIncontriInf = (contagi*100)/incontri;
        UtilityTools.saveTrend(infected_per_step, "infected_trend");
        UtilityTools.saveTrend(healthy_per_step, "healthy_trend");
        UtilityTools.saveTrend(patched_per_step, "patched_trend");
        System.out.println("Nodi infetti: " + infected_nodes + "\nNodi sani: " + healthy_nodes);
        System.out.println("Nodi patchati totali: " + totalPatched);
        System.out.println("Incontri: " + incontri);
        System.out.println("Percetuale incontri infetti: " + percIncontriInf + "%");
        System.out.println("Contagi totali avvenuti: " + contagi);
        System.out.println("Contagi hotspot avvenuti: " + contagi_hotspot);
        System.out.println("Contagi avvenuti in travelling: " + (contagi - contagi_hotspot));
        System.out.println(infected_nodes + "\n" + healthy_nodes);
        System.out.println(totalPatched); 
        System.out.println(incontri);
        System.out.println(percIncontriInf);
        System.out.println(contagi);
        System.out.println(contagi_hotspot);
        System.out.println((contagi - contagi_hotspot));
        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;
        System.out.println("Execution time in milliseconds: " + timeElapsed);
        
    }

    /**
     * Generate Nodes in the map in random positions.
     */
    private static void generateNodes(){
        int max_Android = (int) (Settings.NODE_NUMBER * Settings.ANDROID_DISTRIBUTION);
        int max_iOS = (int) (Settings.NODE_NUMBER * Settings.IOS_DISTRIBUTION);
        int max_Windows = (int) (Settings.NODE_NUMBER * Settings.WINDOWS_DISTRIBUTION);
        int nodeNumber = Settings.NODE_NUMBER;
        int android_counter = 0;
        int iOS_counter = 0;
        int windows_counter = 0;
        int x_min = Settings.NODE_RANGE + 1;
        int x_max = Settings.WIDTH - Settings.NODE_RANGE;
        int y_min = Settings.NODE_RANGE + 1;
        int y_max = Settings.HEIGHT - Settings.NODE_RANGE;
        for (int i = 0; i < nodeNumber; i++) {
            //Generate coordinates for a new node
            Point node_coordinates = UtilityTools.generate_position(x_min, x_max, y_min, y_max);
            while(posOccupate.contains(node_coordinates)) {
                System.err.println(node_coordinates.toString() + "posizione occupata");
                node_coordinates = UtilityTools.generate_position(x_min, x_max, y_min, y_max);
            }
            posOccupate.add(node_coordinates);
            if (android_counter < max_Android) {
                nodes.put(i, new Node(i, 0, "Android", 0, node_coordinates, 0));
                android_counter++;
            } else if (iOS_counter < max_iOS) {
                nodes.put(i, new Node(i, 0, "iOS", 0, node_coordinates, 0));
                iOS_counter++;
            } else {
                nodes.put(i, new Node(i, 0, "Windows", 0, node_coordinates, 0));
                windows_counter++;
            }
        }
        //Infect a random node
        int node_pos = rand.nextInt(Settings.NODE_NUMBER);
        nodes.get(node_pos).setHealth_condition(1);
        if (!Settings.CROSS_OS) {
            switch(nodes.get(node_pos).getOs()){
                case("Android"): 
                    max_infected = (int) (android_counter * Settings.MAX_INF); 
                    break;
                case("iOS"): 
                    max_infected = (int) (iOS_counter * Settings.MAX_INF);
                    break;
                case ("Windows"): 
                    max_infected = (int) (windows_counter * Settings.MAX_INF);
                    break;
            }
        } else {
            max_infected = (int) (Settings.NODE_NUMBER * Settings.MAX_INF);
        }
        System.out.println("Android: " + android_counter + " iOS: " + iOS_counter + " Windows: " + windows_counter);        
        System.out.println("OS nodo infetto: " + nodes.get(node_pos).getOs());
        DAONode.saveNode_pos(nodes, "nodes_init_pos");
    }
    
    
    /**
     * Generates hotspot in random positions on the map.
     */
    private static void generateHotspots(){
        int i = 0;
        ArrayList<Point> posOccupate_hotspot = new ArrayList<>();
        while (i < Settings.HOTSPOT_NUMBER) {            
            int x_min = Settings.HOTSPOT_RANGE;
            int x_max = Settings.WIDTH - Settings.HOTSPOT_RANGE;
            int y_min = Settings.HOTSPOT_RANGE;
            int y_max = Settings.HEIGHT - Settings.HOTSPOT_RANGE;
            Point hotspot_coordinates = UtilityTools.generate_position(x_min, x_max, y_min, y_max);
            for (Point point : posOccupate_hotspot) {
                while (hotspotOutOfRange(point, hotspot_coordinates)) {
                    hotspot_coordinates = UtilityTools.
                    generate_position(x_min, x_max, y_min, y_max);
                }
            }
            posOccupate_hotspot.add(hotspot_coordinates);
            Hotspot h = new Hotspot(i, hotspot_coordinates);
            //System.out.println("Hotspot generato " + h.toString());
            hotspots.put(i ,h);
            i++;
        }
        DAOHotspot.saveHotspotsPosition(hotspots);
    }
    
    /**
     * Check if the two input hotspot area overlaps.
     * @param hs1
     * @param hs2
     * @return 
     */
    private static boolean hotspotOutOfRange(Point hs1, Point hs2){
        return (UtilityTools.compute_distance(hs1,hs2)) < (2 * Settings.HOTSPOT_RANGE);
    }

    /**
     * Pick a random hotspot in the map. Used as node travel destination 
     * @return 
     */
    private static Hotspot nextHotspot(){
        int hotspot_index = rand.nextInt(Settings.HOTSPOT_NUMBER);
        Hotspot hotspot = hotspots.get(hotspot_index);
        return hotspot;
    }
    
                      
    private static void step_exection(){
        for (Node n : nodes.values()) {
            if (actualStep >= Settings.START_UPDATE){
                getUpdate(n);
            }            
            //System.out.println("Nodo ID: " + n.getId());
            int actualStatus = n.getStatus();
            //System.out.println("Stato del nodo " + n.getStatus());
            //se il nodo è fermo viene scelta la prossima azione
            if (n.getStatus() == 0) {
                int nextAction = n.getNextAction();
                //il nodo esplora l'hotspot
                switch (nextAction) {
                    case 0:
                        if (n.isInfected()) {
                            n.checkNeighborhood(nodes, actualStep);
                        }
                        break;   
                    case 1:
                        n.setStatus(1);
                        if (n.getActualHotspot() == null) {
                            Hotspot dest_hotspot = nextHotspot();
                            n.setNextHotspot(dest_hotspot);
                            Point destination = dest_hotspot.aviableLocaion();
                            n.setDest(destination);
                            n.setStatus(2);
                            movingNode(n, destination);
                        } else {
                            movingNode(n, hotspots.get(n.getActualHotspot().getId())
                                    .aviableLocaion());
                        }
                        break;
                    //il nodo adra in un altro hotspot
                    case 2:
                        //System.out.println("Definizione prossimo Hotspot");
                        Hotspot dest_hotspot = nextHotspot();
                        while(n.getActualHotspot() != null && 
                                dest_hotspot.getId() == n.getActualHotspot().getId()){
                            dest_hotspot = nextHotspot();
                        }
                        //System.out.println("Hotspot definito: " + dest_hotspot.getId());
                        n.setNextHotspot(dest_hotspot);
                        if (n.getActualHotspot() != null) {
                            hotspots.get(n.getActualHotspot().getId()).removeNode(n);
                        }
                        n.setActualHotspot(null);
                        Point destination = dest_hotspot.aviableLocaion();
                        n.setDest(destination);
                        n.setStatus(2);
                        movingNode(n, destination);
                        break;
                }
           } else {
               //System.out.println("Il nodo continua a muoversi");
               movingNode(n, n.getDest());
           }
        }
    }    
 
    
    private static void movingNode(Node n, Point dest){
        //System.out.println("destinazione " + dest);
        Point actualNodeLoc = n.getLocation();
        int new_Y = (int) actualNodeLoc.getY();
        int new_X = (int) actualNodeLoc.getX();
        int y_distance = (int) Math.abs(new_Y - dest.getY());
        int x_distance = (int) Math.abs(new_X - dest.getX());
        //System.out.println("posizione attuale " + actualNodeLoc);
        if (UtilityTools.nearDestination(n, dest) 
                || n.getLocation().equals(dest)
                || (x_distance < Settings.NODE_SPEED && y_distance < Settings.NODE_SPEED)) {
            /*
            System.out.println("Adattamento posizione");
            System.out.println(n.getLocation());
            System.out.println(dest);
            System.out.println("Il nodo " + n.getId() + " è arrivato a destinazione");
            */
            n.setActualHotspot(hotspots.get(n.getNextHotspot().getId()));
            hotspots.get(n.getActualHotspot().getId()).addNode(n);
            n.setLocation(dest);
            n.setStatus(0);
            if (n.isInfected()) {
                //System.out.println("Verifico la presenza di nodi da contagiare");
                n.checkNeighborhood(nodes, actualStep);
            }
        } else {
            if (x_distance >= Settings.NODE_SPEED){
                if (actualNodeLoc.getX() < dest.getX()) {
                    new_X = (int) (actualNodeLoc.getX() + Settings.NODE_SPEED);
                } else if (actualNodeLoc.getX() > dest.getX()) {
                    new_X = (int) (actualNodeLoc.getX() - Settings.NODE_SPEED);
                }
            } 
            if (y_distance >= Settings.NODE_SPEED){
                if (actualNodeLoc.getY() < dest.getY()) {
                    new_Y = (int) (actualNodeLoc.getY() + Settings.NODE_SPEED);
                } else if (actualNodeLoc.getY() > dest.getY()) {
                    new_Y = (int) (actualNodeLoc.getY() - Settings.NODE_SPEED);
                }
            }
            n.setLocation(new Point(new_X, new_Y));
            //System.out.println("nuova posizione " + n.getLocation() + "\n");
            if (n.getNextHotspot().isInRange(n)) {
                n.getNextHotspot().addNode(n);
            }
            if (n.isInfected()) {
                n.checkNeighborhood(nodes, actualStep);
            }
            posOccupate.remove(actualNodeLoc);
            posOccupate.add(n.getLocation());
        }
    }

    private static void getUpdate(Node n) {
        double update = rand.nextDouble();
        if (update <= Settings.UPDATE_PROBABILITY && n.getOs_version() == 0) {
            n.setOs_version(1);
            if (actual_infected > 0 && n.isInfected()) {
                actual_infected--;
            }
            n.setHealth_condition(0);
            //System.out.println("*******Nodo "+ n.getId() + " patchato");
            actual_patched++;
            totalPatched++;
        }
    }
    
    
    
    public static Hotspot getHotspot(Integer id){
        return hotspots.get(id);
    }
    
    public static Node getNode(Integer id){
        return nodes.get(id);
    }
    
    private static void saveNodeData(){
        DAONode.saveNode_pos(nodes, "nodes_final_pos");
        DAONode.saveNode_rel(nodes);
        DAONode.saveNode_history(nodes);
        DAONode.saveNodes_infectionStep(nodes);
    }
}