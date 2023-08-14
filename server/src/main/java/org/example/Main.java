package org.example;

import org.example.model.TypeProduct;
import org.example.repository.ProductRepository;
import org.example.repository.TypeProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class Main implements CommandLineRunner {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TypeProductRepository typeProductRepository;

    @Override
    public void run(String... args) throws Exception {

        // create Type Product
        TypeProduct type1 = new TypeProduct("F&V", "Fruits & Vegetables", "icon_apple.png");
        TypeProduct FRUIT = new TypeProduct("FRUIT", "Fruits");
        TypeProduct VEGE = new TypeProduct("VEGE", "Vegetables");
        TypeProduct type2 = new TypeProduct("M&F", "Meat & Fish", "meat.png");
        TypeProduct MEAT = new TypeProduct("MEAT", "Meat");
        TypeProduct FISH = new TypeProduct("FISH", "Fish");
        TypeProduct SNACK = new TypeProduct("SNACK", "Snacks", "coffee-cup.png");
        TypeProduct type3 = new TypeProduct("N&B", "Nuts & Biscuits");
        TypeProduct NOODLE = new TypeProduct("NOODLE", "Noodles");
        TypeProduct DAIRY = new TypeProduct("DAIRY", "Dairy", "milk.png");
        TypeProduct MILK = new TypeProduct("MILK", "Milk");
        TypeProduct BUTTER = new TypeProduct("BUTTER", "Butter");
        TypeProduct YOGURT = new TypeProduct("YOGURT", "Yogurt");
        TypeProduct COOKING = new TypeProduct("COOKING", "Cooking", "pot.png");
        TypeProduct OIL = new TypeProduct("OIL", "Oil");
        TypeProduct type4 = new TypeProduct("S&S", "Salt & Sugar");
        TypeProduct BREAKFAST = new TypeProduct("BREAKFAST", "Breakfast", "pie.png");
        TypeProduct BREAD = new TypeProduct("BREAD", "Bread");
        TypeProduct CEREAL = new TypeProduct("CEREAL", "Cereal");
        TypeProduct JAM = new TypeProduct("JAM", "Jam");
        TypeProduct BEVERAGE = new TypeProduct("BEVERAGE", "Beverage", "drink.png");
        TypeProduct COFFEE = new TypeProduct("COFFEE", "Coffee");
        TypeProduct TEA = new TypeProduct("TEA", "Tea");
        TypeProduct type5 = new TypeProduct("H&B", "Health & Beauty", "mirror.png");

        List<TypeProduct> listType = List.of(type1, FRUIT, VEGE, type2, MEAT, FISH, SNACK,
                type3, NOODLE, DAIRY, MILK, BUTTER, YOGURT, COFFEE, COOKING, OIL,
                type4, BREAKFAST, BREAD, CEREAL, JAM, BEVERAGE, TEA, type5);
        typeProductRepository.saveAll(listType);

    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}