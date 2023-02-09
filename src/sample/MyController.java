package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MyController {
    @FXML
    private Spinner<Integer> age;
    @FXML
    private Spinner<Integer> weight;
    @FXML
    private Spinner<Integer> heightFeet;
    @FXML
    private Spinner<Integer> heightInches;
    @FXML
    private RadioButton genderChoiceFemale;
    @FXML
    private RadioButton genderChoiceMale;
    @FXML
    private ChoiceBox<String> activityLevel;
    @FXML
    private Button calculateButton;
    @FXML
    private Label hcpLabel;
    @FXML
    private Label hccLabel;
    @FXML
    private Label hcfLabel;
    @FXML
    private Label lcpLabel;
    @FXML
    private Label lccLabel;
    @FXML
    private Label lcfLabel;
    @FXML
    private Label lowCalorieLabel;
    @FXML
    private Label highCalorieLabel;

    private double tdee;

    @FXML
    public void initialize() {
        calculateButton.setDisable(true);
    }

    /** Calculates Macros for User */
    @FXML
    private void onButtonClicked() throws NumberFormatException {
        if (!(age.getValue() == null || heightFeet.getValue() == null ||
                heightInches.getValue() == null && activityLevel.getValue() == null)) {
            try {
                int weightInPounds = this.weight.getValue();
                int ageInYears = this.age.getValue();
                int heightInInches = ((12 * this.heightFeet.getValue()) +
                        this.heightInches.getValue());
                String a = activityLevel.getValue();
                char activityLevelChoice = a.charAt(0);

                // BMR is "Basal Metabolic Rate" or the amount of energy expended while at rest
                //BMR is estimated using age, height, weight, and gender
                double bmr;

                if (genderChoiceFemale.selectedProperty().getValue().equals(true)) {
                    bmr = calculateBmrFemale(ageInYears, heightInInches, weightInPounds);
                } else {
                    bmr = calculateBmrMale(ageInYears, heightInInches, weightInPounds);
                }

                // TDEE calculations are based off of the Harris-Benedict equation for Total Daily Energy Expenditer (tdee)
                //Sedentary: x1.2
                //Lightly Active: x1.375
                //Moderately Active: x1.55
                //Very Active: x1.725

                switch (activityLevelChoice) {
                    case '1':
                        this.tdee = bmr * 1.2;
                        break;
                    case '2':
                        this.tdee = bmr * 1.375;
                        break;
                    case '3':
                        this.tdee = bmr * 1.55;
                        break;
                    case '4':
                        this.tdee = bmr * 1.725;
                        break;
                    default:
                        break;
                }

                double lowTdee = tdee * 0.75;
                double highTdee = tdee * 0.9;
                int proteinCals = weightInPounds * 4;
                double lowCarbCals = lowTdee * 0.2;
                int lowCarbGrams = (int) (lowCarbCals / 4);
                double highCarbCals = highTdee * 0.5;
                int highCarbGrams = (int) (highCarbCals / 4);
                double lowFatCals = (lowTdee - proteinCals - lowCarbCals);
                int lowFatGrams = (int) (lowFatCals / 9);
                double highFatCals = (highTdee - proteinCals - highCarbCals);
                int highFatGrams = (int) (highFatCals / 9);

                hcpLabel.setText(Integer.toString(weightInPounds));
                lcpLabel.setText(Integer.toString(weightInPounds));
                hccLabel.setText(Integer.toString(highCarbGrams));
                lccLabel.setText(Integer.toString(lowCarbGrams));
                hcfLabel.setText(Integer.toString(highFatGrams));
                lcfLabel.setText(Integer.toString(lowFatGrams));
                lowCalorieLabel.setText(Integer.toString((int) lowTdee));
                highCalorieLabel.setText(Integer.toString((int) highTdee));

            } catch (NumberFormatException e) {
                System.out.println("Exception: " + e);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Invalid values entered. Please enter credentials using numbers only");
                alert.showAndWait();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("All fields must be filled out");
            alert.showAndWait();
        }


    }

    /** Unlocks calculate button for user */
    @FXML
    private void onKeyPressed() {
        calculateButton.setDisable(false);
    }

    /** Calculates Basal Metabolic Rate for Males
     * @param ageInYears - age of user
     * @param heightInInches - height of user
     * @param weightInPounds - weight of user
     * */
    private double calculateBmrMale(int ageInYears, int heightInInches, int weightInPounds) {
        return ((88.362 + (13.397 * (weightInPounds * 0.453592)) + (4.799 *
                (heightInInches * 2.54)) - (5.677 * ageInYears)));
    }

    /** Calculates Basal Metabolic Rate for Females
     * @param ageInYears - age of user
     * @param heightInInches - height of user
     * @param weightInPounds - weight of user
     * */
    private double calculateBmrFemale(int ageInYears, int heightInInches, int weightInPounds) {
        return ((447.593 + (9.247 * ((weightInPounds * 0.453592))) + (3.098 *
                ((heightInInches * 2.54))) - (4.330 * ((double)ageInYears))));
    }

}
