package sample.converter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.*;

public class Converter {

    private String lineSeparatedValues;
    private Document doc;
    private DocumentBuilder documentBuilder;
    private List<String> linesToBeConverted;
    private List<List<String>> tags;
    private List<List<String>> values;
    private String converted;

    public Converter(String lineSeparatedValues) {
        this.lineSeparatedValues = lineSeparatedValues;
        try{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            this.documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        this.linesToBeConverted = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.values = new ArrayList<>();
        this.doc = documentBuilder.newDocument();
        this.converted = "";
    }

    public void convert (){
        readInput();
        parseTagsAndValues();
        buildTags();
        writeOutput();
    }

    private void readInput(){
        Scanner myReader = new Scanner(lineSeparatedValues);
        while (myReader.hasNextLine()) {
        linesToBeConverted.add(myReader.nextLine());
        }
    }

    private void parseTagsAndValues(){
        List<String> tempValues = new ArrayList<>();
        List<String> tempTags = new ArrayList<>();
        String personTagIdentifier = "P|p|";
        if (linesToBeConverted.size() > 1 && personTagIdentifier.contains(linesToBeConverted.get(0).charAt(0)+""+linesToBeConverted.get(0).charAt(1))) {
            for (int i = 0; i < linesToBeConverted.size(); i++) {
                if (i > 0 && personTagIdentifier.contains(String.valueOf(linesToBeConverted.get(i).charAt(0)))) {
                    tags.add(tempTags);
                    values.add(tempValues);
                    tempTags = new ArrayList<>();
                    tempValues = new ArrayList<>();
                } else if (i == linesToBeConverted.size() - 1) {
                    tags.add(tempTags);
                    values.add(tempValues);
                }
                if (linesToBeConverted.get(i).length() > 2 && linesToBeConverted.get(i).charAt(1) == '|') {
                    tempTags.add(String.valueOf(linesToBeConverted.get(i).charAt(0)));
                    tempValues.add(linesToBeConverted.get(i).substring(2) + " ");
                }
            }
        }
    }

    private void buildTags(){
        Element root = doc.createElement("people");
        doc.appendChild(root);
        for (int i = 0; i < tags.size(); i++) {
            Element person = doc.createElement("person");
            Element family = doc.createElement("family");
            for (int j = 0; j < tags.get(i).size(); j++) {
                String currentTag = tags.get(i).get(j);
                String[] valuesOfCurrentTag = values.get(i).get(j).split("\\|");
                switch (currentTag.toUpperCase()){
                    case "P":
                        buildPersonTag(valuesOfCurrentTag).forEach(person::appendChild);
                        break;
                    case "F":
                        List<Node> tagValues = buildFamilyTag(valuesOfCurrentTag);
                        for (Node tagValue:tagValues){
                            family.appendChild(tagValue);
                        }
                        person.appendChild(family);
                        break;
                    case "A":
                        Element address = doc.createElement("address");
                        buildAddressTag(valuesOfCurrentTag).forEach(address::appendChild);
                        if (tags.get(i).get(j - 1).equalsIgnoreCase("F") || (j > 2)
                                && (tags.get(i).get(j-2).equalsIgnoreCase("F"))) {
                            family.appendChild(address);
                        }else{
                            person.appendChild(address);
                        }
                        break;
                    case "T":
                        Element phone = doc.createElement("phone");
                        buildPhoneTag(valuesOfCurrentTag).forEach(phone::appendChild);
                        if (tags.get(i).get(j - 1).equalsIgnoreCase("F") || (j > 2)
                                && (tags.get(i).get(j-2).equalsIgnoreCase("F"))) {
                            family.appendChild(phone);
                        } else {
                            person.appendChild(phone);
                        }
                        break;
                }
                if (j < tags.get(i).size()-1 && tags.get(i).get(j+1).equalsIgnoreCase("F")) family = doc.createElement("family");
                root.appendChild(person);
            }
        }
    }

    private List<Node> buildPersonTag(String[] values){
        Element firstname = doc.createElement("firstname");
        Element lastname = doc.createElement("lastname");
        values = fillEmptyLines(values);
        if (values.length > 0){
            firstname.appendChild(doc.createTextNode(values[0]));
            if (values.length > 1) lastname.appendChild(doc.createTextNode(values[1]));
        }
        return Arrays.asList(firstname,lastname);
    }

    private List<Node> buildFamilyTag(String[] values){
        Element name = doc.createElement("name");
        Element born = doc.createElement("born");
        values = fillEmptyLines(values);
        if (values.length > 0){
            name.appendChild(doc.createTextNode(values[0]));
            if (values.length > 1) born.appendChild(doc.createTextNode(values[1]));
        }
        return Arrays.asList(name,born);
    }

    private List<Node> buildAddressTag(String[] values){
        Element street = doc.createElement("street");
        Element city = doc.createElement("city");
        Element zipCode = doc.createElement("zipcode");
        values = fillEmptyLines(values);
        if (values.length > 0){
            street.appendChild(doc.createTextNode(values[0]));
            if (values.length > 1) city.appendChild(doc.createTextNode(values[1]));
            if (values.length > 2) zipCode.appendChild(doc.createTextNode(values[2]));
        }
        return Arrays.asList(street,city,zipCode);
    }

    private List<Node> buildPhoneTag(String[] values){
        Element mobile = doc.createElement("mobile");
        Element home = doc.createElement("home");
        values = fillEmptyLines(values);
        if (values.length > 0){
            mobile.appendChild(doc.createTextNode(values[0]));
            if (values.length > 1) home.appendChild(doc.createTextNode(values[1]));
        }
        return Arrays.asList(mobile,home);
    }

    private void writeOutput(){
        try{
            TransformerFactory tranFactory = TransformerFactory.newInstance();
            Transformer transformer = tranFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            Source src = new DOMSource(doc);

            StringWriter outWriter = new StringWriter();
            Result result = new StreamResult(outWriter);

            transformer.transform(src, result);
            StringBuffer sb = outWriter.getBuffer();
            converted = sb.toString();
        }catch (TransformerException e){
            e.printStackTrace();
        }
    }

    private String[] fillEmptyLines (String[] values){
        return Arrays.stream(values)
                .map((val) -> (val.equals("") || val.equals(" ") ? "EMPTY" : val.trim())).toArray(String[]::new);
    }

    public String getConverted() {
        return converted;
    }
}





