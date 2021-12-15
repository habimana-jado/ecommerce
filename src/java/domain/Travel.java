
package domain;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

/**
 *
 * @author Jean de Dieu HABIMANA @2021
 */
@Entity
public class Travel implements Serializable {
    @Id
    private String travelId = UUID.randomUUID().toString();
    private String location;
    private Double amount;
    private String travelName;
    private String tourOperator;
    private int maxGroupSize;
    private String travelDescription;
    private String ageRange;
    private Boolean accomodation;
    private String accomodationDetails;
    private Boolean guide;
    private String guideDetails;
    private Boolean meals;
    private String mealsDetails;
    private Boolean snacks;
    private String snacksDetails;
    private Boolean photoshoot;
    private String photoshootDetails;
    private Boolean transport;
    private String transportDetails;
    private Boolean insurance;
    private String insuranceDetails;
    private String additionalServices;
    @Enumerated(EnumType.STRING)
    private EStatus status;

    public String getTravelId() {
        return travelId;
    }

    public void setTravelId(String travelId) {
        this.travelId = travelId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTravelName() {
        return travelName;
    }

    public void setTravelName(String travelName) {
        this.travelName = travelName;
    }

    public String getTourOperator() {
        return tourOperator;
    }

    public void setTourOperator(String tourOperator) {
        this.tourOperator = tourOperator;
    }

    public int getMaxGroupSize() {
        return maxGroupSize;
    }

    public void setMaxGroupSize(int maxGroupSize) {
        this.maxGroupSize = maxGroupSize;
    }

    public String getTravelDescription() {
        return travelDescription;
    }

    public void setTravelDescription(String travelDescription) {
        this.travelDescription = travelDescription;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public Boolean getAccomodation() {
        return accomodation;
    }

    public void setAccomodation(Boolean accomodation) {
        this.accomodation = accomodation;
    }

    public String getAccomodationDetails() {
        return accomodationDetails;
    }

    public void setAccomodationDetails(String accomodationDetails) {
        this.accomodationDetails = accomodationDetails;
    }

    public Boolean getGuide() {
        return guide;
    }

    public void setGuide(Boolean guide) {
        this.guide = guide;
    }

    public String getGuideDetails() {
        return guideDetails;
    }

    public void setGuideDetails(String guideDetails) {
        this.guideDetails = guideDetails;
    }

    public Boolean getMeals() {
        return meals;
    }

    public void setMeals(Boolean meals) {
        this.meals = meals;
    }

    public String getMealsDetails() {
        return mealsDetails;
    }

    public void setMealsDetails(String mealsDetails) {
        this.mealsDetails = mealsDetails;
    }

    public Boolean getSnacks() {
        return snacks;
    }

    public void setSnacks(Boolean snacks) {
        this.snacks = snacks;
    }

    public String getSnacksDetails() {
        return snacksDetails;
    }

    public void setSnacksDetails(String snacksDetails) {
        this.snacksDetails = snacksDetails;
    }

    public Boolean getPhotoshoot() {
        return photoshoot;
    }

    public void setPhotoshoot(Boolean photoshoot) {
        this.photoshoot = photoshoot;
    }

    public String getPhotoshootDetails() {
        return photoshootDetails;
    }

    public void setPhotoshootDetails(String photoshootDetails) {
        this.photoshootDetails = photoshootDetails;
    }

    public Boolean getTransport() {
        return transport;
    }

    public void setTransport(Boolean transport) {
        this.transport = transport;
    }

    public String getTransportDetails() {
        return transportDetails;
    }

    public void setTransportDetails(String transportDetails) {
        this.transportDetails = transportDetails;
    }

    public Boolean getInsurance() {
        return insurance;
    }

    public void setInsurance(Boolean insurance) {
        this.insurance = insurance;
    }

    public String getInsuranceDetails() {
        return insuranceDetails;
    }

    public void setInsuranceDetails(String insuranceDetails) {
        this.insuranceDetails = insuranceDetails;
    }

    public String getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(String additionalServices) {
        this.additionalServices = additionalServices;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }
    
    
}
