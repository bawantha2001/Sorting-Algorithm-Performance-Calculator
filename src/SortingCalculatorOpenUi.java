import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import helpers.CSVHelper;
import sorting_algorithms.Algorithms;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.Map;


public class SortingCalculatorOpenUi extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextArea resultArea;
    private double[] data;
    private JComboBox<String> columnBox;
    private Map<String, double[]> columnData;
    private JTextArea output;
    private JComboBox<String> orderBox;

   
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

        columnBox = new JComboBox<>();
        topPanel.add(new JLabel("Select Column:"));
        topPanel.add(columnBox);

        orderBox = new JComboBox<>(new String[]{
                "Insertion Sort",
                "Shell Sort",
                "Merge Sort",
                "Quick Sort",
                "Heap Sort"
        });
        topPanel.add(new JLabel("Algorithm:"));
        topPanel.add(orderBox);

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
                    columnData = CSVHelper.importCSVWithColumns(file);

                    columnBox.removeAllItems();
                    model.setRowCount(0);

                    // Populate column selector
                    for (String col : columnData.keySet()) {
                        columnBox.addItem(col);
                    }

                    // Display first column by default
                    String firstCol = columnBox.getItemAt(0);
                    data = columnData.get(firstCol);

                    for (double d : data) {
                        model.addRow(new Object[]{d, ""});
                    }

                    resultArea.setText(
                            "CSV Imported Successfully\nColumns Detected: " + columnData.keySet()
                    );

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "CSV Import Failed: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        columnBox.addActionListener(e -> {

            if (columnData == null) return;

            String selectedColumn = columnBox.getSelectedItem().toString();
            data = columnData.get(selectedColumn);

            model.setRowCount(0);
            for (double d : data) {
                model.addRow(new Object[]{d, ""});
            }
        });


        sortBtn.addActionListener(e -> sortAndEvaluate());

        setVisible(true);
    }

    private void sortAndEvaluate() {

        if (data == null || data.length == 0) {
            JOptionPane.showMessageDialog(this, "Please import a CSV first!");
            return;
        }

        final JOptionPane optionPane = new JOptionPane(
                "Sorting in progress...\nPlease wait",
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                null,
                new Object[]{},   // no buttons
                null
        );

        final JDialog dialog = optionPane.createDialog(this, "Sorting");
        dialog.setModal(false);
        dialog.setVisible(true);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {

            double[] sortedCopy;
            long time;
            String selected;

            @Override
            protected Void doInBackground() {

                sortedCopy = Arrays.copyOf(data, data.length);
                selected = orderBox.getSelectedItem().toString();

                long start = System.nanoTime();

                switch (selected) {
                    case "Insertion Sort" -> Algorithms.insertionSort(sortedCopy);
                    case "Shell Sort" -> Algorithms.shellSort(sortedCopy);
                    case "Merge Sort" -> Algorithms.mergeSort(sortedCopy, 0, sortedCopy.length - 1);
                    case "Quick Sort" -> Algorithms.quickSort(sortedCopy, 0, sortedCopy.length - 1);
                    case "Heap Sort" -> Algorithms.heapSort(sortedCopy);
                }

                time = System.nanoTime() - start;
                return null;
            }

            @Override
            protected void done() {

                dialog.dispose();

                for (int i = 0; i < sortedCopy.length; i++) {
                    model.setValueAt(sortedCopy[i], i, 1);
                }

                resultArea.setText(
                        "Selected Algorithm: " + selected +
                                "\nExecution Time: " + time / 1_000_000.0 + " ms\n"
                );

                detectBestAlgorithm();

                JOptionPane.showMessageDialog(
                        SortingCalculatorOpenUi.this,
                        "Sorting completed successfully",
                        "Done",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        };

        worker.execute();
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
                "\nBest Performing Algorithm: " + bestAlgo + "\nBest Time: " + bestTime / 1_000_000.0 + " ms\n"
        );
    }
   
}
