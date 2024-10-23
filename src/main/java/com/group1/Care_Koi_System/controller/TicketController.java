package com.group1.Care_Koi_System.controller;


import com.group1.Care_Koi_System.dto.Ticket.TicketRequest;
import com.group1.Care_Koi_System.dto.Ticket.TicketResponse;
import com.group1.Care_Koi_System.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("**")
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/create-ticket/{pondID}/{fishID}")
    public ResponseEntity<TicketResponse> addTicket(@RequestBody TicketRequest ticketRequest,
                                                    @PathVariable int pondID, @PathVariable int fishID){
        return ticketService.createTicket(ticketRequest, pondID, fishID);
    }

    @PutMapping("/create-ticket/{ticketID}/{pondID}/{fishID}")
    public ResponseEntity<TicketResponse> addTicket(@RequestBody TicketRequest ticketRequest,
                                                    @PathVariable int ticketID,@PathVariable int pondID, @PathVariable int fishID){
        return ticketService.updateTicket( ticketID,ticketRequest, pondID, fishID);
    }

    @GetMapping("get-all-ticket")
    public ResponseEntity<?> getAllTicket(){
        return  ticketService.getAllTickets();
    }
}
