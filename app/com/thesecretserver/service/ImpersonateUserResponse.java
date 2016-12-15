
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
 *         &lt;element name="ImpersonateUserResult" type="{urn:thesecretserver.com}ImpersonateResult" minOccurs="0"/>
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
    "impersonateUserResult"
})
@XmlRootElement(name = "ImpersonateUserResponse")
public class ImpersonateUserResponse {

    @XmlElement(name = "ImpersonateUserResult")
    protected ImpersonateResult impersonateUserResult;

    /**
     * Gets the value of the impersonateUserResult property.
     * 
     * @return
     *     possible object is
     *     {@link ImpersonateResult }
     *     
     */
    public ImpersonateResult getImpersonateUserResult() {
        return impersonateUserResult;
    }

    /**
     * Sets the value of the impersonateUserResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImpersonateResult }
     *     
     */
    public void setImpersonateUserResult(ImpersonateResult value) {
        this.impersonateUserResult = value;
    }

}
