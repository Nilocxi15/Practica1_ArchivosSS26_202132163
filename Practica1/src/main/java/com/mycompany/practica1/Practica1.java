package com.mycompany.practica1;

import GUI.Login;
import com.formdev.flatlaf.FlatDarculaLaf;
import javax.swing.SwingUtilities;

public class Practica1 {

    public static void main(String[] args) {
        // Configuración de Look and Feel de FlatLaf
        FlatDarculaLaf.setup();

        // Creación de ventana de login con Look and Feel de FlatLaf
        SwingUtilities.invokeLater(() -> {
            Login login = new Login();

            login.setVisible(true);
        });

    }
}
