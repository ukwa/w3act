
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UpdateUserScriptResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UpdateUserScriptResult">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:thesecretserver.com}WebServiceResult">
 *       &lt;sequence>
 *         &lt;element name="UserScript" type="{urn:thesecretserver.com}UserScript" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateUserScriptResult", propOrder = {
    "userScript"
})
public class UpdateUserScriptResult
    extends WebServiceResult
{

    @XmlElement(name = "UserScript")
    protected UserScript userScript;

    /**
     * Gets the value of the userScript property.
     * 
     * @return
     *     possible object is
     *     {@link UserScript }
     *     
     */
    public UserScript getUserScript() {
        return userScript;
    }

    /**
     * Sets the value of the userScript property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserScript }
     *     
     */
    public void setUserScript(UserScript value) {
        this.userScript = value;
    }

}
