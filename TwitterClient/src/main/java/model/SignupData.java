package model;

public class SignupData {
    private String userId = null;
    private String firstName = null;
    private String lastName = null;
    private String email = null;
    private String phoneNumber = null;
    private String password = null;
    private String rePassword = null;
    private String country = null;
    private String birthDate = null;

    public SignupData( String userId, String firstName, String lastName, String email, String phoneNumber, String password, String rePassword, String country, String birthDate) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.rePassword = rePassword;
        this.country = country;
        this.birthDate = birthDate;
    }
}
