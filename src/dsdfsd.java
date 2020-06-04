package client.controlpanel;

import system.User;
import system.billboard.image.Image;
import system.billboard.image.ImageData;
import system.billboard.textfield.Information;
import system.billboard.textfield.Message;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Custom JPanel for allowing users to customise selected and new billboards
 */
class Designer extends SwapPanel {

    private JTextArea imageURL;
    private JTextField message;
    private JTextArea information;
    private JTextField title;
    private JButton billboardBg;
    private JButton imgSelector;
    private JButton messageColour;
    private JButton informationColour;
    private JButton openXml;

    protected Designer() {

        // Initialise parent container and specify label
        super("THE DESIGNER LABEL GOES HERE");

        // Add components to content JPanel
        addComponents();


    }


    /**
     * Adds each control component to the designerPanel
     */

    private void addComponents() {
        content.setLayout(new GridLayout(5, 3));

        // Title
        // set to default black
        title = new JTextField(10);
        content.add(new JLabel("Title: "));
        content.add(title);
        content.add(new JLabel(""));

        // Message
        message = new JTextField(10);
        content.add(new JLabel("Message: "));
        content.add(message);

        // Message colour
        messageColour = new JButton("Message Colour");
        content.add(messageColour);


        // Information
        information = new JTextArea(3, 10);
        content.add(new JLabel("Information: "));
        content.add(information);

        // Information text colour
        informationColour = new JButton("Information Colour");
        content.add(informationColour);

        // Open XML file
        openXml = new JButton("Open XML");
        content.add(openXml);

        // Img Selector
        imgSelector = new JButton("Select IMG");
        content.add(imgSelector);

        // Background Colour
        billboardBg = new JButton("Background Colour");
        content.add(billboardBg);

        // Image Url
        imageURL = new JTextArea(5, 10);
        content.add(new JLabel( "Image URL:"));
        content.add(imageURL);

        billboardBg.addActionListener(e -> { getColour(1); });
        informationColour.addActionListener(e -> { getColour(2); });
        messageColour.addActionListener(e -> { getColour(3); });
        imgSelector.addActionListener(e -> { getImage(); });
        openXml.addActionListener(e -> getXML());

    }


    /**
     * Run when imgSelector is clicked
     */
    private void getImage() {

        JFileChooser file = new JFileChooser();
        file.setCurrentDirectory(new File(System.getProperty("user.home")));

        // Filter the files
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "JPG", "png", "Bmp");
        file.setFileFilter(filter);
        file.addChoosableFileFilter(filter);
        int result = file.showSaveDialog(null);

        // If the user clicks on save in JFileChooser


        if(result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = file.getSelectedFile();

            String path = ((File) selectedFile).getAbsolutePath();

            system.billboard.image.Image mage = new ImageData(path);
            bufferBillboard.setImage(mage);


        }
        else if(result == JFileChooser.CANCEL_OPTION) {
            System.out.println("No File Select");
        }
    }


    /**
     * Run when billboardBg is clicked
     */
    // 1 = billboardBg
    // 2 = messageColour
    // 3 = titleColour
    private void getColour(int type) {
        if (type == 1)
        {
            Color initColour = bufferBillboard.getBackgroundColour();
            Color colourChooser = JColorChooser.showDialog(this, "Choose Colour", initColour);
            bufferBillboard.setBackgroundColour(colourChooser);
            System.out.println(colourChooser);
        }

        if (type == 2)
        {
            Color initColour = bufferBillboard.getMessageColour();
            Color colourChooser = JColorChooser.showDialog(this, "Choose Colour", initColour);
            bufferBillboard.setMessageColour(colourChooser);
        }

        if (type == 3)
        {
            Color initColour = bufferBillboard.getTitleColour();
            Color colourChooser = JColorChooser.showDialog(this, "Choose Colour", initColour);
            bufferBillboard.setTitleColour(colourChooser);

        }
    }

    /**
     * Run when open XML is clicked
     */
    private void getXML() {
        User user = getUser();
        JFileChooser file = new JFileChooser();
        file.setCurrentDirectory(new File(System.getProperty("user.home")));

        // Filter the files
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.XML", "XML");
        file.addChoosableFileFilter(filter);
        file.setFileFilter(filter);
        int result = file.showSaveDialog(null);

        // If the user clicks on save in JFileChooser
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = file.getSelectedFile();

            String path = ((File) selectedFile).getAbsolutePath();

            system.billboard.image.Image mage = new ImageData(path);

            bufferBillboard.importXML(path, user);
        }

    }


    /**
     * Reads in the selected billboard's attributes to save them into this classes attributes for the user to edit
     */
    public Boolean validURL(String url)
    {
        try{
            BufferedImage image = ImageIO.read(new URL(url));
            if(image != null)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (MalformedURLException e)
        {
            System.err.println("URL error with image");
            return false;
        }
        catch (IOException e)
        {
            System.err.println("IO error with image");
            return false;
        }

    }


    public void extractBillboardAttributes() {

        // If the selected billboard does not
        if (bufferBillboard.getTitle() != null) title.setText(bufferBillboard.getTitle());
        else title.setText(null);

        if (bufferBillboard.getInformation() != null) information.setText(bufferBillboard.getInformation().getString());
        else information.setText(null);

        if(bufferBillboard.getMessage() !=null) message.setText(bufferBillboard.getMessage().getString());
        else message.setText(null);

        if(bufferBillboard.getURL() !=null) imageURL.setText(bufferBillboard.getURL());
        else imageURL.setText(null);
    }



    /**
     * Modifies the selected billboard when change occurs
     */
    protected void saveChanges() {
        String var = imageURL.getText();

        if (imageURL.getText().length()==0 || var == null)
        {
            imageURL.setText(null);
            var = null;
        }

        // If the URL text box has content then...
        if(var != null)
        {
            if (validURL(var) == true)
            {
                Image b = new ImageData(var);
                bufferBillboard.setImage(b);
                imageURL.setText(var);
            }
            else
            {
                imageURL.setText(null);
            }
        }
        bufferBillboard.setTitle(title.getText());
        bufferBillboard.setURL(imageURL.getText());
        bufferBillboard.setInformation(new Information(information.getText()));

    }
}
