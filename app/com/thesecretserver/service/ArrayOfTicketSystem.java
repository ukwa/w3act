
package com.thesecretserver.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfTicketSystem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfTicketSystem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TicketSystem" type="{urn:thesecretserver.com}TicketSystem" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfTicketSystem", propOrder = {
    "ticketSystem"
})
public class ArrayOfTicketSystem {

    @XmlElement(name = "TicketSystem", nillable = true)
    protected List<TicketSystem> ticketSystem;

    /**
     * Gets the value of the ticketSystem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ticketSystem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTicketSystem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TicketSystem }
     * 
     * 
     */
    public List<TicketSystem> getTicketSystem() {
        if (ticketSystem == null) {
            ticketSystem = new ArrayList<TicketSystem>();
        }
        return this.ticketSystem;
    }

}
