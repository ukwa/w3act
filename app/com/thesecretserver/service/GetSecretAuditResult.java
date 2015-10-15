
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für GetSecretAuditResult complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="GetSecretAuditResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Errors" type="{urn:thesecretserver.com}ArrayOfString" minOccurs="0"/>
 *         &lt;element name="SecretAudits" type="{urn:thesecretserver.com}ArrayOfAuditSecret" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetSecretAuditResult", propOrder = {
    "errors",
    "secretAudits"
})
public class GetSecretAuditResult {

    @XmlElement(name = "Errors")
    protected ArrayOfString errors;
    @XmlElement(name = "SecretAudits")
    protected ArrayOfAuditSecret secretAudits;

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
     * Ruft den Wert der secretAudits-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAuditSecret }
     *     
     */
    public ArrayOfAuditSecret getSecretAudits() {
        return secretAudits;
    }

    /**
     * Legt den Wert der secretAudits-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAuditSecret }
     *     
     */
    public void setSecretAudits(ArrayOfAuditSecret value) {
        this.secretAudits = value;
    }

}
