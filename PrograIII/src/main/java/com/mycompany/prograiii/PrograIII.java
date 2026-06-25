/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.prograiii;

import Servidor.ServerForm;
import Vistas.LoginForm;
import javax.swing.SwingUtilities;

/**
 *
 * @author catac
 */
public class PrograIII {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {new LoginForm().setVisible(true);});
    }
}
