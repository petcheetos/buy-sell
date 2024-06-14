package org.example.buysell.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.buysell.configuration.CustomUserDetails;
import org.example.buysell.models.Image;
import org.example.buysell.models.Product;
import org.example.buysell.models.User;
import org.example.buysell.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> listProducts(String title) {
        if (title != null && !title.isEmpty()) {
            return productRepository.findByTitleContainingIgnoreCase(title);
        }
        return productRepository.findAll();
    }

    public List<Product> findByCity(String city) {
        return productRepository.findByCity(city);
    }

    public List<Product> findByCityAndTitle(String city, String title) {
        return productRepository.findByCityAndTitleContainingIgnoreCase(city, title);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public void saveProduct(User user, Product product, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        product.setUser(user);
        Image image1;
        Image image2;
        Image image3;
        if (file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            product.addImageToProduct(image1);
        }
        if (file2.getSize() != 0) {
            image2 = toImageEntity(file2);
            product.addImageToProduct(image2);
        }
        if (file3.getSize() != 0) {
            image3 = toImageEntity(file3);
            product.addImageToProduct(image3);
        }
        log.info("Saving new Product. Title: {};", product.getTitle());
        if (file1.getSize() != 0) {
            Product productFromDb = productRepository.save(product);
            productFromDb.setPreviewImageId(productFromDb.getImages().get(0).getId());
        }
        productRepository.save(product);
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    public void deleteProduct(User user, Long id) {
        Product product = productRepository.findById(id)
                .orElse(null);
        if (product != null) {
            if (product.getUser().getId().equals(user.getId())) {
                productRepository.delete(product);
                log.info("Product with id = {} was deleted", id);
            } else {
                log.error("User: {} haven't this product with id = {}", user.getEmail(), id);
            }
        } else {
            log.error("Product with id = {} is not found", id);
        }
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public User getAuthorisedUser(CustomUserDetails userDetails) {
        if (userDetails == null) {
            return new User();
        }
        return userDetails.getUser();
    }
}
