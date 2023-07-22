package controller;

import com.google.gson.Gson;
import com.twitter.twitterclient.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.MyClient;
import model.SignupData;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML
    private Button signUp;
    @FXML
    private TextField FirstNameF;
    @FXML
    private ChoiceBox<String> countryCB;
    @FXML
    private TextField LastName;

    @FXML
    private DatePicker birthDayField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField numberField;

    @FXML
    private PasswordField passWordField;

    @FXML
    private PasswordField rePassWordField;

    @FXML
    private TextField userNameField;
    @FXML
    private Label errorLabel;



    @FXML
    public void countryChoice(MouseEvent event) {

        ObservableList<String> stringObservableList = FXCollections.observableArrayList("Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Antigua and Barbuda", "Argentina", "Armenia",
                "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus",
                "Belgium", "Belize", "Benin", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil",
                "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cabo Verde", "Cambodia", "Cameroon", "Canada",
                "Central African Republic", "Chad", "Chile", "China", "Colombia", "Comoros", "Congo, Democratic Republic of the",
                "Congo, Republic of the", "Costa Rica", "Cote d'Ivoire", "Croatia", "Cuba", "Cyprus", "Czech Republic",
                "Denmark", "Djibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt", "El Salvador",
                "Equatorial Guinea", "Eritrea", "Estonia", "Eswatini", "Ethiopia", "Fiji", "Finland", "France", "Gabon",
                "Gambia", "Georgia", "Germany", "Ghana", "Greece", "Grenada", "Guatemala", "Guinea", "Guinea-Bissau",
                "Guyana", "Haiti", "Honduras", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland",
                "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kosovo", "Kuwait",
                "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania",
                "Luxembourg", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands",
                "Mauritania", "Mauritius", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro",
                "Morocco", "Mozambique", "Myanmar (Burma)", "Namibia", "Nauru", "Nepal", "Netherlands", "New Zealand",
                "Nicaragua", "Niger", "Nigeria", "North Korea", "North Macedonia", "Norway", "Oman", "Pakistan", "Palau",
                "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Qatar",
                "Romania", "Russia", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines",
                "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles",
                "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa",
                "South Korea", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Sweden", "Switzerland",
                "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Timor-Leste", "Togo", "Tonga",
                "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Tuvalu", "Uganda", "Ukraine",
                "United Arab Emirates", "United Kingdom (UK)", "United States of America (USA)", "Uruguay", "Uzbekistan",
                "Vanuatu", "Vatican City (Holy See)", "Venezuela", "Vietnam", "Yemen", "Zambia", "Zimbabwe");
        countryCB.setItems(stringObservableList);


    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signUp.setOnAction(actionEvent -> {
            try {
                if (emailField.getText().length() == 0 && numberField.getText().length() == 0) {
                    errorLabel.setText("you should enter email or number!");
                    errorLabel.setTextFill(Color.RED);

                } else if (userNameField.getText().length() == 0 || FirstNameF.getText().length() == 0 || LastName.getText().length() == 0 || passWordField.getText().length() == 0 || rePassWordField.getText().length() == 0 || countryCB.toString().length() == 0) {
                    errorLabel.setText("enter empty sections!");
                    errorLabel.setTextFill(Color.RED);
                } else {


                    String userName = userNameField.getText();
                    String firstName = FirstNameF.getText();
                    String lastName = LastName.getText();
                    String email = emailField.getText();
                    String phoneNumber = numberField.getText();
                    String passWord = passWordField.getText();
                    String rePassWord = rePassWordField.getText();
                    String country = countryCB.getValue().toString();
                    LocalDate date = birthDayField.getValue();
                    String dateText = date.toString();

                    SignupData signupData = new SignupData(userName, firstName, lastName, email, phoneNumber, passWord, rePassWord, country, dateText);
                    Gson gson = new Gson();
                    String json = gson.toJson(signupData);
                    MyClient myClient = new MyClient();
                    String response = myClient.sendRequest(json, "signup");
                    if (response.equals("Passwords are not the same")) {
                        errorLabel.setText("Passwords are not the same");
                        errorLabel.setTextFill(Color.RED);
                    } else if (response.equals("Email format is wrong")) {
                        errorLabel.setText("Email format is wrong");
                        errorLabel.setTextFill(Color.RED);
                    } else if (response.equals("userId has already reserved")) {
                        errorLabel.setText("userId has already reserved");
                        errorLabel.setTextFill(Color.RED);
                    } else if (response.equals("userEmail has already reserved")) {
                        errorLabel.setText("userEmail has already reserved");
                        errorLabel.setTextFill(Color.RED);
                    } else if (response.equals("userPhone has already reserved")) {
                        errorLabel.setText("userPhone has already reserved");
                        errorLabel.setTextFill(Color.RED);
                    } else if (response.equals("Ok your account created")) {
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LoginPage.fxml"));
                        Scene scene = new Scene(fxmlLoader.load());
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        currentStage.hide();
                        stage.show();

                    }
                }
            } catch (NullPointerException | IOException nullPointerException) {
                errorLabel.setText("You should enter your birth day");
                errorLabel.setTextFill(Color.RED);
            }
        });

    }
}
