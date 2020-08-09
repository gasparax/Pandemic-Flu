/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pandemy.persistance;

import com.google.gson.Gson;
import java.awt.Point;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import pandemy.UtilityTools;
import pandemy.model.Hotspot;

/**
 *
 * @author Nunzio
 */
public class DAOHotspot {
    
    private static Gson gson = new Gson();
    
    
    public static void saveHotspotsPosition(HashMap<Integer, Hotspot> hotspots){
        ArrayList<Point> hotspots_pos = new ArrayList<>();
        for (Hotspot hotspot : hotspots.values()) {
            hotspots_pos.add(hotspot.getHotspot_center());
        }
        try {
            String filePath = "hotspot//hotspots_position.json";
            Writer writer = new FileWriter(filePath);
            gson.toJson(hotspots_pos, writer);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(UtilityTools.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
