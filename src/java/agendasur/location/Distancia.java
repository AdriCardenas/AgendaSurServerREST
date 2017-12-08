/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agendasur.location;

/**
 *
 * @author david
 */
public class Distancia {
    
    public Distancia(){
        
    }
    
    public static double getDistancia(double latitudEvento, double longitudEvento,
            double latitudUsuario, double longitudUsuario) {
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(latitudEvento - latitudUsuario);
        double lonDistance = Math.toRadians(longitudEvento - longitudUsuario);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(latitudUsuario)) * Math.cos(Math.toRadians(latitudEvento))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }
    
}
