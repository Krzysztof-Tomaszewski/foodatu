package pl.company.foodatu.plans.domain;

class PlannedMeal{

    private String name;
    private Double carbons;
    private Double proteins;
    private Double fat;

    public PlannedMeal(String name) {
        this.name = name;
    }

    public PlannedMeal() {
    }

    public PlannedMeal(String name, Double carbons, Double proteins, Double fat) {
        this.name = name;
        this.carbons = carbons;
        this.proteins = proteins;
        this.fat = fat;
    }

    public String getName() {
        return name;
    }

    public Double calculateKCal() {
        return (4 * proteins) + (4 * carbons) + (9 * fat);
    }
}
