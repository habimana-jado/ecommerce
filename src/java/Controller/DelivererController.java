
package Controller;

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
import dao.UserDao;
import domain.Cart;
import domain.Customer;
import domain.CustomerOrder;
import domain.Deliverer;
import domain.Item;
import domain.ItemImage;
import domain.UserX;
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

@ManagedBean
@SessionScoped
public class DelivererController {
    private Cart cart = new Cart();
    
    private CustomerOrder customerOrder = new CustomerOrder();
    
    private Customer customer = new Customer();
    
    private ItemImage itemImage = new ItemImage();
    
    private Item item = new Item();
    
    private UserX loggedInUser = new UserX();
    
    private List<CustomerOrder> pendingOrders = new ArrayList<>();
    
    private List<CustomerOrder> delivererOrders = new ArrayList<>();
    
    private List<CustomerOrder> orderDetails = new ArrayList<>();
    
    private Deliverer loggedInDeliverer = new Deliverer();
    
    @PostConstruct
    public void init(){
        userInit();
        delivererProductInit();
    }
    
    public void delivererProductInit(){
        userInit();
        pendingOrders = new CustomerOrderDao().findByCompany(loggedInUser.getDeliverer().getCompany(), "Pending");
        delivererOrders = new CustomerOrderDao().findByDeliverer(loggedInUser.getDeliverer());    
    }
    public String exploreOrder(String orderNo){
        orderDetails = new CustomerOrderDao().findByOrderNo(orderNo);
        return "explore.xhtml?faces-redirect=true";
    }
     public void updateAccount(){
        new UserDao().Update(loggedInUser);
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage("Account Updated"));
    }
    
    public void updateProfile(){
        new DelivererDao().Update(loggedInDeliverer);
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage("Profile Updated"));
    }

    
    public void userInit() {
        loggedInUser = (UserX) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session");
        loggedInDeliverer = loggedInUser.getDeliverer();
    }
    
    public void takeCustomerOrder(CustomerOrder cust){
        cust.setStatus("Initialized");
        cust.setDeliverer(loggedInUser.getDeliverer());
        new CustomerOrderDao().Update(cust);
        delivererProductInit();
        
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage("Customer Order Initialized"));
    }
    
    public void completeCustomerOrder(CustomerOrder cust){
        cust.setStatus("Completed");
        new CustomerOrderDao().Update(cust);
        
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage("Customer Order Completed"));
    }

    
    public void generateDeliveryPdf() throws FileNotFoundException, DocumentException, BadElementException, IOException, Exception {
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
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("\\uploads");
        path = path.substring(0, path.indexOf("\\build"));
        path = path + "\\web\\uploads\\company\\" + loggedInUser.getDeliverer().getCompany().getImage();
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
        document.add(new Paragraph(loggedInUser.getDeliverer().getCompany().getName()+" PRODUCTS\n"));
        document.add(new Paragraph(loggedInUser.getDeliverer().getCompany().getLocation() +" \n", font0));
        document.add(new Paragraph("TIN No: "+loggedInUser.getDeliverer().getCompany().getTinNumber()+" \n", font0));
        document.add(new Paragraph("KIGALI-RWANDA\n\n", font0));
        Paragraph p = new Paragraph("Delivery Report\n", colorFont);
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
        
        for (CustomerOrder x : delivererOrders) {
            pdfc1 = new PdfPCell(new Phrase(i + "", font6));
            pdfc1.setBorder(Rectangle.BOX);
            tables.addCell(pdfc1);
            pdfc5 = new PdfPCell(new Phrase(x.getCart().getItem().getName(), font6));
            pdfc5.setBorder(Rectangle.BOX);
            tables.addCell(pdfc5);
            pdfc3 = new PdfPCell(new Phrase(x.getCart().getItem().getPackageType() + "", font6));
            pdfc3.setBorder(Rectangle.BOX);
            tables.addCell(pdfc3);
            pdfc4 = new PdfPCell(new Phrase(x.getCart().getItem().getUnitPrice() + "", font6));
            pdfc4.setBorder(Rectangle.BOX);
            tables.addCell(pdfc4);

            pdfc6 = new PdfPCell(new Phrase(x.getQuantity() + "", font6));
            pdfc6.setBorder(Rectangle.BOX);
            tables.addCell(pdfc6);
            
            pdfc7 = new PdfPCell(new Phrase(x.getStatus(), font6));
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
    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ItemImage getItemImage() {
        return itemImage;
    }

    public void setItemImage(ItemImage itemImage) {
        this.itemImage = itemImage;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public UserX getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(UserX loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public List<CustomerOrder> getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(List<CustomerOrder> pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public List<CustomerOrder> getDelivererOrders() {
        return delivererOrders;
    }

    public void setDelivererOrders(List<CustomerOrder> delivererOrders) {
        this.delivererOrders = delivererOrders;
    }

    public List<CustomerOrder> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<CustomerOrder> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Deliverer getLoggedInDeliverer() {
        return loggedInDeliverer;
    }

    public void setLoggedInDeliverer(Deliverer loggedInDeliverer) {
        this.loggedInDeliverer = loggedInDeliverer;
    }
    
    
}
