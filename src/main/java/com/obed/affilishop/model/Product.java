package com.obed.affilishop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;         // Nom du produit
    @Column(length = 1000)
    @Size(max = 250)
    private String description;  // Description courte (max 250)
    private double price;        // Prix
    private String imageUrl;     // URL de l'image
    private String affiliateLink; // Lien affilié
    private String source;       // Amazon, AliExpress, etc.

    // Constructeurs
    public Product() {}

    public Product(Long id, String name, String description, double price, String imageUrl, String affiliateLink, String source) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.affiliateLink = affiliateLink;
        this.source = source;
    }

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getAffiliateLink() { return affiliateLink; }
    public void setAffiliateLink(String affiliateLink) { this.affiliateLink = affiliateLink; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}
