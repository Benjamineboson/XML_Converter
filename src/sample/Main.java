package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class Main extends Application {

    private Controller c;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("XML Converter");
        stage.setScene(setScene());
        stage.show();
    }

    private Scene setScene(){
        Label label = createLabel();
        TextArea inputTextArea = createInputTextArea();
        TextArea outputTextArea = createOutputTextArea();
        Button button = createButton();

        GridPane gridPane = new GridPane();
        Group root = new Group(gridPane);
        Scene scene = new Scene(root, 945, 700, Color.WHITE);

        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);


        gridPane.add(label,0,0);
        gridPane.add(inputTextArea,0,1);
        gridPane.add(outputTextArea,1,1);
        gridPane.add(button,0,3);
        button.setOnAction(action ->  {
            this.c = new Controller(inputTextArea.getText().trim());
            c.convert();
            outputTextArea.setText(c.getConvertedText());
        });
        return scene;
    }

    private Label createLabel (){
        Label label = new Label("Input LSV (Line separated values)");
        Font font = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12);
        label.setFont(font);
        return label;
    }

    private TextArea createInputTextArea (){
        TextArea area = new TextArea();
        area.setText("P|Firstname|Lastname"
                +"\nA|Street|City|Zipcode"
                +"\nT|Mobile|Home"
                +"\nF|Name|Year"
                +"\nA|Street|City|Zipcode");
        area.setPrefColumnCount(15);
        area.setPrefHeight(600);
        area.setPrefWidth(460);
        return area;
    }

    private TextArea createOutputTextArea (){
        TextArea area = new TextArea();
        area.setText("OUTPUT");
        area.setPrefColumnCount(15);
        area.setPrefHeight(600);
        area.setPrefWidth(460);
        area.setEditable(false);
        return area;
    }

    private Button createButton(){
        Button button = new Button("Convert");
        Font font = Font.font("Courier New", FontWeight.BOLD, 15);
        button.setFont(font);
        return button;
    }


}
