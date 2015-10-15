
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für User complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="User">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="UserName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DisplayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DomainId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="IsApplicationAccount" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="RadiusTwoFactor" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="EmailTwoFactor" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="RadiusUserName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EmailAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Enabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="DuoTwoFactor" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="OATHTwoFactor" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "User", propOrder = {
    "id",
    "userName",
    "displayName",
    "domainId",
    "isApplicationAccount",
    "radiusTwoFactor",
    "emailTwoFactor",
    "radiusUserName",
    "emailAddress",
    "password",
    "enabled",
    "duoTwoFactor",
    "oathTwoFactor"
})
public class User {

    @XmlElement(name = "Id", required = true, type = Integer.class, nillable = true)
    protected Integer id;
    @XmlElement(name = "UserName")
    protected String userName;
    @XmlElement(name = "DisplayName")
    protected String displayName;
    @XmlElement(name = "DomainId", required = true, type = Integer.class, nillable = true)
    protected Integer domainId;
    @XmlElement(name = "IsApplicationAccount")
    protected boolean isApplicationAccount;
    @XmlElement(name = "RadiusTwoFactor")
    protected boolean radiusTwoFactor;
    @XmlElement(name = "EmailTwoFactor")
    protected boolean emailTwoFactor;
    @XmlElement(name = "RadiusUserName")
    protected String radiusUserName;
    @XmlElement(name = "EmailAddress")
    protected String emailAddress;
    @XmlElement(name = "Password")
    protected String password;
    @XmlElement(name = "Enabled")
    protected boolean enabled;
    @XmlElement(name = "DuoTwoFactor")
    protected boolean duoTwoFactor;
    @XmlElement(name = "OATHTwoFactor")
    protected boolean oathTwoFactor;

    /**
     * Ruft den Wert der id-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setId(Integer value) {
        this.id = value;
    }

    /**
     * Ruft den Wert der userName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Legt den Wert der userName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Ruft den Wert der displayName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Legt den Wert der displayName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayName(String value) {
        this.displayName = value;
    }

    /**
     * Ruft den Wert der domainId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDomainId() {
        return domainId;
    }

    /**
     * Legt den Wert der domainId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDomainId(Integer value) {
        this.domainId = value;
    }

    /**
     * Ruft den Wert der isApplicationAccount-Eigenschaft ab.
     * 
     */
    public boolean isIsApplicationAccount() {
        return isApplicationAccount;
    }

    /**
     * Legt den Wert der isApplicationAccount-Eigenschaft fest.
     * 
     */
    public void setIsApplicationAccount(boolean value) {
        this.isApplicationAccount = value;
    }

    /**
     * Ruft den Wert der radiusTwoFactor-Eigenschaft ab.
     * 
     */
    public boolean isRadiusTwoFactor() {
        return radiusTwoFactor;
    }

    /**
     * Legt den Wert der radiusTwoFactor-Eigenschaft fest.
     * 
     */
    public void setRadiusTwoFactor(boolean value) {
        this.radiusTwoFactor = value;
    }

    /**
     * Ruft den Wert der emailTwoFactor-Eigenschaft ab.
     * 
     */
    public boolean isEmailTwoFactor() {
        return emailTwoFactor;
    }

    /**
     * Legt den Wert der emailTwoFactor-Eigenschaft fest.
     * 
     */
    public void setEmailTwoFactor(boolean value) {
        this.emailTwoFactor = value;
    }

    /**
     * Ruft den Wert der radiusUserName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRadiusUserName() {
        return radiusUserName;
    }

    /**
     * Legt den Wert der radiusUserName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRadiusUserName(String value) {
        this.radiusUserName = value;
    }

    /**
     * Ruft den Wert der emailAddress-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Legt den Wert der emailAddress-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailAddress(String value) {
        this.emailAddress = value;
    }

    /**
     * Ruft den Wert der password-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Legt den Wert der password-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Ruft den Wert der enabled-Eigenschaft ab.
     * 
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Legt den Wert der enabled-Eigenschaft fest.
     * 
     */
    public void setEnabled(boolean value) {
        this.enabled = value;
    }

    /**
     * Ruft den Wert der duoTwoFactor-Eigenschaft ab.
     * 
     */
    public boolean isDuoTwoFactor() {
        return duoTwoFactor;
    }

    /**
     * Legt den Wert der duoTwoFactor-Eigenschaft fest.
     * 
     */
    public void setDuoTwoFactor(boolean value) {
        this.duoTwoFactor = value;
    }

    /**
     * Ruft den Wert der oathTwoFactor-Eigenschaft ab.
     * 
     */
    public boolean isOATHTwoFactor() {
        return oathTwoFactor;
    }

    /**
     * Legt den Wert der oathTwoFactor-Eigenschaft fest.
     * 
     */
    public void setOATHTwoFactor(boolean value) {
        this.oathTwoFactor = value;
    }

}
