package client.controlpanel;

import client.Exception.CommunicationException;
import system.User;
import system.billboard.Billboard;

import java.awt.*;
import javax.swing.*;
import java.awt.GridBagLayout;

public class Settings extends ControlPanel
{
    // GUI Visual properties
    private final int PAD = 20;
    private final Color color = Color.WHITE;

    // JLists
    private JList<User> userJList;
    private JList<Billboard> billboardJList;

    // Custom JPanels
    private Home.ListSelectorPanel listSelectorPanel;
    private Home.PreviewPanel previewPanel;
    private Home.HomeButtonsPanel homeButtonsPanel = new Home.HomeButtonsPanel();

    GridBagConstraints gbc;

    String[] permissions = {"Permission0", "Permission1", "Permission2", "Permission3", "Permission4"};

    protected Settings() {

        try {
            // When this panel initiates, list of users is collected from the server and saved
            setUserList();
        } catch (CommunicationException e) {
            e.printStackTrace();
        }

        // Layout properties
        setLayout(new GridBagLayout());
        setBackground(color);

        JPanel controlContainer = new JPanel();
        controlContainer.setLayout(new GridBagLayout());

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(2, 1, PAD, PAD));
        gridPanel.setBackground(color);

        listSelectorPanel = new Home.ListSelectorPanel();
        gbc = getGbc(0, 0, 1, 1);
        gbc.fill = GridBagConstraints.BOTH;
        controlContainer.add(listSelectorPanel, gbc);

        previewPanel = new Home.PreviewPanel();
        gbc = getGbc(0, 1, 1, 20);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(PAD, PAD, PAD / 2, PAD);

        gridPanel.add(controlContainer);
        gridPanel.add(previewPanel);
        add(gridPanel, gbc);

        // Parent button panel
        JPanel buttonContainer = new JPanel();
        gbc = getGbc(0, 2, 1, 0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(PAD / 2, PAD, PAD, PAD);
        buttonContainer.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonContainer.add(homeButtonsPanel);  // <-- Start off by showing the home buttons
        add(buttonContainer, gbc);

    }
    private GridBagConstraints getGbc(int x, int y, int wx, int wy) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = wx;
        gbc.weighty = wy;
        return gbc;
    }



}