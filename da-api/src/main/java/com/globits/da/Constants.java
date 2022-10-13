package com.globits.da;

import java.util.regex.Pattern;

public class Constants {

    public static final int MIN_LENGTH_CODE = 6;
    public static final int MAX_LENGTH_CODE = 10;
    public static final int MAX_LENGTH_PHONE = 11;
    public static final String SPACE = " ";
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final String UPDATE = "update";
    public static final String INSERT = "insert";

    public static final String DELETE = "delete";
    public static final String REGEX_VALID_PHONE = "[0-9]+[\\.]?[0-9]*";
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static String title[] = {"Id", "Code", "NAME", "EMAIL", "Phone", "Age", "ProvinceId", "DistrictId", "CommuneId"};

    public static enum StaffType {
        Sale(1), // nhân viên bán hàng
        Cashier(2), // nhân viên thu ngân
        Other(3)// khác
        ;
        private Integer value;

        private StaffType(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    public static enum ChannelAds {// kenh quang cao
        Webiste(1), // website
        Contextual_Advertiser(2), // khen hquang cao
        Social_Netword(3), // mang xa hoi
        Youtube_channel(4)// youtube
        ;
        private Integer value;

        private ChannelAds(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    public static enum Social_Netword {// kenh quang cao
        Facebook(1), // website
        Zalo(2), // khen hquang cao
        Tiktok(3), // mang xa hoi
        Other(4)// youtube
        ;
        private Integer value;

        private Social_Netword(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

}
