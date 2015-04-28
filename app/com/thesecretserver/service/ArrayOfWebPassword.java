
package com.thesecretserver.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfWebPassword complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfWebPassword">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="WebPassword" type="{urn:thesecretserver.com}WebPassword" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfWebPassword", propOrder = {
    "webPassword"
})
public class ArrayOfWebPassword {

    @XmlElement(name = "WebPassword", nillable = true)
    protected List<WebPassword> webPassword;

    /**
     * Gets the value of the webPassword property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the webPassword property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWebPassword().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WebPassword }
     * 
     * 
     */
    public List<WebPassword> getWebPassword() {
        if (webPassword == null) {
            webPassword = new ArrayList<WebPassword>();
        }
        return this.webPassword;
    }

}
