
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="token" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="secretId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="setCheckOut" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="setPasswordChangeOnCheckIn" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="checkOutInterval" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "token",
    "secretId",
    "setCheckOut",
    "setPasswordChangeOnCheckIn",
    "checkOutInterval"
})
@XmlRootElement(name = "SetCheckOutEnabled")
public class SetCheckOutEnabled {

    protected String token;
    protected int secretId;
    protected boolean setCheckOut;
    protected boolean setPasswordChangeOnCheckIn;
    @XmlElement(required = true, type = Integer.class, nillable = true)
    protected Integer checkOutInterval;

    /**
     * Gets the value of the token property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the value of the token property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToken(String value) {
        this.token = value;
    }

    /**
     * Gets the value of the secretId property.
     * 
     */
    public int getSecretId() {
        return secretId;
    }

    /**
     * Sets the value of the secretId property.
     * 
     */
    public void setSecretId(int value) {
        this.secretId = value;
    }

    /**
     * Gets the value of the setCheckOut property.
     * 
     */
    public boolean isSetCheckOut() {
        return setCheckOut;
    }

    /**
     * Sets the value of the setCheckOut property.
     * 
     */
    public void setSetCheckOut(boolean value) {
        this.setCheckOut = value;
    }

    /**
     * Gets the value of the setPasswordChangeOnCheckIn property.
     * 
     */
    public boolean isSetPasswordChangeOnCheckIn() {
        return setPasswordChangeOnCheckIn;
    }

    /**
     * Sets the value of the setPasswordChangeOnCheckIn property.
     * 
     */
    public void setSetPasswordChangeOnCheckIn(boolean value) {
        this.setPasswordChangeOnCheckIn = value;
    }

    /**
     * Gets the value of the checkOutInterval property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCheckOutInterval() {
        return checkOutInterval;
    }

    /**
     * Sets the value of the checkOutInterval property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCheckOutInterval(Integer value) {
        this.checkOutInterval = value;
    }

}
