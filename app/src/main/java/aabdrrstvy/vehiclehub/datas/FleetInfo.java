package aabdrrstvy.vehiclehub.datas;

public class FleetInfo {

    String BrandModel, VUrl, number;
    int rate, rate_ex, rate_late;

    public FleetInfo(String brandModel, String VUrl, String number, int rate, int rate_ex, int rate_late) {
        BrandModel = brandModel;
        this.VUrl = VUrl;
        this.number = number;
        this.rate = rate;
        this.rate_ex = rate_ex;
        this.rate_late = rate_late;
    }

    public String getBrandModel() {
        return BrandModel;
    }

    public void setBrandModel(String brandModel) {
        BrandModel = brandModel;
    }

    public String getVUrl() {
        return VUrl;
    }

    public void setVUrl(String VUrl) {
        this.VUrl = VUrl;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getRate_ex() {
        return rate_ex;
    }

    public void setRate_ex(int rate_ex) {
        this.rate_ex = rate_ex;
    }

    public int getRate_late() {
        return rate_late;
    }

    public void setRate_late(int rate_late) {
        this.rate_late = rate_late;
    }
}
