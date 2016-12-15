
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
 *         &lt;element name="GetSSHLoginCredentialsWithMachineResult" type="{urn:thesecretserver.com}SSHCredentialsResult" minOccurs="0"/>
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
    "getSSHLoginCredentialsWithMachineResult"
})
@XmlRootElement(name = "GetSSHLoginCredentialsWithMachineResponse")
public class GetSSHLoginCredentialsWithMachineResponse {

    @XmlElement(name = "GetSSHLoginCredentialsWithMachineResult")
    protected SSHCredentialsResult getSSHLoginCredentialsWithMachineResult;

    /**
     * Gets the value of the getSSHLoginCredentialsWithMachineResult property.
     * 
     * @return
     *     possible object is
     *     {@link SSHCredentialsResult }
     *     
     */
    public SSHCredentialsResult getGetSSHLoginCredentialsWithMachineResult() {
        return getSSHLoginCredentialsWithMachineResult;
    }

    /**
     * Sets the value of the getSSHLoginCredentialsWithMachineResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link SSHCredentialsResult }
     *     
     */
    public void setGetSSHLoginCredentialsWithMachineResult(SSHCredentialsResult value) {
        this.getSSHLoginCredentialsWithMachineResult = value;
    }

}
