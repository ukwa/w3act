
package com.thesecretserver.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfSshScriptArgument2 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfSshScriptArgument2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SshScriptArgument2" type="{urn:thesecretserver.com}SshScriptArgument2" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfSshScriptArgument2", propOrder = {
    "sshScriptArgument2"
})
public class ArrayOfSshScriptArgument2 {

    @XmlElement(name = "SshScriptArgument2", nillable = true)
    protected List<SshScriptArgument2> sshScriptArgument2;

    /**
     * Gets the value of the sshScriptArgument2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sshScriptArgument2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSshScriptArgument2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SshScriptArgument2 }
     * 
     * 
     */
    public List<SshScriptArgument2> getSshScriptArgument2() {
        if (sshScriptArgument2 == null) {
            sshScriptArgument2 = new ArrayList<SshScriptArgument2>();
        }
        return this.sshScriptArgument2;
    }

}
