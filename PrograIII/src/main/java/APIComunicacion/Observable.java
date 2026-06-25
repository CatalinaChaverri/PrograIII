/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package APIComunicacion;

import Modelos.Message;

/**
 *
 * @author catac
 */
public interface Observable {
    void addObserver(Observer obs);

    void removeObserver(Observer obs);

    void notifyObservers(Message msg);
}
