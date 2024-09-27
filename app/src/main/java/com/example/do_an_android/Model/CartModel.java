package com.example.do_an_android.Model;

import java.io.Serializable;

public class CartModel implements Serializable {
    private ProductModel productModel;
    private int quantity,quantityRemain;
    private String username, image;
    private boolean isChecked;

    public CartModel(String username, ProductModel productModel, int quantity, int quantityRemain, String image) {
        this.username       = username;
        this.productModel   = productModel;
        this.quantity       = quantity;
        this.quantityRemain = quantityRemain;
        this.image = image;
        this.isChecked = false;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ProductModel getProductModel() {
        return productModel;
    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantityRemain() {
        return productModel.getQuantity();
    }

    public void setQuantityRemain(int quantityRemain) {
        this.quantityRemain = quantityRemain;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}