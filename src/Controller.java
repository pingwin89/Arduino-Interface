import gnu.io.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TooManyListenersException;

public class Controller {
    
    public Main main;

    public Controller(Main main){
    this.main=main;
    }
    
    @FXML Button on;
    @FXML Button off;
    @FXML Button buzzer;
    @FXML Button servoLeft;
    @FXML Button servoRight;
    @FXML Button connect;
    @FXML ComboBox<String> combo;
    
    public void initialize(){
        
        ObservableList olist = FXCollections.observableArrayList(main.portsList);
        combo.setItems(olist);
        
        connect.setOnAction(event->{
            if(main.connect(combo.getSelectionModel().getSelectedItem())) connect.setDisable(true);
        });
        
        on.setOnAction(event->{
            try {
                main.output.write(1);
                main.output.flush();
                System.out.println("sent 1");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        
        off.setOnAction(event->{
            try {
                main.output.write(2);
                main.output.flush();
                System.out.println("sent 2");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        
        buzzer.setOnAction(event->{
            try {
                main.output.write(3);
                main.output.flush();
                System.out.println("sent 3");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        
        servoLeft.setOnAction(event->{
            try {
                main.output.write(5);
                main.output.flush();
                System.out.println("sent 5");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        
        servoRight.setOnAction(event->{
            try {
                main.output.write(6);
                main.output.flush();
                System.out.println("sent 6");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        
    }

}