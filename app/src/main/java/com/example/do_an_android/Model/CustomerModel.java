package com.example.do_an_android.Model;

import java.io.Serializable;

public class CustomerModel implements Serializable {
   private String username,name,password,address,phone,image;
   private Integer status, idquyen;

    public CustomerModel(String username, String name, String password, String address, String phone, String image, Integer idquyen, Integer status) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.address = address;
        this.phone = phone;
        this.image = image;
        this.idquyen = idquyen;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getIdquyen() {
        return idquyen;
    }

    public void setIdquyen(Integer idquyen) {
        this.idquyen = idquyen;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
