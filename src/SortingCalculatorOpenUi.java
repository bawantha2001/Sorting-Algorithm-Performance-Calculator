import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import helpers.CSVHelper;
import sorting_algorithms.Algorithms;

import java.awt.*;
import java.io.File;
import java.util.Arrays;





public class SortingCalculatorOpenUi extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextArea resultArea;
    private double[] data;
    private JComboBox<String> algorithmBox;
    private JTextArea output;

   
   // ui jpanel all
    public SortingCalculatorOpenUi() {
        setTitle("Sorting Algorithm Performance Evaluation");
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
                    for (double datum : data) {
                        model.addRow(new Object[]{datum, ""});
                    }
                    resultArea.setText("CSV Imported Successfully!\nTotal Records: " + data.length);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "CSV Import Failed: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        JOptionPane.showMessageDialog(this, "CSV Import Failed: ", "Error", JOptionPane.ERROR_MESSAGE);
       
        sortBtn.addActionListener(e -> sortAndEvaluate());

        setVisible(true);
    }

        private void sortAndEvaluate() {

        if (data == null || data.length == 0) {
            JOptionPane.showMessageDialog(this, "Please import a CSV first!");
            return;
        }

        double[] copy = Arrays.copyOf(data, data.length);
        String selected = algorithmBox.getSelectedItem().toString();

        long start = System.nanoTime();

        switch (selected) {
             case "Insertion Sort" -> Algorithms.insertionSort(copy);
             case "Shell Sort" -> Algorithms.shellSort(copy);
            case "Merge Sort" -> Algorithms.mergeSort(copy, 0, copy.length - 1);
            case "Quick Sort" -> Algorithms.quickSort(copy, 0, copy.length - 1);
            case "Heap Sort" -> Algorithms.heapSort(copy);
        }

        long time = System.nanoTime() - start;

        for (int i = 0; i < copy.length; i++) {
            model.setValueAt(copy[i], i, 1);
        }

        resultArea.setText(
                "Selected Algorithm: " + selected +
                        "\nExecution Time: " + time / 1_000_000.0 + " ms\n"
        );

        detectBestAlgorithm();
    }

    private void detectBestAlgorithm() {

        String[] algorithms = {
                "Insertion Sort", "Shell Sort",
                "Merge Sort", "Quick Sort", "Heap Sort"
        };

        long bestTime = Long.MAX_VALUE;
        String bestAlgo = "";

        for (String algo : algorithms) {

            double[] copy = Arrays.copyOf(data, data.length);
            long start = System.nanoTime();

            switch (algo) {
                case "Insertion Sort" -> Algorithms.insertionSort(copy);
                case "Shell Sort" -> Algorithms.shellSort(copy);
                case "Merge Sort" -> Algorithms.mergeSort(copy, 0, copy.length - 1);
                case "Quick Sort" -> Algorithms.quickSort(copy, 0, copy.length - 1);
                case "Heap Sort" -> Algorithms.heapSort(copy);
            }

            long time = System.nanoTime() - start;

            if (time < bestTime) {
                bestTime = time;
                bestAlgo = algo;
            }
        }

        resultArea.append(
                "\nBest Performing Algorithm: " + bestAlgo +
                        "\nBest Time: " + bestTime / 1_000_000.0 + " ms\n"
        );
    }


    // main method 
    public static void main(String[] args) {
        new SortingCalculatorOpenUi();
    }

   
}
