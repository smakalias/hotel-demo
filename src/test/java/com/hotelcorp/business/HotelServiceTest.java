package com.hotelcorp.business;

import com.hotelcorp.data.Hotel;
import com.hotelcorp.data.HotelRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = NONE)
public class HotelServiceTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private HotelRepository mockHotelRepository;

    // service under test
    private HotelService hotelService = new HotelService(mockHotelRepository);

    @Before
    public void setUp() throws Exception {
        hotelService = new HotelService(mockHotelRepository);
    }

    @Test
    public void test_GetHotelById_WithExistingId_ShouldReturnHotel() {
        Hotel mockHotel = new Hotel("Plaza", null, null);

        when(mockHotelRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(mockHotel));
        Hotel actualHotel = hotelService.getHotelById(1L);

        assertThat(actualHotel, notNullValue());
        assertThat(actualHotel.getName(), is(mockHotel.getName()));
    }

    @Test
    public void test_GetHotelById_WithNonExistingId_ShouldRaiseException() {
        when(mockHotelRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        expectedException.expect(ResponseStatusException.class);
        expectedException.expectMessage("NOT_FOUND");
        hotelService.getHotelById(1L);
    }

    @Test
    public void test_CreateHotel_WithUniqueName_ShouldCreateHotel() {
        Hotel mockHotel = new Hotel("Plaza", null, Byte.valueOf("5"));
        when(mockHotelRepository.findByName(anyString()))
                .thenReturn(Optional.empty());
        // on invocation of save(h) -> return h
        when(mockHotelRepository.save(any(Hotel.class)))
                .thenAnswer(i -> i.getArgument(0));

        Hotel createdHotel = hotelService.createHotel(mockHotel);

        assertThat(createdHotel, notNullValue());
        assertThat(createdHotel.getName(), is(mockHotel.getName()));
    }

    @Test
    public void test_CreateHotel_WithNonUniqueName_ShouldRaiseException() {
        Hotel mockHotel = new Hotel("Plaza", null, Byte.valueOf("5"));
        when(mockHotelRepository.findByName(anyString()))
                .thenReturn(Optional.of(mockHotel));

        expectedException.expect(ResponseStatusException.class);
        expectedException.expectMessage("CONFLICT");
        hotelService.createHotel(mockHotel);

        verify(mockHotelRepository, never()).save(any(Hotel.class));
    }


    @Test
    public void test_UpdateHotel_WithExistingHotel_ShouldUpdateHotel() {
        Hotel mockHotel = new Hotel("Plaza", null, Byte.valueOf("5"));
        when(mockHotelRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(mockHotel));
        // make sure that name is unique
        when(mockHotelRepository.findByName(anyString()))
                .thenReturn(Optional.empty());
        // on invocation of save(h) -> return h
        when(mockHotelRepository.save(any(Hotel.class)))
                .thenAnswer(i -> i.getArgument(0));

        Hotel newHotel = new Hotel("Plaza upd", "new address", null);
        Hotel updatedHotel = hotelService.updateHotel(newHotel, 1L);

        assertThat(updatedHotel, notNullValue());
        assertThat(updatedHotel.getName(), is("Plaza upd"));
        assertThat(updatedHotel.getAddress(), is("new address"));
        assertThat(updatedHotel.getRating(), is(Byte.valueOf("5")));
    }

    @Test
    public void test_UpdateHotel_WithExistingHotelSameName_ShouldUpdateHotel() {
        Hotel mockHotel = new Hotel("Plaza", null, Byte.valueOf("5"));
        when(mockHotelRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(mockHotel));
        // on invocation of save(h) -> return h
        when(mockHotelRepository.save(any(Hotel.class)))
                .thenAnswer(i -> i.getArgument(0));

        Hotel newHotel = new Hotel("Plaza", "new address", null);
        Hotel updatedHotel = hotelService.updateHotel(newHotel, 1L);

        assertThat(updatedHotel, notNullValue());
        assertThat(updatedHotel.getName(), is("Plaza"));
        assertThat(updatedHotel.getAddress(), is("new address"));
        assertThat(updatedHotel.getRating(), is(Byte.valueOf("5")));
        // check that findByName was never invoked as the name did not change
        verify(mockHotelRepository, never()).findByName(anyString());
    }

    @Test
    public void test_UpdateHotel_WithExistingHotelNonUniqueName_ShouldRaiseException() {
        Hotel mockHotel = new Hotel("Plaza", null, Byte.valueOf("5"));
        when(mockHotelRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(mockHotel));
        // name is not unique
        when(mockHotelRepository.findByName("Hilton"))
                .thenReturn(Optional.of(new Hotel("Hilton", null, null)));

        expectedException.expect(ResponseStatusException.class);
        expectedException.expectMessage("CONFLICT");
        Hotel upd = new Hotel("Hilton", "new address", null);
        hotelService.updateHotel(upd, 1L);
        verify(mockHotelRepository, never()).save(any(Hotel.class));
    }

    @Test
    public void test_UpdateHotel_WithNonExistingHotel_ShouldRaiseException() {
        when(mockHotelRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        expectedException.expect(ResponseStatusException.class);
        expectedException.expectMessage("NOT_FOUND");
        hotelService.updateHotel(new Hotel("dummy", null, null), 1L);
    }

    @Test
    public void test_DeleteHotel_WithExistingHotel_ShouldDeleteHotel() {
        when(mockHotelRepository.existsById(any(Long.class)))
                .thenReturn(true);

        hotelService.deleteHotel(1L);
        verify(mockHotelRepository, times(1)).deleteById(any(Long.class));
    }

    @Test
    public void test_GetHotelByIdOrName_WithNullIdAndNullName_ShouldReturnNull() {
        final var optionalHotel = hotelService.getHotelByIdOrName(new Hotel(null, null, null));
        assertThat(optionalHotel.isEmpty(), is(true));
    }

    @Test
    public void test_GetHotelByIdOrName_WithExistingIdAndNullName_ShouldReturnMatchedHotel() {
        Hotel mockHotel = new Hotel("Plaza", null, null);

        when(mockHotelRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(mockHotel));

        Hotel searchHotel = new Hotel(null, null, null);
        searchHotel.setId(1L);
        final var optionalHotel = hotelService.getHotelByIdOrName(searchHotel);

        verify(mockHotelRepository, never()).findByName(anyString());
        assertThat(optionalHotel.isPresent(), is(true));
        assertThat(optionalHotel.get().getName(), is(mockHotel.getName()));
    }

    @Test
    public void test_GetHotelByIdOrName_WithNullIdAndExistingName_ShouldReturnMatchedHotel() {
        Hotel mockHotel = new Hotel("Plaza", null, null);

        when(mockHotelRepository.findByName(anyString()))
                .thenReturn(Optional.of(mockHotel));

        Hotel searchHotel = new Hotel("Plaza", null, null);
        final var optionalHotel = hotelService.getHotelByIdOrName(searchHotel);

        verify(mockHotelRepository, never()).findById(any(Long.class));
        assertThat(optionalHotel.isPresent(), is(true));
        assertThat(optionalHotel.get().getName(), is(mockHotel.getName()));
    }
}