/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pandemy.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import pandemy.Pandemy;
import pandemy.Settings;
import pandemy.UtilityTools;

/**
 *
 * @author nunzi
 */
public class Node {
    
    private int id;
    //Health Condition 0 or 1
    private int health_condition;
    //Salve l'id del nodo e lo step in cui lo ha contagiato
    private HashMap<Integer, Integer> reletionship = new HashMap<>();
    private Hotspot actualHotspot;
    private Hotspot nextHotspot;
    private String os;
    private int os_version;
    private Point location;
    //0 Helting, 1 Exploring o 2 Traveling
    private int status;
    private Point dest;
    private int infectionStep;
    private ArrayList<Point> storico_pos = new ArrayList<>();

    public Node(int id, int health_condition, String os, int os_version, Point location, int status) {
        this.id = id;
        this.health_condition = health_condition;
        this.os = os;
        this.os_version = os_version;
        this.location = location;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHealth_condition() {
        return health_condition;
    }

    public void setHealth_condition(int health_condition) {
        this.health_condition = health_condition;
    }
    
    public boolean isInfected(){
        return this.health_condition == 1;
    }
    
    public HashMap<Integer, Integer> getReletionship() {
        return reletionship;
    }

    public void addReletion(Integer id, Integer step) {
        this.reletionship.put(id, step);
    }


    public Hotspot getActualHotspot() {
        return actualHotspot;
    }

    public void setActualHotspot(Hotspot actualHotspot) {
        this.actualHotspot = actualHotspot;
    }

    public Hotspot getNextHotspot() {
        return nextHotspot;
    }

    public void setNextHotspot(Hotspot nextHotspot) {
        this.nextHotspot = nextHotspot;
    }
    

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public int getOs_version() {
        return os_version;
    }

    public void setOs_version(int os_version) {
        this.os_version = os_version;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
        storico_pos.add(location);
    }

    public ArrayList<Point> getStorico_pos() {
        return storico_pos;
    }
    

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Point getDest() {
        return dest;
    }

    public void setDest(Point dest) {
        this.dest = dest;
    }
    
    public boolean isAtDestination(){
        if (this.dest == null) {
            return true;
        }
        return this.dest.equals(this.location);
    }

    public int getInfectionStep() {
        return infectionStep;
    }

    public void setInfectionStep(int infectionStep) {
        this.infectionStep = infectionStep;
    }
    
    
    
    /**
     * 
     * 
     *
     * @param nodes: HashMap of Nodes ID and Nodes
     * @param step: actual step
     * @return
     */
    public void checkNeighborhood(HashMap<Integer, Node> nodes, int step) {
        if (this.actualHotspot != null
                && UtilityTools.compute_distance(this.location, actualHotspot.getHotspot_center()) <= Settings.HOTSPOT_RANGE) {
            for (Node node : Pandemy.getHotspot(actualHotspot.getId()).getNodes().values()) {
                if (node.getId() == this.id) {
                    continue;
                }
                if (node.getHealth_condition() == 0) {
                    double distanza = UtilityTools.compute_distance(this.location, node.getLocation());
                    //System.out.println("Hotspot checking");
                    //System.out.println("Node: " + node.toString());
                    if (virus_transmission(node, step)) {
                        Pandemy.contagi_hotspot++;
                    }
                }
            }
        } else {
            for (Node node : nodes.values()) {
                if (node.getId() == this.id) {
                    continue;
                }
                if (node.getHealth_condition() == 0) {
                    virus_transmission(node, step);
                }
            }
        }
    }

    private boolean virus_transmission(Node n, int step) {
        double distanza = UtilityTools.compute_distance(this.location, n.getLocation());
        if (distanza <= Settings.NODE_RANGE
                && (this.os.equals(n.os) || Settings.CROSS_OS)
                && n.getOs_version() == 0) {
            Pandemy.incontri++;
            if (UtilityTools.probability_infected()) {
                /*
                System.out.println("__________________________________________________________________________________________");
                System.out.println("Step attuale:" + step);
                System.out.println("Nodo attuale: " + this.id + " nodo in range: " + n.getId());
                System.out.println("distanza: " + distanza);
                System.out.println("Nodo attuale: " + this.location.toString() + " nodo in range: " + n.getLocation().toString());
                System.out.println("Nodo attuale OS: " + this.os + " nodo in range: " + n.getOs());
                System.out.println("Nodo attuale versione OS: " + this.os_version + " nodo in range: " + n.getOs_version());
                System.out.println("Nodo attuale stato di salute: " + this.health_condition + " nodo in range: " + n.getHealth_condition());
                System.out.println("Nodo attuale stato : " + this.status + " nodo in range: " + n.getStatus());
                System.out.println("Nodo attuale hotspot: " + this.actualHotspot + " nodo in range: " + n.getActualHotspot());
                System.out.println("__________________________________________________________________________________________");
                */
                n.setHealth_condition(1);
                n.setInfectionStep(step);
                Pandemy.contagi++;
                Pandemy.actual_infected++;
                //System.out.println("Nodo: " + n.getId() + " infettato");
                if (!n.getReletionship().containsKey(this.id)) {
                    this.reletionship.put(n.id, step);
                    //System.out.println("Nodi incontrati:\n" + this.reletionship);
                }
                return true;
            } 
        }
        return false;
    }
    
    public int getNextAction() {
        Random rand = new Random();
        double action_choise = rand.nextDouble();
        if (action_choise <= Settings.HALTING_PROBABILITY) {
            //System.out.println("Il nodo sta fermo");
            return 0;
        } else if (action_choise > Settings.HALTING_PROBABILITY
                && action_choise <= (Settings.HALTING_PROBABILITY + Settings.EXPLORING_PROBABILITY)) {
            //System.out.println("Il node è in esplorazione");
            return 1;
        } else {
            //System.out.println("Il nodo è in travelling");
            return 2;
        }
    }
    
    
    @Override
    public String toString(){
        return "Id " + id +
                " Health Codition " + health_condition +
                " Status " + status +
                " OS " + os +
                " OS version " + os_version +
                " Postion: " + location.x + " " + location.y +
                " Actual Hotspot " + actualHotspot.toString();
    }
    
    
}
