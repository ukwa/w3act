
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für GetCheckOutStatusResult complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="GetCheckOutStatusResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Errors" type="{urn:thesecretserver.com}ArrayOfString" minOccurs="0"/>
 *         &lt;element name="Secret" type="{urn:thesecretserver.com}Secret" minOccurs="0"/>
 *         &lt;element name="CheckOutMinutesRemaining" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="IsCheckedOut" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="CheckOutUserDisplayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CheckOutUserId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetCheckOutStatusResult", propOrder = {
    "errors",
    "secret",
    "checkOutMinutesRemaining",
    "isCheckedOut",
    "checkOutUserDisplayName",
    "checkOutUserId"
})
public class GetCheckOutStatusResult {

    @XmlElement(name = "Errors")
    protected ArrayOfString errors;
    @XmlElement(name = "Secret")
    protected Secret secret;
    @XmlElement(name = "CheckOutMinutesRemaining")
    protected int checkOutMinutesRemaining;
    @XmlElement(name = "IsCheckedOut")
    protected boolean isCheckedOut;
    @XmlElement(name = "CheckOutUserDisplayName")
    protected String checkOutUserDisplayName;
    @XmlElement(name = "CheckOutUserId")
    protected int checkOutUserId;

    /**
     * Ruft den Wert der errors-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getErrors() {
        return errors;
    }

    /**
     * Legt den Wert der errors-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setErrors(ArrayOfString value) {
        this.errors = value;
    }

    /**
     * Ruft den Wert der secret-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Secret }
     *     
     */
    public Secret getSecret() {
        return secret;
    }

    /**
     * Legt den Wert der secret-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Secret }
     *     
     */
    public void setSecret(Secret value) {
        this.secret = value;
    }

    /**
     * Ruft den Wert der checkOutMinutesRemaining-Eigenschaft ab.
     * 
     */
    public int getCheckOutMinutesRemaining() {
        return checkOutMinutesRemaining;
    }

    /**
     * Legt den Wert der checkOutMinutesRemaining-Eigenschaft fest.
     * 
     */
    public void setCheckOutMinutesRemaining(int value) {
        this.checkOutMinutesRemaining = value;
    }

    /**
     * Ruft den Wert der isCheckedOut-Eigenschaft ab.
     * 
     */
    public boolean isIsCheckedOut() {
        return isCheckedOut;
    }

    /**
     * Legt den Wert der isCheckedOut-Eigenschaft fest.
     * 
     */
    public void setIsCheckedOut(boolean value) {
        this.isCheckedOut = value;
    }

    /**
     * Ruft den Wert der checkOutUserDisplayName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCheckOutUserDisplayName() {
        return checkOutUserDisplayName;
    }

    /**
     * Legt den Wert der checkOutUserDisplayName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCheckOutUserDisplayName(String value) {
        this.checkOutUserDisplayName = value;
    }

    /**
     * Ruft den Wert der checkOutUserId-Eigenschaft ab.
     * 
     */
    public int getCheckOutUserId() {
        return checkOutUserId;
    }

    /**
     * Legt den Wert der checkOutUserId-Eigenschaft fest.
     * 
     */
    public void setCheckOutUserId(int value) {
        this.checkOutUserId = value;
    }

}
