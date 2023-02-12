package mk.ukim.finki.wp.exam.example.service.impl;

import mk.ukim.finki.wp.exam.example.model.Product;
import mk.ukim.finki.wp.exam.example.model.exceptions.InvalidProductIdException;
import mk.ukim.finki.wp.exam.example.repository.CategoryRepository;
import mk.ukim.finki.wp.exam.example.repository.ProductRepository;
import mk.ukim.finki.wp.exam.example.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Product> listAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(InvalidProductIdException::new);
    }

    @Override
    public Product create(String name, Double price, Integer quantity, List<Long> categories) {
        return productRepository.save(new Product(name,price,quantity,categoryRepository.findAllById(categories)));
    }

    @Override
    public Product update(Long id, String name, Double price, Integer quantity, List<Long> categories) {
        Product product=findById(id);
        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setCategories(categoryRepository.findAllById(categories));
        productRepository.save(product);
        return product;
    }

    @Override
    public Product delete(Long id) {
        Product product=findById(id);
        productRepository.deleteById(id);
        return product;
    }

    @Override
    public List<Product> listProductsByNameAndCategory(String name, Long categoryId) {
        if(name!=null && categoryId!=null)
            return productRepository.findAllByNameLikeAndCategoriesContaining("%"+name+"%",categoryRepository.findById(categoryId).orElse(null));
        if(name!=null )
            return productRepository.findAllByNameLike("%"+name+"%");
        if(categoryId!=null)
            return productRepository.findAllByCategoriesContaining(categoryRepository.findById(categoryId).orElse(null));
        return listAllProducts();
    }
}
