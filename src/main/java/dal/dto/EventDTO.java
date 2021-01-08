package dal.dto;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class EventDTO {
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String address;
    private String info;
    private int eventID;

    public EventDTO(){

    }

    public EventDTO(String name, LocalDateTime startDate, LocalDateTime endDate, String address, String info, int eventID){
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.address = address;
        this.info = info;
        this.eventID = eventID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate.toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime();
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate.toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }
}
