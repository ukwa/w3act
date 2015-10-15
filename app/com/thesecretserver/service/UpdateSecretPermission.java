
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
     * Ruft den Wert der groupOrUserRecord-Eigenschaft ab.
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
     * Legt den Wert der groupOrUserRecord-Eigenschaft fest.
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
     * Ruft den Wert der view-Eigenschaft ab.
     * 
     */
    public boolean isView() {
        return view;
    }

    /**
     * Legt den Wert der view-Eigenschaft fest.
     * 
     */
    public void setView(boolean value) {
        this.view = value;
    }

    /**
     * Ruft den Wert der edit-Eigenschaft ab.
     * 
     */
    public boolean isEdit() {
        return edit;
    }

    /**
     * Legt den Wert der edit-Eigenschaft fest.
     * 
     */
    public void setEdit(boolean value) {
        this.edit = value;
    }

    /**
     * Ruft den Wert der owner-Eigenschaft ab.
     * 
     */
    public boolean isOwner() {
        return owner;
    }

    /**
     * Legt den Wert der owner-Eigenschaft fest.
     * 
     */
    public void setOwner(boolean value) {
        this.owner = value;
    }

}
