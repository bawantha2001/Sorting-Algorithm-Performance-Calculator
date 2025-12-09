import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import helpers.CSVHelper;

import java.awt.*;
import java.io.File;





public class SortingCalculatorOpenUi extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextArea resultArea;
    private double[] data;
    private JComboBox<String> algorithmBox;
    private JTextArea output;

   
   // ui jpanel all
    public SortingCalculatorOpenUi() {
        setTitle("Sorting Algorithm Performance Evaluation (CSV Based)");
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        output = new JTextArea();
        output.setEditable(false);

        JPanel topPanel = new JPanel(new FlowLayout());

        JButton importBtn = new JButton("Import CSV");
        topPanel.add(importBtn);

        algorithmBox = new JComboBox<>(new String[]{
                "Insertion Sort", "Shell Sort", "Merge Sort",
                "Quick Sort", "Heap Sort"
        });
        topPanel.add(algorithmBox);

        JButton sortBtn = new JButton("Sort & Evaluate");
        topPanel.add(sortBtn);

        add(topPanel, BorderLayout.NORTH);

         model = new DefaultTableModel(new String[]{"Original Data", "Sorted Data"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);


        resultArea = new JTextArea(6, 50);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Arial", Font.BOLD, 14));
        add(new JScrollPane(resultArea), BorderLayout.SOUTH);


         importBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();

                try {
                    data = CSVHelper.importCSV(file);
                    output.setText("CSV Imported Successfully!\nData Size: " + data.length + "\n");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "CSV Import Failed: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        setVisible(true);
    }


    // main method 
    public static void main(String[] args) {
        new SortingCalculatorOpenUi();
    }

   
}
