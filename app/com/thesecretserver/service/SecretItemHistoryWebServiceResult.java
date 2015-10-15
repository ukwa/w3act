
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java-Klasse für SecretItemHistoryWebServiceResult complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
     * Ruft den Wert der secretItemHistoryId-Eigenschaft ab.
     * 
     */
    public int getSecretItemHistoryId() {
        return secretItemHistoryId;
    }

    /**
     * Legt den Wert der secretItemHistoryId-Eigenschaft fest.
     * 
     */
    public void setSecretItemHistoryId(int value) {
        this.secretItemHistoryId = value;
    }

    /**
     * Ruft den Wert der userId-Eigenschaft ab.
     * 
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Legt den Wert der userId-Eigenschaft fest.
     * 
     */
    public void setUserId(int value) {
        this.userId = value;
    }

    /**
     * Ruft den Wert der secretItemId-Eigenschaft ab.
     * 
     */
    public int getSecretItemId() {
        return secretItemId;
    }

    /**
     * Legt den Wert der secretItemId-Eigenschaft fest.
     * 
     */
    public void setSecretItemId(int value) {
        this.secretItemId = value;
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
     * Ruft den Wert der date-Eigenschaft ab.
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
     * Legt den Wert der date-Eigenschaft fest.
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
     * Ruft den Wert der itemValueNew-Eigenschaft ab.
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
     * Legt den Wert der itemValueNew-Eigenschaft fest.
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
     * Ruft den Wert der itemValueNew2-Eigenschaft ab.
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
     * Legt den Wert der itemValueNew2-Eigenschaft fest.
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
