
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java-Klasse für AuditSecret complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="AuditSecret">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AuditSecretId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SecretId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="DateRecorded" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="Action" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Notes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UserId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SecretName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IpAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ReferenceId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ByUserDisplayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TicketNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuditSecret", propOrder = {
    "auditSecretId",
    "secretId",
    "dateRecorded",
    "action",
    "notes",
    "userId",
    "secretName",
    "ipAddress",
    "referenceId",
    "byUserDisplayName",
    "ticketNumber"
})
public class AuditSecret {

    @XmlElement(name = "AuditSecretId")
    protected int auditSecretId;
    @XmlElement(name = "SecretId")
    protected int secretId;
    @XmlElement(name = "DateRecorded", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateRecorded;
    @XmlElement(name = "Action")
    protected String action;
    @XmlElement(name = "Notes")
    protected String notes;
    @XmlElement(name = "UserId")
    protected int userId;
    @XmlElement(name = "SecretName")
    protected String secretName;
    @XmlElement(name = "IpAddress")
    protected String ipAddress;
    @XmlElement(name = "ReferenceId")
    protected int referenceId;
    @XmlElement(name = "ByUserDisplayName")
    protected String byUserDisplayName;
    @XmlElement(name = "TicketNumber")
    protected String ticketNumber;

    /**
     * Ruft den Wert der auditSecretId-Eigenschaft ab.
     * 
     */
    public int getAuditSecretId() {
        return auditSecretId;
    }

    /**
     * Legt den Wert der auditSecretId-Eigenschaft fest.
     * 
     */
    public void setAuditSecretId(int value) {
        this.auditSecretId = value;
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
     * Ruft den Wert der dateRecorded-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateRecorded() {
        return dateRecorded;
    }

    /**
     * Legt den Wert der dateRecorded-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateRecorded(XMLGregorianCalendar value) {
        this.dateRecorded = value;
    }

    /**
     * Ruft den Wert der action-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAction() {
        return action;
    }

    /**
     * Legt den Wert der action-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAction(String value) {
        this.action = value;
    }

    /**
     * Ruft den Wert der notes-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Legt den Wert der notes-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotes(String value) {
        this.notes = value;
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
     * Ruft den Wert der secretName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecretName() {
        return secretName;
    }

    /**
     * Legt den Wert der secretName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecretName(String value) {
        this.secretName = value;
    }

    /**
     * Ruft den Wert der ipAddress-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Legt den Wert der ipAddress-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpAddress(String value) {
        this.ipAddress = value;
    }

    /**
     * Ruft den Wert der referenceId-Eigenschaft ab.
     * 
     */
    public int getReferenceId() {
        return referenceId;
    }

    /**
     * Legt den Wert der referenceId-Eigenschaft fest.
     * 
     */
    public void setReferenceId(int value) {
        this.referenceId = value;
    }

    /**
     * Ruft den Wert der byUserDisplayName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getByUserDisplayName() {
        return byUserDisplayName;
    }

    /**
     * Legt den Wert der byUserDisplayName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setByUserDisplayName(String value) {
        this.byUserDisplayName = value;
    }

    /**
     * Ruft den Wert der ticketNumber-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTicketNumber() {
        return ticketNumber;
    }

    /**
     * Legt den Wert der ticketNumber-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTicketNumber(String value) {
        this.ticketNumber = value;
    }

}
