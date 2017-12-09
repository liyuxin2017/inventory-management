package com.neuSep17.main;

import com.neuSep17.dto.Dealer;
import com.neuSep17.service.DealerImpleService;
import com.neuSep17.ui.IncentiveUI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;

public class ConsumerApplication extends IncentiveUI {

    private JFrame mainFrame;
    private JLabel headerLabel;
    private JLabel distanceLabel;
    private JPanel controlPanel;
    private JComboBox<Dealer> chooseDealer;

    public ConsumerApplication() {
        super();
    }

    public static void main(String[] args) throws IOException {
        ConsumerApplication consumerApplication = new ConsumerApplication();
        consumerApplication.createUI();
    }

    public void createUI() throws IOException {

        prepareGUI();

        JPanel distanceNestedPanel = new JPanel();
        JLabel distanceTextLabel = createLabel("Show dealers within distance (mi):", JLabel.CENTER);
        JSlider slider = getSlider();
        distanceNestedPanel.add(distanceTextLabel);
        distanceNestedPanel.add(slider);
        distanceNestedPanel.add(distanceLabel);

        JPanel dealersListNestedPanel = new JPanel();
        JLabel label = createLabel("Select Dealer:", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 40));
        chooseDealer = getDealersComboBox();
        dealersListNestedPanel.add(label);
        dealersListNestedPanel.add(chooseDealer);

        JPanel submitNestedPanel = new JPanel();
        JButton submitButton = getSubmitButton();
        submitNestedPanel.add(submitButton);

        JPanel outer = new JPanel(new GridLayout(5,1));
        outer.add(distanceNestedPanel);
        outer.add(dealersListNestedPanel);
        outer.add(submitNestedPanel);

        controlPanel.add(outer);
        mainFrame.setVisible(true);
    }

    private void prepareGUI() throws IOException {
        mainFrame = createFrame("Welcome User");
        mainFrame.setLayout(new GridLayout(3, 2));

        JPanel imageNestedPanel = new JPanel();
        imageNestedPanel.setLayout(new GridLayout(2,1));

        JLabel imageLabel = pictures();

        headerLabel = createLabel("Welcome to your dream car site!", JLabel.CENTER);
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        headerLabel.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 100));

        imageNestedPanel.add(imageLabel);
        imageNestedPanel.add(headerLabel);

        distanceLabel = createLabel("", JLabel.CENTER);

        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        mainFrame.add(imageNestedPanel);
        mainFrame.add(controlPanel);
        mainFrame.setVisible(true);
    }

    private JButton getSubmitButton() {

        JButton submitButton = createButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Dealer selectedDealer = (Dealer) chooseDealer.getSelectedItem();
                String dealerId = selectedDealer.getId();
                System.out.println("Selected dealer id: "+dealerId+","+selectedDealer.getName()+","+selectedDealer.getEmailId());
                // TODO: Call Browse inventory screen by passing dealerId
            }
        });

        return submitButton;
    }

    private JComboBox<Dealer> getDealersComboBox(){
        ArrayList<Dealer> dealers = new ArrayList<>();
        try {
            DealerImpleService dealerImpleService = new DealerImpleService();
            dealers = dealerImpleService.getDealers();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JComboBox<Dealer> comboBox = new JComboBox(dealers.toArray());
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if(value instanceof Dealer){
                    Dealer dealer = (Dealer) value;
                    setText(dealer.getName());
                }
                return this;
            }
        } );
        return comboBox;
    }

    private JSlider getSlider() {
        // Set the slider
        JSlider slider = new JSlider();
        slider.setMinorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setFont(new Font("Arial", Font.PLAIN, 36));

        // Set the labels to be painted on the slider
        slider.setPaintLabels(true);

        // Add positions label in the slider
        Hashtable<Integer, JLabel> position = new Hashtable<Integer, JLabel>();
        position.put(0, new JLabel("0"));
        position.put(50, new JLabel("50"));
        position.put(100, new JLabel("100"));

        // Set the label to be drawn
        slider.setLabelTable(position);

        distanceLabel.setText("(selected: " + slider.getValue() +")");

        // Add change listener to the slider
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                distanceLabel.setText("(selected: " + ((JSlider)e.getSource()).getValue() +")");
            }
        });

        return slider;
    }

    private JLabel pictures() throws IOException{
        JLabel picLabel = createPicture("ConsumerHeaderImage.png");
        return picLabel;
    }
}