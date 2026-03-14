package ParkingLot.models;

import java.math.BigDecimal;

public class PaymentReceipt {
    private final Ticket ticket;
    private final BigDecimal amount;

    public PaymentReceipt(Ticket ticket, BigDecimal amount){
        this.ticket = ticket;
        this.amount = amount;
    }

    public Ticket getTicket(){
        return ticket;
    }

    public BigDecimal getAmount(){
        return amount;
    }
}
