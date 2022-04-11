package org.lambda.quarkus.model;

public class CardDetails {

    private String sessionid;
    private String accountno;
    private String cardtype;

    @Override
    public String toString() {
        return "CardDetails{" +
                "sessionid='" + sessionid + '\'' +
                ", accountno='" + accountno + '\'' +
                ", cardtype='" + cardtype + '\'' +
                ", cardno='" + cardno + '\'' +
                ", pin=" + pin +
                ", aav=" + aav +
                ", custid='" + custid + '\'' +
                ", cardtypeid=" + cardtypeid +
                '}';
    }

    private String cardno;

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getAccountno() {
        return accountno;
    }

    public void setAccountno(String accountno) {
        this.accountno = accountno;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public int getAav() {
        return aav;
    }

    public void setAav(int aav) {
        this.aav = aav;
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    private int pin;

    public int getCardtypeid() {
        return cardtypeid;
    }

    public void setCardtypeid(int cardtypeid) {
        this.cardtypeid = cardtypeid;
    }

    private int aav;
    private String custid;
    private  int cardtypeid;

}
