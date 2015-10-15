
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für GetSecretTemplatesResult complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="GetSecretTemplatesResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Errors" type="{urn:thesecretserver.com}ArrayOfString" minOccurs="0"/>
 *         &lt;element name="SecretTemplates" type="{urn:thesecretserver.com}ArrayOfSecretTemplate" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetSecretTemplatesResult", propOrder = {
    "errors",
    "secretTemplates"
})
public class GetSecretTemplatesResult {

    @XmlElement(name = "Errors")
    protected ArrayOfString errors;
    @XmlElement(name = "SecretTemplates")
    protected ArrayOfSecretTemplate secretTemplates;

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
     * Ruft den Wert der secretTemplates-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSecretTemplate }
     *     
     */
    public ArrayOfSecretTemplate getSecretTemplates() {
        return secretTemplates;
    }

    /**
     * Legt den Wert der secretTemplates-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSecretTemplate }
     *     
     */
    public void setSecretTemplates(ArrayOfSecretTemplate value) {
        this.secretTemplates = value;
    }

}
