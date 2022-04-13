package app.com.vending.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="counter")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Counter {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="counter_id")	
	private int id;
	@Column(name="counter_count")	
	private int count;
	
	public Counter() {
	}
	
	public Counter(int id, int count) {
		this.id = id;
		this.count = count;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public String toString() {
		return String.format("Counter [id=%s, count=%s]", id, count);
	}
	
	
	
}
