package com.hotelcorp.business;

import com.hotelcorp.data.Booking;
import com.hotelcorp.data.BookingRepository;
import com.hotelcorp.data.Hotel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = NONE)
public class BookingServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private HotelService mockHotelService;

    @Mock
    private BookingRepository mockBookingRepository;

    // service under test
    private BookingService bookingService;

    @Before
    public void setUp() throws Exception {
        bookingService = new BookingService(mockHotelService, mockBookingRepository);
    }

    @Test
    public void test_GetBookingById_WithExistingId_ShouldReturnBooking() {
        Booking mockBooking = new Booking("R2",
                "D2",
                Short.valueOf("5"),
                BigDecimal.TEN,
                "EUR",
                new Hotel("Plaza", null, null));

        when(mockBookingRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(mockBooking));
        Booking actualBooking = bookingService.getBookingById(1L);

        assertThat(actualBooking, notNullValue());
        assertThat(actualBooking.getCustomerLastName(), is(mockBooking.getCustomerLastName()));
        assertThat(actualBooking.getHotel(), is(mockBooking.getHotel()));
    }

    @Test
    public void test_GetBookingById_WithNonExistingId_ShouldRaiseException() {
        when(mockBookingRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        expectedException.expect(ResponseStatusException.class);
        expectedException.expectMessage("NOT_FOUND");
        bookingService.getBookingById(1L);
    }

    @Test
    public void test_CreateBooking_WithExistingHotel_ShouldCreateBooking() {
        Hotel mockHotel = new Hotel("Plaza", null, Byte.valueOf("5"));
        Booking mockBooking = new Booking("R2",
                "D2",
                Short.valueOf("5"),
                BigDecimal.TEN,
                "EUR",
                mockHotel);

        // hotel already exists
        when(mockHotelService.getHotelByIdOrName(mockHotel)).thenReturn(Optional.of(mockHotel));
        // on invocation of save(b) -> return b
        when(mockBookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        Booking createdBooking = bookingService.createBooking(mockBooking);
        assertThat(createdBooking, notNullValue());
        assertThat(createdBooking.getCustomerLastName(), is("D2"));
        assertThat(createdBooking.getHotel(), is(mockHotel));
        // verify that createHotel() was not invoked
        verify(mockHotelService, never()).createHotel(any(Hotel.class));
    }

    @Test
    public void test_CreateBooking_WithNonExistingHotel_ShouldCreateHotelAndBooking() {
        Hotel mockHotel = new Hotel("Plaza", null, Byte.valueOf("5"));
        Booking mockBooking = new Booking("R2",
                "D2",
                Short.valueOf("5"),
                BigDecimal.TEN,
                "EUR",
                mockHotel);

        // hotel does not exist
        when(mockHotelService.getHotelByIdOrName(mockHotel)).thenReturn(Optional.empty());
        // create hotel
        when(mockHotelService.createHotel(mockHotel)).thenReturn(mockHotel);
        // on invocation of save(b) -> return b
        when(mockBookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        Booking createdBooking = bookingService.createBooking(mockBooking);
        assertThat(createdBooking, notNullValue());
        assertThat(createdBooking.getCustomerLastName(), is("D2"));
        assertThat(createdBooking.getHotel(), is(mockHotel));
        // verify that createHotel() was indeed invoked
        verify(mockHotelService, times(1)).createHotel(mockHotel);
    }

    @Test
    public void test_UpdateBooking_WithExistingId_ShouldUpdateBooking() {
        Hotel mockHotel = new Hotel("Plaza", null, Byte.valueOf("5"));
        Booking mockBooking = new Booking("R2",
                "D2",
                Short.valueOf("5"),
                BigDecimal.TEN,
                "EUR",
                mockHotel);

        when(mockBookingRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(mockBooking));
        // on invocation of save(b) -> return b
        when(mockBookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        // update price
        Booking newBooking = new Booking(null, null, null, BigDecimal.ONE, null, null);
        Booking updatedBooking = bookingService.updateBooking(newBooking, 1L);

        assertThat(updatedBooking, notNullValue());
        assertThat(updatedBooking.getCustomerLastName(), is("D2"));
        assertThat(updatedBooking.getHotel(), is(mockHotel));
        // check also for updated value
        assertThat(updatedBooking.getPrice(), is(BigDecimal.ONE));
    }

    @Test
    public void test_UpdateBooking_WithExistingIdAndValidHotel_ShouldUpdateBookingAndHotel() {
        Hotel mockHotel = new Hotel("Plaza", null, Byte.valueOf("5"));
        Booking mockBooking = new Booking("R2",
                "D2",
                Short.valueOf("5"),
                BigDecimal.TEN,
                "EUR",
                mockHotel);

        when(mockBookingRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(mockBooking));
        // on invocation of save(b) -> return b
        when(mockBookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        Hotel newHotel = new Hotel("Hilton", null, Byte.valueOf("5"));
        when(mockHotelService.getHotelByIdOrName(any(Hotel.class))).thenReturn(Optional.of(newHotel));

        // update price & hotel
        Booking newBooking = new Booking(null, null, null, BigDecimal.ONE, null, newHotel);
        Booking updatedBooking = bookingService.updateBooking(newBooking, 1L);

        assertThat(updatedBooking, notNullValue());
        assertThat(updatedBooking.getCustomerLastName(), is("D2"));
        // check for updated hotel
        assertThat(updatedBooking.getHotel(), is(newHotel));
        // check for updated price
        assertThat(updatedBooking.getPrice(), is(BigDecimal.ONE));
    }

    @Test
    public void test_UpdateBooking_WithExistingIdButInvalidHotel_ShouldUpdateBookingButNotTheHotel() {
        Hotel mockHotel = new Hotel("Plaza", null, Byte.valueOf("5"));
        Booking mockBooking = new Booking("R2",
                "D2",
                Short.valueOf("5"),
                BigDecimal.TEN,
                "EUR",
                mockHotel);

        when(mockBookingRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(mockBooking));
        // on invocation of save(b) -> return b
        when(mockBookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));
        // simulate an invalid Hotel
        when(mockHotelService.getHotelByIdOrName(any(Hotel.class))).thenReturn(Optional.empty());

        Hotel invalidHotel = new Hotel("Invalid Hotel", null, null);
        // update price & (invalid) hotel
        Booking newBooking = new Booking(null, null, null, BigDecimal.ONE, null, invalidHotel);
        Booking updatedBooking = bookingService.updateBooking(newBooking, 1L);

        assertThat(updatedBooking, notNullValue());
        assertThat(updatedBooking.getCustomerLastName(), is("D2"));
        // validate that hotel info is unchanged
        assertThat(updatedBooking.getHotel(), is(mockHotel));
        // check for updated price
        assertThat(updatedBooking.getPrice(), is(BigDecimal.ONE));
    }

    @Test
    public void test_UpdateBooking_WithNonExistingId_ShouldRaiseException() {
        when(mockBookingRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        Booking dummy  = new Booking(null, "dummy", null, null, null, null);
        expectedException.expect(ResponseStatusException.class);
        expectedException.expectMessage("NOT_FOUND");
        bookingService.updateBooking(dummy, 1L);
    }

    @Test
    public void test_DeleteBooking_WithExistingId_ShouldDeleteBooking() {
        when(mockBookingRepository.existsById(any(Long.class)))
                .thenReturn(true);

        bookingService.deleteBooking(1L);
        verify(mockBookingRepository, times(1)).deleteById(1L);
    }

    @Test
    public void test_DeleteBooking_WithNonExistingId_ShouldRaiseException() {
        when(mockBookingRepository.existsById(any(Long.class)))
                .thenReturn(false);

        expectedException.expect(ResponseStatusException.class);
        expectedException.expectMessage("NOT_FOUND");
        bookingService.deleteBooking(1L);
    }
}