package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "type_product")
public class TypeProduct {
    @Id
    private String id;
    @Column
    private String name;
    @Column
    private String icon;
    @ManyToMany(mappedBy = "types", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private Set<Product> products = new HashSet<>();

    public TypeProduct() {
    }

    public TypeProduct(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public TypeProduct(String id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
