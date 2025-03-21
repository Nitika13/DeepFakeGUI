import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class DeepfakeDetectorGUI extends JFrame {
    private JLabel titleLabel, subtitleLabel, fileLabel, resultLabel, footerLabel;
    private JButton uploadButton, detectButton;
    private JTextField searchField;
    private File selectedFile;
    private JProgressBar progressBar;

    public DeepfakeDetectorGUI() {
        setTitle("Sudarshan - Deepfake Detector");
        setSize(700, 550); // Slightly larger window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(18, 18, 18)); // Dark background

        // Top Panel (Title & Search Bar)
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(18, 18, 18)); // Dark background
        topPanel.setBorder(new EmptyBorder(20, 0, 20, 0)); // Add padding

        titleLabel = new JLabel("SUDARSHAN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40)); // Larger font size
        titleLabel.setForeground(new Color(0, 255, 255)); // Cyan color
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        subtitleLabel = new JLabel("Deepfake Detector", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 20)); // Larger font size
        subtitleLabel.setForeground(new Color(200, 200, 200)); // Light gray
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        searchField = new JTextField(20);
        searchField.setMaximumSize(new Dimension(300, 35)); // Larger search bar
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchField.setBackground(new Color(30, 30, 30)); // Dark background
        searchField.setForeground(Color.WHITE); // White text
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 255, 255), 1), // Cyan border
                BorderFactory.createEmptyBorder(5, 10, 5, 10))); // Padding

        topPanel.add(titleLabel);
        topPanel.add(subtitleLabel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add spacing
        topPanel.add(searchField);

        // Center Panel (Main Content)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(new Color(18, 18, 18)); // Dark background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Add spacing
        gbc.gridx = 0;
        gbc.gridy = 0;

        uploadButton = new JButton("📁 Upload Image");
        styleButton(uploadButton, new Color(0, 150, 255)); // Blue button
        uploadButton.addActionListener(this::selectFile);
        centerPanel.add(uploadButton, gbc);

        gbc.gridy++;
        fileLabel = new JLabel("No file selected", SwingConstants.CENTER);
        fileLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        fileLabel.setForeground(Color.WHITE);
        centerPanel.add(fileLabel, gbc);

        gbc.gridy++;
        detectButton = new JButton("🚀 Detect Deepfake");
        styleButton(detectButton, new Color(255, 0, 100)); // Red button
        detectButton.setEnabled(false);
        detectButton.addActionListener(this::runPythonScript);
        centerPanel.add(detectButton, gbc);

        gbc.gridy++;
        resultLabel = new JLabel("Result: Waiting for image...", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        resultLabel.setForeground(new Color(0, 255, 0)); // Green text
        centerPanel.add(resultLabel, gbc);

        gbc.gridy++;
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        progressBar.setVisible(false);
        progressBar.setForeground(new Color(0, 255, 255)); // Cyan progress bar
        progressBar.setBackground(new Color(30, 30, 30)); // Dark background
        centerPanel.add(progressBar, gbc);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setBackground(new Color(18, 18, 18)); // Dark background
        footerPanel.setBorder(new EmptyBorder(10, 0, 10, 0)); // Add padding

        footerLabel = new JLabel("Visual Information and Signal Processing Team", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(new Color(150, 150, 150)); // Light gray
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel affiliationLabel = new JLabel("Affiliation - Thapar", SwingConstants.CENTER);
        affiliationLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        affiliationLabel.setForeground(new Color(150, 150, 150)); // Light gray
        affiliationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel copyrightLabel = new JLabel("To be Patented ©", SwingConstants.CENTER);
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        copyrightLabel.setForeground(new Color(150, 150, 150)); // Light gray
        copyrightLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        footerPanel.add(footerLabel);
        footerPanel.add(affiliationLabel);
        footerPanel.add(copyrightLabel);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 2), // Darker border
                BorderFactory.createEmptyBorder(10, 20, 10, 20))); // Padding
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
    }

    private void selectFile(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            fileLabel.setText("File: " + selectedFile.getName());
            detectButton.setEnabled(true);
        }
    }

    private void runPythonScript(ActionEvent e) {
        if (selectedFile == null) {
            resultLabel.setText("❌ Please upload an image first!");
            return;
        }

        progressBar.setVisible(true);
        detectButton.setEnabled(false);
        resultLabel.setText("🔄 Processing...");

        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() {
                try {
                    ProcessBuilder pb = new ProcessBuilder("python", "deepfake_predict.py", selectedFile.getAbsolutePath());
                    pb.redirectErrorStream(true);
                    Process process = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String result = reader.readLine();
                    process.waitFor();
                    return result;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return "❌ Error in prediction!";
                }
            }

            @Override
            protected void done() {
                try {
                    progressBar.setVisible(false);
                    detectButton.setEnabled(true);
                    resultLabel.setText("✅ Result: " + get());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    resultLabel.setText("❌ Error displaying result!");
                }
            }
        };
        worker.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeepfakeDetectorGUI().setVisible(true));
    }
}