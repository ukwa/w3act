
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AddNewSecretResult" type="{urn:thesecretserver.com}AddSecretResult" minOccurs="0"/>
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
    "addNewSecretResult"
})
@XmlRootElement(name = "AddNewSecretResponse")
public class AddNewSecretResponse {

    @XmlElement(name = "AddNewSecretResult")
    protected AddSecretResult addNewSecretResult;

    /**
     * Ruft den Wert der addNewSecretResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AddSecretResult }
     *     
     */
    public AddSecretResult getAddNewSecretResult() {
        return addNewSecretResult;
    }

    /**
     * Legt den Wert der addNewSecretResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AddSecretResult }
     *     
     */
    public void setAddNewSecretResult(AddSecretResult value) {
        this.addNewSecretResult = value;
    }

}
