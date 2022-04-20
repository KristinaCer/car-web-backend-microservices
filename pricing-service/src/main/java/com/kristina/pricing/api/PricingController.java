package com.kristina.pricing.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kristina.pricing.domain.price.Price;
import com.kristina.pricing.domain.price.TurboPrice;
import com.kristina.pricing.service.PriceException;
import com.kristina.pricing.service.PricingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Implements a REST-based controller for the pricing service. */
@RestController
@RequestMapping("/services/price")
public class PricingController {

  private final PricingService service;

  public PricingController(PricingService service) {
    this.service = service;
  }

  /**
   * Gets the price for a requested vehicle.
   *
   * @param vehicleId ID number of the vehicle for which the price is requested
   * @return price of the vehicle, or error that it was not found.
   */
  @GetMapping
  public Price get(@RequestParam Long vehicleId) {
    try {
      return service.getPrice(vehicleId);
    } catch (PriceException ex) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Price Not Found", ex);
    }
  }

  @PutMapping("/{vehicleId}")
  public Price update(@PathVariable Long vehicleId, @Valid @RequestBody Price price) {
    Price newPrice = null;
    try {
      newPrice = service.save(vehicleId, price);
    } catch (PriceException e) {
      e.printStackTrace();
    }
    return newPrice;
  }

  @PutMapping("/fake/{vehicleId}")
  public Price updateFake(@PathVariable Long vehicleId, @Valid @RequestBody String price) {
    System.out.println(price);
    ObjectMapper objectMapper = new ObjectMapper();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    objectMapper.setDateFormat(df);
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    try {
      Price parsedPrice = objectMapper.readValue(price, Price.class);
      System.out.println(parsedPrice.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @PutMapping("/fake-date")
  public Price updateFakeDate(@Valid @RequestBody String date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
    System.out.println(dateTime);
    return null;
  }

  @GetMapping("/fake/{price}")
  public Price getFake(@PathVariable Price price) {
    return price;
  }

  @PostMapping("/turbo-price/by-param")
  public void createNewQueryParams(
      @RequestParam Long id,
      @RequestParam String currency,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime timeOfRelease) {
    // 2000-10-31T01:30:00.000-05:00
    TurboPrice turboPrice = new TurboPrice(id, currency, timeOfRelease);
    System.out.println(turboPrice);
  }

  @PostMapping("/turbo-price/by-model")
  public void createNewQueryByModel(@ModelAttribute TurboPrice turboPrice) {
    System.out.println(turboPrice);
  }
}
