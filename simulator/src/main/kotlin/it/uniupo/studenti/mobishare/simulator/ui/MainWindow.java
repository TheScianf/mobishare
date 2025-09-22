package it.uniupo.studenti.mobishare.simulator.ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import it.uniupo.studenti.mobishare.backend_core.entity.Vehicle;
import it.uniupo.studenti.mobishare.simulator.SimulatorContext;
import it.uniupo.studenti.mobishare.simulator.entity.Dock;
import it.uniupo.studenti.mobishare.simulator.ui.components.SensorPanel;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class MainWindow extends JFrame {
    private JPanel mainPanel;
    private JPanel vehiclePanel;
    private JPanel dockPanel;
    private JComboBox<Vehicle> vehiclesComboBox;
    private JComboBox<Dock> docksComboBox;
    private JPanel sensorsPanel;
    private JComboBox<Dock> chooseDockComboBox;
    private JButton connectButton;
    private JPanel connectionPanel;
    private JPanel showConnectionPanel;
    private JLabel connectedLabel;
    private JPanel ledPanel;
    private JLabel lockLabel;
    private JButton aggiorrnaButton;

    public MainWindow() {
        $$$setupUI$$$();
        setTitle("MobiShare Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        setResizable(false);
        setLocationRelativeTo(null); // Center the window
        manualSetup();
        pack();
    }

    private void manualSetup() {
        vehiclesComboBox.setRenderer(new VehicleCellRenderer());
        vehiclesComboBox.setModel(new DefaultComboBoxModel<>(SimulatorContext.vehicles.toArray(new Vehicle[0])));

        docksComboBox.setRenderer(new DockCellRenderer());
        docksComboBox.setModel(new DefaultComboBoxModel<>(SimulatorContext.docks.toArray(new Dock[0])));

        vehiclesComboBox.addActionListener(e -> {
            setupVehicle((Vehicle) vehiclesComboBox.getSelectedItem());
        });

        //SETUP DOCK
        System.out.println("MANUAL SETUP");
        docksComboBox.addActionListener(e -> updateRight());
        aggiorrnaButton.addActionListener((e) -> updateRight());

        pack();
    }

    private void updateRight() {
        Dock dock = (Dock) docksComboBox.getSelectedItem();
        if (dock == null) {
            return;
        }

        var lockStatus = dock.getAttuators().get("Lock");
        var lightStatus = dock.getAttuators().get("Light");
        System.out.println(lightStatus);
        System.out.println(lockStatus);

        switch (lightStatus.get()) {
            case "green" -> ledPanel.setBackground(Color.GREEN);
            case "red" -> ledPanel.setBackground(Color.RED);
            case "off" -> ledPanel.setBackground(Color.GRAY);
        }
        lockLabel.setText(lockStatus.get());
    }

    private void setupVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            return;
        }

        sensorsPanel.removeAll();
        vehicle.getSensors().forEach(
                sensor -> {
                    SensorPanel sensorPanel = new SensorPanel(
                            sensor.getType(),
                            s -> {
                                try {
                                    System.out.println(vehicle.getId());
                                    SimulatorContext.mqttConnection.publish(
                                            "vehicle/" + vehicle.getId(),
                                            new MqttMessage(
                                                    String.format(
                                                            "{\n" +
                                                                    "  \"sensorId\": \"%d\",\n" +
                                                                    "  \"vehicleId\": \"%d\",\n" +
                                                                    "  \"value\": %s,\n" +
                                                                    "  \"time\": \"%s\"\n" +
                                                                    "}",
                                                            sensor.getId(),
                                                            vehicle.getId(),
                                                            s,
                                                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                                                    ).getBytes(StandardCharsets.UTF_8)
                                            )
                                    );
                                    System.out.println(
                                            String.format(
                                                    "{\n" +
                                                            "  \"sensorId\": \"%d\",\n" +
                                                            "  \"value\": %s,\n" +
                                                            "  \"time\": \"%s\"\n" +
                                                            "}",
                                                    sensor.getId(),
                                                    s,
                                                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                                            )
                                    );
                                } catch (MqttException e) {
                                    e.printStackTrace();
                                    System.exit(1);
                                }
                            }
                    );
                    sensorsPanel.add(sensorPanel);
                }
        );

        //setup connection to dock
        if (SimulatorContext.docks.stream().anyMatch(dock -> dock.getVehicle() == vehicle)) {
            showConnectionPanel.setVisible(true);
            connectionPanel.setVisible(false);
        } else {
            showConnectionPanel.setVisible(false);
            connectionPanel.setVisible(true);
        }

        //get list of the void docks
        if (connectionPanel.isShowing()) {
            chooseDockComboBox.setRenderer(new DockCellRenderer());
            chooseDockComboBox.setModel(new DefaultComboBoxModel<Dock>(SimulatorContext.docks.stream().filter(e -> e.getVehicle() == null).toList().toArray(new Dock[0])));
            Arrays.stream(chooseDockComboBox.getActionListeners()).forEach(e -> e.actionPerformed(null));

            connectButton.addActionListener(e -> {
                Dock dock = (Dock) chooseDockComboBox.getSelectedItem();
                if (dock == null) {
                    JOptionPane.showMessageDialog(this, "Seleziona una colonnina valida", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    SimulatorContext.mqttConnection.publish(
                            "parks/" + dock.getPark() + "/docks/" + dock.getNumber() + "/in",
                            new MqttMessage(
                                    String.format("{\"vehicleId\": \"%d\"}", vehicle.getId()).getBytes(StandardCharsets.UTF_8)
                            )
                    );
                    dock.setVehicle(vehicle);
                    System.out.println("Vehicle " + vehicle.getId() + " connected to dock " + dock.getId());
                } catch (MqttException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Errore durante la connessione alla colonnina", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            });
        } else {
            connectedLabel.setText(
                    SimulatorContext.docks.stream().filter(dock -> dock.getVehicle() == vehicle).findFirst()
                            .map(dock -> "park: " + dock.getPark() + " - dock n. " + dock.getNumber()).orElse("NONE")
            );
        }

        pack();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        vehiclePanel = new JPanel();
        vehiclePanel.setLayout(new GridLayoutManager(7, 4, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(vehiclePanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        vehiclePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        label1.setText("VEICOLO:");
        vehiclePanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        vehiclePanel.add(spacer1, new GridConstraints(6, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        vehiclesComboBox = new JComboBox();
        vehiclePanel.add(vehiclesComboBox, new GridConstraints(0, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setBackground(new Color(-16777216));
        vehiclePanel.add(panel1, new GridConstraints(1, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 2), null, new Dimension(-1, 2), 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Sensori:");
        vehiclePanel.add(label2, new GridConstraints(2, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        vehiclePanel.add(sensorsPanel, new GridConstraints(3, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        connectionPanel = new JPanel();
        connectionPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        vehiclePanel.add(connectionPanel, new GridConstraints(4, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Connessione:");
        connectionPanel.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chooseDockComboBox = new JComboBox();
        connectionPanel.add(chooseDockComboBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        connectButton = new JButton();
        connectButton.setText("Connetti");
        connectionPanel.add(connectButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        showConnectionPanel = new JPanel();
        showConnectionPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        vehiclePanel.add(showConnectionPanel, new GridConstraints(5, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Connessa a: ");
        showConnectionPanel.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        connectedLabel = new JLabel();
        connectedLabel.setText("Label");
        showConnectionPanel.add(connectedLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dockPanel = new JPanel();
        dockPanel.setLayout(new GridLayoutManager(6, 3, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(dockPanel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        dockPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label5 = new JLabel();
        label5.setText("COLONNINA:");
        dockPanel.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        dockPanel.add(spacer2, new GridConstraints(5, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        docksComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        docksComboBox.setModel(defaultComboBoxModel1);
        dockPanel.add(docksComboBox, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setBackground(new Color(-16777216));
        dockPanel.add(panel2, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 2), null, new Dimension(-1, 2), 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Led:");
        dockPanel.add(label6, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ledPanel = new JPanel();
        ledPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        ledPanel.setEnabled(true);
        dockPanel.add(ledPanel, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Ganascia:");
        dockPanel.add(label7, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lockLabel = new JLabel();
        lockLabel.setText("");
        dockPanel.add(lockLabel, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        aggiorrnaButton = new JButton();
        aggiorrnaButton.setText("Aggiorrna");
        dockPanel.add(aggiorrnaButton, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.setBackground(new Color(-16777216));
        mainPanel.add(panel3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(2, -1), null, new Dimension(2, -1), 0, false));
    }

    /** @noinspection ALL */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

    private void createUIComponents() {
        sensorsPanel = new JPanel();
        sensorsPanel.setLayout(new BoxLayout(sensorsPanel, BoxLayout.Y_AXIS));
    }
}
