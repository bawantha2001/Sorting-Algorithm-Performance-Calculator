import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;


public class SortingCalculatorOpenUi extends JFrame {
   
   // ui jpanel
    public SortingCalculatorOpenUi() {

        setTitle("Sorting Algorithm Performance Evaluation (CSV Based)");
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());

        JButton importBtn = new JButton("Import CSV");
        topPanel.add(importBtn);

        JButton sortBtn = new JButton("Sort & Evaluate");
        topPanel.add(sortBtn);

        add(topPanel, BorderLayout.NORTH);

        setVisible(true);
    }


    // main method 
    public static void main(String[] args) {
        new SortingCalculatorOpenUi();
    }

   
}
