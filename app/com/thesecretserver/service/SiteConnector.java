
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for SiteConnector complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SiteConnector">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SiteConnectorId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SiteConnectorName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QueueType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HostName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Port" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Active" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Validated" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="UseSsl" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="SslCertificateThumbprint" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LastModifiedDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="UserName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PasswordIV" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="Version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SiteConnector", propOrder = {
    "siteConnectorId",
    "siteConnectorName",
    "queueType",
    "hostName",
    "port",
    "active",
    "validated",
    "useSsl",
    "sslCertificateThumbprint",
    "lastModifiedDate",
    "userName",
    "passwordIV",
    "version"
})
public class SiteConnector {

    @XmlElement(name = "SiteConnectorId")
    protected int siteConnectorId;
    @XmlElement(name = "SiteConnectorName")
    protected String siteConnectorName;
    @XmlElement(name = "QueueType")
    protected String queueType;
    @XmlElement(name = "HostName")
    protected String hostName;
    @XmlElement(name = "Port")
    protected int port;
    @XmlElement(name = "Active")
    protected boolean active;
    @XmlElement(name = "Validated")
    protected boolean validated;
    @XmlElement(name = "UseSsl")
    protected boolean useSsl;
    @XmlElement(name = "SslCertificateThumbprint")
    protected String sslCertificateThumbprint;
    @XmlElement(name = "LastModifiedDate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastModifiedDate;
    @XmlElement(name = "UserName")
    protected String userName;
    @XmlElement(name = "PasswordIV")
    protected byte[] passwordIV;
    @XmlElement(name = "Version")
    protected String version;

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
     * Gets the value of the siteConnectorName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiteConnectorName() {
        return siteConnectorName;
    }

    /**
     * Sets the value of the siteConnectorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiteConnectorName(String value) {
        this.siteConnectorName = value;
    }

    /**
     * Gets the value of the queueType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQueueType() {
        return queueType;
    }

    /**
     * Sets the value of the queueType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQueueType(String value) {
        this.queueType = value;
    }

    /**
     * Gets the value of the hostName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Sets the value of the hostName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHostName(String value) {
        this.hostName = value;
    }

    /**
     * Gets the value of the port property.
     * 
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets the value of the port property.
     * 
     */
    public void setPort(int value) {
        this.port = value;
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
     * Gets the value of the validated property.
     * 
     */
    public boolean isValidated() {
        return validated;
    }

    /**
     * Sets the value of the validated property.
     * 
     */
    public void setValidated(boolean value) {
        this.validated = value;
    }

    /**
     * Gets the value of the useSsl property.
     * 
     */
    public boolean isUseSsl() {
        return useSsl;
    }

    /**
     * Sets the value of the useSsl property.
     * 
     */
    public void setUseSsl(boolean value) {
        this.useSsl = value;
    }

    /**
     * Gets the value of the sslCertificateThumbprint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSslCertificateThumbprint() {
        return sslCertificateThumbprint;
    }

    /**
     * Sets the value of the sslCertificateThumbprint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSslCertificateThumbprint(String value) {
        this.sslCertificateThumbprint = value;
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
     * Gets the value of the userName property.
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
     * Sets the value of the userName property.
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
     * Gets the value of the passwordIV property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getPasswordIV() {
        return passwordIV;
    }

    /**
     * Sets the value of the passwordIV property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setPasswordIV(byte[] value) {
        this.passwordIV = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

}
