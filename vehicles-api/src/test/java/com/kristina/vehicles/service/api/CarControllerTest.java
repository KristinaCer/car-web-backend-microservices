package com.kristina.vehicles.service.api;

import com.kristina.vehicles.client.maps.MapsClient;
import com.kristina.vehicles.client.prices.PriceClient;
import com.kristina.vehicles.domain.Condition;
import com.kristina.vehicles.domain.Location;
import com.kristina.vehicles.domain.car.Car;
import com.kristina.vehicles.domain.car.Details;
import com.kristina.vehicles.domain.manufacturer.Manufacturer;
import com.kristina.vehicles.service.CarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CarControllerTest {
  @Autowired private MockMvc mvc;

  @Autowired private JacksonTester<Car> json;

  @MockBean private CarService carService;

  @MockBean private PriceClient priceClient;

  @MockBean private MapsClient mapsClient;

  @Test
  public void createCar() throws Exception {
    Car car = getCar();
    given(carService.save(any())).willReturn(car);
    mvc.perform(
            post(new URI("/cars"))
                .content(json.write(car).getJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isCreated());
  }

  @Test
  public void listCars() throws Exception {
    given(carService.list()).willReturn(Arrays.asList(getCar()));
    mvc.perform(get("/cars").contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$._embedded.carList.size()", is(1)))
        .andExpect(status().isOk());
  }

  /**
   * Tests the read operation for a single car by ID.
   *
   * @throws Exception if the read operation for a single car fails
   */

  @Test
  public void findCar() throws Exception {
    Car car = getCar();
    car.setId(1L);
    given(carService.findById(any())).willReturn(car);
    mvc.perform(get("/cars/" + car.getId()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.details.model", is(car.getDetails().getModel())))
        .andExpect(status().isOk());
  }

  /**
   * Tests the deletion of a single car by ID.
   *
   * @throws Exception if the delete operation of a vehicle fails
   */
  @Test
  public void deleteCar() throws Exception {
    Car car = getCar();
    car.setId(1L);
    Long id = car.getId();
    doNothing().when(carService).delete(id);
    mvc.perform(delete("/cars/" + id.toString()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

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
