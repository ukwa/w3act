
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
     * Gets the value of the token property.
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
     * Sets the value of the token property.
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
     * Gets the value of the secretId property.
     * 
     */
    public int getSecretId() {
        return secretId;
    }

    /**
     * Sets the value of the secretId property.
     * 
     */
    public void setSecretId(int value) {
        this.secretId = value;
    }

    /**
     * Gets the value of the loadSettingsAndPermissions property.
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
     * Sets the value of the loadSettingsAndPermissions property.
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
     * Gets the value of the codeResponses property.
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
     * Sets the value of the codeResponses property.
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
