
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
    
    public ArrayList<String> portsList;
    public Enumeration ports = null;
    public HashMap portMap = new HashMap();
    public CommPortIdentifier selectedPortIdentifier = null;
    public SerialPort serialPort = null;
    public InputStream input = null;
    public OutputStream output = null;
    public final int TIMEOUT = 2000;
    public final int SPACE_ASCII = 32;
    public final int DASH_ASCII = 45;
    public final int NEW_LINE_ASCII = 10;
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        Main main = new Main();
        main.lookForPorts();
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("Main.fxml"));
        Controller controller = new Controller(this);
        loader.setController(controller);
        AnchorPane anchorPane = (AnchorPane) loader.load();
        primaryStage.setTitle("Controlls");
        Scene scene = new Scene(anchorPane);
        primaryStage.setScene(scene);
        primaryStage.show();
        
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
    
    public  void lookForPorts(){
        portsList = new ArrayList<String>();
      //PART 1: searching for available ports
        ports = CommPortIdentifier.getPortIdentifiers();
                while (ports.hasMoreElements()){
                    CommPortIdentifier curPort = (CommPortIdentifier) ports.nextElement();
                    //get only serial ports
                    if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL)
                    {
                        portMap.put(curPort.getName(), curPort);
                        portsList.add(curPort.getName());
                        System.out.println("Found port: "+curPort.getName());
                    }
                }
    }
    
    public boolean connect(String selection){
        //PART 2 : connecting to selected port
        String selectedPort = selection;
        selectedPortIdentifier = (CommPortIdentifier) portMap.get(selectedPort);
        CommPort commPort = null;

        try {
            //the method below returns an object of type CommPort
            commPort = selectedPortIdentifier.open(selectedPort, TIMEOUT);
            //the CommPort object can be casted to a SerialPort object
            serialPort = (SerialPort)commPort;
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
            output = serialPort.getOutputStream();
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
