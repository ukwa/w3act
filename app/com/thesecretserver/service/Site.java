
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Site complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Site">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SiteId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="OrganizationId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SymmetricKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SymmetricKeyIV" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="InitializationVector" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="SiteName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Active" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="HeartbeatInterval" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="UseWebSite" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="SystemSite" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="LastModifiedDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="WinRMEndpoint" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EnableCredSSPForWinRM" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="SiteConnectorId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SiteConnector" type="{urn:thesecretserver.com}SiteConnector" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Site", propOrder = {
    "siteId",
    "organizationId",
    "symmetricKey",
    "symmetricKeyIV",
    "initializationVector",
    "siteName",
    "active",
    "heartbeatInterval",
    "useWebSite",
    "systemSite",
    "lastModifiedDate",
    "winRMEndpoint",
    "enableCredSSPForWinRM",
    "siteConnectorId",
    "siteConnector"
})
public class Site {

    @XmlElement(name = "SiteId")
    protected int siteId;
    @XmlElement(name = "OrganizationId")
    protected int organizationId;
    @XmlElement(name = "SymmetricKey")
    protected String symmetricKey;
    @XmlElement(name = "SymmetricKeyIV")
    protected byte[] symmetricKeyIV;
    @XmlElement(name = "InitializationVector")
    protected byte[] initializationVector;
    @XmlElement(name = "SiteName")
    protected String siteName;
    @XmlElement(name = "Active")
    protected boolean active;
    @XmlElement(name = "HeartbeatInterval")
    protected int heartbeatInterval;
    @XmlElement(name = "UseWebSite")
    protected boolean useWebSite;
    @XmlElement(name = "SystemSite")
    protected boolean systemSite;
    @XmlElement(name = "LastModifiedDate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastModifiedDate;
    @XmlElement(name = "WinRMEndpoint")
    protected String winRMEndpoint;
    @XmlElement(name = "EnableCredSSPForWinRM", required = true, type = Boolean.class, nillable = true)
    protected Boolean enableCredSSPForWinRM;
    @XmlElement(name = "SiteConnectorId")
    protected int siteConnectorId;
    @XmlElement(name = "SiteConnector")
    protected SiteConnector siteConnector;

    /**
     * Gets the value of the siteId property.
     * 
     */
    public int getSiteId() {
        return siteId;
    }

    /**
     * Sets the value of the siteId property.
     * 
     */
    public void setSiteId(int value) {
        this.siteId = value;
    }

    /**
     * Gets the value of the organizationId property.
     * 
     */
    public int getOrganizationId() {
        return organizationId;
    }

    /**
     * Sets the value of the organizationId property.
     * 
     */
    public void setOrganizationId(int value) {
        this.organizationId = value;
    }

    /**
     * Gets the value of the symmetricKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSymmetricKey() {
        return symmetricKey;
    }

    /**
     * Sets the value of the symmetricKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSymmetricKey(String value) {
        this.symmetricKey = value;
    }

    /**
     * Gets the value of the symmetricKeyIV property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getSymmetricKeyIV() {
        return symmetricKeyIV;
    }

    /**
     * Sets the value of the symmetricKeyIV property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setSymmetricKeyIV(byte[] value) {
        this.symmetricKeyIV = value;
    }

    /**
     * Gets the value of the initializationVector property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getInitializationVector() {
        return initializationVector;
    }

    /**
     * Sets the value of the initializationVector property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setInitializationVector(byte[] value) {
        this.initializationVector = value;
    }

    /**
     * Gets the value of the siteName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiteName() {
        return siteName;
    }

    /**
     * Sets the value of the siteName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiteName(String value) {
        this.siteName = value;
    }

    /**
     * Gets the value of the active property.
     * 
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the value of the active property.
     * 
     */
    public void setActive(boolean value) {
        this.active = value;
    }

    /**
     * Gets the value of the heartbeatInterval property.
     * 
     */
    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }

    /**
     * Sets the value of the heartbeatInterval property.
     * 
     */
    public void setHeartbeatInterval(int value) {
        this.heartbeatInterval = value;
    }

    /**
     * Gets the value of the useWebSite property.
     * 
     */
    public boolean isUseWebSite() {
        return useWebSite;
    }

    /**
     * Sets the value of the useWebSite property.
     * 
     */
    public void setUseWebSite(boolean value) {
        this.useWebSite = value;
    }

    /**
     * Gets the value of the systemSite property.
     * 
     */
    public boolean isSystemSite() {
        return systemSite;
    }

    /**
     * Sets the value of the systemSite property.
     * 
     */
    public void setSystemSite(boolean value) {
        this.systemSite = value;
    }

    /**
     * Gets the value of the lastModifiedDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * Sets the value of the lastModifiedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastModifiedDate(XMLGregorianCalendar value) {
        this.lastModifiedDate = value;
    }

    /**
     * Gets the value of the winRMEndpoint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWinRMEndpoint() {
        return winRMEndpoint;
    }

    /**
     * Sets the value of the winRMEndpoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWinRMEndpoint(String value) {
        this.winRMEndpoint = value;
    }

    /**
     * Gets the value of the enableCredSSPForWinRM property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEnableCredSSPForWinRM() {
        return enableCredSSPForWinRM;
    }

    /**
     * Sets the value of the enableCredSSPForWinRM property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEnableCredSSPForWinRM(Boolean value) {
        this.enableCredSSPForWinRM = value;
    }

    /**
     * Gets the value of the siteConnectorId property.
     * 
     */
    public int getSiteConnectorId() {
        return siteConnectorId;
    }

    /**
     * Sets the value of the siteConnectorId property.
     * 
     */
    public void setSiteConnectorId(int value) {
        this.siteConnectorId = value;
    }

    /**
     * Gets the value of the siteConnector property.
     * 
     * @return
     *     possible object is
     *     {@link SiteConnector }
     *     
     */
    public SiteConnector getSiteConnector() {
        return siteConnector;
    }

    /**
     * Sets the value of the siteConnector property.
     * 
     * @param value
     *     allowed object is
     *     {@link SiteConnector }
     *     
     */
    public void setSiteConnector(SiteConnector value) {
        this.siteConnector = value;
    }

}
