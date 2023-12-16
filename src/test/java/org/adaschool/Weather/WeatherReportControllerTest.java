package org.adaschool.Weather;

import org.adaschool.Weather.controller.WeatherReportController;
import org.adaschool.Weather.data.WeatherReport;
import org.adaschool.Weather.service.WeatherReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

public class WeatherReportControllerTest {

    @Mock
    private WeatherReportService weatherReportService;

    @InjectMocks
    private WeatherReportController weatherReportController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetWeatherReportSuccess() {
        WeatherReport mockReport = new WeatherReport();
        mockReport.setTemperature(20.0);
        mockReport.setHumidity(50.0);

        when(weatherReportService.getWeatherReport(anyDouble(), anyDouble())).thenReturn(mockReport);

        WeatherReport report = weatherReportController.getWeatherReport(37.8267, -122.4233);

        assertNotNull(report);
        assertEquals(20.0, report.getTemperature());
        assertEquals(50.0, report.getHumidity());
    }

    @Test
    public void testGetWeatherReportWithInvalidCoordinates() {
        when(weatherReportService.getWeatherReport(anyDouble(), anyDouble())).thenReturn(null);

        WeatherReport report = weatherReportController.getWeatherReport(-91.0, 181.0);

        assertNull(report);
    }
    @Test
    public void testGetWeatherReportServiceError() {
        when(weatherReportService.getWeatherReport(anyDouble(), anyDouble()))
                .thenThrow(new RuntimeException("Internal server error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            weatherReportController.getWeatherReport(37.8267, -122.4233);
        });

        assertEquals("Internal server error", exception.getMessage());
    }



}
