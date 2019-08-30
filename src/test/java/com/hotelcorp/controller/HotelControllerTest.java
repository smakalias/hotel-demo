package com.hotelcorp.controller;

import com.hotelcorp.business.HotelService;
import com.hotelcorp.data.Hotel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.hotelcorp.utils.Utils.asJsonString;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(HotelController.class)
public class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HotelService hotelService;

    @Test
    public void test_GetAllHotels_WithExistingHotels_ShouldReturnAllHotels() throws Exception {

        when(hotelService.getAllHotels())
                .thenReturn(List.of(
                        new Hotel("Plaza", "address1", null),
                        new Hotel("Hilton", "address2", null))
                );

        mockMvc.perform(get("/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", hasEntry("name", "Plaza")))
                .andExpect(jsonPath("$[1]", hasEntry("name", "Hilton")))
                .andReturn();
    }

    @Test
    public void test_GetAllHotels_WithNoHotels_ShouldReturnEmptyList() throws Exception {

        when(hotelService.getAllHotels())
                .thenReturn(List.of());

        mockMvc.perform(get("/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andReturn();
    }

    @Test
    public void test_GetSingleHotel_WithHotelExisting_ShouldReturnHotel() throws Exception {

        when(hotelService.getHotelById(any(Long.class)))
                .thenReturn(new Hotel("Plaza", "address", null));

        mockMvc.perform(get("/hotels/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasEntry("name", "Plaza")))
                .andReturn();
    }

    @Test
    public void test_GetSingleHotel_WithNonExistingHotel_ShouldReturnStatusNotFound() throws Exception {

        when(hotelService.getHotelById(any(Long.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/hotels/{id}", 1))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void test_Post_WithNewHotel_ShouldReturnHotel() throws Exception {
        var hotel = new Hotel("Plaza", "address", null);

        when(hotelService.createHotel(any(Hotel.class)))
                .thenReturn(hotel);

        mockMvc.perform(post("/hotels")
                .content(asJsonString(hotel))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasEntry("name", "Plaza")))
                .andReturn();
    }

    @Test
    public void test_Post_WithExistingHotel_ShouldReturnStatusConflict() throws Exception {
        var hotel = new Hotel("Plaza", "address", null);

        when(hotelService.createHotel(any(Hotel.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT));

        mockMvc.perform(post("/hotels")
                .content(asJsonString(hotel))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();
    }

    @Test
    public void test_Put_WithExistingHotel_ShouldReturnUpdatedHotel() throws Exception {
        var hotel = new Hotel("Plaza", "new address", null);

        when(hotelService.updateHotel(any(Hotel.class), any(Long.class)))
                .thenReturn(hotel);

        mockMvc.perform(put("/hotels/{id}", 1)
                .content(asJsonString(hotel))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasEntry("name", "Plaza")))
                .andExpect(jsonPath("$", hasEntry("address", "new address")))
                .andReturn();
    }

    @Test
    public void test_Put_WithNonExistingHotel_ShouldReturnStatusNotFound() throws Exception {
        var hotel = new Hotel("Plaza", "new address", null);

        when(hotelService.updateHotel(any(Hotel.class), any(Long.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(put("/hotels/{id}", 1)
                .content(asJsonString(hotel))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void test_Delete_WithExistingHotel_ShouldDeleteHotel() throws Exception {
        doNothing()
                .when(hotelService).deleteHotel(any(Long.class));

        mockMvc.perform(delete("/hotels/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void test_Delete_WithNonExistingHotel_ShouldReturnStatusNotFound() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
                .when(hotelService).deleteHotel(any(Long.class));

        mockMvc.perform(delete("/hotels/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}