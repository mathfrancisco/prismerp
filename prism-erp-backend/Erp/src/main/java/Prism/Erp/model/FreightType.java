public enum FreightType {
    CIF("Cost, Insurance and Freight"),
    FOB("Free On Board"),
    EXW("Ex Works"),
    DDP("Delivered Duty Paid"),
    CPT("Carriage Paid To");
    
    private final String description;
    
    FreightType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}