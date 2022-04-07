package com.kristina.vehicles.service;

import com.kristina.vehicles.client.maps.MapsClient;
import com.kristina.vehicles.client.prices.PriceClient;
import com.kristina.vehicles.domain.Location;
import com.kristina.vehicles.domain.car.Car;
import com.kristina.vehicles.domain.car.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implements the car service create, read, update or delete information about vehicles, as well as
 * gather related location and price data when desired.
 */
@Service
public class CarService {

  private final CarRepository repository;
  private final MapsClient mapsClient;
  private final PriceClient priceClient;

  public CarService(CarRepository repository, MapsClient mapsClient, PriceClient priceClient) {
    this.repository = repository;
    this.mapsClient = mapsClient;
    this.priceClient = priceClient;
  }

  /**
   * Gathers a list of all vehicles
   *
   * @return a list of all vehicles in the CarRepository
   */
  public List<Car> list() {
    return repository.findAll();
  }

  /**
   * Gets car information by ID (or throws exception if non-existent)
   *
   * @param id the ID number of the car to gather information on
   * @return the requested car's information, including location and price
   */
  public Car findById(Long id) {
    Car car = repository.findById(id).orElseThrow(CarNotFoundException::new);
    /**
     * Note: The car class file uses @transient, meaning you will need to call the pricing service
     * each time to get the price.
     */
    String price = priceClient.getPrice(car.getId());
    car.setPrice(price);
    Location carLocation = mapsClient.getAddress(car.getLocation());
    car.setLocation(carLocation);
    return car;
  }

  /**
   * Either creates or updates a vehicle, based on prior existence of car
   *
   * @param car A car object, which can be either new or existing
   * @return the new/updated car is stored in the repository
   */
  public Car save(Car car) {
    if (car.getId() != null) {
      Car carToBeUpdated = repository.findById(car.getId()).orElseThrow(CarNotFoundException::new);
      carToBeUpdated.setDetails(car.getDetails());
      carToBeUpdated.setLocation(car.getLocation());
      carToBeUpdated.setCondition(car.getCondition());
      return repository.save(carToBeUpdated);
    }
    return repository.save(car);
  }

  /**
   * Deletes a given car by ID
   *
   * @param id the ID number of the car to delete
   */
  public void delete(Long id) {
    Car car = repository.findById(id).orElseThrow(CarNotFoundException::new);
    repository.delete(car);
  }
}
