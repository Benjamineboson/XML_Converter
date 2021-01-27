package sample;

import sample.converter.XMLConverter;
import java.util.Scanner;

public class Controller {

    private XMLConverter xmlConverter;
    private String convertedText;
    private String textToBeConverted;


    public Controller(String textToBeConverted) {
        this.textToBeConverted = textToBeConverted;
        this.xmlConverter = new XMLConverter(this.textToBeConverted);
    }

    public void convert(){
        System.out.println();
        xmlConverter.main(null);
        this.convertedText = readFile();
    }

    private String readFile(){
        StringBuilder sb = new StringBuilder();
        Scanner myReader = new Scanner(xmlConverter.getConvertedText());
        System.out.println("----------------Input----------------\n"
                +textToBeConverted
                +"\n----------------Output----------------\n"
                +xmlConverter.getConvertedText());
        while (myReader.hasNextLine()) {
                sb.append(myReader.nextLine()+"\n");
            }
        return sb.toString();
    }

    public String getConvertedText() {
        return convertedText;
    }

}
