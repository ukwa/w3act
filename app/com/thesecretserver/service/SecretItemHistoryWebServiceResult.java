
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for SecretItemHistoryWebServiceResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SecretItemHistoryWebServiceResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SecretItemHistoryId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="UserId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SecretItemId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SecretId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="ItemValueNew" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ItemValueNew2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SecretItemHistoryWebServiceResult", propOrder = {
    "secretItemHistoryId",
    "userId",
    "secretItemId",
    "secretId",
    "date",
    "itemValueNew",
    "itemValueNew2"
})
public class SecretItemHistoryWebServiceResult {

    @XmlElement(name = "SecretItemHistoryId")
    protected int secretItemHistoryId;
    @XmlElement(name = "UserId")
    protected int userId;
    @XmlElement(name = "SecretItemId")
    protected int secretItemId;
    @XmlElement(name = "SecretId")
    protected int secretId;
    @XmlElement(name = "Date", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar date;
    @XmlElement(name = "ItemValueNew")
    protected String itemValueNew;
    @XmlElement(name = "ItemValueNew2")
    protected String itemValueNew2;

    /**
     * Gets the value of the secretItemHistoryId property.
     * 
     */
    public int getSecretItemHistoryId() {
        return secretItemHistoryId;
    }

    /**
     * Sets the value of the secretItemHistoryId property.
     * 
     */
    public void setSecretItemHistoryId(int value) {
        this.secretItemHistoryId = value;
    }

    /**
     * Gets the value of the userId property.
     * 
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the value of the userId property.
     * 
     */
    public void setUserId(int value) {
        this.userId = value;
    }

    /**
     * Gets the value of the secretItemId property.
     * 
     */
    public int getSecretItemId() {
        return secretItemId;
    }

    /**
     * Sets the value of the secretItemId property.
     * 
     */
    public void setSecretItemId(int value) {
        this.secretItemId = value;
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
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDate(XMLGregorianCalendar value) {
        this.date = value;
    }

    /**
     * Gets the value of the itemValueNew property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemValueNew() {
        return itemValueNew;
    }

    /**
     * Sets the value of the itemValueNew property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemValueNew(String value) {
        this.itemValueNew = value;
    }

    /**
     * Gets the value of the itemValueNew2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemValueNew2() {
        return itemValueNew2;
    }

    /**
     * Sets the value of the itemValueNew2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemValueNew2(String value) {
        this.itemValueNew2 = value;
    }

}
