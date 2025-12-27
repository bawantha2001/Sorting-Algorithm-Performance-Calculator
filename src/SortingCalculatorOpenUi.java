import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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


    public SortingCalculatorOpenUi() {

        // -------------------------
        // Frame
        // -------------------------
        setTitle("Sorting Algorithm Performance Evaluation");
        setSize(1000, 560);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // -------------------------
        // Fonts
        // -------------------------
        Font baseFont = new Font("Segoe UI", Font.PLAIN, 13);
        Font boldFont = new Font("Segoe UI", Font.BOLD, 13);

        UIManager.put("Label.font", baseFont);
        UIManager.put("Button.font", baseFont);
        UIManager.put("ComboBox.font", baseFont);
        UIManager.put("Table.font", baseFont);

        // -------------------------
        // Main Container
        // -------------------------
        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        // -------------------------
        // Control Panel
        // -------------------------
        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBackground(new Color(245, 247, 250));
        controlPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 224, 230)),
                        BorderFactory.createEmptyBorder(8, 8, 8, 8)
                )
        );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton importBtn = new JButton("Import CSV");
        JButton sortBtn = new JButton("Sort & Evaluate");

        importBtn.setFocusPainted(false);
        sortBtn.setFocusPainted(false);

        sortBtn.setBackground(new Color(66, 133, 244));
        sortBtn.setForeground(Color.black);

        columnBox = new JComboBox<>();
        orderBox = new JComboBox<>(new String[]{
                "Insertion Sort",
                "Shell Sort",
                "Merge Sort",
                "Quick Sort",
                "Heap Sort"
        });

        gbc.gridx = 0; gbc.gridy = 0;
        controlPanel.add(importBtn, gbc);

        gbc.gridx = 1;
        controlPanel.add(new JLabel("Column"), gbc);

        gbc.gridx = 2;
        controlPanel.add(columnBox, gbc);

        gbc.gridx = 3;
        controlPanel.add(new JLabel("Algorithm"), gbc);

        gbc.gridx = 4;
        controlPanel.add(orderBox, gbc);

        gbc.gridx = 5;
        controlPanel.add(sortBtn, gbc);

        mainPanel.add(controlPanel, BorderLayout.NORTH);

        // -------------------------
        // Table
        // -------------------------
        model = new DefaultTableModel(new String[]{"Original Data", "Sorted Data"}, 0);
        table = new JTable(model);

        table.setRowHeight(26);
        table.setGridColor(new Color(225, 225, 225));
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setSelectionForeground(Color.BLACK);

        table.getTableHeader().setFont(boldFont);
        table.getTableHeader().setBackground(new Color(245, 247, 250));
        table.getTableHeader().setForeground(Color.DARK_GRAY);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(
                BorderFactory.createLineBorder(new Color(220, 224, 230))
        );

        mainPanel.add(tableScroll, BorderLayout.CENTER);

        // -------------------------
        // Result Panel
        // -------------------------
        resultArea = new JTextArea(6, 50);
        resultArea.setEditable(false);
        resultArea.setFont(boldFont);
        resultArea.setBackground(new Color(250, 250, 250));
        resultArea.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 224, 230)),
                        BorderFactory.createEmptyBorder(8, 8, 8, 8)
                )
        );

        JScrollPane resultScroll = new JScrollPane(resultArea);
        resultScroll.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(220, 224, 230)),
                        "Performance Summary",
                        0, 0, boldFont, Color.DARK_GRAY
                )
        );

        mainPanel.add(resultScroll, BorderLayout.SOUTH);

        // -------------------------
        // Status Bar
        // -------------------------
        JLabel statusLabel = new JLabel(" Ready");
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(245, 247, 250));
        statusLabel.setForeground(Color.DARK_GRAY);
        statusLabel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 224, 230)),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                )
        );

        add(statusLabel, BorderLayout.PAGE_END);

        // -------------------------
        // Actions
        // -------------------------
        importBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = chooser.getSelectedFile();
                    columnData = CSVHelper.importCSVWithColumns(file);

                    columnBox.removeAllItems();
                    model.setRowCount(0);

                    for (String col : columnData.keySet()) {
                        columnBox.addItem(col);
                    }

                    data = columnData.get(columnBox.getItemAt(0));
                    for (double d : data) {
                        model.addRow(new Object[]{d, ""});
                    }

                    resultArea.setText(
                            "CSV loaded successfully.\nColumns detected: " + columnData.keySet()
                    );
                    statusLabel.setText(" CSV imported");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Import failed: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        columnBox.addActionListener(e -> {
            if (columnData == null) return;

            data = columnData.get(columnBox.getSelectedItem().toString());
            model.setRowCount(0);

            for (double d : data) {
                model.addRow(new Object[]{d, ""});
            }
        });

        sortBtn.addActionListener(e -> {
            statusLabel.setText(" Executing " + orderBox.getSelectedItem());
            sortAndEvaluate();
        });

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
