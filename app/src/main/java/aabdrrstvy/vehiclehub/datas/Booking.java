package aabdrrstvy.vehiclehub.datas;

public class Booking {
    String startDate, endDate;
    Double amount;
    String ProId, Number;

    public Booking(String startDate, String endDate, Double amount, String proId, String number) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        ProId = proId;
        Number = number;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getProId() {
        return ProId;
    }

    public void setProId(String proId) {
        ProId = proId;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }
}
