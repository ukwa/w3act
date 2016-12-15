
package com.thesecretserver.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.thesecretserver.service package. 
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

    private final static QName _GetUserScriptResult_QNAME = new QName("urn:thesecretserver.com", "GetUserScriptResult");
    private final static QName _ImpersonateResult_QNAME = new QName("urn:thesecretserver.com", "ImpersonateResult");
    private final static QName _UserInfoResult_QNAME = new QName("urn:thesecretserver.com", "UserInfoResult");
    private final static QName _GetSecretTemplateFieldsResult_QNAME = new QName("urn:thesecretserver.com", "GetSecretTemplateFieldsResult");
    private final static QName _GetFolderResult_QNAME = new QName("urn:thesecretserver.com", "GetFolderResult");
    private final static QName _SecretPolicyResult_QNAME = new QName("urn:thesecretserver.com", "SecretPolicyResult");
    private final static QName _GetUserResult_QNAME = new QName("urn:thesecretserver.com", "GetUserResult");
    private final static QName _RequestApprovalResult_QNAME = new QName("urn:thesecretserver.com", "RequestApprovalResult");
    private final static QName _WebServiceResult_QNAME = new QName("urn:thesecretserver.com", "WebServiceResult");
    private final static QName _VersionGetResult_QNAME = new QName("urn:thesecretserver.com", "VersionGetResult");
    private final static QName _SecretPolicyForSecretResult_QNAME = new QName("urn:thesecretserver.com", "SecretPolicyForSecretResult");
    private final static QName _SearchSecretsResult_QNAME = new QName("urn:thesecretserver.com", "SearchSecretsResult");
    private final static QName _GeneratePasswordResult_QNAME = new QName("urn:thesecretserver.com", "GeneratePasswordResult");
    private final static QName _GetUsersResult_QNAME = new QName("urn:thesecretserver.com", "GetUsersResult");
    private final static QName _GetCheckOutStatusResult_QNAME = new QName("urn:thesecretserver.com", "GetCheckOutStatusResult");
    private final static QName _GetTicketSystemsResult_QNAME = new QName("urn:thesecretserver.com", "GetTicketSystemsResult");
    private final static QName _FolderExtendedGetResult_QNAME = new QName("urn:thesecretserver.com", "FolderExtendedGetResult");
    private final static QName _GetUserScriptsResult_QNAME = new QName("urn:thesecretserver.com", "GetUserScriptsResult");
    private final static QName _CreateFolderResult_QNAME = new QName("urn:thesecretserver.com", "CreateFolderResult");
    private final static QName _SecretItemHistoryResult_QNAME = new QName("urn:thesecretserver.com", "SecretItemHistoryResult");
    private final static QName _GetSecretAuditResult_QNAME = new QName("urn:thesecretserver.com", "GetSecretAuditResult");
    private final static QName _SearchSecretPoliciesResult_QNAME = new QName("urn:thesecretserver.com", "SearchSecretPoliciesResult");
    private final static QName _AuthenticateResult_QNAME = new QName("urn:thesecretserver.com", "AuthenticateResult");
    private final static QName _AddSecretResult_QNAME = new QName("urn:thesecretserver.com", "AddSecretResult");
    private final static QName _GetDependenciesResult_QNAME = new QName("urn:thesecretserver.com", "GetDependenciesResult");
    private final static QName _FileDownloadResult_QNAME = new QName("urn:thesecretserver.com", "FileDownloadResult");
    private final static QName _GetSecretTemplatesResult_QNAME = new QName("urn:thesecretserver.com", "GetSecretTemplatesResult");
    private final static QName _SearchFolderResult_QNAME = new QName("urn:thesecretserver.com", "SearchFolderResult");
    private final static QName _TokenIsValidResult_QNAME = new QName("urn:thesecretserver.com", "TokenIsValidResult");
    private final static QName _SSHCredentialsResult_QNAME = new QName("urn:thesecretserver.com", "SSHCredentialsResult");
    private final static QName _GetSitesResult_QNAME = new QName("urn:thesecretserver.com", "GetSitesResult");
    private final static QName _GetAllGroupsResult_QNAME = new QName("urn:thesecretserver.com", "GetAllGroupsResult");
    private final static QName _GetFoldersResult_QNAME = new QName("urn:thesecretserver.com", "GetFoldersResult");
    private final static QName _GetSshCommandMenuResult_QNAME = new QName("urn:thesecretserver.com", "GetSshCommandMenuResult");
    private final static QName _GetFavoritesResult_QNAME = new QName("urn:thesecretserver.com", "GetFavoritesResult");
    private final static QName _GetSecretResult_QNAME = new QName("urn:thesecretserver.com", "GetSecretResult");
    private final static QName _GetSecretsByFieldValueResult_QNAME = new QName("urn:thesecretserver.com", "GetSecretsByFieldValueResult");
    private final static QName _SecretActive_QNAME = new QName("urn:thesecretserver.com", "Active");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.thesecretserver.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FolderExtendedGetNew }
     * 
     */
    public FolderExtendedGetNew createFolderExtendedGetNew() {
        return new FolderExtendedGetNew();
    }

    /**
     * Create an instance of {@link FolderExtendedGetNewRequest }
     * 
     */
    public FolderExtendedGetNewRequest createFolderExtendedGetNewRequest() {
        return new FolderExtendedGetNewRequest();
    }

    /**
     * Create an instance of {@link ExpireSecretResponse }
     * 
     */
    public ExpireSecretResponse createExpireSecretResponse() {
        return new ExpireSecretResponse();
    }

    /**
     * Create an instance of {@link WebServiceResult }
     * 
     */
    public WebServiceResult createWebServiceResult() {
        return new WebServiceResult();
    }

    /**
     * Create an instance of {@link GetTicketSystemsResponse }
     * 
     */
    public GetTicketSystemsResponse createGetTicketSystemsResponse() {
        return new GetTicketSystemsResponse();
    }

    /**
     * Create an instance of {@link GetTicketSystemsResult }
     * 
     */
    public GetTicketSystemsResult createGetTicketSystemsResult() {
        return new GetTicketSystemsResult();
    }

    /**
     * Create an instance of {@link SearchSecretsByExposedFieldValue }
     * 
     */
    public SearchSecretsByExposedFieldValue createSearchSecretsByExposedFieldValue() {
        return new SearchSecretsByExposedFieldValue();
    }

    /**
     * Create an instance of {@link AddUserResponse }
     * 
     */
    public AddUserResponse createAddUserResponse() {
        return new AddUserResponse();
    }

    /**
     * Create an instance of {@link GetSecretResult }
     * 
     */
    public GetSecretResult createGetSecretResult() {
        return new GetSecretResult();
    }

    /**
     * Create an instance of {@link ExpireSecret }
     * 
     */
    public ExpireSecret createExpireSecret() {
        return new ExpireSecret();
    }

    /**
     * Create an instance of {@link AddScriptResponse }
     * 
     */
    public AddScriptResponse createAddScriptResponse() {
        return new AddScriptResponse();
    }

    /**
     * Create an instance of {@link UpdateScriptResponse }
     * 
     */
    public UpdateScriptResponse createUpdateScriptResponse() {
        return new UpdateScriptResponse();
    }

    /**
     * Create an instance of {@link UpdateUserScriptResult }
     * 
     */
    public UpdateUserScriptResult createUpdateUserScriptResult() {
        return new UpdateUserScriptResult();
    }

    /**
     * Create an instance of {@link GetUserScriptsResult }
     * 
     */
    public GetUserScriptsResult createGetUserScriptsResult() {
        return new GetUserScriptsResult();
    }

    /**
     * Create an instance of {@link AuthenticateRADIUS }
     * 
     */
    public AuthenticateRADIUS createAuthenticateRADIUS() {
        return new AuthenticateRADIUS();
    }

    /**
     * Create an instance of {@link GetSecretAuditResult }
     * 
     */
    public GetSecretAuditResult createGetSecretAuditResult() {
        return new GetSecretAuditResult();
    }

    /**
     * Create an instance of {@link FolderExtendedGetResponse }
     * 
     */
    public FolderExtendedGetResponse createFolderExtendedGetResponse() {
        return new FolderExtendedGetResponse();
    }

    /**
     * Create an instance of {@link FolderExtendedGetResult }
     * 
     */
    public FolderExtendedGetResult createFolderExtendedGetResult() {
        return new FolderExtendedGetResult();
    }

    /**
     * Create an instance of {@link AddSecretPolicy }
     * 
     */
    public AddSecretPolicy createAddSecretPolicy() {
        return new AddSecretPolicy();
    }

    /**
     * Create an instance of {@link SecretPolicyDetail }
     * 
     */
    public SecretPolicyDetail createSecretPolicyDetail() {
        return new SecretPolicyDetail();
    }

    /**
     * Create an instance of {@link AddSecretResult }
     * 
     */
    public AddSecretResult createAddSecretResult() {
        return new AddSecretResult();
    }

    /**
     * Create an instance of {@link SearchSecretsResponse }
     * 
     */
    public SearchSecretsResponse createSearchSecretsResponse() {
        return new SearchSecretsResponse();
    }

    /**
     * Create an instance of {@link SearchSecretsResult }
     * 
     */
    public SearchSecretsResult createSearchSecretsResult() {
        return new SearchSecretsResult();
    }

    /**
     * Create an instance of {@link GetSecretTemplatesResult }
     * 
     */
    public GetSecretTemplatesResult createGetSecretTemplatesResult() {
        return new GetSecretTemplatesResult();
    }

    /**
     * Create an instance of {@link GetSecretTemplatesResponse }
     * 
     */
    public GetSecretTemplatesResponse createGetSecretTemplatesResponse() {
        return new GetSecretTemplatesResponse();
    }

    /**
     * Create an instance of {@link GetCheckOutStatusResponse }
     * 
     */
    public GetCheckOutStatusResponse createGetCheckOutStatusResponse() {
        return new GetCheckOutStatusResponse();
    }

    /**
     * Create an instance of {@link GetCheckOutStatusResult }
     * 
     */
    public GetCheckOutStatusResult createGetCheckOutStatusResult() {
        return new GetCheckOutStatusResult();
    }

    /**
     * Create an instance of {@link UpdateSecret }
     * 
     */
    public UpdateSecret createUpdateSecret() {
        return new UpdateSecret();
    }

    /**
     * Create an instance of {@link Secret }
     * 
     */
    public Secret createSecret() {
        return new Secret();
    }

    /**
     * Create an instance of {@link UploadFileAttachmentResponse }
     * 
     */
    public UploadFileAttachmentResponse createUploadFileAttachmentResponse() {
        return new UploadFileAttachmentResponse();
    }

    /**
     * Create an instance of {@link GetSecretTemplateFieldsResponse }
     * 
     */
    public GetSecretTemplateFieldsResponse createGetSecretTemplateFieldsResponse() {
        return new GetSecretTemplateFieldsResponse();
    }

    /**
     * Create an instance of {@link GetSecretTemplateFieldsResult }
     * 
     */
    public GetSecretTemplateFieldsResult createGetSecretTemplateFieldsResult() {
        return new GetSecretTemplateFieldsResult();
    }

    /**
     * Create an instance of {@link AddScript }
     * 
     */
    public AddScript createAddScript() {
        return new AddScript();
    }

    /**
     * Create an instance of {@link SearchSecretsByExposedValuesResponse }
     * 
     */
    public SearchSecretsByExposedValuesResponse createSearchSecretsByExposedValuesResponse() {
        return new SearchSecretsByExposedValuesResponse();
    }

    /**
     * Create an instance of {@link GetAllScriptsResponse }
     * 
     */
    public GetAllScriptsResponse createGetAllScriptsResponse() {
        return new GetAllScriptsResponse();
    }

    /**
     * Create an instance of {@link GetSecretPolicyForSecret }
     * 
     */
    public GetSecretPolicyForSecret createGetSecretPolicyForSecret() {
        return new GetSecretPolicyForSecret();
    }

    /**
     * Create an instance of {@link GetSecretsByExposedFieldValue }
     * 
     */
    public GetSecretsByExposedFieldValue createGetSecretsByExposedFieldValue() {
        return new GetSecretsByExposedFieldValue();
    }

    /**
     * Create an instance of {@link GetSecretsByFieldValue }
     * 
     */
    public GetSecretsByFieldValue createGetSecretsByFieldValue() {
        return new GetSecretsByFieldValue();
    }

    /**
     * Create an instance of {@link AuthenticateRADIUSResponse }
     * 
     */
    public AuthenticateRADIUSResponse createAuthenticateRADIUSResponse() {
        return new AuthenticateRADIUSResponse();
    }

    /**
     * Create an instance of {@link AuthenticateResult }
     * 
     */
    public AuthenticateResult createAuthenticateResult() {
        return new AuthenticateResult();
    }

    /**
     * Create an instance of {@link GeneratePasswordResponse }
     * 
     */
    public GeneratePasswordResponse createGeneratePasswordResponse() {
        return new GeneratePasswordResponse();
    }

    /**
     * Create an instance of {@link GeneratePasswordResult }
     * 
     */
    public GeneratePasswordResult createGeneratePasswordResult() {
        return new GeneratePasswordResult();
    }

    /**
     * Create an instance of {@link DeactivateSecret }
     * 
     */
    public DeactivateSecret createDeactivateSecret() {
        return new DeactivateSecret();
    }

    /**
     * Create an instance of {@link AddSecret }
     * 
     */
    public AddSecret createAddSecret() {
        return new AddSecret();
    }

    /**
     * Create an instance of {@link ArrayOfInt }
     * 
     */
    public ArrayOfInt createArrayOfInt() {
        return new ArrayOfInt();
    }

    /**
     * Create an instance of {@link ArrayOfString }
     * 
     */
    public ArrayOfString createArrayOfString() {
        return new ArrayOfString();
    }

    /**
     * Create an instance of {@link UpdateIsFavorite }
     * 
     */
    public UpdateIsFavorite createUpdateIsFavorite() {
        return new UpdateIsFavorite();
    }

    /**
     * Create an instance of {@link FolderExtendedUpdate }
     * 
     */
    public FolderExtendedUpdate createFolderExtendedUpdate() {
        return new FolderExtendedUpdate();
    }

    /**
     * Create an instance of {@link FolderExtended }
     * 
     */
    public FolderExtended createFolderExtended() {
        return new FolderExtended();
    }

    /**
     * Create an instance of {@link DeactivateSecretResponse }
     * 
     */
    public DeactivateSecretResponse createDeactivateSecretResponse() {
        return new DeactivateSecretResponse();
    }

    /**
     * Create an instance of {@link GetFoldersResult }
     * 
     */
    public GetFoldersResult createGetFoldersResult() {
        return new GetFoldersResult();
    }

    /**
     * Create an instance of {@link GetSSHCommandMenu }
     * 
     */
    public GetSSHCommandMenu createGetSSHCommandMenu() {
        return new GetSSHCommandMenu();
    }

    /**
     * Create an instance of {@link ImportXMLResponse }
     * 
     */
    public ImportXMLResponse createImportXMLResponse() {
        return new ImportXMLResponse();
    }

    /**
     * Create an instance of {@link SearchSecretsByFolderLegacy }
     * 
     */
    public SearchSecretsByFolderLegacy createSearchSecretsByFolderLegacy() {
        return new SearchSecretsByFolderLegacy();
    }

    /**
     * Create an instance of {@link ImpersonateUser }
     * 
     */
    public ImpersonateUser createImpersonateUser() {
        return new ImpersonateUser();
    }

    /**
     * Create an instance of {@link GetSSHLoginCredentialsResponse }
     * 
     */
    public GetSSHLoginCredentialsResponse createGetSSHLoginCredentialsResponse() {
        return new GetSSHLoginCredentialsResponse();
    }

    /**
     * Create an instance of {@link SSHCredentialsResult }
     * 
     */
    public SSHCredentialsResult createSSHCredentialsResult() {
        return new SSHCredentialsResult();
    }

    /**
     * Create an instance of {@link GetAllSSHCommandMenusResponse }
     * 
     */
    public GetAllSSHCommandMenusResponse createGetAllSSHCommandMenusResponse() {
        return new GetAllSSHCommandMenusResponse();
    }

    /**
     * Create an instance of {@link GetSshCommandMenusResult }
     * 
     */
    public GetSshCommandMenusResult createGetSshCommandMenusResult() {
        return new GetSshCommandMenusResult();
    }

    /**
     * Create an instance of {@link GetAllScripts }
     * 
     */
    public GetAllScripts createGetAllScripts() {
        return new GetAllScripts();
    }

    /**
     * Create an instance of {@link AssignUserToGroup }
     * 
     */
    public AssignUserToGroup createAssignUserToGroup() {
        return new AssignUserToGroup();
    }

    /**
     * Create an instance of {@link CheckInByKey }
     * 
     */
    public CheckInByKey createCheckInByKey() {
        return new CheckInByKey();
    }

    /**
     * Create an instance of {@link AddSecretResponse }
     * 
     */
    public AddSecretResponse createAddSecretResponse() {
        return new AddSecretResponse();
    }

    /**
     * Create an instance of {@link AddSecretCustomAudit }
     * 
     */
    public AddSecretCustomAudit createAddSecretCustomAudit() {
        return new AddSecretCustomAudit();
    }

    /**
     * Create an instance of {@link ImpersonateUserResponse }
     * 
     */
    public ImpersonateUserResponse createImpersonateUserResponse() {
        return new ImpersonateUserResponse();
    }

    /**
     * Create an instance of {@link ImpersonateResult }
     * 
     */
    public ImpersonateResult createImpersonateResult() {
        return new ImpersonateResult();
    }

    /**
     * Create an instance of {@link WhoAmIResponse }
     * 
     */
    public WhoAmIResponse createWhoAmIResponse() {
        return new WhoAmIResponse();
    }

    /**
     * Create an instance of {@link UserInfoResult }
     * 
     */
    public UserInfoResult createUserInfoResult() {
        return new UserInfoResult();
    }

    /**
     * Create an instance of {@link GetSSHLoginCredentials }
     * 
     */
    public GetSSHLoginCredentials createGetSSHLoginCredentials() {
        return new GetSSHLoginCredentials();
    }

    /**
     * Create an instance of {@link Authenticate }
     * 
     */
    public Authenticate createAuthenticate() {
        return new Authenticate();
    }

    /**
     * Create an instance of {@link SearchSecretsByFolder }
     * 
     */
    public SearchSecretsByFolder createSearchSecretsByFolder() {
        return new SearchSecretsByFolder();
    }

    /**
     * Create an instance of {@link DenySecretAccessRequest }
     * 
     */
    public DenySecretAccessRequest createDenySecretAccessRequest() {
        return new DenySecretAccessRequest();
    }

    /**
     * Create an instance of {@link SearchSecretsLegacyResponse }
     * 
     */
    public SearchSecretsLegacyResponse createSearchSecretsLegacyResponse() {
        return new SearchSecretsLegacyResponse();
    }

    /**
     * Create an instance of {@link GetDependenciesResult }
     * 
     */
    public GetDependenciesResult createGetDependenciesResult() {
        return new GetDependenciesResult();
    }

    /**
     * Create an instance of {@link GetUser }
     * 
     */
    public GetUser createGetUser() {
        return new GetUser();
    }

    /**
     * Create an instance of {@link VersionGetResult }
     * 
     */
    public VersionGetResult createVersionGetResult() {
        return new VersionGetResult();
    }

    /**
     * Create an instance of {@link GetCheckOutStatus }
     * 
     */
    public GetCheckOutStatus createGetCheckOutStatus() {
        return new GetCheckOutStatus();
    }

    /**
     * Create an instance of {@link DownloadFileAttachmentByItemIdResponse }
     * 
     */
    public DownloadFileAttachmentByItemIdResponse createDownloadFileAttachmentByItemIdResponse() {
        return new DownloadFileAttachmentByItemIdResponse();
    }

    /**
     * Create an instance of {@link FileDownloadResult }
     * 
     */
    public FileDownloadResult createFileDownloadResult() {
        return new FileDownloadResult();
    }

    /**
     * Create an instance of {@link AddGroupToActiveDirectorySynchronization }
     * 
     */
    public AddGroupToActiveDirectorySynchronization createAddGroupToActiveDirectorySynchronization() {
        return new AddGroupToActiveDirectorySynchronization();
    }

    /**
     * Create an instance of {@link AddGroupRequestMessage }
     * 
     */
    public AddGroupRequestMessage createAddGroupRequestMessage() {
        return new AddGroupRequestMessage();
    }

    /**
     * Create an instance of {@link SearchSecretsByExposedValues }
     * 
     */
    public SearchSecretsByExposedValues createSearchSecretsByExposedValues() {
        return new SearchSecretsByExposedValues();
    }

    /**
     * Create an instance of {@link GetSecretItemHistoryByFieldName }
     * 
     */
    public GetSecretItemHistoryByFieldName createGetSecretItemHistoryByFieldName() {
        return new GetSecretItemHistoryByFieldName();
    }

    /**
     * Create an instance of {@link ChangePasswordResponse }
     * 
     */
    public ChangePasswordResponse createChangePasswordResponse() {
        return new ChangePasswordResponse();
    }

    /**
     * Create an instance of {@link SearchFoldersResponse }
     * 
     */
    public SearchFoldersResponse createSearchFoldersResponse() {
        return new SearchFoldersResponse();
    }

    /**
     * Create an instance of {@link SearchFolderResult }
     * 
     */
    public SearchFolderResult createSearchFolderResult() {
        return new SearchFolderResult();
    }

    /**
     * Create an instance of {@link GetSitesResult }
     * 
     */
    public GetSitesResult createGetSitesResult() {
        return new GetSitesResult();
    }

    /**
     * Create an instance of {@link GetAllGroupsResult }
     * 
     */
    public GetAllGroupsResult createGetAllGroupsResult() {
        return new GetAllGroupsResult();
    }

    /**
     * Create an instance of {@link SearchSecretsByExposedFieldValueResponse }
     * 
     */
    public SearchSecretsByExposedFieldValueResponse createSearchSecretsByExposedFieldValueResponse() {
        return new SearchSecretsByExposedFieldValueResponse();
    }

    /**
     * Create an instance of {@link FolderExtendedCreate }
     * 
     */
    public FolderExtendedCreate createFolderExtendedCreate() {
        return new FolderExtendedCreate();
    }

    /**
     * Create an instance of {@link DownloadFileAttachmentResponse }
     * 
     */
    public DownloadFileAttachmentResponse createDownloadFileAttachmentResponse() {
        return new DownloadFileAttachmentResponse();
    }

    /**
     * Create an instance of {@link GetSecretItemHistoryByFieldNameResponse }
     * 
     */
    public GetSecretItemHistoryByFieldNameResponse createGetSecretItemHistoryByFieldNameResponse() {
        return new GetSecretItemHistoryByFieldNameResponse();
    }

    /**
     * Create an instance of {@link SecretItemHistoryResult }
     * 
     */
    public SecretItemHistoryResult createSecretItemHistoryResult() {
        return new SecretItemHistoryResult();
    }

    /**
     * Create an instance of {@link GetAllGroups }
     * 
     */
    public GetAllGroups createGetAllGroups() {
        return new GetAllGroups();
    }

    /**
     * Create an instance of {@link GetSecretLegacyResponse }
     * 
     */
    public GetSecretLegacyResponse createGetSecretLegacyResponse() {
        return new GetSecretLegacyResponse();
    }

    /**
     * Create an instance of {@link GetSecretAudit }
     * 
     */
    public GetSecretAudit createGetSecretAudit() {
        return new GetSecretAudit();
    }

    /**
     * Create an instance of {@link RunActiveDirectorySynchronizationResponse }
     * 
     */
    public RunActiveDirectorySynchronizationResponse createRunActiveDirectorySynchronizationResponse() {
        return new RunActiveDirectorySynchronizationResponse();
    }

    /**
     * Create an instance of {@link CheckInResponse }
     * 
     */
    public CheckInResponse createCheckInResponse() {
        return new CheckInResponse();
    }

    /**
     * Create an instance of {@link RequestApprovalResult }
     * 
     */
    public RequestApprovalResult createRequestApprovalResult() {
        return new RequestApprovalResult();
    }

    /**
     * Create an instance of {@link AddDependencyResponse }
     * 
     */
    public AddDependencyResponse createAddDependencyResponse() {
        return new AddDependencyResponse();
    }

    /**
     * Create an instance of {@link GetNewSecretPolicyResponse }
     * 
     */
    public GetNewSecretPolicyResponse createGetNewSecretPolicyResponse() {
        return new GetNewSecretPolicyResponse();
    }

    /**
     * Create an instance of {@link SecretPolicyResult }
     * 
     */
    public SecretPolicyResult createSecretPolicyResult() {
        return new SecretPolicyResult();
    }

    /**
     * Create an instance of {@link RemoveDependency }
     * 
     */
    public RemoveDependency createRemoveDependency() {
        return new RemoveDependency();
    }

    /**
     * Create an instance of {@link GetAllGroupsResponse }
     * 
     */
    public GetAllGroupsResponse createGetAllGroupsResponse() {
        return new GetAllGroupsResponse();
    }

    /**
     * Create an instance of {@link AddNewSecretResponse }
     * 
     */
    public AddNewSecretResponse createAddNewSecretResponse() {
        return new AddNewSecretResponse();
    }

    /**
     * Create an instance of {@link SearchSecretsByFolderResponse }
     * 
     */
    public SearchSecretsByFolderResponse createSearchSecretsByFolderResponse() {
        return new SearchSecretsByFolderResponse();
    }

    /**
     * Create an instance of {@link AssignSiteResponse }
     * 
     */
    public AssignSiteResponse createAssignSiteResponse() {
        return new AssignSiteResponse();
    }

    /**
     * Create an instance of {@link FolderExtendedUpdateResponse }
     * 
     */
    public FolderExtendedUpdateResponse createFolderExtendedUpdateResponse() {
        return new FolderExtendedUpdateResponse();
    }

    /**
     * Create an instance of {@link FolderExtendedUpdateResult }
     * 
     */
    public FolderExtendedUpdateResult createFolderExtendedUpdateResult() {
        return new FolderExtendedUpdateResult();
    }

    /**
     * Create an instance of {@link GetDependencies }
     * 
     */
    public GetDependencies createGetDependencies() {
        return new GetDependencies();
    }

    /**
     * Create an instance of {@link GetUserResponse }
     * 
     */
    public GetUserResponse createGetUserResponse() {
        return new GetUserResponse();
    }

    /**
     * Create an instance of {@link GetUserResult }
     * 
     */
    public GetUserResult createGetUserResult() {
        return new GetUserResult();
    }

    /**
     * Create an instance of {@link ApproveSecretAccessRequest }
     * 
     */
    public ApproveSecretAccessRequest createApproveSecretAccessRequest() {
        return new ApproveSecretAccessRequest();
    }

    /**
     * Create an instance of {@link AddSecretCustomAuditResponse }
     * 
     */
    public AddSecretCustomAuditResponse createAddSecretCustomAuditResponse() {
        return new AddSecretCustomAuditResponse();
    }

    /**
     * Create an instance of {@link CheckInByKeyResponse }
     * 
     */
    public CheckInByKeyResponse createCheckInByKeyResponse() {
        return new CheckInByKeyResponse();
    }

    /**
     * Create an instance of {@link SaveSSHCommandMenuResponse }
     * 
     */
    public SaveSSHCommandMenuResponse createSaveSSHCommandMenuResponse() {
        return new SaveSSHCommandMenuResponse();
    }

    /**
     * Create an instance of {@link GetSshCommandMenuResult }
     * 
     */
    public GetSshCommandMenuResult createGetSshCommandMenuResult() {
        return new GetSshCommandMenuResult();
    }

    /**
     * Create an instance of {@link AssignSecretPolicyForSecretResponse }
     * 
     */
    public AssignSecretPolicyForSecretResponse createAssignSecretPolicyForSecretResponse() {
        return new AssignSecretPolicyForSecretResponse();
    }

    /**
     * Create an instance of {@link SecretPolicyForSecretResult }
     * 
     */
    public SecretPolicyForSecretResult createSecretPolicyForSecretResult() {
        return new SecretPolicyForSecretResult();
    }

    /**
     * Create an instance of {@link AuthenticateResponse }
     * 
     */
    public AuthenticateResponse createAuthenticateResponse() {
        return new AuthenticateResponse();
    }

    /**
     * Create an instance of {@link SaveSSHCommandMenu }
     * 
     */
    public SaveSSHCommandMenu createSaveSSHCommandMenu() {
        return new SaveSSHCommandMenu();
    }

    /**
     * Create an instance of {@link SshCommandMenu }
     * 
     */
    public SshCommandMenu createSshCommandMenu() {
        return new SshCommandMenu();
    }

    /**
     * Create an instance of {@link GetSecret }
     * 
     */
    public GetSecret createGetSecret() {
        return new GetSecret();
    }

    /**
     * Create an instance of {@link ArrayOfCodeResponse }
     * 
     */
    public ArrayOfCodeResponse createArrayOfCodeResponse() {
        return new ArrayOfCodeResponse();
    }

    /**
     * Create an instance of {@link FolderExtendedGet }
     * 
     */
    public FolderExtendedGet createFolderExtendedGet() {
        return new FolderExtendedGet();
    }

    /**
     * Create an instance of {@link RemoveDependencyResponse }
     * 
     */
    public RemoveDependencyResponse createRemoveDependencyResponse() {
        return new RemoveDependencyResponse();
    }

    /**
     * Create an instance of {@link RestoreSSHCommandMenuResponse }
     * 
     */
    public RestoreSSHCommandMenuResponse createRestoreSSHCommandMenuResponse() {
        return new RestoreSSHCommandMenuResponse();
    }

    /**
     * Create an instance of {@link GetNewSecret }
     * 
     */
    public GetNewSecret createGetNewSecret() {
        return new GetNewSecret();
    }

    /**
     * Create an instance of {@link FolderExtendedCreateResponse }
     * 
     */
    public FolderExtendedCreateResponse createFolderExtendedCreateResponse() {
        return new FolderExtendedCreateResponse();
    }

    /**
     * Create an instance of {@link FolderExtendedCreateResult }
     * 
     */
    public FolderExtendedCreateResult createFolderExtendedCreateResult() {
        return new FolderExtendedCreateResult();
    }

    /**
     * Create an instance of {@link AssignSite }
     * 
     */
    public AssignSite createAssignSite() {
        return new AssignSite();
    }

    /**
     * Create an instance of {@link GetNewSecretResponse }
     * 
     */
    public GetNewSecretResponse createGetNewSecretResponse() {
        return new GetNewSecretResponse();
    }

    /**
     * Create an instance of {@link FolderGet }
     * 
     */
    public FolderGet createFolderGet() {
        return new FolderGet();
    }

    /**
     * Create an instance of {@link GetSecretsByFieldValueResult }
     * 
     */
    public GetSecretsByFieldValueResult createGetSecretsByFieldValueResult() {
        return new GetSecretsByFieldValueResult();
    }

    /**
     * Create an instance of {@link GetFavorites }
     * 
     */
    public GetFavorites createGetFavorites() {
        return new GetFavorites();
    }

    /**
     * Create an instance of {@link GetSecretsByFieldValueResponse }
     * 
     */
    public GetSecretsByFieldValueResponse createGetSecretsByFieldValueResponse() {
        return new GetSecretsByFieldValueResponse();
    }

    /**
     * Create an instance of {@link FolderGetAllChildren }
     * 
     */
    public FolderGetAllChildren createFolderGetAllChildren() {
        return new FolderGetAllChildren();
    }

    /**
     * Create an instance of {@link ImportXML }
     * 
     */
    public ImportXML createImportXML() {
        return new ImportXML();
    }

    /**
     * Create an instance of {@link GetSecretTemplateFields }
     * 
     */
    public GetSecretTemplateFields createGetSecretTemplateFields() {
        return new GetSecretTemplateFields();
    }

    /**
     * Create an instance of {@link SearchUsersResponse }
     * 
     */
    public SearchUsersResponse createSearchUsersResponse() {
        return new SearchUsersResponse();
    }

    /**
     * Create an instance of {@link GetUsersResult }
     * 
     */
    public GetUsersResult createGetUsersResult() {
        return new GetUsersResult();
    }

    /**
     * Create an instance of {@link GetSecretResponse }
     * 
     */
    public GetSecretResponse createGetSecretResponse() {
        return new GetSecretResponse();
    }

    /**
     * Create an instance of {@link GeneratePassword }
     * 
     */
    public GeneratePassword createGeneratePassword() {
        return new GeneratePassword();
    }

    /**
     * Create an instance of {@link SearchSecretsByFolderLegacyResponse }
     * 
     */
    public SearchSecretsByFolderLegacyResponse createSearchSecretsByFolderLegacyResponse() {
        return new SearchSecretsByFolderLegacyResponse();
    }

    /**
     * Create an instance of {@link SearchSecretsLegacy }
     * 
     */
    public SearchSecretsLegacy createSearchSecretsLegacy() {
        return new SearchSecretsLegacy();
    }

    /**
     * Create an instance of {@link GetNewSecretPolicy }
     * 
     */
    public GetNewSecretPolicy createGetNewSecretPolicy() {
        return new GetNewSecretPolicy();
    }

    /**
     * Create an instance of {@link AddUser }
     * 
     */
    public AddUser createAddUser() {
        return new AddUser();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link RunActiveDirectorySynchronization }
     * 
     */
    public RunActiveDirectorySynchronization createRunActiveDirectorySynchronization() {
        return new RunActiveDirectorySynchronization();
    }

    /**
     * Create an instance of {@link GetDependenciesResponse }
     * 
     */
    public GetDependenciesResponse createGetDependenciesResponse() {
        return new GetDependenciesResponse();
    }

    /**
     * Create an instance of {@link ChangePassword }
     * 
     */
    public ChangePassword createChangePassword() {
        return new ChangePassword();
    }

    /**
     * Create an instance of {@link GetSecretTemplates }
     * 
     */
    public GetSecretTemplates createGetSecretTemplates() {
        return new GetSecretTemplates();
    }

    /**
     * Create an instance of {@link GetScriptResponse }
     * 
     */
    public GetScriptResponse createGetScriptResponse() {
        return new GetScriptResponse();
    }

    /**
     * Create an instance of {@link GetUserScriptResult }
     * 
     */
    public GetUserScriptResult createGetUserScriptResult() {
        return new GetUserScriptResult();
    }

    /**
     * Create an instance of {@link UpdateSecretPermission }
     * 
     */
    public UpdateSecretPermission createUpdateSecretPermission() {
        return new UpdateSecretPermission();
    }

    /**
     * Create an instance of {@link GroupOrUserRecord }
     * 
     */
    public GroupOrUserRecord createGroupOrUserRecord() {
        return new GroupOrUserRecord();
    }

    /**
     * Create an instance of {@link DownloadFileAttachment }
     * 
     */
    public DownloadFileAttachment createDownloadFileAttachment() {
        return new DownloadFileAttachment();
    }

    /**
     * Create an instance of {@link CreateFolderResult }
     * 
     */
    public CreateFolderResult createCreateFolderResult() {
        return new CreateFolderResult();
    }

    /**
     * Create an instance of {@link AssignUserToGroupResponse }
     * 
     */
    public AssignUserToGroupResponse createAssignUserToGroupResponse() {
        return new AssignUserToGroupResponse();
    }

    /**
     * Create an instance of {@link SearchSecretPoliciesResult }
     * 
     */
    public SearchSecretPoliciesResult createSearchSecretPoliciesResult() {
        return new SearchSecretPoliciesResult();
    }

    /**
     * Create an instance of {@link CheckIn }
     * 
     */
    public CheckIn createCheckIn() {
        return new CheckIn();
    }

    /**
     * Create an instance of {@link SearchSecrets }
     * 
     */
    public SearchSecrets createSearchSecrets() {
        return new SearchSecrets();
    }

    /**
     * Create an instance of {@link TokenIsValidResult }
     * 
     */
    public TokenIsValidResult createTokenIsValidResult() {
        return new TokenIsValidResult();
    }

    /**
     * Create an instance of {@link GetDistributedEnginesResponse }
     * 
     */
    public GetDistributedEnginesResponse createGetDistributedEnginesResponse() {
        return new GetDistributedEnginesResponse();
    }

    /**
     * Create an instance of {@link GetTokenIsValid }
     * 
     */
    public GetTokenIsValid createGetTokenIsValid() {
        return new GetTokenIsValid();
    }

    /**
     * Create an instance of {@link RestoreSSHCommandMenu }
     * 
     */
    public RestoreSSHCommandMenu createRestoreSSHCommandMenu() {
        return new RestoreSSHCommandMenu();
    }

    /**
     * Create an instance of {@link UpdateSecretPermissionResponse }
     * 
     */
    public UpdateSecretPermissionResponse createUpdateSecretPermissionResponse() {
        return new UpdateSecretPermissionResponse();
    }

    /**
     * Create an instance of {@link UploadFileAttachmentByItemIdResponse }
     * 
     */
    public UploadFileAttachmentByItemIdResponse createUploadFileAttachmentByItemIdResponse() {
        return new UploadFileAttachmentByItemIdResponse();
    }

    /**
     * Create an instance of {@link FolderExtendedGetNewResponse }
     * 
     */
    public FolderExtendedGetNewResponse createFolderExtendedGetNewResponse() {
        return new FolderExtendedGetNewResponse();
    }

    /**
     * Create an instance of {@link FolderExtendedGetNewResult }
     * 
     */
    public FolderExtendedGetNewResult createFolderExtendedGetNewResult() {
        return new FolderExtendedGetNewResult();
    }

    /**
     * Create an instance of {@link GetFavoritesResponse }
     * 
     */
    public GetFavoritesResponse createGetFavoritesResponse() {
        return new GetFavoritesResponse();
    }

    /**
     * Create an instance of {@link GetFavoritesResult }
     * 
     */
    public GetFavoritesResult createGetFavoritesResult() {
        return new GetFavoritesResult();
    }

    /**
     * Create an instance of {@link GetAllSSHCommandMenus }
     * 
     */
    public GetAllSSHCommandMenus createGetAllSSHCommandMenus() {
        return new GetAllSSHCommandMenus();
    }

    /**
     * Create an instance of {@link UpdateUserResponse }
     * 
     */
    public UpdateUserResponse createUpdateUserResponse() {
        return new UpdateUserResponse();
    }

    /**
     * Create an instance of {@link UpdateUserResult }
     * 
     */
    public UpdateUserResult createUpdateUserResult() {
        return new UpdateUserResult();
    }

    /**
     * Create an instance of {@link UpdateUser }
     * 
     */
    public UpdateUser createUpdateUser() {
        return new UpdateUser();
    }

    /**
     * Create an instance of {@link VersionGetResponse }
     * 
     */
    public VersionGetResponse createVersionGetResponse() {
        return new VersionGetResponse();
    }

    /**
     * Create an instance of {@link FolderUpdateResponse }
     * 
     */
    public FolderUpdateResponse createFolderUpdateResponse() {
        return new FolderUpdateResponse();
    }

    /**
     * Create an instance of {@link WhoAmI }
     * 
     */
    public WhoAmI createWhoAmI() {
        return new WhoAmI();
    }

    /**
     * Create an instance of {@link SearchFolders }
     * 
     */
    public SearchFolders createSearchFolders() {
        return new SearchFolders();
    }

    /**
     * Create an instance of {@link SearchSecretsByFieldValueResponse }
     * 
     */
    public SearchSecretsByFieldValueResponse createSearchSecretsByFieldValueResponse() {
        return new SearchSecretsByFieldValueResponse();
    }

    /**
     * Create an instance of {@link UploadFileAttachment }
     * 
     */
    public UploadFileAttachment createUploadFileAttachment() {
        return new UploadFileAttachment();
    }

    /**
     * Create an instance of {@link AssignSecretPolicyForSecret }
     * 
     */
    public AssignSecretPolicyForSecret createAssignSecretPolicyForSecret() {
        return new AssignSecretPolicyForSecret();
    }

    /**
     * Create an instance of {@link SecretPolicyForSecret }
     * 
     */
    public SecretPolicyForSecret createSecretPolicyForSecret() {
        return new SecretPolicyForSecret();
    }

    /**
     * Create an instance of {@link FolderGetAllChildrenResponse }
     * 
     */
    public FolderGetAllChildrenResponse createFolderGetAllChildrenResponse() {
        return new FolderGetAllChildrenResponse();
    }

    /**
     * Create an instance of {@link GetSecretAuditResponse }
     * 
     */
    public GetSecretAuditResponse createGetSecretAuditResponse() {
        return new GetSecretAuditResponse();
    }

    /**
     * Create an instance of {@link AddDependency }
     * 
     */
    public AddDependency createAddDependency() {
        return new AddDependency();
    }

    /**
     * Create an instance of {@link Dependency }
     * 
     */
    public Dependency createDependency() {
        return new Dependency();
    }

    /**
     * Create an instance of {@link GetTokenIsValidResponse }
     * 
     */
    public GetTokenIsValidResponse createGetTokenIsValidResponse() {
        return new GetTokenIsValidResponse();
    }

    /**
     * Create an instance of {@link SearchUsers }
     * 
     */
    public SearchUsers createSearchUsers() {
        return new SearchUsers();
    }

    /**
     * Create an instance of {@link GetDistributedEngines }
     * 
     */
    public GetDistributedEngines createGetDistributedEngines() {
        return new GetDistributedEngines();
    }

    /**
     * Create an instance of {@link AddGroupToActiveDirectorySynchronizationResponse }
     * 
     */
    public AddGroupToActiveDirectorySynchronizationResponse createAddGroupToActiveDirectorySynchronizationResponse() {
        return new AddGroupToActiveDirectorySynchronizationResponse();
    }

    /**
     * Create an instance of {@link GetSecretsByExposedFieldValueResponse }
     * 
     */
    public GetSecretsByExposedFieldValueResponse createGetSecretsByExposedFieldValueResponse() {
        return new GetSecretsByExposedFieldValueResponse();
    }

    /**
     * Create an instance of {@link UpdateIsFavoriteResponse }
     * 
     */
    public UpdateIsFavoriteResponse createUpdateIsFavoriteResponse() {
        return new UpdateIsFavoriteResponse();
    }

    /**
     * Create an instance of {@link UpdateSecretResponse }
     * 
     */
    public UpdateSecretResponse createUpdateSecretResponse() {
        return new UpdateSecretResponse();
    }

    /**
     * Create an instance of {@link GetSSHLoginCredentialsWithMachineResponse }
     * 
     */
    public GetSSHLoginCredentialsWithMachineResponse createGetSSHLoginCredentialsWithMachineResponse() {
        return new GetSSHLoginCredentialsWithMachineResponse();
    }

    /**
     * Create an instance of {@link DenySecretAccessRequestResponse }
     * 
     */
    public DenySecretAccessRequestResponse createDenySecretAccessRequestResponse() {
        return new DenySecretAccessRequestResponse();
    }

    /**
     * Create an instance of {@link VersionGet }
     * 
     */
    public VersionGet createVersionGet() {
        return new VersionGet();
    }

    /**
     * Create an instance of {@link GetSSHCommandMenuResponse }
     * 
     */
    public GetSSHCommandMenuResponse createGetSSHCommandMenuResponse() {
        return new GetSSHCommandMenuResponse();
    }

    /**
     * Create an instance of {@link FolderCreate }
     * 
     */
    public FolderCreate createFolderCreate() {
        return new FolderCreate();
    }

    /**
     * Create an instance of {@link SearchSecretPolicies }
     * 
     */
    public SearchSecretPolicies createSearchSecretPolicies() {
        return new SearchSecretPolicies();
    }

    /**
     * Create an instance of {@link SearchSecretPoliciesResponse }
     * 
     */
    public SearchSecretPoliciesResponse createSearchSecretPoliciesResponse() {
        return new SearchSecretPoliciesResponse();
    }

    /**
     * Create an instance of {@link SetCheckOutEnabled }
     * 
     */
    public SetCheckOutEnabled createSetCheckOutEnabled() {
        return new SetCheckOutEnabled();
    }

    /**
     * Create an instance of {@link SearchSecretsByFieldValue }
     * 
     */
    public SearchSecretsByFieldValue createSearchSecretsByFieldValue() {
        return new SearchSecretsByFieldValue();
    }

    /**
     * Create an instance of {@link SetCheckOutEnabledResponse }
     * 
     */
    public SetCheckOutEnabledResponse createSetCheckOutEnabledResponse() {
        return new SetCheckOutEnabledResponse();
    }

    /**
     * Create an instance of {@link GetFolderResult }
     * 
     */
    public GetFolderResult createGetFolderResult() {
        return new GetFolderResult();
    }

    /**
     * Create an instance of {@link FolderUpdate }
     * 
     */
    public FolderUpdate createFolderUpdate() {
        return new FolderUpdate();
    }

    /**
     * Create an instance of {@link Folder }
     * 
     */
    public Folder createFolder() {
        return new Folder();
    }

    /**
     * Create an instance of {@link UpdateScript }
     * 
     */
    public UpdateScript createUpdateScript() {
        return new UpdateScript();
    }

    /**
     * Create an instance of {@link GetSSHLoginCredentialsWithMachine }
     * 
     */
    public GetSSHLoginCredentialsWithMachine createGetSSHLoginCredentialsWithMachine() {
        return new GetSSHLoginCredentialsWithMachine();
    }

    /**
     * Create an instance of {@link GetSecretPolicyForSecretResponse }
     * 
     */
    public GetSecretPolicyForSecretResponse createGetSecretPolicyForSecretResponse() {
        return new GetSecretPolicyForSecretResponse();
    }

    /**
     * Create an instance of {@link GetScript }
     * 
     */
    public GetScript createGetScript() {
        return new GetScript();
    }

    /**
     * Create an instance of {@link FolderCreateResponse }
     * 
     */
    public FolderCreateResponse createFolderCreateResponse() {
        return new FolderCreateResponse();
    }

    /**
     * Create an instance of {@link UploadFileAttachmentByItemId }
     * 
     */
    public UploadFileAttachmentByItemId createUploadFileAttachmentByItemId() {
        return new UploadFileAttachmentByItemId();
    }

    /**
     * Create an instance of {@link GetTicketSystems }
     * 
     */
    public GetTicketSystems createGetTicketSystems() {
        return new GetTicketSystems();
    }

    /**
     * Create an instance of {@link AddNewSecret }
     * 
     */
    public AddNewSecret createAddNewSecret() {
        return new AddNewSecret();
    }

    /**
     * Create an instance of {@link DeleteSSHCommandMenu }
     * 
     */
    public DeleteSSHCommandMenu createDeleteSSHCommandMenu() {
        return new DeleteSSHCommandMenu();
    }

    /**
     * Create an instance of {@link DeleteSSHCommandMenuResponse }
     * 
     */
    public DeleteSSHCommandMenuResponse createDeleteSSHCommandMenuResponse() {
        return new DeleteSSHCommandMenuResponse();
    }

    /**
     * Create an instance of {@link FolderGetResponse }
     * 
     */
    public FolderGetResponse createFolderGetResponse() {
        return new FolderGetResponse();
    }

    /**
     * Create an instance of {@link GetSecretLegacy }
     * 
     */
    public GetSecretLegacy createGetSecretLegacy() {
        return new GetSecretLegacy();
    }

    /**
     * Create an instance of {@link ApproveSecretAccessRequestResponse }
     * 
     */
    public ApproveSecretAccessRequestResponse createApproveSecretAccessRequestResponse() {
        return new ApproveSecretAccessRequestResponse();
    }

    /**
     * Create an instance of {@link AddSecretPolicyResponse }
     * 
     */
    public AddSecretPolicyResponse createAddSecretPolicyResponse() {
        return new AddSecretPolicyResponse();
    }

    /**
     * Create an instance of {@link DownloadFileAttachmentByItemId }
     * 
     */
    public DownloadFileAttachmentByItemId createDownloadFileAttachmentByItemId() {
        return new DownloadFileAttachmentByItemId();
    }

    /**
     * Create an instance of {@link AdditionalDataSshObject }
     * 
     */
    public AdditionalDataSshObject createAdditionalDataSshObject() {
        return new AdditionalDataSshObject();
    }

    /**
     * Create an instance of {@link SecretItemHistoryWebServiceResult }
     * 
     */
    public SecretItemHistoryWebServiceResult createSecretItemHistoryWebServiceResult() {
        return new SecretItemHistoryWebServiceResult();
    }

    /**
     * Create an instance of {@link ArrayOfSecretPolicyItem }
     * 
     */
    public ArrayOfSecretPolicyItem createArrayOfSecretPolicyItem() {
        return new ArrayOfSecretPolicyItem();
    }

    /**
     * Create an instance of {@link ArrayOfSecretItem }
     * 
     */
    public ArrayOfSecretItem createArrayOfSecretItem() {
        return new ArrayOfSecretItem();
    }

    /**
     * Create an instance of {@link Permission }
     * 
     */
    public Permission createPermission() {
        return new Permission();
    }

    /**
     * Create an instance of {@link ArrayOfFolder }
     * 
     */
    public ArrayOfFolder createArrayOfFolder() {
        return new ArrayOfFolder();
    }

    /**
     * Create an instance of {@link AuditSecret }
     * 
     */
    public AuditSecret createAuditSecret() {
        return new AuditSecret();
    }

    /**
     * Create an instance of {@link ArrayOfTicketSystem }
     * 
     */
    public ArrayOfTicketSystem createArrayOfTicketSystem() {
        return new ArrayOfTicketSystem();
    }

    /**
     * Create an instance of {@link ArrayOfUser }
     * 
     */
    public ArrayOfUser createArrayOfUser() {
        return new ArrayOfUser();
    }

    /**
     * Create an instance of {@link SecretPolicyItem }
     * 
     */
    public SecretPolicyItem createSecretPolicyItem() {
        return new SecretPolicyItem();
    }

    /**
     * Create an instance of {@link ArrayOfOdbcConnectionArg }
     * 
     */
    public ArrayOfOdbcConnectionArg createArrayOfOdbcConnectionArg() {
        return new ArrayOfOdbcConnectionArg();
    }

    /**
     * Create an instance of {@link ArrayOfSshScriptArgument2 }
     * 
     */
    public ArrayOfSshScriptArgument2 createArrayOfSshScriptArgument2() {
        return new ArrayOfSshScriptArgument2();
    }

    /**
     * Create an instance of {@link ArrayOfSshCommandMenu }
     * 
     */
    public ArrayOfSshCommandMenu createArrayOfSshCommandMenu() {
        return new ArrayOfSshCommandMenu();
    }

    /**
     * Create an instance of {@link SqlScriptArgument }
     * 
     */
    public SqlScriptArgument createSqlScriptArgument() {
        return new SqlScriptArgument();
    }

    /**
     * Create an instance of {@link ArrayOfSecret }
     * 
     */
    public ArrayOfSecret createArrayOfSecret() {
        return new ArrayOfSecret();
    }

    /**
     * Create an instance of {@link SshCommandMenuAccessPermission }
     * 
     */
    public SshCommandMenuAccessPermission createSshCommandMenuAccessPermission() {
        return new SshCommandMenuAccessPermission();
    }

    /**
     * Create an instance of {@link Site }
     * 
     */
    public Site createSite() {
        return new Site();
    }

    /**
     * Create an instance of {@link PowerShellUserScript }
     * 
     */
    public PowerShellUserScript createPowerShellUserScript() {
        return new PowerShellUserScript();
    }

    /**
     * Create an instance of {@link Group }
     * 
     */
    public Group createGroup() {
        return new Group();
    }

    /**
     * Create an instance of {@link ArrayOfSecretItemHistoryWebServiceResult }
     * 
     */
    public ArrayOfSecretItemHistoryWebServiceResult createArrayOfSecretItemHistoryWebServiceResult() {
        return new ArrayOfSecretItemHistoryWebServiceResult();
    }

    /**
     * Create an instance of {@link ArrayOfSecretSummary }
     * 
     */
    public ArrayOfSecretSummary createArrayOfSecretSummary() {
        return new ArrayOfSecretSummary();
    }

    /**
     * Create an instance of {@link SecretItem }
     * 
     */
    public SecretItem createSecretItem() {
        return new SecretItem();
    }

    /**
     * Create an instance of {@link ArrayOfGroup }
     * 
     */
    public ArrayOfGroup createArrayOfGroup() {
        return new ArrayOfGroup();
    }

    /**
     * Create an instance of {@link FolderPermission }
     * 
     */
    public FolderPermission createFolderPermission() {
        return new FolderPermission();
    }

    /**
     * Create an instance of {@link UserGroupMap }
     * 
     */
    public UserGroupMap createUserGroupMap() {
        return new UserGroupMap();
    }

    /**
     * Create an instance of {@link AdditionalDependencyInfoJson }
     * 
     */
    public AdditionalDependencyInfoJson createAdditionalDependencyInfoJson() {
        return new AdditionalDependencyInfoJson();
    }

    /**
     * Create an instance of {@link FolderSettings }
     * 
     */
    public FolderSettings createFolderSettings() {
        return new FolderSettings();
    }

    /**
     * Create an instance of {@link SecretPolicySummary }
     * 
     */
    public SecretPolicySummary createSecretPolicySummary() {
        return new SecretPolicySummary();
    }

    /**
     * Create an instance of {@link SecretTemplate }
     * 
     */
    public SecretTemplate createSecretTemplate() {
        return new SecretTemplate();
    }

    /**
     * Create an instance of {@link ArrayOfAuditSecret }
     * 
     */
    public ArrayOfAuditSecret createArrayOfAuditSecret() {
        return new ArrayOfAuditSecret();
    }

    /**
     * Create an instance of {@link ArrayOfSshScriptArgument }
     * 
     */
    public ArrayOfSshScriptArgument createArrayOfSshScriptArgument() {
        return new ArrayOfSshScriptArgument();
    }

    /**
     * Create an instance of {@link SecretSettings }
     * 
     */
    public SecretSettings createSecretSettings() {
        return new SecretSettings();
    }

    /**
     * Create an instance of {@link OdbcConnectionArg }
     * 
     */
    public OdbcConnectionArg createOdbcConnectionArg() {
        return new OdbcConnectionArg();
    }

    /**
     * Create an instance of {@link ArrayOfPermission }
     * 
     */
    public ArrayOfPermission createArrayOfPermission() {
        return new ArrayOfPermission();
    }

    /**
     * Create an instance of {@link SiteConnector }
     * 
     */
    public SiteConnector createSiteConnector() {
        return new SiteConnector();
    }

    /**
     * Create an instance of {@link SshCommandMenuGroupMap }
     * 
     */
    public SshCommandMenuGroupMap createSshCommandMenuGroupMap() {
        return new SshCommandMenuGroupMap();
    }

    /**
     * Create an instance of {@link CodeResponse }
     * 
     */
    public CodeResponse createCodeResponse() {
        return new CodeResponse();
    }

    /**
     * Create an instance of {@link SshUserScript }
     * 
     */
    public SshUserScript createSshUserScript() {
        return new SshUserScript();
    }

    /**
     * Create an instance of {@link SecretSummary }
     * 
     */
    public SecretSummary createSecretSummary() {
        return new SecretSummary();
    }

    /**
     * Create an instance of {@link GenericResult }
     * 
     */
    public GenericResult createGenericResult() {
        return new GenericResult();
    }

    /**
     * Create an instance of {@link ArrayOfSshCommandMenuGroupMap }
     * 
     */
    public ArrayOfSshCommandMenuGroupMap createArrayOfSshCommandMenuGroupMap() {
        return new ArrayOfSshCommandMenuGroupMap();
    }

    /**
     * Create an instance of {@link ApprovalInfo }
     * 
     */
    public ApprovalInfo createApprovalInfo() {
        return new ApprovalInfo();
    }

    /**
     * Create an instance of {@link ArrayOfFolderPermission }
     * 
     */
    public ArrayOfFolderPermission createArrayOfFolderPermission() {
        return new ArrayOfFolderPermission();
    }

    /**
     * Create an instance of {@link ArrayOfSecretField }
     * 
     */
    public ArrayOfSecretField createArrayOfSecretField() {
        return new ArrayOfSecretField();
    }

    /**
     * Create an instance of {@link SqlScriptArgument2 }
     * 
     */
    public SqlScriptArgument2 createSqlScriptArgument2() {
        return new SqlScriptArgument2();
    }

    /**
     * Create an instance of {@link ArrayOfSite }
     * 
     */
    public ArrayOfSite createArrayOfSite() {
        return new ArrayOfSite();
    }

    /**
     * Create an instance of {@link SecretField }
     * 
     */
    public SecretField createSecretField() {
        return new SecretField();
    }

    /**
     * Create an instance of {@link ArrayOfSecretTemplate }
     * 
     */
    public ArrayOfSecretTemplate createArrayOfSecretTemplate() {
        return new ArrayOfSecretTemplate();
    }

    /**
     * Create an instance of {@link ArrayOfGroupOrUserRecord }
     * 
     */
    public ArrayOfGroupOrUserRecord createArrayOfGroupOrUserRecord() {
        return new ArrayOfGroupOrUserRecord();
    }

    /**
     * Create an instance of {@link AdditionalDataSqlObject }
     * 
     */
    public AdditionalDataSqlObject createAdditionalDataSqlObject() {
        return new AdditionalDataSqlObject();
    }

    /**
     * Create an instance of {@link ArrayOfDependency }
     * 
     */
    public ArrayOfDependency createArrayOfDependency() {
        return new ArrayOfDependency();
    }

    /**
     * Create an instance of {@link TicketSystem }
     * 
     */
    public TicketSystem createTicketSystem() {
        return new TicketSystem();
    }

    /**
     * Create an instance of {@link FolderPermissions }
     * 
     */
    public FolderPermissions createFolderPermissions() {
        return new FolderPermissions();
    }

    /**
     * Create an instance of {@link SecretError }
     * 
     */
    public SecretError createSecretError() {
        return new SecretError();
    }

    /**
     * Create an instance of {@link ArrayOfSecretPolicySummary }
     * 
     */
    public ArrayOfSecretPolicySummary createArrayOfSecretPolicySummary() {
        return new ArrayOfSecretPolicySummary();
    }

    /**
     * Create an instance of {@link SshScriptArgument }
     * 
     */
    public SshScriptArgument createSshScriptArgument() {
        return new SshScriptArgument();
    }

    /**
     * Create an instance of {@link ArrayOfSqlScriptArgument2 }
     * 
     */
    public ArrayOfSqlScriptArgument2 createArrayOfSqlScriptArgument2() {
        return new ArrayOfSqlScriptArgument2();
    }

    /**
     * Create an instance of {@link ArrayOfSshCommandMenuAccessPermission }
     * 
     */
    public ArrayOfSshCommandMenuAccessPermission createArrayOfSshCommandMenuAccessPermission() {
        return new ArrayOfSshCommandMenuAccessPermission();
    }

    /**
     * Create an instance of {@link FolderExtendedResultBase }
     * 
     */
    public FolderExtendedResultBase createFolderExtendedResultBase() {
        return new FolderExtendedResultBase();
    }

    /**
     * Create an instance of {@link ArrayOfUserGroupMap }
     * 
     */
    public ArrayOfUserGroupMap createArrayOfUserGroupMap() {
        return new ArrayOfUserGroupMap();
    }

    /**
     * Create an instance of {@link SqlUserScript }
     * 
     */
    public SqlUserScript createSqlUserScript() {
        return new SqlUserScript();
    }

    /**
     * Create an instance of {@link SshScriptArgument2 }
     * 
     */
    public SshScriptArgument2 createSshScriptArgument2() {
        return new SshScriptArgument2();
    }

    /**
     * Create an instance of {@link ArrayOfSqlScriptArgument }
     * 
     */
    public ArrayOfSqlScriptArgument createArrayOfSqlScriptArgument() {
        return new ArrayOfSqlScriptArgument();
    }

    /**
     * Create an instance of {@link ArrayOfUserScript }
     * 
     */
    public ArrayOfUserScript createArrayOfUserScript() {
        return new ArrayOfUserScript();
    }

    /**
     * Create an instance of {@link SecretPermissions }
     * 
     */
    public SecretPermissions createSecretPermissions() {
        return new SecretPermissions();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserScriptResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "GetUserScriptResult")
    public JAXBElement<GetUserScriptResult> createGetUserScriptResult(GetUserScriptResult value) {
        return new JAXBElement<GetUserScriptResult>(_GetUserScriptResult_QNAME, GetUserScriptResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ImpersonateResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "ImpersonateResult")
    public JAXBElement<ImpersonateResult> createImpersonateResult(ImpersonateResult value) {
        return new JAXBElement<ImpersonateResult>(_ImpersonateResult_QNAME, ImpersonateResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserInfoResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "UserInfoResult")
    public JAXBElement<UserInfoResult> createUserInfoResult(UserInfoResult value) {
        return new JAXBElement<UserInfoResult>(_UserInfoResult_QNAME, UserInfoResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSecretTemplateFieldsResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "GetSecretTemplateFieldsResult")
    public JAXBElement<GetSecretTemplateFieldsResult> createGetSecretTemplateFieldsResult(GetSecretTemplateFieldsResult value) {
        return new JAXBElement<GetSecretTemplateFieldsResult>(_GetSecretTemplateFieldsResult_QNAME, GetSecretTemplateFieldsResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFolderResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "GetFolderResult")
    public JAXBElement<GetFolderResult> createGetFolderResult(GetFolderResult value) {
        return new JAXBElement<GetFolderResult>(_GetFolderResult_QNAME, GetFolderResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SecretPolicyResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "SecretPolicyResult")
    public JAXBElement<SecretPolicyResult> createSecretPolicyResult(SecretPolicyResult value) {
        return new JAXBElement<SecretPolicyResult>(_SecretPolicyResult_QNAME, SecretPolicyResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "GetUserResult")
    public JAXBElement<GetUserResult> createGetUserResult(GetUserResult value) {
        return new JAXBElement<GetUserResult>(_GetUserResult_QNAME, GetUserResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestApprovalResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "RequestApprovalResult")
    public JAXBElement<RequestApprovalResult> createRequestApprovalResult(RequestApprovalResult value) {
        return new JAXBElement<RequestApprovalResult>(_RequestApprovalResult_QNAME, RequestApprovalResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WebServiceResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "WebServiceResult")
    public JAXBElement<WebServiceResult> createWebServiceResult(WebServiceResult value) {
        return new JAXBElement<WebServiceResult>(_WebServiceResult_QNAME, WebServiceResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VersionGetResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "VersionGetResult")
    public JAXBElement<VersionGetResult> createVersionGetResult(VersionGetResult value) {
        return new JAXBElement<VersionGetResult>(_VersionGetResult_QNAME, VersionGetResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SecretPolicyForSecretResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "SecretPolicyForSecretResult")
    public JAXBElement<SecretPolicyForSecretResult> createSecretPolicyForSecretResult(SecretPolicyForSecretResult value) {
        return new JAXBElement<SecretPolicyForSecretResult>(_SecretPolicyForSecretResult_QNAME, SecretPolicyForSecretResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SearchSecretsResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "SearchSecretsResult")
    public JAXBElement<SearchSecretsResult> createSearchSecretsResult(SearchSecretsResult value) {
        return new JAXBElement<SearchSecretsResult>(_SearchSecretsResult_QNAME, SearchSecretsResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GeneratePasswordResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "GeneratePasswordResult")
    public JAXBElement<GeneratePasswordResult> createGeneratePasswordResult(GeneratePasswordResult value) {
        return new JAXBElement<GeneratePasswordResult>(_GeneratePasswordResult_QNAME, GeneratePasswordResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUsersResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "GetUsersResult")
    public JAXBElement<GetUsersResult> createGetUsersResult(GetUsersResult value) {
        return new JAXBElement<GetUsersResult>(_GetUsersResult_QNAME, GetUsersResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCheckOutStatusResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "GetCheckOutStatusResult")
    public JAXBElement<GetCheckOutStatusResult> createGetCheckOutStatusResult(GetCheckOutStatusResult value) {
        return new JAXBElement<GetCheckOutStatusResult>(_GetCheckOutStatusResult_QNAME, GetCheckOutStatusResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTicketSystemsResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "GetTicketSystemsResult")
    public JAXBElement<GetTicketSystemsResult> createGetTicketSystemsResult(GetTicketSystemsResult value) {
        return new JAXBElement<GetTicketSystemsResult>(_GetTicketSystemsResult_QNAME, GetTicketSystemsResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FolderExtendedGetResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "FolderExtendedGetResult")
    public JAXBElement<FolderExtendedGetResult> createFolderExtendedGetResult(FolderExtendedGetResult value) {
        return new JAXBElement<FolderExtendedGetResult>(_FolderExtendedGetResult_QNAME, FolderExtendedGetResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserScriptsResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "GetUserScriptsResult")
    public JAXBElement<GetUserScriptsResult> createGetUserScriptsResult(GetUserScriptsResult value) {
        return new JAXBElement<GetUserScriptsResult>(_GetUserScriptsResult_QNAME, GetUserScriptsResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateFolderResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "CreateFolderResult")
    public JAXBElement<CreateFolderResult> createCreateFolderResult(CreateFolderResult value) {
        return new JAXBElement<CreateFolderResult>(_CreateFolderResult_QNAME, CreateFolderResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SecretItemHistoryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "SecretItemHistoryResult")
    public JAXBElement<SecretItemHistoryResult> createSecretItemHistoryResult(SecretItemHistoryResult value) {
        return new JAXBElement<SecretItemHistoryResult>(_SecretItemHistoryResult_QNAME, SecretItemHistoryResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSecretAuditResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "GetSecretAuditResult")
    public JAXBElement<GetSecretAuditResult> createGetSecretAuditResult(GetSecretAuditResult value) {
        return new JAXBElement<GetSecretAuditResult>(_GetSecretAuditResult_QNAME, GetSecretAuditResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SearchSecretPoliciesResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "SearchSecretPoliciesResult")
    public JAXBElement<SearchSecretPoliciesResult> createSearchSecretPoliciesResult(SearchSecretPoliciesResult value) {
        return new JAXBElement<SearchSecretPoliciesResult>(_SearchSecretPoliciesResult_QNAME, SearchSecretPoliciesResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthenticateResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "AuthenticateResult")
    public JAXBElement<AuthenticateResult> createAuthenticateResult(AuthenticateResult value) {
        return new JAXBElement<AuthenticateResult>(_AuthenticateResult_QNAME, AuthenticateResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddSecretResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "AddSecretResult")
    public JAXBElement<AddSecretResult> createAddSecretResult(AddSecretResult value) {
        return new JAXBElement<AddSecretResult>(_AddSecretResult_QNAME, AddSecretResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDependenciesResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "GetDependenciesResult")
    public JAXBElement<GetDependenciesResult> createGetDependenciesResult(GetDependenciesResult value) {
        return new JAXBElement<GetDependenciesResult>(_GetDependenciesResult_QNAME, GetDependenciesResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FileDownloadResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "FileDownloadResult")
    public JAXBElement<FileDownloadResult> createFileDownloadResult(FileDownloadResult value) {
        return new JAXBElement<FileDownloadResult>(_FileDownloadResult_QNAME, FileDownloadResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSecretTemplatesResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "GetSecretTemplatesResult")
    public JAXBElement<GetSecretTemplatesResult> createGetSecretTemplatesResult(GetSecretTemplatesResult value) {
        return new JAXBElement<GetSecretTemplatesResult>(_GetSecretTemplatesResult_QNAME, GetSecretTemplatesResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SearchFolderResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "SearchFolderResult")
    public JAXBElement<SearchFolderResult> createSearchFolderResult(SearchFolderResult value) {
        return new JAXBElement<SearchFolderResult>(_SearchFolderResult_QNAME, SearchFolderResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TokenIsValidResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "TokenIsValidResult")
    public JAXBElement<TokenIsValidResult> createTokenIsValidResult(TokenIsValidResult value) {
        return new JAXBElement<TokenIsValidResult>(_TokenIsValidResult_QNAME, TokenIsValidResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SSHCredentialsResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "SSHCredentialsResult")
    public JAXBElement<SSHCredentialsResult> createSSHCredentialsResult(SSHCredentialsResult value) {
        return new JAXBElement<SSHCredentialsResult>(_SSHCredentialsResult_QNAME, SSHCredentialsResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSitesResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "GetSitesResult")
    public JAXBElement<GetSitesResult> createGetSitesResult(GetSitesResult value) {
        return new JAXBElement<GetSitesResult>(_GetSitesResult_QNAME, GetSitesResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllGroupsResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "GetAllGroupsResult")
    public JAXBElement<GetAllGroupsResult> createGetAllGroupsResult(GetAllGroupsResult value) {
        return new JAXBElement<GetAllGroupsResult>(_GetAllGroupsResult_QNAME, GetAllGroupsResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFoldersResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "GetFoldersResult")
    public JAXBElement<GetFoldersResult> createGetFoldersResult(GetFoldersResult value) {
        return new JAXBElement<GetFoldersResult>(_GetFoldersResult_QNAME, GetFoldersResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSshCommandMenuResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "GetSshCommandMenuResult")
    public JAXBElement<GetSshCommandMenuResult> createGetSshCommandMenuResult(GetSshCommandMenuResult value) {
        return new JAXBElement<GetSshCommandMenuResult>(_GetSshCommandMenuResult_QNAME, GetSshCommandMenuResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFavoritesResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "GetFavoritesResult")
    public JAXBElement<GetFavoritesResult> createGetFavoritesResult(GetFavoritesResult value) {
        return new JAXBElement<GetFavoritesResult>(_GetFavoritesResult_QNAME, GetFavoritesResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSecretResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "GetSecretResult")
    public JAXBElement<GetSecretResult> createGetSecretResult(GetSecretResult value) {
        return new JAXBElement<GetSecretResult>(_GetSecretResult_QNAME, GetSecretResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSecretsByFieldValueResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "GetSecretsByFieldValueResult")
    public JAXBElement<GetSecretsByFieldValueResult> createGetSecretsByFieldValueResult(GetSecretsByFieldValueResult value) {
        return new JAXBElement<GetSecretsByFieldValueResult>(_GetSecretsByFieldValueResult_QNAME, GetSecretsByFieldValueResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:thesecretserver.com", name = "Active", scope = Secret.class)
    public JAXBElement<Boolean> createSecretActive(Boolean value) {
        return new JAXBElement<Boolean>(_SecretActive_QNAME, Boolean.class, Secret.class, value);
    }

}
