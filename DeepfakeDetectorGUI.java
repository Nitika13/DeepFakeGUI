import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class DeepfakeDetectorGUI extends JFrame {
    private JLabel label;
    private JButton uploadButton;
    private JLabel resultLabel;
    private File selectedFile;

    public DeepfakeDetectorGUI() {
        setTitle("Deepfake Detector");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        uploadButton = new JButton("Upload Image");
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    label.setText("File: " + selectedFile.getName());
                    processImage(selectedFile);
                }
            }
        });

        label = new JLabel("No image selected");
        resultLabel = new JLabel("Result: Pending");

        add(uploadButton);
        add(label);
        add(resultLabel);

        setVisible(true);
    }

    private void processImage(File image) {
        try {
            // Run the Python script with the image path as an argument
            ProcessBuilder processBuilder = new ProcessBuilder("python", "deepfake_predict.py", image.getAbsolutePath());
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Read the output from the Python script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String result = reader.readLine();  // Read the prediction result
            process.waitFor();  // Wait for the process to complete

            // Update GUI with prediction result
            resultLabel.setText("Result: " + result);
        } catch (Exception ex) {
            ex.printStackTrace();
            resultLabel.setText("Error in prediction!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeepfakeDetectorGUI());
    }
}
