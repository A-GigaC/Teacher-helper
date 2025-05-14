package nsu.ru.output;

import nsu.ru.pipeline.ReportBuilder;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GraphicalReport {
    private final JFrame gui;
    private JLabel statusLabel;

    public GraphicalReport() {
        gui = new JFrame("Teacher helper");
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(1000, 900);
        gui.setLocationRelativeTo(null);

        JLabel info = new JLabel("Teacher helper started");
        gui.getContentPane().add(info);
        gui.setVisible(true);
        gui.setLocationRelativeTo(null);
        statusLabel = info;
    }

    public void setStatus(String status) {
        gui.getContentPane().remove(statusLabel);
        JLabel info = new JLabel(status);
        gui.getContentPane().add(info);

        statusLabel = info;
    }

    public void showReport(List<ReportBuilder.StudentResults> studentsResults) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (ReportBuilder.StudentResults studentResults : studentsResults) {
            mainPanel.add(createStudentPanel(studentResults));
            mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        gui.add(scrollPane);
        gui.setVisible(true);
    }

    private JPanel createStudentPanel(ReportBuilder.StudentResults studentResults) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(studentResults.studentLabel));

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(new JLabel("Total score: " + studentResults.totalScore));
        infoPanel.add(new JLabel("Course grade: " + studentResults.courseGrade));

        if (!studentResults.unknownFolders.isEmpty()) {
            JLabel foldersLabel = new JLabel("Unknown folders: " + studentResults.unknownFolders);
            infoPanel.add(foldersLabel);
        }

        panel.add(infoPanel, BorderLayout.NORTH);

        String[] columnNames = {"Task", "First commit", "Last commit", "Days", "Score", "Plagiarism", "Passed"};

        JTable table = new JTable(studentResults.data, columnNames);
        table.setAutoCreateRowSorter(true);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(950, 150));

        panel.add(tableScroll, BorderLayout.CENTER);

        return panel;
    }
}
