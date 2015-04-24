
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ApprovalInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ApprovalInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IsApproved" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Responder" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ResponseDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="ResponseComment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ExpirationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApprovalInfo", propOrder = {
    "isApproved",
    "responder",
    "responseDate",
    "responseComment",
    "expirationDate"
})
public class ApprovalInfo {

    @XmlElement(name = "IsApproved")
    protected boolean isApproved;
    @XmlElement(name = "Responder")
    protected String responder;
    @XmlElement(name = "ResponseDate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar responseDate;
    @XmlElement(name = "ResponseComment")
    protected String responseComment;
    @XmlElement(name = "ExpirationDate", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar expirationDate;

    /**
     * Gets the value of the isApproved property.
     * 
     */
    public boolean isIsApproved() {
        return isApproved;
    }

    /**
     * Sets the value of the isApproved property.
     * 
     */
    public void setIsApproved(boolean value) {
        this.isApproved = value;
    }

    /**
     * Gets the value of the responder property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponder() {
        return responder;
    }

    /**
     * Sets the value of the responder property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponder(String value) {
        this.responder = value;
    }

    /**
     * Gets the value of the responseDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getResponseDate() {
        return responseDate;
    }

    /**
     * Sets the value of the responseDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setResponseDate(XMLGregorianCalendar value) {
        this.responseDate = value;
    }

    /**
     * Gets the value of the responseComment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseComment() {
        return responseComment;
    }

    /**
     * Sets the value of the responseComment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseComment(String value) {
        this.responseComment = value;
    }

    /**
     * Gets the value of the expirationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the value of the expirationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setExpirationDate(XMLGregorianCalendar value) {
        this.expirationDate = value;
    }

}
