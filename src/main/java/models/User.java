package models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User模型类，用于表示JSONPlaceholder API中的User数据结构
 */
public class User {
    @JsonProperty("id")
    private Integer id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("address")
    private Address address;
    
    @JsonProperty("phone")
    private String phone;
    
    @JsonProperty("website")
    private String website;
    
    @JsonProperty("company")
    private Company company;
    
    // 默认构造函数
    public User() {}
    
    // 带参数的构造函数
    public User(String name, String username, String email) {
        this.name = name;
        this.username = username;
        this.email = email;
    }
    
    // Getter和Setter方法
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Address getAddress() {
        return address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getWebsite() {
        return website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
    
    public Company getCompany() {
        return company;
    }
    
    public void setCompany(Company company) {
        this.company = company;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", address=" + address +
                ", phone='" + phone + '\'' +
                ", website='" + website + '\'' +
                ", company=" + company +
                '}';
    }
    
    // 内部类：地址
    public static class Address {
        @JsonProperty("street")
        private String street;
        
        @JsonProperty("suite")
        private String suite;
        
        @JsonProperty("city")
        private String city;
        
        @JsonProperty("zipcode")
        private String zipcode;
        
        @JsonProperty("geo")
        private Geo geo;
        
        // Getter和Setter方法
        public String getStreet() {
            return street;
        }
        
        public void setStreet(String street) {
            this.street = street;
        }
        
        public String getSuite() {
            return suite;
        }
        
        public void setSuite(String suite) {
            this.suite = suite;
        }
        
        public String getCity() {
            return city;
        }
        
        public void setCity(String city) {
            this.city = city;
        }
        
        public String getZipcode() {
            return zipcode;
        }
        
        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }
        
        public Geo getGeo() {
            return geo;
        }
        
        public void setGeo(Geo geo) {
            this.geo = geo;
        }
        
        @Override
        public String toString() {
            return "Address{" +
                    "street='" + street + '\'' +
                    ", suite='" + suite + '\'' +
                    ", city='" + city + '\'' +
                    ", zipcode='" + zipcode + '\'' +
                    ", geo=" + geo +
                    '}';
        }
    }
    
    // 内部类：地理位置
    public static class Geo {
        @JsonProperty("lat")
        private String lat;
        
        @JsonProperty("lng")
        private String lng;
        
        public String getLat() {
            return lat;
        }
        
        public void setLat(String lat) {
            this.lat = lat;
        }
        
        public String getLng() {
            return lng;
        }
        
        public void setLng(String lng) {
            this.lng = lng;
        }
        
        @Override
        public String toString() {
            return "Geo{" +
                    "lat='" + lat + '\'' +
                    ", lng='" + lng + '\'' +
                    '}';
        }
    }
    
    // 内部类：公司
    public static class Company {
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("catchPhrase")
        private String catchPhrase;
        
        @JsonProperty("bs")
        private String bs;
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getCatchPhrase() {
            return catchPhrase;
        }
        
        public void setCatchPhrase(String catchPhrase) {
            this.catchPhrase = catchPhrase;
        }
        
        public String getBs() {
            return bs;
        }
        
        public void setBs(String bs) {
            this.bs = bs;
        }
        
        @Override
        public String toString() {
            return "Company{" +
                    "name='" + name + '\'' +
                    ", catchPhrase='" + catchPhrase + '\'' +
                    ", bs='" + bs + '\'' +
                    '}';
        }
    }
}