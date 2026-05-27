package Inventory;

public class Potions extends Item {
    public Potions(String name, String type, int value) {
        super(name, type, value);
    }

    @Override
    public String toString() {
        return getName() + " (+" + getValue() + " " + getType() + ")";
    }
}