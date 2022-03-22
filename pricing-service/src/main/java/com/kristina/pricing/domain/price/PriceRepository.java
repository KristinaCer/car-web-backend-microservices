package com.kristina.pricing.domain.price;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Repository
public class PriceRepository {

  private static final Map<Long, Price> PRICES =
      LongStream.range(1, 20)
          .mapToObj(i -> new Price("USD", randomPrice(), i))
          .collect(Collectors.toMap(Price::getVehicleId, p -> p));

  private static BigDecimal randomPrice() {
    return new BigDecimal(ThreadLocalRandom.current().nextDouble(1, 5))
        .multiply(new BigDecimal(5000d))
        .setScale(2, RoundingMode.HALF_UP);
  }

  public Map<Long, Price> findAllPrices() {
    return PRICES;
  }
}
