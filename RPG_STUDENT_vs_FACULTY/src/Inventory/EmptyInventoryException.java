/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Inventory;

/**
 *
 * @author user
 */
public class EmptyInventoryException extends Exception {
    public EmptyInventoryException(String itemType) {
        super("No " + itemType + " potions remaining in inventory!");
    }
}