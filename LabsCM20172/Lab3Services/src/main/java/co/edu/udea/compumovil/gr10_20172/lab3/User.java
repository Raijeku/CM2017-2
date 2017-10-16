package co.edu.udea.compumovil.gr10_20172.lab3;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Davquiroga on 15/09/2017.
 */

public class User {
    @SerializedName("Nombreusuario")
    String userName;
    @SerializedName("Nombres")
    String names;
    @SerializedName("Apellidos")
    String lastNames;
    @SerializedName("Sexo")
    String sex;
    @SerializedName("Nacimiento")
    String birthDate;
    @SerializedName("Telefono")
    double phone;
    @SerializedName("Direccion")
    String postalAddress;
    @SerializedName("Email")
    String email;
    @SerializedName("Contrasena")
    String password;
    @SerializedName("Ciudad")
    String city;
    @SerializedName("Imagen")
    String image;
    int id;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User(String names, String lastNames, String sex, String birthDate, double phone, String postalAddress, String email, String password, String city, String image) {
        this.names = names;
        this.lastNames = lastNames;
        this.sex = sex;
        this.birthDate = birthDate;
        this.phone = phone;
        this.postalAddress = postalAddress;
        this.email = email;
        this.password = password;
        this.city = city;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User() {
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getLastNames() {
        return lastNames;
    }

    public void setLastNames(String lastNames) {
        this.lastNames = lastNames;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public double getPhone() {
        return phone;
    }

    public void setPhone(double phone) {
        this.phone = phone;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
