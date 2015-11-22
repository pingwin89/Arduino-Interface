
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import gnu.io.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TooManyListenersException;

public class Main extends Application {
    
    public static ArrayList<String> listaPortow;
    public static Enumeration ports = null;
    public static HashMap portMap = new HashMap();
    public static CommPortIdentifier selectedPortIdentifier = null;
    public static SerialPort serialPort = null;
    public static InputStream input = null;
    public static OutputStream output = null;
    public static int TIMEOUT = 2000;
    public static int SPACE_ASCII = 32;
    public static int DASH_ASCII = 45;
    public static int NEW_LINE_ASCII = 10;
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        lookForPorts();
        AnchorPane anchorPane = (AnchorPane) FXMLLoader.load(ClassLoader.getSystemResource("Main.fxml"));
        primaryStage.setTitle("Controlls");
        Scene scene = new Scene(anchorPane);
        primaryStage.setScene(scene);
        primaryStage.show();
        //connect();
        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
            
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public static void lookForPorts(){
        listaPortow = new ArrayList<String>();
      //PART 1: searching for available ports
        Main.ports = CommPortIdentifier.getPortIdentifiers();
                while ( Main.ports.hasMoreElements()){
                    CommPortIdentifier curPort = (CommPortIdentifier) Main.ports.nextElement();
                    //get only serial ports
                    if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL)
                    {
                        Main.portMap.put(curPort.getName(), curPort);
                        listaPortow.add(curPort.getName());
                        System.out.println("Found port: "+curPort.getName());
                    }
                }
    }
    
    public static boolean connect(String selection){
        //PART 2 : connecting to COM7
        String selectedPort = selection;
        Main.selectedPortIdentifier = (CommPortIdentifier) Main.portMap.get(selectedPort);
        CommPort commPort = null;

        try {
            //the method below returns an object of type CommPort
            commPort = Main.selectedPortIdentifier.open("COM7", Main.TIMEOUT);
            //the CommPort object can be casted to a SerialPort object
            Main.serialPort = (SerialPort)commPort;
            System.out.println(selectedPort + " opened successfully.");
      
        }
        catch (PortInUseException e){
            System.out.println(selectedPort + " is in use. (" + e.toString() + ")");
            return false;
        }
        catch (Exception e){
            System.out.println("Failed to open " + selectedPort + "(" + e.toString() + ")");
            return false;
        }     
        
        //PART 3 : initializing stream
        try{
            Main.output = Main.serialPort.getOutputStream();
            System.out.println("Stream opened succesfully");
        }
        catch (IOException e) {
            System.out.println("I/O Streams failed to open. (" + e.toString() + ")");
            return false;
        }
        
        // waiting for arduino to reset before sending data
        try {
            Thread.sleep(4000);
            return true;
        } catch (InterruptedException e1) {
            e1.printStackTrace();
            return false;
        }
    }
    

}