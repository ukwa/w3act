
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SshUserScript complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SshUserScript">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:thesecretserver.com}UserScript">
 *       &lt;sequence>
 *         &lt;element name="AdditionalDataObject" type="{urn:thesecretserver.com}AdditionalDataSshObject" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SshUserScript", propOrder = {
    "additionalDataObject"
})
public class SshUserScript
    extends UserScript
{

    @XmlElement(name = "AdditionalDataObject")
    protected AdditionalDataSshObject additionalDataObject;

    /**
     * Gets the value of the additionalDataObject property.
     * 
     * @return
     *     possible object is
     *     {@link AdditionalDataSshObject }
     *     
     */
    public AdditionalDataSshObject getAdditionalDataObject() {
        return additionalDataObject;
    }

    /**
     * Sets the value of the additionalDataObject property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdditionalDataSshObject }
     *     
     */
    public void setAdditionalDataObject(AdditionalDataSshObject value) {
        this.additionalDataObject = value;
    }

}
