package com.kristina.vehicles.service;

import com.kristina.vehicles.client.maps.MapsClient;
import com.kristina.vehicles.client.prices.PriceClient;
import com.kristina.vehicles.domain.Condition;
import com.kristina.vehicles.domain.Location;
import com.kristina.vehicles.domain.car.Car;
import com.kristina.vehicles.domain.car.CarRepository;
import com.kristina.vehicles.domain.car.Details;
import com.kristina.vehicles.domain.manufacturer.Manufacturer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {CarService.class})
@ExtendWith(SpringExtension.class)
class CarServiceTest {
    @MockBean
    private CarRepository carRepository;

    @Autowired
    private CarService carService;

    @MockBean
    private MapsClient mapsClient;

    @MockBean
    private PriceClient priceClient;

    @Test
    void listAllCars_success() {
        //when
        carService.list();
        //then
        verify(carRepository).findAll();
    }

    @Test
    void saveCar_success() {
        //given
        Car car = getCar();
        //when
        carService.save(car);
        //then
        ArgumentCaptor<Car> carArgumentCaptor = ArgumentCaptor.forClass(Car.class);
        verify(carRepository).save(carArgumentCaptor.capture());
        Car capturedCar = carArgumentCaptor.getValue();
        assertThat(capturedCar).isEqualTo(car);
    }

    @Test
    void saveCar_whenIdNonExisting_throwsCarNotFoundException() {
        //given
        Car car = getCar();
        car.setId(123l);
        given(this.carRepository.findById((Long) any())).willReturn(Optional.ofNullable(null));
        //when
        //then
        assertThrows(CarNotFoundException.class, () -> this.carService.save(car));
        verify(carRepository, never()).save(any());
    }

    @Test
    void deleteCar_success(){

    }


















    /**
     * Creates an example Car object for use in testing.
     *
     * @return an example Car object
     */
    private Car getCar() {
        Car car = new Car();
        car.setLocation(new Location(40.730610, -73.935242));
        Details details = new Details();
        Manufacturer manufacturer = new Manufacturer(101, "Chevrolet");
        details.setManufacturer(manufacturer);
        details.setModel("Impala");
        details.setMileage(32280);
        details.setExternalColor("white");
        details.setBody("sedan");
        details.setEngine("3.6L V6");
        details.setFuelType("Gasoline");
        details.setModelYear(2018);
        details.setProductionYear(2018);
        details.setNumberOfDoors(4);
        car.setDetails(details);
        car.setCondition(Condition.USED);
        return car;
    }
}

