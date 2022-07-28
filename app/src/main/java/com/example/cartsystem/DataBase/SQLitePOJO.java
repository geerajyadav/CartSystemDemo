package com.example.cartsystem.DataBase;
public class SQLitePOJO {

    String productid;
    String productname;
    String prices;
    String Dprices;
    String imagesurl;
    String discount;
    String Weight;
    int ids;
    int count;
    String subtotal;
    String mrp;
    String title2;

    public int getcount() { return count; }
    public void setcount(int count) { this.count = count; }

    public String getsubtotal() { return subtotal; }
    public void setsubtotal(String subtotal) { this.subtotal = subtotal; }

    public int getIds() { return ids; }
    public void setIds(int ids) { this.ids = ids; }

    public String getId() { return productid; }
    public void setId(String productid) { this.productid = productid; }

    public String getproductname() { return productname; }
    public void setproductname(String productname) { this.productname = productname; }

    public String getprices() { return prices; }
    public void setprices(String prices) { this.prices = prices; }

    public String getDprices() { return Dprices; }
    public void setDprices(String Dprices) { this.Dprices = Dprices; }

    public String getimagesurl() { return imagesurl; }
    public void setimagesurl(String imagesurl) { this.imagesurl = imagesurl; }

    public String getdiscount() { return discount; }
    public void setdiscount(String discount) { this.discount = discount; }


    public String getWeight() { return Weight; }
    public void setWeight(String Weight) { this.Weight = Weight; }

    public String getMrp() { return mrp; }
    public void setMrp(String mrp) { this.mrp = mrp; }
    public String getTitle2() { return title2; }
    public void setTitle2(String title2) { this.title2 = title2; }

    @Override
    public String toString() {
        return "{" + "count:'" + Weight + '\'' + ", id:'" + ids + '\'' + ",price:'"+prices+ '\''+",mrp:'"+mrp+ "'}";    }


}

