package com.driver.services;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import com.driver.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HotelService {

    HotelRepository hotelRepository = new HotelRepository();

    public String addHotel(Hotel hotel){
        String ans = "";
        if(hotel == null || hotel.getHotelName() == null){
            return ans;
        }
        else if(hotelRepository.hotelDb.containsKey(hotel.getHotelName())){
            return  ans;
        }
        else{
            hotelRepository.hotelDb.put(hotel.getHotelName(),hotel);
            return "SUCCESS";
        }
    }

    public int addUser(User user){
        hotelRepository.userDb.put(user.getaadharCardNo(),user);
        return user.getaadharCardNo();
    }

    public String getHotelWithMostFacilities(){
        ArrayList<String> hotelName = new ArrayList<>();
        int count = 0;
        for(String hName : hotelRepository.hotelDb.keySet()){
            Hotel hotel = hotelRepository.hotelDb.get(hName);
            int size = hotel.getFacilities().size();
            if(size>0 && size>=count){
                count = size;
                hotelName.add(hName);
            }
        }
        if(hotelName.isEmpty()) return "";
        Collections.sort(hotelName);
        return hotelName.get(0);
    }

    public int bookARoom (Booking booking){
        if(hotelRepository.hotelDb.get(booking.getHotelName()).getAvailableRooms()<booking.getNoOfRooms()){
            return -1;
        }
        UUID uuid = UUID.randomUUID();
        String genUuid = uuid.toString();
        booking.setBookingId(genUuid);
        hotelRepository.bookingDb.put(genUuid,booking);
        int amount = hotelRepository.hotelDb.get(booking.getHotelName()).getPricePerNight()
                *booking.getNoOfRooms();
        booking.setAmountToBePaid(amount);
        return amount;
    }

    public int getBookingsByUser(Integer aadharCard){
        int count = 0;
        for(String bookingId : hotelRepository.bookingDb.keySet()){
            Booking booking = hotelRepository.bookingDb.get(bookingId);
            if(booking.getBookingAadharCard() == aadharCard) count++;
        }
        return count;
    }

    public Hotel updateFacilities (List<Facility> newFacilities, String hotelName){
        Hotel hotel = hotelRepository.hotelDb.get(hotelName);
        List<Facility> alreadyFacility = hotel.getFacilities();
        HashSet<Facility> st = new HashSet<>(alreadyFacility);
        for (Facility facility : newFacilities){
            if (!st.contains(facility)) alreadyFacility.add(facility);
        }
        return hotel;
    }
}
