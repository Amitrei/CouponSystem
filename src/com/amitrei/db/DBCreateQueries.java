package com.amitrei.db;
public class DBCreateQueries {

    /**
     * All the creating tables queries
     */

    public static final String CREATE_COMPANIES_TABLE="CREATE TABLE `couponsystem`.`companies` (\n" +
            "  `ID` INT NOT NULL AUTO_INCREMENT,\n" +
            "  `NAME` VARCHAR(45) NOT NULL,\n" +
            "  `EMAIL` VARCHAR(45) NOT NULL,\n" +
            "  `PASSWORD` VARCHAR(45) NOT NULL,\n" +
            "  PRIMARY KEY (`ID`));\n";

    public static final String CREATE_CUSTOMERS_TABLE="CREATE TABLE `couponsystem`.`customers` (\n" +
            "  `ID` INT NOT NULL AUTO_INCREMENT,\n" +
            "  `FIRST_NAME` VARCHAR(45) NOT NULL,\n" +
            "  `LAST_NAME` VARCHAR(45) NOT NULL,\n" +
            "  `EMAIL` VARCHAR(45) NOT NULL,\n" +
            "  `PASSWORD` VARCHAR(45) NOT NULL,\n" +
            "  PRIMARY KEY (`ID`));\n";

    public static final String CREATE_CATEGORIES_TABLE="CREATE TABLE `couponsystem`.`categories` (\n" +
        "  `ID` INT NOT NULL AUTO_INCREMENT,\n" +
        "  `NAME` VARCHAR(45) NOT NULL,\n" +
        "  PRIMARY KEY (`ID`));\n";


    public static final String CREATE_COUPONS_TABLE="CREATE TABLE `couponsystem`.`coupons` (\n" +
            "  `ID` INT NOT NULL AUTO_INCREMENT,\n" +
            "  `COMPANY_ID` INT NOT NULL,\n" +
            "  `CATEGORY_ID` INT NOT NULL,\n" +
            "  `TITLE` VARCHAR(45) NOT NULL,\n" +
            "  `DESCRIPTION` VARCHAR(45) NOT NULL,\n" +
            "  `START_DATE` DATE NOT NULL,\n" +
            "  `END_DATE` DATE NOT NULL,\n" +
            "  `AMOUNT` INT NOT NULL,\n" +
            "  `PRICE` DOUBLE NOT NULL,\n" +
            "  `IMAGE` VARCHAR(45) NOT NULL,\n" +
            "  PRIMARY KEY (`ID`),\n" +
            "  INDEX `CATEGORY_ID_idx` (`CATEGORY_ID` ASC) VISIBLE,\n" +
            "  INDEX `COMPANY_ID_idx` (`COMPANY_ID` ASC) VISIBLE,\n" +
            "  CONSTRAINT `CATEGORY_ID`\n" +
            "    FOREIGN KEY (`CATEGORY_ID`)\n" +
            "    REFERENCES `couponsystem`.`categories` (`ID`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION,\n" +
            "  CONSTRAINT `COMPANY_ID`\n" +
            "    FOREIGN KEY (`COMPANY_ID`)\n" +
            "    REFERENCES `couponsystem`.`companies` (`ID`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION);\n";

    public static final String CREATE_CUSTOMERS_VS_COUPONS_TABLE ="CREATE TABLE `couponsystem`.`customers_vs_coupons` (\n" +
            "  `CUSTOMER_ID` INT NOT NULL,\n" +
            "  `COUPON_ID` INT NOT NULL,\n" +
            "  PRIMARY KEY (`CUSTOMER_ID`, `COUPON_ID`),\n" +
            "  INDEX `COUPON_ID_idx` (`COUPON_ID` ASC) VISIBLE,\n" +
            "  CONSTRAINT `CUSTOMER_ID`\n" +
            "    FOREIGN KEY (`CUSTOMER_ID`)\n" +
            "    REFERENCES `couponsystem`.`customers` (`ID`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION,\n" +
            "  CONSTRAINT `COUPON_ID`\n" +
            "    FOREIGN KEY (`COUPON_ID`)\n" +
            "    REFERENCES `couponsystem`.`coupons` (`ID`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION);\n";

}
