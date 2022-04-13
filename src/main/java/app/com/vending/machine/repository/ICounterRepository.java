package app.com.vending.machine.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.com.vending.entities.Counter;
import org.springframework.stereotype.Repository;

@Repository
public interface ICounterRepository extends JpaRepository<Counter, Integer> {

}
