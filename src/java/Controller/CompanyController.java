package Controller;

import Common.FileUpload;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import dao.CustomerOrderDao;
import dao.DelivererDao;
import dao.ItemDao;
import dao.ItemImageDao;
import dao.UserDao;
import domain.Company;
import domain.CustomerOrder;
import domain.Deliverer;
import domain.Item;
import domain.ItemImage;
import domain.User;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;

@SessionScoped
@ManagedBean
public class CompanyController {

    private Item item = new Item();

    private List<String> choosenImage = new ArrayList<>();

    private List<Item> items = new ArrayList<>();

    private List<ItemImage> itemImages = new ArrayList<>();

    private String phone = new String();

    private Deliverer deliverer = new Deliverer();

    private List<Deliverer> deliverers = new ArrayList<>();

    private User user = new User();

    private String password = new String();

    private User loggedInUser = new User();

    private ItemImage chosenItemImage = new ItemImage();

    private List<CustomerOrder> customerOrders = new ArrayList<>();

    private List<CustomerOrder> detailedOrders = new ArrayList<>(); 
    
    private String searchName = new String();
    
    private CustomerOrder chosenCustomerOrder = new CustomerOrder();
    
    private Double totalPrice = 0.0;
    
    private Double totalPriceInUSD = 0.0;
    
    @PostConstruct
    public void init() {
        companyProductImageInit();
    }

    public void companyProductImageInit() {
        userInit();
        itemImages = new ItemImageDao().findByCompany(loggedInUser.getCompany());
        deliverers = new DelivererDao().findByCompany(loggedInUser.getCompany());
        customerOrders = new ItemDao().findByCompany(loggedInUser.getCompany());
        
    }
    
    public void search(){
        itemImages = new ItemImageDao().findByItemName(loggedInUser.getCompany(), searchName);
    }
    
    public void refresh(){
        itemImages = new ItemImageDao().findByCompany(loggedInUser.getCompany());
    }
    
    public String exploreOrder(String orderNo){
        detailedOrders = new CustomerOrderDao().findByOrderNo(orderNo);
        return "explore.xhtml?faces-redirect=true";
    }
    
    public String exploreOrderRefund(CustomerOrder cust){
        chosenCustomerOrder = cust;
        return "refund.xhtml?faces-redirect=true";
    }
    
    public String exploreOrderPay(CustomerOrder cust){
        chosenCustomerOrder = cust;
        totalPrice = chosenCustomerOrder.getCart().getItem().getUnitPrice() * chosenCustomerOrder.getCart().getQuantity();
        totalPriceInUSD = totalPrice / 948;
        return "pay.xhtml?faces-redirect=true";
    }
    
    public String approveRefund(){
        totalPrice = chosenCustomerOrder.getCart().getItem().getUnitPrice() * chosenCustomerOrder.getCart().getQuantity();
        totalPriceInUSD = totalPrice / 948;
        chosenCustomerOrder.setStatus("RefundCompleted");
        new CustomerOrderDao().Update(chosenCustomerOrder);
        Item it = chosenCustomerOrder.getCart().getItem();
        it.setQuantity(it.getQuantity() + chosenCustomerOrder.getQuantity());
        new ItemDao().Update(it);
        customerOrders = new ItemDao().findByCompany(loggedInUser.getCompany());
        return "pay.xhtml?faces-redirect=true";
    }
         
    public void rejectRefund(){
        chosenCustomerOrder.setStatus("RefundRejected");
        new CustomerOrderDao().Update(chosenCustomerOrder);
        customerOrders = new ItemDao().findByCompany(loggedInUser.getCompany());
    }
    
    public void userInit() {
        loggedInUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session"); //hano nuku getting the username

    }

    public void registerItem() {
        if (choosenImage.isEmpty()) {
            FacesContext ct = FacesContext.getCurrentInstance();
            ct.addMessage(null, new FacesMessage("Upload Product Images"));
        } else {
            userInit();
            item.setCompany(loggedInUser.getCompany());
            new ItemDao().register(item);
            for (String x : choosenImage) {
                ItemImage itemImage = new ItemImage();
                itemImage.setImage(x);
                itemImage.setItem(item);
                new ItemImageDao().register(itemImage);
            }
            choosenImage.clear();
            companyProductImageInit();
            items = new ItemDao().FindAll(Item.class);
//            itemImages = new ItemImageDao().FindAll(ItemImage.class);

            item = new Item();

            FacesContext ct = FacesContext.getCurrentInstance();
            ct.addMessage(null, new FacesMessage("Product Registered"));
        }
    }

    public void updateItem(ItemImage im) {
        new ItemDao().Update(im.getItem());

        FacesContext ct = FacesContext.getCurrentInstance();
        ct.addMessage(null, new FacesMessage("Product Updated"));
    }
    
    

    public void registerDeliverer() throws Exception {
        userInit();
        deliverer.setCompany(loggedInUser.getCompany());
        new DelivererDao().register(deliverer);

        user.setAccess("Deliverer");
        user.setStatus("Active");
        user.setDeliverer(deliverer);
        user.setPassword((password));
        new UserDao().register(user);

        user = new User();
        deliverer = new Deliverer();
        companyProductImageInit();
        FacesContext ct = FacesContext.getCurrentInstance();
        ct.addMessage(null, new FacesMessage("Deliverer Registered"));
    }

    public void Upload(FileUploadEvent event) {
        choosenImage.add(new FileUpload().Upload(event, "C:\\Users\\nizey\\OneDrive\\Documents\\NetBeansProjects\\Thesis\\madeinrw\\web\\uploads\\item\\"));
    }

    public void generateOrdersPdf() throws FileNotFoundException, DocumentException, BadElementException, IOException, Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        Document document = new Document();
        Rectangle rect = new Rectangle(20, 20, 580, 500);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance((com.lowagie.text.Document) document, baos);
        writer.setBoxSize("art", rect);
        document.setPageSize(rect);
        if (!document.isOpen()) {
            document.open();
        }
        userInit();
        Company xx = loggedInUser.getCompany();
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("\\uploads");
        path = path.substring(0, path.indexOf("\\build"));
        path = path + "\\web\\uploads\\company\\" + xx.getImage();
        Image image = Image.getInstance(path);
        image.scaleAbsolute(50, 50);
        image.setAlignment(Element.ALIGN_LEFT);
        Paragraph title = new Paragraph();
        //BEGIN page
        title.add(image);
        document.add(title);
        Font font0 = new Font(Font.TIMES_ROMAN, 9, Font.NORMAL);
        Font font1 = new Font(Font.TIMES_ROMAN, 9, Font.ITALIC, new Color(90, 255, 20));
        Font font2 = new Font(Font.TIMES_ROMAN, 9, Font.NORMAL, new Color(0, 0, 0));
        Font font5 = new Font(Font.TIMES_ROMAN, 10, Font.ITALIC, new Color(0, 0, 0));
        Font colorFont = new Font(Font.TIMES_ROMAN, 10, Font.BOLD, new Color(0, 0, 0));
        Font font6 = new Font(Font.TIMES_ROMAN, 9, Font.NORMAL);
        document.add(new Paragraph(loggedInUser.getCompany().getName()+" PRODUCTS\n"));
        document.add(new Paragraph(loggedInUser.getCompany().getLocation() +" \n", font0));
        document.add(new Paragraph("TIN No: "+loggedInUser.getCompany().getTinNumber()+" \n", font0));
        document.add(new Paragraph("KIGALI-RWANDA\n\n", font0));
        Paragraph p = new Paragraph("Orders Reports\n", colorFont);
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        document.add(new Paragraph("\n"));
        PdfPTable tables = new PdfPTable(6);
        tables.setWidthPercentage(100);
        PdfPCell cell1s = new PdfPCell(new Phrase("#", font2));
        cell1s.setBorder(Rectangle.BOTTOM);
        tables.addCell(cell1s);
        PdfPCell cell1 = new PdfPCell(new Phrase("Product Name", font2));
        cell1.setBorder(Rectangle.BOTTOM);
        tables.addCell(cell1);
        PdfPCell cell = new PdfPCell(new Phrase("Quantity", font2));
        cell.setBorder(Rectangle.BOTTOM);
        tables.addCell(cell);
        PdfPCell c1 = new PdfPCell(new Phrase("Unit Price", font2));
        c1.setBorder(Rectangle.BOTTOM);
        tables.addCell(c1);
        PdfPCell c2 = new PdfPCell(new Phrase("Total Price", font2));
        c2.setBorder(Rectangle.BOTTOM);
        tables.addCell(c2);
        
        PdfPCell c3 = new PdfPCell(new Phrase("Status", font2));
        c1.setBorder(Rectangle.BOTTOM);
        tables.addCell(c3);
        
        tables.setHeaderRows(1);
        PdfPCell pdfc5;
        PdfPCell pdfc1;
        PdfPCell pdfc3;
        PdfPCell pdfc2;
        PdfPCell pdfc4;
        PdfPCell pdfc6;
        PdfPCell pdfc7;
        int i = 1;
        DecimalFormat dcf = new DecimalFormat("###,###,###");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        for (CustomerOrder x : customerOrders) {
            pdfc1 = new PdfPCell(new Phrase(i + "", font6));
            pdfc1.setBorder(Rectangle.BOX);
            tables.addCell(pdfc1);
            pdfc5 = new PdfPCell(new Phrase(x.getCart().getItem().getName(), font6));
            pdfc5.setBorder(Rectangle.BOX);
            tables.addCell(pdfc5);
            pdfc3 = new PdfPCell(new Phrase(x.getQuantity() + "", font6));
            pdfc3.setBorder(Rectangle.BOX);
            tables.addCell(pdfc3);
            pdfc4 = new PdfPCell(new Phrase(x.getCart().getItem().getUnitPrice() + "", font6));
            pdfc4.setBorder(Rectangle.BOX);
            tables.addCell(pdfc4);

            pdfc6 = new PdfPCell(new Phrase(x.getCart().getItem().getUnitPrice() * x.getQuantity() + "", font6));
            pdfc6.setBorder(Rectangle.BOX);
            tables.addCell(pdfc6);
            
            pdfc7 = new PdfPCell(new Phrase(x.getStatus() + "", font6));
            pdfc7.setBorder(Rectangle.BOX);
            tables.addCell(pdfc7);

            i++;
        }
        document.add(tables);
        Paragraph par = new Paragraph("\n\nPrinted On: " + sdf.format(new Date()), font1);
        par.setAlignment(Element.ALIGN_RIGHT);
        document.add(par);
        document.close();
        String fileName = "Report_" + new Date().getTime() / (1000 * 3600 * 24);
        writePDFToResponse(context.getExternalContext(), baos, fileName);
        context.responseComplete();
    }

    
    public void generateProductsPdf() throws FileNotFoundException, DocumentException, BadElementException, IOException, Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        Document document = new Document();
        Rectangle rect = new Rectangle(20, 20, 580, 500);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance((com.lowagie.text.Document) document, baos);
        writer.setBoxSize("art", rect);
        document.setPageSize(rect);
        if (!document.isOpen()) {
            document.open();
        }
        userInit();
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("\\uploads");
        path = path.substring(0, path.indexOf("\\build"));
        path = path + "\\web\\uploads\\company\\" + loggedInUser.getCompany().getImage();
        Image image = Image.getInstance(path);
        image.scaleAbsolute(50, 50);
        image.setAlignment(Element.ALIGN_LEFT);
        Paragraph title = new Paragraph();
        //BEGIN page
        title.add(image);
        document.add(title);
        Font font0 = new Font(Font.TIMES_ROMAN, 9, Font.NORMAL);
        Font font1 = new Font(Font.TIMES_ROMAN, 9, Font.ITALIC, new Color(90, 255, 20));
        Font font2 = new Font(Font.TIMES_ROMAN, 9, Font.NORMAL, new Color(0, 0, 0));
        Font font5 = new Font(Font.TIMES_ROMAN, 10, Font.ITALIC, new Color(0, 0, 0));
        Font colorFont = new Font(Font.TIMES_ROMAN, 10, Font.BOLD, new Color(0, 0, 0));
        Font font6 = new Font(Font.TIMES_ROMAN, 9, Font.NORMAL);
        document.add(new Paragraph(loggedInUser.getCompany().getName()+" PRODUCTS\n"));
        document.add(new Paragraph(loggedInUser.getCompany().getLocation() +" \n", font0));
        document.add(new Paragraph("TIN No: "+loggedInUser.getCompany().getTinNumber()+" \n", font0));
        document.add(new Paragraph("KIGALI-RWANDA\n\n", font0));
        Paragraph p = new Paragraph("Products Report\n", colorFont);
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        document.add(new Paragraph("\n"));
        PdfPTable tables = new PdfPTable(6);
        tables.setWidthPercentage(100);
        PdfPCell cell1s = new PdfPCell(new Phrase("#", font2));
        cell1s.setBorder(Rectangle.BOTTOM);
        tables.addCell(cell1s);
        PdfPCell cell1 = new PdfPCell(new Phrase("Product Name", font2));
        cell1.setBorder(Rectangle.BOTTOM);
        tables.addCell(cell1);
        PdfPCell cell = new PdfPCell(new Phrase("Weight", font2));
        cell.setBorder(Rectangle.BOTTOM);
        tables.addCell(cell);
        PdfPCell c1 = new PdfPCell(new Phrase("Unit Price", font2));
        c1.setBorder(Rectangle.BOTTOM);
        tables.addCell(c1);
        PdfPCell c2 = new PdfPCell(new Phrase("Quantity", font2));
        c2.setBorder(Rectangle.BOTTOM);
        tables.addCell(c2);
        
        PdfPCell c3 = new PdfPCell(new Phrase("Category", font2));
        c1.setBorder(Rectangle.BOTTOM);
        tables.addCell(c3);
        
        tables.setHeaderRows(1);
        PdfPCell pdfc5;
        PdfPCell pdfc1;
        PdfPCell pdfc3;
        PdfPCell pdfc2;
        PdfPCell pdfc4;
        PdfPCell pdfc6;
        PdfPCell pdfc7;
        int i = 1;
        DecimalFormat dcf = new DecimalFormat("###,###,###");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        
        for (ItemImage x : itemImages) {
            pdfc1 = new PdfPCell(new Phrase(i + "", font6));
            pdfc1.setBorder(Rectangle.BOX);
            tables.addCell(pdfc1);
            pdfc5 = new PdfPCell(new Phrase(x.getItem().getName(), font6));
            pdfc5.setBorder(Rectangle.BOX);
            tables.addCell(pdfc5);
            pdfc3 = new PdfPCell(new Phrase(x.getItem().getPackageType() + "", font6));
            pdfc3.setBorder(Rectangle.BOX);
            tables.addCell(pdfc3);
            pdfc4 = new PdfPCell(new Phrase(x.getItem().getUnitPrice() + "", font6));
            pdfc4.setBorder(Rectangle.BOX);
            tables.addCell(pdfc4);

            pdfc6 = new PdfPCell(new Phrase(x.getItem().getQuantity() + "", font6));
            pdfc6.setBorder(Rectangle.BOX);
            tables.addCell(pdfc6);
            
            pdfc7 = new PdfPCell(new Phrase(x.getItem().getType(), font6));
            pdfc7.setBorder(Rectangle.BOX);
            tables.addCell(pdfc7);

            i++;
        }
        document.add(tables);
        Paragraph par = new Paragraph("\n\nPrinted On: " + sdf.format(new Date()), font1);
        par.setAlignment(Element.ALIGN_RIGHT);
        document.add(par);
        document.close();
        String fileName = "Report_" + new Date().getTime() / (1000 * 3600 * 24);
        writePDFToResponse(context.getExternalContext(), baos, fileName);
        context.responseComplete();
    }

    private void writePDFToResponse(ExternalContext externalContext, ByteArrayOutputStream baos, String fileName) throws IOException {
        externalContext.responseReset();
        externalContext.setResponseContentType("application/pdf");
        externalContext.setResponseHeader("Expires", "0");
        externalContext.setResponseHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        externalContext.setResponseHeader("Pragma", "public");
        externalContext.setResponseHeader("Content-disposition", "attachment;filename=" + fileName + ".pdf");
        externalContext.setResponseContentLength(baos.size());
        OutputStream out = externalContext.getResponseOutputStream();
        baos.writeTo(out);
        externalContext.responseFlushBuffer();
    }
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public List<String> getChoosenImage() {
        return choosenImage;
    }

    public void setChoosenImage(List<String> choosenImage) {
        this.choosenImage = choosenImage;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<ItemImage> getItemImages() {
        return itemImages;
    }

    public void setItemImages(List<ItemImage> itemImages) {
        this.itemImages = itemImages;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Deliverer getDeliverer() {
        return deliverer;
    }

    public void setDeliverer(Deliverer deliverer) {
        this.deliverer = deliverer;
    }

    public List<Deliverer> getDeliverers() {
        return deliverers;
    }

    public void setDeliverers(List<Deliverer> deliverers) {
        this.deliverers = deliverers;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public ItemImage getChosenItemImage() {
        return chosenItemImage;
    }

    public void setChosenItemImage(ItemImage chosenItemImage) {
        this.chosenItemImage = chosenItemImage;
    }

    public List<CustomerOrder> getCustomerOrders() {
        return customerOrders;
    }

    public void setCustomerOrders(List<CustomerOrder> customerOrders) {
        this.customerOrders = customerOrders;
    }

    public List<CustomerOrder> getDetailedOrders() {
        return detailedOrders;
    }

    public void setDetailedOrders(List<CustomerOrder> detailedOrders) {
        this.detailedOrders = detailedOrders;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public CustomerOrder getChosenCustomerOrder() {
        return chosenCustomerOrder;
    }

    public void setChosenCustomerOrder(CustomerOrder chosenCustomerOrder) {
        this.chosenCustomerOrder = chosenCustomerOrder;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getTotalPriceInUSD() {
        return totalPriceInUSD;
    }

    public void setTotalPriceInUSD(Double totalPriceInUSD) {
        this.totalPriceInUSD = totalPriceInUSD;
    }

}
