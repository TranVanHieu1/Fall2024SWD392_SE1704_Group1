package com.group1.Care_Koi_System.dto.Ticket;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class TicketResponse {

    private int id;

    private String name;

    private String pondName;

    private String fishName;

    private String text;

    private String message;

    public TicketResponse(int id, String name, String pondName, String fishName, String text) {
        this.id = id;
        this.name = name;
        this.pondName = pondName;
        this.fishName = fishName;
        this.text = text;
    }

    public TicketResponse(String message) {
        this.message = message;
    }
}
