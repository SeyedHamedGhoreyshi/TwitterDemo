package Server;

public class Signup {
    private String userId = null;
    private String firstName = null;
    private String lastName = null;
    private String email = null;
    private String phoneNumber = null;
    private String password = null;
    private String rePassword = null;
    private String country = null;
    private String birthDate = null;

    public Signup(String userId, String firstName, String lastName, String email, String phoneNumber, String password, String rePassword, String country, String birthDate) {
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

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public String getCountry() {
        return country;
    }

    public String getBirthDate() {
        return birthDate;
    }
}
