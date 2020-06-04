package system.billboard;

import org.w3c.dom.*;
import system.User;
import system.billboard.image.Image;
import system.billboard.image.ImageURL;
import system.billboard.schedule.Schedule;
import system.billboard.textfield.Information;
import system.billboard.textfield.Message;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import java.io.Serializable;
import java.nio.file.Path;

/**
 * Class representing billboards
 */
public class Billboard implements Serializable {

    private String title = "Untitled";
    private User author;
    private Integer billboardID; // can be null before initialised
    private Color backgroundColour;
    private Color messageColour;
    private Color informationColour;
    private Message message;
    private String imageUrl = "";
    private Information information;
    private Image image;
    private Schedule schedule;


    /**
     * Constructor, requiring an associated author. Used for when client want to create a billboard to be
     * sent over network but doesn't yet know what its ID will be.
     * @param author User responsible for creating the billboard.
     */
    public Billboard(User author) {
        this.author = author;
        backgroundColour = Color.white; // Default
    }

    /**
     * Constructor requiring both author and ID for the server to create billboards for sending back to the
     * client.
     * @param billboardID Uniquely identifying ID auto-incremented when billboards are added to the database.
     */
    public Billboard(User author, Integer billboardID) {
        this.author = author;
        this.billboardID = billboardID;
        backgroundColour = Color.white;
    }


    // ============== Getters & setters ============= //
    // ============================================== //
    // These will need to more tightly control user input (Max characters, non-negID etc..)
    // Title
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    // Message
    public Message getMessage() {
        return message;
    }
    public void setMessage(Message message) {
        this.message = message;
    }

    // Image URL
    public String getURL() {
        return imageUrl;
    }
    public void setURL(String URL)
    {
        this.imageUrl = URL;
    }



    // Information
    public Information getInformation() {
        return information;
    }
    public void setInformation(Information information) {
        this.information = information;
    }
    // Image
    public Image getImage() {
        return image;
    }
    public void setImage(Image image) {
        this.image = image;
    }
    // ID
    public Integer getID() {
        return billboardID;
    }
    public void setID(Integer billboardID) {
        this.billboardID = billboardID;
    }
    // Author
    public User getAuthor() {
        return author;
    }
    // Background colour
    public Color getBackgroundColour() {
        return backgroundColour;
    }
    public void setBackgroundColour(Color backgroundColour) {
        this.backgroundColour = backgroundColour;
    }

    // Message colour
    public Color getMessageColour() {
        return messageColour;
    }
    public void setMessageColour(Color messageColour) {
        this.messageColour = messageColour;
    }

    // Information colour
    public Color getInformationColour() {
        return informationColour;
    }
    public void setInformationColour(Color informationColour) {
        this.informationColour = informationColour;
    }


    public Schedule getSchedule() {
        return schedule;
    }
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return title;

    }

    // ============================================== //
    // ============================================== //


    public String getScheduleString() {

        String author = "None";
        String title = "None";
        String schedule = "None";

        if (this.schedule != null) schedule = this.schedule.toString();
        if (this.title != null) title = this.title;
        if (this.author != null) author = this.author.getUserID();

        return (author + " -- " + title + " -- " + schedule);

    }

    // To do..

    // public getBackgroundColourHex(), public setBackgroundColourHex() might be useful here and in text field

    // Factory method that creates a billboard instance from a given XML file
    //String path, User author
    public static Billboard importXML(String path, User author) {

        Billboard b = new Billboard(author);
        try {
            // Creating a constructor of file class and parsing an XML file
            File file = new File(path);

            // An instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            // An instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("billboard");

            // NodeList is not iterable, so we are using for
            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                Node Node = nodeList.item(itr);
                System.out.println("\nNode Name :" + Node.getNodeName());

                // Grab value of background colour
                if(doc.getElementsByTagName("billboard").getLength() != 0 && doc.getElementsByTagName("billboard").item(0).getAttributes().getLength() != 0)
                {
                    System.out.println("Background colour: " + doc.getElementsByTagName("billboard").item(0).getAttributes().item(0).getTextContent());
                }

                if (Node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) Node;

                    if (eElement.getElementsByTagName("message").getLength() != 0) {
                        if (eElement.getElementsByTagName("message").item(0).getAttributes().getLength() != 0) {
                            System.out.println("Message colour: " + eElement.getElementsByTagName("message").item(0).getAttributes().item(0).getTextContent());
                        }
                        System.out.println("Billboard message: " + eElement.getElementsByTagName("message").item(0).getTextContent());
                        Message message = new Message(eElement.getElementsByTagName("message").item(0).getTextContent());
                        b.setMessage(message);
                    }

                    if (eElement.getElementsByTagName("information").getLength() != 0) {
                        if (eElement.getElementsByTagName("information").item(0).getAttributes().getLength() != 0) {
                            System.out.println("Information colour: " + eElement.getElementsByTagName("information").item(0).getAttributes().item(0).getTextContent());
                        }
                        System.out.println("Billboard information: " + eElement.getElementsByTagName("information").item(0).getTextContent());
                        Information information = new Information(eElement.getElementsByTagName("information").item(0).getTextContent());
                        b.setInformation(information);
                    }

                    if (eElement.getElementsByTagName("picture").getLength() != 0) {
                        String buffer = (eElement.getElementsByTagName("picture").item(0).getAttributes().item(0).getTextContent());
                        System.out.println("Billboard picture: " + buffer);
                        system.billboard.image.ImageURL image = new ImageURL(buffer);
                        b.setImage(image);
                    }

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



        // Read an XML file and Populate b's parameters from it.
        // (use b.setTitle("XML billboards title") etc)

        return b;
    }

    // Method that uses this instance to generate an xml representation of this billboard
    public void exportXML(File path)
    {

        Billboard billboard = this;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element mainRootElement = doc.createElement("billboard");
            doc.appendChild(mainRootElement);

            if (billboard.getMessage() != null)
            {
                Element messageNode = doc.createElement("Message");
                messageNode.appendChild(doc.createTextNode(billboard.getMessage().toString()));

                billboard.getMessage().getColour();
                Attr attr = doc.createAttribute("colour");
                attr.setValue(String.valueOf(billboard.getMessage().getColour()));
                messageNode.setAttributeNode(attr);
                mainRootElement.appendChild(messageNode);

                // need option to select stored message colour
                //mainRootElement.setAttribute("colour", billboard.getBackgroundColour());
                // tdd required before completing further
            }

            if (billboard.getImage() != null)
            {
                Element imageNode = doc.createElement("picture");

                Attr attr;
                if(billboard.getImage().getClass() == system.billboard.image.ImageURL.class) {

                    attr = doc.createAttribute("url");

                }else{
                    attr = doc.createAttribute("data");

                }
                attr.setValue(String.valueOf(billboard.getImage().getData()));
                imageNode.setAttributeNode(attr);
                mainRootElement.appendChild(imageNode);

                // need option to select stored message colour
                //mainRootElement.setAttribute("colour", billboard.getBackgroundColour());
                // tdd required before completing further
            }
            if(billboard.getInformation() != null){
                Element informationNode = doc.createElement("Information");
                informationNode.appendChild(doc.createTextNode(billboard.getInformation().toString()));

                Attr attr = doc.createAttribute("colour");
                attr.setValue(String.valueOf(billboard.getInformation().getColour()));
                informationNode.setAttributeNode(attr);
                mainRootElement.appendChild(informationNode);
            }


            // output dom xml to console
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult console = new StreamResult(new File(String.valueOf(path)));
            transformer.transform(source, console);

            //System.out.println("\n XML DOM Created Successfully...");

        } catch (ParserConfigurationException | TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }


        // Creates an XML file based on set billboard parameters.
        // To call: b.exportXML("path")
    }

    // Creates an identical copy of this billboard
    public Billboard copy() {
        Billboard billboard = new Billboard(author);
        if (title != null) billboard.setTitle(title);
        if(imageUrl != null) billboard.setURL(imageUrl);
        if (billboardID != null) billboard.setID(billboardID);
        if (backgroundColour != null) billboard.setBackgroundColour(backgroundColour);
        if (message != null) billboard.setMessage(message);
        if (information != null) billboard.setInformation(information);
        if (image != null) billboard.setImage(image);
        if (schedule != null) billboard.setSchedule(schedule);
        return billboard;
    }
}