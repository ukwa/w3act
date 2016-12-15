
package com.thesecretserver.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfSqlScriptArgument2 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfSqlScriptArgument2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SqlScriptArgument2" type="{urn:thesecretserver.com}SqlScriptArgument2" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfSqlScriptArgument2", propOrder = {
    "sqlScriptArgument2"
})
public class ArrayOfSqlScriptArgument2 {

    @XmlElement(name = "SqlScriptArgument2", nillable = true)
    protected List<SqlScriptArgument2> sqlScriptArgument2;

    /**
     * Gets the value of the sqlScriptArgument2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sqlScriptArgument2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSqlScriptArgument2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SqlScriptArgument2 }
     * 
     * 
     */
    public List<SqlScriptArgument2> getSqlScriptArgument2() {
        if (sqlScriptArgument2 == null) {
            sqlScriptArgument2 = new ArrayList<SqlScriptArgument2>();
        }
        return this.sqlScriptArgument2;
    }

}
