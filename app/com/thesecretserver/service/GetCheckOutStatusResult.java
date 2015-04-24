
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetCheckOutStatusResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
     * Gets the value of the errors property.
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
     * Sets the value of the errors property.
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
     * Gets the value of the secret property.
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
     * Sets the value of the secret property.
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
     * Gets the value of the checkOutMinutesRemaining property.
     * 
     */
    public int getCheckOutMinutesRemaining() {
        return checkOutMinutesRemaining;
    }

    /**
     * Sets the value of the checkOutMinutesRemaining property.
     * 
     */
    public void setCheckOutMinutesRemaining(int value) {
        this.checkOutMinutesRemaining = value;
    }

    /**
     * Gets the value of the isCheckedOut property.
     * 
     */
    public boolean isIsCheckedOut() {
        return isCheckedOut;
    }

    /**
     * Sets the value of the isCheckedOut property.
     * 
     */
    public void setIsCheckedOut(boolean value) {
        this.isCheckedOut = value;
    }

    /**
     * Gets the value of the checkOutUserDisplayName property.
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
     * Sets the value of the checkOutUserDisplayName property.
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
     * Gets the value of the checkOutUserId property.
     * 
     */
    public int getCheckOutUserId() {
        return checkOutUserId;
    }

    /**
     * Sets the value of the checkOutUserId property.
     * 
     */
    public void setCheckOutUserId(int value) {
        this.checkOutUserId = value;
    }

}
