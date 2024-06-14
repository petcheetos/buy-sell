package org.example.buysell.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.buysell.configuration.CustomUserDetails;
import org.example.buysell.models.Product;
import org.example.buysell.models.User;
import org.example.buysell.services.ProductService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    //    @GetMapping("/")
//    public String products(@RequestParam(name = "searchWord", required = false) String title,
//                           @AuthenticationPrincipal CustomUserDetails user, Model model) {
//        log.debug("Authenticated user: " + user);
//        model.addAttribute("products", productService.listProducts(title));
//        model.addAttribute("user", productService.getAuthorisedUser(user));
//        model.addAttribute("searchWord", title);
//        return "products";
//    }
    @GetMapping("/")
    public String listProducts(
            @RequestParam(value = "searchCity", required = false) String searchCity,
            @RequestParam(value = "searchWord", required = false) String searchWord,
            Model model,
            @AuthenticationPrincipal CustomUserDetails user) {

        List<Product> products;
        if ((searchCity != null && !searchCity.isEmpty()) && (searchWord != null && !searchWord.isEmpty())) {
            products = productService.findByCityAndTitle(searchCity, searchWord);
        } else if (searchCity != null && !searchCity.isEmpty()) {
            products = productService.findByCity(searchCity);
        } else if (searchWord != null && !searchWord.isEmpty()) {
            products = productService.listProducts(searchWord);
        } else {
            products = productService.findAll();
        }

        model.addAttribute("products", products);
        model.addAttribute("user", productService.getAuthorisedUser(user));
        model.addAttribute("searchCity", searchCity);
        model.addAttribute("searchWord", searchWord);
        return "products";
    }


    @GetMapping("/product/{id}")
    public String productInfo(@PathVariable Long id, Model model, @AuthenticationPrincipal CustomUserDetails user) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        model.addAttribute("authorProduct", product.getUser());
        model.addAttribute("user", productService.getAuthorisedUser(user));
        return "product-info";
    }

    @PostMapping("/product/create")
    public String createProduct(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
                                @RequestParam("file3") MultipartFile file3, Product product,
                                @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        productService.saveProduct(productService.getAuthorisedUser(customUserDetails), product, file1, file2, file3);
        return "redirect:/my/products";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        productService.deleteProduct(productService.getAuthorisedUser(customUserDetails), id);
        return "redirect:/my/products";
    }

    @GetMapping("/my/products")
    public String userProducts(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        User user = productService.getAuthorisedUser(customUserDetails);
        model.addAttribute("user", user);
        model.addAttribute("products", user.getProducts());
        return "my-products";
    }
}
