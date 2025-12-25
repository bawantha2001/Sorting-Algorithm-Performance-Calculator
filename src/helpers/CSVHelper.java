package helpers;

import java.io.*;
import java.util.*;

public class CSVHelper {

    // Returns columnName → data[]
    public static Map<String, double[]> importCSVWithColumns(File file) throws IOException {

        Map<String, List<Double>> tempMap = new LinkedHashMap<>();

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        // 🔹 Read header
        String headerLine = br.readLine();
        if (headerLine == null) {
            throw new IOException("Empty CSV file");
        }

        String[] headers = headerLine.split(",");

        // Initialize lists for each column
        for (String header : headers) {
            tempMap.put(header.trim(), new ArrayList<>());
        }

        // 🔹 Read data rows
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            String[] values = line.split(",");

            for (int i = 0; i < values.length; i++) {
                String v = values[i].trim();
                if (!v.isEmpty()) {
                    tempMap.get(headers[i].trim()).add(Double.parseDouble(v));
                }
            }
        }
        br.close();

        // 🔹 Convert List → double[]
        Map<String, double[]> result = new LinkedHashMap<>();
        for (String key : tempMap.keySet()) {
            List<Double> list = tempMap.get(key);
            double[] arr = new double[list.size()];
            for (int i = 0; i < list.size(); i++) {
                arr[i] = list.get(i);
            }
            result.put(key, arr);
        }

        return result;
    }
}
