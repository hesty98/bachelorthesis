package director_repository;

import EnvironmentObjects.Software.Software;
import Messages.SoftwareSearchRequest;

import java.util.ArrayList;

public class SUPR {

    /*
    Todo: add searchParam and Kennzahlen
     */
    public static ArrayList<Software> findBestSoftware(SoftwareSearchRequest request){
        return null;
    }

    public static void determineIfSoftwareIsNeeded(){
        System.err.println("------------------------------------- SUPR -------------------------------------");
        System.err.println("Server bestimmt die Mehrwerte der Software für das Fahrzeug.");
        System.err.println("Funktionalität nicht implementiert, soll lediglich den Ablauf verdeutlichen.");
        System.err.println("--------------------------------------------------------------------------------");
    }
}
