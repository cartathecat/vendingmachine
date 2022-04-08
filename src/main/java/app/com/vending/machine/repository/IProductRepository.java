package app.com.vending.machine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import app.com.vending.entities.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<Product, Integer> {

}
