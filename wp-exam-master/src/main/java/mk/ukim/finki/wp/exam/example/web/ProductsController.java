package mk.ukim.finki.wp.exam.example.web;

import mk.ukim.finki.wp.exam.example.model.Product;
import mk.ukim.finki.wp.exam.example.service.CategoryService;
import mk.ukim.finki.wp.exam.example.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductsController {

    private final ProductService productService;
    public final CategoryService categoryService;

    public ProductsController(ProductService productService, CategoryService categoryService, CategoryService categoryService1) {
        this.productService = productService;
        this.categoryService = categoryService1;
    }

    @GetMapping({"/","/products"})
    public String showProducts(@RequestParam(required = false) String nameSearch,
                               @RequestParam(required = false) Long categoryId,
                               Model model) {
        List<Product> products;
        if (nameSearch == null && categoryId == null) {
            products=this.productService.listAllProducts();
        } else {
            products=this.productService.listProductsByNameAndCategory(nameSearch, categoryId);
        }
        model.addAttribute("products",products);
        model.addAttribute("categories",categoryService.listAll());
        return "list";
    }
    @GetMapping("/products/add")
    public String showAdd(Model model) {
        model.addAttribute("categories",categoryService.listAll());
        return "form";
    }
    @GetMapping("/products/{id}/edit")
    public String showEdit(@PathVariable Long id, Model model) {
        model.addAttribute("product",this.productService.findById(id));
        model.addAttribute("categories",categoryService.listAll());
        return "form";
    }

    @PostMapping("/products")
    public String create(@RequestParam String name,
                         @RequestParam Double price,
                         @RequestParam Integer quantity,
                         @RequestParam List<Long> categories) {
        this.productService.create(name, price, quantity, categories);
        return "redirect:/products";
    }

    @PostMapping("/products/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam Double price,
                         @RequestParam Integer quantity,
                         @RequestParam List<Long> categories) {
        this.productService.update(id, name, price, quantity, categories);
        return "redirect:/products";
    }

    @PostMapping("/products/{id}/delete")
    public String delete(@PathVariable Long id) {
        this.productService.delete(id);
        return "redirect:/products";
    }

}
