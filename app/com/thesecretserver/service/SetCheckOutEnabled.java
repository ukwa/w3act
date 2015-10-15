
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
     * Ruft den Wert der token-Eigenschaft ab.
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
     * Legt den Wert der token-Eigenschaft fest.
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
     * Ruft den Wert der secretId-Eigenschaft ab.
     * 
     */
    public int getSecretId() {
        return secretId;
    }

    /**
     * Legt den Wert der secretId-Eigenschaft fest.
     * 
     */
    public void setSecretId(int value) {
        this.secretId = value;
    }

    /**
     * Ruft den Wert der setCheckOut-Eigenschaft ab.
     * 
     */
    public boolean isSetCheckOut() {
        return setCheckOut;
    }

    /**
     * Legt den Wert der setCheckOut-Eigenschaft fest.
     * 
     */
    public void setSetCheckOut(boolean value) {
        this.setCheckOut = value;
    }

    /**
     * Ruft den Wert der setPasswordChangeOnCheckIn-Eigenschaft ab.
     * 
     */
    public boolean isSetPasswordChangeOnCheckIn() {
        return setPasswordChangeOnCheckIn;
    }

    /**
     * Legt den Wert der setPasswordChangeOnCheckIn-Eigenschaft fest.
     * 
     */
    public void setSetPasswordChangeOnCheckIn(boolean value) {
        this.setPasswordChangeOnCheckIn = value;
    }

    /**
     * Ruft den Wert der checkOutInterval-Eigenschaft ab.
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
     * Legt den Wert der checkOutInterval-Eigenschaft fest.
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
