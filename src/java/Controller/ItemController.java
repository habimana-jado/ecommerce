
package Controller;

import dao.ItemImageDao;
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
    
    
}
