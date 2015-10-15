
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für Permission complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Permission">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UserOrGroup" type="{urn:thesecretserver.com}GroupOrUserRecord" minOccurs="0"/>
 *         &lt;element name="View" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Edit" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Owner" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Permission", propOrder = {
    "userOrGroup",
    "view",
    "edit",
    "owner"
})
public class Permission {

    @XmlElement(name = "UserOrGroup")
    protected GroupOrUserRecord userOrGroup;
    @XmlElement(name = "View")
    protected boolean view;
    @XmlElement(name = "Edit")
    protected boolean edit;
    @XmlElement(name = "Owner")
    protected boolean owner;

    /**
     * Ruft den Wert der userOrGroup-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link GroupOrUserRecord }
     *     
     */
    public GroupOrUserRecord getUserOrGroup() {
        return userOrGroup;
    }

    /**
     * Legt den Wert der userOrGroup-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupOrUserRecord }
     *     
     */
    public void setUserOrGroup(GroupOrUserRecord value) {
        this.userOrGroup = value;
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
