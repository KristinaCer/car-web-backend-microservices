package com.kristina.pricing.domain.price;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class TurboPrice {
    private Long id;
    private String currency;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime timeOfRelease;

    public TurboPrice() {
    }

    public TurboPrice(Long id, String currency, LocalDateTime timeOfRelease) {
        this.id = id;
        this.currency = currency;
        this.timeOfRelease = timeOfRelease;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDateTime getTimeOfRelease() {
        return timeOfRelease;
    }

    public void setTimeOfRelease(LocalDateTime timeOfRelease) {
        this.timeOfRelease = timeOfRelease;
    }
}
