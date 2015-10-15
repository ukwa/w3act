
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für GetSecretsByFieldValueResult complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="GetSecretsByFieldValueResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Errors" type="{urn:thesecretserver.com}ArrayOfString" minOccurs="0"/>
 *         &lt;element name="Secrets" type="{urn:thesecretserver.com}ArrayOfSecret" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetSecretsByFieldValueResult", propOrder = {
    "errors",
    "secrets"
})
public class GetSecretsByFieldValueResult {

    @XmlElement(name = "Errors")
    protected ArrayOfString errors;
    @XmlElement(name = "Secrets")
    protected ArrayOfSecret secrets;

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
     * Ruft den Wert der secrets-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSecret }
     *     
     */
    public ArrayOfSecret getSecrets() {
        return secrets;
    }

    /**
     * Legt den Wert der secrets-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSecret }
     *     
     */
    public void setSecrets(ArrayOfSecret value) {
        this.secrets = value;
    }

}
