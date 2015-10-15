
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
 *         &lt;element name="token" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="secretId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="loadSettingsAndPermissions" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="codeResponses" type="{urn:thesecretserver.com}ArrayOfCodeResponse" minOccurs="0"/>
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
    "token",
    "secretId",
    "loadSettingsAndPermissions",
    "codeResponses"
})
@XmlRootElement(name = "GetSecret")
public class GetSecret {

    protected String token;
    protected int secretId;
    @XmlElement(required = true, type = Boolean.class, nillable = true)
    protected Boolean loadSettingsAndPermissions;
    protected ArrayOfCodeResponse codeResponses;

    /**
     * Ruft den Wert der token-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToken() {
        return token;
    }

    /**
     * Legt den Wert der token-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToken(String value) {
        this.token = value;
    }

    /**
     * Ruft den Wert der secretId-Eigenschaft ab.
     * 
     */
    public int getSecretId() {
        return secretId;
    }

    /**
     * Legt den Wert der secretId-Eigenschaft fest.
     * 
     */
    public void setSecretId(int value) {
        this.secretId = value;
    }

    /**
     * Ruft den Wert der loadSettingsAndPermissions-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLoadSettingsAndPermissions() {
        return loadSettingsAndPermissions;
    }

    /**
     * Legt den Wert der loadSettingsAndPermissions-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLoadSettingsAndPermissions(Boolean value) {
        this.loadSettingsAndPermissions = value;
    }

    /**
     * Ruft den Wert der codeResponses-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfCodeResponse }
     *     
     */
    public ArrayOfCodeResponse getCodeResponses() {
        return codeResponses;
    }

    /**
     * Legt den Wert der codeResponses-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfCodeResponse }
     *     
     */
    public void setCodeResponses(ArrayOfCodeResponse value) {
        this.codeResponses = value;
    }

}
