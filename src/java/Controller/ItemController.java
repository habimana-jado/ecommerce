
package Controller;

import dao.ItemImageDao;
import domain.EStatus;
import domain.ItemImage;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ItemController {
    
    private List<ItemImage> allItems = new ItemImageDao().FindAll(ItemImage.class);
    private List<ItemImage> crafts = new ItemImageDao().findByItemType("Crafts");
    private List<ItemImage> industrials = new ItemImageDao().findByItemType("Industrials");
    private List<ItemImage> agriculturals = new ItemImageDao().findByItemType("Agricultural");
    private List<ItemImage> electronics = new ItemImageDao().findByItemType("Electronics");
    private List<ItemImage> clothes = new ItemImageDao().findByItemType("Clothes");
    private List<ItemImage> hotels = new ItemImageDao().findByItemType("Hotel");
    private List<ItemImage> softwares = new ItemImageDao().findByItemType("Software");
    private List<ItemImage> books = new ItemImageDao().findByItemType("Book");
    private List<ItemImage> travels = new ItemImageDao().findAvailableTours(EStatus.ACTIVE);

    public List<ItemImage> getAllItems() {
        return allItems;
    }

    public void setAllItems(List<ItemImage> allItems) {
        this.allItems = allItems;
    }

    public List<ItemImage> getCrafts() {
        return crafts;
    }

    public void setCrafts(List<ItemImage> crafts) {
        this.crafts = crafts;
    }

    public List<ItemImage> getIndustrials() {
        return industrials;
    }

    public void setIndustrials(List<ItemImage> industrials) {
        this.industrials = industrials;
    }

    public List<ItemImage> getAgriculturals() {
        return agriculturals;
    }

    public void setAgriculturals(List<ItemImage> agriculturals) {
        this.agriculturals = agriculturals;
    }

    public List<ItemImage> getElectronics() {
        return electronics;
    }

    public void setElectronics(List<ItemImage> electronics) {
        this.electronics = electronics;
    }

    public List<ItemImage> getClothes() {
        return clothes;
    }

    public void setClothes(List<ItemImage> clothes) {
        this.clothes = clothes;
    }

    public List<ItemImage> getHotels() {
        return hotels;
    }

    public void setHotels(List<ItemImage> hotels) {
        this.hotels = hotels;
    }

    public List<ItemImage> getTravels() {
        return travels;
    }

    public void setTravels(List<ItemImage> travels) {
        this.travels = travels;
    }

    public List<ItemImage> getSoftwares() {
        return softwares;
    }

    public void setSoftwares(List<ItemImage> softwares) {
        this.softwares = softwares;
    }

    public List<ItemImage> getBooks() {
        return books;
    }

    public void setBooks(List<ItemImage> books) {
        this.books = books;
    }
        
}
