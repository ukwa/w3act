
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AdditionalDependencyInfoJson complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AdditionalDependencyInfoJson">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Regex" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PowershellArguments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SshArguments" type="{urn:thesecretserver.com}ArrayOfSshScriptArgument" minOccurs="0"/>
 *         &lt;element name="SqlArguments" type="{urn:thesecretserver.com}ArrayOfSqlScriptArgument" minOccurs="0"/>
 *         &lt;element name="OdbcConnectionArguments" type="{urn:thesecretserver.com}ArrayOfOdbcConnectionArg" minOccurs="0"/>
 *         &lt;element name="Port" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Database" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ServerKeyDigest" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdditionalDependencyInfoJson", propOrder = {
    "regex",
    "powershellArguments",
    "sshArguments",
    "sqlArguments",
    "odbcConnectionArguments",
    "port",
    "database",
    "serverKeyDigest"
})
public class AdditionalDependencyInfoJson {

    @XmlElement(name = "Regex")
    protected String regex;
    @XmlElement(name = "PowershellArguments")
    protected String powershellArguments;
    @XmlElement(name = "SshArguments")
    protected ArrayOfSshScriptArgument sshArguments;
    @XmlElement(name = "SqlArguments")
    protected ArrayOfSqlScriptArgument sqlArguments;
    @XmlElement(name = "OdbcConnectionArguments")
    protected ArrayOfOdbcConnectionArg odbcConnectionArguments;
    @XmlElement(name = "Port")
    protected String port;
    @XmlElement(name = "Database")
    protected String database;
    @XmlElement(name = "ServerKeyDigest")
    protected String serverKeyDigest;

    /**
     * Gets the value of the regex property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegex() {
        return regex;
    }

    /**
     * Sets the value of the regex property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegex(String value) {
        this.regex = value;
    }

    /**
     * Gets the value of the powershellArguments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPowershellArguments() {
        return powershellArguments;
    }

    /**
     * Sets the value of the powershellArguments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPowershellArguments(String value) {
        this.powershellArguments = value;
    }

    /**
     * Gets the value of the sshArguments property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSshScriptArgument }
     *     
     */
    public ArrayOfSshScriptArgument getSshArguments() {
        return sshArguments;
    }

    /**
     * Sets the value of the sshArguments property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSshScriptArgument }
     *     
     */
    public void setSshArguments(ArrayOfSshScriptArgument value) {
        this.sshArguments = value;
    }

    /**
     * Gets the value of the sqlArguments property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSqlScriptArgument }
     *     
     */
    public ArrayOfSqlScriptArgument getSqlArguments() {
        return sqlArguments;
    }

    /**
     * Sets the value of the sqlArguments property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSqlScriptArgument }
     *     
     */
    public void setSqlArguments(ArrayOfSqlScriptArgument value) {
        this.sqlArguments = value;
    }

    /**
     * Gets the value of the odbcConnectionArguments property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfOdbcConnectionArg }
     *     
     */
    public ArrayOfOdbcConnectionArg getOdbcConnectionArguments() {
        return odbcConnectionArguments;
    }

    /**
     * Sets the value of the odbcConnectionArguments property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfOdbcConnectionArg }
     *     
     */
    public void setOdbcConnectionArguments(ArrayOfOdbcConnectionArg value) {
        this.odbcConnectionArguments = value;
    }

    /**
     * Gets the value of the port property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPort() {
        return port;
    }

    /**
     * Sets the value of the port property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPort(String value) {
        this.port = value;
    }

    /**
     * Gets the value of the database property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Sets the value of the database property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatabase(String value) {
        this.database = value;
    }

    /**
     * Gets the value of the serverKeyDigest property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServerKeyDigest() {
        return serverKeyDigest;
    }

    /**
     * Sets the value of the serverKeyDigest property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServerKeyDigest(String value) {
        this.serverKeyDigest = value;
    }

}
