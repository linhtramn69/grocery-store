package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
public class Product {
    private static final DecimalFormat df = new DecimalFormat("#.##");
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Lob
    @Column(length = 100000)
    private String description;

    @Column
    private Double price;

    @Column
    private String image;

    @Column
    private Integer quantity;

    @Column
    private boolean status;

    @ManyToMany
    @JoinTable(name = "product_type_product",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id"))
    private Set<TypeProduct> types = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private Set<CartProduct> cartProducts;

    public Product() {
    }

    public Product(Long id, String name, String description, Double price, String image, Set<TypeProduct> types) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.types = types;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = Double.valueOf(df.format(price));
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Set<TypeProduct> getTypes() {
        return types;
    }

    public void setTypes(Set<TypeProduct> types) {
        this.types = types;
    }

    public Set<CartProduct> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(Set<CartProduct> cartProducts) {
        this.cartProducts = cartProducts;
    }

}
