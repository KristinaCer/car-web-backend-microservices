package com.kristina.pricing.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.kristina.pricing.domain.price.Price;
import com.kristina.pricing.domain.price.PriceRepository;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {PricingService.class})
@ExtendWith(SpringExtension.class)
class PricingServiceTest {
    @MockBean
    private PriceRepository priceRepository;

    @Autowired
    private PricingService pricingService;

    @Test
    void getPrice_withInvalidVehicleId_throwsPriceException() throws PriceException {
        //given
        given(this.priceRepository.findAllPrices()).willReturn(new HashMap<>());
        //when
        //then
        assertThrows(PriceException.class, () -> this.pricingService.getPrice(123L));
        //or
        assertThatThrownBy(() -> this.pricingService.getPrice(123L))
                .isInstanceOf(PriceException.class)
                .hasMessageContaining("Cannot find price for Vehicle ");
    }

    @Test
    void getPrice_withValidVehicleId_success() throws PriceException {
        //given
        HashMap<Long, Price> resultLongPriceMap = new HashMap<>();
        Price price = new Price();
        resultLongPriceMap.put(1L, price);
        given(this.priceRepository.findAllPrices()).willReturn(resultLongPriceMap);
        //when
        Price result = this.pricingService.getPrice(1L);
        //then
        assertSame(price, result);
        verify(this.priceRepository).findAllPrices();
    }
}

