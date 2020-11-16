package com.travelapp.travelplanner.controllers;

import com.travelapp.travelplanner.models.dto.ForecastRecordDto;
import com.travelapp.travelplanner.services.IForecastRecordService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import javax.validation.Valid;


@RestController
//@RequestMapping("weather")
public class WeatherController {
    @Autowired
    IForecastRecordService forecastRecordService;

//    @ApiOperation(value = "add bookings")
//    @RequestMapping(
//            produces = {"application/json"},
//            consumes = {"application/json"},
//            method = RequestMethod.POST)
//    public ResponseEntity<Booking> addBookings(@RequestBody Booking booking) {
//        Booking savedBooking;
//        try {
//            savedBooking = bookingService.addBookings(booking);
//        } catch (ApiException e) {
//            throw new BookingsApiException(e.getHttpStatus(), e.getMessage());
//        } catch (Exception e) {
//            throw new BookingsApiException(HttpStatus.BAD_REQUEST, e.getMessage());
//        }
//        return new ResponseEntity<>(savedBooking, HttpStatus.CREATED);
//    }

    @ApiOperation("Return a JSON object that gives the weather averages.")
    @PostMapping(value = "/forecast", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCurrentWeatherInformation(@ApiParam("City's name")
                                                    @RequestParam(required = true) String cityName,
                                                    @RequestParam(required = true) String weatherDate) {
        return forecastRecordService.getCurrentWeather(cityName, weatherDate);
    }

    @ApiOperation("Return a JSON object that gives the weather averages.")
    @PostMapping(value = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getWeatherSummary(@ApiParam("City's name")
                                                    @RequestParam(required = true) String cityName,
                                                    @RequestParam(required = true) String weatherDate) {
        return forecastRecordService.getGeneralSummary(cityName, weatherDate);
    }

    @PostMapping("/weather")
    public ResponseEntity<Object> createForecastRecord(@Valid @RequestBody ForecastRecordDto forecastRecordDto) {
        forecastRecordService.save(forecastRecordDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/weather")
    public ResponseEntity<List<ForecastRecordDto>> getAllByDate(@RequestParam(required = false) String date) {
        try {
            List<ForecastRecordDto> forecastRecordDtos;

            if (date == null) {
                forecastRecordDtos = forecastRecordService.getAll();
            } else {
                forecastRecordDtos = forecastRecordService.getAllByWeatherDate(date);
            }

            if (forecastRecordDtos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(forecastRecordDtos, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/cleanup")
    public ResponseEntity<Object> deleteAll() {
        forecastRecordService.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}