//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2024.02.11 at 12:13:02 PM GMT 
//


package net.pepdog.toolbox.rbxl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.oikmo.toolbox.rbxl package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RobloxDeleteItem_QNAME = new QName("", "DeleteItem");
    private final static QName _RobloxExternal_QNAME = new QName("", "External");
    private final static QName _PropertyContainerCoordinateFrameR10_QNAME = new QName("", "R10");
    private final static QName _PropertyContainerCoordinateFrameR21_QNAME = new QName("", "R21");
    private final static QName _PropertyContainerCoordinateFrameR20_QNAME = new QName("", "R20");
    private final static QName _PropertyContainerCoordinateFrameR01_QNAME = new QName("", "R01");
    private final static QName _PropertyContainerCoordinateFrameR12_QNAME = new QName("", "R12");
    private final static QName _PropertyContainerCoordinateFrameR00_QNAME = new QName("", "R00");
    private final static QName _PropertyContainerCoordinateFrameR11_QNAME = new QName("", "R11");
    private final static QName _PropertyContainerCoordinateFrameR22_QNAME = new QName("", "R22");
    private final static QName _PropertyContainerCoordinateFrameR02_QNAME = new QName("", "R02");
    private final static QName _PropertyContainerCoordinateFrameX_QNAME = new QName("", "X");
    private final static QName _PropertyContainerCoordinateFrameY_QNAME = new QName("", "Y");
    private final static QName _PropertyContainerCoordinateFrameZ_QNAME = new QName("", "Z");
    private final static QName _PropertyContainerContentNull_QNAME = new QName("", "null");
    private final static QName _PropertyContainerContentBinary_QNAME = new QName("", "binary");
    private final static QName _PropertyContainerContentUrl_QNAME = new QName("", "url");
    private final static QName _PropertyContainerContentHash_QNAME = new QName("", "hash");
    private final static QName _PropertyContainerColor3R_QNAME = new QName("", "R");
    private final static QName _PropertyContainerColor3B_QNAME = new QName("", "B");
    private final static QName _PropertyContainerColor3G_QNAME = new QName("", "G");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.oikmo.toolbox.rbxl
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ItemType }
     * 
     */
    public ItemType createItemType() {
        return new ItemType();
    }

    /**
     * Create an instance of {@link PropertyContainer }
     * 
     */
    public PropertyContainer createPropertyContainer() {
        return new PropertyContainer();
    }

    /**
     * Create an instance of {@link PropertyContainer.Content }
     * 
     */
    public PropertyContainer.Content createPropertyContainerContent() {
        return new PropertyContainer.Content();
    }

    /**
     * Create an instance of {@link PropertyContainer.Color3 }
     * 
     */
    public PropertyContainer.Color3 createPropertyContainerColor3() {
        return new PropertyContainer.Color3();
    }

    /**
     * Create an instance of {@link PropertyContainer.CoordinateFrame }
     * 
     */
    public PropertyContainer.CoordinateFrame createPropertyContainerCoordinateFrame() {
        return new PropertyContainer.CoordinateFrame();
    }

    /**
     * Create an instance of {@link PropertyContainer.Vector3 }
     * 
     */
    public PropertyContainer.Vector3 createPropertyContainerVector3() {
        return new PropertyContainer.Vector3();
    }

    /**
     * Create an instance of {@link Item }
     * 
     */
    public Item createItem() {
        return new Item();
    }

    /**
     * Create an instance of {@link ItemType.Properties }
     * 
     */
    public ItemType.Properties createItemTypeProperties() {
        return new ItemType.Properties();
    }

    /**
     * Create an instance of {@link Roblox }
     * 
     */
    public Roblox createRoblox() {
        return new Roblox();
    }

    /**
     * Create an instance of {@link Base64Binary }
     * 
     */
    public Base64Binary createBase64Binary() {
        return new Base64Binary();
    }

    /**
     * Create an instance of {@link HexBinary }
     * 
     */
    public HexBinary createHexBinary() {
        return new HexBinary();
    }

    /**
     * Create an instance of {@link PropertyContainer.String }
     * 
     */
    public PropertyContainer.String createPropertyContainerString() {
        return new PropertyContainer.String();
    }

    /**
     * Create an instance of {@link PropertyContainer.ProtectedString }
     * 
     */
    public PropertyContainer.ProtectedString createPropertyContainerProtectedString() {
        return new PropertyContainer.ProtectedString();
    }

    /**
     * Create an instance of {@link PropertyContainer.Int }
     * 
     */
    public PropertyContainer.Int createPropertyContainerInt() {
        return new PropertyContainer.Int();
    }

    /**
     * Create an instance of {@link PropertyContainer.Float }
     * 
     */
    public PropertyContainer.Float createPropertyContainerFloat() {
        return new PropertyContainer.Float();
    }

    /**
     * Create an instance of {@link PropertyContainer.Double }
     * 
     */
    public PropertyContainer.Double createPropertyContainerDouble() {
        return new PropertyContainer.Double();
    }

    /**
     * Create an instance of {@link PropertyContainer.Bool }
     * 
     */
    public PropertyContainer.Bool createPropertyContainerBool() {
        return new PropertyContainer.Bool();
    }

    /**
     * Create an instance of {@link PropertyContainer.Token }
     * 
     */
    public PropertyContainer.Token createPropertyContainerToken() {
        return new PropertyContainer.Token();
    }

    /**
     * Create an instance of {@link PropertyContainer.Tokens }
     * 
     */
    public PropertyContainer.Tokens createPropertyContainerTokens() {
        return new PropertyContainer.Tokens();
    }

    /**
     * Create an instance of {@link PropertyContainer.Enum }
     * 
     */
    public PropertyContainer.Enum createPropertyContainerEnum() {
        return new PropertyContainer.Enum();
    }

    /**
     * Create an instance of {@link PropertyContainer.Ref }
     * 
     */
    public PropertyContainer.Ref createPropertyContainerRef() {
        return new PropertyContainer.Ref();
    }

    /**
     * Create an instance of {@link PropertyContainer.Refs }
     * 
     */
    public PropertyContainer.Refs createPropertyContainerRefs() {
        return new PropertyContainer.Refs();
    }

    /**
     * Create an instance of {@link PropertyContainer.Complex }
     * 
     */
    public PropertyContainer.Complex createPropertyContainerComplex() {
        return new PropertyContainer.Complex();
    }

    /**
     * Create an instance of {@link PropertyContainer.Content.Binary }
     * 
     */
    public PropertyContainer.Content.Binary createPropertyContainerContentBinary() {
        return new PropertyContainer.Content.Binary();
    }

    /**
     * Create an instance of {@link PropertyContainer.Color3 .R }
     * 
     */
    public PropertyContainer.Color3 .R createPropertyContainerColor3R() {
        return new PropertyContainer.Color3 .R();
    }

    /**
     * Create an instance of {@link PropertyContainer.Color3 .G }
     * 
     */
    public PropertyContainer.Color3 .G createPropertyContainerColor3G() {
        return new PropertyContainer.Color3 .G();
    }

    /**
     * Create an instance of {@link PropertyContainer.Color3 .B }
     * 
     */
    public PropertyContainer.Color3 .B createPropertyContainerColor3B() {
        return new PropertyContainer.Color3 .B();
    }

    /**
     * Create an instance of {@link PropertyContainer.CoordinateFrame.X }
     * 
     */
    public PropertyContainer.CoordinateFrame.X createPropertyContainerCoordinateFrameX() {
        return new PropertyContainer.CoordinateFrame.X();
    }

    /**
     * Create an instance of {@link PropertyContainer.CoordinateFrame.Y }
     * 
     */
    public PropertyContainer.CoordinateFrame.Y createPropertyContainerCoordinateFrameY() {
        return new PropertyContainer.CoordinateFrame.Y();
    }

    /**
     * Create an instance of {@link PropertyContainer.CoordinateFrame.Z }
     * 
     */
    public PropertyContainer.CoordinateFrame.Z createPropertyContainerCoordinateFrameZ() {
        return new PropertyContainer.CoordinateFrame.Z();
    }

    /**
     * Create an instance of {@link PropertyContainer.CoordinateFrame.R00 }
     * 
     */
    public PropertyContainer.CoordinateFrame.R00 createPropertyContainerCoordinateFrameR00() {
        return new PropertyContainer.CoordinateFrame.R00();
    }

    /**
     * Create an instance of {@link PropertyContainer.CoordinateFrame.R01 }
     * 
     */
    public PropertyContainer.CoordinateFrame.R01 createPropertyContainerCoordinateFrameR01() {
        return new PropertyContainer.CoordinateFrame.R01();
    }

    /**
     * Create an instance of {@link PropertyContainer.CoordinateFrame.R02 }
     * 
     */
    public PropertyContainer.CoordinateFrame.R02 createPropertyContainerCoordinateFrameR02() {
        return new PropertyContainer.CoordinateFrame.R02();
    }

    /**
     * Create an instance of {@link PropertyContainer.CoordinateFrame.R10 }
     * 
     */
    public PropertyContainer.CoordinateFrame.R10 createPropertyContainerCoordinateFrameR10() {
        return new PropertyContainer.CoordinateFrame.R10();
    }

    /**
     * Create an instance of {@link PropertyContainer.CoordinateFrame.R11 }
     * 
     */
    public PropertyContainer.CoordinateFrame.R11 createPropertyContainerCoordinateFrameR11() {
        return new PropertyContainer.CoordinateFrame.R11();
    }

    /**
     * Create an instance of {@link PropertyContainer.CoordinateFrame.R12 }
     * 
     */
    public PropertyContainer.CoordinateFrame.R12 createPropertyContainerCoordinateFrameR12() {
        return new PropertyContainer.CoordinateFrame.R12();
    }

    /**
     * Create an instance of {@link PropertyContainer.CoordinateFrame.R20 }
     * 
     */
    public PropertyContainer.CoordinateFrame.R20 createPropertyContainerCoordinateFrameR20() {
        return new PropertyContainer.CoordinateFrame.R20();
    }

    /**
     * Create an instance of {@link PropertyContainer.CoordinateFrame.R21 }
     * 
     */
    public PropertyContainer.CoordinateFrame.R21 createPropertyContainerCoordinateFrameR21() {
        return new PropertyContainer.CoordinateFrame.R21();
    }

    /**
     * Create an instance of {@link PropertyContainer.CoordinateFrame.R22 }
     * 
     */
    public PropertyContainer.CoordinateFrame.R22 createPropertyContainerCoordinateFrameR22() {
        return new PropertyContainer.CoordinateFrame.R22();
    }

    /**
     * Create an instance of {@link PropertyContainer.Vector3 .X }
     * 
     */
    public PropertyContainer.Vector3 .X createPropertyContainerVector3X() {
        return new PropertyContainer.Vector3 .X();
    }

    /**
     * Create an instance of {@link PropertyContainer.Vector3 .Y }
     * 
     */
    public PropertyContainer.Vector3 .Y createPropertyContainerVector3Y() {
        return new PropertyContainer.Vector3 .Y();
    }

    /**
     * Create an instance of {@link PropertyContainer.Vector3 .Z }
     * 
     */
    public PropertyContainer.Vector3 .Z createPropertyContainerVector3Z() {
        return new PropertyContainer.Vector3 .Z();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.lang.String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "DeleteItem", scope = Roblox.class)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    public JAXBElement<java.lang.String> createRobloxDeleteItem(java.lang.String value) {
        return new JAXBElement<java.lang.String>(_RobloxDeleteItem_QNAME, java.lang.String.class, Roblox.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.lang.String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "External", scope = Roblox.class)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    public JAXBElement<java.lang.String> createRobloxExternal(java.lang.String value) {
        return new JAXBElement<java.lang.String>(_RobloxExternal_QNAME, java.lang.String.class, Roblox.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PropertyContainer.CoordinateFrame.R10 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "R10", scope = PropertyContainer.CoordinateFrame.class)
    public JAXBElement<PropertyContainer.CoordinateFrame.R10> createPropertyContainerCoordinateFrameR10(PropertyContainer.CoordinateFrame.R10 value) {
        return new JAXBElement<PropertyContainer.CoordinateFrame.R10>(_PropertyContainerCoordinateFrameR10_QNAME, PropertyContainer.CoordinateFrame.R10 .class, PropertyContainer.CoordinateFrame.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PropertyContainer.CoordinateFrame.R21 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "R21", scope = PropertyContainer.CoordinateFrame.class)
    public JAXBElement<PropertyContainer.CoordinateFrame.R21> createPropertyContainerCoordinateFrameR21(PropertyContainer.CoordinateFrame.R21 value) {
        return new JAXBElement<PropertyContainer.CoordinateFrame.R21>(_PropertyContainerCoordinateFrameR21_QNAME, PropertyContainer.CoordinateFrame.R21 .class, PropertyContainer.CoordinateFrame.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PropertyContainer.CoordinateFrame.R20 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "R20", scope = PropertyContainer.CoordinateFrame.class)
    public JAXBElement<PropertyContainer.CoordinateFrame.R20> createPropertyContainerCoordinateFrameR20(PropertyContainer.CoordinateFrame.R20 value) {
        return new JAXBElement<PropertyContainer.CoordinateFrame.R20>(_PropertyContainerCoordinateFrameR20_QNAME, PropertyContainer.CoordinateFrame.R20 .class, PropertyContainer.CoordinateFrame.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PropertyContainer.CoordinateFrame.R01 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "R01", scope = PropertyContainer.CoordinateFrame.class)
    public JAXBElement<PropertyContainer.CoordinateFrame.R01> createPropertyContainerCoordinateFrameR01(PropertyContainer.CoordinateFrame.R01 value) {
        return new JAXBElement<PropertyContainer.CoordinateFrame.R01>(_PropertyContainerCoordinateFrameR01_QNAME, PropertyContainer.CoordinateFrame.R01 .class, PropertyContainer.CoordinateFrame.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PropertyContainer.CoordinateFrame.R12 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "R12", scope = PropertyContainer.CoordinateFrame.class)
    public JAXBElement<PropertyContainer.CoordinateFrame.R12> createPropertyContainerCoordinateFrameR12(PropertyContainer.CoordinateFrame.R12 value) {
        return new JAXBElement<PropertyContainer.CoordinateFrame.R12>(_PropertyContainerCoordinateFrameR12_QNAME, PropertyContainer.CoordinateFrame.R12 .class, PropertyContainer.CoordinateFrame.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PropertyContainer.CoordinateFrame.R00 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "R00", scope = PropertyContainer.CoordinateFrame.class)
    public JAXBElement<PropertyContainer.CoordinateFrame.R00> createPropertyContainerCoordinateFrameR00(PropertyContainer.CoordinateFrame.R00 value) {
        return new JAXBElement<PropertyContainer.CoordinateFrame.R00>(_PropertyContainerCoordinateFrameR00_QNAME, PropertyContainer.CoordinateFrame.R00 .class, PropertyContainer.CoordinateFrame.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PropertyContainer.CoordinateFrame.R11 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "R11", scope = PropertyContainer.CoordinateFrame.class)
    public JAXBElement<PropertyContainer.CoordinateFrame.R11> createPropertyContainerCoordinateFrameR11(PropertyContainer.CoordinateFrame.R11 value) {
        return new JAXBElement<PropertyContainer.CoordinateFrame.R11>(_PropertyContainerCoordinateFrameR11_QNAME, PropertyContainer.CoordinateFrame.R11 .class, PropertyContainer.CoordinateFrame.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PropertyContainer.CoordinateFrame.R22 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "R22", scope = PropertyContainer.CoordinateFrame.class)
    public JAXBElement<PropertyContainer.CoordinateFrame.R22> createPropertyContainerCoordinateFrameR22(PropertyContainer.CoordinateFrame.R22 value) {
        return new JAXBElement<PropertyContainer.CoordinateFrame.R22>(_PropertyContainerCoordinateFrameR22_QNAME, PropertyContainer.CoordinateFrame.R22 .class, PropertyContainer.CoordinateFrame.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PropertyContainer.CoordinateFrame.R02 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "R02", scope = PropertyContainer.CoordinateFrame.class)
    public JAXBElement<PropertyContainer.CoordinateFrame.R02> createPropertyContainerCoordinateFrameR02(PropertyContainer.CoordinateFrame.R02 value) {
        return new JAXBElement<PropertyContainer.CoordinateFrame.R02>(_PropertyContainerCoordinateFrameR02_QNAME, PropertyContainer.CoordinateFrame.R02 .class, PropertyContainer.CoordinateFrame.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PropertyContainer.CoordinateFrame.X }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "X", scope = PropertyContainer.CoordinateFrame.class)
    public JAXBElement<PropertyContainer.CoordinateFrame.X> createPropertyContainerCoordinateFrameX(PropertyContainer.CoordinateFrame.X value) {
        return new JAXBElement<PropertyContainer.CoordinateFrame.X>(_PropertyContainerCoordinateFrameX_QNAME, PropertyContainer.CoordinateFrame.X.class, PropertyContainer.CoordinateFrame.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PropertyContainer.CoordinateFrame.Y }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Y", scope = PropertyContainer.CoordinateFrame.class)
    public JAXBElement<PropertyContainer.CoordinateFrame.Y> createPropertyContainerCoordinateFrameY(PropertyContainer.CoordinateFrame.Y value) {
        return new JAXBElement<PropertyContainer.CoordinateFrame.Y>(_PropertyContainerCoordinateFrameY_QNAME, PropertyContainer.CoordinateFrame.Y.class, PropertyContainer.CoordinateFrame.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PropertyContainer.CoordinateFrame.Z }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Z", scope = PropertyContainer.CoordinateFrame.class)
    public JAXBElement<PropertyContainer.CoordinateFrame.Z> createPropertyContainerCoordinateFrameZ(PropertyContainer.CoordinateFrame.Z value) {
        return new JAXBElement<PropertyContainer.CoordinateFrame.Z>(_PropertyContainerCoordinateFrameZ_QNAME, PropertyContainer.CoordinateFrame.Z.class, PropertyContainer.CoordinateFrame.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "null", scope = PropertyContainer.Content.class)
    public JAXBElement<Object> createPropertyContainerContentNull(Object value) {
        return new JAXBElement<Object>(_PropertyContainerContentNull_QNAME, Object.class, PropertyContainer.Content.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PropertyContainer.Content.Binary }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "binary", scope = PropertyContainer.Content.class)
    public JAXBElement<PropertyContainer.Content.Binary> createPropertyContainerContentBinary(PropertyContainer.Content.Binary value) {
        return new JAXBElement<PropertyContainer.Content.Binary>(_PropertyContainerContentBinary_QNAME, PropertyContainer.Content.Binary.class, PropertyContainer.Content.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.lang.String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "url", scope = PropertyContainer.Content.class)
    public JAXBElement<java.lang.String> createPropertyContainerContentUrl(java.lang.String value) {
        return new JAXBElement<java.lang.String>(_PropertyContainerContentUrl_QNAME, java.lang.String.class, PropertyContainer.Content.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.lang.String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "hash", scope = PropertyContainer.Content.class)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<java.lang.String> createPropertyContainerContentHash(java.lang.String value) {
        return new JAXBElement<java.lang.String>(_PropertyContainerContentHash_QNAME, java.lang.String.class, PropertyContainer.Content.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PropertyContainer.Color3 .R }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "R", scope = PropertyContainer.Color3 .class)
    public JAXBElement<PropertyContainer.Color3 .R> createPropertyContainerColor3R(PropertyContainer.Color3 .R value) {
        return new JAXBElement<PropertyContainer.Color3 .R>(_PropertyContainerColor3R_QNAME, PropertyContainer.Color3 .R.class, PropertyContainer.Color3 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PropertyContainer.Color3 .B }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "B", scope = PropertyContainer.Color3 .class)
    public JAXBElement<PropertyContainer.Color3 .B> createPropertyContainerColor3B(PropertyContainer.Color3 .B value) {
        return new JAXBElement<PropertyContainer.Color3 .B>(_PropertyContainerColor3B_QNAME, PropertyContainer.Color3 .B.class, PropertyContainer.Color3 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PropertyContainer.Color3 .G }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "G", scope = PropertyContainer.Color3 .class)
    public JAXBElement<PropertyContainer.Color3 .G> createPropertyContainerColor3G(PropertyContainer.Color3 .G value) {
        return new JAXBElement<PropertyContainer.Color3 .G>(_PropertyContainerColor3G_QNAME, PropertyContainer.Color3 .G.class, PropertyContainer.Color3 .class, value);
    }

}
