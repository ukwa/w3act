
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="VersionGetResult" type="{urn:thesecretserver.com}VersionGetResult" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "versionGetResult"
})
@XmlRootElement(name = "VersionGetResponse")
public class VersionGetResponse {

    @XmlElement(name = "VersionGetResult")
    protected VersionGetResult versionGetResult;

    /**
     * Gets the value of the versionGetResult property.
     * 
     * @return
     *     possible object is
     *     {@link VersionGetResult }
     *     
     */
    public VersionGetResult getVersionGetResult() {
        return versionGetResult;
    }

    /**
     * Sets the value of the versionGetResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link VersionGetResult }
     *     
     */
    public void setVersionGetResult(VersionGetResult value) {
        this.versionGetResult = value;
    }

}
