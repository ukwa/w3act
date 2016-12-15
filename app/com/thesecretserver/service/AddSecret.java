
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="secretTypeId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="secretName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="secretFieldIds" type="{urn:thesecretserver.com}ArrayOfInt" minOccurs="0"/>
 *         &lt;element name="secretItemValues" type="{urn:thesecretserver.com}ArrayOfString" minOccurs="0"/>
 *         &lt;element name="folderId" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "secretTypeId",
    "secretName",
    "secretFieldIds",
    "secretItemValues",
    "folderId"
})
@XmlRootElement(name = "AddSecret")
public class AddSecret {

    protected String token;
    protected int secretTypeId;
    protected String secretName;
    protected ArrayOfInt secretFieldIds;
    protected ArrayOfString secretItemValues;
    protected int folderId;

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
     * Gets the value of the secretTypeId property.
     * 
     */
    public int getSecretTypeId() {
        return secretTypeId;
    }

    /**
     * Sets the value of the secretTypeId property.
     * 
     */
    public void setSecretTypeId(int value) {
        this.secretTypeId = value;
    }

    /**
     * Gets the value of the secretName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecretName() {
        return secretName;
    }

    /**
     * Sets the value of the secretName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecretName(String value) {
        this.secretName = value;
    }

    /**
     * Gets the value of the secretFieldIds property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfInt }
     *     
     */
    public ArrayOfInt getSecretFieldIds() {
        return secretFieldIds;
    }

    /**
     * Sets the value of the secretFieldIds property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfInt }
     *     
     */
    public void setSecretFieldIds(ArrayOfInt value) {
        this.secretFieldIds = value;
    }

    /**
     * Gets the value of the secretItemValues property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getSecretItemValues() {
        return secretItemValues;
    }

    /**
     * Sets the value of the secretItemValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setSecretItemValues(ArrayOfString value) {
        this.secretItemValues = value;
    }

    /**
     * Gets the value of the folderId property.
     * 
     */
    public int getFolderId() {
        return folderId;
    }

    /**
     * Sets the value of the folderId property.
     * 
     */
    public void setFolderId(int value) {
        this.folderId = value;
    }

}
