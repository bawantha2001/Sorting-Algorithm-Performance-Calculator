package helpers;

import java.io.*;
import java.util.*;

public class CSVHelper {

    public static double[] importCSV(File file) throws IOException {
        List<Double> tempList = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        boolean hasHeader = true;

        while ((line = br.readLine()) != null) {

            if (hasHeader) {
                hasHeader = false;
                continue;
            }

            if (line.trim().isEmpty()) continue;

            String[] parts = line.split(",");

            if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                double value = Double.parseDouble(parts[1].trim());
                tempList.add(value);
            }
        }
        br.close();

        double[] data = new double[tempList.size()];
        for (int i = 0; i < tempList.size(); i++) {
            data[i] = tempList.get(i);
        }

        return data;
    }
}
