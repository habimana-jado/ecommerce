package Controller;

import dao.ItemImageDao;
import domain.ItemImage;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ItemController {

    private List<ItemImage> clothes = new ArrayList<>();

    private List<ItemImage> beverages = new ArrayList<>();

    private List<ItemImage> foods = new ArrayList<>();

    private List<ItemImage> electronics = new ArrayList<>();

    private List<ItemImage> others = new ArrayList<>();
    
    private ItemImage chosenItemImage = new ItemImage();
    
    private List<ItemImage> itemImages = new ArrayList<>();
    
    @PostConstruct
    public void init() {
        clothes = new ItemImageDao().findByItemType("Clothes");
        foods = new ItemImageDao().findByItemType("Foods");
        beverages = new ItemImageDao().findByItemType("Beverages");
        others = new ItemImageDao().findByItemType("Other");
        electronics = new ItemImageDao().findByItemType("Electronics");
    }
    
    public String navigate() {
        return "login.xhtml?faces-redirect=true";
    }
    
    public String navigateItem(ItemImage itemImage) {
        chosenItemImage = itemImage;
        itemImages = new ItemImageDao().findByItem(itemImage.getItem());
        return "single.xhtml?faces-redirect=true";
    }


    public List<ItemImage> getClothes() {
        return clothes;
    }

    public void setClothes(List<ItemImage> clothes) {
        this.clothes = clothes;
    }

    public List<ItemImage> getBeverages() {
        return beverages;
    }

    public void setBeverages(List<ItemImage> beverages) {
        this.beverages = beverages;
    }

    public List<ItemImage> getFoods() {
        return foods;
    }

    public void setFoods(List<ItemImage> foods) {
        this.foods = foods;
    }

    public List<ItemImage> getElectronics() {
        return electronics;
    }

    public void setElectronics(List<ItemImage> electronics) {
        this.electronics = electronics;
    }

    public List<ItemImage> getOthers() {
        return others;
    }

    public void setOthers(List<ItemImage> others) {
        this.others = others;
    }

    public ItemImage getChosenItemImage() {
        return chosenItemImage;
    }

    public void setChosenItemImage(ItemImage chosenItemImage) {
        this.chosenItemImage = chosenItemImage;
    }

    public List<ItemImage> getItemImages() {
        return itemImages;
    }

    public void setItemImages(List<ItemImage> itemImages) {
        this.itemImages = itemImages;
    }

}
