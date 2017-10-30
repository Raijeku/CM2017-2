package co.edu.udea.compumovil.gr10_20172.lab4;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Davquiroga on 15/09/2017.
 */

public class Apartment {
    //List<Bitmap> photos;
    @SerializedName("Imagen")
    String image;
    int id;
    @SerializedName("Tipo")
    String propertyType;
    @SerializedName("Valor")
    double value;
    @SerializedName("Cuartos")
    int amountRooms;
    @SerializedName("Area")
    double area;
    @SerializedName("Descripcion")
    String description;
    @SerializedName("Ubicacion")
    String location;
    @SerializedName("Nombre")
    String name;
    @SerializedName("Descripcioncorta")
    String shortDescription;
    @SerializedName("Banos")
    int bathRooms;

    public Apartment(String image, int id, String propertyType, double value, int amountRooms, double area, String description, String location, String name, String shortDescription, int bathRooms) {
        this.image = image;
        this.id = id;
        this.propertyType = propertyType;
        this.value = value;
        this.amountRooms = amountRooms;
        this.area = area;
        this.description = description;
        this.location = location;
        this.name = name;
        this.shortDescription = shortDescription;
        this.bathRooms = bathRooms;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBathRooms() {
        return bathRooms;
    }

    public void setBathRooms(int bathRooms) {
        this.bathRooms = bathRooms;
    }

    public Apartment() {
    }

    /*public List<Bitmap> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Bitmap> photos) {
        this.photos = photos;
    }*/

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getAmountRooms() {
        return amountRooms;
    }

    public void setAmountRooms(int amountRooms) {
        this.amountRooms = amountRooms;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
}
