
package org.oasis_open.docs.ws_sx.ws_trust._200512;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import javax.xml.ws.wsaddressing.W3CEndpointReference;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.oasis_open.docs.ws_sx.ws_trust._200512 package. 
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

    private final static QName _RequestType_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "RequestType");
    private final static QName _RenewTarget_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "RenewTarget");
    private final static QName _Delegatable_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "Delegatable");
    private final static QName _AllowPostdating_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "AllowPostdating");
    private final static QName _DelegateTo_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "DelegateTo");
    private final static QName _Renewing_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "Renewing");
    private final static QName _ValidateTarget_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "ValidateTarget");
    private final static QName _Status_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "Status");
    private final static QName _RequestSecurityTokenResponseCollection_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "RequestSecurityTokenResponseCollection");
    private final static QName _KeyType_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "KeyType");
    private final static QName _BinaryExchange_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "BinaryExchange");
    private final static QName _SignatureAlgorithm_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "SignatureAlgorithm");
    private final static QName _Participants_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "Participants");
    private final static QName _UseKey_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "UseKey");
    private final static QName _Lifetime_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "Lifetime");
    private final static QName _RequestSecurityTokenResponse_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "RequestSecurityTokenResponse");
    private final static QName _RequestSecurityToken_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "RequestSecurityToken");
    private final static QName _TokenType_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "TokenType");
    private final static QName _RequestedSecurityToken_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "RequestedSecurityToken");
    private final static QName _Issuer_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "Issuer");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.oasis_open.docs.ws_sx.ws_trust._200512
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link StatusType }
     * 
     */
    public StatusType createStatusType() {
        return new StatusType();
    }

    /**
     * Create an instance of {@link ValidateTargetType }
     * 
     */
    public ValidateTargetType createValidateTargetType() {
        return new ValidateTargetType();
    }

    /**
     * Create an instance of {@link RequestedSecurityTokenType }
     * 
     */
    public RequestedSecurityTokenType createRequestedSecurityTokenType() {
        return new RequestedSecurityTokenType();
    }

    /**
     * Create an instance of {@link RenewingType }
     * 
     */
    public RenewingType createRenewingType() {
        return new RenewingType();
    }

    /**
     * Create an instance of {@link DelegateToType }
     * 
     */
    public DelegateToType createDelegateToType() {
        return new DelegateToType();
    }

    /**
     * Create an instance of {@link RequestSecurityTokenResponseCollectionType }
     * 
     */
    public RequestSecurityTokenResponseCollectionType createRequestSecurityTokenResponseCollectionType() {
        return new RequestSecurityTokenResponseCollectionType();
    }

    /**
     * Create an instance of {@link ParticipantsType }
     * 
     */
    public ParticipantsType createParticipantsType() {
        return new ParticipantsType();
    }

    /**
     * Create an instance of {@link BinaryExchangeType }
     * 
     */
    public BinaryExchangeType createBinaryExchangeType() {
        return new BinaryExchangeType();
    }

    /**
     * Create an instance of {@link LifetimeType }
     * 
     */
    public LifetimeType createLifetimeType() {
        return new LifetimeType();
    }

    /**
     * Create an instance of {@link RequestSecurityTokenType }
     * 
     */
    public RequestSecurityTokenType createRequestSecurityTokenType() {
        return new RequestSecurityTokenType();
    }

    /**
     * Create an instance of {@link UseKeyType }
     * 
     */
    public UseKeyType createUseKeyType() {
        return new UseKeyType();
    }

    /**
     * Create an instance of {@link AllowPostdatingType }
     * 
     */
    public AllowPostdatingType createAllowPostdatingType() {
        return new AllowPostdatingType();
    }

    /**
     * Create an instance of {@link RequestSecurityTokenResponseType }
     * 
     */
    public RequestSecurityTokenResponseType createRequestSecurityTokenResponseType() {
        return new RequestSecurityTokenResponseType();
    }

    /**
     * Create an instance of {@link RenewTargetType }
     * 
     */
    public RenewTargetType createRenewTargetType() {
        return new RenewTargetType();
    }

    /**
     * Create an instance of {@link ParticipantType }
     * 
     */
    public ParticipantType createParticipantType() {
        return new ParticipantType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", name = "RequestType")
    public JAXBElement<String> createRequestType(String value) {
        return new JAXBElement<String>(_RequestType_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RenewTargetType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", name = "RenewTarget")
    public JAXBElement<RenewTargetType> createRenewTarget(RenewTargetType value) {
        return new JAXBElement<RenewTargetType>(_RenewTarget_QNAME, RenewTargetType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", name = "Delegatable")
    public JAXBElement<Boolean> createDelegatable(Boolean value) {
        return new JAXBElement<Boolean>(_Delegatable_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AllowPostdatingType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", name = "AllowPostdating")
    public JAXBElement<AllowPostdatingType> createAllowPostdating(AllowPostdatingType value) {
        return new JAXBElement<AllowPostdatingType>(_AllowPostdating_QNAME, AllowPostdatingType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DelegateToType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", name = "DelegateTo")
    public JAXBElement<DelegateToType> createDelegateTo(DelegateToType value) {
        return new JAXBElement<DelegateToType>(_DelegateTo_QNAME, DelegateToType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RenewingType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", name = "Renewing")
    public JAXBElement<RenewingType> createRenewing(RenewingType value) {
        return new JAXBElement<RenewingType>(_Renewing_QNAME, RenewingType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateTargetType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", name = "ValidateTarget")
    public JAXBElement<ValidateTargetType> createValidateTarget(ValidateTargetType value) {
        return new JAXBElement<ValidateTargetType>(_ValidateTarget_QNAME, ValidateTargetType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StatusType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", name = "Status")
    public JAXBElement<StatusType> createStatus(StatusType value) {
        return new JAXBElement<StatusType>(_Status_QNAME, StatusType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestSecurityTokenResponseCollectionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", name = "RequestSecurityTokenResponseCollection")
    public JAXBElement<RequestSecurityTokenResponseCollectionType> createRequestSecurityTokenResponseCollection(RequestSecurityTokenResponseCollectionType value) {
        return new JAXBElement<RequestSecurityTokenResponseCollectionType>(_RequestSecurityTokenResponseCollection_QNAME, RequestSecurityTokenResponseCollectionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", name = "KeyType")
    public JAXBElement<String> createKeyType(String value) {
        return new JAXBElement<String>(_KeyType_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BinaryExchangeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", name = "BinaryExchange")
    public JAXBElement<BinaryExchangeType> createBinaryExchange(BinaryExchangeType value) {
        return new JAXBElement<BinaryExchangeType>(_BinaryExchange_QNAME, BinaryExchangeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", name = "SignatureAlgorithm")
    public JAXBElement<String> createSignatureAlgorithm(String value) {
        return new JAXBElement<String>(_SignatureAlgorithm_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParticipantsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", name = "Participants")
    public JAXBElement<ParticipantsType> createParticipants(ParticipantsType value) {
        return new JAXBElement<ParticipantsType>(_Participants_QNAME, ParticipantsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UseKeyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", name = "UseKey")
    public JAXBElement<UseKeyType> createUseKey(UseKeyType value) {
        return new JAXBElement<UseKeyType>(_UseKey_QNAME, UseKeyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LifetimeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", name = "Lifetime")
    public JAXBElement<LifetimeType> createLifetime(LifetimeType value) {
        return new JAXBElement<LifetimeType>(_Lifetime_QNAME, LifetimeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestSecurityTokenResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", name = "RequestSecurityTokenResponse")
    public JAXBElement<RequestSecurityTokenResponseType> createRequestSecurityTokenResponse(RequestSecurityTokenResponseType value) {
        return new JAXBElement<RequestSecurityTokenResponseType>(_RequestSecurityTokenResponse_QNAME, RequestSecurityTokenResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestSecurityTokenType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", name = "RequestSecurityToken")
    public JAXBElement<RequestSecurityTokenType> createRequestSecurityToken(RequestSecurityTokenType value) {
        return new JAXBElement<RequestSecurityTokenType>(_RequestSecurityToken_QNAME, RequestSecurityTokenType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", name = "TokenType")
    public JAXBElement<String> createTokenType(String value) {
        return new JAXBElement<String>(_TokenType_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestedSecurityTokenType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", name = "RequestedSecurityToken")
    public JAXBElement<RequestedSecurityTokenType> createRequestedSecurityToken(RequestedSecurityTokenType value) {
        return new JAXBElement<RequestedSecurityTokenType>(_RequestedSecurityToken_QNAME, RequestedSecurityTokenType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link W3CEndpointReference }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", name = "Issuer")
    public JAXBElement<W3CEndpointReference> createIssuer(W3CEndpointReference value) {
        return new JAXBElement<W3CEndpointReference>(_Issuer_QNAME, W3CEndpointReference.class, null, value);
    }

}
