package aabdrrstvy.vehiclehub.datas;

public class Profile {
    String fullName, email, phone;

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
    public Profile() {
    }

    public Profile(String fullName, String email, String phone) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }
}
