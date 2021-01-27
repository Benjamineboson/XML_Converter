package sample.converter;

public class XMLConverter {
    private static Converter converter;
    private String textToBeConverted;
    private static String convertedText;
    public XMLConverter(String textToBeConverted) {
        this.textToBeConverted = textToBeConverted;
        converter = new Converter(this.textToBeConverted);
    }

    public static void main(String[] args )
    {
        converter.convert();
        convertedText = converter.getConverted();
    }

    public String getConvertedText() {
        return convertedText;
    }
}
