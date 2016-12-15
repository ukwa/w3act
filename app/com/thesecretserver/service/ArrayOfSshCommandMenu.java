
package com.thesecretserver.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfSshCommandMenu complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfSshCommandMenu">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SshCommandMenu" type="{urn:thesecretserver.com}SshCommandMenu" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfSshCommandMenu", propOrder = {
    "sshCommandMenu"
})
public class ArrayOfSshCommandMenu {

    @XmlElement(name = "SshCommandMenu", nillable = true)
    protected List<SshCommandMenu> sshCommandMenu;

    /**
     * Gets the value of the sshCommandMenu property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sshCommandMenu property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSshCommandMenu().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SshCommandMenu }
     * 
     * 
     */
    public List<SshCommandMenu> getSshCommandMenu() {
        if (sshCommandMenu == null) {
            sshCommandMenu = new ArrayList<SshCommandMenu>();
        }
        return this.sshCommandMenu;
    }

}
