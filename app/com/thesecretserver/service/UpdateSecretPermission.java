
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
 *         &lt;element name="secretId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="groupOrUserRecord" type="{urn:thesecretserver.com}GroupOrUserRecord" minOccurs="0"/>
 *         &lt;element name="view" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="edit" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="owner" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "groupOrUserRecord",
    "view",
    "edit",
    "owner"
})
@XmlRootElement(name = "UpdateSecretPermission")
public class UpdateSecretPermission {

    protected String token;
    protected int secretId;
    protected GroupOrUserRecord groupOrUserRecord;
    protected boolean view;
    protected boolean edit;
    protected boolean owner;

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
     * Gets the value of the groupOrUserRecord property.
     * 
     * @return
     *     possible object is
     *     {@link GroupOrUserRecord }
     *     
     */
    public GroupOrUserRecord getGroupOrUserRecord() {
        return groupOrUserRecord;
    }

    /**
     * Sets the value of the groupOrUserRecord property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupOrUserRecord }
     *     
     */
    public void setGroupOrUserRecord(GroupOrUserRecord value) {
        this.groupOrUserRecord = value;
    }

    /**
     * Gets the value of the view property.
     * 
     */
    public boolean isView() {
        return view;
    }

    /**
     * Sets the value of the view property.
     * 
     */
    public void setView(boolean value) {
        this.view = value;
    }

    /**
     * Gets the value of the edit property.
     * 
     */
    public boolean isEdit() {
        return edit;
    }

    /**
     * Sets the value of the edit property.
     * 
     */
    public void setEdit(boolean value) {
        this.edit = value;
    }

    /**
     * Gets the value of the owner property.
     * 
     */
    public boolean isOwner() {
        return owner;
    }

    /**
     * Sets the value of the owner property.
     * 
     */
    public void setOwner(boolean value) {
        this.owner = value;
    }

}
