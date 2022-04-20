package com.kristina.pricing.service;

import com.kristina.pricing.domain.price.Price;
import com.kristina.pricing.domain.price.PriceRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

/** Implements the pricing service to get prices for each vehicle. */
@Service
public class PricingService {

  private final PriceRepository repository;

  public PricingService(PriceRepository repository) {
    this.repository = repository;
  }

  public Price save(Long id, Price price) throws PriceException {
    if(id == null && id>0 || id > 20){
      throw new PriceException("Price not found!");
    } else{
      repository.updatePrice(id,price);
    } return price;
  }

  /**
   * If a valid vehicle ID, gets the price of the vehicle from the stored array.
   *
   * @param vehicleId ID number of the vehicle the price is requested for.
   * @return price of the requested vehicle
   * @throws PriceException vehicleID was not found
   */
  public Price getPrice(Long vehicleId) throws PriceException {
    // ctrl+w - mark code
    // ctrl+alt+v - refactor
    // shift+f6 - rename
    Map<Long, Price> allPrices = repository.findAllPrices();
    if (!allPrices.containsKey(vehicleId)) {
      throw new PriceException("Cannot find price for Vehicle " + vehicleId);
    }
    return allPrices.get(vehicleId);
  }
}
