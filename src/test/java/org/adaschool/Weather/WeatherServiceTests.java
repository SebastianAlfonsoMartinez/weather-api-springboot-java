package org.adaschool.Weather;

import org.adaschool.Weather.data.WeatherApiResponse;
import org.adaschool.Weather.data.WeatherReport;
import org.adaschool.Weather.service.WeatherReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class WeatherServiceTests {
	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private WeatherReportService weatherReportService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGetWeatherReport() {
		// Configura una respuesta simulada
		WeatherApiResponse mockResponse = new WeatherApiResponse();
		WeatherApiResponse.Main mockMain = new WeatherApiResponse.Main();
		mockMain.setTemperature(0.0);
		mockMain.setHumidity(63);
		mockResponse.setMain(mockMain);

		// Simula la respuesta del restTemplate
		when(restTemplate.getForObject(anyString(), eq(WeatherApiResponse.class))).thenReturn(mockResponse);

		// Llama al método que quieres probar
		WeatherReport report = weatherReportService.getWeatherReport(40.7128, -74.0060);

		// Verifica los resultados
		assertNotNull(report);
		assertEquals(0.0, report.getTemperature());
		assertEquals(63, report.getHumidity());
	}

	@Test
	public void testGetWeatherReportWithInvalidCoordinates() {
		// Configura RestTemplate para lanzar HttpClientErrorException.BadRequest
		when(restTemplate.getForObject(anyString(), eq(WeatherApiResponse.class)))
				.thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

		// Llama al método que quieres probar y captura la excepción
		try {
			weatherReportService.getWeatherReport(-91.0, 181.0);
			fail("Expected HttpClientErrorException.BadRequest to be thrown");
		} catch (HttpClientErrorException ex) {
			// Verifica que el estado de la excepción es 400
			assertEquals(400, ex.getRawStatusCode());
		}
	}
	@Test
	public void testGetWeatherReportWithUnauthorizedApiKey() {
		// Configura RestTemplate para lanzar HttpClientErrorException.Unauthorized
		when(restTemplate.getForObject(anyString(), eq(WeatherApiResponse.class)))
				.thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Unauthorized: {\"cod\":401, \"message\": \"Invalid API key. Please see https://openweathermap.org/faq#error401 for more info.\"}"));

		// Llama al método que quieres probar y captura la excepción
		try {
			weatherReportService.getWeatherReport(40.7128, -74.0060);
			fail("Expected HttpClientErrorException.Unauthorized to be thrown");
		} catch (HttpClientErrorException ex) {
			// Verifica que el estado de la excepción sea 401 Unauthorized
			assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
		}
	}





}
