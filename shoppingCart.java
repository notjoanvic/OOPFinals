package shophomepage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class shoppingCart extends JFrame implements ActionListener {

    private JPanel mainPanel, itemPanel, pricePanel, totalPanel;
    private JLabel totalLabel;
    public JTextArea totalArea, itemTextArea, priceTextArea;
    private JButton btnCheckOut, btnBackToHp, btnRemoveItem, btnTotal;

    public shoppingCart() {

        setTitle("SHOPPING CART");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        mainPanel.setLayout(new GridLayout(1, 2));

        JLabel titleLabel = new JLabel("SHOPPING CART");
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 25));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 25, 0, 0));
        add(titleLabel, BorderLayout.WEST);

        itemPanel = new JPanel();
        itemPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "ITEMS",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Times New Roman", Font.BOLD, 16)));
        itemPanel.setLayout(new GridLayout(0, 1));
        itemPanel.setBackground(Color.WHITE);
        itemTextArea = new JTextArea();
        Font itemFont = new Font("Calibri", Font.PLAIN, 16);
        itemTextArea.setFont(itemFont);
        itemTextArea.setEditable(false);
        JScrollPane itemScrollPane = new JScrollPane(itemTextArea);
        itemPanel.add(itemScrollPane, BorderLayout.CENTER);

        pricePanel = new JPanel();
        pricePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "PRICES",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Times New Roman", Font.BOLD, 16)));
        pricePanel.setLayout(new GridLayout(0, 1));
        pricePanel.setBackground(Color.WHITE);
        priceTextArea = new JTextArea();
        Font priceFont = new Font("Calibri", Font.PLAIN, 16);
        priceTextArea.setFont(priceFont);
        priceTextArea.setEditable(false);
        JScrollPane priceScrollPane = new JScrollPane(priceTextArea);
        pricePanel.add(priceScrollPane, BorderLayout.CENTER);

        mainPanel.add(itemPanel);
        mainPanel.add(pricePanel);

        Container pane = getContentPane();
        setContentPane(pane);
        pane.add(mainPanel);

        totalLabel = new JLabel("Total:");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalArea = new JTextArea();
        Font totalFont = new Font("Calibri", Font.PLAIN, 16);
        totalArea.setFont(totalFont);
        totalArea.setEditable(false);
        JScrollPane totalScrollPane = new JScrollPane(totalArea);

        totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "RECEIPT",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Times New Roman", Font.BOLD, 16)));
        totalPanel.setBackground(Color.WHITE);
        totalPanel.add(totalLabel, BorderLayout.SOUTH);
        totalPanel.add(totalScrollPane, BorderLayout.CENTER);
        mainPanel.add(totalPanel, BorderLayout.EAST);

        btnCheckOut = new JButton("CHECKOUT");
        btnCheckOut.setFont(new Font("Arial", Font.BOLD, 13));
        btnCheckOut.addActionListener(this);

        btnBackToHp = new JButton("BACK TO HOMEPAGE");
        btnBackToHp.setFont(new Font("Arial", Font.BOLD, 13));
        btnBackToHp.addActionListener(this);

        btnRemoveItem = new JButton("REMOVE ITEM");
        btnRemoveItem.setFont(new Font("Arial", Font.BOLD, 13));
        btnRemoveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedText = itemTextArea.getSelectedText();
                if (selectedText != null) {
                    removeFromCart(selectedText);
                }
            }
        });

        btnTotal = new JButton("Total");
        btnTotal.setFont(new Font("Arial", Font.BOLD, 13));
        btnTotal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateTotal();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        buttonPanel.add(btnBackToHp);
        buttonPanel.add(btnCheckOut);
        buttonPanel.add(btnRemoveItem);
        buttonPanel.add(btnTotal);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);

        loadCartItems();

    }

    // ... (the rest of the class remains unchanged)


    private void loadCartItems() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("cart.txt"));
            String line;
            StringBuilder itemsBuilder = new StringBuilder();
            StringBuilder pricesBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                String[] itemData = line.split(":");
                if (itemData.length == 2) {
                    itemsBuilder.append(itemData[0]).append("\n");
                    pricesBuilder.append(itemData[1]).append("\n");
                }
            }
            itemTextArea.setText(itemsBuilder.toString());
            priceTextArea.setText(pricesBuilder.toString());
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
 // Update the removeFromCart method to remove selected item from the cart
    private void removeFromCart(String item) {
        String currentItems = itemTextArea.getText();
        String currentPrices = priceTextArea.getText();

        String[] items = currentItems.split("\n");
        String[] prices = currentPrices.split("\n");

        StringBuilder updatedItems = new StringBuilder();
        StringBuilder updatedPrices = new StringBuilder();

        for (int i = 0; i < items.length; i++) {
            if (!items[i].equals(item)) {
                updatedItems.append(items[i]).append("\n");
                updatedPrices.append(prices[i]).append("\n");
            }
        }

        itemTextArea.setText(updatedItems.toString());
        priceTextArea.setText(updatedPrices.toString());

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("cart.txt"));
            for (int i = 0; i < updatedItems.length(); i++) {
                // Using "tab" as a delimiter between item and price when writing to the file
                writer.write(updatedItems.toString().split("\n")[i] + ":" + updatedPrices.toString().split("\n")[i]);
                writer.newLine();
            }
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Update the actionPerformed method for btnRemoveItem
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCheckOut) {
            dispose();
            // new CheckOut();
        } else if (e.getSource() == btnBackToHp) {
            dispose();
            new Homepage().setVisible(true); // Show the Homepage window
        } else if (e.getSource() == btnRemoveItem) {
            String selectedText = itemTextArea.getSelectedText();
            if (selectedText != null) {
                removeFromCart(selectedText);
            }
        } else if (e.getSource() == btnTotal) {
            calculateTotal();
        }
    }
    
    private void calculateTotal() {
        String[] items = itemTextArea.getText().split("\n");
        String[] prices = priceTextArea.getText().split("\n");

        StringBuilder receiptBuilder = new StringBuilder();
        double total = 0.0;

        for (int i = 0; i < items.length; i++) {
            receiptBuilder.append(items[i]).append("\t").append(prices[i]).append("\n");
            try {
                double itemPrice = Double.parseDouble(prices[i]);
                total += itemPrice;
            } catch (NumberFormatException ex) {
                // Ignore any non-numeric price values
            }
        }

        totalArea.setText(receiptBuilder.toString());
        totalLabel.setText("Total: P" + String.format("%.2f", total));
    }
    
}