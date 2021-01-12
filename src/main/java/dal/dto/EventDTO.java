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
    private String hyperlink;
    private int eventID;

    public EventDTO(){

    }

    public EventDTO(String name, LocalDateTime startDate, LocalDateTime endDate, String address, String info, String hyperlink, int eventID){
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.address = address;
        this.info = info;
        this.eventID = eventID;
        this.hyperlink = hyperlink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate.toString();
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate.toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime();
    }

    public String getEndDate() {
        return endDate.toString();
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

    public String getHyperlink() {
        return hyperlink;
    }

    public void setHyperlink(String hyperlink) {
        this.hyperlink = hyperlink;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }
}
