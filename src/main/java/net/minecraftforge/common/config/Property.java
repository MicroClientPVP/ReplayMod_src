package net.minecraftforge.common.config;

public class Property {
    public String comment;
    private String value;

    public Property(String value) {
        this.value = value;
    }

    public String getString() {
        return value;
    }

    public boolean getBoolean() {
        return Boolean.parseBoolean(value);
    }

    public int getInt() {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    public double getDouble() {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public void set(String value) {
        this.value = value;
    }

    public void set(boolean value) {
        this.value = String.valueOf(value);
    }

    public void set(int value) {
        this.value = String.valueOf(value);
    }

    public void set(double value) {
        this.value = String.valueOf(value);
    }

    public Property setValidValues(String[] values) {
        return this;
    }
}