
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetTicketSystemsResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetTicketSystemsResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TicketSystems" type="{urn:thesecretserver.com}ArrayOfTicketSystem" minOccurs="0"/>
 *         &lt;element name="Errors" type="{urn:thesecretserver.com}ArrayOfString" minOccurs="0"/>
 *         &lt;element name="Success" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetTicketSystemsResult", propOrder = {
    "ticketSystems",
    "errors",
    "success"
})
public class GetTicketSystemsResult {

    @XmlElement(name = "TicketSystems")
    protected ArrayOfTicketSystem ticketSystems;
    @XmlElement(name = "Errors")
    protected ArrayOfString errors;
    @XmlElement(name = "Success")
    protected boolean success;

    /**
     * Gets the value of the ticketSystems property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfTicketSystem }
     *     
     */
    public ArrayOfTicketSystem getTicketSystems() {
        return ticketSystems;
    }

    /**
     * Sets the value of the ticketSystems property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfTicketSystem }
     *     
     */
    public void setTicketSystems(ArrayOfTicketSystem value) {
        this.ticketSystems = value;
    }

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
     * Gets the value of the success property.
     * 
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the value of the success property.
     * 
     */
    public void setSuccess(boolean value) {
        this.success = value;
    }

}
