package com.group1.Care_Koi_System.service;

import com.group1.Care_Koi_System.dto.KoiFish.KoiFishResponse;
import com.group1.Care_Koi_System.dto.Ticket.TicketRequest;
import com.group1.Care_Koi_System.dto.Ticket.TicketResponse;
import com.group1.Care_Koi_System.entity.*;
import com.group1.Care_Koi_System.exceptionhandler.ErrorCode;
import com.group1.Care_Koi_System.exceptionhandler.ResponseException;
import com.group1.Care_Koi_System.exceptionhandler.SystemException;
import com.group1.Care_Koi_System.repository.KoiFishRepository;
import com.group1.Care_Koi_System.repository.PondRepository;
import com.group1.Care_Koi_System.repository.TicketRepository;
import com.group1.Care_Koi_System.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private PondRepository pondRepository;

    @Autowired
    private KoiFishRepository koiFishRepository;

    @Autowired
    private AccountUtils accountUtils;

    @Autowired
    private TicketRepository ticketRepository;


    public ResponseEntity<TicketResponse> createTicket(TicketRequest ticketRequest, int pondId, int fishId) {
        try {
            Account account = accountUtils.getCurrentAccount();
            if(account == null){
                throw new SystemException(ErrorCode.NOT_LOGIN);
            }

            Ticket ticket = new Ticket();
            ticket.setName(ticketRequest.getName());

            Ponds pond = pondRepository.findById(pondId);
            KoiFish koiFish = koiFishRepository.findById(fishId);

            if (pond == null || koiFish == null) {
                throw new SystemException(ErrorCode.INVALIDPONDANDFISH);
            }

            ticket.setPonds(pond);
            ticket.setKoiFish(koiFish);
            ticket.setText(ticketRequest.getText());
            ticket.setResolved(false);
            ticketRepository.save(ticket);
            TicketResponse response = new TicketResponse(
                    ticket.getId(),
                    ticket.getName(),
                    ticket.getPonds().getNamePond(),
                    ticket.getKoiFish().getFishName(),
                    ticket.getText()
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (SystemException ex) {
            ErrorCode errorCode = ex.getErrorCode();
            TicketResponse respon = new TicketResponse(ex.getMessage());
            return new ResponseEntity<>(respon, errorCode.getHttpStatus());
        }

    }

    public ResponseEntity<?> getAllTickets() {
        try{
            Account account = accountUtils.getCurrentAccount();
            if(account == null){
                throw new SystemException(ErrorCode.NOT_LOGIN);
            }
            List<Ticket> listTicket = ticketRepository.findByIsResolvedFalseAndIsDeletedFalse();

            if(listTicket.isEmpty()){
                throw new SystemException(ErrorCode.EMPTY);
            }
            List<TicketResponse> tickets = new ArrayList<>();
            for(Ticket ticket: listTicket){

                tickets.add(new TicketResponse(
                        ticket.getId(),
                        ticket.getName(),
                        ticket.getPonds().getNamePond(),
                        ticket.getKoiFish().getFishName(),
                        ticket.getText()
                ));
            }
            return new ResponseEntity<>(tickets, HttpStatus.OK);
    } catch (SystemException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        TicketResponse respon = new TicketResponse(ex.getMessage());
        return new ResponseEntity<>(respon, errorCode.getHttpStatus());
    }
    }
}
