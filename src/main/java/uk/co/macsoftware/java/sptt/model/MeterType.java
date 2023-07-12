package uk.co.macsoftware.java.sptt.model;

public enum MeterType {
    GAS("GAS"),
    ELEC("ELEC");
    private final String value;
    MeterType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
