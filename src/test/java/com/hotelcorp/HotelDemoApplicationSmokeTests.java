package com.hotelcorp;

import com.hotelcorp.controller.BookingController;
import com.hotelcorp.controller.HotelController;
import com.hotelcorp.controller.QueryController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HotelDemoApplicationSmokeTests {

	@Autowired
	private HotelController hotelController;

	@Autowired
	private BookingController bookingController;

	@Autowired
	private QueryController queryController;

	@Test
	public void contextLoads() {
		assertThat(hotelController, notNullValue());
		assertThat(bookingController, notNullValue());
		assertThat(queryController, notNullValue());
	}

}
