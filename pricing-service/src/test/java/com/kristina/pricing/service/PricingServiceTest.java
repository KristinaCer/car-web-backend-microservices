package com.kristina.pricing.service;

import com.kristina.pricing.domain.price.Price;
import com.kristina.pricing.domain.price.PriceRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PricingServiceTest {

  private static final Map<Long, Price> PRICES =
      LongStream.range(1, 20)
          .mapToObj(idx -> new Price("USD", new BigDecimal(idx), idx))
          .collect(Collectors.toMap(Price::getVehicleId, p -> p));

  @InjectMocks private PricingService service;
  @Mock private PriceRepository repository;

  @Test
  public void whenGettingExistingPrice_Succeed() throws PriceException {
    // given
    when(repository.findAll()).thenReturn(PRICES);
    long vehicleId = 1l;
    BigDecimal expectedPrice = new BigDecimal(1l);
    // when
    BigDecimal result = service.getPrice(vehicleId).getPrice();
    // then
    Assert.assertEquals(result, expectedPrice);
  }

  @Test(expected = PriceException.class)
  public void whenGettingNonExistingPrice_CatchException() throws PriceException {
    // given
    when(repository.findAll()).thenReturn(PRICES);
    long vehicleId = 99l;
    BigDecimal expectedPrice = new BigDecimal(1l);
    // when
    BigDecimal result = service.getPrice(vehicleId).getPrice();
    // expect
    Assert.assertEquals(result, expectedPrice);
  }
}
