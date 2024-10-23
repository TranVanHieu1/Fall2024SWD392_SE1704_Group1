package com.group1.Care_Koi_System.dto.Ticket;


import com.group1.Care_Koi_System.entity.KoiFish;
import com.group1.Care_Koi_System.entity.Ponds;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketRequest {

    private String name;

    private String text;

}
