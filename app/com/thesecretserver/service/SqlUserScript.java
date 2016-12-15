
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SqlUserScript complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SqlUserScript">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:thesecretserver.com}UserScript">
 *       &lt;sequence>
 *         &lt;element name="AdditionalDataObject" type="{urn:thesecretserver.com}AdditionalDataSqlObject" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SqlUserScript", propOrder = {
    "additionalDataObject"
})
public class SqlUserScript
    extends UserScript
{

    @XmlElement(name = "AdditionalDataObject")
    protected AdditionalDataSqlObject additionalDataObject;

    /**
     * Gets the value of the additionalDataObject property.
     * 
     * @return
     *     possible object is
     *     {@link AdditionalDataSqlObject }
     *     
     */
    public AdditionalDataSqlObject getAdditionalDataObject() {
        return additionalDataObject;
    }

    /**
     * Sets the value of the additionalDataObject property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdditionalDataSqlObject }
     *     
     */
    public void setAdditionalDataObject(AdditionalDataSqlObject value) {
        this.additionalDataObject = value;
    }

}
