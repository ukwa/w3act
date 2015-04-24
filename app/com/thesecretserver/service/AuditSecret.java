
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for AuditSecret complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
     * Gets the value of the auditSecretId property.
     * 
     */
    public int getAuditSecretId() {
        return auditSecretId;
    }

    /**
     * Sets the value of the auditSecretId property.
     * 
     */
    public void setAuditSecretId(int value) {
        this.auditSecretId = value;
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
     * Gets the value of the dateRecorded property.
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
     * Sets the value of the dateRecorded property.
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
     * Gets the value of the action property.
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
     * Sets the value of the action property.
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
     * Gets the value of the notes property.
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
     * Sets the value of the notes property.
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
     * Gets the value of the secretName property.
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
     * Sets the value of the secretName property.
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
     * Gets the value of the ipAddress property.
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
     * Sets the value of the ipAddress property.
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
     * Gets the value of the referenceId property.
     * 
     */
    public int getReferenceId() {
        return referenceId;
    }

    /**
     * Sets the value of the referenceId property.
     * 
     */
    public void setReferenceId(int value) {
        this.referenceId = value;
    }

    /**
     * Gets the value of the byUserDisplayName property.
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
     * Sets the value of the byUserDisplayName property.
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
     * Gets the value of the ticketNumber property.
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
     * Sets the value of the ticketNumber property.
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
