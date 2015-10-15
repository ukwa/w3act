
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für GetFavoritesResult complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="GetFavoritesResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Errors" type="{urn:thesecretserver.com}ArrayOfString" minOccurs="0"/>
 *         &lt;element name="SecretSummaries" type="{urn:thesecretserver.com}ArrayOfSecretSummary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetFavoritesResult", propOrder = {
    "errors",
    "secretSummaries"
})
public class GetFavoritesResult {

    @XmlElement(name = "Errors")
    protected ArrayOfString errors;
    @XmlElement(name = "SecretSummaries")
    protected ArrayOfSecretSummary secretSummaries;

    /**
     * Ruft den Wert der errors-Eigenschaft ab.
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
     * Legt den Wert der errors-Eigenschaft fest.
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
     * Ruft den Wert der secretSummaries-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSecretSummary }
     *     
     */
    public ArrayOfSecretSummary getSecretSummaries() {
        return secretSummaries;
    }

    /**
     * Legt den Wert der secretSummaries-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSecretSummary }
     *     
     */
    public void setSecretSummaries(ArrayOfSecretSummary value) {
        this.secretSummaries = value;
    }

}
