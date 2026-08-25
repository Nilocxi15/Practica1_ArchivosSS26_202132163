package DTO;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class ReportsDto {

    // Creción de carpeta para almacenar reportes
    public void createFolder() throws IOException {
        File folder = new File("data/reports");
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    // Apertura de carpeta que almacena reportes
    public void openFolder() {
        File folder = new File("data/reports");
        try {
            Desktop.getDesktop().open(folder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
