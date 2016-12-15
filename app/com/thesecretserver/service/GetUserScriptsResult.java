
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetUserScriptsResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetUserScriptsResult">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:thesecretserver.com}WebServiceResult">
 *       &lt;sequence>
 *         &lt;element name="UserScripts" type="{urn:thesecretserver.com}ArrayOfUserScript" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetUserScriptsResult", propOrder = {
    "userScripts"
})
public class GetUserScriptsResult
    extends WebServiceResult
{

    @XmlElement(name = "UserScripts")
    protected ArrayOfUserScript userScripts;

    /**
     * Gets the value of the userScripts property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfUserScript }
     *     
     */
    public ArrayOfUserScript getUserScripts() {
        return userScripts;
    }

    /**
     * Sets the value of the userScripts property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfUserScript }
     *     
     */
    public void setUserScripts(ArrayOfUserScript value) {
        this.userScripts = value;
    }

}
