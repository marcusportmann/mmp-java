
package vim25service;

import java.util.Calendar;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import vim25.AlarmSpec;
import vim25.AlarmState;
import vim25.AnswerFile;
import vim25.AnswerFileCreateSpec;
import vim25.AnswerFileStatusResult;
import vim25.ApplyProfile;
import vim25.ClusterConfigSpec;
import vim25.ClusterConfigSpecEx;
import vim25.ClusterDasAdvancedRuntimeInfo;
import vim25.ClusterEnterMaintenanceResult;
import vim25.ClusterHostRecommendation;
import vim25.ClusterIoFilterInfo;
import vim25.ClusterProfileConfigSpec;
import vim25.ClusterResourceUsageSummary;
import vim25.ClusterRuleInfo;
import vim25.ComplianceResult;
import vim25.ComputeResourceConfigSpec;
import vim25.ConfigTarget;
import vim25.CustomFieldDef;
import vim25.CustomizationSpec;
import vim25.CustomizationSpecItem;
import vim25.DVPortConfigSpec;
import vim25.DVPortgroupConfigSpec;
import vim25.DVSCapability;
import vim25.DVSConfigSpec;
import vim25.DVSCreateSpec;
import vim25.DVSFeatureCapability;
import vim25.DVSHealthCheckConfig;
import vim25.DVSManagerDvsConfigTarget;
import vim25.DVSNetworkResourcePoolConfigSpec;
import vim25.DatabaseSizeEstimate;
import vim25.DatabaseSizeParam;
import vim25.DatacenterConfigSpec;
import vim25.DatastoreMountPathDatastorePair;
import vim25.DiagnosticManagerLogDescriptor;
import vim25.DiagnosticManagerLogHeader;
import vim25.DiskChangeInfo;
import vim25.DistributedVirtualPort;
import vim25.DistributedVirtualSwitchHostProductSpec;
import vim25.DistributedVirtualSwitchManagerCompatibilityResult;
import vim25.DistributedVirtualSwitchManagerDvsProductSpec;
import vim25.DistributedVirtualSwitchManagerHostContainer;
import vim25.DistributedVirtualSwitchManagerHostDvsFilterSpec;
import vim25.DistributedVirtualSwitchPortCriteria;
import vim25.DistributedVirtualSwitchProductSpec;
import vim25.DvsVmVnicResourcePoolConfigSpec;
import vim25.EntityBackupConfig;
import vim25.EntityPrivilege;
import vim25.EnvironmentBrowserConfigOptionQuerySpec;
import vim25.Event;
import vim25.EventArgDesc;
import vim25.EventFilterSpec;
import vim25.Extension;
import vim25.ExtensionManagerIpAllocationUsage;
import vim25.FaultToleranceConfigSpec;
import vim25.FcoeConfigFcoeSpecification;
import vim25.FileTransferInformation;
import vim25.GuestAliases;
import vim25.GuestAuthAliasInfo;
import vim25.GuestAuthSubject;
import vim25.GuestAuthentication;
import vim25.GuestFileAttributes;
import vim25.GuestListFileInfo;
import vim25.GuestMappedAliases;
import vim25.GuestProcessInfo;
import vim25.GuestProgramSpec;
import vim25.GuestRegKeyNameSpec;
import vim25.GuestRegKeyRecordSpec;
import vim25.GuestRegValueNameSpec;
import vim25.GuestRegValueSpec;
import vim25.HostAccessControlEntry;
import vim25.HostAccessMode;
import vim25.HostAccountSpec;
import vim25.HostApplyProfile;
import vim25.HostAutoStartManagerConfig;
import vim25.HostBootDeviceInfo;
import vim25.HostCacheConfigurationSpec;
import vim25.HostCapability;
import vim25.HostConfigSpec;
import vim25.HostConnectInfo;
import vim25.HostConnectSpec;
import vim25.HostDatastoreBrowserSearchSpec;
import vim25.HostDatastoreSystemVvolDatastoreSpec;
import vim25.HostDateTimeConfig;
import vim25.HostDateTimeSystemTimeZone;
import vim25.HostDiagnosticPartition;
import vim25.HostDiagnosticPartitionCreateDescription;
import vim25.HostDiagnosticPartitionCreateOption;
import vim25.HostDiagnosticPartitionCreateSpec;
import vim25.HostDiskDimensionsChs;
import vim25.HostDiskPartitionBlockRange;
import vim25.HostDiskPartitionInfo;
import vim25.HostDiskPartitionLayout;
import vim25.HostDiskPartitionSpec;
import vim25.HostDnsConfig;
import vim25.HostEsxAgentHostManagerConfigInfo;
import vim25.HostFirewallDefaultPolicy;
import vim25.HostFirewallRulesetRulesetSpec;
import vim25.HostFlagInfo;
import vim25.HostImageProfileSummary;
import vim25.HostInternetScsiHbaAuthenticationProperties;
import vim25.HostInternetScsiHbaDigestProperties;
import vim25.HostInternetScsiHbaDiscoveryProperties;
import vim25.HostInternetScsiHbaIPProperties;
import vim25.HostInternetScsiHbaParamValue;
import vim25.HostInternetScsiHbaSendTarget;
import vim25.HostInternetScsiHbaStaticTarget;
import vim25.HostInternetScsiHbaTargetSet;
import vim25.HostIpConfig;
import vim25.HostIpRouteConfig;
import vim25.HostIpRouteTableConfig;
import vim25.HostIpmiInfo;
import vim25.HostLockdownMode;
import vim25.HostMaintenanceSpec;
import vim25.HostMultipathInfoLogicalUnitPolicy;
import vim25.HostNasVolumeSpec;
import vim25.HostNasVolumeUserInfo;
import vim25.HostNetworkConfig;
import vim25.HostNetworkConfigResult;
import vim25.HostPatchManagerLocator;
import vim25.HostPatchManagerPatchManagerOperationSpec;
import vim25.HostPathSelectionPolicyOption;
import vim25.HostPciPassthruConfig;
import vim25.HostPortGroupSpec;
import vim25.HostProfileConfigSpec;
import vim25.HostProfileManagerConfigTaskList;
import vim25.HostScsiDisk;
import vim25.HostScsiDiskPartition;
import vim25.HostServiceTicket;
import vim25.HostSnmpConfigSpec;
import vim25.HostStorageArrayTypePolicyOption;
import vim25.HostSystemReconnectSpec;
import vim25.HostSystemResourceInfo;
import vim25.HostSystemSwapConfiguration;
import vim25.HostTpmAttestationReport;
import vim25.HostUnresolvedVmfsResignatureSpec;
import vim25.HostUnresolvedVmfsResolutionResult;
import vim25.HostUnresolvedVmfsResolutionSpec;
import vim25.HostUnresolvedVmfsVolume;
import vim25.HostVFlashManagerVFlashCacheConfigSpec;
import vim25.HostVFlashManagerVFlashResourceConfigSpec;
import vim25.HostVMotionCompatibility;
import vim25.HostVffsSpec;
import vim25.HostVffsVolume;
import vim25.HostVirtualNicSpec;
import vim25.HostVirtualSwitchSpec;
import vim25.HostVmfsSpec;
import vim25.HostVmfsVolume;
import vim25.HostVsanInternalSystemCmmdsQuery;
import vim25.HostVsanInternalSystemDeleteVsanObjectsResult;
import vim25.HostVsanInternalSystemVsanObjectOperationResult;
import vim25.HostVsanInternalSystemVsanPhysicalDiskDiagnosticsResult;
import vim25.HttpNfcLeaseManifestEntry;
import vim25.ImportSpec;
import vim25.IoFilterQueryIssueResult;
import vim25.IpPool;
import vim25.IpPoolManagerIpAllocation;
import vim25.IscsiMigrationDependency;
import vim25.IscsiPortInfo;
import vim25.IscsiStatus;
import vim25.KernelModuleInfo;
import vim25.KeyValue;
import vim25.LicenseAssignmentManagerLicenseAssignment;
import vim25.LicenseAvailabilityInfo;
import vim25.LicenseFeatureInfo;
import vim25.LicenseManagerLicenseInfo;
import vim25.LicenseSource;
import vim25.LicenseUsageInfo;
import vim25.LocalizableMessage;
import vim25.LocalizedMethodFault;
import vim25.ManagedObjectReference;
import vim25.ObjectContent;
import vim25.ObjectFactory;
import vim25.OptionValue;
import vim25.OvfCreateDescriptorParams;
import vim25.OvfCreateDescriptorResult;
import vim25.OvfCreateImportSpecParams;
import vim25.OvfCreateImportSpecResult;
import vim25.OvfParseDescriptorParams;
import vim25.OvfParseDescriptorResult;
import vim25.OvfValidateHostParams;
import vim25.OvfValidateHostResult;
import vim25.PerfCompositeMetric;
import vim25.PerfCounterInfo;
import vim25.PerfEntityMetricBase;
import vim25.PerfInterval;
import vim25.PerfMetricId;
import vim25.PerfProviderSummary;
import vim25.PerfQuerySpec;
import vim25.PerformanceManagerCounterLevelMapping;
import vim25.Permission;
import vim25.PhysicalNicHintInfo;
import vim25.PhysicalNicLinkInfo;
import vim25.PlacementResult;
import vim25.PlacementSpec;
import vim25.PrivilegePolicyDef;
import vim25.ProductComponentInfo;
import vim25.ProfileCreateSpec;
import vim25.ProfileDeferredPolicyOptionParameter;
import vim25.ProfileDescription;
import vim25.ProfileExecuteResult;
import vim25.ProfileExpressionMetadata;
import vim25.ProfileMetadata;
import vim25.ProfilePolicyMetadata;
import vim25.ProfileProfileStructure;
import vim25.PropertyFilterSpec;
import vim25.ResourceConfigOption;
import vim25.ResourceConfigSpec;
import vim25.RetrieveOptions;
import vim25.RetrieveResult;
import vim25.ScheduledTaskSpec;
import vim25.SelectionSet;
import vim25.ServiceContent;
import vim25.ServiceManagerServiceInfo;
import vim25.SessionManagerGenericServiceTicket;
import vim25.SessionManagerLocalTicket;
import vim25.SessionManagerServiceRequestSpec;
import vim25.StorageDrsConfigSpec;
import vim25.StorageIORMConfigOption;
import vim25.StorageIORMConfigSpec;
import vim25.StoragePerformanceSummary;
import vim25.StoragePlacementResult;
import vim25.StoragePlacementSpec;
import vim25.TaskFilterSpec;
import vim25.TaskInfo;
import vim25.TaskInfoState;
import vim25.UpdateSet;
import vim25.UserSearchResult;
import vim25.UserSession;
import vim25.VAppCloneSpec;
import vim25.VAppConfigSpec;
import vim25.VMwareDvsLacpGroupSpec;
import vim25.VRPEditSpec;
import vim25.VirtualAppLinkInfo;
import vim25.VirtualDisk;
import vim25.VirtualDiskId;
import vim25.VirtualDiskSpec;
import vim25.VirtualDiskVFlashCacheConfigInfo;
import vim25.VirtualMachineCloneSpec;
import vim25.VirtualMachineConfigInfo;
import vim25.VirtualMachineConfigOption;
import vim25.VirtualMachineConfigOptionDescriptor;
import vim25.VirtualMachineConfigSpec;
import vim25.VirtualMachineDisplayTopology;
import vim25.VirtualMachineMemoryReservationSpec;
import vim25.VirtualMachineMksTicket;
import vim25.VirtualMachineMovePriority;
import vim25.VirtualMachinePowerState;
import vim25.VirtualMachineProfileSpec;
import vim25.VirtualMachineRelocateSpec;
import vim25.VirtualMachineTicket;
import vim25.VirtualNicManagerNetConfig;
import vim25.VirtualResourcePoolSpec;
import vim25.VirtualResourcePoolUsage;
import vim25.VmfsDatastoreCreateSpec;
import vim25.VmfsDatastoreExpandSpec;
import vim25.VmfsDatastoreExtendSpec;
import vim25.VmfsDatastoreOption;
import vim25.VsanHostClusterStatus;
import vim25.VsanHostConfigInfo;
import vim25.VsanHostDiskMapping;
import vim25.VsanHostDiskResult;
import vim25.VsanNewPolicyBatch;
import vim25.VsanPolicyChangeBatch;
import vim25.VsanPolicySatisfiability;
import vim25.VsanUpgradeSystemPreflightCheckResult;
import vim25.VsanUpgradeSystemUpgradeStatus;
import vim25.WaitOptions;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "VimPortType", targetNamespace = "urn:vim25")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface VimPortType {


    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "DestroyPropertyFilter", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DestroyPropertyFilter", targetNamespace = "urn:vim25", className = "vim25.DestroyPropertyFilterRequestType")
    @ResponseWrapper(localName = "DestroyPropertyFilterResponse", targetNamespace = "urn:vim25", className = "vim25.DestroyPropertyFilterResponse")
    public void destroyPropertyFilter(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @param partialUpdates
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidPropertyFaultMsg
     */
    @WebMethod(operationName = "CreateFilter", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateFilter", targetNamespace = "urn:vim25", className = "vim25.CreateFilterRequestType")
    @ResponseWrapper(localName = "CreateFilterResponse", targetNamespace = "urn:vim25", className = "vim25.CreateFilterResponse")
    public ManagedObjectReference createFilter(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        PropertyFilterSpec spec,
        @WebParam(name = "partialUpdates", targetNamespace = "urn:vim25")
        boolean partialUpdates)
        throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param specSet
     * @return
     *     returns java.util.List<vim25.ObjectContent>
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidPropertyFaultMsg
     */
    @WebMethod(operationName = "RetrieveProperties", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RetrieveProperties", targetNamespace = "urn:vim25", className = "vim25.RetrievePropertiesRequestType")
    @ResponseWrapper(localName = "RetrievePropertiesResponse", targetNamespace = "urn:vim25", className = "vim25.RetrievePropertiesResponse")
    public List<ObjectContent> retrieveProperties(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "specSet", targetNamespace = "urn:vim25")
        List<PropertyFilterSpec> specSet)
        throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param version
     * @return
     *     returns vim25.UpdateSet
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidCollectorVersionFaultMsg
     */
    @WebMethod(operationName = "CheckForUpdates", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CheckForUpdates", targetNamespace = "urn:vim25", className = "vim25.CheckForUpdatesRequestType")
    @ResponseWrapper(localName = "CheckForUpdatesResponse", targetNamespace = "urn:vim25", className = "vim25.CheckForUpdatesResponse")
    public UpdateSet checkForUpdates(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "version", targetNamespace = "urn:vim25")
        String version)
        throws InvalidCollectorVersionFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param version
     * @return
     *     returns vim25.UpdateSet
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidCollectorVersionFaultMsg
     */
    @WebMethod(operationName = "WaitForUpdates", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "WaitForUpdates", targetNamespace = "urn:vim25", className = "vim25.WaitForUpdatesRequestType")
    @ResponseWrapper(localName = "WaitForUpdatesResponse", targetNamespace = "urn:vim25", className = "vim25.WaitForUpdatesResponse")
    public UpdateSet waitForUpdates(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "version", targetNamespace = "urn:vim25")
        String version)
        throws InvalidCollectorVersionFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "CancelWaitForUpdates", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "CancelWaitForUpdates", targetNamespace = "urn:vim25", className = "vim25.CancelWaitForUpdatesRequestType")
    @ResponseWrapper(localName = "CancelWaitForUpdatesResponse", targetNamespace = "urn:vim25", className = "vim25.CancelWaitForUpdatesResponse")
    public void cancelWaitForUpdates(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param options
     * @param _this
     * @param version
     * @return
     *     returns vim25.UpdateSet
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidCollectorVersionFaultMsg
     */
    @WebMethod(operationName = "WaitForUpdatesEx", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "WaitForUpdatesEx", targetNamespace = "urn:vim25", className = "vim25.WaitForUpdatesExRequestType")
    @ResponseWrapper(localName = "WaitForUpdatesExResponse", targetNamespace = "urn:vim25", className = "vim25.WaitForUpdatesExResponse")
    public UpdateSet waitForUpdatesEx(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "version", targetNamespace = "urn:vim25")
        String version,
        @WebParam(name = "options", targetNamespace = "urn:vim25")
        WaitOptions options)
        throws InvalidCollectorVersionFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param options
     * @param _this
     * @param specSet
     * @return
     *     returns vim25.RetrieveResult
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidPropertyFaultMsg
     */
    @WebMethod(operationName = "RetrievePropertiesEx", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RetrievePropertiesEx", targetNamespace = "urn:vim25", className = "vim25.RetrievePropertiesExRequestType")
    @ResponseWrapper(localName = "RetrievePropertiesExResponse", targetNamespace = "urn:vim25", className = "vim25.RetrievePropertiesExResponse")
    public RetrieveResult retrievePropertiesEx(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "specSet", targetNamespace = "urn:vim25")
        List<PropertyFilterSpec> specSet,
        @WebParam(name = "options", targetNamespace = "urn:vim25")
        RetrieveOptions options)
        throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param token
     * @return
     *     returns vim25.RetrieveResult
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidPropertyFaultMsg
     */
    @WebMethod(operationName = "ContinueRetrievePropertiesEx", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ContinueRetrievePropertiesEx", targetNamespace = "urn:vim25", className = "vim25.ContinueRetrievePropertiesExRequestType")
    @ResponseWrapper(localName = "ContinueRetrievePropertiesExResponse", targetNamespace = "urn:vim25", className = "vim25.ContinueRetrievePropertiesExResponse")
    public RetrieveResult continueRetrievePropertiesEx(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "token", targetNamespace = "urn:vim25")
        String token)
        throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param token
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidPropertyFaultMsg
     */
    @WebMethod(operationName = "CancelRetrievePropertiesEx", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "CancelRetrievePropertiesEx", targetNamespace = "urn:vim25", className = "vim25.CancelRetrievePropertiesExRequestType")
    @ResponseWrapper(localName = "CancelRetrievePropertiesExResponse", targetNamespace = "urn:vim25", className = "vim25.CancelRetrievePropertiesExResponse")
    public void cancelRetrievePropertiesEx(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "token", targetNamespace = "urn:vim25")
        String token)
        throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "CreatePropertyCollector", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreatePropertyCollector", targetNamespace = "urn:vim25", className = "vim25.CreatePropertyCollectorRequestType")
    @ResponseWrapper(localName = "CreatePropertyCollectorResponse", targetNamespace = "urn:vim25", className = "vim25.CreatePropertyCollectorResponse")
    public ManagedObjectReference createPropertyCollector(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "DestroyPropertyCollector", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DestroyPropertyCollector", targetNamespace = "urn:vim25", className = "vim25.DestroyPropertyCollectorRequestType")
    @ResponseWrapper(localName = "DestroyPropertyCollectorResponse", targetNamespace = "urn:vim25", className = "vim25.DestroyPropertyCollectorResponse")
    public void destroyPropertyCollector(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param privIds
     * @param _this
     * @return
     *     returns int
     * @throws RuntimeFaultFaultMsg
     * @throws AlreadyExistsFaultMsg
     * @throws InvalidNameFaultMsg
     */
    @WebMethod(operationName = "AddAuthorizationRole", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "AddAuthorizationRole", targetNamespace = "urn:vim25", className = "vim25.AddAuthorizationRoleRequestType")
    @ResponseWrapper(localName = "AddAuthorizationRoleResponse", targetNamespace = "urn:vim25", className = "vim25.AddAuthorizationRoleResponse")
    public int addAuthorizationRole(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "privIds", targetNamespace = "urn:vim25")
        List<String> privIds)
        throws AlreadyExistsFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param roleId
     * @param _this
     * @param failIfUsed
     * @throws RuntimeFaultFaultMsg
     * @throws RemoveFailedFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "RemoveAuthorizationRole", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RemoveAuthorizationRole", targetNamespace = "urn:vim25", className = "vim25.RemoveAuthorizationRoleRequestType")
    @ResponseWrapper(localName = "RemoveAuthorizationRoleResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveAuthorizationRoleResponse")
    public void removeAuthorizationRole(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "roleId", targetNamespace = "urn:vim25")
        int roleId,
        @WebParam(name = "failIfUsed", targetNamespace = "urn:vim25")
        boolean failIfUsed)
        throws NotFoundFaultMsg, RemoveFailedFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param newName
     * @param roleId
     * @param privIds
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws AlreadyExistsFaultMsg
     * @throws InvalidNameFaultMsg
     */
    @WebMethod(operationName = "UpdateAuthorizationRole", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateAuthorizationRole", targetNamespace = "urn:vim25", className = "vim25.UpdateAuthorizationRoleRequestType")
    @ResponseWrapper(localName = "UpdateAuthorizationRoleResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateAuthorizationRoleResponse")
    public void updateAuthorizationRole(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "roleId", targetNamespace = "urn:vim25")
        int roleId,
        @WebParam(name = "newName", targetNamespace = "urn:vim25")
        String newName,
        @WebParam(name = "privIds", targetNamespace = "urn:vim25")
        List<String> privIds)
        throws AlreadyExistsFaultMsg, InvalidNameFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param dstRoleId
     * @param _this
     * @param srcRoleId
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws AuthMinimumAdminPermissionFaultMsg
     */
    @WebMethod(operationName = "MergePermissions", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "MergePermissions", targetNamespace = "urn:vim25", className = "vim25.MergePermissionsRequestType")
    @ResponseWrapper(localName = "MergePermissionsResponse", targetNamespace = "urn:vim25", className = "vim25.MergePermissionsResponse")
    public void mergePermissions(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "srcRoleId", targetNamespace = "urn:vim25")
        int srcRoleId,
        @WebParam(name = "dstRoleId", targetNamespace = "urn:vim25")
        int dstRoleId)
        throws AuthMinimumAdminPermissionFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param roleId
     * @param _this
     * @return
     *     returns java.util.List<vim25.Permission>
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "RetrieveRolePermissions", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RetrieveRolePermissions", targetNamespace = "urn:vim25", className = "vim25.RetrieveRolePermissionsRequestType")
    @ResponseWrapper(localName = "RetrieveRolePermissionsResponse", targetNamespace = "urn:vim25", className = "vim25.RetrieveRolePermissionsResponse")
    public List<Permission> retrieveRolePermissions(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "roleId", targetNamespace = "urn:vim25")
        int roleId)
        throws NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param inherited
     * @param _this
     * @param entity
     * @return
     *     returns java.util.List<vim25.Permission>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RetrieveEntityPermissions", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RetrieveEntityPermissions", targetNamespace = "urn:vim25", className = "vim25.RetrieveEntityPermissionsRequestType")
    @ResponseWrapper(localName = "RetrieveEntityPermissionsResponse", targetNamespace = "urn:vim25", className = "vim25.RetrieveEntityPermissionsResponse")
    public List<Permission> retrieveEntityPermissions(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        ManagedObjectReference entity,
        @WebParam(name = "inherited", targetNamespace = "urn:vim25")
        boolean inherited)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.util.List<vim25.Permission>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RetrieveAllPermissions", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RetrieveAllPermissions", targetNamespace = "urn:vim25", className = "vim25.RetrieveAllPermissionsRequestType")
    @ResponseWrapper(localName = "RetrieveAllPermissionsResponse", targetNamespace = "urn:vim25", className = "vim25.RetrieveAllPermissionsResponse")
    public List<Permission> retrieveAllPermissions(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param permission
     * @param _this
     * @param entity
     * @throws UserNotFoundFaultMsg
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws AuthMinimumAdminPermissionFaultMsg
     */
    @WebMethod(operationName = "SetEntityPermissions", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "SetEntityPermissions", targetNamespace = "urn:vim25", className = "vim25.SetEntityPermissionsRequestType")
    @ResponseWrapper(localName = "SetEntityPermissionsResponse", targetNamespace = "urn:vim25", className = "vim25.SetEntityPermissionsResponse")
    public void setEntityPermissions(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        ManagedObjectReference entity,
        @WebParam(name = "permission", targetNamespace = "urn:vim25")
        List<Permission> permission)
        throws AuthMinimumAdminPermissionFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg, UserNotFoundFaultMsg
    ;

    /**
     * 
     * @param permission
     * @param _this
     * @param entity
     * @throws UserNotFoundFaultMsg
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws AuthMinimumAdminPermissionFaultMsg
     */
    @WebMethod(operationName = "ResetEntityPermissions", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ResetEntityPermissions", targetNamespace = "urn:vim25", className = "vim25.ResetEntityPermissionsRequestType")
    @ResponseWrapper(localName = "ResetEntityPermissionsResponse", targetNamespace = "urn:vim25", className = "vim25.ResetEntityPermissionsResponse")
    public void resetEntityPermissions(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        ManagedObjectReference entity,
        @WebParam(name = "permission", targetNamespace = "urn:vim25")
        List<Permission> permission)
        throws AuthMinimumAdminPermissionFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg, UserNotFoundFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param isGroup
     * @param user
     * @param entity
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws AuthMinimumAdminPermissionFaultMsg
     */
    @WebMethod(operationName = "RemoveEntityPermission", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RemoveEntityPermission", targetNamespace = "urn:vim25", className = "vim25.RemoveEntityPermissionRequestType")
    @ResponseWrapper(localName = "RemoveEntityPermissionResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveEntityPermissionResponse")
    public void removeEntityPermission(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        ManagedObjectReference entity,
        @WebParam(name = "user", targetNamespace = "urn:vim25")
        String user,
        @WebParam(name = "isGroup", targetNamespace = "urn:vim25")
        boolean isGroup)
        throws AuthMinimumAdminPermissionFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param sessionId
     * @param _this
     * @param privId
     * @param entity
     * @return
     *     returns java.util.List<java.lang.Boolean>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "HasPrivilegeOnEntity", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "HasPrivilegeOnEntity", targetNamespace = "urn:vim25", className = "vim25.HasPrivilegeOnEntityRequestType")
    @ResponseWrapper(localName = "HasPrivilegeOnEntityResponse", targetNamespace = "urn:vim25", className = "vim25.HasPrivilegeOnEntityResponse")
    public List<Boolean> hasPrivilegeOnEntity(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        ManagedObjectReference entity,
        @WebParam(name = "sessionId", targetNamespace = "urn:vim25")
        String sessionId,
        @WebParam(name = "privId", targetNamespace = "urn:vim25")
        List<String> privId)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param sessionId
     * @param _this
     * @param privId
     * @param entity
     * @return
     *     returns java.util.List<vim25.EntityPrivilege>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "HasPrivilegeOnEntities", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "HasPrivilegeOnEntities", targetNamespace = "urn:vim25", className = "vim25.HasPrivilegeOnEntitiesRequestType")
    @ResponseWrapper(localName = "HasPrivilegeOnEntitiesResponse", targetNamespace = "urn:vim25", className = "vim25.HasPrivilegeOnEntitiesResponse")
    public List<EntityPrivilege> hasPrivilegeOnEntities(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> entity,
        @WebParam(name = "sessionId", targetNamespace = "urn:vim25")
        String sessionId,
        @WebParam(name = "privId", targetNamespace = "urn:vim25")
        List<String> privId)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "CertMgrRefreshCACertificatesAndCRLs_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CertMgrRefreshCACertificatesAndCRLs_Task", targetNamespace = "urn:vim25", className = "vim25.CertMgrRefreshCACertificatesAndCRLsRequestType")
    @ResponseWrapper(localName = "CertMgrRefreshCACertificatesAndCRLs_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CertMgrRefreshCACertificatesAndCRLsTaskResponse")
    public ManagedObjectReference certMgrRefreshCACertificatesAndCRLsTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> host)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "CertMgrRefreshCertificates_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CertMgrRefreshCertificates_Task", targetNamespace = "urn:vim25", className = "vim25.CertMgrRefreshCertificatesRequestType")
    @ResponseWrapper(localName = "CertMgrRefreshCertificates_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CertMgrRefreshCertificatesTaskResponse")
    public ManagedObjectReference certMgrRefreshCertificatesTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> host)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "CertMgrRevokeCertificates_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CertMgrRevokeCertificates_Task", targetNamespace = "urn:vim25", className = "vim25.CertMgrRevokeCertificatesRequestType")
    @ResponseWrapper(localName = "CertMgrRevokeCertificates_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CertMgrRevokeCertificatesTaskResponse")
    public ManagedObjectReference certMgrRevokeCertificatesTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> host)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param modify
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ReconfigureCluster_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ReconfigureCluster_Task", targetNamespace = "urn:vim25", className = "vim25.ReconfigureClusterRequestType")
    @ResponseWrapper(localName = "ReconfigureCluster_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ReconfigureClusterTaskResponse")
    public ManagedObjectReference reconfigureClusterTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        ClusterConfigSpec spec,
        @WebParam(name = "modify", targetNamespace = "urn:vim25")
        boolean modify)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param key
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ApplyRecommendation", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ApplyRecommendation", targetNamespace = "urn:vim25", className = "vim25.ApplyRecommendationRequestType")
    @ResponseWrapper(localName = "ApplyRecommendationResponse", targetNamespace = "urn:vim25", className = "vim25.ApplyRecommendationResponse")
    public void applyRecommendation(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "key", targetNamespace = "urn:vim25")
        String key)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param key
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "CancelRecommendation", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "CancelRecommendation", targetNamespace = "urn:vim25", className = "vim25.CancelRecommendationRequestType")
    @ResponseWrapper(localName = "CancelRecommendationResponse", targetNamespace = "urn:vim25", className = "vim25.CancelRecommendationResponse")
    public void cancelRecommendation(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "key", targetNamespace = "urn:vim25")
        String key)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vm
     * @param pool
     * @param _this
     * @return
     *     returns java.util.List<vim25.ClusterHostRecommendation>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RecommendHostsForVm", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RecommendHostsForVm", targetNamespace = "urn:vim25", className = "vim25.RecommendHostsForVmRequestType")
    @ResponseWrapper(localName = "RecommendHostsForVmResponse", targetNamespace = "urn:vim25", className = "vim25.RecommendHostsForVmResponse")
    public List<ClusterHostRecommendation> recommendHostsForVm(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "pool", targetNamespace = "urn:vim25")
        ManagedObjectReference pool)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param license
     * @param asConnected
     * @param _this
     * @param spec
     * @param resourcePool
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws HostConnectFaultFaultMsg
     * @throws InvalidLoginFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "AddHost_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "AddHost_Task", targetNamespace = "urn:vim25", className = "vim25.AddHostRequestType")
    @ResponseWrapper(localName = "AddHost_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.AddHostTaskResponse")
    public ManagedObjectReference addHostTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        HostConnectSpec spec,
        @WebParam(name = "asConnected", targetNamespace = "urn:vim25")
        boolean asConnected,
        @WebParam(name = "resourcePool", targetNamespace = "urn:vim25")
        ManagedObjectReference resourcePool,
        @WebParam(name = "license", targetNamespace = "urn:vim25")
        String license)
        throws DuplicateNameFaultMsg, HostConnectFaultFaultMsg, InvalidLoginFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws TooManyHostsFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "MoveInto_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "MoveInto_Task", targetNamespace = "urn:vim25", className = "vim25.MoveIntoRequestType")
    @ResponseWrapper(localName = "MoveInto_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.MoveIntoTaskResponse")
    public ManagedObjectReference moveIntoTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> host)
        throws DuplicateNameFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TooManyHostsFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @param resourcePool
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws TooManyHostsFaultMsg
     */
    @WebMethod(operationName = "MoveHostInto_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "MoveHostInto_Task", targetNamespace = "urn:vim25", className = "vim25.MoveHostIntoRequestType")
    @ResponseWrapper(localName = "MoveHostInto_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.MoveHostIntoTaskResponse")
    public ManagedObjectReference moveHostIntoTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host,
        @WebParam(name = "resourcePool", targetNamespace = "urn:vim25")
        ManagedObjectReference resourcePool)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TooManyHostsFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RefreshRecommendation", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RefreshRecommendation", targetNamespace = "urn:vim25", className = "vim25.RefreshRecommendationRequestType")
    @ResponseWrapper(localName = "RefreshRecommendationResponse", targetNamespace = "urn:vim25", className = "vim25.RefreshRecommendationResponse")
    public void refreshRecommendation(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "EvcManager", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "EvcManager", targetNamespace = "urn:vim25", className = "vim25.EvcManagerRequestType")
    @ResponseWrapper(localName = "EvcManagerResponse", targetNamespace = "urn:vim25", className = "vim25.EvcManagerResponse")
    public ManagedObjectReference evcManager(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ClusterDasAdvancedRuntimeInfo
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RetrieveDasAdvancedRuntimeInfo", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RetrieveDasAdvancedRuntimeInfo", targetNamespace = "urn:vim25", className = "vim25.RetrieveDasAdvancedRuntimeInfoRequestType")
    @ResponseWrapper(localName = "RetrieveDasAdvancedRuntimeInfoResponse", targetNamespace = "urn:vim25", className = "vim25.RetrieveDasAdvancedRuntimeInfoResponse")
    public ClusterDasAdvancedRuntimeInfo retrieveDasAdvancedRuntimeInfo(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @param option
     * @return
     *     returns vim25.ClusterEnterMaintenanceResult
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ClusterEnterMaintenanceMode", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ClusterEnterMaintenanceMode", targetNamespace = "urn:vim25", className = "vim25.ClusterEnterMaintenanceModeRequestType")
    @ResponseWrapper(localName = "ClusterEnterMaintenanceModeResponse", targetNamespace = "urn:vim25", className = "vim25.ClusterEnterMaintenanceModeResponse")
    public ClusterEnterMaintenanceResult clusterEnterMaintenanceMode(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> host,
        @WebParam(name = "option", targetNamespace = "urn:vim25")
        List<OptionValue> option)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param placementSpec
     * @param _this
     * @return
     *     returns vim25.PlacementResult
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "PlaceVm", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "PlaceVm", targetNamespace = "urn:vim25", className = "vim25.PlaceVmRequestType")
    @ResponseWrapper(localName = "PlaceVmResponse", targetNamespace = "urn:vim25", className = "vim25.PlaceVmResponse")
    public PlacementResult placeVm(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "placementSpec", targetNamespace = "urn:vim25")
        PlacementSpec placementSpec)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vm
     * @param _this
     * @return
     *     returns java.util.List<vim25.ClusterRuleInfo>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "FindRulesForVm", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "FindRulesForVm", targetNamespace = "urn:vim25", className = "vim25.FindRulesForVmRequestType")
    @ResponseWrapper(localName = "FindRulesForVmResponse", targetNamespace = "urn:vim25", className = "vim25.FindRulesForVmResponse")
    public List<ClusterRuleInfo> findRulesForVm(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "StampAllRulesWithUuid_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "StampAllRulesWithUuid_Task", targetNamespace = "urn:vim25", className = "vim25.StampAllRulesWithUuidRequestType")
    @ResponseWrapper(localName = "StampAllRulesWithUuid_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.StampAllRulesWithUuidTaskResponse")
    public ManagedObjectReference stampAllRulesWithUuidTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ClusterResourceUsageSummary
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "GetResourceUsage", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "GetResourceUsage", targetNamespace = "urn:vim25", className = "vim25.GetResourceUsageRequestType")
    @ResponseWrapper(localName = "GetResourceUsageResponse", targetNamespace = "urn:vim25", className = "vim25.GetResourceUsageResponse")
    public ClusterResourceUsageSummary getResourceUsage(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param modify
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ReconfigureComputeResource_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ReconfigureComputeResource_Task", targetNamespace = "urn:vim25", className = "vim25.ReconfigureComputeResourceRequestType")
    @ResponseWrapper(localName = "ReconfigureComputeResource_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ReconfigureComputeResourceTaskResponse")
    public ManagedObjectReference reconfigureComputeResourceTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        ComputeResourceConfigSpec spec,
        @WebParam(name = "modify", targetNamespace = "urn:vim25")
        boolean modify)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param fieldPolicy
     * @param name
     * @param moType
     * @param _this
     * @param fieldDefPolicy
     * @return
     *     returns vim25.CustomFieldDef
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidPrivilegeFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "AddCustomFieldDef", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "AddCustomFieldDef", targetNamespace = "urn:vim25", className = "vim25.AddCustomFieldDefRequestType")
    @ResponseWrapper(localName = "AddCustomFieldDefResponse", targetNamespace = "urn:vim25", className = "vim25.AddCustomFieldDefResponse")
    public CustomFieldDef addCustomFieldDef(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "moType", targetNamespace = "urn:vim25")
        String moType,
        @WebParam(name = "fieldDefPolicy", targetNamespace = "urn:vim25")
        PrivilegePolicyDef fieldDefPolicy,
        @WebParam(name = "fieldPolicy", targetNamespace = "urn:vim25")
        PrivilegePolicyDef fieldPolicy)
        throws DuplicateNameFaultMsg, InvalidPrivilegeFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param key
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RemoveCustomFieldDef", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RemoveCustomFieldDef", targetNamespace = "urn:vim25", className = "vim25.RemoveCustomFieldDefRequestType")
    @ResponseWrapper(localName = "RemoveCustomFieldDefResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveCustomFieldDefResponse")
    public void removeCustomFieldDef(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "key", targetNamespace = "urn:vim25")
        int key)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param _this
     * @param key
     * @throws RuntimeFaultFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "RenameCustomFieldDef", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RenameCustomFieldDef", targetNamespace = "urn:vim25", className = "vim25.RenameCustomFieldDefRequestType")
    @ResponseWrapper(localName = "RenameCustomFieldDefResponse", targetNamespace = "urn:vim25", className = "vim25.RenameCustomFieldDefResponse")
    public void renameCustomFieldDef(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "key", targetNamespace = "urn:vim25")
        int key,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name)
        throws DuplicateNameFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param value
     * @param entity
     * @param key
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "SetField", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "SetField", targetNamespace = "urn:vim25", className = "vim25.SetFieldRequestType")
    @ResponseWrapper(localName = "SetFieldResponse", targetNamespace = "urn:vim25", className = "vim25.SetFieldResponse")
    public void setField(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        ManagedObjectReference entity,
        @WebParam(name = "key", targetNamespace = "urn:vim25")
        int key,
        @WebParam(name = "value", targetNamespace = "urn:vim25")
        String value)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param _this
     * @return
     *     returns boolean
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "DoesCustomizationSpecExist", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "DoesCustomizationSpecExist", targetNamespace = "urn:vim25", className = "vim25.DoesCustomizationSpecExistRequestType")
    @ResponseWrapper(localName = "DoesCustomizationSpecExistResponse", targetNamespace = "urn:vim25", className = "vim25.DoesCustomizationSpecExistResponse")
    public boolean doesCustomizationSpecExist(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param _this
     * @return
     *     returns vim25.CustomizationSpecItem
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "GetCustomizationSpec", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "GetCustomizationSpec", targetNamespace = "urn:vim25", className = "vim25.GetCustomizationSpecRequestType")
    @ResponseWrapper(localName = "GetCustomizationSpecResponse", targetNamespace = "urn:vim25", className = "vim25.GetCustomizationSpecResponse")
    public CustomizationSpecItem getCustomizationSpec(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name)
        throws NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param item
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws CustomizationFaultFaultMsg
     * @throws AlreadyExistsFaultMsg
     */
    @WebMethod(operationName = "CreateCustomizationSpec", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "CreateCustomizationSpec", targetNamespace = "urn:vim25", className = "vim25.CreateCustomizationSpecRequestType")
    @ResponseWrapper(localName = "CreateCustomizationSpecResponse", targetNamespace = "urn:vim25", className = "vim25.CreateCustomizationSpecResponse")
    public void createCustomizationSpec(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "item", targetNamespace = "urn:vim25")
        CustomizationSpecItem item)
        throws AlreadyExistsFaultMsg, CustomizationFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param item
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws ConcurrentAccessFaultMsg
     * @throws CustomizationFaultFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "OverwriteCustomizationSpec", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "OverwriteCustomizationSpec", targetNamespace = "urn:vim25", className = "vim25.OverwriteCustomizationSpecRequestType")
    @ResponseWrapper(localName = "OverwriteCustomizationSpecResponse", targetNamespace = "urn:vim25", className = "vim25.OverwriteCustomizationSpecResponse")
    public void overwriteCustomizationSpec(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "item", targetNamespace = "urn:vim25")
        CustomizationSpecItem item)
        throws ConcurrentAccessFaultMsg, CustomizationFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "DeleteCustomizationSpec", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DeleteCustomizationSpec", targetNamespace = "urn:vim25", className = "vim25.DeleteCustomizationSpecRequestType")
    @ResponseWrapper(localName = "DeleteCustomizationSpecResponse", targetNamespace = "urn:vim25", className = "vim25.DeleteCustomizationSpecResponse")
    public void deleteCustomizationSpec(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name)
        throws NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param newName
     * @param name
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws AlreadyExistsFaultMsg
     */
    @WebMethod(operationName = "DuplicateCustomizationSpec", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DuplicateCustomizationSpec", targetNamespace = "urn:vim25", className = "vim25.DuplicateCustomizationSpecRequestType")
    @ResponseWrapper(localName = "DuplicateCustomizationSpecResponse", targetNamespace = "urn:vim25", className = "vim25.DuplicateCustomizationSpecResponse")
    public void duplicateCustomizationSpec(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "newName", targetNamespace = "urn:vim25")
        String newName)
        throws AlreadyExistsFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param newName
     * @param name
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws AlreadyExistsFaultMsg
     */
    @WebMethod(operationName = "RenameCustomizationSpec", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RenameCustomizationSpec", targetNamespace = "urn:vim25", className = "vim25.RenameCustomizationSpecRequestType")
    @ResponseWrapper(localName = "RenameCustomizationSpecResponse", targetNamespace = "urn:vim25", className = "vim25.RenameCustomizationSpecResponse")
    public void renameCustomizationSpec(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "newName", targetNamespace = "urn:vim25")
        String newName)
        throws AlreadyExistsFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param item
     * @param _this
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "CustomizationSpecItemToXml", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CustomizationSpecItemToXml", targetNamespace = "urn:vim25", className = "vim25.CustomizationSpecItemToXmlRequestType")
    @ResponseWrapper(localName = "CustomizationSpecItemToXmlResponse", targetNamespace = "urn:vim25", className = "vim25.CustomizationSpecItemToXmlResponse")
    public String customizationSpecItemToXml(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "item", targetNamespace = "urn:vim25")
        CustomizationSpecItem item)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param specItemXml
     * @param _this
     * @return
     *     returns vim25.CustomizationSpecItem
     * @throws RuntimeFaultFaultMsg
     * @throws CustomizationFaultFaultMsg
     */
    @WebMethod(operationName = "XmlToCustomizationSpecItem", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "XmlToCustomizationSpecItem", targetNamespace = "urn:vim25", className = "vim25.XmlToCustomizationSpecItemRequestType")
    @ResponseWrapper(localName = "XmlToCustomizationSpecItemResponse", targetNamespace = "urn:vim25", className = "vim25.XmlToCustomizationSpecItemResponse")
    public CustomizationSpecItem xmlToCustomizationSpecItem(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "specItemXml", targetNamespace = "urn:vim25")
        String specItemXml)
        throws CustomizationFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param guestOs
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws CustomizationFaultFaultMsg
     */
    @WebMethod(operationName = "CheckCustomizationResources", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "CheckCustomizationResources", targetNamespace = "urn:vim25", className = "vim25.CheckCustomizationResourcesRequestType")
    @ResponseWrapper(localName = "CheckCustomizationResourcesResponse", targetNamespace = "urn:vim25", className = "vim25.CheckCustomizationResourcesResponse")
    public void checkCustomizationResources(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "guestOs", targetNamespace = "urn:vim25")
        String guestOs)
        throws CustomizationFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param hostname
     * @param password
     * @param port
     * @param sslThumbprint
     * @param _this
     * @param username
     * @return
     *     returns vim25.HostConnectInfo
     * @throws RuntimeFaultFaultMsg
     * @throws HostConnectFaultFaultMsg
     * @throws InvalidLoginFaultMsg
     */
    @WebMethod(operationName = "QueryConnectionInfo", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryConnectionInfo", targetNamespace = "urn:vim25", className = "vim25.QueryConnectionInfoRequestType")
    @ResponseWrapper(localName = "QueryConnectionInfoResponse", targetNamespace = "urn:vim25", className = "vim25.QueryConnectionInfoResponse")
    public HostConnectInfo queryConnectionInfo(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "hostname", targetNamespace = "urn:vim25")
        String hostname,
        @WebParam(name = "port", targetNamespace = "urn:vim25")
        int port,
        @WebParam(name = "username", targetNamespace = "urn:vim25")
        String username,
        @WebParam(name = "password", targetNamespace = "urn:vim25")
        String password,
        @WebParam(name = "sslThumbprint", targetNamespace = "urn:vim25")
        String sslThumbprint)
        throws HostConnectFaultFaultMsg, InvalidLoginFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @return
     *     returns vim25.HostConnectInfo
     * @throws RuntimeFaultFaultMsg
     * @throws HostConnectFaultFaultMsg
     * @throws InvalidLoginFaultMsg
     */
    @WebMethod(operationName = "QueryConnectionInfoViaSpec", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryConnectionInfoViaSpec", targetNamespace = "urn:vim25", className = "vim25.QueryConnectionInfoViaSpecRequestType")
    @ResponseWrapper(localName = "QueryConnectionInfoViaSpecResponse", targetNamespace = "urn:vim25", className = "vim25.QueryConnectionInfoViaSpecResponse")
    public HostConnectInfo queryConnectionInfoViaSpec(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        HostConnectSpec spec)
        throws HostConnectFaultFaultMsg, InvalidLoginFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vm
     * @param _this
     * @param option
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "PowerOnMultiVM_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "PowerOnMultiVM_Task", targetNamespace = "urn:vim25", className = "vim25.PowerOnMultiVMRequestType")
    @ResponseWrapper(localName = "PowerOnMultiVM_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.PowerOnMultiVMTaskResponse")
    public ManagedObjectReference powerOnMultiVMTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> vm,
        @WebParam(name = "option", targetNamespace = "urn:vim25")
        List<OptionValue> option)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.util.List<vim25.VirtualMachineConfigOptionDescriptor>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "queryDatacenterConfigOptionDescriptor", targetNamespace = "urn:vim25", className = "vim25.QueryDatacenterConfigOptionDescriptorRequestType")
    @ResponseWrapper(localName = "queryDatacenterConfigOptionDescriptorResponse", targetNamespace = "urn:vim25", className = "vim25.QueryDatacenterConfigOptionDescriptorResponse")
    public List<VirtualMachineConfigOptionDescriptor> queryDatacenterConfigOptionDescriptor(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param modify
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ReconfigureDatacenter_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ReconfigureDatacenter_Task", targetNamespace = "urn:vim25", className = "vim25.ReconfigureDatacenterRequestType")
    @ResponseWrapper(localName = "ReconfigureDatacenter_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ReconfigureDatacenterTaskResponse")
    public ManagedObjectReference reconfigureDatacenterTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        DatacenterConfigSpec spec,
        @WebParam(name = "modify", targetNamespace = "urn:vim25")
        boolean modify)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "RefreshDatastore", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RefreshDatastore", targetNamespace = "urn:vim25", className = "vim25.RefreshDatastoreRequestType")
    @ResponseWrapper(localName = "RefreshDatastoreResponse", targetNamespace = "urn:vim25", className = "vim25.RefreshDatastoreResponse")
    public void refreshDatastore(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RefreshDatastoreStorageInfo", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RefreshDatastoreStorageInfo", targetNamespace = "urn:vim25", className = "vim25.RefreshDatastoreStorageInfoRequestType")
    @ResponseWrapper(localName = "RefreshDatastoreStorageInfoResponse", targetNamespace = "urn:vim25", className = "vim25.RefreshDatastoreStorageInfoResponse")
    public void refreshDatastoreStorageInfo(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param mountPathDatastoreMapping
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws ResourceInUseFaultMsg
     * @throws PlatformConfigFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "UpdateVirtualMachineFiles_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "UpdateVirtualMachineFiles_Task", targetNamespace = "urn:vim25", className = "vim25.UpdateVirtualMachineFilesRequestType")
    @ResponseWrapper(localName = "UpdateVirtualMachineFiles_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateVirtualMachineFilesTaskResponse")
    public ManagedObjectReference updateVirtualMachineFilesTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "mountPathDatastoreMapping", targetNamespace = "urn:vim25")
        List<DatastoreMountPathDatastorePair> mountPathDatastoreMapping)
        throws InvalidDatastoreFaultMsg, PlatformConfigFaultFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param newName
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "RenameDatastore", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RenameDatastore", targetNamespace = "urn:vim25", className = "vim25.RenameDatastoreRequestType")
    @ResponseWrapper(localName = "RenameDatastoreResponse", targetNamespace = "urn:vim25", className = "vim25.RenameDatastoreResponse")
    public void renameDatastore(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "newName", targetNamespace = "urn:vim25")
        String newName)
        throws DuplicateNameFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws ResourceInUseFaultMsg
     */
    @WebMethod(operationName = "DestroyDatastore", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DestroyDatastore", targetNamespace = "urn:vim25", className = "vim25.DestroyDatastoreRequestType")
    @ResponseWrapper(localName = "DestroyDatastoreResponse", targetNamespace = "urn:vim25", className = "vim25.DestroyDatastoreResponse")
    public void destroyDatastore(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.StoragePlacementResult
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "DatastoreEnterMaintenanceMode", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "DatastoreEnterMaintenanceMode", targetNamespace = "urn:vim25", className = "vim25.DatastoreEnterMaintenanceModeRequestType")
    @ResponseWrapper(localName = "DatastoreEnterMaintenanceModeResponse", targetNamespace = "urn:vim25", className = "vim25.DatastoreEnterMaintenanceModeResponse")
    public StoragePlacementResult datastoreEnterMaintenanceMode(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "DatastoreExitMaintenanceMode_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "DatastoreExitMaintenanceMode_Task", targetNamespace = "urn:vim25", className = "vim25.DatastoreExitMaintenanceModeRequestType")
    @ResponseWrapper(localName = "DatastoreExitMaintenanceMode_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.DatastoreExitMaintenanceModeTaskResponse")
    public ManagedObjectReference datastoreExitMaintenanceModeTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param datastore
     * @param displayName
     * @param _this
     * @param policy
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     * @throws FileAlreadyExistsFaultMsg
     * @throws CannotCreateFileFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "CreateDirectory", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateDirectory", targetNamespace = "urn:vim25", className = "vim25.CreateDirectoryRequestType")
    @ResponseWrapper(localName = "CreateDirectoryResponse", targetNamespace = "urn:vim25", className = "vim25.CreateDirectoryResponse")
    public String createDirectory(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "datastore", targetNamespace = "urn:vim25")
        ManagedObjectReference datastore,
        @WebParam(name = "displayName", targetNamespace = "urn:vim25")
        String displayName,
        @WebParam(name = "policy", targetNamespace = "urn:vim25")
        String policy)
        throws CannotCreateFileFaultMsg, FileAlreadyExistsFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param datastorePath
     * @param datacenter
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws FileNotFoundFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastorePathFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "DeleteDirectory", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DeleteDirectory", targetNamespace = "urn:vim25", className = "vim25.DeleteDirectoryRequestType")
    @ResponseWrapper(localName = "DeleteDirectoryResponse", targetNamespace = "urn:vim25", className = "vim25.DeleteDirectoryResponse")
    public void deleteDirectory(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "datacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference datacenter,
        @WebParam(name = "datastorePath", targetNamespace = "urn:vim25")
        String datastorePath)
        throws FileFaultFaultMsg, FileNotFoundFaultMsg, InvalidDatastoreFaultMsg, InvalidDatastorePathFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @return
     *     returns java.util.List<vim25.DiagnosticManagerLogDescriptor>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryDescriptions", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryDescriptions", targetNamespace = "urn:vim25", className = "vim25.QueryDescriptionsRequestType")
    @ResponseWrapper(localName = "QueryDescriptionsResponse", targetNamespace = "urn:vim25", className = "vim25.QueryDescriptionsResponse")
    public List<DiagnosticManagerLogDescriptor> queryDescriptions(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param start
     * @param _this
     * @param lines
     * @param key
     * @return
     *     returns vim25.DiagnosticManagerLogHeader
     * @throws RuntimeFaultFaultMsg
     * @throws CannotAccessFileFaultMsg
     */
    @WebMethod(operationName = "BrowseDiagnosticLog", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "BrowseDiagnosticLog", targetNamespace = "urn:vim25", className = "vim25.BrowseDiagnosticLogRequestType")
    @ResponseWrapper(localName = "BrowseDiagnosticLogResponse", targetNamespace = "urn:vim25", className = "vim25.BrowseDiagnosticLogResponse")
    public DiagnosticManagerLogHeader browseDiagnosticLog(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host,
        @WebParam(name = "key", targetNamespace = "urn:vim25")
        String key,
        @WebParam(name = "start", targetNamespace = "urn:vim25")
        Integer start,
        @WebParam(name = "lines", targetNamespace = "urn:vim25")
        Integer lines)
        throws CannotAccessFileFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param includeDefault
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws LogBundlingFailedFaultMsg
     */
    @WebMethod(operationName = "GenerateLogBundles_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "GenerateLogBundles_Task", targetNamespace = "urn:vim25", className = "vim25.GenerateLogBundlesRequestType")
    @ResponseWrapper(localName = "GenerateLogBundles_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.GenerateLogBundlesTaskResponse")
    public ManagedObjectReference generateLogBundlesTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "includeDefault", targetNamespace = "urn:vim25")
        boolean includeDefault,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> host)
        throws LogBundlingFailedFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param criteria
     * @param _this
     * @return
     *     returns java.util.List<java.lang.String>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "FetchDVPortKeys", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "FetchDVPortKeys", targetNamespace = "urn:vim25", className = "vim25.FetchDVPortKeysRequestType")
    @ResponseWrapper(localName = "FetchDVPortKeysResponse", targetNamespace = "urn:vim25", className = "vim25.FetchDVPortKeysResponse")
    public List<String> fetchDVPortKeys(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "criteria", targetNamespace = "urn:vim25")
        DistributedVirtualSwitchPortCriteria criteria)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param criteria
     * @param _this
     * @return
     *     returns java.util.List<vim25.DistributedVirtualPort>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "FetchDVPorts", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "FetchDVPorts", targetNamespace = "urn:vim25", className = "vim25.FetchDVPortsRequestType")
    @ResponseWrapper(localName = "FetchDVPortsResponse", targetNamespace = "urn:vim25", className = "vim25.FetchDVPortsResponse")
    public List<DistributedVirtualPort> fetchDVPorts(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "criteria", targetNamespace = "urn:vim25")
        DistributedVirtualSwitchPortCriteria criteria)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.util.List<java.lang.Integer>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryUsedVlanIdInDvs", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryUsedVlanIdInDvs", targetNamespace = "urn:vim25", className = "vim25.QueryUsedVlanIdInDvsRequestType")
    @ResponseWrapper(localName = "QueryUsedVlanIdInDvsResponse", targetNamespace = "urn:vim25", className = "vim25.QueryUsedVlanIdInDvsResponse")
    public List<Integer> queryUsedVlanIdInDvs(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws DvsFaultFaultMsg
     * @throws RuntimeFaultFaultMsg
     * @throws ConcurrentAccessFaultMsg
     * @throws ResourceNotAvailableFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws ResourceInUseFaultMsg
     * @throws NotFoundFaultMsg
     * @throws AlreadyExistsFaultMsg
     * @throws LimitExceededFaultMsg
     * @throws DvsNotAuthorizedFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "ReconfigureDvs_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ReconfigureDvs_Task", targetNamespace = "urn:vim25", className = "vim25.ReconfigureDvsRequestType")
    @ResponseWrapper(localName = "ReconfigureDvs_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ReconfigureDvsTaskResponse")
    public ManagedObjectReference reconfigureDvsTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        DVSConfigSpec spec)
        throws AlreadyExistsFaultMsg, ConcurrentAccessFaultMsg, DuplicateNameFaultMsg, DvsFaultFaultMsg, DvsNotAuthorizedFaultMsg, InvalidNameFaultMsg, InvalidStateFaultMsg, LimitExceededFaultMsg, NotFoundFaultMsg, ResourceInUseFaultMsg, ResourceNotAvailableFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param operation
     * @param productSpec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws DvsFaultFaultMsg
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "PerformDvsProductSpecOperation_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "PerformDvsProductSpecOperation_Task", targetNamespace = "urn:vim25", className = "vim25.PerformDvsProductSpecOperationRequestType")
    @ResponseWrapper(localName = "PerformDvsProductSpecOperation_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.PerformDvsProductSpecOperationTaskResponse")
    public ManagedObjectReference performDvsProductSpecOperationTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "operation", targetNamespace = "urn:vim25")
        String operation,
        @WebParam(name = "productSpec", targetNamespace = "urn:vim25")
        DistributedVirtualSwitchProductSpec productSpec)
        throws DvsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param dvs
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws DvsFaultFaultMsg
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws ResourceInUseFaultMsg
     * @throws InvalidHostStateFaultMsg
     */
    @WebMethod(operationName = "MergeDvs_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "MergeDvs_Task", targetNamespace = "urn:vim25", className = "vim25.MergeDvsRequestType")
    @ResponseWrapper(localName = "MergeDvs_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.MergeDvsTaskResponse")
    public ManagedObjectReference mergeDvsTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "dvs", targetNamespace = "urn:vim25")
        ManagedObjectReference dvs)
        throws DvsFaultFaultMsg, InvalidHostStateFaultMsg, NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws DvsFaultFaultMsg
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "AddDVPortgroup_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "AddDVPortgroup_Task", targetNamespace = "urn:vim25", className = "vim25.AddDVPortgroupRequestType")
    @ResponseWrapper(localName = "AddDVPortgroup_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.AddDVPortgroupTaskResponse")
    public ManagedObjectReference addDVPortgroupTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        List<DVPortgroupConfigSpec> spec)
        throws DuplicateNameFaultMsg, DvsFaultFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param destinationPortgroupKey
     * @param _this
     * @param portKey
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws DvsFaultFaultMsg
     * @throws RuntimeFaultFaultMsg
     * @throws ConcurrentAccessFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "MoveDVPort_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "MoveDVPort_Task", targetNamespace = "urn:vim25", className = "vim25.MoveDVPortRequestType")
    @ResponseWrapper(localName = "MoveDVPort_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.MoveDVPortTaskResponse")
    public ManagedObjectReference moveDVPortTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "portKey", targetNamespace = "urn:vim25")
        List<String> portKey,
        @WebParam(name = "destinationPortgroupKey", targetNamespace = "urn:vim25")
        String destinationPortgroupKey)
        throws ConcurrentAccessFaultMsg, DvsFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param capability
     * @param _this
     * @throws DvsFaultFaultMsg
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateDvsCapability", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateDvsCapability", targetNamespace = "urn:vim25", className = "vim25.UpdateDvsCapabilityRequestType")
    @ResponseWrapper(localName = "UpdateDvsCapabilityResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateDvsCapabilityResponse")
    public void updateDvsCapability(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "capability", targetNamespace = "urn:vim25")
        DVSCapability capability)
        throws DvsFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param port
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws DvsFaultFaultMsg
     * @throws RuntimeFaultFaultMsg
     * @throws ConcurrentAccessFaultMsg
     * @throws NotFoundFaultMsg
     * @throws ResourceInUseFaultMsg
     */
    @WebMethod(operationName = "ReconfigureDVPort_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ReconfigureDVPort_Task", targetNamespace = "urn:vim25", className = "vim25.ReconfigureDVPortRequestType")
    @ResponseWrapper(localName = "ReconfigureDVPort_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ReconfigureDVPortTaskResponse")
    public ManagedObjectReference reconfigureDVPortTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "port", targetNamespace = "urn:vim25")
        List<DVPortConfigSpec> port)
        throws ConcurrentAccessFaultMsg, DvsFaultFaultMsg, NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param portKeys
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws DvsFaultFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "RefreshDVPortState", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RefreshDVPortState", targetNamespace = "urn:vim25", className = "vim25.RefreshDVPortStateRequestType")
    @ResponseWrapper(localName = "RefreshDVPortStateResponse", targetNamespace = "urn:vim25", className = "vim25.RefreshDVPortStateResponse")
    public void refreshDVPortState(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "portKeys", targetNamespace = "urn:vim25")
        List<String> portKeys)
        throws DvsFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param hosts
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws DvsFaultFaultMsg
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "RectifyDvsHost_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RectifyDvsHost_Task", targetNamespace = "urn:vim25", className = "vim25.RectifyDvsHostRequestType")
    @ResponseWrapper(localName = "RectifyDvsHost_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.RectifyDvsHostTaskResponse")
    public ManagedObjectReference rectifyDvsHostTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "hosts", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> hosts)
        throws DvsFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param configSpec
     * @param _this
     * @throws DvsFaultFaultMsg
     * @throws RuntimeFaultFaultMsg
     * @throws ConcurrentAccessFaultMsg
     * @throws NotFoundFaultMsg
     * @throws InvalidNameFaultMsg
     */
    @WebMethod(operationName = "UpdateNetworkResourcePool", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateNetworkResourcePool", targetNamespace = "urn:vim25", className = "vim25.UpdateNetworkResourcePoolRequestType")
    @ResponseWrapper(localName = "UpdateNetworkResourcePoolResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateNetworkResourcePoolResponse")
    public void updateNetworkResourcePool(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "configSpec", targetNamespace = "urn:vim25")
        List<DVSNetworkResourcePoolConfigSpec> configSpec)
        throws ConcurrentAccessFaultMsg, DvsFaultFaultMsg, InvalidNameFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param configSpec
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws DvsFaultFaultMsg
     * @throws InvalidNameFaultMsg
     */
    @WebMethod(operationName = "AddNetworkResourcePool", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "AddNetworkResourcePool", targetNamespace = "urn:vim25", className = "vim25.AddNetworkResourcePoolRequestType")
    @ResponseWrapper(localName = "AddNetworkResourcePoolResponse", targetNamespace = "urn:vim25", className = "vim25.AddNetworkResourcePoolResponse")
    public void addNetworkResourcePool(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "configSpec", targetNamespace = "urn:vim25")
        List<DVSNetworkResourcePoolConfigSpec> configSpec)
        throws DvsFaultFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param key
     * @throws RuntimeFaultFaultMsg
     * @throws DvsFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws ResourceInUseFaultMsg
     * @throws InvalidNameFaultMsg
     */
    @WebMethod(operationName = "RemoveNetworkResourcePool", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RemoveNetworkResourcePool", targetNamespace = "urn:vim25", className = "vim25.RemoveNetworkResourcePoolRequestType")
    @ResponseWrapper(localName = "RemoveNetworkResourcePoolResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveNetworkResourcePoolResponse")
    public void removeNetworkResourcePool(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "key", targetNamespace = "urn:vim25")
        List<String> key)
        throws DvsFaultFaultMsg, InvalidNameFaultMsg, NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param configSpec
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws DvsFaultFaultMsg
     * @throws ConcurrentAccessFaultMsg
     * @throws ConflictingConfigurationFaultMsg
     * @throws NotFoundFaultMsg
     * @throws ResourceInUseFaultMsg
     * @throws InvalidNameFaultMsg
     */
    @WebMethod(operationName = "DvsReconfigureVmVnicNetworkResourcePool_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "DvsReconfigureVmVnicNetworkResourcePool_Task", targetNamespace = "urn:vim25", className = "vim25.DvsReconfigureVmVnicNetworkResourcePoolRequestType")
    @ResponseWrapper(localName = "DvsReconfigureVmVnicNetworkResourcePool_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.DvsReconfigureVmVnicNetworkResourcePoolTaskResponse")
    public ManagedObjectReference dvsReconfigureVmVnicNetworkResourcePoolTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "configSpec", targetNamespace = "urn:vim25")
        List<DvsVmVnicResourcePoolConfigSpec> configSpec)
        throws ConcurrentAccessFaultMsg, ConflictingConfigurationFaultMsg, DvsFaultFaultMsg, InvalidNameFaultMsg, NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param enable
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws DvsFaultFaultMsg
     */
    @WebMethod(operationName = "EnableNetworkResourceManagement", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "EnableNetworkResourceManagement", targetNamespace = "urn:vim25", className = "vim25.EnableNetworkResourceManagementRequestType")
    @ResponseWrapper(localName = "EnableNetworkResourceManagementResponse", targetNamespace = "urn:vim25", className = "vim25.EnableNetworkResourceManagementResponse")
    public void enableNetworkResourceManagement(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "enable", targetNamespace = "urn:vim25")
        boolean enable)
        throws DvsFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param entityBackup
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws DvsFaultFaultMsg
     * @throws RollbackFailureFaultMsg
     */
    @WebMethod(operationName = "DVSRollback_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "DVSRollback_Task", targetNamespace = "urn:vim25", className = "vim25.DVSRollbackRequestType")
    @ResponseWrapper(localName = "DVSRollback_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.DVSRollbackTaskResponse")
    public ManagedObjectReference dvsRollbackTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entityBackup", targetNamespace = "urn:vim25")
        EntityBackupConfig entityBackup)
        throws DvsFaultFaultMsg, RollbackFailureFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws DvsFaultFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "CreateDVPortgroup_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateDVPortgroup_Task", targetNamespace = "urn:vim25", className = "vim25.CreateDVPortgroupRequestType")
    @ResponseWrapper(localName = "CreateDVPortgroup_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CreateDVPortgroupTaskResponse")
    public ManagedObjectReference createDVPortgroupTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        DVPortgroupConfigSpec spec)
        throws DuplicateNameFaultMsg, DvsFaultFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param healthCheckConfig
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws DvsFaultFaultMsg
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateDVSHealthCheckConfig_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "UpdateDVSHealthCheckConfig_Task", targetNamespace = "urn:vim25", className = "vim25.UpdateDVSHealthCheckConfigRequestType")
    @ResponseWrapper(localName = "UpdateDVSHealthCheckConfig_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateDVSHealthCheckConfigTaskResponse")
    public ManagedObjectReference updateDVSHealthCheckConfigTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "healthCheckConfig", targetNamespace = "urn:vim25")
        List<DVSHealthCheckConfig> healthCheckConfig)
        throws DvsFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param portgroupKey
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "LookupDvPortGroup", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "LookupDvPortGroup", targetNamespace = "urn:vim25", className = "vim25.LookupDvPortGroupRequestType")
    @ResponseWrapper(localName = "LookupDvPortGroupResponse", targetNamespace = "urn:vim25", className = "vim25.LookupDvPortGroupResponse")
    public ManagedObjectReference lookupDvPortGroup(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "portgroupKey", targetNamespace = "urn:vim25")
        String portgroupKey)
        throws NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.util.List<vim25.VirtualMachineConfigOptionDescriptor>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryConfigOptionDescriptor", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryConfigOptionDescriptor", targetNamespace = "urn:vim25", className = "vim25.QueryConfigOptionDescriptorRequestType")
    @ResponseWrapper(localName = "QueryConfigOptionDescriptorResponse", targetNamespace = "urn:vim25", className = "vim25.QueryConfigOptionDescriptorResponse")
    public List<VirtualMachineConfigOptionDescriptor> queryConfigOptionDescriptor(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @param key
     * @return
     *     returns vim25.VirtualMachineConfigOption
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryConfigOption", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryConfigOption", targetNamespace = "urn:vim25", className = "vim25.QueryConfigOptionRequestType")
    @ResponseWrapper(localName = "QueryConfigOptionResponse", targetNamespace = "urn:vim25", className = "vim25.QueryConfigOptionResponse")
    public VirtualMachineConfigOption queryConfigOption(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "key", targetNamespace = "urn:vim25")
        String key,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @return
     *     returns vim25.VirtualMachineConfigOption
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryConfigOptionEx", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryConfigOptionEx", targetNamespace = "urn:vim25", className = "vim25.QueryConfigOptionExRequestType")
    @ResponseWrapper(localName = "QueryConfigOptionExResponse", targetNamespace = "urn:vim25", className = "vim25.QueryConfigOptionExResponse")
    public VirtualMachineConfigOption queryConfigOptionEx(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        EnvironmentBrowserConfigOptionQuerySpec spec)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @return
     *     returns vim25.ConfigTarget
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryConfigTarget", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryConfigTarget", targetNamespace = "urn:vim25", className = "vim25.QueryConfigTargetRequestType")
    @ResponseWrapper(localName = "QueryConfigTargetResponse", targetNamespace = "urn:vim25", className = "vim25.QueryConfigTargetResponse")
    public ConfigTarget queryConfigTarget(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @return
     *     returns vim25.HostCapability
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryTargetCapabilities", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryTargetCapabilities", targetNamespace = "urn:vim25", className = "vim25.QueryTargetCapabilitiesRequestType")
    @ResponseWrapper(localName = "QueryTargetCapabilitiesResponse", targetNamespace = "urn:vim25", className = "vim25.QueryTargetCapabilitiesResponse")
    public HostCapability queryTargetCapabilities(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param value
     * @param key
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(action = "urn:vim25/6.0")
    @RequestWrapper(localName = "setCustomValue", targetNamespace = "urn:vim25", className = "vim25.SetCustomValueRequestType")
    @ResponseWrapper(localName = "setCustomValueResponse", targetNamespace = "urn:vim25", className = "vim25.SetCustomValueResponse")
    public void setCustomValue(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "key", targetNamespace = "urn:vim25")
        String key,
        @WebParam(name = "value", targetNamespace = "urn:vim25")
        String value)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param extensionKey
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "UnregisterExtension", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UnregisterExtension", targetNamespace = "urn:vim25", className = "vim25.UnregisterExtensionRequestType")
    @ResponseWrapper(localName = "UnregisterExtensionResponse", targetNamespace = "urn:vim25", className = "vim25.UnregisterExtensionResponse")
    public void unregisterExtension(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "extensionKey", targetNamespace = "urn:vim25")
        String extensionKey)
        throws NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param extensionKey
     * @param _this
     * @return
     *     returns vim25.Extension
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "FindExtension", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "FindExtension", targetNamespace = "urn:vim25", className = "vim25.FindExtensionRequestType")
    @ResponseWrapper(localName = "FindExtensionResponse", targetNamespace = "urn:vim25", className = "vim25.FindExtensionResponse")
    public Extension findExtension(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "extensionKey", targetNamespace = "urn:vim25")
        String extensionKey)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param extension
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RegisterExtension", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RegisterExtension", targetNamespace = "urn:vim25", className = "vim25.RegisterExtensionRequestType")
    @ResponseWrapper(localName = "RegisterExtensionResponse", targetNamespace = "urn:vim25", className = "vim25.RegisterExtensionResponse")
    public void registerExtension(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "extension", targetNamespace = "urn:vim25")
        Extension extension)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param extension
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "UpdateExtension", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateExtension", targetNamespace = "urn:vim25", className = "vim25.UpdateExtensionRequestType")
    @ResponseWrapper(localName = "UpdateExtensionResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateExtensionResponse")
    public void updateExtension(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "extension", targetNamespace = "urn:vim25")
        Extension extension)
        throws NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "GetPublicKey", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "GetPublicKey", targetNamespace = "urn:vim25", className = "vim25.GetPublicKeyRequestType")
    @ResponseWrapper(localName = "GetPublicKeyResponse", targetNamespace = "urn:vim25", className = "vim25.GetPublicKeyResponse")
    public String getPublicKey(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param extensionKey
     * @param publicKey
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "SetPublicKey", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "SetPublicKey", targetNamespace = "urn:vim25", className = "vim25.SetPublicKeyRequestType")
    @ResponseWrapper(localName = "SetPublicKeyResponse", targetNamespace = "urn:vim25", className = "vim25.SetPublicKeyResponse")
    public void setPublicKey(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "extensionKey", targetNamespace = "urn:vim25")
        String extensionKey,
        @WebParam(name = "publicKey", targetNamespace = "urn:vim25")
        String publicKey)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param certificatePem
     * @param extensionKey
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws NoClientCertificateFaultMsg
     */
    @WebMethod(operationName = "SetExtensionCertificate", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "SetExtensionCertificate", targetNamespace = "urn:vim25", className = "vim25.SetExtensionCertificateRequestType")
    @ResponseWrapper(localName = "SetExtensionCertificateResponse", targetNamespace = "urn:vim25", className = "vim25.SetExtensionCertificateResponse")
    public void setExtensionCertificate(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "extensionKey", targetNamespace = "urn:vim25")
        String extensionKey,
        @WebParam(name = "certificatePem", targetNamespace = "urn:vim25")
        String certificatePem)
        throws NoClientCertificateFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param extensionKey
     * @param _this
     * @return
     *     returns java.util.List<vim25.ManagedObjectReference>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryManagedBy", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryManagedBy", targetNamespace = "urn:vim25", className = "vim25.QueryManagedByRequestType")
    @ResponseWrapper(localName = "QueryManagedByResponse", targetNamespace = "urn:vim25", className = "vim25.QueryManagedByResponse")
    public List<ManagedObjectReference> queryManagedBy(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "extensionKey", targetNamespace = "urn:vim25")
        String extensionKey)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param extensionKeys
     * @param _this
     * @return
     *     returns java.util.List<vim25.ExtensionManagerIpAllocationUsage>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryExtensionIpAllocationUsage", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryExtensionIpAllocationUsage", targetNamespace = "urn:vim25", className = "vim25.QueryExtensionIpAllocationUsageRequestType")
    @ResponseWrapper(localName = "QueryExtensionIpAllocationUsageResponse", targetNamespace = "urn:vim25", className = "vim25.QueryExtensionIpAllocationUsageResponse")
    public List<ExtensionManagerIpAllocationUsage> queryExtensionIpAllocationUsage(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "extensionKeys", targetNamespace = "urn:vim25")
        List<String> extensionKeys)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param destinationName
     * @param destinationDatacenter
     * @param force
     * @param sourceName
     * @param _this
     * @param sourceDatacenter
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "MoveDatastoreFile_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "MoveDatastoreFile_Task", targetNamespace = "urn:vim25", className = "vim25.MoveDatastoreFileRequestType")
    @ResponseWrapper(localName = "MoveDatastoreFile_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.MoveDatastoreFileTaskResponse")
    public ManagedObjectReference moveDatastoreFileTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "sourceName", targetNamespace = "urn:vim25")
        String sourceName,
        @WebParam(name = "sourceDatacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference sourceDatacenter,
        @WebParam(name = "destinationName", targetNamespace = "urn:vim25")
        String destinationName,
        @WebParam(name = "destinationDatacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference destinationDatacenter,
        @WebParam(name = "force", targetNamespace = "urn:vim25")
        Boolean force)
        throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param destinationName
     * @param destinationDatacenter
     * @param force
     * @param sourceName
     * @param _this
     * @param sourceDatacenter
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "CopyDatastoreFile_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CopyDatastoreFile_Task", targetNamespace = "urn:vim25", className = "vim25.CopyDatastoreFileRequestType")
    @ResponseWrapper(localName = "CopyDatastoreFile_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CopyDatastoreFileTaskResponse")
    public ManagedObjectReference copyDatastoreFileTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "sourceName", targetNamespace = "urn:vim25")
        String sourceName,
        @WebParam(name = "sourceDatacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference sourceDatacenter,
        @WebParam(name = "destinationName", targetNamespace = "urn:vim25")
        String destinationName,
        @WebParam(name = "destinationDatacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference destinationDatacenter,
        @WebParam(name = "force", targetNamespace = "urn:vim25")
        Boolean force)
        throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param datacenter
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "DeleteDatastoreFile_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "DeleteDatastoreFile_Task", targetNamespace = "urn:vim25", className = "vim25.DeleteDatastoreFileRequestType")
    @ResponseWrapper(localName = "DeleteDatastoreFile_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.DeleteDatastoreFileTaskResponse")
    public ManagedObjectReference deleteDatastoreFileTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "datacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference datacenter)
        throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param createParentDirectories
     * @param name
     * @param datacenter
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "MakeDirectory", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "MakeDirectory", targetNamespace = "urn:vim25", className = "vim25.MakeDirectoryRequestType")
    @ResponseWrapper(localName = "MakeDirectoryResponse", targetNamespace = "urn:vim25", className = "vim25.MakeDirectoryResponse")
    public void makeDirectory(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "datacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference datacenter,
        @WebParam(name = "createParentDirectories", targetNamespace = "urn:vim25")
        Boolean createParentDirectories)
        throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param owner
     * @param name
     * @param datacenter
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws UserNotFoundFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "ChangeOwner", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ChangeOwner", targetNamespace = "urn:vim25", className = "vim25.ChangeOwnerRequestType")
    @ResponseWrapper(localName = "ChangeOwnerResponse", targetNamespace = "urn:vim25", className = "vim25.ChangeOwnerResponse")
    public void changeOwner(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "datacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference datacenter,
        @WebParam(name = "owner", targetNamespace = "urn:vim25")
        String owner)
        throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg, UserNotFoundFaultMsg
    ;

    /**
     * 
     * @param name
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "CreateFolder", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateFolder", targetNamespace = "urn:vim25", className = "vim25.CreateFolderRequestType")
    @ResponseWrapper(localName = "CreateFolderResponse", targetNamespace = "urn:vim25", className = "vim25.CreateFolderResponse")
    public ManagedObjectReference createFolder(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name)
        throws DuplicateNameFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param list
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidFolderFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "MoveIntoFolder_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "MoveIntoFolder_Task", targetNamespace = "urn:vim25", className = "vim25.MoveIntoFolderRequestType")
    @ResponseWrapper(localName = "MoveIntoFolder_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.MoveIntoFolderTaskResponse")
    public ManagedObjectReference moveIntoFolderTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "list", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> list)
        throws DuplicateNameFaultMsg, InvalidFolderFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param pool
     * @param host
     * @param _this
     * @param config
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws AlreadyExistsFaultMsg
     * @throws OutOfBoundsFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "CreateVM_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateVM_Task", targetNamespace = "urn:vim25", className = "vim25.CreateVMRequestType")
    @ResponseWrapper(localName = "CreateVM_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CreateVMTaskResponse")
    public ManagedObjectReference createVMTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "config", targetNamespace = "urn:vim25")
        VirtualMachineConfigSpec config,
        @WebParam(name = "pool", targetNamespace = "urn:vim25")
        ManagedObjectReference pool,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws AlreadyExistsFaultMsg, DuplicateNameFaultMsg, FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidDatastoreFaultMsg, InvalidNameFaultMsg, InvalidStateFaultMsg, OutOfBoundsFaultMsg, RuntimeFaultFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param path
     * @param name
     * @param pool
     * @param host
     * @param asTemplate
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NotFoundFaultMsg
     * @throws OutOfBoundsFaultMsg
     * @throws AlreadyExistsFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "RegisterVM_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RegisterVM_Task", targetNamespace = "urn:vim25", className = "vim25.RegisterVMRequestType")
    @ResponseWrapper(localName = "RegisterVM_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.RegisterVMTaskResponse")
    public ManagedObjectReference registerVMTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "path", targetNamespace = "urn:vim25")
        String path,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "asTemplate", targetNamespace = "urn:vim25")
        boolean asTemplate,
        @WebParam(name = "pool", targetNamespace = "urn:vim25")
        ManagedObjectReference pool,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws AlreadyExistsFaultMsg, DuplicateNameFaultMsg, FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidDatastoreFaultMsg, InvalidNameFaultMsg, InvalidStateFaultMsg, NotFoundFaultMsg, OutOfBoundsFaultMsg, RuntimeFaultFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "CreateCluster", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateCluster", targetNamespace = "urn:vim25", className = "vim25.CreateClusterRequestType")
    @ResponseWrapper(localName = "CreateClusterResponse", targetNamespace = "urn:vim25", className = "vim25.CreateClusterResponse")
    public ManagedObjectReference createCluster(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        ClusterConfigSpec spec)
        throws DuplicateNameFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "CreateClusterEx", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateClusterEx", targetNamespace = "urn:vim25", className = "vim25.CreateClusterExRequestType")
    @ResponseWrapper(localName = "CreateClusterExResponse", targetNamespace = "urn:vim25", className = "vim25.CreateClusterExResponse")
    public ManagedObjectReference createClusterEx(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        ClusterConfigSpecEx spec)
        throws DuplicateNameFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param license
     * @param compResSpec
     * @param addConnected
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws HostConnectFaultFaultMsg
     * @throws InvalidLoginFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "AddStandaloneHost_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "AddStandaloneHost_Task", targetNamespace = "urn:vim25", className = "vim25.AddStandaloneHostRequestType")
    @ResponseWrapper(localName = "AddStandaloneHost_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.AddStandaloneHostTaskResponse")
    public ManagedObjectReference addStandaloneHostTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        HostConnectSpec spec,
        @WebParam(name = "compResSpec", targetNamespace = "urn:vim25")
        ComputeResourceConfigSpec compResSpec,
        @WebParam(name = "addConnected", targetNamespace = "urn:vim25")
        boolean addConnected,
        @WebParam(name = "license", targetNamespace = "urn:vim25")
        String license)
        throws DuplicateNameFaultMsg, HostConnectFaultFaultMsg, InvalidLoginFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "CreateDatacenter", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateDatacenter", targetNamespace = "urn:vim25", className = "vim25.CreateDatacenterRequestType")
    @ResponseWrapper(localName = "CreateDatacenterResponse", targetNamespace = "urn:vim25", className = "vim25.CreateDatacenterResponse")
    public ManagedObjectReference createDatacenter(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name)
        throws DuplicateNameFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws ConcurrentAccessFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "UnregisterAndDestroy_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "UnregisterAndDestroy_Task", targetNamespace = "urn:vim25", className = "vim25.UnregisterAndDestroyRequestType")
    @ResponseWrapper(localName = "UnregisterAndDestroy_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.UnregisterAndDestroyTaskResponse")
    public ManagedObjectReference unregisterAndDestroyTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws ConcurrentAccessFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws DvsFaultFaultMsg
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws DvsNotAuthorizedFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "CreateDVS_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateDVS_Task", targetNamespace = "urn:vim25", className = "vim25.CreateDVSRequestType")
    @ResponseWrapper(localName = "CreateDVS_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CreateDVSTaskResponse")
    public ManagedObjectReference createDVSTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        DVSCreateSpec spec)
        throws DuplicateNameFaultMsg, DvsFaultFaultMsg, DvsNotAuthorizedFaultMsg, InvalidNameFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "CreateStoragePod", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateStoragePod", targetNamespace = "urn:vim25", className = "vim25.CreateStoragePodRequestType")
    @ResponseWrapper(localName = "CreateStoragePodResponse", targetNamespace = "urn:vim25", className = "vim25.CreateStoragePodResponse")
    public ManagedObjectReference createStoragePod(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name)
        throws DuplicateNameFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param maxCount
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "SetCollectorPageSize", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "SetCollectorPageSize", targetNamespace = "urn:vim25", className = "vim25.SetCollectorPageSizeRequestType")
    @ResponseWrapper(localName = "SetCollectorPageSizeResponse", targetNamespace = "urn:vim25", className = "vim25.SetCollectorPageSizeResponse")
    public void setCollectorPageSize(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "maxCount", targetNamespace = "urn:vim25")
        int maxCount)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RewindCollector", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RewindCollector", targetNamespace = "urn:vim25", className = "vim25.RewindCollectorRequestType")
    @ResponseWrapper(localName = "RewindCollectorResponse", targetNamespace = "urn:vim25", className = "vim25.RewindCollectorResponse")
    public void rewindCollector(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ResetCollector", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ResetCollector", targetNamespace = "urn:vim25", className = "vim25.ResetCollectorRequestType")
    @ResponseWrapper(localName = "ResetCollectorResponse", targetNamespace = "urn:vim25", className = "vim25.ResetCollectorResponse")
    public void resetCollector(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "DestroyCollector", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DestroyCollector", targetNamespace = "urn:vim25", className = "vim25.DestroyCollectorRequestType")
    @ResponseWrapper(localName = "DestroyCollectorResponse", targetNamespace = "urn:vim25", className = "vim25.DestroyCollectorResponse")
    public void destroyCollector(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.HostTpmAttestationReport
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryTpmAttestationReport", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryTpmAttestationReport", targetNamespace = "urn:vim25", className = "vim25.QueryTpmAttestationReportRequestType")
    @ResponseWrapper(localName = "QueryTpmAttestationReportResponse", targetNamespace = "urn:vim25", className = "vim25.QueryTpmAttestationReportResponse")
    public HostTpmAttestationReport queryTpmAttestationReport(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.HostConnectInfo
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryHostConnectionInfo", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryHostConnectionInfo", targetNamespace = "urn:vim25", className = "vim25.QueryHostConnectionInfoRequestType")
    @ResponseWrapper(localName = "QueryHostConnectionInfoResponse", targetNamespace = "urn:vim25", className = "vim25.QueryHostConnectionInfoResponse")
    public HostConnectInfo queryHostConnectionInfo(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param resourceInfo
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateSystemResources", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateSystemResources", targetNamespace = "urn:vim25", className = "vim25.UpdateSystemResourcesRequestType")
    @ResponseWrapper(localName = "UpdateSystemResourcesResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateSystemResourcesResponse")
    public void updateSystemResources(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "resourceInfo", targetNamespace = "urn:vim25")
        HostSystemResourceInfo resourceInfo)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param sysSwapConfig
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateSystemSwapConfiguration", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateSystemSwapConfiguration", targetNamespace = "urn:vim25", className = "vim25.UpdateSystemSwapConfigurationRequestType")
    @ResponseWrapper(localName = "UpdateSystemSwapConfigurationResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateSystemSwapConfigurationResponse")
    public void updateSystemSwapConfiguration(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "sysSwapConfig", targetNamespace = "urn:vim25")
        HostSystemSwapConfiguration sysSwapConfig)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param reconnectSpec
     * @param cnxSpec
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws HostConnectFaultFaultMsg
     * @throws InvalidLoginFaultMsg
     * @throws InvalidNameFaultMsg
     */
    @WebMethod(operationName = "ReconnectHost_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ReconnectHost_Task", targetNamespace = "urn:vim25", className = "vim25.ReconnectHostRequestType")
    @ResponseWrapper(localName = "ReconnectHost_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ReconnectHostTaskResponse")
    public ManagedObjectReference reconnectHostTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "cnxSpec", targetNamespace = "urn:vim25")
        HostConnectSpec cnxSpec,
        @WebParam(name = "reconnectSpec", targetNamespace = "urn:vim25")
        HostSystemReconnectSpec reconnectSpec)
        throws HostConnectFaultFaultMsg, InvalidLoginFaultMsg, InvalidNameFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "DisconnectHost_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "DisconnectHost_Task", targetNamespace = "urn:vim25", className = "vim25.DisconnectHostRequestType")
    @ResponseWrapper(localName = "DisconnectHost_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.DisconnectHostTaskResponse")
    public ManagedObjectReference disconnectHostTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param evacuatePoweredOffVms
     * @param timeout
     * @param maintenanceSpec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws TimedoutFaultMsg
     */
    @WebMethod(operationName = "EnterMaintenanceMode_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "EnterMaintenanceMode_Task", targetNamespace = "urn:vim25", className = "vim25.EnterMaintenanceModeRequestType")
    @ResponseWrapper(localName = "EnterMaintenanceMode_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.EnterMaintenanceModeTaskResponse")
    public ManagedObjectReference enterMaintenanceModeTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "timeout", targetNamespace = "urn:vim25")
        int timeout,
        @WebParam(name = "evacuatePoweredOffVms", targetNamespace = "urn:vim25")
        Boolean evacuatePoweredOffVms,
        @WebParam(name = "maintenanceSpec", targetNamespace = "urn:vim25")
        HostMaintenanceSpec maintenanceSpec)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TimedoutFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param timeout
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws TimedoutFaultMsg
     */
    @WebMethod(operationName = "ExitMaintenanceMode_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ExitMaintenanceMode_Task", targetNamespace = "urn:vim25", className = "vim25.ExitMaintenanceModeRequestType")
    @ResponseWrapper(localName = "ExitMaintenanceMode_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ExitMaintenanceModeTaskResponse")
    public ManagedObjectReference exitMaintenanceModeTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "timeout", targetNamespace = "urn:vim25")
        int timeout)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TimedoutFaultMsg
    ;

    /**
     * 
     * @param force
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "RebootHost_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RebootHost_Task", targetNamespace = "urn:vim25", className = "vim25.RebootHostRequestType")
    @ResponseWrapper(localName = "RebootHost_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.RebootHostTaskResponse")
    public ManagedObjectReference rebootHostTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "force", targetNamespace = "urn:vim25")
        boolean force)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param force
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "ShutdownHost_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ShutdownHost_Task", targetNamespace = "urn:vim25", className = "vim25.ShutdownHostRequestType")
    @ResponseWrapper(localName = "ShutdownHost_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ShutdownHostTaskResponse")
    public ManagedObjectReference shutdownHostTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "force", targetNamespace = "urn:vim25")
        boolean force)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param evacuatePoweredOffVms
     * @param timeoutSec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws RequestCanceledFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NotSupportedFaultMsg
     * @throws HostPowerOpFailedFaultMsg
     * @throws TimedoutFaultMsg
     */
    @WebMethod(operationName = "PowerDownHostToStandBy_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "PowerDownHostToStandBy_Task", targetNamespace = "urn:vim25", className = "vim25.PowerDownHostToStandByRequestType")
    @ResponseWrapper(localName = "PowerDownHostToStandBy_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.PowerDownHostToStandByTaskResponse")
    public ManagedObjectReference powerDownHostToStandByTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "timeoutSec", targetNamespace = "urn:vim25")
        int timeoutSec,
        @WebParam(name = "evacuatePoweredOffVms", targetNamespace = "urn:vim25")
        Boolean evacuatePoweredOffVms)
        throws HostPowerOpFailedFaultMsg, InvalidStateFaultMsg, NotSupportedFaultMsg, RequestCanceledFaultMsg, RuntimeFaultFaultMsg, TimedoutFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param timeoutSec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NotSupportedFaultMsg
     * @throws HostPowerOpFailedFaultMsg
     * @throws TimedoutFaultMsg
     */
    @WebMethod(operationName = "PowerUpHostFromStandBy_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "PowerUpHostFromStandBy_Task", targetNamespace = "urn:vim25", className = "vim25.PowerUpHostFromStandByRequestType")
    @ResponseWrapper(localName = "PowerUpHostFromStandBy_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.PowerUpHostFromStandByTaskResponse")
    public ManagedObjectReference powerUpHostFromStandByTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "timeoutSec", targetNamespace = "urn:vim25")
        int timeoutSec)
        throws HostPowerOpFailedFaultMsg, InvalidStateFaultMsg, NotSupportedFaultMsg, RuntimeFaultFaultMsg, TimedoutFaultMsg
    ;

    /**
     * 
     * @param memorySize
     * @param videoRamSize
     * @param _this
     * @param numVcpus
     * @return
     *     returns long
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryMemoryOverhead", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryMemoryOverhead", targetNamespace = "urn:vim25", className = "vim25.QueryMemoryOverheadRequestType")
    @ResponseWrapper(localName = "QueryMemoryOverheadResponse", targetNamespace = "urn:vim25", className = "vim25.QueryMemoryOverheadResponse")
    public long queryMemoryOverhead(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "memorySize", targetNamespace = "urn:vim25")
        long memorySize,
        @WebParam(name = "videoRamSize", targetNamespace = "urn:vim25")
        Integer videoRamSize,
        @WebParam(name = "numVcpus", targetNamespace = "urn:vim25")
        int numVcpus)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vmConfigInfo
     * @param _this
     * @return
     *     returns long
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryMemoryOverheadEx", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryMemoryOverheadEx", targetNamespace = "urn:vim25", className = "vim25.QueryMemoryOverheadExRequestType")
    @ResponseWrapper(localName = "QueryMemoryOverheadExResponse", targetNamespace = "urn:vim25", className = "vim25.QueryMemoryOverheadExResponse")
    public long queryMemoryOverheadEx(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vmConfigInfo", targetNamespace = "urn:vim25")
        VirtualMachineConfigInfo vmConfigInfo)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws DasConfigFaultFaultMsg
     */
    @WebMethod(operationName = "ReconfigureHostForDAS_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ReconfigureHostForDAS_Task", targetNamespace = "urn:vim25", className = "vim25.ReconfigureHostForDASRequestType")
    @ResponseWrapper(localName = "ReconfigureHostForDAS_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ReconfigureHostForDASTaskResponse")
    public ManagedObjectReference reconfigureHostForDASTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws DasConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param flagInfo
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateFlags", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateFlags", targetNamespace = "urn:vim25", className = "vim25.UpdateFlagsRequestType")
    @ResponseWrapper(localName = "UpdateFlagsResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateFlagsResponse")
    public void updateFlags(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "flagInfo", targetNamespace = "urn:vim25")
        HostFlagInfo flagInfo)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "EnterLockdownMode", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "EnterLockdownMode", targetNamespace = "urn:vim25", className = "vim25.EnterLockdownModeRequestType")
    @ResponseWrapper(localName = "EnterLockdownModeResponse", targetNamespace = "urn:vim25", className = "vim25.EnterLockdownModeResponse")
    public void enterLockdownMode(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "ExitLockdownMode", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ExitLockdownMode", targetNamespace = "urn:vim25", className = "vim25.ExitLockdownModeRequestType")
    @ResponseWrapper(localName = "ExitLockdownModeResponse", targetNamespace = "urn:vim25", className = "vim25.ExitLockdownModeResponse")
    public void exitLockdownMode(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.HostServiceTicket
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "AcquireCimServicesTicket", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "AcquireCimServicesTicket", targetNamespace = "urn:vim25", className = "vim25.AcquireCimServicesTicketRequestType")
    @ResponseWrapper(localName = "AcquireCimServicesTicketResponse", targetNamespace = "urn:vim25", className = "vim25.AcquireCimServicesTicketResponse")
    public HostServiceTicket acquireCimServicesTicket(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param ipmiInfo
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidIpmiMacAddressFaultMsg
     * @throws InvalidIpmiLoginInfoFaultMsg
     */
    @WebMethod(operationName = "UpdateIpmi", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateIpmi", targetNamespace = "urn:vim25", className = "vim25.UpdateIpmiRequestType")
    @ResponseWrapper(localName = "UpdateIpmiResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateIpmiResponse")
    public void updateIpmi(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "ipmiInfo", targetNamespace = "urn:vim25")
        HostIpmiInfo ipmiInfo)
        throws InvalidIpmiLoginInfoFaultMsg, InvalidIpmiMacAddressFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns long
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RetrieveHardwareUptime", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RetrieveHardwareUptime", targetNamespace = "urn:vim25", className = "vim25.RetrieveHardwareUptimeRequestType")
    @ResponseWrapper(localName = "RetrieveHardwareUptimeResponse", targetNamespace = "urn:vim25", className = "vim25.RetrieveHardwareUptimeResponse")
    public long retrieveHardwareUptime(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.util.List<vim25.HttpNfcLeaseManifestEntry>
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws TimedoutFaultMsg
     */
    @WebMethod(operationName = "HttpNfcLeaseGetManifest", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "HttpNfcLeaseGetManifest", targetNamespace = "urn:vim25", className = "vim25.HttpNfcLeaseGetManifestRequestType")
    @ResponseWrapper(localName = "HttpNfcLeaseGetManifestResponse", targetNamespace = "urn:vim25", className = "vim25.HttpNfcLeaseGetManifestResponse")
    public List<HttpNfcLeaseManifestEntry> httpNfcLeaseGetManifest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TimedoutFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws TimedoutFaultMsg
     */
    @WebMethod(operationName = "HttpNfcLeaseComplete", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "HttpNfcLeaseComplete", targetNamespace = "urn:vim25", className = "vim25.HttpNfcLeaseCompleteRequestType")
    @ResponseWrapper(localName = "HttpNfcLeaseCompleteResponse", targetNamespace = "urn:vim25", className = "vim25.HttpNfcLeaseCompleteResponse")
    public void httpNfcLeaseComplete(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TimedoutFaultMsg
    ;

    /**
     * 
     * @param fault
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws TimedoutFaultMsg
     */
    @WebMethod(operationName = "HttpNfcLeaseAbort", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "HttpNfcLeaseAbort", targetNamespace = "urn:vim25", className = "vim25.HttpNfcLeaseAbortRequestType")
    @ResponseWrapper(localName = "HttpNfcLeaseAbortResponse", targetNamespace = "urn:vim25", className = "vim25.HttpNfcLeaseAbortResponse")
    public void httpNfcLeaseAbort(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "fault", targetNamespace = "urn:vim25")
        LocalizedMethodFault fault)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TimedoutFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param percent
     * @throws RuntimeFaultFaultMsg
     * @throws TimedoutFaultMsg
     */
    @WebMethod(operationName = "HttpNfcLeaseProgress", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "HttpNfcLeaseProgress", targetNamespace = "urn:vim25", className = "vim25.HttpNfcLeaseProgressRequestType")
    @ResponseWrapper(localName = "HttpNfcLeaseProgressResponse", targetNamespace = "urn:vim25", className = "vim25.HttpNfcLeaseProgressResponse")
    public void httpNfcLeaseProgress(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "percent", targetNamespace = "urn:vim25")
        int percent)
        throws RuntimeFaultFaultMsg, TimedoutFaultMsg
    ;

    /**
     * 
     * @param vibUrl
     * @param compRes
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws AlreadyExistsFaultMsg
     */
    @WebMethod(operationName = "InstallIoFilter_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "InstallIoFilter_Task", targetNamespace = "urn:vim25", className = "vim25.InstallIoFilterRequestType")
    @ResponseWrapper(localName = "InstallIoFilter_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.InstallIoFilterTaskResponse")
    public ManagedObjectReference installIoFilterTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vibUrl", targetNamespace = "urn:vim25")
        String vibUrl,
        @WebParam(name = "compRes", targetNamespace = "urn:vim25")
        ManagedObjectReference compRes)
        throws AlreadyExistsFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param filterId
     * @param compRes
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NotFoundFaultMsg
     * @throws FilterInUseFaultMsg
     */
    @WebMethod(operationName = "UninstallIoFilter_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "UninstallIoFilter_Task", targetNamespace = "urn:vim25", className = "vim25.UninstallIoFilterRequestType")
    @ResponseWrapper(localName = "UninstallIoFilter_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.UninstallIoFilterTaskResponse")
    public ManagedObjectReference uninstallIoFilterTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "filterId", targetNamespace = "urn:vim25")
        String filterId,
        @WebParam(name = "compRes", targetNamespace = "urn:vim25")
        ManagedObjectReference compRes)
        throws FilterInUseFaultMsg, InvalidStateFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param filterId
     * @param vibUrl
     * @param compRes
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "UpgradeIoFilter_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "UpgradeIoFilter_Task", targetNamespace = "urn:vim25", className = "vim25.UpgradeIoFilterRequestType")
    @ResponseWrapper(localName = "UpgradeIoFilter_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.UpgradeIoFilterTaskResponse")
    public ManagedObjectReference upgradeIoFilterTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "filterId", targetNamespace = "urn:vim25")
        String filterId,
        @WebParam(name = "compRes", targetNamespace = "urn:vim25")
        ManagedObjectReference compRes,
        @WebParam(name = "vibUrl", targetNamespace = "urn:vim25")
        String vibUrl)
        throws InvalidStateFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param filterId
     * @param compRes
     * @param _this
     * @return
     *     returns vim25.IoFilterQueryIssueResult
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "QueryIoFilterIssues", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryIoFilterIssues", targetNamespace = "urn:vim25", className = "vim25.QueryIoFilterIssuesRequestType")
    @ResponseWrapper(localName = "QueryIoFilterIssuesResponse", targetNamespace = "urn:vim25", className = "vim25.QueryIoFilterIssuesResponse")
    public IoFilterQueryIssueResult queryIoFilterIssues(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "filterId", targetNamespace = "urn:vim25")
        String filterId,
        @WebParam(name = "compRes", targetNamespace = "urn:vim25")
        ManagedObjectReference compRes)
        throws NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param compRes
     * @param _this
     * @return
     *     returns java.util.List<vim25.ClusterIoFilterInfo>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryIoFilterInfo", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryIoFilterInfo", targetNamespace = "urn:vim25", className = "vim25.QueryIoFilterInfoRequestType")
    @ResponseWrapper(localName = "QueryIoFilterInfoResponse", targetNamespace = "urn:vim25", className = "vim25.QueryIoFilterInfoResponse")
    public List<ClusterIoFilterInfo> queryIoFilterInfo(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "compRes", targetNamespace = "urn:vim25")
        ManagedObjectReference compRes)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param filterId
     * @param host
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "ResolveInstallationErrorsOnHost_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ResolveInstallationErrorsOnHost_Task", targetNamespace = "urn:vim25", className = "vim25.ResolveInstallationErrorsOnHostRequestType")
    @ResponseWrapper(localName = "ResolveInstallationErrorsOnHost_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ResolveInstallationErrorsOnHostTaskResponse")
    public ManagedObjectReference resolveInstallationErrorsOnHostTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "filterId", targetNamespace = "urn:vim25")
        String filterId,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param filterId
     * @param cluster
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "ResolveInstallationErrorsOnCluster_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ResolveInstallationErrorsOnCluster_Task", targetNamespace = "urn:vim25", className = "vim25.ResolveInstallationErrorsOnClusterRequestType")
    @ResponseWrapper(localName = "ResolveInstallationErrorsOnCluster_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ResolveInstallationErrorsOnClusterTaskResponse")
    public ManagedObjectReference resolveInstallationErrorsOnClusterTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "filterId", targetNamespace = "urn:vim25")
        String filterId,
        @WebParam(name = "cluster", targetNamespace = "urn:vim25")
        ManagedObjectReference cluster)
        throws NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param filterId
     * @param compRes
     * @param _this
     * @return
     *     returns java.util.List<vim25.VirtualDiskId>
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "QueryDisksUsingFilter", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryDisksUsingFilter", targetNamespace = "urn:vim25", className = "vim25.QueryDisksUsingFilterRequestType")
    @ResponseWrapper(localName = "QueryDisksUsingFilterResponse", targetNamespace = "urn:vim25", className = "vim25.QueryDisksUsingFilterResponse")
    public List<VirtualDiskId> queryDisksUsingFilter(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "filterId", targetNamespace = "urn:vim25")
        String filterId,
        @WebParam(name = "compRes", targetNamespace = "urn:vim25")
        ManagedObjectReference compRes)
        throws NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param dc
     * @return
     *     returns java.util.List<vim25.IpPool>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryIpPools", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryIpPools", targetNamespace = "urn:vim25", className = "vim25.QueryIpPoolsRequestType")
    @ResponseWrapper(localName = "QueryIpPoolsResponse", targetNamespace = "urn:vim25", className = "vim25.QueryIpPoolsResponse")
    public List<IpPool> queryIpPools(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "dc", targetNamespace = "urn:vim25")
        ManagedObjectReference dc)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param pool
     * @param _this
     * @param dc
     * @return
     *     returns int
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "CreateIpPool", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateIpPool", targetNamespace = "urn:vim25", className = "vim25.CreateIpPoolRequestType")
    @ResponseWrapper(localName = "CreateIpPoolResponse", targetNamespace = "urn:vim25", className = "vim25.CreateIpPoolResponse")
    public int createIpPool(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "dc", targetNamespace = "urn:vim25")
        ManagedObjectReference dc,
        @WebParam(name = "pool", targetNamespace = "urn:vim25")
        IpPool pool)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param pool
     * @param _this
     * @param dc
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateIpPool", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateIpPool", targetNamespace = "urn:vim25", className = "vim25.UpdateIpPoolRequestType")
    @ResponseWrapper(localName = "UpdateIpPoolResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateIpPoolResponse")
    public void updateIpPool(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "dc", targetNamespace = "urn:vim25")
        ManagedObjectReference dc,
        @WebParam(name = "pool", targetNamespace = "urn:vim25")
        IpPool pool)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param force
     * @param id
     * @param _this
     * @param dc
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "DestroyIpPool", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DestroyIpPool", targetNamespace = "urn:vim25", className = "vim25.DestroyIpPoolRequestType")
    @ResponseWrapper(localName = "DestroyIpPoolResponse", targetNamespace = "urn:vim25", className = "vim25.DestroyIpPoolResponse")
    public void destroyIpPool(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "dc", targetNamespace = "urn:vim25")
        ManagedObjectReference dc,
        @WebParam(name = "id", targetNamespace = "urn:vim25")
        int id,
        @WebParam(name = "force", targetNamespace = "urn:vim25")
        boolean force)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param poolId
     * @param allocationId
     * @param _this
     * @param dc
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "AllocateIpv4Address", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "AllocateIpv4Address", targetNamespace = "urn:vim25", className = "vim25.AllocateIpv4AddressRequestType")
    @ResponseWrapper(localName = "AllocateIpv4AddressResponse", targetNamespace = "urn:vim25", className = "vim25.AllocateIpv4AddressResponse")
    public String allocateIpv4Address(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "dc", targetNamespace = "urn:vim25")
        ManagedObjectReference dc,
        @WebParam(name = "poolId", targetNamespace = "urn:vim25")
        int poolId,
        @WebParam(name = "allocationId", targetNamespace = "urn:vim25")
        String allocationId)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param poolId
     * @param allocationId
     * @param _this
     * @param dc
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "AllocateIpv6Address", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "AllocateIpv6Address", targetNamespace = "urn:vim25", className = "vim25.AllocateIpv6AddressRequestType")
    @ResponseWrapper(localName = "AllocateIpv6AddressResponse", targetNamespace = "urn:vim25", className = "vim25.AllocateIpv6AddressResponse")
    public String allocateIpv6Address(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "dc", targetNamespace = "urn:vim25")
        ManagedObjectReference dc,
        @WebParam(name = "poolId", targetNamespace = "urn:vim25")
        int poolId,
        @WebParam(name = "allocationId", targetNamespace = "urn:vim25")
        String allocationId)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param poolId
     * @param allocationId
     * @param _this
     * @param dc
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ReleaseIpAllocation", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ReleaseIpAllocation", targetNamespace = "urn:vim25", className = "vim25.ReleaseIpAllocationRequestType")
    @ResponseWrapper(localName = "ReleaseIpAllocationResponse", targetNamespace = "urn:vim25", className = "vim25.ReleaseIpAllocationResponse")
    public void releaseIpAllocation(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "dc", targetNamespace = "urn:vim25")
        ManagedObjectReference dc,
        @WebParam(name = "poolId", targetNamespace = "urn:vim25")
        int poolId,
        @WebParam(name = "allocationId", targetNamespace = "urn:vim25")
        String allocationId)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param poolId
     * @param extensionKey
     * @param _this
     * @param dc
     * @return
     *     returns java.util.List<vim25.IpPoolManagerIpAllocation>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryIPAllocations", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryIPAllocations", targetNamespace = "urn:vim25", className = "vim25.QueryIPAllocationsRequestType")
    @ResponseWrapper(localName = "QueryIPAllocationsResponse", targetNamespace = "urn:vim25", className = "vim25.QueryIPAllocationsResponse")
    public List<IpPoolManagerIpAllocation> queryIPAllocations(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "dc", targetNamespace = "urn:vim25")
        ManagedObjectReference dc,
        @WebParam(name = "poolId", targetNamespace = "urn:vim25")
        int poolId,
        @WebParam(name = "extensionKey", targetNamespace = "urn:vim25")
        String extensionKey)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param licenseKey
     * @param entityDisplayName
     * @param _this
     * @param entity
     * @return
     *     returns vim25.LicenseManagerLicenseInfo
     * @throws RuntimeFaultFaultMsg
     * @throws LicenseEntityNotFoundFaultMsg
     */
    @WebMethod(operationName = "UpdateAssignedLicense", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "UpdateAssignedLicense", targetNamespace = "urn:vim25", className = "vim25.UpdateAssignedLicenseRequestType")
    @ResponseWrapper(localName = "UpdateAssignedLicenseResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateAssignedLicenseResponse")
    public LicenseManagerLicenseInfo updateAssignedLicense(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        String entity,
        @WebParam(name = "licenseKey", targetNamespace = "urn:vim25")
        String licenseKey,
        @WebParam(name = "entityDisplayName", targetNamespace = "urn:vim25")
        String entityDisplayName)
        throws LicenseEntityNotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param entityId
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws LicenseEntityNotFoundFaultMsg
     */
    @WebMethod(operationName = "RemoveAssignedLicense", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RemoveAssignedLicense", targetNamespace = "urn:vim25", className = "vim25.RemoveAssignedLicenseRequestType")
    @ResponseWrapper(localName = "RemoveAssignedLicenseResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveAssignedLicenseResponse")
    public void removeAssignedLicense(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entityId", targetNamespace = "urn:vim25")
        String entityId)
        throws LicenseEntityNotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param entityId
     * @param _this
     * @return
     *     returns java.util.List<vim25.LicenseAssignmentManagerLicenseAssignment>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryAssignedLicenses", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryAssignedLicenses", targetNamespace = "urn:vim25", className = "vim25.QueryAssignedLicensesRequestType")
    @ResponseWrapper(localName = "QueryAssignedLicensesResponse", targetNamespace = "urn:vim25", className = "vim25.QueryAssignedLicensesResponse")
    public List<LicenseAssignmentManagerLicenseAssignment> queryAssignedLicenses(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entityId", targetNamespace = "urn:vim25")
        String entityId)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @return
     *     returns java.util.List<vim25.LicenseFeatureInfo>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QuerySupportedFeatures", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QuerySupportedFeatures", targetNamespace = "urn:vim25", className = "vim25.QuerySupportedFeaturesRequestType")
    @ResponseWrapper(localName = "QuerySupportedFeaturesResponse", targetNamespace = "urn:vim25", className = "vim25.QuerySupportedFeaturesResponse")
    public List<LicenseFeatureInfo> querySupportedFeatures(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @return
     *     returns java.util.List<vim25.LicenseAvailabilityInfo>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryLicenseSourceAvailability", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryLicenseSourceAvailability", targetNamespace = "urn:vim25", className = "vim25.QueryLicenseSourceAvailabilityRequestType")
    @ResponseWrapper(localName = "QueryLicenseSourceAvailabilityResponse", targetNamespace = "urn:vim25", className = "vim25.QueryLicenseSourceAvailabilityResponse")
    public List<LicenseAvailabilityInfo> queryLicenseSourceAvailability(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @return
     *     returns vim25.LicenseUsageInfo
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryLicenseUsage", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryLicenseUsage", targetNamespace = "urn:vim25", className = "vim25.QueryLicenseUsageRequestType")
    @ResponseWrapper(localName = "QueryLicenseUsageResponse", targetNamespace = "urn:vim25", className = "vim25.QueryLicenseUsageResponse")
    public LicenseUsageInfo queryLicenseUsage(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @param featureKey
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws LicenseServerUnavailableFaultMsg
     */
    @WebMethod(operationName = "SetLicenseEdition", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "SetLicenseEdition", targetNamespace = "urn:vim25", className = "vim25.SetLicenseEditionRequestType")
    @ResponseWrapper(localName = "SetLicenseEditionResponse", targetNamespace = "urn:vim25", className = "vim25.SetLicenseEditionResponse")
    public void setLicenseEdition(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host,
        @WebParam(name = "featureKey", targetNamespace = "urn:vim25")
        String featureKey)
        throws InvalidStateFaultMsg, LicenseServerUnavailableFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @param featureKey
     * @return
     *     returns boolean
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "CheckLicenseFeature", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CheckLicenseFeature", targetNamespace = "urn:vim25", className = "vim25.CheckLicenseFeatureRequestType")
    @ResponseWrapper(localName = "CheckLicenseFeatureResponse", targetNamespace = "urn:vim25", className = "vim25.CheckLicenseFeatureResponse")
    public boolean checkLicenseFeature(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host,
        @WebParam(name = "featureKey", targetNamespace = "urn:vim25")
        String featureKey)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @param featureKey
     * @return
     *     returns boolean
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws LicenseServerUnavailableFaultMsg
     */
    @WebMethod(operationName = "EnableFeature", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "EnableFeature", targetNamespace = "urn:vim25", className = "vim25.EnableFeatureRequestType")
    @ResponseWrapper(localName = "EnableFeatureResponse", targetNamespace = "urn:vim25", className = "vim25.EnableFeatureResponse")
    public boolean enableFeature(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host,
        @WebParam(name = "featureKey", targetNamespace = "urn:vim25")
        String featureKey)
        throws InvalidStateFaultMsg, LicenseServerUnavailableFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @param featureKey
     * @return
     *     returns boolean
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws LicenseServerUnavailableFaultMsg
     */
    @WebMethod(operationName = "DisableFeature", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "DisableFeature", targetNamespace = "urn:vim25", className = "vim25.DisableFeatureRequestType")
    @ResponseWrapper(localName = "DisableFeatureResponse", targetNamespace = "urn:vim25", className = "vim25.DisableFeatureResponse")
    public boolean disableFeature(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host,
        @WebParam(name = "featureKey", targetNamespace = "urn:vim25")
        String featureKey)
        throws InvalidStateFaultMsg, LicenseServerUnavailableFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param licenseSource
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidLicenseFaultMsg
     * @throws CannotAccessLocalSourceFaultMsg
     * @throws LicenseServerUnavailableFaultMsg
     */
    @WebMethod(operationName = "ConfigureLicenseSource", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ConfigureLicenseSource", targetNamespace = "urn:vim25", className = "vim25.ConfigureLicenseSourceRequestType")
    @ResponseWrapper(localName = "ConfigureLicenseSourceResponse", targetNamespace = "urn:vim25", className = "vim25.ConfigureLicenseSourceResponse")
    public void configureLicenseSource(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host,
        @WebParam(name = "licenseSource", targetNamespace = "urn:vim25")
        LicenseSource licenseSource)
        throws CannotAccessLocalSourceFaultMsg, InvalidLicenseFaultMsg, LicenseServerUnavailableFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param licenseKey
     * @param _this
     * @param labels
     * @return
     *     returns vim25.LicenseManagerLicenseInfo
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateLicense", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "UpdateLicense", targetNamespace = "urn:vim25", className = "vim25.UpdateLicenseRequestType")
    @ResponseWrapper(localName = "UpdateLicenseResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateLicenseResponse")
    public LicenseManagerLicenseInfo updateLicense(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "licenseKey", targetNamespace = "urn:vim25")
        String licenseKey,
        @WebParam(name = "labels", targetNamespace = "urn:vim25")
        List<KeyValue> labels)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param licenseKey
     * @param _this
     * @param labels
     * @return
     *     returns vim25.LicenseManagerLicenseInfo
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "AddLicense", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "AddLicense", targetNamespace = "urn:vim25", className = "vim25.AddLicenseRequestType")
    @ResponseWrapper(localName = "AddLicenseResponse", targetNamespace = "urn:vim25", className = "vim25.AddLicenseResponse")
    public LicenseManagerLicenseInfo addLicense(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "licenseKey", targetNamespace = "urn:vim25")
        String licenseKey,
        @WebParam(name = "labels", targetNamespace = "urn:vim25")
        List<KeyValue> labels)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param licenseKey
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RemoveLicense", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RemoveLicense", targetNamespace = "urn:vim25", className = "vim25.RemoveLicenseRequestType")
    @ResponseWrapper(localName = "RemoveLicenseResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveLicenseResponse")
    public void removeLicense(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "licenseKey", targetNamespace = "urn:vim25")
        String licenseKey)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param licenseKey
     * @param _this
     * @return
     *     returns vim25.LicenseManagerLicenseInfo
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "DecodeLicense", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "DecodeLicense", targetNamespace = "urn:vim25", className = "vim25.DecodeLicenseRequestType")
    @ResponseWrapper(localName = "DecodeLicenseResponse", targetNamespace = "urn:vim25", className = "vim25.DecodeLicenseResponse")
    public LicenseManagerLicenseInfo decodeLicense(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "licenseKey", targetNamespace = "urn:vim25")
        String licenseKey)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param licenseKey
     * @param labelValue
     * @param _this
     * @param labelKey
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateLicenseLabel", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateLicenseLabel", targetNamespace = "urn:vim25", className = "vim25.UpdateLicenseLabelRequestType")
    @ResponseWrapper(localName = "UpdateLicenseLabelResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateLicenseLabelResponse")
    public void updateLicenseLabel(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "licenseKey", targetNamespace = "urn:vim25")
        String licenseKey,
        @WebParam(name = "labelKey", targetNamespace = "urn:vim25")
        String labelKey,
        @WebParam(name = "labelValue", targetNamespace = "urn:vim25")
        String labelValue)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param licenseKey
     * @param _this
     * @param labelKey
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RemoveLicenseLabel", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RemoveLicenseLabel", targetNamespace = "urn:vim25", className = "vim25.RemoveLicenseLabelRequestType")
    @ResponseWrapper(localName = "RemoveLicenseLabelResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveLicenseLabelResponse")
    public void removeLicenseLabel(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "licenseKey", targetNamespace = "urn:vim25")
        String licenseKey,
        @WebParam(name = "labelKey", targetNamespace = "urn:vim25")
        String labelKey)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "Reload", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "Reload", targetNamespace = "urn:vim25", className = "vim25.ReloadRequestType")
    @ResponseWrapper(localName = "ReloadResponse", targetNamespace = "urn:vim25", className = "vim25.ReloadResponse")
    public void reload(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param newName
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "Rename_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "Rename_Task", targetNamespace = "urn:vim25", className = "vim25.RenameRequestType")
    @ResponseWrapper(localName = "Rename_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.RenameTaskResponse")
    public ManagedObjectReference renameTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "newName", targetNamespace = "urn:vim25")
        String newName)
        throws DuplicateNameFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws VimFaultFaultMsg
     */
    @WebMethod(operationName = "Destroy_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "Destroy_Task", targetNamespace = "urn:vim25", className = "vim25.DestroyRequestType")
    @ResponseWrapper(localName = "Destroy_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.DestroyTaskResponse")
    public ManagedObjectReference destroyTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg, VimFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws ResourceInUseFaultMsg
     */
    @WebMethod(operationName = "DestroyNetwork", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DestroyNetwork", targetNamespace = "urn:vim25", className = "vim25.DestroyNetworkRequestType")
    @ResponseWrapper(localName = "DestroyNetworkResponse", targetNamespace = "urn:vim25", className = "vim25.DestroyNetworkResponse")
    public void destroyNetwork(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vm
     * @param host
     * @param _this
     * @return
     *     returns long
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidTypeFaultMsg
     * @throws NotFoundFaultMsg
     * @throws InvalidArgumentFaultMsg
     * @throws ManagedObjectNotFoundFaultMsg
     */
    @WebMethod(operationName = "LookupVmOverheadMemory", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "LookupVmOverheadMemory", targetNamespace = "urn:vim25", className = "vim25.LookupVmOverheadMemoryRequestType")
    @ResponseWrapper(localName = "LookupVmOverheadMemoryResponse", targetNamespace = "urn:vim25", className = "vim25.LookupVmOverheadMemoryResponse")
    public long lookupVmOverheadMemory(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws InvalidArgumentFaultMsg, InvalidTypeFaultMsg, ManagedObjectNotFoundFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param ovfDescriptor
     * @param _this
     * @param vhp
     * @return
     *     returns vim25.OvfValidateHostResult
     * @throws RuntimeFaultFaultMsg
     * @throws ConcurrentAccessFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "ValidateHost", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ValidateHost", targetNamespace = "urn:vim25", className = "vim25.ValidateHostRequestType")
    @ResponseWrapper(localName = "ValidateHostResponse", targetNamespace = "urn:vim25", className = "vim25.ValidateHostResponse")
    public OvfValidateHostResult validateHost(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "ovfDescriptor", targetNamespace = "urn:vim25")
        String ovfDescriptor,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host,
        @WebParam(name = "vhp", targetNamespace = "urn:vim25")
        OvfValidateHostParams vhp)
        throws ConcurrentAccessFaultMsg, FileFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param ovfDescriptor
     * @param _this
     * @param pdp
     * @return
     *     returns vim25.OvfParseDescriptorResult
     * @throws RuntimeFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws ConcurrentAccessFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "ParseDescriptor", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ParseDescriptor", targetNamespace = "urn:vim25", className = "vim25.ParseDescriptorRequestType")
    @ResponseWrapper(localName = "ParseDescriptorResponse", targetNamespace = "urn:vim25", className = "vim25.ParseDescriptorResponse")
    public OvfParseDescriptorResult parseDescriptor(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "ovfDescriptor", targetNamespace = "urn:vim25")
        String ovfDescriptor,
        @WebParam(name = "pdp", targetNamespace = "urn:vim25")
        OvfParseDescriptorParams pdp)
        throws ConcurrentAccessFaultMsg, FileFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param datastore
     * @param ovfDescriptor
     * @param _this
     * @param cisp
     * @param resourcePool
     * @return
     *     returns vim25.OvfCreateImportSpecResult
     * @throws RuntimeFaultFaultMsg
     * @throws ConcurrentAccessFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "CreateImportSpec", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateImportSpec", targetNamespace = "urn:vim25", className = "vim25.CreateImportSpecRequestType")
    @ResponseWrapper(localName = "CreateImportSpecResponse", targetNamespace = "urn:vim25", className = "vim25.CreateImportSpecResponse")
    public OvfCreateImportSpecResult createImportSpec(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "ovfDescriptor", targetNamespace = "urn:vim25")
        String ovfDescriptor,
        @WebParam(name = "resourcePool", targetNamespace = "urn:vim25")
        ManagedObjectReference resourcePool,
        @WebParam(name = "datastore", targetNamespace = "urn:vim25")
        ManagedObjectReference datastore,
        @WebParam(name = "cisp", targetNamespace = "urn:vim25")
        OvfCreateImportSpecParams cisp)
        throws ConcurrentAccessFaultMsg, FileFaultFaultMsg, InvalidDatastoreFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param obj
     * @param _this
     * @param cdp
     * @return
     *     returns vim25.OvfCreateDescriptorResult
     * @throws RuntimeFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws ConcurrentAccessFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "CreateDescriptor", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateDescriptor", targetNamespace = "urn:vim25", className = "vim25.CreateDescriptorRequestType")
    @ResponseWrapper(localName = "CreateDescriptorResponse", targetNamespace = "urn:vim25", className = "vim25.CreateDescriptorResponse")
    public OvfCreateDescriptorResult createDescriptor(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "obj", targetNamespace = "urn:vim25")
        ManagedObjectReference obj,
        @WebParam(name = "cdp", targetNamespace = "urn:vim25")
        OvfCreateDescriptorParams cdp)
        throws ConcurrentAccessFaultMsg, FileFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param entity
     * @return
     *     returns vim25.PerfProviderSummary
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryPerfProviderSummary", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryPerfProviderSummary", targetNamespace = "urn:vim25", className = "vim25.QueryPerfProviderSummaryRequestType")
    @ResponseWrapper(localName = "QueryPerfProviderSummaryResponse", targetNamespace = "urn:vim25", className = "vim25.QueryPerfProviderSummaryResponse")
    public PerfProviderSummary queryPerfProviderSummary(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        ManagedObjectReference entity)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param intervalId
     * @param beginTime
     * @param endTime
     * @param _this
     * @param entity
     * @return
     *     returns java.util.List<vim25.PerfMetricId>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryAvailablePerfMetric", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryAvailablePerfMetric", targetNamespace = "urn:vim25", className = "vim25.QueryAvailablePerfMetricRequestType")
    @ResponseWrapper(localName = "QueryAvailablePerfMetricResponse", targetNamespace = "urn:vim25", className = "vim25.QueryAvailablePerfMetricResponse")
    public List<PerfMetricId> queryAvailablePerfMetric(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        ManagedObjectReference entity,
        @WebParam(name = "beginTime", targetNamespace = "urn:vim25")
        Calendar beginTime,
        @WebParam(name = "endTime", targetNamespace = "urn:vim25")
        Calendar endTime,
        @WebParam(name = "intervalId", targetNamespace = "urn:vim25")
        Integer intervalId)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param counterId
     * @return
     *     returns java.util.List<vim25.PerfCounterInfo>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryPerfCounter", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryPerfCounter", targetNamespace = "urn:vim25", className = "vim25.QueryPerfCounterRequestType")
    @ResponseWrapper(localName = "QueryPerfCounterResponse", targetNamespace = "urn:vim25", className = "vim25.QueryPerfCounterResponse")
    public List<PerfCounterInfo> queryPerfCounter(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "counterId", targetNamespace = "urn:vim25")
        List<Integer> counterId)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param level
     * @param _this
     * @return
     *     returns java.util.List<vim25.PerfCounterInfo>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryPerfCounterByLevel", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryPerfCounterByLevel", targetNamespace = "urn:vim25", className = "vim25.QueryPerfCounterByLevelRequestType")
    @ResponseWrapper(localName = "QueryPerfCounterByLevelResponse", targetNamespace = "urn:vim25", className = "vim25.QueryPerfCounterByLevelResponse")
    public List<PerfCounterInfo> queryPerfCounterByLevel(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "level", targetNamespace = "urn:vim25")
        int level)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param querySpec
     * @return
     *     returns java.util.List<vim25.PerfEntityMetricBase>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryPerf", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryPerf", targetNamespace = "urn:vim25", className = "vim25.QueryPerfRequestType")
    @ResponseWrapper(localName = "QueryPerfResponse", targetNamespace = "urn:vim25", className = "vim25.QueryPerfResponse")
    public List<PerfEntityMetricBase> queryPerf(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "querySpec", targetNamespace = "urn:vim25")
        List<PerfQuerySpec> querySpec)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param querySpec
     * @return
     *     returns vim25.PerfCompositeMetric
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryPerfComposite", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryPerfComposite", targetNamespace = "urn:vim25", className = "vim25.QueryPerfCompositeRequestType")
    @ResponseWrapper(localName = "QueryPerfCompositeResponse", targetNamespace = "urn:vim25", className = "vim25.QueryPerfCompositeResponse")
    public PerfCompositeMetric queryPerfComposite(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "querySpec", targetNamespace = "urn:vim25")
        PerfQuerySpec querySpec)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param intervalId
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "CreatePerfInterval", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "CreatePerfInterval", targetNamespace = "urn:vim25", className = "vim25.CreatePerfIntervalRequestType")
    @ResponseWrapper(localName = "CreatePerfIntervalResponse", targetNamespace = "urn:vim25", className = "vim25.CreatePerfIntervalResponse")
    public void createPerfInterval(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "intervalId", targetNamespace = "urn:vim25")
        PerfInterval intervalId)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param samplePeriod
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RemovePerfInterval", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RemovePerfInterval", targetNamespace = "urn:vim25", className = "vim25.RemovePerfIntervalRequestType")
    @ResponseWrapper(localName = "RemovePerfIntervalResponse", targetNamespace = "urn:vim25", className = "vim25.RemovePerfIntervalResponse")
    public void removePerfInterval(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "samplePeriod", targetNamespace = "urn:vim25")
        int samplePeriod)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param interval
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "UpdatePerfInterval", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdatePerfInterval", targetNamespace = "urn:vim25", className = "vim25.UpdatePerfIntervalRequestType")
    @ResponseWrapper(localName = "UpdatePerfIntervalResponse", targetNamespace = "urn:vim25", className = "vim25.UpdatePerfIntervalResponse")
    public void updatePerfInterval(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "interval", targetNamespace = "urn:vim25")
        PerfInterval interval)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param counterLevelMap
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateCounterLevelMapping", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateCounterLevelMapping", targetNamespace = "urn:vim25", className = "vim25.UpdateCounterLevelMappingRequestType")
    @ResponseWrapper(localName = "UpdateCounterLevelMappingResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateCounterLevelMappingResponse")
    public void updateCounterLevelMapping(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "counterLevelMap", targetNamespace = "urn:vim25")
        List<PerformanceManagerCounterLevelMapping> counterLevelMap)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param counters
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ResetCounterLevelMapping", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ResetCounterLevelMapping", targetNamespace = "urn:vim25", className = "vim25.ResetCounterLevelMappingRequestType")
    @ResponseWrapper(localName = "ResetCounterLevelMappingResponse", targetNamespace = "urn:vim25", className = "vim25.ResetCounterLevelMappingResponse")
    public void resetCounterLevelMapping(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "counters", targetNamespace = "urn:vim25")
        List<Integer> counters)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param dbSizeParam
     * @param _this
     * @return
     *     returns vim25.DatabaseSizeEstimate
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "EstimateDatabaseSize", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "EstimateDatabaseSize", targetNamespace = "urn:vim25", className = "vim25.EstimateDatabaseSizeRequestType")
    @ResponseWrapper(localName = "EstimateDatabaseSizeResponse", targetNamespace = "urn:vim25", className = "vim25.EstimateDatabaseSizeResponse")
    public DatabaseSizeEstimate estimateDatabaseSize(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "dbSizeParam", targetNamespace = "urn:vim25")
        DatabaseSizeParam dbSizeParam)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param _this
     * @param config
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws ConcurrentAccessFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "UpdateConfig", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateConfig", targetNamespace = "urn:vim25", className = "vim25.UpdateConfigRequestType")
    @ResponseWrapper(localName = "UpdateConfigResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateConfigResponse")
    public void updateConfig(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "config", targetNamespace = "urn:vim25")
        ResourceConfigSpec config)
        throws ConcurrentAccessFaultMsg, DuplicateNameFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param list
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "MoveIntoResourcePool", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "MoveIntoResourcePool", targetNamespace = "urn:vim25", className = "vim25.MoveIntoResourcePoolRequestType")
    @ResponseWrapper(localName = "MoveIntoResourcePoolResponse", targetNamespace = "urn:vim25", className = "vim25.MoveIntoResourcePoolResponse")
    public void moveIntoResourcePool(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "list", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> list)
        throws DuplicateNameFaultMsg, InsufficientResourcesFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "UpdateChildResourceConfiguration", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateChildResourceConfiguration", targetNamespace = "urn:vim25", className = "vim25.UpdateChildResourceConfigurationRequestType")
    @ResponseWrapper(localName = "UpdateChildResourceConfigurationResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateChildResourceConfigurationResponse")
    public void updateChildResourceConfiguration(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        List<ResourceConfigSpec> spec)
        throws InsufficientResourcesFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "CreateResourcePool", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateResourcePool", targetNamespace = "urn:vim25", className = "vim25.CreateResourcePoolRequestType")
    @ResponseWrapper(localName = "CreateResourcePoolResponse", targetNamespace = "urn:vim25", className = "vim25.CreateResourcePoolResponse")
    public ManagedObjectReference createResourcePool(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        ResourceConfigSpec spec)
        throws DuplicateNameFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "DestroyChildren", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DestroyChildren", targetNamespace = "urn:vim25", className = "vim25.DestroyChildrenRequestType")
    @ResponseWrapper(localName = "DestroyChildrenResponse", targetNamespace = "urn:vim25", className = "vim25.DestroyChildrenResponse")
    public void destroyChildren(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param resSpec
     * @param configSpec
     * @param vmFolder
     * @param name
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "CreateVApp", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateVApp", targetNamespace = "urn:vim25", className = "vim25.CreateVAppRequestType")
    @ResponseWrapper(localName = "CreateVAppResponse", targetNamespace = "urn:vim25", className = "vim25.CreateVAppResponse")
    public ManagedObjectReference createVApp(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "resSpec", targetNamespace = "urn:vim25")
        ResourceConfigSpec resSpec,
        @WebParam(name = "configSpec", targetNamespace = "urn:vim25")
        VAppConfigSpec configSpec,
        @WebParam(name = "vmFolder", targetNamespace = "urn:vim25")
        ManagedObjectReference vmFolder)
        throws DuplicateNameFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidNameFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @param config
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws OutOfBoundsFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "CreateChildVM_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateChildVM_Task", targetNamespace = "urn:vim25", className = "vim25.CreateChildVMRequestType")
    @ResponseWrapper(localName = "CreateChildVM_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CreateChildVMTaskResponse")
    public ManagedObjectReference createChildVMTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "config", targetNamespace = "urn:vim25")
        VirtualMachineConfigSpec config,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidDatastoreFaultMsg, InvalidNameFaultMsg, OutOfBoundsFaultMsg, RuntimeFaultFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param path
     * @param name
     * @param host
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws AlreadyExistsFaultMsg
     * @throws OutOfBoundsFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "RegisterChildVM_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RegisterChildVM_Task", targetNamespace = "urn:vim25", className = "vim25.RegisterChildVMRequestType")
    @ResponseWrapper(localName = "RegisterChildVM_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.RegisterChildVMTaskResponse")
    public ManagedObjectReference registerChildVMTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "path", targetNamespace = "urn:vim25")
        String path,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws AlreadyExistsFaultMsg, FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidDatastoreFaultMsg, InvalidNameFaultMsg, NotFoundFaultMsg, OutOfBoundsFaultMsg, RuntimeFaultFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param folder
     * @param host
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws OutOfBoundsFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "ImportVApp", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ImportVApp", targetNamespace = "urn:vim25", className = "vim25.ImportVAppRequestType")
    @ResponseWrapper(localName = "ImportVAppResponse", targetNamespace = "urn:vim25", className = "vim25.ImportVAppResponse")
    public ManagedObjectReference importVApp(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        ImportSpec spec,
        @WebParam(name = "folder", targetNamespace = "urn:vim25")
        ManagedObjectReference folder,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws DuplicateNameFaultMsg, FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidDatastoreFaultMsg, InvalidNameFaultMsg, OutOfBoundsFaultMsg, RuntimeFaultFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ResourceConfigOption
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryResourceConfigOption", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryResourceConfigOption", targetNamespace = "urn:vim25", className = "vim25.QueryResourceConfigOptionRequestType")
    @ResponseWrapper(localName = "QueryResourceConfigOptionResponse", targetNamespace = "urn:vim25", className = "vim25.QueryResourceConfigOptionResponse")
    public ResourceConfigOption queryResourceConfigOption(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RefreshRuntime", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RefreshRuntime", targetNamespace = "urn:vim25", className = "vim25.RefreshRuntimeRequestType")
    @ResponseWrapper(localName = "RefreshRuntimeResponse", targetNamespace = "urn:vim25", className = "vim25.RefreshRuntimeResponse")
    public void refreshRuntime(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param instanceUuid
     * @param datacenter
     * @param _this
     * @param uuid
     * @param vmSearch
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "FindByUuid", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "FindByUuid", targetNamespace = "urn:vim25", className = "vim25.FindByUuidRequestType")
    @ResponseWrapper(localName = "FindByUuidResponse", targetNamespace = "urn:vim25", className = "vim25.FindByUuidResponse")
    public ManagedObjectReference findByUuid(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "datacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference datacenter,
        @WebParam(name = "uuid", targetNamespace = "urn:vim25")
        String uuid,
        @WebParam(name = "vmSearch", targetNamespace = "urn:vim25")
        boolean vmSearch,
        @WebParam(name = "instanceUuid", targetNamespace = "urn:vim25")
        Boolean instanceUuid)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param path
     * @param datacenter
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "FindByDatastorePath", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "FindByDatastorePath", targetNamespace = "urn:vim25", className = "vim25.FindByDatastorePathRequestType")
    @ResponseWrapper(localName = "FindByDatastorePathResponse", targetNamespace = "urn:vim25", className = "vim25.FindByDatastorePathResponse")
    public ManagedObjectReference findByDatastorePath(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "datacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference datacenter,
        @WebParam(name = "path", targetNamespace = "urn:vim25")
        String path)
        throws InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param dnsName
     * @param datacenter
     * @param _this
     * @param vmSearch
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "FindByDnsName", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "FindByDnsName", targetNamespace = "urn:vim25", className = "vim25.FindByDnsNameRequestType")
    @ResponseWrapper(localName = "FindByDnsNameResponse", targetNamespace = "urn:vim25", className = "vim25.FindByDnsNameResponse")
    public ManagedObjectReference findByDnsName(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "datacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference datacenter,
        @WebParam(name = "dnsName", targetNamespace = "urn:vim25")
        String dnsName,
        @WebParam(name = "vmSearch", targetNamespace = "urn:vim25")
        boolean vmSearch)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param ip
     * @param datacenter
     * @param _this
     * @param vmSearch
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "FindByIp", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "FindByIp", targetNamespace = "urn:vim25", className = "vim25.FindByIpRequestType")
    @ResponseWrapper(localName = "FindByIpResponse", targetNamespace = "urn:vim25", className = "vim25.FindByIpResponse")
    public ManagedObjectReference findByIp(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "datacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference datacenter,
        @WebParam(name = "ip", targetNamespace = "urn:vim25")
        String ip,
        @WebParam(name = "vmSearch", targetNamespace = "urn:vim25")
        boolean vmSearch)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param inventoryPath
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "FindByInventoryPath", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "FindByInventoryPath", targetNamespace = "urn:vim25", className = "vim25.FindByInventoryPathRequestType")
    @ResponseWrapper(localName = "FindByInventoryPathResponse", targetNamespace = "urn:vim25", className = "vim25.FindByInventoryPathResponse")
    public ManagedObjectReference findByInventoryPath(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "inventoryPath", targetNamespace = "urn:vim25")
        String inventoryPath)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param _this
     * @param entity
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "FindChild", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "FindChild", targetNamespace = "urn:vim25", className = "vim25.FindChildRequestType")
    @ResponseWrapper(localName = "FindChildResponse", targetNamespace = "urn:vim25", className = "vim25.FindChildResponse")
    public ManagedObjectReference findChild(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        ManagedObjectReference entity,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param instanceUuid
     * @param datacenter
     * @param _this
     * @param uuid
     * @param vmSearch
     * @return
     *     returns java.util.List<vim25.ManagedObjectReference>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "FindAllByUuid", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "FindAllByUuid", targetNamespace = "urn:vim25", className = "vim25.FindAllByUuidRequestType")
    @ResponseWrapper(localName = "FindAllByUuidResponse", targetNamespace = "urn:vim25", className = "vim25.FindAllByUuidResponse")
    public List<ManagedObjectReference> findAllByUuid(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "datacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference datacenter,
        @WebParam(name = "uuid", targetNamespace = "urn:vim25")
        String uuid,
        @WebParam(name = "vmSearch", targetNamespace = "urn:vim25")
        boolean vmSearch,
        @WebParam(name = "instanceUuid", targetNamespace = "urn:vim25")
        Boolean instanceUuid)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param dnsName
     * @param datacenter
     * @param _this
     * @param vmSearch
     * @return
     *     returns java.util.List<vim25.ManagedObjectReference>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "FindAllByDnsName", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "FindAllByDnsName", targetNamespace = "urn:vim25", className = "vim25.FindAllByDnsNameRequestType")
    @ResponseWrapper(localName = "FindAllByDnsNameResponse", targetNamespace = "urn:vim25", className = "vim25.FindAllByDnsNameResponse")
    public List<ManagedObjectReference> findAllByDnsName(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "datacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference datacenter,
        @WebParam(name = "dnsName", targetNamespace = "urn:vim25")
        String dnsName,
        @WebParam(name = "vmSearch", targetNamespace = "urn:vim25")
        boolean vmSearch)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param ip
     * @param datacenter
     * @param _this
     * @param vmSearch
     * @return
     *     returns java.util.List<vim25.ManagedObjectReference>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "FindAllByIp", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "FindAllByIp", targetNamespace = "urn:vim25", className = "vim25.FindAllByIpRequestType")
    @ResponseWrapper(localName = "FindAllByIpResponse", targetNamespace = "urn:vim25", className = "vim25.FindAllByIpResponse")
    public List<ManagedObjectReference> findAllByIp(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "datacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference datacenter,
        @WebParam(name = "ip", targetNamespace = "urn:vim25")
        String ip,
        @WebParam(name = "vmSearch", targetNamespace = "urn:vim25")
        boolean vmSearch)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.util.Calendar
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "CurrentTime", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CurrentTime", targetNamespace = "urn:vim25", className = "vim25.CurrentTimeRequestType")
    @ResponseWrapper(localName = "CurrentTimeResponse", targetNamespace = "urn:vim25", className = "vim25.CurrentTimeResponse")
    public Calendar currentTime(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ServiceContent
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RetrieveServiceContent", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RetrieveServiceContent", targetNamespace = "urn:vim25", className = "vim25.RetrieveServiceContentRequestType")
    @ResponseWrapper(localName = "RetrieveServiceContentResponse", targetNamespace = "urn:vim25", className = "vim25.RetrieveServiceContentResponse")
    public ServiceContent retrieveServiceContent(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vm
     * @param pool
     * @param host
     * @param testType
     * @param state
     * @param _this
     * @return
     *     returns java.util.List<vim25.Event>
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "ValidateMigration", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ValidateMigration", targetNamespace = "urn:vim25", className = "vim25.ValidateMigrationRequestType")
    @ResponseWrapper(localName = "ValidateMigrationResponse", targetNamespace = "urn:vim25", className = "vim25.ValidateMigrationResponse")
    public List<Event> validateMigration(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> vm,
        @WebParam(name = "state", targetNamespace = "urn:vim25")
        VirtualMachinePowerState state,
        @WebParam(name = "testType", targetNamespace = "urn:vim25")
        List<String> testType,
        @WebParam(name = "pool", targetNamespace = "urn:vim25")
        ManagedObjectReference pool,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vm
     * @param host
     * @param _this
     * @param compatibility
     * @return
     *     returns java.util.List<vim25.HostVMotionCompatibility>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryVMotionCompatibility", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryVMotionCompatibility", targetNamespace = "urn:vim25", className = "vim25.QueryVMotionCompatibilityRequestType")
    @ResponseWrapper(localName = "QueryVMotionCompatibilityResponse", targetNamespace = "urn:vim25", className = "vim25.QueryVMotionCompatibilityResponse")
    public List<HostVMotionCompatibility> queryVMotionCompatibility(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> host,
        @WebParam(name = "compatibility", targetNamespace = "urn:vim25")
        List<String> compatibility)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.util.List<vim25.ProductComponentInfo>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RetrieveProductComponents", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RetrieveProductComponents", targetNamespace = "urn:vim25", className = "vim25.RetrieveProductComponentsRequestType")
    @ResponseWrapper(localName = "RetrieveProductComponentsResponse", targetNamespace = "urn:vim25", className = "vim25.RetrieveProductComponentsResponse")
    public List<ProductComponentInfo> retrieveProductComponents(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param location
     * @param _this
     * @param serviceName
     * @return
     *     returns java.util.List<vim25.ServiceManagerServiceInfo>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryServiceList", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryServiceList", targetNamespace = "urn:vim25", className = "vim25.QueryServiceListRequestType")
    @ResponseWrapper(localName = "QueryServiceListResponse", targetNamespace = "urn:vim25", className = "vim25.QueryServiceListResponse")
    public List<ServiceManagerServiceInfo> queryServiceList(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "serviceName", targetNamespace = "urn:vim25")
        String serviceName,
        @WebParam(name = "location", targetNamespace = "urn:vim25")
        List<String> location)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param message
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateServiceMessage", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateServiceMessage", targetNamespace = "urn:vim25", className = "vim25.UpdateServiceMessageRequestType")
    @ResponseWrapper(localName = "UpdateServiceMessageResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateServiceMessageResponse")
    public void updateServiceMessage(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "message", targetNamespace = "urn:vim25")
        String message)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param locale
     * @return
     *     returns vim25.UserSession
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidLoginFaultMsg
     * @throws InvalidLocaleFaultMsg
     */
    @WebMethod(operationName = "LoginByToken", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "LoginByToken", targetNamespace = "urn:vim25", className = "vim25.LoginByTokenRequestType")
    @ResponseWrapper(localName = "LoginByTokenResponse", targetNamespace = "urn:vim25", className = "vim25.LoginByTokenResponse")
    public UserSession loginByToken(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "locale", targetNamespace = "urn:vim25")
        String locale)
        throws InvalidLocaleFaultMsg, InvalidLoginFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param password
     * @param _this
     * @param userName
     * @param locale
     * @return
     *     returns vim25.UserSession
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidLoginFaultMsg
     * @throws InvalidLocaleFaultMsg
     */
    @WebMethod(operationName = "Login", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "Login", targetNamespace = "urn:vim25", className = "vim25.LoginRequestType")
    @ResponseWrapper(localName = "LoginResponse", targetNamespace = "urn:vim25", className = "vim25.LoginResponse")
    public UserSession login(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "userName", targetNamespace = "urn:vim25")
        String userName,
        @WebParam(name = "password", targetNamespace = "urn:vim25")
        String password,
        @WebParam(name = "locale", targetNamespace = "urn:vim25")
        String locale)
        throws InvalidLocaleFaultMsg, InvalidLoginFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param locale
     * @param base64Token
     * @return
     *     returns vim25.UserSession
     * @throws RuntimeFaultFaultMsg
     * @throws SSPIChallengeFaultMsg
     * @throws InvalidLoginFaultMsg
     * @throws InvalidLocaleFaultMsg
     */
    @WebMethod(operationName = "LoginBySSPI", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "LoginBySSPI", targetNamespace = "urn:vim25", className = "vim25.LoginBySSPIRequestType")
    @ResponseWrapper(localName = "LoginBySSPIResponse", targetNamespace = "urn:vim25", className = "vim25.LoginBySSPIResponse")
    public UserSession loginBySSPI(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "base64Token", targetNamespace = "urn:vim25")
        String base64Token,
        @WebParam(name = "locale", targetNamespace = "urn:vim25")
        String locale)
        throws InvalidLocaleFaultMsg, InvalidLoginFaultMsg, RuntimeFaultFaultMsg, SSPIChallengeFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "Logout", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "Logout", targetNamespace = "urn:vim25", className = "vim25.LogoutRequestType")
    @ResponseWrapper(localName = "LogoutResponse", targetNamespace = "urn:vim25", className = "vim25.LogoutResponse")
    public void logout(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param userName
     * @return
     *     returns vim25.SessionManagerLocalTicket
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidLoginFaultMsg
     */
    @WebMethod(operationName = "AcquireLocalTicket", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "AcquireLocalTicket", targetNamespace = "urn:vim25", className = "vim25.AcquireLocalTicketRequestType")
    @ResponseWrapper(localName = "AcquireLocalTicketResponse", targetNamespace = "urn:vim25", className = "vim25.AcquireLocalTicketResponse")
    public SessionManagerLocalTicket acquireLocalTicket(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "userName", targetNamespace = "urn:vim25")
        String userName)
        throws InvalidLoginFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @return
     *     returns vim25.SessionManagerGenericServiceTicket
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "AcquireGenericServiceTicket", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "AcquireGenericServiceTicket", targetNamespace = "urn:vim25", className = "vim25.AcquireGenericServiceTicketRequestType")
    @ResponseWrapper(localName = "AcquireGenericServiceTicketResponse", targetNamespace = "urn:vim25", className = "vim25.AcquireGenericServiceTicketResponse")
    public SessionManagerGenericServiceTicket acquireGenericServiceTicket(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        SessionManagerServiceRequestSpec spec)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param sessionId
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "TerminateSession", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "TerminateSession", targetNamespace = "urn:vim25", className = "vim25.TerminateSessionRequestType")
    @ResponseWrapper(localName = "TerminateSessionResponse", targetNamespace = "urn:vim25", className = "vim25.TerminateSessionResponse")
    public void terminateSession(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "sessionId", targetNamespace = "urn:vim25")
        List<String> sessionId)
        throws NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param locale
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidLocaleFaultMsg
     */
    @WebMethod(operationName = "SetLocale", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "SetLocale", targetNamespace = "urn:vim25", className = "vim25.SetLocaleRequestType")
    @ResponseWrapper(localName = "SetLocaleResponse", targetNamespace = "urn:vim25", className = "vim25.SetLocaleResponse")
    public void setLocale(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "locale", targetNamespace = "urn:vim25")
        String locale)
        throws InvalidLocaleFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param extensionKey
     * @param _this
     * @param locale
     * @return
     *     returns vim25.UserSession
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws InvalidLoginFaultMsg
     * @throws NoClientCertificateFaultMsg
     * @throws NoSubjectNameFaultMsg
     * @throws InvalidLocaleFaultMsg
     */
    @WebMethod(operationName = "LoginExtensionBySubjectName", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "LoginExtensionBySubjectName", targetNamespace = "urn:vim25", className = "vim25.LoginExtensionBySubjectNameRequestType")
    @ResponseWrapper(localName = "LoginExtensionBySubjectNameResponse", targetNamespace = "urn:vim25", className = "vim25.LoginExtensionBySubjectNameResponse")
    public UserSession loginExtensionBySubjectName(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "extensionKey", targetNamespace = "urn:vim25")
        String extensionKey,
        @WebParam(name = "locale", targetNamespace = "urn:vim25")
        String locale)
        throws InvalidLocaleFaultMsg, InvalidLoginFaultMsg, NoClientCertificateFaultMsg, NoSubjectNameFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param extensionKey
     * @param _this
     * @param locale
     * @return
     *     returns vim25.UserSession
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidLoginFaultMsg
     * @throws NoClientCertificateFaultMsg
     * @throws InvalidLocaleFaultMsg
     */
    @WebMethod(operationName = "LoginExtensionByCertificate", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "LoginExtensionByCertificate", targetNamespace = "urn:vim25", className = "vim25.LoginExtensionByCertificateRequestType")
    @ResponseWrapper(localName = "LoginExtensionByCertificateResponse", targetNamespace = "urn:vim25", className = "vim25.LoginExtensionByCertificateResponse")
    public UserSession loginExtensionByCertificate(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "extensionKey", targetNamespace = "urn:vim25")
        String extensionKey,
        @WebParam(name = "locale", targetNamespace = "urn:vim25")
        String locale)
        throws InvalidLocaleFaultMsg, InvalidLoginFaultMsg, NoClientCertificateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param userName
     * @param locale
     * @return
     *     returns vim25.UserSession
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidLoginFaultMsg
     * @throws InvalidLocaleFaultMsg
     */
    @WebMethod(operationName = "ImpersonateUser", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ImpersonateUser", targetNamespace = "urn:vim25", className = "vim25.ImpersonateUserRequestType")
    @ResponseWrapper(localName = "ImpersonateUserResponse", targetNamespace = "urn:vim25", className = "vim25.ImpersonateUserResponse")
    public UserSession impersonateUser(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "userName", targetNamespace = "urn:vim25")
        String userName,
        @WebParam(name = "locale", targetNamespace = "urn:vim25")
        String locale)
        throws InvalidLocaleFaultMsg, InvalidLoginFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param sessionID
     * @param _this
     * @param userName
     * @return
     *     returns boolean
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "SessionIsActive", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "SessionIsActive", targetNamespace = "urn:vim25", className = "vim25.SessionIsActiveRequestType")
    @ResponseWrapper(localName = "SessionIsActiveResponse", targetNamespace = "urn:vim25", className = "vim25.SessionIsActiveResponse")
    public boolean sessionIsActive(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "sessionID", targetNamespace = "urn:vim25")
        String sessionID,
        @WebParam(name = "userName", targetNamespace = "urn:vim25")
        String userName)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "AcquireCloneTicket", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "AcquireCloneTicket", targetNamespace = "urn:vim25", className = "vim25.AcquireCloneTicketRequestType")
    @ResponseWrapper(localName = "AcquireCloneTicketResponse", targetNamespace = "urn:vim25", className = "vim25.AcquireCloneTicketResponse")
    public String acquireCloneTicket(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param cloneTicket
     * @param _this
     * @return
     *     returns vim25.UserSession
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidLoginFaultMsg
     */
    @WebMethod(operationName = "CloneSession", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CloneSession", targetNamespace = "urn:vim25", className = "vim25.CloneSessionRequestType")
    @ResponseWrapper(localName = "CloneSessionResponse", targetNamespace = "urn:vim25", className = "vim25.CloneSessionResponse")
    public UserSession cloneSession(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "cloneTicket", targetNamespace = "urn:vim25")
        String cloneTicket)
        throws InvalidLoginFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param arguments
     * @param _this
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ExecuteSimpleCommand", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ExecuteSimpleCommand", targetNamespace = "urn:vim25", className = "vim25.ExecuteSimpleCommandRequestType")
    @ResponseWrapper(localName = "ExecuteSimpleCommandResponse", targetNamespace = "urn:vim25", className = "vim25.ExecuteSimpleCommandResponse")
    public String executeSimpleCommand(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "arguments", targetNamespace = "urn:vim25")
        List<String> arguments)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param datastore
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InaccessibleDatastoreFaultMsg
     * @throws IORMNotSupportedHostOnDatastoreFaultMsg
     */
    @WebMethod(operationName = "ConfigureDatastoreIORM_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ConfigureDatastoreIORM_Task", targetNamespace = "urn:vim25", className = "vim25.ConfigureDatastoreIORMRequestType")
    @ResponseWrapper(localName = "ConfigureDatastoreIORM_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ConfigureDatastoreIORMTaskResponse")
    public ManagedObjectReference configureDatastoreIORMTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "datastore", targetNamespace = "urn:vim25")
        ManagedObjectReference datastore,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        StorageIORMConfigSpec spec)
        throws IORMNotSupportedHostOnDatastoreFaultMsg, InaccessibleDatastoreFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @return
     *     returns vim25.StorageIORMConfigOption
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryIORMConfigOption", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryIORMConfigOption", targetNamespace = "urn:vim25", className = "vim25.QueryIORMConfigOptionRequestType")
    @ResponseWrapper(localName = "QueryIORMConfigOptionResponse", targetNamespace = "urn:vim25", className = "vim25.QueryIORMConfigOptionResponse")
    public StorageIORMConfigOption queryIORMConfigOption(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param datastore
     * @param _this
     * @return
     *     returns java.util.List<vim25.StoragePerformanceSummary>
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "QueryDatastorePerformanceSummary", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryDatastorePerformanceSummary", targetNamespace = "urn:vim25", className = "vim25.QueryDatastorePerformanceSummaryRequestType")
    @ResponseWrapper(localName = "QueryDatastorePerformanceSummaryResponse", targetNamespace = "urn:vim25", className = "vim25.QueryDatastorePerformanceSummaryResponse")
    public List<StoragePerformanceSummary> queryDatastorePerformanceSummary(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "datastore", targetNamespace = "urn:vim25")
        ManagedObjectReference datastore)
        throws NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param pod
     * @param _this
     * @param key
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ApplyStorageDrsRecommendationToPod_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ApplyStorageDrsRecommendationToPod_Task", targetNamespace = "urn:vim25", className = "vim25.ApplyStorageDrsRecommendationToPodRequestType")
    @ResponseWrapper(localName = "ApplyStorageDrsRecommendationToPod_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ApplyStorageDrsRecommendationToPodTaskResponse")
    public ManagedObjectReference applyStorageDrsRecommendationToPodTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "pod", targetNamespace = "urn:vim25")
        ManagedObjectReference pod,
        @WebParam(name = "key", targetNamespace = "urn:vim25")
        String key)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param key
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ApplyStorageDrsRecommendation_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ApplyStorageDrsRecommendation_Task", targetNamespace = "urn:vim25", className = "vim25.ApplyStorageDrsRecommendationRequestType")
    @ResponseWrapper(localName = "ApplyStorageDrsRecommendation_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ApplyStorageDrsRecommendationTaskResponse")
    public ManagedObjectReference applyStorageDrsRecommendationTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "key", targetNamespace = "urn:vim25")
        List<String> key)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param key
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "CancelStorageDrsRecommendation", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "CancelStorageDrsRecommendation", targetNamespace = "urn:vim25", className = "vim25.CancelStorageDrsRecommendationRequestType")
    @ResponseWrapper(localName = "CancelStorageDrsRecommendationResponse", targetNamespace = "urn:vim25", className = "vim25.CancelStorageDrsRecommendationResponse")
    public void cancelStorageDrsRecommendation(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "key", targetNamespace = "urn:vim25")
        List<String> key)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param pod
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RefreshStorageDrsRecommendation", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RefreshStorageDrsRecommendation", targetNamespace = "urn:vim25", className = "vim25.RefreshStorageDrsRecommendationRequestType")
    @ResponseWrapper(localName = "RefreshStorageDrsRecommendationResponse", targetNamespace = "urn:vim25", className = "vim25.RefreshStorageDrsRecommendationResponse")
    public void refreshStorageDrsRecommendation(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "pod", targetNamespace = "urn:vim25")
        ManagedObjectReference pod)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param modify
     * @param pod
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ConfigureStorageDrsForPod_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ConfigureStorageDrsForPod_Task", targetNamespace = "urn:vim25", className = "vim25.ConfigureStorageDrsForPodRequestType")
    @ResponseWrapper(localName = "ConfigureStorageDrsForPod_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ConfigureStorageDrsForPodTaskResponse")
    public ManagedObjectReference configureStorageDrsForPodTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "pod", targetNamespace = "urn:vim25")
        ManagedObjectReference pod,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        StorageDrsConfigSpec spec,
        @WebParam(name = "modify", targetNamespace = "urn:vim25")
        boolean modify)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param storageSpec
     * @return
     *     returns vim25.StoragePlacementResult
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RecommendDatastores", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RecommendDatastores", targetNamespace = "urn:vim25", className = "vim25.RecommendDatastoresRequestType")
    @ResponseWrapper(localName = "RecommendDatastoresResponse", targetNamespace = "urn:vim25", className = "vim25.RecommendDatastoresResponse")
    public StoragePlacementResult recommendDatastores(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "storageSpec", targetNamespace = "urn:vim25")
        StoragePlacementSpec storageSpec)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "CancelTask", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "CancelTask", targetNamespace = "urn:vim25", className = "vim25.CancelTaskRequestType")
    @ResponseWrapper(localName = "CancelTaskResponse", targetNamespace = "urn:vim25", className = "vim25.CancelTaskResponse")
    public void cancelTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param percentDone
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws OutOfBoundsFaultMsg
     */
    @WebMethod(operationName = "UpdateProgress", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateProgress", targetNamespace = "urn:vim25", className = "vim25.UpdateProgressRequestType")
    @ResponseWrapper(localName = "UpdateProgressResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateProgressResponse")
    public void updateProgress(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "percentDone", targetNamespace = "urn:vim25")
        int percentDone)
        throws InvalidStateFaultMsg, OutOfBoundsFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param result
     * @param fault
     * @param state
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "SetTaskState", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "SetTaskState", targetNamespace = "urn:vim25", className = "vim25.SetTaskStateRequestType")
    @ResponseWrapper(localName = "SetTaskStateResponse", targetNamespace = "urn:vim25", className = "vim25.SetTaskStateResponse")
    public void setTaskState(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "state", targetNamespace = "urn:vim25")
        TaskInfoState state,
        @WebParam(name = "result", targetNamespace = "urn:vim25")
        Object result,
        @WebParam(name = "fault", targetNamespace = "urn:vim25")
        LocalizedMethodFault fault)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param description
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "SetTaskDescription", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "SetTaskDescription", targetNamespace = "urn:vim25", className = "vim25.SetTaskDescriptionRequestType")
    @ResponseWrapper(localName = "SetTaskDescriptionResponse", targetNamespace = "urn:vim25", className = "vim25.SetTaskDescriptionResponse")
    public void setTaskDescription(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "description", targetNamespace = "urn:vim25")
        LocalizableMessage description)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param maxCount
     * @return
     *     returns java.util.List<vim25.TaskInfo>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ReadNextTasks", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ReadNextTasks", targetNamespace = "urn:vim25", className = "vim25.ReadNextTasksRequestType")
    @ResponseWrapper(localName = "ReadNextTasksResponse", targetNamespace = "urn:vim25", className = "vim25.ReadNextTasksResponse")
    public List<TaskInfo> readNextTasks(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "maxCount", targetNamespace = "urn:vim25")
        int maxCount)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param maxCount
     * @return
     *     returns java.util.List<vim25.TaskInfo>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ReadPreviousTasks", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ReadPreviousTasks", targetNamespace = "urn:vim25", className = "vim25.ReadPreviousTasksRequestType")
    @ResponseWrapper(localName = "ReadPreviousTasksResponse", targetNamespace = "urn:vim25", className = "vim25.ReadPreviousTasksResponse")
    public List<TaskInfo> readPreviousTasks(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "maxCount", targetNamespace = "urn:vim25")
        int maxCount)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param filter
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "CreateCollectorForTasks", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateCollectorForTasks", targetNamespace = "urn:vim25", className = "vim25.CreateCollectorForTasksRequestType")
    @ResponseWrapper(localName = "CreateCollectorForTasksResponse", targetNamespace = "urn:vim25", className = "vim25.CreateCollectorForTasksResponse")
    public ManagedObjectReference createCollectorForTasks(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "filter", targetNamespace = "urn:vim25")
        TaskFilterSpec filter)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param taskTypeId
     * @param cancelable
     * @param obj
     * @param parentTaskKey
     * @param _this
     * @param initiatedBy
     * @param activationId
     * @return
     *     returns vim25.TaskInfo
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "CreateTask", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateTask", targetNamespace = "urn:vim25", className = "vim25.CreateTaskRequestType")
    @ResponseWrapper(localName = "CreateTaskResponse", targetNamespace = "urn:vim25", className = "vim25.CreateTaskResponse")
    public TaskInfo createTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "obj", targetNamespace = "urn:vim25")
        ManagedObjectReference obj,
        @WebParam(name = "taskTypeId", targetNamespace = "urn:vim25")
        String taskTypeId,
        @WebParam(name = "initiatedBy", targetNamespace = "urn:vim25")
        String initiatedBy,
        @WebParam(name = "cancelable", targetNamespace = "urn:vim25")
        boolean cancelable,
        @WebParam(name = "parentTaskKey", targetNamespace = "urn:vim25")
        String parentTaskKey,
        @WebParam(name = "activationId", targetNamespace = "urn:vim25")
        String activationId)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param belongsToUser
     * @param exactMatch
     * @param searchStr
     * @param belongsToGroup
     * @param domain
     * @param findUsers
     * @param findGroups
     * @param _this
     * @return
     *     returns java.util.List<vim25.UserSearchResult>
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "RetrieveUserGroups", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RetrieveUserGroups", targetNamespace = "urn:vim25", className = "vim25.RetrieveUserGroupsRequestType")
    @ResponseWrapper(localName = "RetrieveUserGroupsResponse", targetNamespace = "urn:vim25", className = "vim25.RetrieveUserGroupsResponse")
    public List<UserSearchResult> retrieveUserGroups(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "domain", targetNamespace = "urn:vim25")
        String domain,
        @WebParam(name = "searchStr", targetNamespace = "urn:vim25")
        String searchStr,
        @WebParam(name = "belongsToGroup", targetNamespace = "urn:vim25")
        String belongsToGroup,
        @WebParam(name = "belongsToUser", targetNamespace = "urn:vim25")
        String belongsToUser,
        @WebParam(name = "exactMatch", targetNamespace = "urn:vim25")
        boolean exactMatch,
        @WebParam(name = "findUsers", targetNamespace = "urn:vim25")
        boolean findUsers,
        @WebParam(name = "findGroups", targetNamespace = "urn:vim25")
        boolean findGroups)
        throws NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "CreateVRP", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateVRP", targetNamespace = "urn:vim25", className = "vim25.CreateVRPRequestType")
    @ResponseWrapper(localName = "CreateVRPResponse", targetNamespace = "urn:vim25", className = "vim25.CreateVRPResponse")
    public String createVRP(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        VirtualResourcePoolSpec spec)
        throws InsufficientResourcesFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "UpdateVRP", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateVRP", targetNamespace = "urn:vim25", className = "vim25.UpdateVRPRequestType")
    @ResponseWrapper(localName = "UpdateVRPResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateVRPResponse")
    public void updateVRP(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        VRPEditSpec spec)
        throws InsufficientResourcesFaultFaultMsg, InvalidStateFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vrpId
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "DeleteVRP", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DeleteVRP", targetNamespace = "urn:vim25", className = "vim25.DeleteVRPRequestType")
    @ResponseWrapper(localName = "DeleteVRPResponse", targetNamespace = "urn:vim25", className = "vim25.DeleteVRPResponse")
    public void deleteVRP(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vrpId", targetNamespace = "urn:vim25")
        String vrpId)
        throws InvalidStateFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param cluster
     * @param vm
     * @param vrpId
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "DeployVM", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DeployVM", targetNamespace = "urn:vim25", className = "vim25.DeployVMRequestType")
    @ResponseWrapper(localName = "DeployVMResponse", targetNamespace = "urn:vim25", className = "vim25.DeployVMResponse")
    public void deployVM(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vrpId", targetNamespace = "urn:vim25")
        String vrpId,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "cluster", targetNamespace = "urn:vim25")
        ManagedObjectReference cluster)
        throws InsufficientResourcesFaultFaultMsg, InvalidStateFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param cluster
     * @param vm
     * @param vrpId
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "UndeployVM", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UndeployVM", targetNamespace = "urn:vim25", className = "vim25.UndeployVMRequestType")
    @ResponseWrapper(localName = "UndeployVMResponse", targetNamespace = "urn:vim25", className = "vim25.UndeployVMResponse")
    public void undeployVM(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vrpId", targetNamespace = "urn:vim25")
        String vrpId,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "cluster", targetNamespace = "urn:vim25")
        ManagedObjectReference cluster)
        throws InvalidStateFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param cluster
     * @param _this
     * @param status
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "SetManagedByVDC", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "SetManagedByVDC", targetNamespace = "urn:vim25", className = "vim25.SetManagedByVDCRequestType")
    @ResponseWrapper(localName = "SetManagedByVDCResponse", targetNamespace = "urn:vim25", className = "vim25.SetManagedByVDCResponse")
    public void setManagedByVDC(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "cluster", targetNamespace = "urn:vim25")
        ManagedObjectReference cluster,
        @WebParam(name = "status", targetNamespace = "urn:vim25")
        boolean status)
        throws InvalidStateFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.util.List<java.lang.String>
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "GetAllVRPIds", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "GetAllVRPIds", targetNamespace = "urn:vim25", className = "vim25.GetAllVRPIdsRequestType")
    @ResponseWrapper(localName = "GetAllVRPIdsResponse", targetNamespace = "urn:vim25", className = "vim25.GetAllVRPIdsResponse")
    public List<String> getAllVRPIds(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param resourcePool
     * @return
     *     returns vim25.ResourceConfigSpec
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "GetRPSettings", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "GetRPSettings", targetNamespace = "urn:vim25", className = "vim25.GetRPSettingsRequestType")
    @ResponseWrapper(localName = "GetRPSettingsResponse", targetNamespace = "urn:vim25", className = "vim25.GetRPSettingsResponse")
    public ResourceConfigSpec getRPSettings(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "resourcePool", targetNamespace = "urn:vim25")
        ManagedObjectReference resourcePool)
        throws NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vrpId
     * @param _this
     * @return
     *     returns vim25.VirtualResourcePoolSpec
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "GetVRPSettings", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "GetVRPSettings", targetNamespace = "urn:vim25", className = "vim25.GetVRPSettingsRequestType")
    @ResponseWrapper(localName = "GetVRPSettingsResponse", targetNamespace = "urn:vim25", className = "vim25.GetVRPSettingsResponse")
    public VirtualResourcePoolSpec getVRPSettings(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vrpId", targetNamespace = "urn:vim25")
        String vrpId)
        throws InvalidStateFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vrpId
     * @param _this
     * @return
     *     returns vim25.VirtualResourcePoolUsage
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "GetVRPUsage", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "GetVRPUsage", targetNamespace = "urn:vim25", className = "vim25.GetVRPUsageRequestType")
    @ResponseWrapper(localName = "GetVRPUsageResponse", targetNamespace = "urn:vim25", className = "vim25.GetVRPUsageResponse")
    public VirtualResourcePoolUsage getVRPUsage(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vrpId", targetNamespace = "urn:vim25")
        String vrpId)
        throws InvalidStateFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vm
     * @param _this
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "GetVRPofVM", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "GetVRPofVM", targetNamespace = "urn:vim25", className = "vim25.GetVRPofVMRequestType")
    @ResponseWrapper(localName = "GetVRPofVMResponse", targetNamespace = "urn:vim25", className = "vim25.GetVRPofVMResponse")
    public String getVRPofVM(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm)
        throws InvalidStateFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param hub
     * @param vrpId
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "GetChildRPforHub", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "GetChildRPforHub", targetNamespace = "urn:vim25", className = "vim25.GetChildRPforHubRequestType")
    @ResponseWrapper(localName = "GetChildRPforHubResponse", targetNamespace = "urn:vim25", className = "vim25.GetChildRPforHubResponse")
    public ManagedObjectReference getChildRPforHub(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vrpId", targetNamespace = "urn:vim25")
        String vrpId,
        @WebParam(name = "hub", targetNamespace = "urn:vim25")
        ManagedObjectReference hub)
        throws InvalidStateFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws ConcurrentAccessFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "UpdateVAppConfig", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateVAppConfig", targetNamespace = "urn:vim25", className = "vim25.UpdateVAppConfigRequestType")
    @ResponseWrapper(localName = "UpdateVAppConfigResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateVAppConfigResponse")
    public void updateVAppConfig(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        VAppConfigSpec spec)
        throws ConcurrentAccessFaultMsg, DuplicateNameFaultMsg, FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidDatastoreFaultMsg, InvalidNameFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param removeSet
     * @param addChangeSet
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws ConcurrentAccessFaultMsg
     */
    @WebMethod(operationName = "UpdateLinkedChildren", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateLinkedChildren", targetNamespace = "urn:vim25", className = "vim25.UpdateLinkedChildrenRequestType")
    @ResponseWrapper(localName = "UpdateLinkedChildrenResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateLinkedChildrenResponse")
    public void updateLinkedChildren(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "addChangeSet", targetNamespace = "urn:vim25")
        List<VirtualAppLinkInfo> addChangeSet,
        @WebParam(name = "removeSet", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> removeSet)
        throws ConcurrentAccessFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param _this
     * @param spec
     * @param target
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws MigrationFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "CloneVApp_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CloneVApp_Task", targetNamespace = "urn:vim25", className = "vim25.CloneVAppRequestType")
    @ResponseWrapper(localName = "CloneVApp_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CloneVAppTaskResponse")
    public ManagedObjectReference cloneVAppTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "target", targetNamespace = "urn:vim25")
        ManagedObjectReference target,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        VAppCloneSpec spec)
        throws FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidDatastoreFaultMsg, InvalidStateFaultMsg, MigrationFaultFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws InvalidPowerStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "ExportVApp", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ExportVApp", targetNamespace = "urn:vim25", className = "vim25.ExportVAppRequestType")
    @ResponseWrapper(localName = "ExportVAppResponse", targetNamespace = "urn:vim25", className = "vim25.ExportVAppResponse")
    public ManagedObjectReference exportVApp(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws FileFaultFaultMsg, InvalidPowerStateFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws VAppConfigFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "PowerOnVApp_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "PowerOnVApp_Task", targetNamespace = "urn:vim25", className = "vim25.PowerOnVAppRequestType")
    @ResponseWrapper(localName = "PowerOnVApp_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.PowerOnVAppTaskResponse")
    public ManagedObjectReference powerOnVAppTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VAppConfigFaultFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param force
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws VAppConfigFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "PowerOffVApp_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "PowerOffVApp_Task", targetNamespace = "urn:vim25", className = "vim25.PowerOffVAppRequestType")
    @ResponseWrapper(localName = "PowerOffVApp_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.PowerOffVAppTaskResponse")
    public ManagedObjectReference powerOffVAppTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "force", targetNamespace = "urn:vim25")
        boolean force)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VAppConfigFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws VAppConfigFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "SuspendVApp_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "SuspendVApp_Task", targetNamespace = "urn:vim25", className = "vim25.SuspendVAppRequestType")
    @ResponseWrapper(localName = "SuspendVApp_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.SuspendVAppTaskResponse")
    public ManagedObjectReference suspendVAppTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VAppConfigFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws ConcurrentAccessFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "unregisterVApp_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "unregisterVApp_Task", targetNamespace = "urn:vim25", className = "vim25.UnregisterVAppRequestType")
    @ResponseWrapper(localName = "unregisterVApp_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.UnregisterVAppTaskResponse")
    public ManagedObjectReference unregisterVAppTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws ConcurrentAccessFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param datacenter
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "CreateVirtualDisk_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateVirtualDisk_Task", targetNamespace = "urn:vim25", className = "vim25.CreateVirtualDiskRequestType")
    @ResponseWrapper(localName = "CreateVirtualDisk_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CreateVirtualDiskTaskResponse")
    public ManagedObjectReference createVirtualDiskTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "datacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference datacenter,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        VirtualDiskSpec spec)
        throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param datacenter
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "DeleteVirtualDisk_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "DeleteVirtualDisk_Task", targetNamespace = "urn:vim25", className = "vim25.DeleteVirtualDiskRequestType")
    @ResponseWrapper(localName = "DeleteVirtualDisk_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.DeleteVirtualDiskTaskResponse")
    public ManagedObjectReference deleteVirtualDiskTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "datacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference datacenter)
        throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param destName
     * @param profile
     * @param force
     * @param sourceName
     * @param destDatacenter
     * @param _this
     * @param sourceDatacenter
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "MoveVirtualDisk_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "MoveVirtualDisk_Task", targetNamespace = "urn:vim25", className = "vim25.MoveVirtualDiskRequestType")
    @ResponseWrapper(localName = "MoveVirtualDisk_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.MoveVirtualDiskTaskResponse")
    public ManagedObjectReference moveVirtualDiskTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "sourceName", targetNamespace = "urn:vim25")
        String sourceName,
        @WebParam(name = "sourceDatacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference sourceDatacenter,
        @WebParam(name = "destName", targetNamespace = "urn:vim25")
        String destName,
        @WebParam(name = "destDatacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference destDatacenter,
        @WebParam(name = "force", targetNamespace = "urn:vim25")
        Boolean force,
        @WebParam(name = "profile", targetNamespace = "urn:vim25")
        List<VirtualMachineProfileSpec> profile)
        throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param destName
     * @param force
     * @param sourceName
     * @param destDatacenter
     * @param _this
     * @param sourceDatacenter
     * @param destSpec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDiskFormatFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "CopyVirtualDisk_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CopyVirtualDisk_Task", targetNamespace = "urn:vim25", className = "vim25.CopyVirtualDiskRequestType")
    @ResponseWrapper(localName = "CopyVirtualDisk_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CopyVirtualDiskTaskResponse")
    public ManagedObjectReference copyVirtualDiskTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "sourceName", targetNamespace = "urn:vim25")
        String sourceName,
        @WebParam(name = "sourceDatacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference sourceDatacenter,
        @WebParam(name = "destName", targetNamespace = "urn:vim25")
        String destName,
        @WebParam(name = "destDatacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference destDatacenter,
        @WebParam(name = "destSpec", targetNamespace = "urn:vim25")
        VirtualDiskSpec destSpec,
        @WebParam(name = "force", targetNamespace = "urn:vim25")
        Boolean force)
        throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, InvalidDiskFormatFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param newCapacityKb
     * @param name
     * @param datacenter
     * @param _this
     * @param eagerZero
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "ExtendVirtualDisk_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ExtendVirtualDisk_Task", targetNamespace = "urn:vim25", className = "vim25.ExtendVirtualDiskRequestType")
    @ResponseWrapper(localName = "ExtendVirtualDisk_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ExtendVirtualDiskTaskResponse")
    public ManagedObjectReference extendVirtualDiskTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "datacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference datacenter,
        @WebParam(name = "newCapacityKb", targetNamespace = "urn:vim25")
        long newCapacityKb,
        @WebParam(name = "eagerZero", targetNamespace = "urn:vim25")
        Boolean eagerZero)
        throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param datacenter
     * @param _this
     * @return
     *     returns int
     * @throws RuntimeFaultFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "QueryVirtualDiskFragmentation", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryVirtualDiskFragmentation", targetNamespace = "urn:vim25", className = "vim25.QueryVirtualDiskFragmentationRequestType")
    @ResponseWrapper(localName = "QueryVirtualDiskFragmentationResponse", targetNamespace = "urn:vim25", className = "vim25.QueryVirtualDiskFragmentationResponse")
    public int queryVirtualDiskFragmentation(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "datacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference datacenter)
        throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param datacenter
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "DefragmentVirtualDisk_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "DefragmentVirtualDisk_Task", targetNamespace = "urn:vim25", className = "vim25.DefragmentVirtualDiskRequestType")
    @ResponseWrapper(localName = "DefragmentVirtualDisk_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.DefragmentVirtualDiskTaskResponse")
    public ManagedObjectReference defragmentVirtualDiskTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "datacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference datacenter)
        throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param datacenter
     * @param copy
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "ShrinkVirtualDisk_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ShrinkVirtualDisk_Task", targetNamespace = "urn:vim25", className = "vim25.ShrinkVirtualDiskRequestType")
    @ResponseWrapper(localName = "ShrinkVirtualDisk_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ShrinkVirtualDiskTaskResponse")
    public ManagedObjectReference shrinkVirtualDiskTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "datacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference datacenter,
        @WebParam(name = "copy", targetNamespace = "urn:vim25")
        Boolean copy)
        throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param datacenter
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "InflateVirtualDisk_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "InflateVirtualDisk_Task", targetNamespace = "urn:vim25", className = "vim25.InflateVirtualDiskRequestType")
    @ResponseWrapper(localName = "InflateVirtualDisk_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.InflateVirtualDiskTaskResponse")
    public ManagedObjectReference inflateVirtualDiskTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "datacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference datacenter)
        throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param datacenter
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "EagerZeroVirtualDisk_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "EagerZeroVirtualDisk_Task", targetNamespace = "urn:vim25", className = "vim25.EagerZeroVirtualDiskRequestType")
    @ResponseWrapper(localName = "EagerZeroVirtualDisk_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.EagerZeroVirtualDiskTaskResponse")
    public ManagedObjectReference eagerZeroVirtualDiskTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "datacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference datacenter)
        throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param datacenter
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "ZeroFillVirtualDisk_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ZeroFillVirtualDisk_Task", targetNamespace = "urn:vim25", className = "vim25.ZeroFillVirtualDiskRequestType")
    @ResponseWrapper(localName = "ZeroFillVirtualDisk_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ZeroFillVirtualDiskTaskResponse")
    public ManagedObjectReference zeroFillVirtualDiskTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "datacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference datacenter)
        throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param datacenter
     * @param _this
     * @param uuid
     * @throws RuntimeFaultFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "SetVirtualDiskUuid", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "SetVirtualDiskUuid", targetNamespace = "urn:vim25", className = "vim25.SetVirtualDiskUuidRequestType")
    @ResponseWrapper(localName = "SetVirtualDiskUuidResponse", targetNamespace = "urn:vim25", className = "vim25.SetVirtualDiskUuidResponse")
    public void setVirtualDiskUuid(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "datacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference datacenter,
        @WebParam(name = "uuid", targetNamespace = "urn:vim25")
        String uuid)
        throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param datacenter
     * @param _this
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "QueryVirtualDiskUuid", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryVirtualDiskUuid", targetNamespace = "urn:vim25", className = "vim25.QueryVirtualDiskUuidRequestType")
    @ResponseWrapper(localName = "QueryVirtualDiskUuidResponse", targetNamespace = "urn:vim25", className = "vim25.QueryVirtualDiskUuidResponse")
    public String queryVirtualDiskUuid(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "datacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference datacenter)
        throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param datacenter
     * @param _this
     * @return
     *     returns vim25.HostDiskDimensionsChs
     * @throws RuntimeFaultFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "QueryVirtualDiskGeometry", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryVirtualDiskGeometry", targetNamespace = "urn:vim25", className = "vim25.QueryVirtualDiskGeometryRequestType")
    @ResponseWrapper(localName = "QueryVirtualDiskGeometryResponse", targetNamespace = "urn:vim25", className = "vim25.QueryVirtualDiskGeometryResponse")
    public HostDiskDimensionsChs queryVirtualDiskGeometry(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "datacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference datacenter)
        throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vvolId
     * @param datacenter
     * @param _this
     * @param vdisk
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "ImportUnmanagedSnapshot", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ImportUnmanagedSnapshot", targetNamespace = "urn:vim25", className = "vim25.ImportUnmanagedSnapshotRequestType")
    @ResponseWrapper(localName = "ImportUnmanagedSnapshotResponse", targetNamespace = "urn:vim25", className = "vim25.ImportUnmanagedSnapshotResponse")
    public void importUnmanagedSnapshot(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vdisk", targetNamespace = "urn:vim25")
        String vdisk,
        @WebParam(name = "datacenter", targetNamespace = "urn:vim25")
        ManagedObjectReference datacenter,
        @WebParam(name = "vvolId", targetNamespace = "urn:vim25")
        String vvolId)
        throws InvalidDatastoreFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RefreshStorageInfo", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RefreshStorageInfo", targetNamespace = "urn:vim25", className = "vim25.RefreshStorageInfoRequestType")
    @ResponseWrapper(localName = "RefreshStorageInfoResponse", targetNamespace = "urn:vim25", className = "vim25.RefreshStorageInfoResponse")
    public void refreshStorageInfo(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param memory
     * @param name
     * @param description
     * @param quiesce
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     * @throws SnapshotFaultFaultMsg
     * @throws InvalidNameFaultMsg
     */
    @WebMethod(operationName = "CreateSnapshot_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateSnapshot_Task", targetNamespace = "urn:vim25", className = "vim25.CreateSnapshotRequestType")
    @ResponseWrapper(localName = "CreateSnapshot_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CreateSnapshotTaskResponse")
    public ManagedObjectReference createSnapshotTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "description", targetNamespace = "urn:vim25")
        String description,
        @WebParam(name = "memory", targetNamespace = "urn:vim25")
        boolean memory,
        @WebParam(name = "quiesce", targetNamespace = "urn:vim25")
        boolean quiesce)
        throws FileFaultFaultMsg, InvalidNameFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, SnapshotFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param suppressPowerOn
     * @param host
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NotFoundFaultMsg
     * @throws SnapshotFaultFaultMsg
     */
    @WebMethod(operationName = "RevertToCurrentSnapshot_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RevertToCurrentSnapshot_Task", targetNamespace = "urn:vim25", className = "vim25.RevertToCurrentSnapshotRequestType")
    @ResponseWrapper(localName = "RevertToCurrentSnapshot_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.RevertToCurrentSnapshotTaskResponse")
    public ManagedObjectReference revertToCurrentSnapshotTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host,
        @WebParam(name = "suppressPowerOn", targetNamespace = "urn:vim25")
        Boolean suppressPowerOn)
        throws InsufficientResourcesFaultFaultMsg, InvalidStateFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg, SnapshotFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param consolidate
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws SnapshotFaultFaultMsg
     */
    @WebMethod(operationName = "RemoveAllSnapshots_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RemoveAllSnapshots_Task", targetNamespace = "urn:vim25", className = "vim25.RemoveAllSnapshotsRequestType")
    @ResponseWrapper(localName = "RemoveAllSnapshots_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveAllSnapshotsTaskResponse")
    public ManagedObjectReference removeAllSnapshotsTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "consolidate", targetNamespace = "urn:vim25")
        Boolean consolidate)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, SnapshotFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "ConsolidateVMDisks_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ConsolidateVMDisks_Task", targetNamespace = "urn:vim25", className = "vim25.ConsolidateVMDisksRequestType")
    @ResponseWrapper(localName = "ConsolidateVMDisks_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ConsolidateVMDisksTaskResponse")
    public ManagedObjectReference consolidateVMDisksTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws FileFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "EstimateStorageForConsolidateSnapshots_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "EstimateStorageForConsolidateSnapshots_Task", targetNamespace = "urn:vim25", className = "vim25.EstimateStorageForConsolidateSnapshotsRequestType")
    @ResponseWrapper(localName = "EstimateStorageForConsolidateSnapshots_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.EstimateStorageForConsolidateSnapshotsTaskResponse")
    public ManagedObjectReference estimateStorageForConsolidateSnapshotsTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws FileFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws ConcurrentAccessFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "ReconfigVM_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ReconfigVM_Task", targetNamespace = "urn:vim25", className = "vim25.ReconfigVMRequestType")
    @ResponseWrapper(localName = "ReconfigVM_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ReconfigVMTaskResponse")
    public ManagedObjectReference reconfigVMTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        VirtualMachineConfigSpec spec)
        throws ConcurrentAccessFaultMsg, DuplicateNameFaultMsg, FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidDatastoreFaultMsg, InvalidNameFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param version
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws AlreadyUpgradedFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws NoDiskFoundFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "UpgradeVM_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "UpgradeVM_Task", targetNamespace = "urn:vim25", className = "vim25.UpgradeVMRequestType")
    @ResponseWrapper(localName = "UpgradeVM_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.UpgradeVMTaskResponse")
    public ManagedObjectReference upgradeVMTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "version", targetNamespace = "urn:vim25")
        String version)
        throws AlreadyUpgradedFaultMsg, InvalidStateFaultMsg, NoDiskFoundFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "ExtractOvfEnvironment", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ExtractOvfEnvironment", targetNamespace = "urn:vim25", className = "vim25.ExtractOvfEnvironmentRequestType")
    @ResponseWrapper(localName = "ExtractOvfEnvironmentResponse", targetNamespace = "urn:vim25", className = "vim25.ExtractOvfEnvironmentResponse")
    public String extractOvfEnvironment(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "PowerOnVM_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "PowerOnVM_Task", targetNamespace = "urn:vim25", className = "vim25.PowerOnVMRequestType")
    @ResponseWrapper(localName = "PowerOnVM_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.PowerOnVMTaskResponse")
    public ManagedObjectReference powerOnVMTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "PowerOffVM_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "PowerOffVM_Task", targetNamespace = "urn:vim25", className = "vim25.PowerOffVMRequestType")
    @ResponseWrapper(localName = "PowerOffVM_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.PowerOffVMTaskResponse")
    public ManagedObjectReference powerOffVMTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "SuspendVM_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "SuspendVM_Task", targetNamespace = "urn:vim25", className = "vim25.SuspendVMRequestType")
    @ResponseWrapper(localName = "SuspendVM_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.SuspendVMTaskResponse")
    public ManagedObjectReference suspendVMTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "ResetVM_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ResetVM_Task", targetNamespace = "urn:vim25", className = "vim25.ResetVMRequestType")
    @ResponseWrapper(localName = "ResetVM_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ResetVMTaskResponse")
    public ManagedObjectReference resetVMTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws ToolsUnavailableFaultMsg
     */
    @WebMethod(operationName = "ShutdownGuest", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ShutdownGuest", targetNamespace = "urn:vim25", className = "vim25.ShutdownGuestRequestType")
    @ResponseWrapper(localName = "ShutdownGuestResponse", targetNamespace = "urn:vim25", className = "vim25.ShutdownGuestResponse")
    public void shutdownGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, ToolsUnavailableFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws ToolsUnavailableFaultMsg
     */
    @WebMethod(operationName = "RebootGuest", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RebootGuest", targetNamespace = "urn:vim25", className = "vim25.RebootGuestRequestType")
    @ResponseWrapper(localName = "RebootGuestResponse", targetNamespace = "urn:vim25", className = "vim25.RebootGuestResponse")
    public void rebootGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, ToolsUnavailableFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws ToolsUnavailableFaultMsg
     */
    @WebMethod(operationName = "StandbyGuest", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "StandbyGuest", targetNamespace = "urn:vim25", className = "vim25.StandbyGuestRequestType")
    @ResponseWrapper(localName = "StandbyGuestResponse", targetNamespace = "urn:vim25", className = "vim25.StandbyGuestResponse")
    public void standbyGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, ToolsUnavailableFaultMsg
    ;

    /**
     * 
     * @param questionId
     * @param _this
     * @param answerChoice
     * @throws RuntimeFaultFaultMsg
     * @throws ConcurrentAccessFaultMsg
     */
    @WebMethod(operationName = "AnswerVM", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "AnswerVM", targetNamespace = "urn:vim25", className = "vim25.AnswerVMRequestType")
    @ResponseWrapper(localName = "AnswerVMResponse", targetNamespace = "urn:vim25", className = "vim25.AnswerVMResponse")
    public void answerVM(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "questionId", targetNamespace = "urn:vim25")
        String questionId,
        @WebParam(name = "answerChoice", targetNamespace = "urn:vim25")
        String answerChoice)
        throws ConcurrentAccessFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws CustomizationFaultFaultMsg
     */
    @WebMethod(operationName = "CustomizeVM_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CustomizeVM_Task", targetNamespace = "urn:vim25", className = "vim25.CustomizeVMRequestType")
    @ResponseWrapper(localName = "CustomizeVM_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CustomizeVMTaskResponse")
    public ManagedObjectReference customizeVMTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        CustomizationSpec spec)
        throws CustomizationFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @throws RuntimeFaultFaultMsg
     * @throws CustomizationFaultFaultMsg
     */
    @WebMethod(operationName = "CheckCustomizationSpec", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "CheckCustomizationSpec", targetNamespace = "urn:vim25", className = "vim25.CheckCustomizationSpecRequestType")
    @ResponseWrapper(localName = "CheckCustomizationSpecResponse", targetNamespace = "urn:vim25", className = "vim25.CheckCustomizationSpecResponse")
    public void checkCustomizationSpec(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        CustomizationSpec spec)
        throws CustomizationFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param pool
     * @param host
     * @param state
     * @param _this
     * @param priority
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws MigrationFaultFaultMsg
     * @throws FileFaultFaultMsg
     * @throws TimedoutFaultMsg
     */
    @WebMethod(operationName = "MigrateVM_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "MigrateVM_Task", targetNamespace = "urn:vim25", className = "vim25.MigrateVMRequestType")
    @ResponseWrapper(localName = "MigrateVM_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.MigrateVMTaskResponse")
    public ManagedObjectReference migrateVMTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "pool", targetNamespace = "urn:vim25")
        ManagedObjectReference pool,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host,
        @WebParam(name = "priority", targetNamespace = "urn:vim25")
        VirtualMachineMovePriority priority,
        @WebParam(name = "state", targetNamespace = "urn:vim25")
        VirtualMachinePowerState state)
        throws FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidStateFaultMsg, MigrationFaultFaultMsg, RuntimeFaultFaultMsg, TimedoutFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param priority
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws MigrationFaultFaultMsg
     * @throws FileFaultFaultMsg
     * @throws TimedoutFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "RelocateVM_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RelocateVM_Task", targetNamespace = "urn:vim25", className = "vim25.RelocateVMRequestType")
    @ResponseWrapper(localName = "RelocateVM_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.RelocateVMTaskResponse")
    public ManagedObjectReference relocateVMTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        VirtualMachineRelocateSpec spec,
        @WebParam(name = "priority", targetNamespace = "urn:vim25")
        VirtualMachineMovePriority priority)
        throws FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidDatastoreFaultMsg, InvalidStateFaultMsg, MigrationFaultFaultMsg, RuntimeFaultFaultMsg, TimedoutFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param folder
     * @param name
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws CustomizationFaultFaultMsg
     * @throws MigrationFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "CloneVM_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CloneVM_Task", targetNamespace = "urn:vim25", className = "vim25.CloneVMRequestType")
    @ResponseWrapper(localName = "CloneVM_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CloneVMTaskResponse")
    public ManagedObjectReference cloneVMTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "folder", targetNamespace = "urn:vim25")
        ManagedObjectReference folder,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        VirtualMachineCloneSpec spec)
        throws CustomizationFaultFaultMsg, FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidDatastoreFaultMsg, InvalidStateFaultMsg, MigrationFaultFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws InvalidPowerStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "ExportVm", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ExportVm", targetNamespace = "urn:vim25", className = "vim25.ExportVmRequestType")
    @ResponseWrapper(localName = "ExportVmResponse", targetNamespace = "urn:vim25", className = "vim25.ExportVmResponse")
    public ManagedObjectReference exportVm(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws FileFaultFaultMsg, InvalidPowerStateFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "MarkAsTemplate", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "MarkAsTemplate", targetNamespace = "urn:vim25", className = "vim25.MarkAsTemplateRequestType")
    @ResponseWrapper(localName = "MarkAsTemplateResponse", targetNamespace = "urn:vim25", className = "vim25.MarkAsTemplateResponse")
    public void markAsTemplate(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws FileFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param pool
     * @param host
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "MarkAsVirtualMachine", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "MarkAsVirtualMachine", targetNamespace = "urn:vim25", className = "vim25.MarkAsVirtualMachineRequestType")
    @ResponseWrapper(localName = "MarkAsVirtualMachineResponse", targetNamespace = "urn:vim25", className = "vim25.MarkAsVirtualMachineResponse")
    public void markAsVirtualMachine(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "pool", targetNamespace = "urn:vim25")
        ManagedObjectReference pool,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidPowerStateFaultMsg
     */
    @WebMethod(operationName = "UnregisterVM", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UnregisterVM", targetNamespace = "urn:vim25", className = "vim25.UnregisterVMRequestType")
    @ResponseWrapper(localName = "UnregisterVMResponse", targetNamespace = "urn:vim25", className = "vim25.UnregisterVMResponse")
    public void unregisterVM(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidPowerStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "ResetGuestInformation", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ResetGuestInformation", targetNamespace = "urn:vim25", className = "vim25.ResetGuestInformationRequestType")
    @ResponseWrapper(localName = "ResetGuestInformationResponse", targetNamespace = "urn:vim25", className = "vim25.ResetGuestInformationResponse")
    public void resetGuestInformation(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws VmToolsUpgradeFaultFaultMsg
     */
    @WebMethod(operationName = "MountToolsInstaller", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "MountToolsInstaller", targetNamespace = "urn:vim25", className = "vim25.MountToolsInstallerRequestType")
    @ResponseWrapper(localName = "MountToolsInstallerResponse", targetNamespace = "urn:vim25", className = "vim25.MountToolsInstallerResponse")
    public void mountToolsInstaller(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, VmConfigFaultFaultMsg, VmToolsUpgradeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "UnmountToolsInstaller", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UnmountToolsInstaller", targetNamespace = "urn:vim25", className = "vim25.UnmountToolsInstallerRequestType")
    @ResponseWrapper(localName = "UnmountToolsInstallerResponse", targetNamespace = "urn:vim25", className = "vim25.UnmountToolsInstallerResponse")
    public void unmountToolsInstaller(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param installerOptions
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws VmToolsUpgradeFaultFaultMsg
     * @throws ToolsUnavailableFaultMsg
     */
    @WebMethod(operationName = "UpgradeTools_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "UpgradeTools_Task", targetNamespace = "urn:vim25", className = "vim25.UpgradeToolsRequestType")
    @ResponseWrapper(localName = "UpgradeTools_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.UpgradeToolsTaskResponse")
    public ManagedObjectReference upgradeToolsTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "installerOptions", targetNamespace = "urn:vim25")
        String installerOptions)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, ToolsUnavailableFaultMsg, VmConfigFaultFaultMsg, VmToolsUpgradeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.VirtualMachineMksTicket
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "AcquireMksTicket", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "AcquireMksTicket", targetNamespace = "urn:vim25", className = "vim25.AcquireMksTicketRequestType")
    @ResponseWrapper(localName = "AcquireMksTicketResponse", targetNamespace = "urn:vim25", className = "vim25.AcquireMksTicketResponse")
    public VirtualMachineMksTicket acquireMksTicket(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param ticketType
     * @param _this
     * @return
     *     returns vim25.VirtualMachineTicket
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "AcquireTicket", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "AcquireTicket", targetNamespace = "urn:vim25", className = "vim25.AcquireTicketRequestType")
    @ResponseWrapper(localName = "AcquireTicketResponse", targetNamespace = "urn:vim25", className = "vim25.AcquireTicketResponse")
    public VirtualMachineTicket acquireTicket(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "ticketType", targetNamespace = "urn:vim25")
        String ticketType)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param width
     * @param _this
     * @param height
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws ToolsUnavailableFaultMsg
     */
    @WebMethod(operationName = "SetScreenResolution", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "SetScreenResolution", targetNamespace = "urn:vim25", className = "vim25.SetScreenResolutionRequestType")
    @ResponseWrapper(localName = "SetScreenResolutionResponse", targetNamespace = "urn:vim25", className = "vim25.SetScreenResolutionResponse")
    public void setScreenResolution(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "width", targetNamespace = "urn:vim25")
        int width,
        @WebParam(name = "height", targetNamespace = "urn:vim25")
        int height)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, ToolsUnavailableFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws InvalidPowerStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "DefragmentAllDisks", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DefragmentAllDisks", targetNamespace = "urn:vim25", className = "vim25.DefragmentAllDisksRequestType")
    @ResponseWrapper(localName = "DefragmentAllDisksResponse", targetNamespace = "urn:vim25", className = "vim25.DefragmentAllDisksResponse")
    public void defragmentAllDisks(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws FileFaultFaultMsg, InvalidPowerStateFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws VmFaultToleranceIssueFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "CreateSecondaryVM_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateSecondaryVM_Task", targetNamespace = "urn:vim25", className = "vim25.CreateSecondaryVMRequestType")
    @ResponseWrapper(localName = "CreateSecondaryVM_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CreateSecondaryVMTaskResponse")
    public ManagedObjectReference createSecondaryVMTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg, VmFaultToleranceIssueFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws VmFaultToleranceIssueFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "CreateSecondaryVMEx_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateSecondaryVMEx_Task", targetNamespace = "urn:vim25", className = "vim25.CreateSecondaryVMExRequestType")
    @ResponseWrapper(localName = "CreateSecondaryVMEx_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CreateSecondaryVMExTaskResponse")
    public ManagedObjectReference createSecondaryVMExTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        FaultToleranceConfigSpec spec)
        throws FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg, VmFaultToleranceIssueFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws VmFaultToleranceIssueFaultMsg
     */
    @WebMethod(operationName = "TurnOffFaultToleranceForVM_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "TurnOffFaultToleranceForVM_Task", targetNamespace = "urn:vim25", className = "vim25.TurnOffFaultToleranceForVMRequestType")
    @ResponseWrapper(localName = "TurnOffFaultToleranceForVM_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.TurnOffFaultToleranceForVMTaskResponse")
    public ManagedObjectReference turnOffFaultToleranceForVMTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmFaultToleranceIssueFaultMsg
    ;

    /**
     * 
     * @param vm
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws VmFaultToleranceIssueFaultMsg
     */
    @WebMethod(operationName = "MakePrimaryVM_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "MakePrimaryVM_Task", targetNamespace = "urn:vim25", className = "vim25.MakePrimaryVMRequestType")
    @ResponseWrapper(localName = "MakePrimaryVM_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.MakePrimaryVMTaskResponse")
    public ManagedObjectReference makePrimaryVMTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmFaultToleranceIssueFaultMsg
    ;

    /**
     * 
     * @param vm
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws VmFaultToleranceIssueFaultMsg
     */
    @WebMethod(operationName = "TerminateFaultTolerantVM_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "TerminateFaultTolerantVM_Task", targetNamespace = "urn:vim25", className = "vim25.TerminateFaultTolerantVMRequestType")
    @ResponseWrapper(localName = "TerminateFaultTolerantVM_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.TerminateFaultTolerantVMTaskResponse")
    public ManagedObjectReference terminateFaultTolerantVMTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmFaultToleranceIssueFaultMsg
    ;

    /**
     * 
     * @param vm
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws VmFaultToleranceIssueFaultMsg
     */
    @WebMethod(operationName = "DisableSecondaryVM_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "DisableSecondaryVM_Task", targetNamespace = "urn:vim25", className = "vim25.DisableSecondaryVMRequestType")
    @ResponseWrapper(localName = "DisableSecondaryVM_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.DisableSecondaryVMTaskResponse")
    public ManagedObjectReference disableSecondaryVMTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmFaultToleranceIssueFaultMsg
    ;

    /**
     * 
     * @param vm
     * @param host
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws VmFaultToleranceIssueFaultMsg
     */
    @WebMethod(operationName = "EnableSecondaryVM_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "EnableSecondaryVM_Task", targetNamespace = "urn:vim25", className = "vim25.EnableSecondaryVMRequestType")
    @ResponseWrapper(localName = "EnableSecondaryVM_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.EnableSecondaryVMTaskResponse")
    public ManagedObjectReference enableSecondaryVMTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg, VmFaultToleranceIssueFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param displays
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws ToolsUnavailableFaultMsg
     */
    @WebMethod(operationName = "SetDisplayTopology", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "SetDisplayTopology", targetNamespace = "urn:vim25", className = "vim25.SetDisplayTopologyRequestType")
    @ResponseWrapper(localName = "SetDisplayTopologyResponse", targetNamespace = "urn:vim25", className = "vim25.SetDisplayTopologyResponse")
    public void setDisplayTopology(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "displays", targetNamespace = "urn:vim25")
        List<VirtualMachineDisplayTopology> displays)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, ToolsUnavailableFaultMsg
    ;

    /**
     * 
     * @param name
     * @param description
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws InvalidPowerStateFaultMsg
     * @throws SnapshotFaultFaultMsg
     * @throws RecordReplayDisabledFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws HostIncompatibleForRecordReplayFaultMsg
     */
    @WebMethod(operationName = "StartRecording_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "StartRecording_Task", targetNamespace = "urn:vim25", className = "vim25.StartRecordingRequestType")
    @ResponseWrapper(localName = "StartRecording_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.StartRecordingTaskResponse")
    public ManagedObjectReference startRecordingTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "description", targetNamespace = "urn:vim25")
        String description)
        throws FileFaultFaultMsg, HostIncompatibleForRecordReplayFaultMsg, InvalidNameFaultMsg, InvalidPowerStateFaultMsg, InvalidStateFaultMsg, RecordReplayDisabledFaultMsg, RuntimeFaultFaultMsg, SnapshotFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws InvalidPowerStateFaultMsg
     * @throws FileFaultFaultMsg
     * @throws SnapshotFaultFaultMsg
     */
    @WebMethod(operationName = "StopRecording_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "StopRecording_Task", targetNamespace = "urn:vim25", className = "vim25.StopRecordingRequestType")
    @ResponseWrapper(localName = "StopRecording_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.StopRecordingTaskResponse")
    public ManagedObjectReference stopRecordingTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws FileFaultFaultMsg, InvalidPowerStateFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, SnapshotFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param replaySnapshot
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NotFoundFaultMsg
     * @throws InvalidPowerStateFaultMsg
     * @throws SnapshotFaultFaultMsg
     * @throws RecordReplayDisabledFaultMsg
     * @throws FileFaultFaultMsg
     * @throws HostIncompatibleForRecordReplayFaultMsg
     */
    @WebMethod(operationName = "StartReplaying_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "StartReplaying_Task", targetNamespace = "urn:vim25", className = "vim25.StartReplayingRequestType")
    @ResponseWrapper(localName = "StartReplaying_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.StartReplayingTaskResponse")
    public ManagedObjectReference startReplayingTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "replaySnapshot", targetNamespace = "urn:vim25")
        ManagedObjectReference replaySnapshot)
        throws FileFaultFaultMsg, HostIncompatibleForRecordReplayFaultMsg, InvalidPowerStateFaultMsg, InvalidStateFaultMsg, NotFoundFaultMsg, RecordReplayDisabledFaultMsg, RuntimeFaultFaultMsg, SnapshotFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws InvalidPowerStateFaultMsg
     * @throws SnapshotFaultFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "StopReplaying_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "StopReplaying_Task", targetNamespace = "urn:vim25", className = "vim25.StopReplayingRequestType")
    @ResponseWrapper(localName = "StopReplaying_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.StopReplayingTaskResponse")
    public ManagedObjectReference stopReplayingTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws FileFaultFaultMsg, InvalidPowerStateFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, SnapshotFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param unlink
     * @param disks
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws InvalidPowerStateFaultMsg
     */
    @WebMethod(operationName = "PromoteDisks_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "PromoteDisks_Task", targetNamespace = "urn:vim25", className = "vim25.PromoteDisksRequestType")
    @ResponseWrapper(localName = "PromoteDisks_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.PromoteDisksTaskResponse")
    public ManagedObjectReference promoteDisksTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "unlink", targetNamespace = "urn:vim25")
        boolean unlink,
        @WebParam(name = "disks", targetNamespace = "urn:vim25")
        List<VirtualDisk> disks)
        throws InvalidPowerStateFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "CreateScreenshot_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateScreenshot_Task", targetNamespace = "urn:vim25", className = "vim25.CreateScreenshotRequestType")
    @ResponseWrapper(localName = "CreateScreenshot_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CreateScreenshotTaskResponse")
    public ManagedObjectReference createScreenshotTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws FileFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param startOffset
     * @param deviceKey
     * @param changeId
     * @param _this
     * @param snapshot
     * @return
     *     returns vim25.DiskChangeInfo
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "QueryChangedDiskAreas", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryChangedDiskAreas", targetNamespace = "urn:vim25", className = "vim25.QueryChangedDiskAreasRequestType")
    @ResponseWrapper(localName = "QueryChangedDiskAreasResponse", targetNamespace = "urn:vim25", className = "vim25.QueryChangedDiskAreasResponse")
    public DiskChangeInfo queryChangedDiskAreas(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "snapshot", targetNamespace = "urn:vim25")
        ManagedObjectReference snapshot,
        @WebParam(name = "deviceKey", targetNamespace = "urn:vim25")
        int deviceKey,
        @WebParam(name = "startOffset", targetNamespace = "urn:vim25")
        long startOffset,
        @WebParam(name = "changeId", targetNamespace = "urn:vim25")
        String changeId)
        throws FileFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.util.List<java.lang.String>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryUnownedFiles", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryUnownedFiles", targetNamespace = "urn:vim25", className = "vim25.QueryUnownedFilesRequestType")
    @ResponseWrapper(localName = "QueryUnownedFilesResponse", targetNamespace = "urn:vim25", className = "vim25.QueryUnownedFilesResponse")
    public List<String> queryUnownedFiles(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param configurationPath
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws AlreadyExistsFaultMsg
     * @throws InvalidPowerStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "reloadVirtualMachineFromPath_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "reloadVirtualMachineFromPath_Task", targetNamespace = "urn:vim25", className = "vim25.ReloadVirtualMachineFromPathRequestType")
    @ResponseWrapper(localName = "reloadVirtualMachineFromPath_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ReloadVirtualMachineFromPathTaskResponse")
    public ManagedObjectReference reloadVirtualMachineFromPathTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "configurationPath", targetNamespace = "urn:vim25")
        String configurationPath)
        throws AlreadyExistsFaultMsg, FileFaultFaultMsg, InvalidPowerStateFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.util.List<vim25.LocalizedMethodFault>
     * @throws RuntimeFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "QueryFaultToleranceCompatibility", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryFaultToleranceCompatibility", targetNamespace = "urn:vim25", className = "vim25.QueryFaultToleranceCompatibilityRequestType")
    @ResponseWrapper(localName = "QueryFaultToleranceCompatibilityResponse", targetNamespace = "urn:vim25", className = "vim25.QueryFaultToleranceCompatibilityResponse")
    public List<LocalizedMethodFault> queryFaultToleranceCompatibility(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param forLegacyFt
     * @param _this
     * @return
     *     returns java.util.List<vim25.LocalizedMethodFault>
     * @throws RuntimeFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "QueryFaultToleranceCompatibilityEx", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryFaultToleranceCompatibilityEx", targetNamespace = "urn:vim25", className = "vim25.QueryFaultToleranceCompatibilityExRequestType")
    @ResponseWrapper(localName = "QueryFaultToleranceCompatibilityExResponse", targetNamespace = "urn:vim25", className = "vim25.QueryFaultToleranceCompatibilityExResponse")
    public List<LocalizedMethodFault> queryFaultToleranceCompatibilityEx(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "forLegacyFt", targetNamespace = "urn:vim25")
        Boolean forLegacyFt)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "TerminateVM", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "TerminateVM", targetNamespace = "urn:vim25", className = "vim25.TerminateVMRequestType")
    @ResponseWrapper(localName = "TerminateVMResponse", targetNamespace = "urn:vim25", className = "vim25.TerminateVMResponse")
    public void terminateVM(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "SendNMI", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "SendNMI", targetNamespace = "urn:vim25", className = "vim25.SendNMIRequestType")
    @ResponseWrapper(localName = "SendNMIResponse", targetNamespace = "urn:vim25", className = "vim25.SendNMIResponse")
    public void sendNMI(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param cluster
     * @param _this
     * @param downgradeFormat
     * @return
     *     returns vim25.VsanUpgradeSystemPreflightCheckResult
     * @throws RuntimeFaultFaultMsg
     * @throws VsanFaultFaultMsg
     */
    @WebMethod(operationName = "PerformVsanUpgradePreflightCheck", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "PerformVsanUpgradePreflightCheck", targetNamespace = "urn:vim25", className = "vim25.PerformVsanUpgradePreflightCheckRequestType")
    @ResponseWrapper(localName = "PerformVsanUpgradePreflightCheckResponse", targetNamespace = "urn:vim25", className = "vim25.PerformVsanUpgradePreflightCheckResponse")
    public VsanUpgradeSystemPreflightCheckResult performVsanUpgradePreflightCheck(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "cluster", targetNamespace = "urn:vim25")
        ManagedObjectReference cluster,
        @WebParam(name = "downgradeFormat", targetNamespace = "urn:vim25")
        Boolean downgradeFormat)
        throws RuntimeFaultFaultMsg, VsanFaultFaultMsg
    ;

    /**
     * 
     * @param cluster
     * @param _this
     * @return
     *     returns vim25.VsanUpgradeSystemUpgradeStatus
     * @throws RuntimeFaultFaultMsg
     * @throws VsanFaultFaultMsg
     */
    @WebMethod(operationName = "QueryVsanUpgradeStatus", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryVsanUpgradeStatus", targetNamespace = "urn:vim25", className = "vim25.QueryVsanUpgradeStatusRequestType")
    @ResponseWrapper(localName = "QueryVsanUpgradeStatusResponse", targetNamespace = "urn:vim25", className = "vim25.QueryVsanUpgradeStatusResponse")
    public VsanUpgradeSystemUpgradeStatus queryVsanUpgradeStatus(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "cluster", targetNamespace = "urn:vim25")
        ManagedObjectReference cluster)
        throws RuntimeFaultFaultMsg, VsanFaultFaultMsg
    ;

    /**
     * 
     * @param allowReducedRedundancy
     * @param cluster
     * @param performObjectUpgrade
     * @param _this
     * @param excludeHosts
     * @param downgradeFormat
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws VsanFaultFaultMsg
     */
    @WebMethod(operationName = "PerformVsanUpgrade_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "PerformVsanUpgrade_Task", targetNamespace = "urn:vim25", className = "vim25.PerformVsanUpgradeRequestType")
    @ResponseWrapper(localName = "PerformVsanUpgrade_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.PerformVsanUpgradeTaskResponse")
    public ManagedObjectReference performVsanUpgradeTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "cluster", targetNamespace = "urn:vim25")
        ManagedObjectReference cluster,
        @WebParam(name = "performObjectUpgrade", targetNamespace = "urn:vim25")
        Boolean performObjectUpgrade,
        @WebParam(name = "downgradeFormat", targetNamespace = "urn:vim25")
        Boolean downgradeFormat,
        @WebParam(name = "allowReducedRedundancy", targetNamespace = "urn:vim25")
        Boolean allowReducedRedundancy,
        @WebParam(name = "excludeHosts", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> excludeHosts)
        throws RuntimeFaultFaultMsg, VsanFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RemoveAlarm", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RemoveAlarm", targetNamespace = "urn:vim25", className = "vim25.RemoveAlarmRequestType")
    @ResponseWrapper(localName = "RemoveAlarmResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveAlarmResponse")
    public void removeAlarm(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "ReconfigureAlarm", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ReconfigureAlarm", targetNamespace = "urn:vim25", className = "vim25.ReconfigureAlarmRequestType")
    @ResponseWrapper(localName = "ReconfigureAlarmResponse", targetNamespace = "urn:vim25", className = "vim25.ReconfigureAlarmResponse")
    public void reconfigureAlarm(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        AlarmSpec spec)
        throws DuplicateNameFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param entity
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "CreateAlarm", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateAlarm", targetNamespace = "urn:vim25", className = "vim25.CreateAlarmRequestType")
    @ResponseWrapper(localName = "CreateAlarmResponse", targetNamespace = "urn:vim25", className = "vim25.CreateAlarmResponse")
    public ManagedObjectReference createAlarm(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        ManagedObjectReference entity,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        AlarmSpec spec)
        throws DuplicateNameFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param entity
     * @return
     *     returns java.util.List<vim25.ManagedObjectReference>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "GetAlarm", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "GetAlarm", targetNamespace = "urn:vim25", className = "vim25.GetAlarmRequestType")
    @ResponseWrapper(localName = "GetAlarmResponse", targetNamespace = "urn:vim25", className = "vim25.GetAlarmResponse")
    public List<ManagedObjectReference> getAlarm(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        ManagedObjectReference entity)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param entity
     * @return
     *     returns boolean
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "AreAlarmActionsEnabled", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "AreAlarmActionsEnabled", targetNamespace = "urn:vim25", className = "vim25.AreAlarmActionsEnabledRequestType")
    @ResponseWrapper(localName = "AreAlarmActionsEnabledResponse", targetNamespace = "urn:vim25", className = "vim25.AreAlarmActionsEnabledResponse")
    public boolean areAlarmActionsEnabled(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        ManagedObjectReference entity)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param entity
     * @param enabled
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "EnableAlarmActions", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "EnableAlarmActions", targetNamespace = "urn:vim25", className = "vim25.EnableAlarmActionsRequestType")
    @ResponseWrapper(localName = "EnableAlarmActionsResponse", targetNamespace = "urn:vim25", className = "vim25.EnableAlarmActionsResponse")
    public void enableAlarmActions(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        ManagedObjectReference entity,
        @WebParam(name = "enabled", targetNamespace = "urn:vim25")
        boolean enabled)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param entity
     * @return
     *     returns java.util.List<vim25.AlarmState>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "GetAlarmState", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "GetAlarmState", targetNamespace = "urn:vim25", className = "vim25.GetAlarmStateRequestType")
    @ResponseWrapper(localName = "GetAlarmStateResponse", targetNamespace = "urn:vim25", className = "vim25.GetAlarmStateResponse")
    public List<AlarmState> getAlarmState(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        ManagedObjectReference entity)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param alarm
     * @param _this
     * @param entity
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "AcknowledgeAlarm", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "AcknowledgeAlarm", targetNamespace = "urn:vim25", className = "vim25.AcknowledgeAlarmRequestType")
    @ResponseWrapper(localName = "AcknowledgeAlarmResponse", targetNamespace = "urn:vim25", className = "vim25.AcknowledgeAlarmResponse")
    public void acknowledgeAlarm(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "alarm", targetNamespace = "urn:vim25")
        ManagedObjectReference alarm,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        ManagedObjectReference entity)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param evcModeKey
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws EVCConfigFaultFaultMsg
     */
    @WebMethod(operationName = "ConfigureEvcMode_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ConfigureEvcMode_Task", targetNamespace = "urn:vim25", className = "vim25.ConfigureEvcModeRequestType")
    @ResponseWrapper(localName = "ConfigureEvcMode_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ConfigureEvcModeTaskResponse")
    public ManagedObjectReference configureEvcModeTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "evcModeKey", targetNamespace = "urn:vim25")
        String evcModeKey)
        throws EVCConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "DisableEvcMode_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "DisableEvcMode_Task", targetNamespace = "urn:vim25", className = "vim25.DisableEvcModeRequestType")
    @ResponseWrapper(localName = "DisableEvcMode_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.DisableEvcModeTaskResponse")
    public ManagedObjectReference disableEvcModeTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param evcModeKey
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "CheckConfigureEvcMode_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CheckConfigureEvcMode_Task", targetNamespace = "urn:vim25", className = "vim25.CheckConfigureEvcModeRequestType")
    @ResponseWrapper(localName = "CheckConfigureEvcMode_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CheckConfigureEvcModeTaskResponse")
    public ManagedObjectReference checkConfigureEvcModeTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "evcModeKey", targetNamespace = "urn:vim25")
        String evcModeKey)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param cnxSpec
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws HostConnectFaultFaultMsg
     * @throws InvalidLoginFaultMsg
     */
    @WebMethod(operationName = "CheckAddHostEvc_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CheckAddHostEvc_Task", targetNamespace = "urn:vim25", className = "vim25.CheckAddHostEvcRequestType")
    @ResponseWrapper(localName = "CheckAddHostEvc_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CheckAddHostEvcTaskResponse")
    public ManagedObjectReference checkAddHostEvcTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "cnxSpec", targetNamespace = "urn:vim25")
        HostConnectSpec cnxSpec)
        throws HostConnectFaultFaultMsg, InvalidLoginFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws DvsFaultFaultMsg
     * @throws RuntimeFaultFaultMsg
     * @throws ConcurrentAccessFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "ReconfigureDVPortgroup_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ReconfigureDVPortgroup_Task", targetNamespace = "urn:vim25", className = "vim25.ReconfigureDVPortgroupRequestType")
    @ResponseWrapper(localName = "ReconfigureDVPortgroup_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ReconfigureDVPortgroupTaskResponse")
    public ManagedObjectReference reconfigureDVPortgroupTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        DVPortgroupConfigSpec spec)
        throws ConcurrentAccessFaultMsg, DuplicateNameFaultMsg, DvsFaultFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param entityBackup
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws DvsFaultFaultMsg
     * @throws RollbackFailureFaultMsg
     */
    @WebMethod(operationName = "DVPortgroupRollback_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "DVPortgroupRollback_Task", targetNamespace = "urn:vim25", className = "vim25.DVPortgroupRollbackRequestType")
    @ResponseWrapper(localName = "DVPortgroupRollback_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.DVPortgroupRollbackTaskResponse")
    public ManagedObjectReference dvPortgroupRollbackTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entityBackup", targetNamespace = "urn:vim25")
        EntityBackupConfig entityBackup)
        throws DvsFaultFaultMsg, RollbackFailureFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param recommended
     * @return
     *     returns java.util.List<vim25.DistributedVirtualSwitchProductSpec>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryAvailableDvsSpec", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryAvailableDvsSpec", targetNamespace = "urn:vim25", className = "vim25.QueryAvailableDvsSpecRequestType")
    @ResponseWrapper(localName = "QueryAvailableDvsSpecResponse", targetNamespace = "urn:vim25", className = "vim25.QueryAvailableDvsSpecResponse")
    public List<DistributedVirtualSwitchProductSpec> queryAvailableDvsSpec(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "recommended", targetNamespace = "urn:vim25")
        Boolean recommended)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param container
     * @param switchProductSpec
     * @param _this
     * @param recursive
     * @return
     *     returns java.util.List<vim25.ManagedObjectReference>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryCompatibleHostForNewDvs", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryCompatibleHostForNewDvs", targetNamespace = "urn:vim25", className = "vim25.QueryCompatibleHostForNewDvsRequestType")
    @ResponseWrapper(localName = "QueryCompatibleHostForNewDvsResponse", targetNamespace = "urn:vim25", className = "vim25.QueryCompatibleHostForNewDvsResponse")
    public List<ManagedObjectReference> queryCompatibleHostForNewDvs(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "container", targetNamespace = "urn:vim25")
        ManagedObjectReference container,
        @WebParam(name = "recursive", targetNamespace = "urn:vim25")
        boolean recursive,
        @WebParam(name = "switchProductSpec", targetNamespace = "urn:vim25")
        DistributedVirtualSwitchProductSpec switchProductSpec)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param container
     * @param dvs
     * @param _this
     * @param recursive
     * @return
     *     returns java.util.List<vim25.ManagedObjectReference>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryCompatibleHostForExistingDvs", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryCompatibleHostForExistingDvs", targetNamespace = "urn:vim25", className = "vim25.QueryCompatibleHostForExistingDvsRequestType")
    @ResponseWrapper(localName = "QueryCompatibleHostForExistingDvsResponse", targetNamespace = "urn:vim25", className = "vim25.QueryCompatibleHostForExistingDvsResponse")
    public List<ManagedObjectReference> queryCompatibleHostForExistingDvs(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "container", targetNamespace = "urn:vim25")
        ManagedObjectReference container,
        @WebParam(name = "recursive", targetNamespace = "urn:vim25")
        boolean recursive,
        @WebParam(name = "dvs", targetNamespace = "urn:vim25")
        ManagedObjectReference dvs)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param switchProductSpec
     * @param _this
     * @return
     *     returns java.util.List<vim25.DistributedVirtualSwitchHostProductSpec>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryDvsCompatibleHostSpec", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryDvsCompatibleHostSpec", targetNamespace = "urn:vim25", className = "vim25.QueryDvsCompatibleHostSpecRequestType")
    @ResponseWrapper(localName = "QueryDvsCompatibleHostSpecResponse", targetNamespace = "urn:vim25", className = "vim25.QueryDvsCompatibleHostSpecResponse")
    public List<DistributedVirtualSwitchHostProductSpec> queryDvsCompatibleHostSpec(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "switchProductSpec", targetNamespace = "urn:vim25")
        DistributedVirtualSwitchProductSpec switchProductSpec)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param switchProductSpec
     * @param _this
     * @return
     *     returns vim25.DVSFeatureCapability
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryDvsFeatureCapability", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryDvsFeatureCapability", targetNamespace = "urn:vim25", className = "vim25.QueryDvsFeatureCapabilityRequestType")
    @ResponseWrapper(localName = "QueryDvsFeatureCapabilityResponse", targetNamespace = "urn:vim25", className = "vim25.QueryDvsFeatureCapabilityResponse")
    public DVSFeatureCapability queryDvsFeatureCapability(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "switchProductSpec", targetNamespace = "urn:vim25")
        DistributedVirtualSwitchProductSpec switchProductSpec)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param uuid
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "QueryDvsByUuid", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryDvsByUuid", targetNamespace = "urn:vim25", className = "vim25.QueryDvsByUuidRequestType")
    @ResponseWrapper(localName = "QueryDvsByUuidResponse", targetNamespace = "urn:vim25", className = "vim25.QueryDvsByUuidResponse")
    public ManagedObjectReference queryDvsByUuid(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "uuid", targetNamespace = "urn:vim25")
        String uuid)
        throws NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param dvs
     * @param host
     * @param _this
     * @return
     *     returns vim25.DVSManagerDvsConfigTarget
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryDvsConfigTarget", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryDvsConfigTarget", targetNamespace = "urn:vim25", className = "vim25.QueryDvsConfigTargetRequestType")
    @ResponseWrapper(localName = "QueryDvsConfigTargetResponse", targetNamespace = "urn:vim25", className = "vim25.QueryDvsConfigTargetResponse")
    public DVSManagerDvsConfigTarget queryDvsConfigTarget(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host,
        @WebParam(name = "dvs", targetNamespace = "urn:vim25")
        ManagedObjectReference dvs)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param hostContainer
     * @param _this
     * @param dvsProductSpec
     * @param hostFilterSpec
     * @return
     *     returns java.util.List<vim25.DistributedVirtualSwitchManagerCompatibilityResult>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryDvsCheckCompatibility", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryDvsCheckCompatibility", targetNamespace = "urn:vim25", className = "vim25.QueryDvsCheckCompatibilityRequestType")
    @ResponseWrapper(localName = "QueryDvsCheckCompatibilityResponse", targetNamespace = "urn:vim25", className = "vim25.QueryDvsCheckCompatibilityResponse")
    public List<DistributedVirtualSwitchManagerCompatibilityResult> queryDvsCheckCompatibility(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "hostContainer", targetNamespace = "urn:vim25")
        DistributedVirtualSwitchManagerHostContainer hostContainer,
        @WebParam(name = "dvsProductSpec", targetNamespace = "urn:vim25")
        DistributedVirtualSwitchManagerDvsProductSpec dvsProductSpec,
        @WebParam(name = "hostFilterSpec", targetNamespace = "urn:vim25")
        List<DistributedVirtualSwitchManagerHostDvsFilterSpec> hostFilterSpec)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param hosts
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws DvsFaultFaultMsg
     */
    @WebMethod(operationName = "RectifyDvsOnHost_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RectifyDvsOnHost_Task", targetNamespace = "urn:vim25", className = "vim25.RectifyDvsOnHostRequestType")
    @ResponseWrapper(localName = "RectifyDvsOnHost_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.RectifyDvsOnHostTaskResponse")
    public ManagedObjectReference rectifyDvsOnHostTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "hosts", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> hosts)
        throws DvsFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param selectionSet
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws BackupBlobWriteFailureFaultMsg
     */
    @WebMethod(operationName = "DVSManagerExportEntity_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "DVSManagerExportEntity_Task", targetNamespace = "urn:vim25", className = "vim25.DVSManagerExportEntityRequestType")
    @ResponseWrapper(localName = "DVSManagerExportEntity_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.DVSManagerExportEntityTaskResponse")
    public ManagedObjectReference dvsManagerExportEntityTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "selectionSet", targetNamespace = "urn:vim25")
        List<SelectionSet> selectionSet)
        throws BackupBlobWriteFailureFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param importType
     * @param entityBackup
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws DvsFaultFaultMsg
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "DVSManagerImportEntity_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "DVSManagerImportEntity_Task", targetNamespace = "urn:vim25", className = "vim25.DVSManagerImportEntityRequestType")
    @ResponseWrapper(localName = "DVSManagerImportEntity_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.DVSManagerImportEntityTaskResponse")
    public ManagedObjectReference dvsManagerImportEntityTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entityBackup", targetNamespace = "urn:vim25")
        List<EntityBackupConfig> entityBackup,
        @WebParam(name = "importType", targetNamespace = "urn:vim25")
        String importType)
        throws DvsFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param switchUuid
     * @param portgroupKey
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "DVSManagerLookupDvPortGroup", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "DVSManagerLookupDvPortGroup", targetNamespace = "urn:vim25", className = "vim25.DVSManagerLookupDvPortGroupRequestType")
    @ResponseWrapper(localName = "DVSManagerLookupDvPortGroupResponse", targetNamespace = "urn:vim25", className = "vim25.DVSManagerLookupDvPortGroupResponse")
    public ManagedObjectReference dvsManagerLookupDvPortGroup(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "switchUuid", targetNamespace = "urn:vim25")
        String switchUuid,
        @WebParam(name = "portgroupKey", targetNamespace = "urn:vim25")
        String portgroupKey)
        throws NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param lacpGroupSpec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws DvsFaultFaultMsg
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateDVSLacpGroupConfig_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "UpdateDVSLacpGroupConfig_Task", targetNamespace = "urn:vim25", className = "vim25.UpdateDVSLacpGroupConfigRequestType")
    @ResponseWrapper(localName = "UpdateDVSLacpGroupConfig_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateDVSLacpGroupConfigTaskResponse")
    public ManagedObjectReference updateDVSLacpGroupConfigTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "lacpGroupSpec", targetNamespace = "urn:vim25")
        List<VMwareDvsLacpGroupSpec> lacpGroupSpec)
        throws DvsFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param maxCount
     * @return
     *     returns java.util.List<vim25.Event>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ReadNextEvents", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ReadNextEvents", targetNamespace = "urn:vim25", className = "vim25.ReadNextEventsRequestType")
    @ResponseWrapper(localName = "ReadNextEventsResponse", targetNamespace = "urn:vim25", className = "vim25.ReadNextEventsResponse")
    public List<Event> readNextEvents(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "maxCount", targetNamespace = "urn:vim25")
        int maxCount)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param maxCount
     * @return
     *     returns java.util.List<vim25.Event>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ReadPreviousEvents", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ReadPreviousEvents", targetNamespace = "urn:vim25", className = "vim25.ReadPreviousEventsRequestType")
    @ResponseWrapper(localName = "ReadPreviousEventsResponse", targetNamespace = "urn:vim25", className = "vim25.ReadPreviousEventsResponse")
    public List<Event> readPreviousEvents(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "maxCount", targetNamespace = "urn:vim25")
        int maxCount)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param eventTypeId
     * @param _this
     * @return
     *     returns java.util.List<vim25.EventArgDesc>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RetrieveArgumentDescription", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RetrieveArgumentDescription", targetNamespace = "urn:vim25", className = "vim25.RetrieveArgumentDescriptionRequestType")
    @ResponseWrapper(localName = "RetrieveArgumentDescriptionResponse", targetNamespace = "urn:vim25", className = "vim25.RetrieveArgumentDescriptionResponse")
    public List<EventArgDesc> retrieveArgumentDescription(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "eventTypeId", targetNamespace = "urn:vim25")
        String eventTypeId)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param filter
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "CreateCollectorForEvents", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateCollectorForEvents", targetNamespace = "urn:vim25", className = "vim25.CreateCollectorForEventsRequestType")
    @ResponseWrapper(localName = "CreateCollectorForEventsResponse", targetNamespace = "urn:vim25", className = "vim25.CreateCollectorForEventsResponse")
    public ManagedObjectReference createCollectorForEvents(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "filter", targetNamespace = "urn:vim25")
        EventFilterSpec filter)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param msg
     * @param _this
     * @param entity
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "LogUserEvent", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "LogUserEvent", targetNamespace = "urn:vim25", className = "vim25.LogUserEventRequestType")
    @ResponseWrapper(localName = "LogUserEventResponse", targetNamespace = "urn:vim25", className = "vim25.LogUserEventResponse")
    public void logUserEvent(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        ManagedObjectReference entity,
        @WebParam(name = "msg", targetNamespace = "urn:vim25")
        String msg)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param filter
     * @param _this
     * @return
     *     returns java.util.List<vim25.Event>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryEvents", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryEvents", targetNamespace = "urn:vim25", className = "vim25.QueryEventsRequestType")
    @ResponseWrapper(localName = "QueryEventsResponse", targetNamespace = "urn:vim25", className = "vim25.QueryEventsResponse")
    public List<Event> queryEvents(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "filter", targetNamespace = "urn:vim25")
        EventFilterSpec filter)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param eventToPost
     * @param taskInfo
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidEventFaultMsg
     */
    @WebMethod(operationName = "PostEvent", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "PostEvent", targetNamespace = "urn:vim25", className = "vim25.PostEventRequestType")
    @ResponseWrapper(localName = "PostEventResponse", targetNamespace = "urn:vim25", className = "vim25.PostEventResponse")
    public void postEvent(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "eventToPost", targetNamespace = "urn:vim25")
        Event eventToPost,
        @WebParam(name = "taskInfo", targetNamespace = "urn:vim25")
        TaskInfo taskInfo)
        throws InvalidEventFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param password
     * @param domainName
     * @param _this
     * @param userName
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws InvalidLoginFaultMsg
     * @throws ActiveDirectoryFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "JoinDomain_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "JoinDomain_Task", targetNamespace = "urn:vim25", className = "vim25.JoinDomainRequestType")
    @ResponseWrapper(localName = "JoinDomain_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.JoinDomainTaskResponse")
    public ManagedObjectReference joinDomainTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "domainName", targetNamespace = "urn:vim25")
        String domainName,
        @WebParam(name = "userName", targetNamespace = "urn:vim25")
        String userName,
        @WebParam(name = "password", targetNamespace = "urn:vim25")
        String password)
        throws ActiveDirectoryFaultFaultMsg, HostConfigFaultFaultMsg, InvalidLoginFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param camServer
     * @param domainName
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws ActiveDirectoryFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "JoinDomainWithCAM_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "JoinDomainWithCAM_Task", targetNamespace = "urn:vim25", className = "vim25.JoinDomainWithCAMRequestType")
    @ResponseWrapper(localName = "JoinDomainWithCAM_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.JoinDomainWithCAMTaskResponse")
    public ManagedObjectReference joinDomainWithCAMTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "domainName", targetNamespace = "urn:vim25")
        String domainName,
        @WebParam(name = "camServer", targetNamespace = "urn:vim25")
        String camServer)
        throws ActiveDirectoryFaultFaultMsg, HostConfigFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param certPath
     * @param camServer
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws ActiveDirectoryFaultFaultMsg
     * @throws FileNotFoundFaultMsg
     */
    @WebMethod(operationName = "ImportCertificateForCAM_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ImportCertificateForCAM_Task", targetNamespace = "urn:vim25", className = "vim25.ImportCertificateForCAMRequestType")
    @ResponseWrapper(localName = "ImportCertificateForCAM_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ImportCertificateForCAMTaskResponse")
    public ManagedObjectReference importCertificateForCAMTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "certPath", targetNamespace = "urn:vim25")
        String certPath,
        @WebParam(name = "camServer", targetNamespace = "urn:vim25")
        String camServer)
        throws ActiveDirectoryFaultFaultMsg, FileNotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param force
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws ActiveDirectoryFaultFaultMsg
     * @throws AuthMinimumAdminPermissionFaultMsg
     */
    @WebMethod(operationName = "LeaveCurrentDomain_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "LeaveCurrentDomain_Task", targetNamespace = "urn:vim25", className = "vim25.LeaveCurrentDomainRequestType")
    @ResponseWrapper(localName = "LeaveCurrentDomain_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.LeaveCurrentDomainTaskResponse")
    public ManagedObjectReference leaveCurrentDomainTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "force", targetNamespace = "urn:vim25")
        boolean force)
        throws ActiveDirectoryFaultFaultMsg, AuthMinimumAdminPermissionFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws ActiveDirectoryFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "EnableSmartCardAuthentication", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "EnableSmartCardAuthentication", targetNamespace = "urn:vim25", className = "vim25.EnableSmartCardAuthenticationRequestType")
    @ResponseWrapper(localName = "EnableSmartCardAuthenticationResponse", targetNamespace = "urn:vim25", className = "vim25.EnableSmartCardAuthenticationResponse")
    public void enableSmartCardAuthentication(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws ActiveDirectoryFaultFaultMsg, HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param cert
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "InstallSmartCardTrustAnchor", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "InstallSmartCardTrustAnchor", targetNamespace = "urn:vim25", className = "vim25.InstallSmartCardTrustAnchorRequestType")
    @ResponseWrapper(localName = "InstallSmartCardTrustAnchorResponse", targetNamespace = "urn:vim25", className = "vim25.InstallSmartCardTrustAnchorResponse")
    public void installSmartCardTrustAnchor(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "cert", targetNamespace = "urn:vim25")
        String cert)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param certs
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ReplaceSmartCardTrustAnchors", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ReplaceSmartCardTrustAnchors", targetNamespace = "urn:vim25", className = "vim25.ReplaceSmartCardTrustAnchorsRequestType")
    @ResponseWrapper(localName = "ReplaceSmartCardTrustAnchorsResponse", targetNamespace = "urn:vim25", className = "vim25.ReplaceSmartCardTrustAnchorsResponse")
    public void replaceSmartCardTrustAnchors(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "certs", targetNamespace = "urn:vim25")
        List<String> certs)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param serial
     * @param _this
     * @param issuer
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "RemoveSmartCardTrustAnchor", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RemoveSmartCardTrustAnchor", targetNamespace = "urn:vim25", className = "vim25.RemoveSmartCardTrustAnchorRequestType")
    @ResponseWrapper(localName = "RemoveSmartCardTrustAnchorResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveSmartCardTrustAnchorResponse")
    public void removeSmartCardTrustAnchor(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "issuer", targetNamespace = "urn:vim25")
        String issuer,
        @WebParam(name = "serial", targetNamespace = "urn:vim25")
        String serial)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param fingerprint
     * @param digest
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "RemoveSmartCardTrustAnchorByFingerprint", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RemoveSmartCardTrustAnchorByFingerprint", targetNamespace = "urn:vim25", className = "vim25.RemoveSmartCardTrustAnchorByFingerprintRequestType")
    @ResponseWrapper(localName = "RemoveSmartCardTrustAnchorByFingerprintResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveSmartCardTrustAnchorByFingerprintResponse")
    public void removeSmartCardTrustAnchorByFingerprint(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "fingerprint", targetNamespace = "urn:vim25")
        String fingerprint,
        @WebParam(name = "digest", targetNamespace = "urn:vim25")
        String digest)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.util.List<java.lang.String>
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "ListSmartCardTrustAnchors", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ListSmartCardTrustAnchors", targetNamespace = "urn:vim25", className = "vim25.ListSmartCardTrustAnchorsRequestType")
    @ResponseWrapper(localName = "ListSmartCardTrustAnchorsResponse", targetNamespace = "urn:vim25", className = "vim25.ListSmartCardTrustAnchorsResponse")
    public List<String> listSmartCardTrustAnchors(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws ActiveDirectoryFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "DisableSmartCardAuthentication", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DisableSmartCardAuthentication", targetNamespace = "urn:vim25", className = "vim25.DisableSmartCardAuthenticationRequestType")
    @ResponseWrapper(localName = "DisableSmartCardAuthenticationResponse", targetNamespace = "urn:vim25", className = "vim25.DisableSmartCardAuthenticationResponse")
    public void disableSmartCardAuthentication(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws ActiveDirectoryFaultFaultMsg, HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ReconfigureAutostart", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ReconfigureAutostart", targetNamespace = "urn:vim25", className = "vim25.ReconfigureAutostartRequestType")
    @ResponseWrapper(localName = "ReconfigureAutostartResponse", targetNamespace = "urn:vim25", className = "vim25.ReconfigureAutostartResponse")
    public void reconfigureAutostart(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        HostAutoStartManagerConfig spec)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "AutoStartPowerOn", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "AutoStartPowerOn", targetNamespace = "urn:vim25", className = "vim25.AutoStartPowerOnRequestType")
    @ResponseWrapper(localName = "AutoStartPowerOnResponse", targetNamespace = "urn:vim25", className = "vim25.AutoStartPowerOnResponse")
    public void autoStartPowerOn(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "AutoStartPowerOff", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "AutoStartPowerOff", targetNamespace = "urn:vim25", className = "vim25.AutoStartPowerOffRequestType")
    @ResponseWrapper(localName = "AutoStartPowerOffResponse", targetNamespace = "urn:vim25", className = "vim25.AutoStartPowerOffResponse")
    public void autoStartPowerOff(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.HostBootDeviceInfo
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryBootDevices", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryBootDevices", targetNamespace = "urn:vim25", className = "vim25.QueryBootDevicesRequestType")
    @ResponseWrapper(localName = "QueryBootDevicesResponse", targetNamespace = "urn:vim25", className = "vim25.QueryBootDevicesResponse")
    public HostBootDeviceInfo queryBootDevices(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param key
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateBootDevice", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateBootDevice", targetNamespace = "urn:vim25", className = "vim25.UpdateBootDeviceRequestType")
    @ResponseWrapper(localName = "UpdateBootDeviceResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateBootDeviceResponse")
    public void updateBootDevice(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "key", targetNamespace = "urn:vim25")
        String key)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ConfigureHostCache_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ConfigureHostCache_Task", targetNamespace = "urn:vim25", className = "vim25.ConfigureHostCacheRequestType")
    @ResponseWrapper(localName = "ConfigureHostCache_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ConfigureHostCacheTaskResponse")
    public ManagedObjectReference configureHostCacheTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        HostCacheConfigurationSpec spec)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param useIpAddressAsCommonName
     * @param _this
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "GenerateCertificateSigningRequest", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "GenerateCertificateSigningRequest", targetNamespace = "urn:vim25", className = "vim25.GenerateCertificateSigningRequestRequestType")
    @ResponseWrapper(localName = "GenerateCertificateSigningRequestResponse", targetNamespace = "urn:vim25", className = "vim25.GenerateCertificateSigningRequestResponse")
    public String generateCertificateSigningRequest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "useIpAddressAsCommonName", targetNamespace = "urn:vim25")
        boolean useIpAddressAsCommonName)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param distinguishedName
     * @param _this
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "GenerateCertificateSigningRequestByDn", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "GenerateCertificateSigningRequestByDn", targetNamespace = "urn:vim25", className = "vim25.GenerateCertificateSigningRequestByDnRequestType")
    @ResponseWrapper(localName = "GenerateCertificateSigningRequestByDnResponse", targetNamespace = "urn:vim25", className = "vim25.GenerateCertificateSigningRequestByDnResponse")
    public String generateCertificateSigningRequestByDn(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "distinguishedName", targetNamespace = "urn:vim25")
        String distinguishedName)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param cert
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "InstallServerCertificate", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "InstallServerCertificate", targetNamespace = "urn:vim25", className = "vim25.InstallServerCertificateRequestType")
    @ResponseWrapper(localName = "InstallServerCertificateResponse", targetNamespace = "urn:vim25", className = "vim25.InstallServerCertificateResponse")
    public void installServerCertificate(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "cert", targetNamespace = "urn:vim25")
        String cert)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param caCert
     * @param _this
     * @param caCrl
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "ReplaceCACertificatesAndCRLs", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ReplaceCACertificatesAndCRLs", targetNamespace = "urn:vim25", className = "vim25.ReplaceCACertificatesAndCRLsRequestType")
    @ResponseWrapper(localName = "ReplaceCACertificatesAndCRLsResponse", targetNamespace = "urn:vim25", className = "vim25.ReplaceCACertificatesAndCRLsResponse")
    public void replaceCACertificatesAndCRLs(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "caCert", targetNamespace = "urn:vim25")
        List<String> caCert,
        @WebParam(name = "caCrl", targetNamespace = "urn:vim25")
        List<String> caCrl)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.util.List<java.lang.String>
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "ListCACertificates", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ListCACertificates", targetNamespace = "urn:vim25", className = "vim25.ListCACertificatesRequestType")
    @ResponseWrapper(localName = "ListCACertificatesResponse", targetNamespace = "urn:vim25", className = "vim25.ListCACertificatesResponse")
    public List<String> listCACertificates(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.util.List<java.lang.String>
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "ListCACertificateRevocationLists", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ListCACertificateRevocationLists", targetNamespace = "urn:vim25", className = "vim25.ListCACertificateRevocationListsRequestType")
    @ResponseWrapper(localName = "ListCACertificateRevocationListsResponse", targetNamespace = "urn:vim25", className = "vim25.ListCACertificateRevocationListsResponse")
    public List<String> listCACertificateRevocationLists(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "EnableHyperThreading", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "EnableHyperThreading", targetNamespace = "urn:vim25", className = "vim25.EnableHyperThreadingRequestType")
    @ResponseWrapper(localName = "EnableHyperThreadingResponse", targetNamespace = "urn:vim25", className = "vim25.EnableHyperThreadingResponse")
    public void enableHyperThreading(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "DisableHyperThreading", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DisableHyperThreading", targetNamespace = "urn:vim25", className = "vim25.DisableHyperThreadingRequestType")
    @ResponseWrapper(localName = "DisableHyperThreadingResponse", targetNamespace = "urn:vim25", className = "vim25.DisableHyperThreadingResponse")
    public void disableHyperThreading(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param searchSpec
     * @param datastorePath
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "SearchDatastore_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "SearchDatastore_Task", targetNamespace = "urn:vim25", className = "vim25.SearchDatastoreRequestType")
    @ResponseWrapper(localName = "SearchDatastore_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.SearchDatastoreTaskResponse")
    public ManagedObjectReference searchDatastoreTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "datastorePath", targetNamespace = "urn:vim25")
        String datastorePath,
        @WebParam(name = "searchSpec", targetNamespace = "urn:vim25")
        HostDatastoreBrowserSearchSpec searchSpec)
        throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param searchSpec
     * @param datastorePath
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "SearchDatastoreSubFolders_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "SearchDatastoreSubFolders_Task", targetNamespace = "urn:vim25", className = "vim25.SearchDatastoreSubFoldersRequestType")
    @ResponseWrapper(localName = "SearchDatastoreSubFolders_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.SearchDatastoreSubFoldersTaskResponse")
    public ManagedObjectReference searchDatastoreSubFoldersTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "datastorePath", targetNamespace = "urn:vim25")
        String datastorePath,
        @WebParam(name = "searchSpec", targetNamespace = "urn:vim25")
        HostDatastoreBrowserSearchSpec searchSpec)
        throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param datastorePath
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws FileFaultFaultMsg
     * @throws InvalidDatastoreFaultMsg
     */
    @WebMethod(operationName = "DeleteFile", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DeleteFile", targetNamespace = "urn:vim25", className = "vim25.DeleteFileRequestType")
    @ResponseWrapper(localName = "DeleteFileResponse", targetNamespace = "urn:vim25", className = "vim25.DeleteFileResponse")
    public void deleteFile(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "datastorePath", targetNamespace = "urn:vim25")
        String datastorePath)
        throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param datastore
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InaccessibleDatastoreFaultMsg
     * @throws DatastoreNotWritableOnHostFaultMsg
     */
    @WebMethod(operationName = "UpdateLocalSwapDatastore", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateLocalSwapDatastore", targetNamespace = "urn:vim25", className = "vim25.UpdateLocalSwapDatastoreRequestType")
    @ResponseWrapper(localName = "UpdateLocalSwapDatastoreResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateLocalSwapDatastoreResponse")
    public void updateLocalSwapDatastore(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "datastore", targetNamespace = "urn:vim25")
        ManagedObjectReference datastore)
        throws DatastoreNotWritableOnHostFaultMsg, InaccessibleDatastoreFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param datastore
     * @param _this
     * @return
     *     returns java.util.List<vim25.HostScsiDisk>
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "QueryAvailableDisksForVmfs", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryAvailableDisksForVmfs", targetNamespace = "urn:vim25", className = "vim25.QueryAvailableDisksForVmfsRequestType")
    @ResponseWrapper(localName = "QueryAvailableDisksForVmfsResponse", targetNamespace = "urn:vim25", className = "vim25.QueryAvailableDisksForVmfsResponse")
    public List<HostScsiDisk> queryAvailableDisksForVmfs(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "datastore", targetNamespace = "urn:vim25")
        ManagedObjectReference datastore)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vmfsMajorVersion
     * @param devicePath
     * @param _this
     * @return
     *     returns java.util.List<vim25.VmfsDatastoreOption>
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "QueryVmfsDatastoreCreateOptions", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryVmfsDatastoreCreateOptions", targetNamespace = "urn:vim25", className = "vim25.QueryVmfsDatastoreCreateOptionsRequestType")
    @ResponseWrapper(localName = "QueryVmfsDatastoreCreateOptionsResponse", targetNamespace = "urn:vim25", className = "vim25.QueryVmfsDatastoreCreateOptionsResponse")
    public List<VmfsDatastoreOption> queryVmfsDatastoreCreateOptions(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "devicePath", targetNamespace = "urn:vim25")
        String devicePath,
        @WebParam(name = "vmfsMajorVersion", targetNamespace = "urn:vim25")
        Integer vmfsMajorVersion)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "CreateVmfsDatastore", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateVmfsDatastore", targetNamespace = "urn:vim25", className = "vim25.CreateVmfsDatastoreRequestType")
    @ResponseWrapper(localName = "CreateVmfsDatastoreResponse", targetNamespace = "urn:vim25", className = "vim25.CreateVmfsDatastoreResponse")
    public ManagedObjectReference createVmfsDatastore(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        VmfsDatastoreCreateSpec spec)
        throws DuplicateNameFaultMsg, HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param datastore
     * @param devicePath
     * @param _this
     * @param suppressExpandCandidates
     * @return
     *     returns java.util.List<vim25.VmfsDatastoreOption>
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "QueryVmfsDatastoreExtendOptions", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryVmfsDatastoreExtendOptions", targetNamespace = "urn:vim25", className = "vim25.QueryVmfsDatastoreExtendOptionsRequestType")
    @ResponseWrapper(localName = "QueryVmfsDatastoreExtendOptionsResponse", targetNamespace = "urn:vim25", className = "vim25.QueryVmfsDatastoreExtendOptionsResponse")
    public List<VmfsDatastoreOption> queryVmfsDatastoreExtendOptions(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "datastore", targetNamespace = "urn:vim25")
        ManagedObjectReference datastore,
        @WebParam(name = "devicePath", targetNamespace = "urn:vim25")
        String devicePath,
        @WebParam(name = "suppressExpandCandidates", targetNamespace = "urn:vim25")
        Boolean suppressExpandCandidates)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param datastore
     * @param _this
     * @return
     *     returns java.util.List<vim25.VmfsDatastoreOption>
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "QueryVmfsDatastoreExpandOptions", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryVmfsDatastoreExpandOptions", targetNamespace = "urn:vim25", className = "vim25.QueryVmfsDatastoreExpandOptionsRequestType")
    @ResponseWrapper(localName = "QueryVmfsDatastoreExpandOptionsResponse", targetNamespace = "urn:vim25", className = "vim25.QueryVmfsDatastoreExpandOptionsResponse")
    public List<VmfsDatastoreOption> queryVmfsDatastoreExpandOptions(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "datastore", targetNamespace = "urn:vim25")
        ManagedObjectReference datastore)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param datastore
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "ExtendVmfsDatastore", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ExtendVmfsDatastore", targetNamespace = "urn:vim25", className = "vim25.ExtendVmfsDatastoreRequestType")
    @ResponseWrapper(localName = "ExtendVmfsDatastoreResponse", targetNamespace = "urn:vim25", className = "vim25.ExtendVmfsDatastoreResponse")
    public ManagedObjectReference extendVmfsDatastore(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "datastore", targetNamespace = "urn:vim25")
        ManagedObjectReference datastore,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        VmfsDatastoreExtendSpec spec)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param datastore
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "ExpandVmfsDatastore", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ExpandVmfsDatastore", targetNamespace = "urn:vim25", className = "vim25.ExpandVmfsDatastoreRequestType")
    @ResponseWrapper(localName = "ExpandVmfsDatastoreResponse", targetNamespace = "urn:vim25", className = "vim25.ExpandVmfsDatastoreResponse")
    public ManagedObjectReference expandVmfsDatastore(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "datastore", targetNamespace = "urn:vim25")
        ManagedObjectReference datastore,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        VmfsDatastoreExpandSpec spec)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws AlreadyExistsFaultMsg
     * @throws HostConfigFaultFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "CreateNasDatastore", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateNasDatastore", targetNamespace = "urn:vim25", className = "vim25.CreateNasDatastoreRequestType")
    @ResponseWrapper(localName = "CreateNasDatastoreResponse", targetNamespace = "urn:vim25", className = "vim25.CreateNasDatastoreResponse")
    public ManagedObjectReference createNasDatastore(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        HostNasVolumeSpec spec)
        throws AlreadyExistsFaultMsg, DuplicateNameFaultMsg, HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param path
     * @param name
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     * @throws FileNotFoundFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "CreateLocalDatastore", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateLocalDatastore", targetNamespace = "urn:vim25", className = "vim25.CreateLocalDatastoreRequestType")
    @ResponseWrapper(localName = "CreateLocalDatastoreResponse", targetNamespace = "urn:vim25", className = "vim25.CreateLocalDatastoreResponse")
    public ManagedObjectReference createLocalDatastore(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "path", targetNamespace = "urn:vim25")
        String path)
        throws DuplicateNameFaultMsg, FileNotFoundFaultMsg, HostConfigFaultFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "CreateVvolDatastore", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateVvolDatastore", targetNamespace = "urn:vim25", className = "vim25.CreateVvolDatastoreRequestType")
    @ResponseWrapper(localName = "CreateVvolDatastoreResponse", targetNamespace = "urn:vim25", className = "vim25.CreateVvolDatastoreResponse")
    public ManagedObjectReference createVvolDatastore(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        HostDatastoreSystemVvolDatastoreSpec spec)
        throws DuplicateNameFaultMsg, HostConfigFaultFaultMsg, InvalidNameFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param datastore
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws ResourceInUseFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "RemoveDatastore", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RemoveDatastore", targetNamespace = "urn:vim25", className = "vim25.RemoveDatastoreRequestType")
    @ResponseWrapper(localName = "RemoveDatastoreResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveDatastoreResponse")
    public void removeDatastore(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "datastore", targetNamespace = "urn:vim25")
        ManagedObjectReference datastore)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param datastore
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "RemoveDatastoreEx_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RemoveDatastoreEx_Task", targetNamespace = "urn:vim25", className = "vim25.RemoveDatastoreExRequestType")
    @ResponseWrapper(localName = "RemoveDatastoreEx_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveDatastoreExTaskResponse")
    public ManagedObjectReference removeDatastoreExTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "datastore", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> datastore)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param password
     * @param _this
     * @param userName
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "ConfigureDatastorePrincipal", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ConfigureDatastorePrincipal", targetNamespace = "urn:vim25", className = "vim25.ConfigureDatastorePrincipalRequestType")
    @ResponseWrapper(localName = "ConfigureDatastorePrincipalResponse", targetNamespace = "urn:vim25", className = "vim25.ConfigureDatastorePrincipalResponse")
    public void configureDatastorePrincipal(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "userName", targetNamespace = "urn:vim25")
        String userName,
        @WebParam(name = "password", targetNamespace = "urn:vim25")
        String password)
        throws HostConfigFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.util.List<vim25.HostUnresolvedVmfsVolume>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryUnresolvedVmfsVolumes", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryUnresolvedVmfsVolumes", targetNamespace = "urn:vim25", className = "vim25.QueryUnresolvedVmfsVolumesRequestType")
    @ResponseWrapper(localName = "QueryUnresolvedVmfsVolumesResponse", targetNamespace = "urn:vim25", className = "vim25.QueryUnresolvedVmfsVolumesResponse")
    public List<HostUnresolvedVmfsVolume> queryUnresolvedVmfsVolumes(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param resolutionSpec
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     * @throws VmfsAmbiguousMountFaultMsg
     */
    @WebMethod(operationName = "ResignatureUnresolvedVmfsVolume_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ResignatureUnresolvedVmfsVolume_Task", targetNamespace = "urn:vim25", className = "vim25.ResignatureUnresolvedVmfsVolumeRequestType")
    @ResponseWrapper(localName = "ResignatureUnresolvedVmfsVolume_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ResignatureUnresolvedVmfsVolumeTaskResponse")
    public ManagedObjectReference resignatureUnresolvedVmfsVolumeTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "resolutionSpec", targetNamespace = "urn:vim25")
        HostUnresolvedVmfsResignatureSpec resolutionSpec)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg, VmfsAmbiguousMountFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param config
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateDateTimeConfig", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateDateTimeConfig", targetNamespace = "urn:vim25", className = "vim25.UpdateDateTimeConfigRequestType")
    @ResponseWrapper(localName = "UpdateDateTimeConfigResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateDateTimeConfigResponse")
    public void updateDateTimeConfig(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "config", targetNamespace = "urn:vim25")
        HostDateTimeConfig config)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.util.List<vim25.HostDateTimeSystemTimeZone>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryAvailableTimeZones", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryAvailableTimeZones", targetNamespace = "urn:vim25", className = "vim25.QueryAvailableTimeZonesRequestType")
    @ResponseWrapper(localName = "QueryAvailableTimeZonesResponse", targetNamespace = "urn:vim25", className = "vim25.QueryAvailableTimeZonesResponse")
    public List<HostDateTimeSystemTimeZone> queryAvailableTimeZones(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.util.Calendar
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryDateTime", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryDateTime", targetNamespace = "urn:vim25", className = "vim25.QueryDateTimeRequestType")
    @ResponseWrapper(localName = "QueryDateTimeResponse", targetNamespace = "urn:vim25", className = "vim25.QueryDateTimeResponse")
    public Calendar queryDateTime(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param dateTime
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateDateTime", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateDateTime", targetNamespace = "urn:vim25", className = "vim25.UpdateDateTimeRequestType")
    @ResponseWrapper(localName = "UpdateDateTimeResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateDateTimeResponse")
    public void updateDateTime(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "dateTime", targetNamespace = "urn:vim25")
        Calendar dateTime)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RefreshDateTimeSystem", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RefreshDateTimeSystem", targetNamespace = "urn:vim25", className = "vim25.RefreshDateTimeSystemRequestType")
    @ResponseWrapper(localName = "RefreshDateTimeSystemResponse", targetNamespace = "urn:vim25", className = "vim25.RefreshDateTimeSystemResponse")
    public void refreshDateTimeSystem(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.util.List<vim25.HostDiagnosticPartition>
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "QueryAvailablePartition", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryAvailablePartition", targetNamespace = "urn:vim25", className = "vim25.QueryAvailablePartitionRequestType")
    @ResponseWrapper(localName = "QueryAvailablePartitionResponse", targetNamespace = "urn:vim25", className = "vim25.QueryAvailablePartitionResponse")
    public List<HostDiagnosticPartition> queryAvailablePartition(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param partition
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "SelectActivePartition", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "SelectActivePartition", targetNamespace = "urn:vim25", className = "vim25.SelectActivePartitionRequestType")
    @ResponseWrapper(localName = "SelectActivePartitionResponse", targetNamespace = "urn:vim25", className = "vim25.SelectActivePartitionResponse")
    public void selectActivePartition(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "partition", targetNamespace = "urn:vim25")
        HostScsiDiskPartition partition)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param diagnosticType
     * @param storageType
     * @param _this
     * @return
     *     returns java.util.List<vim25.HostDiagnosticPartitionCreateOption>
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "QueryPartitionCreateOptions", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryPartitionCreateOptions", targetNamespace = "urn:vim25", className = "vim25.QueryPartitionCreateOptionsRequestType")
    @ResponseWrapper(localName = "QueryPartitionCreateOptionsResponse", targetNamespace = "urn:vim25", className = "vim25.QueryPartitionCreateOptionsResponse")
    public List<HostDiagnosticPartitionCreateOption> queryPartitionCreateOptions(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "storageType", targetNamespace = "urn:vim25")
        String storageType,
        @WebParam(name = "diagnosticType", targetNamespace = "urn:vim25")
        String diagnosticType)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param diagnosticType
     * @param diskUuid
     * @param _this
     * @return
     *     returns vim25.HostDiagnosticPartitionCreateDescription
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "QueryPartitionCreateDesc", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryPartitionCreateDesc", targetNamespace = "urn:vim25", className = "vim25.QueryPartitionCreateDescRequestType")
    @ResponseWrapper(localName = "QueryPartitionCreateDescResponse", targetNamespace = "urn:vim25", className = "vim25.QueryPartitionCreateDescResponse")
    public HostDiagnosticPartitionCreateDescription queryPartitionCreateDesc(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "diskUuid", targetNamespace = "urn:vim25")
        String diskUuid,
        @WebParam(name = "diagnosticType", targetNamespace = "urn:vim25")
        String diagnosticType)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "CreateDiagnosticPartition", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "CreateDiagnosticPartition", targetNamespace = "urn:vim25", className = "vim25.CreateDiagnosticPartitionRequestType")
    @ResponseWrapper(localName = "CreateDiagnosticPartitionResponse", targetNamespace = "urn:vim25", className = "vim25.CreateDiagnosticPartitionResponse")
    public void createDiagnosticPartition(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        HostDiagnosticPartitionCreateSpec spec)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param configInfo
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "EsxAgentHostManagerUpdateConfig", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "EsxAgentHostManagerUpdateConfig", targetNamespace = "urn:vim25", className = "vim25.EsxAgentHostManagerUpdateConfigRequestType")
    @ResponseWrapper(localName = "EsxAgentHostManagerUpdateConfigResponse", targetNamespace = "urn:vim25", className = "vim25.EsxAgentHostManagerUpdateConfigResponse")
    public void esxAgentHostManagerUpdateConfig(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "configInfo", targetNamespace = "urn:vim25")
        HostEsxAgentHostManagerConfigInfo configInfo)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param defaultPolicy
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateDefaultPolicy", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateDefaultPolicy", targetNamespace = "urn:vim25", className = "vim25.UpdateDefaultPolicyRequestType")
    @ResponseWrapper(localName = "UpdateDefaultPolicyResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateDefaultPolicyResponse")
    public void updateDefaultPolicy(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "defaultPolicy", targetNamespace = "urn:vim25")
        HostFirewallDefaultPolicy defaultPolicy)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param id
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "EnableRuleset", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "EnableRuleset", targetNamespace = "urn:vim25", className = "vim25.EnableRulesetRequestType")
    @ResponseWrapper(localName = "EnableRulesetResponse", targetNamespace = "urn:vim25", className = "vim25.EnableRulesetResponse")
    public void enableRuleset(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "id", targetNamespace = "urn:vim25")
        String id)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param id
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "DisableRuleset", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DisableRuleset", targetNamespace = "urn:vim25", className = "vim25.DisableRulesetRequestType")
    @ResponseWrapper(localName = "DisableRulesetResponse", targetNamespace = "urn:vim25", className = "vim25.DisableRulesetResponse")
    public void disableRuleset(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "id", targetNamespace = "urn:vim25")
        String id)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param id
     * @param _this
     * @param spec
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateRuleset", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateRuleset", targetNamespace = "urn:vim25", className = "vim25.UpdateRulesetRequestType")
    @ResponseWrapper(localName = "UpdateRulesetResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateRulesetResponse")
    public void updateRuleset(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "id", targetNamespace = "urn:vim25")
        String id,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        HostFirewallRulesetRulesetSpec spec)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RefreshFirewall", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RefreshFirewall", targetNamespace = "urn:vim25", className = "vim25.RefreshFirewallRequestType")
    @ResponseWrapper(localName = "RefreshFirewallResponse", targetNamespace = "urn:vim25", className = "vim25.RefreshFirewallResponse")
    public void refreshFirewall(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "ResetFirmwareToFactoryDefaults", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ResetFirmwareToFactoryDefaults", targetNamespace = "urn:vim25", className = "vim25.ResetFirmwareToFactoryDefaultsRequestType")
    @ResponseWrapper(localName = "ResetFirmwareToFactoryDefaultsResponse", targetNamespace = "urn:vim25", className = "vim25.ResetFirmwareToFactoryDefaultsResponse")
    public void resetFirmwareToFactoryDefaults(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "BackupFirmwareConfiguration", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "BackupFirmwareConfiguration", targetNamespace = "urn:vim25", className = "vim25.BackupFirmwareConfigurationRequestType")
    @ResponseWrapper(localName = "BackupFirmwareConfigurationResponse", targetNamespace = "urn:vim25", className = "vim25.BackupFirmwareConfigurationResponse")
    public String backupFirmwareConfiguration(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryFirmwareConfigUploadURL", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryFirmwareConfigUploadURL", targetNamespace = "urn:vim25", className = "vim25.QueryFirmwareConfigUploadURLRequestType")
    @ResponseWrapper(localName = "QueryFirmwareConfigUploadURLResponse", targetNamespace = "urn:vim25", className = "vim25.QueryFirmwareConfigUploadURLResponse")
    public String queryFirmwareConfigUploadURL(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param force
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidBundleFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     * @throws MismatchedBundleFaultMsg
     */
    @WebMethod(operationName = "RestoreFirmwareConfiguration", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RestoreFirmwareConfiguration", targetNamespace = "urn:vim25", className = "vim25.RestoreFirmwareConfigurationRequestType")
    @ResponseWrapper(localName = "RestoreFirmwareConfigurationResponse", targetNamespace = "urn:vim25", className = "vim25.RestoreFirmwareConfigurationResponse")
    public void restoreFirmwareConfiguration(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "force", targetNamespace = "urn:vim25")
        boolean force)
        throws FileFaultFaultMsg, InvalidBundleFaultMsg, InvalidStateFaultMsg, MismatchedBundleFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RefreshGraphicsManager", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RefreshGraphicsManager", targetNamespace = "urn:vim25", className = "vim25.RefreshGraphicsManagerRequestType")
    @ResponseWrapper(localName = "RefreshGraphicsManagerResponse", targetNamespace = "urn:vim25", className = "vim25.RefreshGraphicsManagerResponse")
    public void refreshGraphicsManager(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns boolean
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "IsSharedGraphicsActive", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "IsSharedGraphicsActive", targetNamespace = "urn:vim25", className = "vim25.IsSharedGraphicsActiveRequestType")
    @ResponseWrapper(localName = "IsSharedGraphicsActiveResponse", targetNamespace = "urn:vim25", className = "vim25.IsSharedGraphicsActiveResponse")
    public boolean isSharedGraphicsActive(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RefreshHealthStatusSystem", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RefreshHealthStatusSystem", targetNamespace = "urn:vim25", className = "vim25.RefreshHealthStatusSystemRequestType")
    @ResponseWrapper(localName = "RefreshHealthStatusSystemResponse", targetNamespace = "urn:vim25", className = "vim25.RefreshHealthStatusSystemResponse")
    public void refreshHealthStatusSystem(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ResetSystemHealthInfo", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ResetSystemHealthInfo", targetNamespace = "urn:vim25", className = "vim25.ResetSystemHealthInfoRequestType")
    @ResponseWrapper(localName = "ResetSystemHealthInfoResponse", targetNamespace = "urn:vim25", className = "vim25.ResetSystemHealthInfoResponse")
    public void resetSystemHealthInfo(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.util.List<vim25.HostAccessControlEntry>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RetrieveHostAccessControlEntries", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RetrieveHostAccessControlEntries", targetNamespace = "urn:vim25", className = "vim25.RetrieveHostAccessControlEntriesRequestType")
    @ResponseWrapper(localName = "RetrieveHostAccessControlEntriesResponse", targetNamespace = "urn:vim25", className = "vim25.RetrieveHostAccessControlEntriesResponse")
    public List<HostAccessControlEntry> retrieveHostAccessControlEntries(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param principal
     * @param _this
     * @param isGroup
     * @param accessMode
     * @throws UserNotFoundFaultMsg
     * @throws RuntimeFaultFaultMsg
     * @throws AuthMinimumAdminPermissionFaultMsg
     */
    @WebMethod(operationName = "ChangeAccessMode", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ChangeAccessMode", targetNamespace = "urn:vim25", className = "vim25.ChangeAccessModeRequestType")
    @ResponseWrapper(localName = "ChangeAccessModeResponse", targetNamespace = "urn:vim25", className = "vim25.ChangeAccessModeResponse")
    public void changeAccessMode(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "principal", targetNamespace = "urn:vim25")
        String principal,
        @WebParam(name = "isGroup", targetNamespace = "urn:vim25")
        boolean isGroup,
        @WebParam(name = "accessMode", targetNamespace = "urn:vim25")
        HostAccessMode accessMode)
        throws AuthMinimumAdminPermissionFaultMsg, RuntimeFaultFaultMsg, UserNotFoundFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.util.List<java.lang.String>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QuerySystemUsers", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QuerySystemUsers", targetNamespace = "urn:vim25", className = "vim25.QuerySystemUsersRequestType")
    @ResponseWrapper(localName = "QuerySystemUsersResponse", targetNamespace = "urn:vim25", className = "vim25.QuerySystemUsersResponse")
    public List<String> querySystemUsers(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param users
     * @throws UserNotFoundFaultMsg
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateSystemUsers", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateSystemUsers", targetNamespace = "urn:vim25", className = "vim25.UpdateSystemUsersRequestType")
    @ResponseWrapper(localName = "UpdateSystemUsersResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateSystemUsersResponse")
    public void updateSystemUsers(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "users", targetNamespace = "urn:vim25")
        List<String> users)
        throws RuntimeFaultFaultMsg, UserNotFoundFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.util.List<java.lang.String>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryLockdownExceptions", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryLockdownExceptions", targetNamespace = "urn:vim25", className = "vim25.QueryLockdownExceptionsRequestType")
    @ResponseWrapper(localName = "QueryLockdownExceptionsResponse", targetNamespace = "urn:vim25", className = "vim25.QueryLockdownExceptionsResponse")
    public List<String> queryLockdownExceptions(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param users
     * @throws UserNotFoundFaultMsg
     * @throws RuntimeFaultFaultMsg
     * @throws AuthMinimumAdminPermissionFaultMsg
     */
    @WebMethod(operationName = "UpdateLockdownExceptions", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateLockdownExceptions", targetNamespace = "urn:vim25", className = "vim25.UpdateLockdownExceptionsRequestType")
    @ResponseWrapper(localName = "UpdateLockdownExceptionsResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateLockdownExceptionsResponse")
    public void updateLockdownExceptions(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "users", targetNamespace = "urn:vim25")
        List<String> users)
        throws AuthMinimumAdminPermissionFaultMsg, RuntimeFaultFaultMsg, UserNotFoundFaultMsg
    ;

    /**
     * 
     * @param mode
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws AuthMinimumAdminPermissionFaultMsg
     */
    @WebMethod(operationName = "ChangeLockdownMode", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ChangeLockdownMode", targetNamespace = "urn:vim25", className = "vim25.ChangeLockdownModeRequestType")
    @ResponseWrapper(localName = "ChangeLockdownModeResponse", targetNamespace = "urn:vim25", className = "vim25.ChangeLockdownModeResponse")
    public void changeLockdownMode(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "mode", targetNamespace = "urn:vim25")
        HostLockdownMode mode)
        throws AuthMinimumAdminPermissionFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "HostImageConfigGetAcceptance", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "HostImageConfigGetAcceptance", targetNamespace = "urn:vim25", className = "vim25.HostImageConfigGetAcceptanceRequestType")
    @ResponseWrapper(localName = "HostImageConfigGetAcceptanceResponse", targetNamespace = "urn:vim25", className = "vim25.HostImageConfigGetAcceptanceResponse")
    public String hostImageConfigGetAcceptance(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.HostImageProfileSummary
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "HostImageConfigGetProfile", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "HostImageConfigGetProfile", targetNamespace = "urn:vim25", className = "vim25.HostImageConfigGetProfileRequestType")
    @ResponseWrapper(localName = "HostImageConfigGetProfileResponse", targetNamespace = "urn:vim25", className = "vim25.HostImageConfigGetProfileResponse")
    public HostImageProfileSummary hostImageConfigGetProfile(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param newAcceptanceLevel
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateHostImageAcceptanceLevel", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateHostImageAcceptanceLevel", targetNamespace = "urn:vim25", className = "vim25.UpdateHostImageAcceptanceLevelRequestType")
    @ResponseWrapper(localName = "UpdateHostImageAcceptanceLevelResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateHostImageAcceptanceLevelResponse")
    public void updateHostImageAcceptanceLevel(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "newAcceptanceLevel", targetNamespace = "urn:vim25")
        String newAcceptanceLevel)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param vnicDevice
     * @return
     *     returns vim25.IscsiStatus
     * @throws RuntimeFaultFaultMsg
     * @throws IscsiFaultFaultMsg
     */
    @WebMethod(operationName = "QueryVnicStatus", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryVnicStatus", targetNamespace = "urn:vim25", className = "vim25.QueryVnicStatusRequestType")
    @ResponseWrapper(localName = "QueryVnicStatusResponse", targetNamespace = "urn:vim25", className = "vim25.QueryVnicStatusResponse")
    public IscsiStatus queryVnicStatus(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vnicDevice", targetNamespace = "urn:vim25")
        String vnicDevice)
        throws IscsiFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param pnicDevice
     * @param _this
     * @return
     *     returns vim25.IscsiStatus
     * @throws RuntimeFaultFaultMsg
     * @throws IscsiFaultFaultMsg
     */
    @WebMethod(operationName = "QueryPnicStatus", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryPnicStatus", targetNamespace = "urn:vim25", className = "vim25.QueryPnicStatusRequestType")
    @ResponseWrapper(localName = "QueryPnicStatusResponse", targetNamespace = "urn:vim25", className = "vim25.QueryPnicStatusResponse")
    public IscsiStatus queryPnicStatus(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "pnicDevice", targetNamespace = "urn:vim25")
        String pnicDevice)
        throws IscsiFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param iScsiHbaName
     * @param _this
     * @return
     *     returns java.util.List<vim25.IscsiPortInfo>
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws IscsiFaultFaultMsg
     */
    @WebMethod(operationName = "QueryBoundVnics", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryBoundVnics", targetNamespace = "urn:vim25", className = "vim25.QueryBoundVnicsRequestType")
    @ResponseWrapper(localName = "QueryBoundVnicsResponse", targetNamespace = "urn:vim25", className = "vim25.QueryBoundVnicsResponse")
    public List<IscsiPortInfo> queryBoundVnics(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "iScsiHbaName", targetNamespace = "urn:vim25")
        String iScsiHbaName)
        throws IscsiFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param iScsiHbaName
     * @param _this
     * @return
     *     returns java.util.List<vim25.IscsiPortInfo>
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws IscsiFaultFaultMsg
     */
    @WebMethod(operationName = "QueryCandidateNics", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryCandidateNics", targetNamespace = "urn:vim25", className = "vim25.QueryCandidateNicsRequestType")
    @ResponseWrapper(localName = "QueryCandidateNicsResponse", targetNamespace = "urn:vim25", className = "vim25.QueryCandidateNicsResponse")
    public List<IscsiPortInfo> queryCandidateNics(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "iScsiHbaName", targetNamespace = "urn:vim25")
        String iScsiHbaName)
        throws IscsiFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param iScsiHbaName
     * @param _this
     * @param vnicDevice
     * @throws RuntimeFaultFaultMsg
     * @throws IscsiFaultVnicHasMultipleUplinksFaultMsg
     * @throws IscsiFaultVnicHasNoUplinksFaultMsg
     * @throws NotFoundFaultMsg
     * @throws PlatformConfigFaultFaultMsg
     * @throws IscsiFaultVnicAlreadyBoundFaultMsg
     * @throws IscsiFaultVnicNotFoundFaultMsg
     * @throws IscsiFaultInvalidVnicFaultMsg
     * @throws IscsiFaultFaultMsg
     * @throws IscsiFaultVnicHasWrongUplinkFaultMsg
     */
    @WebMethod(operationName = "BindVnic", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "BindVnic", targetNamespace = "urn:vim25", className = "vim25.BindVnicRequestType")
    @ResponseWrapper(localName = "BindVnicResponse", targetNamespace = "urn:vim25", className = "vim25.BindVnicResponse")
    public void bindVnic(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "iScsiHbaName", targetNamespace = "urn:vim25")
        String iScsiHbaName,
        @WebParam(name = "vnicDevice", targetNamespace = "urn:vim25")
        String vnicDevice)
        throws IscsiFaultFaultMsg, IscsiFaultInvalidVnicFaultMsg, IscsiFaultVnicAlreadyBoundFaultMsg, IscsiFaultVnicHasMultipleUplinksFaultMsg, IscsiFaultVnicHasNoUplinksFaultMsg, IscsiFaultVnicHasWrongUplinkFaultMsg, IscsiFaultVnicNotFoundFaultMsg, NotFoundFaultMsg, PlatformConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param iScsiHbaName
     * @param force
     * @param _this
     * @param vnicDevice
     * @throws RuntimeFaultFaultMsg
     * @throws IscsiFaultVnicHasActivePathsFaultMsg
     * @throws IscsiFaultVnicNotBoundFaultMsg
     * @throws NotFoundFaultMsg
     * @throws PlatformConfigFaultFaultMsg
     * @throws IscsiFaultFaultMsg
     * @throws IscsiFaultVnicIsLastPathFaultMsg
     */
    @WebMethod(operationName = "UnbindVnic", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UnbindVnic", targetNamespace = "urn:vim25", className = "vim25.UnbindVnicRequestType")
    @ResponseWrapper(localName = "UnbindVnicResponse", targetNamespace = "urn:vim25", className = "vim25.UnbindVnicResponse")
    public void unbindVnic(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "iScsiHbaName", targetNamespace = "urn:vim25")
        String iScsiHbaName,
        @WebParam(name = "vnicDevice", targetNamespace = "urn:vim25")
        String vnicDevice,
        @WebParam(name = "force", targetNamespace = "urn:vim25")
        boolean force)
        throws IscsiFaultFaultMsg, IscsiFaultVnicHasActivePathsFaultMsg, IscsiFaultVnicIsLastPathFaultMsg, IscsiFaultVnicNotBoundFaultMsg, NotFoundFaultMsg, PlatformConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param pnicDevice
     * @param _this
     * @return
     *     returns vim25.IscsiMigrationDependency
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryMigrationDependencies", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryMigrationDependencies", targetNamespace = "urn:vim25", className = "vim25.QueryMigrationDependenciesRequestType")
    @ResponseWrapper(localName = "QueryMigrationDependenciesResponse", targetNamespace = "urn:vim25", className = "vim25.QueryMigrationDependenciesResponse")
    public IscsiMigrationDependency queryMigrationDependencies(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "pnicDevice", targetNamespace = "urn:vim25")
        List<String> pnicDevice)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.util.List<vim25.KernelModuleInfo>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryModules", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryModules", targetNamespace = "urn:vim25", className = "vim25.QueryModulesRequestType")
    @ResponseWrapper(localName = "QueryModulesResponse", targetNamespace = "urn:vim25", className = "vim25.QueryModulesResponse")
    public List<KernelModuleInfo> queryModules(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param options
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "UpdateModuleOptionString", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateModuleOptionString", targetNamespace = "urn:vim25", className = "vim25.UpdateModuleOptionStringRequestType")
    @ResponseWrapper(localName = "UpdateModuleOptionStringResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateModuleOptionStringResponse")
    public void updateModuleOptionString(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "options", targetNamespace = "urn:vim25")
        String options)
        throws NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param _this
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "QueryConfiguredModuleOptionString", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryConfiguredModuleOptionString", targetNamespace = "urn:vim25", className = "vim25.QueryConfiguredModuleOptionStringRequestType")
    @ResponseWrapper(localName = "QueryConfiguredModuleOptionStringResponse", targetNamespace = "urn:vim25", className = "vim25.QueryConfiguredModuleOptionStringResponse")
    public String queryConfiguredModuleOptionString(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name)
        throws NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param user
     * @throws RuntimeFaultFaultMsg
     * @throws AlreadyExistsFaultMsg
     */
    @WebMethod(operationName = "CreateUser", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "CreateUser", targetNamespace = "urn:vim25", className = "vim25.CreateUserRequestType")
    @ResponseWrapper(localName = "CreateUserResponse", targetNamespace = "urn:vim25", className = "vim25.CreateUserResponse")
    public void createUser(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "user", targetNamespace = "urn:vim25")
        HostAccountSpec user)
        throws AlreadyExistsFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param user
     * @throws UserNotFoundFaultMsg
     * @throws RuntimeFaultFaultMsg
     * @throws AlreadyExistsFaultMsg
     */
    @WebMethod(operationName = "UpdateUser", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateUser", targetNamespace = "urn:vim25", className = "vim25.UpdateUserRequestType")
    @ResponseWrapper(localName = "UpdateUserResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateUserResponse")
    public void updateUser(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "user", targetNamespace = "urn:vim25")
        HostAccountSpec user)
        throws AlreadyExistsFaultMsg, RuntimeFaultFaultMsg, UserNotFoundFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param group
     * @throws RuntimeFaultFaultMsg
     * @throws AlreadyExistsFaultMsg
     */
    @WebMethod(operationName = "CreateGroup", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "CreateGroup", targetNamespace = "urn:vim25", className = "vim25.CreateGroupRequestType")
    @ResponseWrapper(localName = "CreateGroupResponse", targetNamespace = "urn:vim25", className = "vim25.CreateGroupResponse")
    public void createGroup(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "group", targetNamespace = "urn:vim25")
        HostAccountSpec group)
        throws AlreadyExistsFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param userName
     * @throws RuntimeFaultFaultMsg
     * @throws UserNotFoundFaultMsg
     */
    @WebMethod(operationName = "RemoveUser", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RemoveUser", targetNamespace = "urn:vim25", className = "vim25.RemoveUserRequestType")
    @ResponseWrapper(localName = "RemoveUserResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveUserResponse")
    public void removeUser(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "userName", targetNamespace = "urn:vim25")
        String userName)
        throws RuntimeFaultFaultMsg, UserNotFoundFaultMsg
    ;

    /**
     * 
     * @param groupName
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws UserNotFoundFaultMsg
     */
    @WebMethod(operationName = "RemoveGroup", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RemoveGroup", targetNamespace = "urn:vim25", className = "vim25.RemoveGroupRequestType")
    @ResponseWrapper(localName = "RemoveGroupResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveGroupResponse")
    public void removeGroup(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "groupName", targetNamespace = "urn:vim25")
        String groupName)
        throws RuntimeFaultFaultMsg, UserNotFoundFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param user
     * @param group
     * @throws RuntimeFaultFaultMsg
     * @throws UserNotFoundFaultMsg
     * @throws AlreadyExistsFaultMsg
     */
    @WebMethod(operationName = "AssignUserToGroup", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "AssignUserToGroup", targetNamespace = "urn:vim25", className = "vim25.AssignUserToGroupRequestType")
    @ResponseWrapper(localName = "AssignUserToGroupResponse", targetNamespace = "urn:vim25", className = "vim25.AssignUserToGroupResponse")
    public void assignUserToGroup(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "user", targetNamespace = "urn:vim25")
        String user,
        @WebParam(name = "group", targetNamespace = "urn:vim25")
        String group)
        throws AlreadyExistsFaultMsg, RuntimeFaultFaultMsg, UserNotFoundFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param user
     * @param group
     * @throws UserNotFoundFaultMsg
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "UnassignUserFromGroup", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UnassignUserFromGroup", targetNamespace = "urn:vim25", className = "vim25.UnassignUserFromGroupRequestType")
    @ResponseWrapper(localName = "UnassignUserFromGroupResponse", targetNamespace = "urn:vim25", className = "vim25.UnassignUserFromGroupResponse")
    public void unassignUserFromGroup(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "user", targetNamespace = "urn:vim25")
        String user,
        @WebParam(name = "group", targetNamespace = "urn:vim25")
        String group)
        throws RuntimeFaultFaultMsg, UserNotFoundFaultMsg
    ;

    /**
     * 
     * @param cfgBytes
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ReconfigureServiceConsoleReservation", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ReconfigureServiceConsoleReservation", targetNamespace = "urn:vim25", className = "vim25.ReconfigureServiceConsoleReservationRequestType")
    @ResponseWrapper(localName = "ReconfigureServiceConsoleReservationResponse", targetNamespace = "urn:vim25", className = "vim25.ReconfigureServiceConsoleReservationResponse")
    public void reconfigureServiceConsoleReservation(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "cfgBytes", targetNamespace = "urn:vim25")
        long cfgBytes)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ReconfigureVirtualMachineReservation", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ReconfigureVirtualMachineReservation", targetNamespace = "urn:vim25", className = "vim25.ReconfigureVirtualMachineReservationRequestType")
    @ResponseWrapper(localName = "ReconfigureVirtualMachineReservationResponse", targetNamespace = "urn:vim25", className = "vim25.ReconfigureVirtualMachineReservationResponse")
    public void reconfigureVirtualMachineReservation(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        VirtualMachineMemoryReservationSpec spec)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param changeMode
     * @param _this
     * @param config
     * @return
     *     returns vim25.HostNetworkConfigResult
     * @throws RuntimeFaultFaultMsg
     * @throws ResourceInUseFaultMsg
     * @throws NotFoundFaultMsg
     * @throws AlreadyExistsFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateNetworkConfig", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "UpdateNetworkConfig", targetNamespace = "urn:vim25", className = "vim25.UpdateNetworkConfigRequestType")
    @ResponseWrapper(localName = "UpdateNetworkConfigResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateNetworkConfigResponse")
    public HostNetworkConfigResult updateNetworkConfig(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "config", targetNamespace = "urn:vim25")
        HostNetworkConfig config,
        @WebParam(name = "changeMode", targetNamespace = "urn:vim25")
        String changeMode)
        throws AlreadyExistsFaultMsg, HostConfigFaultFaultMsg, NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param config
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateDnsConfig", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateDnsConfig", targetNamespace = "urn:vim25", className = "vim25.UpdateDnsConfigRequestType")
    @ResponseWrapper(localName = "UpdateDnsConfigResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateDnsConfigResponse")
    public void updateDnsConfig(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "config", targetNamespace = "urn:vim25")
        HostDnsConfig config)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param config
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateIpRouteConfig", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateIpRouteConfig", targetNamespace = "urn:vim25", className = "vim25.UpdateIpRouteConfigRequestType")
    @ResponseWrapper(localName = "UpdateIpRouteConfigResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateIpRouteConfigResponse")
    public void updateIpRouteConfig(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "config", targetNamespace = "urn:vim25")
        HostIpRouteConfig config)
        throws HostConfigFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param config
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateConsoleIpRouteConfig", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateConsoleIpRouteConfig", targetNamespace = "urn:vim25", className = "vim25.UpdateConsoleIpRouteConfigRequestType")
    @ResponseWrapper(localName = "UpdateConsoleIpRouteConfigResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateConsoleIpRouteConfigResponse")
    public void updateConsoleIpRouteConfig(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "config", targetNamespace = "urn:vim25")
        HostIpRouteConfig config)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param config
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateIpRouteTableConfig", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateIpRouteTableConfig", targetNamespace = "urn:vim25", className = "vim25.UpdateIpRouteTableConfigRequestType")
    @ResponseWrapper(localName = "UpdateIpRouteTableConfigResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateIpRouteTableConfigResponse")
    public void updateIpRouteTableConfig(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "config", targetNamespace = "urn:vim25")
        HostIpRouteTableConfig config)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vswitchName
     * @param _this
     * @param spec
     * @throws RuntimeFaultFaultMsg
     * @throws ResourceInUseFaultMsg
     * @throws AlreadyExistsFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "AddVirtualSwitch", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "AddVirtualSwitch", targetNamespace = "urn:vim25", className = "vim25.AddVirtualSwitchRequestType")
    @ResponseWrapper(localName = "AddVirtualSwitchResponse", targetNamespace = "urn:vim25", className = "vim25.AddVirtualSwitchResponse")
    public void addVirtualSwitch(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vswitchName", targetNamespace = "urn:vim25")
        String vswitchName,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        HostVirtualSwitchSpec spec)
        throws AlreadyExistsFaultMsg, HostConfigFaultFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vswitchName
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws ResourceInUseFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "RemoveVirtualSwitch", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RemoveVirtualSwitch", targetNamespace = "urn:vim25", className = "vim25.RemoveVirtualSwitchRequestType")
    @ResponseWrapper(localName = "RemoveVirtualSwitchResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveVirtualSwitchResponse")
    public void removeVirtualSwitch(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vswitchName", targetNamespace = "urn:vim25")
        String vswitchName)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vswitchName
     * @param _this
     * @param spec
     * @throws RuntimeFaultFaultMsg
     * @throws ResourceInUseFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateVirtualSwitch", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateVirtualSwitch", targetNamespace = "urn:vim25", className = "vim25.UpdateVirtualSwitchRequestType")
    @ResponseWrapper(localName = "UpdateVirtualSwitchResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateVirtualSwitchResponse")
    public void updateVirtualSwitch(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vswitchName", targetNamespace = "urn:vim25")
        String vswitchName,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        HostVirtualSwitchSpec spec)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param portgrp
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws AlreadyExistsFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "AddPortGroup", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "AddPortGroup", targetNamespace = "urn:vim25", className = "vim25.AddPortGroupRequestType")
    @ResponseWrapper(localName = "AddPortGroupResponse", targetNamespace = "urn:vim25", className = "vim25.AddPortGroupResponse")
    public void addPortGroup(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "portgrp", targetNamespace = "urn:vim25")
        HostPortGroupSpec portgrp)
        throws AlreadyExistsFaultMsg, HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param pgName
     * @throws RuntimeFaultFaultMsg
     * @throws ResourceInUseFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "RemovePortGroup", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RemovePortGroup", targetNamespace = "urn:vim25", className = "vim25.RemovePortGroupRequestType")
    @ResponseWrapper(localName = "RemovePortGroupResponse", targetNamespace = "urn:vim25", className = "vim25.RemovePortGroupResponse")
    public void removePortGroup(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "pgName", targetNamespace = "urn:vim25")
        String pgName)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param portgrp
     * @param _this
     * @param pgName
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws AlreadyExistsFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdatePortGroup", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdatePortGroup", targetNamespace = "urn:vim25", className = "vim25.UpdatePortGroupRequestType")
    @ResponseWrapper(localName = "UpdatePortGroupResponse", targetNamespace = "urn:vim25", className = "vim25.UpdatePortGroupResponse")
    public void updatePortGroup(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "pgName", targetNamespace = "urn:vim25")
        String pgName,
        @WebParam(name = "portgrp", targetNamespace = "urn:vim25")
        HostPortGroupSpec portgrp)
        throws AlreadyExistsFaultMsg, HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param linkSpeed
     * @param _this
     * @param device
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdatePhysicalNicLinkSpeed", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdatePhysicalNicLinkSpeed", targetNamespace = "urn:vim25", className = "vim25.UpdatePhysicalNicLinkSpeedRequestType")
    @ResponseWrapper(localName = "UpdatePhysicalNicLinkSpeedResponse", targetNamespace = "urn:vim25", className = "vim25.UpdatePhysicalNicLinkSpeedResponse")
    public void updatePhysicalNicLinkSpeed(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "device", targetNamespace = "urn:vim25")
        String device,
        @WebParam(name = "linkSpeed", targetNamespace = "urn:vim25")
        PhysicalNicLinkInfo linkSpeed)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param device
     * @return
     *     returns java.util.List<vim25.PhysicalNicHintInfo>
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "QueryNetworkHint", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryNetworkHint", targetNamespace = "urn:vim25", className = "vim25.QueryNetworkHintRequestType")
    @ResponseWrapper(localName = "QueryNetworkHintResponse", targetNamespace = "urn:vim25", className = "vim25.QueryNetworkHintResponse")
    public List<PhysicalNicHintInfo> queryNetworkHint(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "device", targetNamespace = "urn:vim25")
        List<String> device)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param portgroup
     * @param nic
     * @param _this
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws AlreadyExistsFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "AddVirtualNic", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "AddVirtualNic", targetNamespace = "urn:vim25", className = "vim25.AddVirtualNicRequestType")
    @ResponseWrapper(localName = "AddVirtualNicResponse", targetNamespace = "urn:vim25", className = "vim25.AddVirtualNicResponse")
    public String addVirtualNic(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "portgroup", targetNamespace = "urn:vim25")
        String portgroup,
        @WebParam(name = "nic", targetNamespace = "urn:vim25")
        HostVirtualNicSpec nic)
        throws AlreadyExistsFaultMsg, HostConfigFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param device
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "RemoveVirtualNic", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RemoveVirtualNic", targetNamespace = "urn:vim25", className = "vim25.RemoveVirtualNicRequestType")
    @ResponseWrapper(localName = "RemoveVirtualNicResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveVirtualNicResponse")
    public void removeVirtualNic(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "device", targetNamespace = "urn:vim25")
        String device)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param nic
     * @param _this
     * @param device
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateVirtualNic", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateVirtualNic", targetNamespace = "urn:vim25", className = "vim25.UpdateVirtualNicRequestType")
    @ResponseWrapper(localName = "UpdateVirtualNicResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateVirtualNicResponse")
    public void updateVirtualNic(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "device", targetNamespace = "urn:vim25")
        String device,
        @WebParam(name = "nic", targetNamespace = "urn:vim25")
        HostVirtualNicSpec nic)
        throws HostConfigFaultFaultMsg, InvalidStateFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param portgroup
     * @param nic
     * @param _this
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "AddServiceConsoleVirtualNic", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "AddServiceConsoleVirtualNic", targetNamespace = "urn:vim25", className = "vim25.AddServiceConsoleVirtualNicRequestType")
    @ResponseWrapper(localName = "AddServiceConsoleVirtualNicResponse", targetNamespace = "urn:vim25", className = "vim25.AddServiceConsoleVirtualNicResponse")
    public String addServiceConsoleVirtualNic(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "portgroup", targetNamespace = "urn:vim25")
        String portgroup,
        @WebParam(name = "nic", targetNamespace = "urn:vim25")
        HostVirtualNicSpec nic)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param device
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws ResourceInUseFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "RemoveServiceConsoleVirtualNic", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RemoveServiceConsoleVirtualNic", targetNamespace = "urn:vim25", className = "vim25.RemoveServiceConsoleVirtualNicRequestType")
    @ResponseWrapper(localName = "RemoveServiceConsoleVirtualNicResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveServiceConsoleVirtualNicResponse")
    public void removeServiceConsoleVirtualNic(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "device", targetNamespace = "urn:vim25")
        String device)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param nic
     * @param _this
     * @param device
     * @throws RuntimeFaultFaultMsg
     * @throws ResourceInUseFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateServiceConsoleVirtualNic", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateServiceConsoleVirtualNic", targetNamespace = "urn:vim25", className = "vim25.UpdateServiceConsoleVirtualNicRequestType")
    @ResponseWrapper(localName = "UpdateServiceConsoleVirtualNicResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateServiceConsoleVirtualNicResponse")
    public void updateServiceConsoleVirtualNic(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "device", targetNamespace = "urn:vim25")
        String device,
        @WebParam(name = "nic", targetNamespace = "urn:vim25")
        HostVirtualNicSpec nic)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param device
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "RestartServiceConsoleVirtualNic", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RestartServiceConsoleVirtualNic", targetNamespace = "urn:vim25", className = "vim25.RestartServiceConsoleVirtualNicRequestType")
    @ResponseWrapper(localName = "RestartServiceConsoleVirtualNicResponse", targetNamespace = "urn:vim25", className = "vim25.RestartServiceConsoleVirtualNicResponse")
    public void restartServiceConsoleVirtualNic(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "device", targetNamespace = "urn:vim25")
        String device)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RefreshNetworkSystem", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RefreshNetworkSystem", targetNamespace = "urn:vim25", className = "vim25.RefreshNetworkSystemRequestType")
    @ResponseWrapper(localName = "RefreshNetworkSystemResponse", targetNamespace = "urn:vim25", className = "vim25.RefreshNetworkSystemResponse")
    public void refreshNetworkSystem(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param bundleUrls
     * @param _this
     * @param spec
     * @param metaUrls
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws RequestCanceledFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws PlatformConfigFaultFaultMsg
     */
    @WebMethod(operationName = "CheckHostPatch_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CheckHostPatch_Task", targetNamespace = "urn:vim25", className = "vim25.CheckHostPatchRequestType")
    @ResponseWrapper(localName = "CheckHostPatch_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CheckHostPatchTaskResponse")
    public ManagedObjectReference checkHostPatchTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "metaUrls", targetNamespace = "urn:vim25")
        List<String> metaUrls,
        @WebParam(name = "bundleUrls", targetNamespace = "urn:vim25")
        List<String> bundleUrls,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        HostPatchManagerPatchManagerOperationSpec spec)
        throws InvalidStateFaultMsg, PlatformConfigFaultFaultMsg, RequestCanceledFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param updateID
     * @param _this
     * @param repository
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws RequestCanceledFaultMsg
     * @throws PlatformConfigFaultFaultMsg
     * @throws PatchMetadataInvalidFaultMsg
     */
    @WebMethod(operationName = "ScanHostPatch_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ScanHostPatch_Task", targetNamespace = "urn:vim25", className = "vim25.ScanHostPatchRequestType")
    @ResponseWrapper(localName = "ScanHostPatch_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ScanHostPatchTaskResponse")
    public ManagedObjectReference scanHostPatchTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "repository", targetNamespace = "urn:vim25")
        HostPatchManagerLocator repository,
        @WebParam(name = "updateID", targetNamespace = "urn:vim25")
        List<String> updateID)
        throws PatchMetadataInvalidFaultMsg, PlatformConfigFaultFaultMsg, RequestCanceledFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param bundleUrls
     * @param _this
     * @param spec
     * @param metaUrls
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws RequestCanceledFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws PlatformConfigFaultFaultMsg
     */
    @WebMethod(operationName = "ScanHostPatchV2_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ScanHostPatchV2_Task", targetNamespace = "urn:vim25", className = "vim25.ScanHostPatchV2RequestType")
    @ResponseWrapper(localName = "ScanHostPatchV2_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ScanHostPatchV2TaskResponse")
    public ManagedObjectReference scanHostPatchV2Task(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "metaUrls", targetNamespace = "urn:vim25")
        List<String> metaUrls,
        @WebParam(name = "bundleUrls", targetNamespace = "urn:vim25")
        List<String> bundleUrls,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        HostPatchManagerPatchManagerOperationSpec spec)
        throws InvalidStateFaultMsg, PlatformConfigFaultFaultMsg, RequestCanceledFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param bundleUrls
     * @param vibUrls
     * @param _this
     * @param spec
     * @param metaUrls
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws RequestCanceledFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws PlatformConfigFaultFaultMsg
     */
    @WebMethod(operationName = "StageHostPatch_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "StageHostPatch_Task", targetNamespace = "urn:vim25", className = "vim25.StageHostPatchRequestType")
    @ResponseWrapper(localName = "StageHostPatch_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.StageHostPatchTaskResponse")
    public ManagedObjectReference stageHostPatchTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "metaUrls", targetNamespace = "urn:vim25")
        List<String> metaUrls,
        @WebParam(name = "bundleUrls", targetNamespace = "urn:vim25")
        List<String> bundleUrls,
        @WebParam(name = "vibUrls", targetNamespace = "urn:vim25")
        List<String> vibUrls,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        HostPatchManagerPatchManagerOperationSpec spec)
        throws InvalidStateFaultMsg, PlatformConfigFaultFaultMsg, RequestCanceledFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param updateID
     * @param force
     * @param _this
     * @param repository
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NoDiskSpaceFaultMsg
     * @throws PatchNotApplicableFaultMsg
     * @throws PatchMetadataInvalidFaultMsg
     * @throws PatchInstallFailedFaultMsg
     * @throws RebootRequiredFaultMsg
     * @throws PatchBinariesNotFoundFaultMsg
     */
    @WebMethod(operationName = "InstallHostPatch_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "InstallHostPatch_Task", targetNamespace = "urn:vim25", className = "vim25.InstallHostPatchRequestType")
    @ResponseWrapper(localName = "InstallHostPatch_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.InstallHostPatchTaskResponse")
    public ManagedObjectReference installHostPatchTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "repository", targetNamespace = "urn:vim25")
        HostPatchManagerLocator repository,
        @WebParam(name = "updateID", targetNamespace = "urn:vim25")
        String updateID,
        @WebParam(name = "force", targetNamespace = "urn:vim25")
        Boolean force)
        throws InvalidStateFaultMsg, NoDiskSpaceFaultMsg, PatchBinariesNotFoundFaultMsg, PatchInstallFailedFaultMsg, PatchMetadataInvalidFaultMsg, PatchNotApplicableFaultMsg, RebootRequiredFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param bundleUrls
     * @param vibUrls
     * @param _this
     * @param spec
     * @param metaUrls
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws RequestCanceledFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws PlatformConfigFaultFaultMsg
     */
    @WebMethod(operationName = "InstallHostPatchV2_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "InstallHostPatchV2_Task", targetNamespace = "urn:vim25", className = "vim25.InstallHostPatchV2RequestType")
    @ResponseWrapper(localName = "InstallHostPatchV2_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.InstallHostPatchV2TaskResponse")
    public ManagedObjectReference installHostPatchV2Task(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "metaUrls", targetNamespace = "urn:vim25")
        List<String> metaUrls,
        @WebParam(name = "bundleUrls", targetNamespace = "urn:vim25")
        List<String> bundleUrls,
        @WebParam(name = "vibUrls", targetNamespace = "urn:vim25")
        List<String> vibUrls,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        HostPatchManagerPatchManagerOperationSpec spec)
        throws InvalidStateFaultMsg, PlatformConfigFaultFaultMsg, RequestCanceledFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param bulletinIds
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws PlatformConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UninstallHostPatch_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "UninstallHostPatch_Task", targetNamespace = "urn:vim25", className = "vim25.UninstallHostPatchRequestType")
    @ResponseWrapper(localName = "UninstallHostPatch_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.UninstallHostPatchTaskResponse")
    public ManagedObjectReference uninstallHostPatchTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "bulletinIds", targetNamespace = "urn:vim25")
        List<String> bulletinIds,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        HostPatchManagerPatchManagerOperationSpec spec)
        throws InvalidStateFaultMsg, PlatformConfigFaultFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws RequestCanceledFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws PlatformConfigFaultFaultMsg
     */
    @WebMethod(operationName = "QueryHostPatch_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryHostPatch_Task", targetNamespace = "urn:vim25", className = "vim25.QueryHostPatchRequestType")
    @ResponseWrapper(localName = "QueryHostPatch_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.QueryHostPatchTaskResponse")
    public ManagedObjectReference queryHostPatchTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        HostPatchManagerPatchManagerOperationSpec spec)
        throws InvalidStateFaultMsg, PlatformConfigFaultFaultMsg, RequestCanceledFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "Refresh", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "Refresh", targetNamespace = "urn:vim25", className = "vim25.RefreshRequestType")
    @ResponseWrapper(localName = "RefreshResponse", targetNamespace = "urn:vim25", className = "vim25.RefreshResponse")
    public void refresh(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param config
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdatePassthruConfig", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdatePassthruConfig", targetNamespace = "urn:vim25", className = "vim25.UpdatePassthruConfigRequestType")
    @ResponseWrapper(localName = "UpdatePassthruConfigResponse", targetNamespace = "urn:vim25", className = "vim25.UpdatePassthruConfigResponse")
    public void updatePassthruConfig(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "config", targetNamespace = "urn:vim25")
        List<HostPciPassthruConfig> config)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param key
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "ConfigurePowerPolicy", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ConfigurePowerPolicy", targetNamespace = "urn:vim25", className = "vim25.ConfigurePowerPolicyRequestType")
    @ResponseWrapper(localName = "ConfigurePowerPolicyResponse", targetNamespace = "urn:vim25", className = "vim25.ConfigurePowerPolicyResponse")
    public void configurePowerPolicy(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "key", targetNamespace = "urn:vim25")
        int key)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param id
     * @param _this
     * @param policy
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateServicePolicy", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateServicePolicy", targetNamespace = "urn:vim25", className = "vim25.UpdateServicePolicyRequestType")
    @ResponseWrapper(localName = "UpdateServicePolicyResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateServicePolicyResponse")
    public void updateServicePolicy(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "id", targetNamespace = "urn:vim25")
        String id,
        @WebParam(name = "policy", targetNamespace = "urn:vim25")
        String policy)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param id
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "StartService", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "StartService", targetNamespace = "urn:vim25", className = "vim25.StartServiceRequestType")
    @ResponseWrapper(localName = "StartServiceResponse", targetNamespace = "urn:vim25", className = "vim25.StartServiceResponse")
    public void startService(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "id", targetNamespace = "urn:vim25")
        String id)
        throws HostConfigFaultFaultMsg, InvalidStateFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param id
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "StopService", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "StopService", targetNamespace = "urn:vim25", className = "vim25.StopServiceRequestType")
    @ResponseWrapper(localName = "StopServiceResponse", targetNamespace = "urn:vim25", className = "vim25.StopServiceResponse")
    public void stopService(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "id", targetNamespace = "urn:vim25")
        String id)
        throws HostConfigFaultFaultMsg, InvalidStateFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param id
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "RestartService", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RestartService", targetNamespace = "urn:vim25", className = "vim25.RestartServiceRequestType")
    @ResponseWrapper(localName = "RestartServiceResponse", targetNamespace = "urn:vim25", className = "vim25.RestartServiceResponse")
    public void restartService(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "id", targetNamespace = "urn:vim25")
        String id)
        throws HostConfigFaultFaultMsg, InvalidStateFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param id
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UninstallService", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UninstallService", targetNamespace = "urn:vim25", className = "vim25.UninstallServiceRequestType")
    @ResponseWrapper(localName = "UninstallServiceResponse", targetNamespace = "urn:vim25", className = "vim25.UninstallServiceResponse")
    public void uninstallService(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "id", targetNamespace = "urn:vim25")
        String id)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RefreshServices", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RefreshServices", targetNamespace = "urn:vim25", className = "vim25.RefreshServicesRequestType")
    @ResponseWrapper(localName = "RefreshServicesResponse", targetNamespace = "urn:vim25", className = "vim25.RefreshServicesResponse")
    public void refreshServices(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "ReconfigureSnmpAgent", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ReconfigureSnmpAgent", targetNamespace = "urn:vim25", className = "vim25.ReconfigureSnmpAgentRequestType")
    @ResponseWrapper(localName = "ReconfigureSnmpAgentResponse", targetNamespace = "urn:vim25", className = "vim25.ReconfigureSnmpAgentResponse")
    public void reconfigureSnmpAgent(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        HostSnmpConfigSpec spec)
        throws InsufficientResourcesFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws NotFoundFaultMsg
     */
    @WebMethod(operationName = "SendTestNotification", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "SendTestNotification", targetNamespace = "urn:vim25", className = "vim25.SendTestNotificationRequestType")
    @ResponseWrapper(localName = "SendTestNotificationResponse", targetNamespace = "urn:vim25", className = "vim25.SendTestNotificationResponse")
    public void sendTestNotification(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InsufficientResourcesFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param devicePath
     * @param _this
     * @return
     *     returns java.util.List<vim25.HostDiskPartitionInfo>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RetrieveDiskPartitionInfo", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RetrieveDiskPartitionInfo", targetNamespace = "urn:vim25", className = "vim25.RetrieveDiskPartitionInfoRequestType")
    @ResponseWrapper(localName = "RetrieveDiskPartitionInfoResponse", targetNamespace = "urn:vim25", className = "vim25.RetrieveDiskPartitionInfoResponse")
    public List<HostDiskPartitionInfo> retrieveDiskPartitionInfo(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "devicePath", targetNamespace = "urn:vim25")
        List<String> devicePath)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param layout
     * @param devicePath
     * @param partitionFormat
     * @param _this
     * @return
     *     returns vim25.HostDiskPartitionInfo
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "ComputeDiskPartitionInfo", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ComputeDiskPartitionInfo", targetNamespace = "urn:vim25", className = "vim25.ComputeDiskPartitionInfoRequestType")
    @ResponseWrapper(localName = "ComputeDiskPartitionInfoResponse", targetNamespace = "urn:vim25", className = "vim25.ComputeDiskPartitionInfoResponse")
    public HostDiskPartitionInfo computeDiskPartitionInfo(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "devicePath", targetNamespace = "urn:vim25")
        String devicePath,
        @WebParam(name = "layout", targetNamespace = "urn:vim25")
        HostDiskPartitionLayout layout,
        @WebParam(name = "partitionFormat", targetNamespace = "urn:vim25")
        String partitionFormat)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param partition
     * @param partitionFormat
     * @param _this
     * @param blockRange
     * @return
     *     returns vim25.HostDiskPartitionInfo
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "ComputeDiskPartitionInfoForResize", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ComputeDiskPartitionInfoForResize", targetNamespace = "urn:vim25", className = "vim25.ComputeDiskPartitionInfoForResizeRequestType")
    @ResponseWrapper(localName = "ComputeDiskPartitionInfoForResizeResponse", targetNamespace = "urn:vim25", className = "vim25.ComputeDiskPartitionInfoForResizeResponse")
    public HostDiskPartitionInfo computeDiskPartitionInfoForResize(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "partition", targetNamespace = "urn:vim25")
        HostScsiDiskPartition partition,
        @WebParam(name = "blockRange", targetNamespace = "urn:vim25")
        HostDiskPartitionBlockRange blockRange,
        @WebParam(name = "partitionFormat", targetNamespace = "urn:vim25")
        String partitionFormat)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param devicePath
     * @param _this
     * @param spec
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateDiskPartitions", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateDiskPartitions", targetNamespace = "urn:vim25", className = "vim25.UpdateDiskPartitionsRequestType")
    @ResponseWrapper(localName = "UpdateDiskPartitionsResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateDiskPartitionsResponse")
    public void updateDiskPartitions(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "devicePath", targetNamespace = "urn:vim25")
        String devicePath,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        HostDiskPartitionSpec spec)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param createSpec
     * @param _this
     * @return
     *     returns vim25.HostVmfsVolume
     * @throws RuntimeFaultFaultMsg
     * @throws AlreadyExistsFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "FormatVmfs", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "FormatVmfs", targetNamespace = "urn:vim25", className = "vim25.FormatVmfsRequestType")
    @ResponseWrapper(localName = "FormatVmfsResponse", targetNamespace = "urn:vim25", className = "vim25.FormatVmfsResponse")
    public HostVmfsVolume formatVmfs(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "createSpec", targetNamespace = "urn:vim25")
        HostVmfsSpec createSpec)
        throws AlreadyExistsFaultMsg, HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vmfsUuid
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws ResourceInUseFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "MountVmfsVolume", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "MountVmfsVolume", targetNamespace = "urn:vim25", className = "vim25.MountVmfsVolumeRequestType")
    @ResponseWrapper(localName = "MountVmfsVolumeResponse", targetNamespace = "urn:vim25", className = "vim25.MountVmfsVolumeResponse")
    public void mountVmfsVolume(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vmfsUuid", targetNamespace = "urn:vim25")
        String vmfsUuid)
        throws HostConfigFaultFaultMsg, InvalidStateFaultMsg, NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vmfsUuid
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NotFoundFaultMsg
     * @throws ResourceInUseFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UnmountVmfsVolume", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UnmountVmfsVolume", targetNamespace = "urn:vim25", className = "vim25.UnmountVmfsVolumeRequestType")
    @ResponseWrapper(localName = "UnmountVmfsVolumeResponse", targetNamespace = "urn:vim25", className = "vim25.UnmountVmfsVolumeResponse")
    public void unmountVmfsVolume(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vmfsUuid", targetNamespace = "urn:vim25")
        String vmfsUuid)
        throws HostConfigFaultFaultMsg, InvalidStateFaultMsg, NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vmfsUuid
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UnmountVmfsVolumeEx_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "UnmountVmfsVolumeEx_Task", targetNamespace = "urn:vim25", className = "vim25.UnmountVmfsVolumeExRequestType")
    @ResponseWrapper(localName = "UnmountVmfsVolumeEx_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.UnmountVmfsVolumeExTaskResponse")
    public ManagedObjectReference unmountVmfsVolumeExTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vmfsUuid", targetNamespace = "urn:vim25")
        List<String> vmfsUuid)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vmfsUuid
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "MountVmfsVolumeEx_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "MountVmfsVolumeEx_Task", targetNamespace = "urn:vim25", className = "vim25.MountVmfsVolumeExRequestType")
    @ResponseWrapper(localName = "MountVmfsVolumeEx_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.MountVmfsVolumeExTaskResponse")
    public ManagedObjectReference mountVmfsVolumeExTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vmfsUuid", targetNamespace = "urn:vim25")
        List<String> vmfsUuid)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vmfsUuid
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UnmapVmfsVolumeEx_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "UnmapVmfsVolumeEx_Task", targetNamespace = "urn:vim25", className = "vim25.UnmapVmfsVolumeExRequestType")
    @ResponseWrapper(localName = "UnmapVmfsVolumeEx_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.UnmapVmfsVolumeExTaskResponse")
    public ManagedObjectReference unmapVmfsVolumeExTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vmfsUuid", targetNamespace = "urn:vim25")
        List<String> vmfsUuid)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vmfsUuid
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "DeleteVmfsVolumeState", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DeleteVmfsVolumeState", targetNamespace = "urn:vim25", className = "vim25.DeleteVmfsVolumeStateRequestType")
    @ResponseWrapper(localName = "DeleteVmfsVolumeStateResponse", targetNamespace = "urn:vim25", className = "vim25.DeleteVmfsVolumeStateResponse")
    public void deleteVmfsVolumeState(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vmfsUuid", targetNamespace = "urn:vim25")
        String vmfsUuid)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "RescanVmfs", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RescanVmfs", targetNamespace = "urn:vim25", className = "vim25.RescanVmfsRequestType")
    @ResponseWrapper(localName = "RescanVmfsResponse", targetNamespace = "urn:vim25", className = "vim25.RescanVmfsResponse")
    public void rescanVmfs(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param extent
     * @param vmfsPath
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "AttachVmfsExtent", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "AttachVmfsExtent", targetNamespace = "urn:vim25", className = "vim25.AttachVmfsExtentRequestType")
    @ResponseWrapper(localName = "AttachVmfsExtentResponse", targetNamespace = "urn:vim25", className = "vim25.AttachVmfsExtentResponse")
    public void attachVmfsExtent(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vmfsPath", targetNamespace = "urn:vim25")
        String vmfsPath,
        @WebParam(name = "extent", targetNamespace = "urn:vim25")
        HostScsiDiskPartition extent)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param extent
     * @param vmfsPath
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "ExpandVmfsExtent", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ExpandVmfsExtent", targetNamespace = "urn:vim25", className = "vim25.ExpandVmfsExtentRequestType")
    @ResponseWrapper(localName = "ExpandVmfsExtentResponse", targetNamespace = "urn:vim25", className = "vim25.ExpandVmfsExtentResponse")
    public void expandVmfsExtent(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vmfsPath", targetNamespace = "urn:vim25")
        String vmfsPath,
        @WebParam(name = "extent", targetNamespace = "urn:vim25")
        HostScsiDiskPartition extent)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vmfsPath
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpgradeVmfs", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpgradeVmfs", targetNamespace = "urn:vim25", className = "vim25.UpgradeVmfsRequestType")
    @ResponseWrapper(localName = "UpgradeVmfsResponse", targetNamespace = "urn:vim25", className = "vim25.UpgradeVmfsResponse")
    public void upgradeVmfs(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vmfsPath", targetNamespace = "urn:vim25")
        String vmfsPath)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "UpgradeVmLayout", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpgradeVmLayout", targetNamespace = "urn:vim25", className = "vim25.UpgradeVmLayoutRequestType")
    @ResponseWrapper(localName = "UpgradeVmLayoutResponse", targetNamespace = "urn:vim25", className = "vim25.UpgradeVmLayoutResponse")
    public void upgradeVmLayout(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.util.List<vim25.HostUnresolvedVmfsVolume>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryUnresolvedVmfsVolume", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryUnresolvedVmfsVolume", targetNamespace = "urn:vim25", className = "vim25.QueryUnresolvedVmfsVolumeRequestType")
    @ResponseWrapper(localName = "QueryUnresolvedVmfsVolumeResponse", targetNamespace = "urn:vim25", className = "vim25.QueryUnresolvedVmfsVolumeResponse")
    public List<HostUnresolvedVmfsVolume> queryUnresolvedVmfsVolume(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param resolutionSpec
     * @param _this
     * @return
     *     returns java.util.List<vim25.HostUnresolvedVmfsResolutionResult>
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "ResolveMultipleUnresolvedVmfsVolumes", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ResolveMultipleUnresolvedVmfsVolumes", targetNamespace = "urn:vim25", className = "vim25.ResolveMultipleUnresolvedVmfsVolumesRequestType")
    @ResponseWrapper(localName = "ResolveMultipleUnresolvedVmfsVolumesResponse", targetNamespace = "urn:vim25", className = "vim25.ResolveMultipleUnresolvedVmfsVolumesResponse")
    public List<HostUnresolvedVmfsResolutionResult> resolveMultipleUnresolvedVmfsVolumes(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "resolutionSpec", targetNamespace = "urn:vim25")
        List<HostUnresolvedVmfsResolutionSpec> resolutionSpec)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param resolutionSpec
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "ResolveMultipleUnresolvedVmfsVolumesEx_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ResolveMultipleUnresolvedVmfsVolumesEx_Task", targetNamespace = "urn:vim25", className = "vim25.ResolveMultipleUnresolvedVmfsVolumesExRequestType")
    @ResponseWrapper(localName = "ResolveMultipleUnresolvedVmfsVolumesEx_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ResolveMultipleUnresolvedVmfsVolumesExTaskResponse")
    public ManagedObjectReference resolveMultipleUnresolvedVmfsVolumesExTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "resolutionSpec", targetNamespace = "urn:vim25")
        List<HostUnresolvedVmfsResolutionSpec> resolutionSpec)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vmfsUuid
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UnmountForceMountedVmfsVolume", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UnmountForceMountedVmfsVolume", targetNamespace = "urn:vim25", className = "vim25.UnmountForceMountedVmfsVolumeRequestType")
    @ResponseWrapper(localName = "UnmountForceMountedVmfsVolumeResponse", targetNamespace = "urn:vim25", className = "vim25.UnmountForceMountedVmfsVolumeResponse")
    public void unmountForceMountedVmfsVolume(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vmfsUuid", targetNamespace = "urn:vim25")
        String vmfsUuid)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param hbaDevice
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "RescanHba", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RescanHba", targetNamespace = "urn:vim25", className = "vim25.RescanHbaRequestType")
    @ResponseWrapper(localName = "RescanHbaResponse", targetNamespace = "urn:vim25", className = "vim25.RescanHbaResponse")
    public void rescanHba(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "hbaDevice", targetNamespace = "urn:vim25")
        String hbaDevice)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "RescanAllHba", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RescanAllHba", targetNamespace = "urn:vim25", className = "vim25.RescanAllHbaRequestType")
    @ResponseWrapper(localName = "RescanAllHbaResponse", targetNamespace = "urn:vim25", className = "vim25.RescanAllHbaResponse")
    public void rescanAllHba(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param enabled
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateSoftwareInternetScsiEnabled", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateSoftwareInternetScsiEnabled", targetNamespace = "urn:vim25", className = "vim25.UpdateSoftwareInternetScsiEnabledRequestType")
    @ResponseWrapper(localName = "UpdateSoftwareInternetScsiEnabledResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateSoftwareInternetScsiEnabledResponse")
    public void updateSoftwareInternetScsiEnabled(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "enabled", targetNamespace = "urn:vim25")
        boolean enabled)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param discoveryProperties
     * @param iScsiHbaDevice
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateInternetScsiDiscoveryProperties", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateInternetScsiDiscoveryProperties", targetNamespace = "urn:vim25", className = "vim25.UpdateInternetScsiDiscoveryPropertiesRequestType")
    @ResponseWrapper(localName = "UpdateInternetScsiDiscoveryPropertiesResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateInternetScsiDiscoveryPropertiesResponse")
    public void updateInternetScsiDiscoveryProperties(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "iScsiHbaDevice", targetNamespace = "urn:vim25")
        String iScsiHbaDevice,
        @WebParam(name = "discoveryProperties", targetNamespace = "urn:vim25")
        HostInternetScsiHbaDiscoveryProperties discoveryProperties)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param targetSet
     * @param authenticationProperties
     * @param _this
     * @param iScsiHbaDevice
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateInternetScsiAuthenticationProperties", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateInternetScsiAuthenticationProperties", targetNamespace = "urn:vim25", className = "vim25.UpdateInternetScsiAuthenticationPropertiesRequestType")
    @ResponseWrapper(localName = "UpdateInternetScsiAuthenticationPropertiesResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateInternetScsiAuthenticationPropertiesResponse")
    public void updateInternetScsiAuthenticationProperties(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "iScsiHbaDevice", targetNamespace = "urn:vim25")
        String iScsiHbaDevice,
        @WebParam(name = "authenticationProperties", targetNamespace = "urn:vim25")
        HostInternetScsiHbaAuthenticationProperties authenticationProperties,
        @WebParam(name = "targetSet", targetNamespace = "urn:vim25")
        HostInternetScsiHbaTargetSet targetSet)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param targetSet
     * @param digestProperties
     * @param _this
     * @param iScsiHbaDevice
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateInternetScsiDigestProperties", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateInternetScsiDigestProperties", targetNamespace = "urn:vim25", className = "vim25.UpdateInternetScsiDigestPropertiesRequestType")
    @ResponseWrapper(localName = "UpdateInternetScsiDigestPropertiesResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateInternetScsiDigestPropertiesResponse")
    public void updateInternetScsiDigestProperties(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "iScsiHbaDevice", targetNamespace = "urn:vim25")
        String iScsiHbaDevice,
        @WebParam(name = "targetSet", targetNamespace = "urn:vim25")
        HostInternetScsiHbaTargetSet targetSet,
        @WebParam(name = "digestProperties", targetNamespace = "urn:vim25")
        HostInternetScsiHbaDigestProperties digestProperties)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param targetSet
     * @param options
     * @param _this
     * @param iScsiHbaDevice
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateInternetScsiAdvancedOptions", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateInternetScsiAdvancedOptions", targetNamespace = "urn:vim25", className = "vim25.UpdateInternetScsiAdvancedOptionsRequestType")
    @ResponseWrapper(localName = "UpdateInternetScsiAdvancedOptionsResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateInternetScsiAdvancedOptionsResponse")
    public void updateInternetScsiAdvancedOptions(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "iScsiHbaDevice", targetNamespace = "urn:vim25")
        String iScsiHbaDevice,
        @WebParam(name = "targetSet", targetNamespace = "urn:vim25")
        HostInternetScsiHbaTargetSet targetSet,
        @WebParam(name = "options", targetNamespace = "urn:vim25")
        List<HostInternetScsiHbaParamValue> options)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param ipProperties
     * @param _this
     * @param iScsiHbaDevice
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateInternetScsiIPProperties", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateInternetScsiIPProperties", targetNamespace = "urn:vim25", className = "vim25.UpdateInternetScsiIPPropertiesRequestType")
    @ResponseWrapper(localName = "UpdateInternetScsiIPPropertiesResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateInternetScsiIPPropertiesResponse")
    public void updateInternetScsiIPProperties(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "iScsiHbaDevice", targetNamespace = "urn:vim25")
        String iScsiHbaDevice,
        @WebParam(name = "ipProperties", targetNamespace = "urn:vim25")
        HostInternetScsiHbaIPProperties ipProperties)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param iScsiHbaDevice
     * @param iScsiName
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateInternetScsiName", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateInternetScsiName", targetNamespace = "urn:vim25", className = "vim25.UpdateInternetScsiNameRequestType")
    @ResponseWrapper(localName = "UpdateInternetScsiNameResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateInternetScsiNameResponse")
    public void updateInternetScsiName(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "iScsiHbaDevice", targetNamespace = "urn:vim25")
        String iScsiHbaDevice,
        @WebParam(name = "iScsiName", targetNamespace = "urn:vim25")
        String iScsiName)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param iScsiAlias
     * @param _this
     * @param iScsiHbaDevice
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateInternetScsiAlias", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateInternetScsiAlias", targetNamespace = "urn:vim25", className = "vim25.UpdateInternetScsiAliasRequestType")
    @ResponseWrapper(localName = "UpdateInternetScsiAliasResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateInternetScsiAliasResponse")
    public void updateInternetScsiAlias(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "iScsiHbaDevice", targetNamespace = "urn:vim25")
        String iScsiHbaDevice,
        @WebParam(name = "iScsiAlias", targetNamespace = "urn:vim25")
        String iScsiAlias)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param targets
     * @param iScsiHbaDevice
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "AddInternetScsiSendTargets", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "AddInternetScsiSendTargets", targetNamespace = "urn:vim25", className = "vim25.AddInternetScsiSendTargetsRequestType")
    @ResponseWrapper(localName = "AddInternetScsiSendTargetsResponse", targetNamespace = "urn:vim25", className = "vim25.AddInternetScsiSendTargetsResponse")
    public void addInternetScsiSendTargets(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "iScsiHbaDevice", targetNamespace = "urn:vim25")
        String iScsiHbaDevice,
        @WebParam(name = "targets", targetNamespace = "urn:vim25")
        List<HostInternetScsiHbaSendTarget> targets)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param targets
     * @param iScsiHbaDevice
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "RemoveInternetScsiSendTargets", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RemoveInternetScsiSendTargets", targetNamespace = "urn:vim25", className = "vim25.RemoveInternetScsiSendTargetsRequestType")
    @ResponseWrapper(localName = "RemoveInternetScsiSendTargetsResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveInternetScsiSendTargetsResponse")
    public void removeInternetScsiSendTargets(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "iScsiHbaDevice", targetNamespace = "urn:vim25")
        String iScsiHbaDevice,
        @WebParam(name = "targets", targetNamespace = "urn:vim25")
        List<HostInternetScsiHbaSendTarget> targets)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param targets
     * @param iScsiHbaDevice
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "AddInternetScsiStaticTargets", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "AddInternetScsiStaticTargets", targetNamespace = "urn:vim25", className = "vim25.AddInternetScsiStaticTargetsRequestType")
    @ResponseWrapper(localName = "AddInternetScsiStaticTargetsResponse", targetNamespace = "urn:vim25", className = "vim25.AddInternetScsiStaticTargetsResponse")
    public void addInternetScsiStaticTargets(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "iScsiHbaDevice", targetNamespace = "urn:vim25")
        String iScsiHbaDevice,
        @WebParam(name = "targets", targetNamespace = "urn:vim25")
        List<HostInternetScsiHbaStaticTarget> targets)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param targets
     * @param iScsiHbaDevice
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "RemoveInternetScsiStaticTargets", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RemoveInternetScsiStaticTargets", targetNamespace = "urn:vim25", className = "vim25.RemoveInternetScsiStaticTargetsRequestType")
    @ResponseWrapper(localName = "RemoveInternetScsiStaticTargetsResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveInternetScsiStaticTargetsResponse")
    public void removeInternetScsiStaticTargets(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "iScsiHbaDevice", targetNamespace = "urn:vim25")
        String iScsiHbaDevice,
        @WebParam(name = "targets", targetNamespace = "urn:vim25")
        List<HostInternetScsiHbaStaticTarget> targets)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param pathName
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "EnableMultipathPath", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "EnableMultipathPath", targetNamespace = "urn:vim25", className = "vim25.EnableMultipathPathRequestType")
    @ResponseWrapper(localName = "EnableMultipathPathResponse", targetNamespace = "urn:vim25", className = "vim25.EnableMultipathPathResponse")
    public void enableMultipathPath(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "pathName", targetNamespace = "urn:vim25")
        String pathName)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param pathName
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "DisableMultipathPath", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DisableMultipathPath", targetNamespace = "urn:vim25", className = "vim25.DisableMultipathPathRequestType")
    @ResponseWrapper(localName = "DisableMultipathPathResponse", targetNamespace = "urn:vim25", className = "vim25.DisableMultipathPathResponse")
    public void disableMultipathPath(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "pathName", targetNamespace = "urn:vim25")
        String pathName)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param lunId
     * @param _this
     * @param policy
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "SetMultipathLunPolicy", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "SetMultipathLunPolicy", targetNamespace = "urn:vim25", className = "vim25.SetMultipathLunPolicyRequestType")
    @ResponseWrapper(localName = "SetMultipathLunPolicyResponse", targetNamespace = "urn:vim25", className = "vim25.SetMultipathLunPolicyResponse")
    public void setMultipathLunPolicy(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "lunId", targetNamespace = "urn:vim25")
        String lunId,
        @WebParam(name = "policy", targetNamespace = "urn:vim25")
        HostMultipathInfoLogicalUnitPolicy policy)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.util.List<vim25.HostPathSelectionPolicyOption>
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "QueryPathSelectionPolicyOptions", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryPathSelectionPolicyOptions", targetNamespace = "urn:vim25", className = "vim25.QueryPathSelectionPolicyOptionsRequestType")
    @ResponseWrapper(localName = "QueryPathSelectionPolicyOptionsResponse", targetNamespace = "urn:vim25", className = "vim25.QueryPathSelectionPolicyOptionsResponse")
    public List<HostPathSelectionPolicyOption> queryPathSelectionPolicyOptions(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.util.List<vim25.HostStorageArrayTypePolicyOption>
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "QueryStorageArrayTypePolicyOptions", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryStorageArrayTypePolicyOptions", targetNamespace = "urn:vim25", className = "vim25.QueryStorageArrayTypePolicyOptionsRequestType")
    @ResponseWrapper(localName = "QueryStorageArrayTypePolicyOptionsResponse", targetNamespace = "urn:vim25", className = "vim25.QueryStorageArrayTypePolicyOptionsResponse")
    public List<HostStorageArrayTypePolicyOption> queryStorageArrayTypePolicyOptions(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param displayName
     * @param _this
     * @param lunUuid
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "UpdateScsiLunDisplayName", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateScsiLunDisplayName", targetNamespace = "urn:vim25", className = "vim25.UpdateScsiLunDisplayNameRequestType")
    @ResponseWrapper(localName = "UpdateScsiLunDisplayNameResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateScsiLunDisplayNameResponse")
    public void updateScsiLunDisplayName(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "lunUuid", targetNamespace = "urn:vim25")
        String lunUuid,
        @WebParam(name = "displayName", targetNamespace = "urn:vim25")
        String displayName)
        throws DuplicateNameFaultMsg, HostConfigFaultFaultMsg, InvalidNameFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param lunUuid
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NotFoundFaultMsg
     * @throws ResourceInUseFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "DetachScsiLun", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DetachScsiLun", targetNamespace = "urn:vim25", className = "vim25.DetachScsiLunRequestType")
    @ResponseWrapper(localName = "DetachScsiLunResponse", targetNamespace = "urn:vim25", className = "vim25.DetachScsiLunResponse")
    public void detachScsiLun(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "lunUuid", targetNamespace = "urn:vim25")
        String lunUuid)
        throws HostConfigFaultFaultMsg, InvalidStateFaultMsg, NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param lunUuid
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "DetachScsiLunEx_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "DetachScsiLunEx_Task", targetNamespace = "urn:vim25", className = "vim25.DetachScsiLunExRequestType")
    @ResponseWrapper(localName = "DetachScsiLunEx_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.DetachScsiLunExTaskResponse")
    public ManagedObjectReference detachScsiLunExTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "lunUuid", targetNamespace = "urn:vim25")
        List<String> lunUuid)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param lunCanonicalName
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "DeleteScsiLunState", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DeleteScsiLunState", targetNamespace = "urn:vim25", className = "vim25.DeleteScsiLunStateRequestType")
    @ResponseWrapper(localName = "DeleteScsiLunStateResponse", targetNamespace = "urn:vim25", className = "vim25.DeleteScsiLunStateResponse")
    public void deleteScsiLunState(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "lunCanonicalName", targetNamespace = "urn:vim25")
        String lunCanonicalName)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param lunUuid
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "AttachScsiLun", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "AttachScsiLun", targetNamespace = "urn:vim25", className = "vim25.AttachScsiLunRequestType")
    @ResponseWrapper(localName = "AttachScsiLunResponse", targetNamespace = "urn:vim25", className = "vim25.AttachScsiLunResponse")
    public void attachScsiLun(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "lunUuid", targetNamespace = "urn:vim25")
        String lunUuid)
        throws HostConfigFaultFaultMsg, InvalidStateFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param lunUuid
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "AttachScsiLunEx_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "AttachScsiLunEx_Task", targetNamespace = "urn:vim25", className = "vim25.AttachScsiLunExRequestType")
    @ResponseWrapper(localName = "AttachScsiLunEx_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.AttachScsiLunExTaskResponse")
    public ManagedObjectReference attachScsiLunExTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "lunUuid", targetNamespace = "urn:vim25")
        List<String> lunUuid)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RefreshStorageSystem", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RefreshStorageSystem", targetNamespace = "urn:vim25", className = "vim25.RefreshStorageSystemRequestType")
    @ResponseWrapper(localName = "RefreshStorageSystemResponse", targetNamespace = "urn:vim25", className = "vim25.RefreshStorageSystemResponse")
    public void refreshStorageSystem(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param fcoeSpec
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     * @throws FcoeFaultPnicHasNoPortSetFaultMsg
     */
    @WebMethod(operationName = "DiscoverFcoeHbas", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DiscoverFcoeHbas", targetNamespace = "urn:vim25", className = "vim25.DiscoverFcoeHbasRequestType")
    @ResponseWrapper(localName = "DiscoverFcoeHbasResponse", targetNamespace = "urn:vim25", className = "vim25.DiscoverFcoeHbasResponse")
    public void discoverFcoeHbas(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "fcoeSpec", targetNamespace = "urn:vim25")
        FcoeConfigFcoeSpecification fcoeSpec)
        throws FcoeFaultPnicHasNoPortSetFaultMsg, HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param hbaName
     * @param remove
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "MarkForRemoval", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "MarkForRemoval", targetNamespace = "urn:vim25", className = "vim25.MarkForRemovalRequestType")
    @ResponseWrapper(localName = "MarkForRemovalResponse", targetNamespace = "urn:vim25", className = "vim25.MarkForRemovalResponse")
    public void markForRemoval(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "hbaName", targetNamespace = "urn:vim25")
        String hbaName,
        @WebParam(name = "remove", targetNamespace = "urn:vim25")
        boolean remove)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param createSpec
     * @param _this
     * @return
     *     returns vim25.HostVffsVolume
     * @throws RuntimeFaultFaultMsg
     * @throws ResourceInUseFaultMsg
     * @throws AlreadyExistsFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "FormatVffs", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "FormatVffs", targetNamespace = "urn:vim25", className = "vim25.FormatVffsRequestType")
    @ResponseWrapper(localName = "FormatVffsResponse", targetNamespace = "urn:vim25", className = "vim25.FormatVffsResponse")
    public HostVffsVolume formatVffs(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "createSpec", targetNamespace = "urn:vim25")
        HostVffsSpec createSpec)
        throws AlreadyExistsFaultMsg, HostConfigFaultFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param devicePath
     * @param _this
     * @param vffsPath
     * @param spec
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws ResourceInUseFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "ExtendVffs", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ExtendVffs", targetNamespace = "urn:vim25", className = "vim25.ExtendVffsRequestType")
    @ResponseWrapper(localName = "ExtendVffsResponse", targetNamespace = "urn:vim25", className = "vim25.ExtendVffsResponse")
    public void extendVffs(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vffsPath", targetNamespace = "urn:vim25")
        String vffsPath,
        @WebParam(name = "devicePath", targetNamespace = "urn:vim25")
        String devicePath,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        HostDiskPartitionSpec spec)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param vffsPath
     * @throws RuntimeFaultFaultMsg
     * @throws ResourceInUseFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "DestroyVffs", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DestroyVffs", targetNamespace = "urn:vim25", className = "vim25.DestroyVffsRequestType")
    @ResponseWrapper(localName = "DestroyVffsResponse", targetNamespace = "urn:vim25", className = "vim25.DestroyVffsResponse")
    public void destroyVffs(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vffsPath", targetNamespace = "urn:vim25")
        String vffsPath)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vffsUuid
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws ResourceInUseFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "MountVffsVolume", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "MountVffsVolume", targetNamespace = "urn:vim25", className = "vim25.MountVffsVolumeRequestType")
    @ResponseWrapper(localName = "MountVffsVolumeResponse", targetNamespace = "urn:vim25", className = "vim25.MountVffsVolumeResponse")
    public void mountVffsVolume(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vffsUuid", targetNamespace = "urn:vim25")
        String vffsUuid)
        throws HostConfigFaultFaultMsg, InvalidStateFaultMsg, NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vffsUuid
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws ResourceInUseFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UnmountVffsVolume", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UnmountVffsVolume", targetNamespace = "urn:vim25", className = "vim25.UnmountVffsVolumeRequestType")
    @ResponseWrapper(localName = "UnmountVffsVolumeResponse", targetNamespace = "urn:vim25", className = "vim25.UnmountVffsVolumeResponse")
    public void unmountVffsVolume(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vffsUuid", targetNamespace = "urn:vim25")
        String vffsUuid)
        throws HostConfigFaultFaultMsg, InvalidStateFaultMsg, NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vffsUuid
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "DeleteVffsVolumeState", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DeleteVffsVolumeState", targetNamespace = "urn:vim25", className = "vim25.DeleteVffsVolumeStateRequestType")
    @ResponseWrapper(localName = "DeleteVffsVolumeStateResponse", targetNamespace = "urn:vim25", className = "vim25.DeleteVffsVolumeStateResponse")
    public void deleteVffsVolumeState(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vffsUuid", targetNamespace = "urn:vim25")
        String vffsUuid)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "RescanVffs", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RescanVffs", targetNamespace = "urn:vim25", className = "vim25.RescanVffsRequestType")
    @ResponseWrapper(localName = "RescanVffsResponse", targetNamespace = "urn:vim25", className = "vim25.RescanVffsResponse")
    public void rescanVffs(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param vffsPath
     * @return
     *     returns java.util.List<vim25.HostScsiDisk>
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "QueryAvailableSsds", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryAvailableSsds", targetNamespace = "urn:vim25", className = "vim25.QueryAvailableSsdsRequestType")
    @ResponseWrapper(localName = "QueryAvailableSsdsResponse", targetNamespace = "urn:vim25", className = "vim25.QueryAvailableSsdsResponse")
    public List<HostScsiDisk> queryAvailableSsds(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vffsPath", targetNamespace = "urn:vim25")
        String vffsPath)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param password
     * @param _this
     * @param user
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "SetNFSUser", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "SetNFSUser", targetNamespace = "urn:vim25", className = "vim25.SetNFSUserRequestType")
    @ResponseWrapper(localName = "SetNFSUserResponse", targetNamespace = "urn:vim25", className = "vim25.SetNFSUserResponse")
    public void setNFSUser(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "user", targetNamespace = "urn:vim25")
        String user,
        @WebParam(name = "password", targetNamespace = "urn:vim25")
        String password)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param password
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "ChangeNFSUserPassword", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ChangeNFSUserPassword", targetNamespace = "urn:vim25", className = "vim25.ChangeNFSUserPasswordRequestType")
    @ResponseWrapper(localName = "ChangeNFSUserPasswordResponse", targetNamespace = "urn:vim25", className = "vim25.ChangeNFSUserPasswordResponse")
    public void changeNFSUserPassword(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "password", targetNamespace = "urn:vim25")
        String password)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.HostNasVolumeUserInfo
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "QueryNFSUser", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryNFSUser", targetNamespace = "urn:vim25", className = "vim25.QueryNFSUserRequestType")
    @ResponseWrapper(localName = "QueryNFSUserResponse", targetNamespace = "urn:vim25", className = "vim25.QueryNFSUserResponse")
    public HostNasVolumeUserInfo queryNFSUser(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "ClearNFSUser", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ClearNFSUser", targetNamespace = "urn:vim25", className = "vim25.ClearNFSUserRequestType")
    @ResponseWrapper(localName = "ClearNFSUserResponse", targetNamespace = "urn:vim25", className = "vim25.ClearNFSUserResponse")
    public void clearNFSUser(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param scsiDiskUuids
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "TurnDiskLocatorLedOn_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "TurnDiskLocatorLedOn_Task", targetNamespace = "urn:vim25", className = "vim25.TurnDiskLocatorLedOnRequestType")
    @ResponseWrapper(localName = "TurnDiskLocatorLedOn_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.TurnDiskLocatorLedOnTaskResponse")
    public ManagedObjectReference turnDiskLocatorLedOnTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "scsiDiskUuids", targetNamespace = "urn:vim25")
        List<String> scsiDiskUuids)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param scsiDiskUuids
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "TurnDiskLocatorLedOff_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "TurnDiskLocatorLedOff_Task", targetNamespace = "urn:vim25", className = "vim25.TurnDiskLocatorLedOffRequestType")
    @ResponseWrapper(localName = "TurnDiskLocatorLedOff_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.TurnDiskLocatorLedOffTaskResponse")
    public ManagedObjectReference turnDiskLocatorLedOffTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "scsiDiskUuids", targetNamespace = "urn:vim25")
        List<String> scsiDiskUuids)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param scsiDiskUuid
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "MarkAsSsd_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "MarkAsSsd_Task", targetNamespace = "urn:vim25", className = "vim25.MarkAsSsdRequestType")
    @ResponseWrapper(localName = "MarkAsSsd_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.MarkAsSsdTaskResponse")
    public ManagedObjectReference markAsSsdTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "scsiDiskUuid", targetNamespace = "urn:vim25")
        String scsiDiskUuid)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param scsiDiskUuid
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "MarkAsNonSsd_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "MarkAsNonSsd_Task", targetNamespace = "urn:vim25", className = "vim25.MarkAsNonSsdRequestType")
    @ResponseWrapper(localName = "MarkAsNonSsd_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.MarkAsNonSsdTaskResponse")
    public ManagedObjectReference markAsNonSsdTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "scsiDiskUuid", targetNamespace = "urn:vim25")
        String scsiDiskUuid)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param scsiDiskUuid
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "MarkAsLocal_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "MarkAsLocal_Task", targetNamespace = "urn:vim25", className = "vim25.MarkAsLocalRequestType")
    @ResponseWrapper(localName = "MarkAsLocal_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.MarkAsLocalTaskResponse")
    public ManagedObjectReference markAsLocalTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "scsiDiskUuid", targetNamespace = "urn:vim25")
        String scsiDiskUuid)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param scsiDiskUuid
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "MarkAsNonLocal_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "MarkAsNonLocal_Task", targetNamespace = "urn:vim25", className = "vim25.MarkAsNonLocalRequestType")
    @ResponseWrapper(localName = "MarkAsNonLocal_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.MarkAsNonLocalTaskResponse")
    public ManagedObjectReference markAsNonLocalTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "scsiDiskUuid", targetNamespace = "urn:vim25")
        String scsiDiskUuid)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param devicePath
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "ConfigureVFlashResourceEx_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ConfigureVFlashResourceEx_Task", targetNamespace = "urn:vim25", className = "vim25.ConfigureVFlashResourceExRequestType")
    @ResponseWrapper(localName = "ConfigureVFlashResourceEx_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ConfigureVFlashResourceExTaskResponse")
    public ManagedObjectReference configureVFlashResourceExTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "devicePath", targetNamespace = "urn:vim25")
        List<String> devicePath)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @throws RuntimeFaultFaultMsg
     * @throws ResourceInUseFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "HostConfigureVFlashResource", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "HostConfigureVFlashResource", targetNamespace = "urn:vim25", className = "vim25.HostConfigureVFlashResourceRequestType")
    @ResponseWrapper(localName = "HostConfigureVFlashResourceResponse", targetNamespace = "urn:vim25", className = "vim25.HostConfigureVFlashResourceResponse")
    public void hostConfigureVFlashResource(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        HostVFlashManagerVFlashResourceConfigSpec spec)
        throws HostConfigFaultFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws ResourceInUseFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "HostRemoveVFlashResource", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "HostRemoveVFlashResource", targetNamespace = "urn:vim25", className = "vim25.HostRemoveVFlashResourceRequestType")
    @ResponseWrapper(localName = "HostRemoveVFlashResourceResponse", targetNamespace = "urn:vim25", className = "vim25.HostRemoveVFlashResourceResponse")
    public void hostRemoveVFlashResource(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @throws RuntimeFaultFaultMsg
     * @throws InaccessibleVFlashSourceFaultMsg
     * @throws ResourceInUseFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "HostConfigVFlashCache", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "HostConfigVFlashCache", targetNamespace = "urn:vim25", className = "vim25.HostConfigVFlashCacheRequestType")
    @ResponseWrapper(localName = "HostConfigVFlashCacheResponse", targetNamespace = "urn:vim25", className = "vim25.HostConfigVFlashCacheResponse")
    public void hostConfigVFlashCache(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        HostVFlashManagerVFlashCacheConfigSpec spec)
        throws HostConfigFaultFaultMsg, InaccessibleVFlashSourceFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vFlashModule
     * @param _this
     * @return
     *     returns vim25.VirtualDiskVFlashCacheConfigInfo
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "HostGetVFlashModuleDefaultConfig", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "HostGetVFlashModuleDefaultConfig", targetNamespace = "urn:vim25", className = "vim25.HostGetVFlashModuleDefaultConfigRequestType")
    @ResponseWrapper(localName = "HostGetVFlashModuleDefaultConfigResponse", targetNamespace = "urn:vim25", className = "vim25.HostGetVFlashModuleDefaultConfigResponse")
    public VirtualDiskVFlashCacheConfigInfo hostGetVFlashModuleDefaultConfig(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vFlashModule", targetNamespace = "urn:vim25")
        String vFlashModule)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param ipConfig
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws NotFoundFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateIpConfig", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateIpConfig", targetNamespace = "urn:vim25", className = "vim25.UpdateIpConfigRequestType")
    @ResponseWrapper(localName = "UpdateIpConfigResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateIpConfigResponse")
    public void updateIpConfig(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "ipConfig", targetNamespace = "urn:vim25")
        HostIpConfig ipConfig)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param device
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "SelectVnic", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "SelectVnic", targetNamespace = "urn:vim25", className = "vim25.SelectVnicRequestType")
    @ResponseWrapper(localName = "SelectVnicResponse", targetNamespace = "urn:vim25", className = "vim25.SelectVnicResponse")
    public void selectVnic(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "device", targetNamespace = "urn:vim25")
        String device)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "DeselectVnic", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DeselectVnic", targetNamespace = "urn:vim25", className = "vim25.DeselectVnicRequestType")
    @ResponseWrapper(localName = "DeselectVnicResponse", targetNamespace = "urn:vim25", className = "vim25.DeselectVnicResponse")
    public void deselectVnic(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param nicType
     * @param _this
     * @return
     *     returns vim25.VirtualNicManagerNetConfig
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidArgumentFaultMsg
     * @throws HostConfigFaultFaultMsg
     */
    @WebMethod(operationName = "QueryNetConfig", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryNetConfig", targetNamespace = "urn:vim25", className = "vim25.QueryNetConfigRequestType")
    @ResponseWrapper(localName = "QueryNetConfigResponse", targetNamespace = "urn:vim25", className = "vim25.QueryNetConfigResponse")
    public VirtualNicManagerNetConfig queryNetConfig(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "nicType", targetNamespace = "urn:vim25")
        String nicType)
        throws HostConfigFaultFaultMsg, InvalidArgumentFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param nicType
     * @param _this
     * @param device
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     * @throws InvalidArgumentFaultMsg
     */
    @WebMethod(operationName = "SelectVnicForNicType", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "SelectVnicForNicType", targetNamespace = "urn:vim25", className = "vim25.SelectVnicForNicTypeRequestType")
    @ResponseWrapper(localName = "SelectVnicForNicTypeResponse", targetNamespace = "urn:vim25", className = "vim25.SelectVnicForNicTypeResponse")
    public void selectVnicForNicType(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "nicType", targetNamespace = "urn:vim25")
        String nicType,
        @WebParam(name = "device", targetNamespace = "urn:vim25")
        String device)
        throws HostConfigFaultFaultMsg, InvalidArgumentFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param nicType
     * @param _this
     * @param device
     * @throws RuntimeFaultFaultMsg
     * @throws HostConfigFaultFaultMsg
     * @throws InvalidArgumentFaultMsg
     */
    @WebMethod(operationName = "DeselectVnicForNicType", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DeselectVnicForNicType", targetNamespace = "urn:vim25", className = "vim25.DeselectVnicForNicTypeRequestType")
    @ResponseWrapper(localName = "DeselectVnicForNicTypeResponse", targetNamespace = "urn:vim25", className = "vim25.DeselectVnicForNicTypeResponse")
    public void deselectVnicForNicType(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "nicType", targetNamespace = "urn:vim25")
        String nicType,
        @WebParam(name = "device", targetNamespace = "urn:vim25")
        String device)
        throws HostConfigFaultFaultMsg, InvalidArgumentFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param queries
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryCmmds", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryCmmds", targetNamespace = "urn:vim25", className = "vim25.QueryCmmdsRequestType")
    @ResponseWrapper(localName = "QueryCmmdsResponse", targetNamespace = "urn:vim25", className = "vim25.QueryCmmdsResponse")
    public String queryCmmds(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "queries", targetNamespace = "urn:vim25")
        List<HostVsanInternalSystemCmmdsQuery> queries)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param props
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryPhysicalVsanDisks", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryPhysicalVsanDisks", targetNamespace = "urn:vim25", className = "vim25.QueryPhysicalVsanDisksRequestType")
    @ResponseWrapper(localName = "QueryPhysicalVsanDisksResponse", targetNamespace = "urn:vim25", className = "vim25.QueryPhysicalVsanDisksResponse")
    public String queryPhysicalVsanDisks(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "props", targetNamespace = "urn:vim25")
        List<String> props)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param uuids
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryVsanObjects", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryVsanObjects", targetNamespace = "urn:vim25", className = "vim25.QueryVsanObjectsRequestType")
    @ResponseWrapper(localName = "QueryVsanObjectsResponse", targetNamespace = "urn:vim25", className = "vim25.QueryVsanObjectsResponse")
    public String queryVsanObjects(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "uuids", targetNamespace = "urn:vim25")
        List<String> uuids)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param disks
     * @param _this
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryObjectsOnPhysicalVsanDisk", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryObjectsOnPhysicalVsanDisk", targetNamespace = "urn:vim25", className = "vim25.QueryObjectsOnPhysicalVsanDiskRequestType")
    @ResponseWrapper(localName = "QueryObjectsOnPhysicalVsanDiskResponse", targetNamespace = "urn:vim25", className = "vim25.QueryObjectsOnPhysicalVsanDiskResponse")
    public String queryObjectsOnPhysicalVsanDisk(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "disks", targetNamespace = "urn:vim25")
        List<String> disks)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param uuids
     * @return
     *     returns java.util.List<java.lang.String>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "AbdicateDomOwnership", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "AbdicateDomOwnership", targetNamespace = "urn:vim25", className = "vim25.AbdicateDomOwnershipRequestType")
    @ResponseWrapper(localName = "AbdicateDomOwnershipResponse", targetNamespace = "urn:vim25", className = "vim25.AbdicateDomOwnershipResponse")
    public List<String> abdicateDomOwnership(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "uuids", targetNamespace = "urn:vim25")
        List<String> uuids)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param labels
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryVsanStatistics", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryVsanStatistics", targetNamespace = "urn:vim25", className = "vim25.QueryVsanStatisticsRequestType")
    @ResponseWrapper(localName = "QueryVsanStatisticsResponse", targetNamespace = "urn:vim25", className = "vim25.QueryVsanStatisticsResponse")
    public String queryVsanStatistics(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "labels", targetNamespace = "urn:vim25")
        List<String> labels)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param uuid
     * @param policy
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ReconfigureDomObject", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ReconfigureDomObject", targetNamespace = "urn:vim25", className = "vim25.ReconfigureDomObjectRequestType")
    @ResponseWrapper(localName = "ReconfigureDomObjectResponse", targetNamespace = "urn:vim25", className = "vim25.ReconfigureDomObjectResponse")
    public void reconfigureDomObject(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "uuid", targetNamespace = "urn:vim25")
        String uuid,
        @WebParam(name = "policy", targetNamespace = "urn:vim25")
        String policy)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param uuids
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QuerySyncingVsanObjects", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QuerySyncingVsanObjects", targetNamespace = "urn:vim25", className = "vim25.QuerySyncingVsanObjectsRequestType")
    @ResponseWrapper(localName = "QuerySyncingVsanObjectsResponse", targetNamespace = "urn:vim25", className = "vim25.QuerySyncingVsanObjectsResponse")
    public String querySyncingVsanObjects(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "uuids", targetNamespace = "urn:vim25")
        List<String> uuids)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param disks
     * @param _this
     * @return
     *     returns java.util.List<vim25.HostVsanInternalSystemVsanPhysicalDiskDiagnosticsResult>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RunVsanPhysicalDiskDiagnostics", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RunVsanPhysicalDiskDiagnostics", targetNamespace = "urn:vim25", className = "vim25.RunVsanPhysicalDiskDiagnosticsRequestType")
    @ResponseWrapper(localName = "RunVsanPhysicalDiskDiagnosticsResponse", targetNamespace = "urn:vim25", className = "vim25.RunVsanPhysicalDiskDiagnosticsResponse")
    public List<HostVsanInternalSystemVsanPhysicalDiskDiagnosticsResult> runVsanPhysicalDiskDiagnostics(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "disks", targetNamespace = "urn:vim25")
        List<String> disks)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param uuids
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     * @throws VimFaultFaultMsg
     */
    @WebMethod(operationName = "GetVsanObjExtAttrs", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "GetVsanObjExtAttrs", targetNamespace = "urn:vim25", className = "vim25.GetVsanObjExtAttrsRequestType")
    @ResponseWrapper(localName = "GetVsanObjExtAttrsResponse", targetNamespace = "urn:vim25", className = "vim25.GetVsanObjExtAttrsResponse")
    public String getVsanObjExtAttrs(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "uuids", targetNamespace = "urn:vim25")
        List<String> uuids)
        throws RuntimeFaultFaultMsg, VimFaultFaultMsg
    ;

    /**
     * 
     * @param pcbs
     * @param ignoreSatisfiability
     * @param _this
     * @return
     *     returns java.util.List<vim25.VsanPolicySatisfiability>
     * @throws RuntimeFaultFaultMsg
     * @throws VimFaultFaultMsg
     */
    @WebMethod(operationName = "ReconfigurationSatisfiable", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ReconfigurationSatisfiable", targetNamespace = "urn:vim25", className = "vim25.ReconfigurationSatisfiableRequestType")
    @ResponseWrapper(localName = "ReconfigurationSatisfiableResponse", targetNamespace = "urn:vim25", className = "vim25.ReconfigurationSatisfiableResponse")
    public List<VsanPolicySatisfiability> reconfigurationSatisfiable(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "pcbs", targetNamespace = "urn:vim25")
        List<VsanPolicyChangeBatch> pcbs,
        @WebParam(name = "ignoreSatisfiability", targetNamespace = "urn:vim25")
        Boolean ignoreSatisfiability)
        throws RuntimeFaultFaultMsg, VimFaultFaultMsg
    ;

    /**
     * 
     * @param npbs
     * @param ignoreSatisfiability
     * @param _this
     * @return
     *     returns java.util.List<vim25.VsanPolicySatisfiability>
     * @throws RuntimeFaultFaultMsg
     * @throws VimFaultFaultMsg
     */
    @WebMethod(operationName = "CanProvisionObjects", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CanProvisionObjects", targetNamespace = "urn:vim25", className = "vim25.CanProvisionObjectsRequestType")
    @ResponseWrapper(localName = "CanProvisionObjectsResponse", targetNamespace = "urn:vim25", className = "vim25.CanProvisionObjectsResponse")
    public List<VsanPolicySatisfiability> canProvisionObjects(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "npbs", targetNamespace = "urn:vim25")
        List<VsanNewPolicyBatch> npbs,
        @WebParam(name = "ignoreSatisfiability", targetNamespace = "urn:vim25")
        Boolean ignoreSatisfiability)
        throws RuntimeFaultFaultMsg, VimFaultFaultMsg
    ;

    /**
     * 
     * @param force
     * @param _this
     * @param uuids
     * @return
     *     returns java.util.List<vim25.HostVsanInternalSystemDeleteVsanObjectsResult>
     * @throws RuntimeFaultFaultMsg
     * @throws VimFaultFaultMsg
     */
    @WebMethod(operationName = "DeleteVsanObjects", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "DeleteVsanObjects", targetNamespace = "urn:vim25", className = "vim25.DeleteVsanObjectsRequestType")
    @ResponseWrapper(localName = "DeleteVsanObjectsResponse", targetNamespace = "urn:vim25", className = "vim25.DeleteVsanObjectsResponse")
    public List<HostVsanInternalSystemDeleteVsanObjectsResult> deleteVsanObjects(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "uuids", targetNamespace = "urn:vim25")
        List<String> uuids,
        @WebParam(name = "force", targetNamespace = "urn:vim25")
        Boolean force)
        throws RuntimeFaultFaultMsg, VimFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param uuids
     * @param newVersion
     * @return
     *     returns java.util.List<vim25.HostVsanInternalSystemVsanObjectOperationResult>
     * @throws RuntimeFaultFaultMsg
     * @throws VsanFaultFaultMsg
     */
    @WebMethod(operationName = "UpgradeVsanObjects", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "UpgradeVsanObjects", targetNamespace = "urn:vim25", className = "vim25.UpgradeVsanObjectsRequestType")
    @ResponseWrapper(localName = "UpgradeVsanObjectsResponse", targetNamespace = "urn:vim25", className = "vim25.UpgradeVsanObjectsResponse")
    public List<HostVsanInternalSystemVsanObjectOperationResult> upgradeVsanObjects(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "uuids", targetNamespace = "urn:vim25")
        List<String> uuids,
        @WebParam(name = "newVersion", targetNamespace = "urn:vim25")
        int newVersion)
        throws RuntimeFaultFaultMsg, VsanFaultFaultMsg
    ;

    /**
     * 
     * @param limit
     * @param _this
     * @param version
     * @param uuids
     * @return
     *     returns java.util.List<java.lang.String>
     * @throws RuntimeFaultFaultMsg
     * @throws VsanFaultFaultMsg
     */
    @WebMethod(operationName = "QueryVsanObjectUuidsByFilter", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryVsanObjectUuidsByFilter", targetNamespace = "urn:vim25", className = "vim25.QueryVsanObjectUuidsByFilterRequestType")
    @ResponseWrapper(localName = "QueryVsanObjectUuidsByFilterResponse", targetNamespace = "urn:vim25", className = "vim25.QueryVsanObjectUuidsByFilterResponse")
    public List<String> queryVsanObjectUuidsByFilter(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "uuids", targetNamespace = "urn:vim25")
        List<String> uuids,
        @WebParam(name = "limit", targetNamespace = "urn:vim25")
        Integer limit,
        @WebParam(name = "version", targetNamespace = "urn:vim25")
        Integer version)
        throws RuntimeFaultFaultMsg, VsanFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param canonicalName
     * @return
     *     returns java.util.List<vim25.VsanHostDiskResult>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryDisksForVsan", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryDisksForVsan", targetNamespace = "urn:vim25", className = "vim25.QueryDisksForVsanRequestType")
    @ResponseWrapper(localName = "QueryDisksForVsanResponse", targetNamespace = "urn:vim25", className = "vim25.QueryDisksForVsanResponse")
    public List<VsanHostDiskResult> queryDisksForVsan(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "canonicalName", targetNamespace = "urn:vim25")
        List<String> canonicalName)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param disk
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "AddDisks_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "AddDisks_Task", targetNamespace = "urn:vim25", className = "vim25.AddDisksRequestType")
    @ResponseWrapper(localName = "AddDisks_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.AddDisksTaskResponse")
    public ManagedObjectReference addDisksTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "disk", targetNamespace = "urn:vim25")
        List<HostScsiDisk> disk)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param mapping
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "InitializeDisks_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "InitializeDisks_Task", targetNamespace = "urn:vim25", className = "vim25.InitializeDisksRequestType")
    @ResponseWrapper(localName = "InitializeDisks_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.InitializeDisksTaskResponse")
    public ManagedObjectReference initializeDisksTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "mapping", targetNamespace = "urn:vim25")
        List<VsanHostDiskMapping> mapping)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param disk
     * @param _this
     * @param maintenanceSpec
     * @param timeout
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RemoveDisk_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RemoveDisk_Task", targetNamespace = "urn:vim25", className = "vim25.RemoveDiskRequestType")
    @ResponseWrapper(localName = "RemoveDisk_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveDiskTaskResponse")
    public ManagedObjectReference removeDiskTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "disk", targetNamespace = "urn:vim25")
        List<HostScsiDisk> disk,
        @WebParam(name = "maintenanceSpec", targetNamespace = "urn:vim25")
        HostMaintenanceSpec maintenanceSpec,
        @WebParam(name = "timeout", targetNamespace = "urn:vim25")
        Integer timeout)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param mapping
     * @param _this
     * @param maintenanceSpec
     * @param timeout
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RemoveDiskMapping_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RemoveDiskMapping_Task", targetNamespace = "urn:vim25", className = "vim25.RemoveDiskMappingRequestType")
    @ResponseWrapper(localName = "RemoveDiskMapping_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveDiskMappingTaskResponse")
    public ManagedObjectReference removeDiskMappingTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "mapping", targetNamespace = "urn:vim25")
        List<VsanHostDiskMapping> mapping,
        @WebParam(name = "maintenanceSpec", targetNamespace = "urn:vim25")
        HostMaintenanceSpec maintenanceSpec,
        @WebParam(name = "timeout", targetNamespace = "urn:vim25")
        Integer timeout)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param mapping
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws VsanFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "UnmountDiskMapping_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "UnmountDiskMapping_Task", targetNamespace = "urn:vim25", className = "vim25.UnmountDiskMappingRequestType")
    @ResponseWrapper(localName = "UnmountDiskMapping_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.UnmountDiskMappingTaskResponse")
    public ManagedObjectReference unmountDiskMappingTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "mapping", targetNamespace = "urn:vim25")
        List<VsanHostDiskMapping> mapping)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, VsanFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param config
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateVsan_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "UpdateVsan_Task", targetNamespace = "urn:vim25", className = "vim25.UpdateVsanRequestType")
    @ResponseWrapper(localName = "UpdateVsan_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateVsanTaskResponse")
    public ManagedObjectReference updateVsanTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "config", targetNamespace = "urn:vim25")
        VsanHostConfigInfo config)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.VsanHostClusterStatus
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryHostStatus", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryHostStatus", targetNamespace = "urn:vim25", className = "vim25.QueryHostStatusRequestType")
    @ResponseWrapper(localName = "QueryHostStatusResponse", targetNamespace = "urn:vim25", className = "vim25.QueryHostStatusResponse")
    public VsanHostClusterStatus queryHostStatus(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param maintenanceSpec
     * @param timeout
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws RequestCanceledFaultMsg
     * @throws VsanFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws TimedoutFaultMsg
     */
    @WebMethod(operationName = "EvacuateVsanNode_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "EvacuateVsanNode_Task", targetNamespace = "urn:vim25", className = "vim25.EvacuateVsanNodeRequestType")
    @ResponseWrapper(localName = "EvacuateVsanNode_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.EvacuateVsanNodeTaskResponse")
    public ManagedObjectReference evacuateVsanNodeTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "maintenanceSpec", targetNamespace = "urn:vim25")
        HostMaintenanceSpec maintenanceSpec,
        @WebParam(name = "timeout", targetNamespace = "urn:vim25")
        int timeout)
        throws InvalidStateFaultMsg, RequestCanceledFaultMsg, RuntimeFaultFaultMsg, TimedoutFaultMsg, VsanFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws VsanFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "RecommissionVsanNode_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RecommissionVsanNode_Task", targetNamespace = "urn:vim25", className = "vim25.RecommissionVsanNodeRequestType")
    @ResponseWrapper(localName = "RecommissionVsanNode_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.RecommissionVsanNodeTaskResponse")
    public ManagedObjectReference recommissionVsanNodeTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, VsanFaultFaultMsg
    ;

    /**
     * 
     * @param name
     * @param _this
     * @return
     *     returns java.util.List<vim25.OptionValue>
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidNameFaultMsg
     */
    @WebMethod(operationName = "QueryOptions", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryOptions", targetNamespace = "urn:vim25", className = "vim25.QueryOptionsRequestType")
    @ResponseWrapper(localName = "QueryOptionsResponse", targetNamespace = "urn:vim25", className = "vim25.QueryOptionsResponse")
    public List<OptionValue> queryOptions(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name)
        throws InvalidNameFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param changedValue
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidNameFaultMsg
     */
    @WebMethod(operationName = "UpdateOptions", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateOptions", targetNamespace = "urn:vim25", className = "vim25.UpdateOptionsRequestType")
    @ResponseWrapper(localName = "UpdateOptionsResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateOptionsResponse")
    public void updateOptions(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "changedValue", targetNamespace = "urn:vim25")
        List<OptionValue> changedValue)
        throws InvalidNameFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param profile
     * @param _this
     * @param entity
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "CheckCompliance_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CheckCompliance_Task", targetNamespace = "urn:vim25", className = "vim25.CheckComplianceRequestType")
    @ResponseWrapper(localName = "CheckCompliance_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CheckComplianceTaskResponse")
    public ManagedObjectReference checkComplianceTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "profile", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> profile,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> entity)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param profile
     * @param _this
     * @param entity
     * @return
     *     returns java.util.List<vim25.ComplianceResult>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryComplianceStatus", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryComplianceStatus", targetNamespace = "urn:vim25", className = "vim25.QueryComplianceStatusRequestType")
    @ResponseWrapper(localName = "QueryComplianceStatusResponse", targetNamespace = "urn:vim25", className = "vim25.QueryComplianceStatusResponse")
    public List<ComplianceResult> queryComplianceStatus(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "profile", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> profile,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> entity)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param profile
     * @param _this
     * @param entity
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ClearComplianceStatus", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ClearComplianceStatus", targetNamespace = "urn:vim25", className = "vim25.ClearComplianceStatusRequestType")
    @ResponseWrapper(localName = "ClearComplianceStatusResponse", targetNamespace = "urn:vim25", className = "vim25.ClearComplianceStatusResponse")
    public void clearComplianceStatus(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "profile", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> profile,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> entity)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param expressionName
     * @param profile
     * @param _this
     * @return
     *     returns java.util.List<vim25.ProfileExpressionMetadata>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryExpressionMetadata", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryExpressionMetadata", targetNamespace = "urn:vim25", className = "vim25.QueryExpressionMetadataRequestType")
    @ResponseWrapper(localName = "QueryExpressionMetadataResponse", targetNamespace = "urn:vim25", className = "vim25.QueryExpressionMetadataResponse")
    public List<ProfileExpressionMetadata> queryExpressionMetadata(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "expressionName", targetNamespace = "urn:vim25")
        List<String> expressionName,
        @WebParam(name = "profile", targetNamespace = "urn:vim25")
        ManagedObjectReference profile)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ProfileDescription
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RetrieveDescription", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RetrieveDescription", targetNamespace = "urn:vim25", className = "vim25.RetrieveDescriptionRequestType")
    @ResponseWrapper(localName = "RetrieveDescriptionResponse", targetNamespace = "urn:vim25", className = "vim25.RetrieveDescriptionResponse")
    public ProfileDescription retrieveDescription(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "DestroyProfile", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DestroyProfile", targetNamespace = "urn:vim25", className = "vim25.DestroyProfileRequestType")
    @ResponseWrapper(localName = "DestroyProfileResponse", targetNamespace = "urn:vim25", className = "vim25.DestroyProfileResponse")
    public void destroyProfile(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param entity
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "AssociateProfile", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "AssociateProfile", targetNamespace = "urn:vim25", className = "vim25.AssociateProfileRequestType")
    @ResponseWrapper(localName = "AssociateProfileResponse", targetNamespace = "urn:vim25", className = "vim25.AssociateProfileResponse")
    public void associateProfile(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> entity)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param entity
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "DissociateProfile", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DissociateProfile", targetNamespace = "urn:vim25", className = "vim25.DissociateProfileRequestType")
    @ResponseWrapper(localName = "DissociateProfileResponse", targetNamespace = "urn:vim25", className = "vim25.DissociateProfileResponse")
    public void dissociateProfile(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> entity)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param entity
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "CheckProfileCompliance_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CheckProfileCompliance_Task", targetNamespace = "urn:vim25", className = "vim25.CheckProfileComplianceRequestType")
    @ResponseWrapper(localName = "CheckProfileCompliance_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CheckProfileComplianceTaskResponse")
    public ManagedObjectReference checkProfileComplianceTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> entity)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ExportProfile", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ExportProfile", targetNamespace = "urn:vim25", className = "vim25.ExportProfileRequestType")
    @ResponseWrapper(localName = "ExportProfileResponse", targetNamespace = "urn:vim25", className = "vim25.ExportProfileResponse")
    public String exportProfile(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param createSpec
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "CreateProfile", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateProfile", targetNamespace = "urn:vim25", className = "vim25.CreateProfileRequestType")
    @ResponseWrapper(localName = "CreateProfileResponse", targetNamespace = "urn:vim25", className = "vim25.CreateProfileResponse")
    public ManagedObjectReference createProfile(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "createSpec", targetNamespace = "urn:vim25")
        ProfileCreateSpec createSpec)
        throws DuplicateNameFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param policyName
     * @param profile
     * @param _this
     * @return
     *     returns java.util.List<vim25.ProfilePolicyMetadata>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryPolicyMetadata", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryPolicyMetadata", targetNamespace = "urn:vim25", className = "vim25.QueryPolicyMetadataRequestType")
    @ResponseWrapper(localName = "QueryPolicyMetadataResponse", targetNamespace = "urn:vim25", className = "vim25.QueryPolicyMetadataResponse")
    public List<ProfilePolicyMetadata> queryPolicyMetadata(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "policyName", targetNamespace = "urn:vim25")
        List<String> policyName,
        @WebParam(name = "profile", targetNamespace = "urn:vim25")
        ManagedObjectReference profile)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param entity
     * @return
     *     returns java.util.List<vim25.ManagedObjectReference>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "FindAssociatedProfile", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "FindAssociatedProfile", targetNamespace = "urn:vim25", className = "vim25.FindAssociatedProfileRequestType")
    @ResponseWrapper(localName = "FindAssociatedProfileResponse", targetNamespace = "urn:vim25", className = "vim25.FindAssociatedProfileResponse")
    public List<ManagedObjectReference> findAssociatedProfile(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        ManagedObjectReference entity)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param config
     * @throws RuntimeFaultFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "UpdateClusterProfile", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateClusterProfile", targetNamespace = "urn:vim25", className = "vim25.UpdateClusterProfileRequestType")
    @ResponseWrapper(localName = "UpdateClusterProfileResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateClusterProfileResponse")
    public void updateClusterProfile(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "config", targetNamespace = "urn:vim25")
        ClusterProfileConfigSpec config)
        throws DuplicateNameFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "UpdateReferenceHost", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateReferenceHost", targetNamespace = "urn:vim25", className = "vim25.UpdateReferenceHostRequestType")
    @ResponseWrapper(localName = "UpdateReferenceHostResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateReferenceHostResponse")
    public void updateReferenceHost(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param config
     * @throws RuntimeFaultFaultMsg
     * @throws ProfileUpdateFailedFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "UpdateHostProfile", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "UpdateHostProfile", targetNamespace = "urn:vim25", className = "vim25.UpdateHostProfileRequestType")
    @ResponseWrapper(localName = "UpdateHostProfileResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateHostProfileResponse")
    public void updateHostProfile(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "config", targetNamespace = "urn:vim25")
        HostProfileConfigSpec config)
        throws DuplicateNameFaultMsg, ProfileUpdateFailedFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param deferredParam
     * @param host
     * @param _this
     * @return
     *     returns vim25.ProfileExecuteResult
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ExecuteHostProfile", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ExecuteHostProfile", targetNamespace = "urn:vim25", className = "vim25.ExecuteHostProfileRequestType")
    @ResponseWrapper(localName = "ExecuteHostProfileResponse", targetNamespace = "urn:vim25", className = "vim25.ExecuteHostProfileResponse")
    public ProfileExecuteResult executeHostProfile(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host,
        @WebParam(name = "deferredParam", targetNamespace = "urn:vim25")
        List<ProfileDeferredPolicyOptionParameter> deferredParam)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param configSpec
     * @param host
     * @param userInput
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws HostConfigFailedFaultMsg
     */
    @WebMethod(operationName = "ApplyHostConfig_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ApplyHostConfig_Task", targetNamespace = "urn:vim25", className = "vim25.ApplyHostConfigRequestType")
    @ResponseWrapper(localName = "ApplyHostConfig_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ApplyHostConfigTaskResponse")
    public ManagedObjectReference applyHostConfigTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host,
        @WebParam(name = "configSpec", targetNamespace = "urn:vim25")
        HostConfigSpec configSpec,
        @WebParam(name = "userInput", targetNamespace = "urn:vim25")
        List<ProfileDeferredPolicyOptionParameter> userInput)
        throws HostConfigFailedFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param configSpec
     * @param host
     * @param _this
     * @return
     *     returns vim25.HostProfileManagerConfigTaskList
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "GenerateConfigTaskList", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "GenerateConfigTaskList", targetNamespace = "urn:vim25", className = "vim25.GenerateConfigTaskListRequestType")
    @ResponseWrapper(localName = "GenerateConfigTaskListResponse", targetNamespace = "urn:vim25", className = "vim25.GenerateConfigTaskListResponse")
    public HostProfileManagerConfigTaskList generateConfigTaskList(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "configSpec", targetNamespace = "urn:vim25")
        HostConfigSpec configSpec,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param configSpec
     * @param host
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "GenerateHostProfileTaskList_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "GenerateHostProfileTaskList_Task", targetNamespace = "urn:vim25", className = "vim25.GenerateHostProfileTaskListRequestType")
    @ResponseWrapper(localName = "GenerateHostProfileTaskList_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.GenerateHostProfileTaskListTaskResponse")
    public ManagedObjectReference generateHostProfileTaskListTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "configSpec", targetNamespace = "urn:vim25")
        HostConfigSpec configSpec,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param profileName
     * @param profile
     * @param _this
     * @return
     *     returns java.util.List<vim25.ProfileMetadata>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryHostProfileMetadata", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryHostProfileMetadata", targetNamespace = "urn:vim25", className = "vim25.QueryHostProfileMetadataRequestType")
    @ResponseWrapper(localName = "QueryHostProfileMetadataResponse", targetNamespace = "urn:vim25", className = "vim25.QueryHostProfileMetadataResponse")
    public List<ProfileMetadata> queryHostProfileMetadata(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "profileName", targetNamespace = "urn:vim25")
        List<String> profileName,
        @WebParam(name = "profile", targetNamespace = "urn:vim25")
        ManagedObjectReference profile)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param profile
     * @param _this
     * @return
     *     returns vim25.ProfileProfileStructure
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryProfileStructure", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryProfileStructure", targetNamespace = "urn:vim25", className = "vim25.QueryProfileStructureRequestType")
    @ResponseWrapper(localName = "QueryProfileStructureResponse", targetNamespace = "urn:vim25", className = "vim25.QueryProfileStructureResponse")
    public ProfileProfileStructure queryProfileStructure(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "profile", targetNamespace = "urn:vim25")
        ManagedObjectReference profile)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param profileType
     * @param profileTypeName
     * @param profile
     * @param _this
     * @return
     *     returns vim25.ApplyProfile
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "CreateDefaultProfile", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateDefaultProfile", targetNamespace = "urn:vim25", className = "vim25.CreateDefaultProfileRequestType")
    @ResponseWrapper(localName = "CreateDefaultProfileResponse", targetNamespace = "urn:vim25", className = "vim25.CreateDefaultProfileResponse")
    public ApplyProfile createDefaultProfile(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "profileType", targetNamespace = "urn:vim25")
        String profileType,
        @WebParam(name = "profileTypeName", targetNamespace = "urn:vim25")
        String profileTypeName,
        @WebParam(name = "profile", targetNamespace = "urn:vim25")
        ManagedObjectReference profile)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param configSpec
     * @param host
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws AnswerFileUpdateFailedFaultMsg
     */
    @WebMethod(operationName = "UpdateAnswerFile_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "UpdateAnswerFile_Task", targetNamespace = "urn:vim25", className = "vim25.UpdateAnswerFileRequestType")
    @ResponseWrapper(localName = "UpdateAnswerFile_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.UpdateAnswerFileTaskResponse")
    public ManagedObjectReference updateAnswerFileTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host,
        @WebParam(name = "configSpec", targetNamespace = "urn:vim25")
        AnswerFileCreateSpec configSpec)
        throws AnswerFileUpdateFailedFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @return
     *     returns vim25.AnswerFile
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RetrieveAnswerFile", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RetrieveAnswerFile", targetNamespace = "urn:vim25", className = "vim25.RetrieveAnswerFileRequestType")
    @ResponseWrapper(localName = "RetrieveAnswerFileResponse", targetNamespace = "urn:vim25", className = "vim25.RetrieveAnswerFileResponse")
    public AnswerFile retrieveAnswerFile(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param applyProfile
     * @param _this
     * @return
     *     returns vim25.AnswerFile
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RetrieveAnswerFileForProfile", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RetrieveAnswerFileForProfile", targetNamespace = "urn:vim25", className = "vim25.RetrieveAnswerFileForProfileRequestType")
    @ResponseWrapper(localName = "RetrieveAnswerFileForProfileResponse", targetNamespace = "urn:vim25", className = "vim25.RetrieveAnswerFileForProfileResponse")
    public AnswerFile retrieveAnswerFileForProfile(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host,
        @WebParam(name = "applyProfile", targetNamespace = "urn:vim25")
        HostApplyProfile applyProfile)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ExportAnswerFile_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ExportAnswerFile_Task", targetNamespace = "urn:vim25", className = "vim25.ExportAnswerFileRequestType")
    @ResponseWrapper(localName = "ExportAnswerFile_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.ExportAnswerFileTaskResponse")
    public ManagedObjectReference exportAnswerFileTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "CheckAnswerFileStatus_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CheckAnswerFileStatus_Task", targetNamespace = "urn:vim25", className = "vim25.CheckAnswerFileStatusRequestType")
    @ResponseWrapper(localName = "CheckAnswerFileStatus_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CheckAnswerFileStatusTaskResponse")
    public ManagedObjectReference checkAnswerFileStatusTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> host)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param host
     * @param _this
     * @return
     *     returns java.util.List<vim25.AnswerFileStatusResult>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryAnswerFileStatus", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryAnswerFileStatus", targetNamespace = "urn:vim25", className = "vim25.QueryAnswerFileStatusRequestType")
    @ResponseWrapper(localName = "QueryAnswerFileStatusResponse", targetNamespace = "urn:vim25", className = "vim25.QueryAnswerFileStatusResponse")
    public List<AnswerFileStatusResult> queryAnswerFileStatus(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> host)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "RemoveScheduledTask", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RemoveScheduledTask", targetNamespace = "urn:vim25", className = "vim25.RemoveScheduledTaskRequestType")
    @ResponseWrapper(localName = "RemoveScheduledTaskResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveScheduledTaskResponse")
    public void removeScheduledTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param spec
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "ReconfigureScheduledTask", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ReconfigureScheduledTask", targetNamespace = "urn:vim25", className = "vim25.ReconfigureScheduledTaskRequestType")
    @ResponseWrapper(localName = "ReconfigureScheduledTaskResponse", targetNamespace = "urn:vim25", className = "vim25.ReconfigureScheduledTaskResponse")
    public void reconfigureScheduledTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        ScheduledTaskSpec spec)
        throws DuplicateNameFaultMsg, InvalidNameFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "RunScheduledTask", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RunScheduledTask", targetNamespace = "urn:vim25", className = "vim25.RunScheduledTaskRequestType")
    @ResponseWrapper(localName = "RunScheduledTaskResponse", targetNamespace = "urn:vim25", className = "vim25.RunScheduledTaskResponse")
    public void runScheduledTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param entity
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "CreateScheduledTask", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateScheduledTask", targetNamespace = "urn:vim25", className = "vim25.CreateScheduledTaskRequestType")
    @ResponseWrapper(localName = "CreateScheduledTaskResponse", targetNamespace = "urn:vim25", className = "vim25.CreateScheduledTaskResponse")
    public ManagedObjectReference createScheduledTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        ManagedObjectReference entity,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        ScheduledTaskSpec spec)
        throws DuplicateNameFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param entity
     * @return
     *     returns java.util.List<vim25.ManagedObjectReference>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RetrieveEntityScheduledTask", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RetrieveEntityScheduledTask", targetNamespace = "urn:vim25", className = "vim25.RetrieveEntityScheduledTaskRequestType")
    @ResponseWrapper(localName = "RetrieveEntityScheduledTaskResponse", targetNamespace = "urn:vim25", className = "vim25.RetrieveEntityScheduledTaskResponse")
    public List<ManagedObjectReference> retrieveEntityScheduledTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        ManagedObjectReference entity)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param obj
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidNameFaultMsg
     * @throws DuplicateNameFaultMsg
     */
    @WebMethod(operationName = "CreateObjectScheduledTask", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateObjectScheduledTask", targetNamespace = "urn:vim25", className = "vim25.CreateObjectScheduledTaskRequestType")
    @ResponseWrapper(localName = "CreateObjectScheduledTaskResponse", targetNamespace = "urn:vim25", className = "vim25.CreateObjectScheduledTaskResponse")
    public ManagedObjectReference createObjectScheduledTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "obj", targetNamespace = "urn:vim25")
        ManagedObjectReference obj,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        ScheduledTaskSpec spec)
        throws DuplicateNameFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param obj
     * @param _this
     * @return
     *     returns java.util.List<vim25.ManagedObjectReference>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "RetrieveObjectScheduledTask", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RetrieveObjectScheduledTask", targetNamespace = "urn:vim25", className = "vim25.RetrieveObjectScheduledTaskRequestType")
    @ResponseWrapper(localName = "RetrieveObjectScheduledTaskResponse", targetNamespace = "urn:vim25", className = "vim25.RetrieveObjectScheduledTaskResponse")
    public List<ManagedObjectReference> retrieveObjectScheduledTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "obj", targetNamespace = "urn:vim25")
        ManagedObjectReference obj)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param entity
     * @return
     *     returns java.util.List<vim25.ManagedObjectReference>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "OpenInventoryViewFolder", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "OpenInventoryViewFolder", targetNamespace = "urn:vim25", className = "vim25.OpenInventoryViewFolderRequestType")
    @ResponseWrapper(localName = "OpenInventoryViewFolderResponse", targetNamespace = "urn:vim25", className = "vim25.OpenInventoryViewFolderResponse")
    public List<ManagedObjectReference> openInventoryViewFolder(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> entity)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @param entity
     * @return
     *     returns java.util.List<vim25.ManagedObjectReference>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "CloseInventoryViewFolder", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CloseInventoryViewFolder", targetNamespace = "urn:vim25", className = "vim25.CloseInventoryViewFolderRequestType")
    @ResponseWrapper(localName = "CloseInventoryViewFolderResponse", targetNamespace = "urn:vim25", className = "vim25.CloseInventoryViewFolderResponse")
    public List<ManagedObjectReference> closeInventoryViewFolder(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "entity", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> entity)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param add
     * @param _this
     * @param remove
     * @return
     *     returns java.util.List<vim25.ManagedObjectReference>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ModifyListView", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ModifyListView", targetNamespace = "urn:vim25", className = "vim25.ModifyListViewRequestType")
    @ResponseWrapper(localName = "ModifyListViewResponse", targetNamespace = "urn:vim25", className = "vim25.ModifyListViewResponse")
    public List<ManagedObjectReference> modifyListView(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "add", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> add,
        @WebParam(name = "remove", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> remove)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param obj
     * @param _this
     * @return
     *     returns java.util.List<vim25.ManagedObjectReference>
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ResetListView", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ResetListView", targetNamespace = "urn:vim25", className = "vim25.ResetListViewRequestType")
    @ResponseWrapper(localName = "ResetListViewResponse", targetNamespace = "urn:vim25", className = "vim25.ResetListViewResponse")
    public List<ManagedObjectReference> resetListView(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "obj", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> obj)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param view
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "ResetListViewFromView", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ResetListViewFromView", targetNamespace = "urn:vim25", className = "vim25.ResetListViewFromViewRequestType")
    @ResponseWrapper(localName = "ResetListViewFromViewResponse", targetNamespace = "urn:vim25", className = "vim25.ResetListViewFromViewResponse")
    public void resetListViewFromView(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "view", targetNamespace = "urn:vim25")
        ManagedObjectReference view)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "DestroyView", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DestroyView", targetNamespace = "urn:vim25", className = "vim25.DestroyViewRequestType")
    @ResponseWrapper(localName = "DestroyViewResponse", targetNamespace = "urn:vim25", className = "vim25.DestroyViewResponse")
    public void destroyView(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "CreateInventoryView", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateInventoryView", targetNamespace = "urn:vim25", className = "vim25.CreateInventoryViewRequestType")
    @ResponseWrapper(localName = "CreateInventoryViewResponse", targetNamespace = "urn:vim25", className = "vim25.CreateInventoryViewResponse")
    public ManagedObjectReference createInventoryView(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param container
     * @param _this
     * @param type
     * @param recursive
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "CreateContainerView", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateContainerView", targetNamespace = "urn:vim25", className = "vim25.CreateContainerViewRequestType")
    @ResponseWrapper(localName = "CreateContainerViewResponse", targetNamespace = "urn:vim25", className = "vim25.CreateContainerViewResponse")
    public ManagedObjectReference createContainerView(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "container", targetNamespace = "urn:vim25")
        ManagedObjectReference container,
        @WebParam(name = "type", targetNamespace = "urn:vim25")
        List<String> type,
        @WebParam(name = "recursive", targetNamespace = "urn:vim25")
        boolean recursive)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param obj
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "CreateListView", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateListView", targetNamespace = "urn:vim25", className = "vim25.CreateListViewRequestType")
    @ResponseWrapper(localName = "CreateListViewResponse", targetNamespace = "urn:vim25", className = "vim25.CreateListViewResponse")
    public ManagedObjectReference createListView(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "obj", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> obj)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param view
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "CreateListViewFromView", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateListViewFromView", targetNamespace = "urn:vim25", className = "vim25.CreateListViewFromViewRequestType")
    @ResponseWrapper(localName = "CreateListViewFromViewResponse", targetNamespace = "urn:vim25", className = "vim25.CreateListViewFromViewResponse")
    public ManagedObjectReference createListViewFromView(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "view", targetNamespace = "urn:vim25")
        ManagedObjectReference view)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param suppressPowerOn
     * @param host
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InsufficientResourcesFaultFaultMsg
     * @throws VmConfigFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "RevertToSnapshot_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RevertToSnapshot_Task", targetNamespace = "urn:vim25", className = "vim25.RevertToSnapshotRequestType")
    @ResponseWrapper(localName = "RevertToSnapshot_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.RevertToSnapshotTaskResponse")
    public ManagedObjectReference revertToSnapshotTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host,
        @WebParam(name = "suppressPowerOn", targetNamespace = "urn:vim25")
        Boolean suppressPowerOn)
        throws FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg
    ;

    /**
     * 
     * @param removeChildren
     * @param consolidate
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     */
    @WebMethod(operationName = "RemoveSnapshot_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "RemoveSnapshot_Task", targetNamespace = "urn:vim25", className = "vim25.RemoveSnapshotRequestType")
    @ResponseWrapper(localName = "RemoveSnapshot_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveSnapshotTaskResponse")
    public ManagedObjectReference removeSnapshotTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "removeChildren", targetNamespace = "urn:vim25")
        boolean removeChildren,
        @WebParam(name = "consolidate", targetNamespace = "urn:vim25")
        Boolean consolidate)
        throws RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param name
     * @param description
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws InvalidNameFaultMsg
     */
    @WebMethod(operationName = "RenameSnapshot", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RenameSnapshot", targetNamespace = "urn:vim25", className = "vim25.RenameSnapshotRequestType")
    @ResponseWrapper(localName = "RenameSnapshotResponse", targetNamespace = "urn:vim25", className = "vim25.RenameSnapshotResponse")
    public void renameSnapshot(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "name", targetNamespace = "urn:vim25")
        String name,
        @WebParam(name = "description", targetNamespace = "urn:vim25")
        String description)
        throws InvalidNameFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "ExportSnapshot", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ExportSnapshot", targetNamespace = "urn:vim25", className = "vim25.ExportSnapshotRequestType")
    @ResponseWrapper(localName = "ExportSnapshotResponse", targetNamespace = "urn:vim25", className = "vim25.ExportSnapshotResponse")
    public ManagedObjectReference exportSnapshot(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this)
        throws FileFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param vm
     * @param host
     * @param pool
     * @param testType
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws NoActiveHostInClusterFaultMsg
     */
    @WebMethod(operationName = "CheckCompatibility_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CheckCompatibility_Task", targetNamespace = "urn:vim25", className = "vim25.CheckCompatibilityRequestType")
    @ResponseWrapper(localName = "CheckCompatibility_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CheckCompatibilityTaskResponse")
    public ManagedObjectReference checkCompatibilityTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host,
        @WebParam(name = "pool", targetNamespace = "urn:vim25")
        ManagedObjectReference pool,
        @WebParam(name = "testType", targetNamespace = "urn:vim25")
        List<String> testType)
        throws InvalidStateFaultMsg, NoActiveHostInClusterFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vm
     * @param host
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     */
    @WebMethod(operationName = "QueryVMotionCompatibilityEx_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "QueryVMotionCompatibilityEx_Task", targetNamespace = "urn:vim25", className = "vim25.QueryVMotionCompatibilityExRequestType")
    @ResponseWrapper(localName = "QueryVMotionCompatibilityEx_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.QueryVMotionCompatibilityExTaskResponse")
    public ManagedObjectReference queryVMotionCompatibilityExTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> vm,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        List<ManagedObjectReference> host)
        throws RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vm
     * @param host
     * @param pool
     * @param testType
     * @param state
     * @param _this
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "CheckMigrate_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CheckMigrate_Task", targetNamespace = "urn:vim25", className = "vim25.CheckMigrateRequestType")
    @ResponseWrapper(localName = "CheckMigrate_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CheckMigrateTaskResponse")
    public ManagedObjectReference checkMigrateTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "host", targetNamespace = "urn:vim25")
        ManagedObjectReference host,
        @WebParam(name = "pool", targetNamespace = "urn:vim25")
        ManagedObjectReference pool,
        @WebParam(name = "state", targetNamespace = "urn:vim25")
        VirtualMachinePowerState state,
        @WebParam(name = "testType", targetNamespace = "urn:vim25")
        List<String> testType)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param vm
     * @param testType
     * @param _this
     * @param spec
     * @return
     *     returns vim25.ManagedObjectReference
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "CheckRelocate_Task", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CheckRelocate_Task", targetNamespace = "urn:vim25", className = "vim25.CheckRelocateRequestType")
    @ResponseWrapper(localName = "CheckRelocate_TaskResponse", targetNamespace = "urn:vim25", className = "vim25.CheckRelocateTaskResponse")
    public ManagedObjectReference checkRelocateTask(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        VirtualMachineRelocateSpec spec,
        @WebParam(name = "testType", targetNamespace = "urn:vim25")
        List<String> testType)
        throws InvalidStateFaultMsg, RuntimeFaultFaultMsg
    ;

    /**
     * 
     * @param auth
     * @param aliasInfo
     * @param vm
     * @param mapCert
     * @param base64Cert
     * @param _this
     * @param username
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "AddGuestAlias", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "AddGuestAlias", targetNamespace = "urn:vim25", className = "vim25.AddGuestAliasRequestType")
    @ResponseWrapper(localName = "AddGuestAliasResponse", targetNamespace = "urn:vim25", className = "vim25.AddGuestAliasResponse")
    public void addGuestAlias(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "username", targetNamespace = "urn:vim25")
        String username,
        @WebParam(name = "mapCert", targetNamespace = "urn:vim25")
        boolean mapCert,
        @WebParam(name = "base64Cert", targetNamespace = "urn:vim25")
        String base64Cert,
        @WebParam(name = "aliasInfo", targetNamespace = "urn:vim25")
        GuestAuthAliasInfo aliasInfo)
        throws GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param auth
     * @param subject
     * @param vm
     * @param base64Cert
     * @param _this
     * @param username
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "RemoveGuestAlias", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RemoveGuestAlias", targetNamespace = "urn:vim25", className = "vim25.RemoveGuestAliasRequestType")
    @ResponseWrapper(localName = "RemoveGuestAliasResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveGuestAliasResponse")
    public void removeGuestAlias(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "username", targetNamespace = "urn:vim25")
        String username,
        @WebParam(name = "base64Cert", targetNamespace = "urn:vim25")
        String base64Cert,
        @WebParam(name = "subject", targetNamespace = "urn:vim25")
        GuestAuthSubject subject)
        throws GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param auth
     * @param vm
     * @param base64Cert
     * @param _this
     * @param username
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "RemoveGuestAliasByCert", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "RemoveGuestAliasByCert", targetNamespace = "urn:vim25", className = "vim25.RemoveGuestAliasByCertRequestType")
    @ResponseWrapper(localName = "RemoveGuestAliasByCertResponse", targetNamespace = "urn:vim25", className = "vim25.RemoveGuestAliasByCertResponse")
    public void removeGuestAliasByCert(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "username", targetNamespace = "urn:vim25")
        String username,
        @WebParam(name = "base64Cert", targetNamespace = "urn:vim25")
        String base64Cert)
        throws GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param auth
     * @param vm
     * @param _this
     * @param username
     * @return
     *     returns java.util.List<vim25.GuestAliases>
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "ListGuestAliases", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ListGuestAliases", targetNamespace = "urn:vim25", className = "vim25.ListGuestAliasesRequestType")
    @ResponseWrapper(localName = "ListGuestAliasesResponse", targetNamespace = "urn:vim25", className = "vim25.ListGuestAliasesResponse")
    public List<GuestAliases> listGuestAliases(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "username", targetNamespace = "urn:vim25")
        String username)
        throws GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param auth
     * @param vm
     * @param _this
     * @return
     *     returns java.util.List<vim25.GuestMappedAliases>
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "ListGuestMappedAliases", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ListGuestMappedAliases", targetNamespace = "urn:vim25", className = "vim25.ListGuestMappedAliasesRequestType")
    @ResponseWrapper(localName = "ListGuestMappedAliasesResponse", targetNamespace = "urn:vim25", className = "vim25.ListGuestMappedAliasesResponse")
    public List<GuestMappedAliases> listGuestMappedAliases(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth)
        throws GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param auth
     * @param vm
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "ValidateCredentialsInGuest", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ValidateCredentialsInGuest", targetNamespace = "urn:vim25", className = "vim25.ValidateCredentialsInGuestRequestType")
    @ResponseWrapper(localName = "ValidateCredentialsInGuestResponse", targetNamespace = "urn:vim25", className = "vim25.ValidateCredentialsInGuestResponse")
    public void validateCredentialsInGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth)
        throws GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param requestedAuth
     * @param vm
     * @param sessionID
     * @param _this
     * @return
     *     returns vim25.GuestAuthentication
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "AcquireCredentialsInGuest", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "AcquireCredentialsInGuest", targetNamespace = "urn:vim25", className = "vim25.AcquireCredentialsInGuestRequestType")
    @ResponseWrapper(localName = "AcquireCredentialsInGuestResponse", targetNamespace = "urn:vim25", className = "vim25.AcquireCredentialsInGuestResponse")
    public GuestAuthentication acquireCredentialsInGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "requestedAuth", targetNamespace = "urn:vim25")
        GuestAuthentication requestedAuth,
        @WebParam(name = "sessionID", targetNamespace = "urn:vim25")
        Long sessionID)
        throws GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param auth
     * @param vm
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "ReleaseCredentialsInGuest", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ReleaseCredentialsInGuest", targetNamespace = "urn:vim25", className = "vim25.ReleaseCredentialsInGuestRequestType")
    @ResponseWrapper(localName = "ReleaseCredentialsInGuestResponse", targetNamespace = "urn:vim25", className = "vim25.ReleaseCredentialsInGuestResponse")
    public void releaseCredentialsInGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth)
        throws GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param directoryPath
     * @param auth
     * @param createParentDirectories
     * @param vm
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "MakeDirectoryInGuest", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "MakeDirectoryInGuest", targetNamespace = "urn:vim25", className = "vim25.MakeDirectoryInGuestRequestType")
    @ResponseWrapper(localName = "MakeDirectoryInGuestResponse", targetNamespace = "urn:vim25", className = "vim25.MakeDirectoryInGuestResponse")
    public void makeDirectoryInGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "directoryPath", targetNamespace = "urn:vim25")
        String directoryPath,
        @WebParam(name = "createParentDirectories", targetNamespace = "urn:vim25")
        boolean createParentDirectories)
        throws FileFaultFaultMsg, GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param auth
     * @param vm
     * @param filePath
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "DeleteFileInGuest", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DeleteFileInGuest", targetNamespace = "urn:vim25", className = "vim25.DeleteFileInGuestRequestType")
    @ResponseWrapper(localName = "DeleteFileInGuestResponse", targetNamespace = "urn:vim25", className = "vim25.DeleteFileInGuestResponse")
    public void deleteFileInGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "filePath", targetNamespace = "urn:vim25")
        String filePath)
        throws FileFaultFaultMsg, GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param directoryPath
     * @param auth
     * @param vm
     * @param _this
     * @param recursive
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "DeleteDirectoryInGuest", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DeleteDirectoryInGuest", targetNamespace = "urn:vim25", className = "vim25.DeleteDirectoryInGuestRequestType")
    @ResponseWrapper(localName = "DeleteDirectoryInGuestResponse", targetNamespace = "urn:vim25", className = "vim25.DeleteDirectoryInGuestResponse")
    public void deleteDirectoryInGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "directoryPath", targetNamespace = "urn:vim25")
        String directoryPath,
        @WebParam(name = "recursive", targetNamespace = "urn:vim25")
        boolean recursive)
        throws FileFaultFaultMsg, GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param auth
     * @param vm
     * @param srcDirectoryPath
     * @param _this
     * @param dstDirectoryPath
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "MoveDirectoryInGuest", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "MoveDirectoryInGuest", targetNamespace = "urn:vim25", className = "vim25.MoveDirectoryInGuestRequestType")
    @ResponseWrapper(localName = "MoveDirectoryInGuestResponse", targetNamespace = "urn:vim25", className = "vim25.MoveDirectoryInGuestResponse")
    public void moveDirectoryInGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "srcDirectoryPath", targetNamespace = "urn:vim25")
        String srcDirectoryPath,
        @WebParam(name = "dstDirectoryPath", targetNamespace = "urn:vim25")
        String dstDirectoryPath)
        throws FileFaultFaultMsg, GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param dstFilePath
     * @param auth
     * @param vm
     * @param _this
     * @param srcFilePath
     * @param overwrite
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "MoveFileInGuest", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "MoveFileInGuest", targetNamespace = "urn:vim25", className = "vim25.MoveFileInGuestRequestType")
    @ResponseWrapper(localName = "MoveFileInGuestResponse", targetNamespace = "urn:vim25", className = "vim25.MoveFileInGuestResponse")
    public void moveFileInGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "srcFilePath", targetNamespace = "urn:vim25")
        String srcFilePath,
        @WebParam(name = "dstFilePath", targetNamespace = "urn:vim25")
        String dstFilePath,
        @WebParam(name = "overwrite", targetNamespace = "urn:vim25")
        boolean overwrite)
        throws FileFaultFaultMsg, GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param directoryPath
     * @param auth
     * @param prefix
     * @param vm
     * @param _this
     * @param suffix
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "CreateTemporaryFileInGuest", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateTemporaryFileInGuest", targetNamespace = "urn:vim25", className = "vim25.CreateTemporaryFileInGuestRequestType")
    @ResponseWrapper(localName = "CreateTemporaryFileInGuestResponse", targetNamespace = "urn:vim25", className = "vim25.CreateTemporaryFileInGuestResponse")
    public String createTemporaryFileInGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "prefix", targetNamespace = "urn:vim25")
        String prefix,
        @WebParam(name = "suffix", targetNamespace = "urn:vim25")
        String suffix,
        @WebParam(name = "directoryPath", targetNamespace = "urn:vim25")
        String directoryPath)
        throws FileFaultFaultMsg, GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param directoryPath
     * @param auth
     * @param prefix
     * @param vm
     * @param _this
     * @param suffix
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "CreateTemporaryDirectoryInGuest", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "CreateTemporaryDirectoryInGuest", targetNamespace = "urn:vim25", className = "vim25.CreateTemporaryDirectoryInGuestRequestType")
    @ResponseWrapper(localName = "CreateTemporaryDirectoryInGuestResponse", targetNamespace = "urn:vim25", className = "vim25.CreateTemporaryDirectoryInGuestResponse")
    public String createTemporaryDirectoryInGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "prefix", targetNamespace = "urn:vim25")
        String prefix,
        @WebParam(name = "suffix", targetNamespace = "urn:vim25")
        String suffix,
        @WebParam(name = "directoryPath", targetNamespace = "urn:vim25")
        String directoryPath)
        throws FileFaultFaultMsg, GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param auth
     * @param maxResults
     * @param vm
     * @param filePath
     * @param index
     * @param _this
     * @param matchPattern
     * @return
     *     returns vim25.GuestListFileInfo
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "ListFilesInGuest", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ListFilesInGuest", targetNamespace = "urn:vim25", className = "vim25.ListFilesInGuestRequestType")
    @ResponseWrapper(localName = "ListFilesInGuestResponse", targetNamespace = "urn:vim25", className = "vim25.ListFilesInGuestResponse")
    public GuestListFileInfo listFilesInGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "filePath", targetNamespace = "urn:vim25")
        String filePath,
        @WebParam(name = "index", targetNamespace = "urn:vim25")
        Integer index,
        @WebParam(name = "maxResults", targetNamespace = "urn:vim25")
        Integer maxResults,
        @WebParam(name = "matchPattern", targetNamespace = "urn:vim25")
        String matchPattern)
        throws FileFaultFaultMsg, GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param fileAttributes
     * @param auth
     * @param vm
     * @param guestFilePath
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "ChangeFileAttributesInGuest", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "ChangeFileAttributesInGuest", targetNamespace = "urn:vim25", className = "vim25.ChangeFileAttributesInGuestRequestType")
    @ResponseWrapper(localName = "ChangeFileAttributesInGuestResponse", targetNamespace = "urn:vim25", className = "vim25.ChangeFileAttributesInGuestResponse")
    public void changeFileAttributesInGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "guestFilePath", targetNamespace = "urn:vim25")
        String guestFilePath,
        @WebParam(name = "fileAttributes", targetNamespace = "urn:vim25")
        GuestFileAttributes fileAttributes)
        throws FileFaultFaultMsg, GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param auth
     * @param vm
     * @param guestFilePath
     * @param _this
     * @return
     *     returns vim25.FileTransferInformation
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "InitiateFileTransferFromGuest", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "InitiateFileTransferFromGuest", targetNamespace = "urn:vim25", className = "vim25.InitiateFileTransferFromGuestRequestType")
    @ResponseWrapper(localName = "InitiateFileTransferFromGuestResponse", targetNamespace = "urn:vim25", className = "vim25.InitiateFileTransferFromGuestResponse")
    public FileTransferInformation initiateFileTransferFromGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "guestFilePath", targetNamespace = "urn:vim25")
        String guestFilePath)
        throws FileFaultFaultMsg, GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param fileAttributes
     * @param auth
     * @param fileSize
     * @param vm
     * @param guestFilePath
     * @param _this
     * @param overwrite
     * @return
     *     returns java.lang.String
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "InitiateFileTransferToGuest", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "InitiateFileTransferToGuest", targetNamespace = "urn:vim25", className = "vim25.InitiateFileTransferToGuestRequestType")
    @ResponseWrapper(localName = "InitiateFileTransferToGuestResponse", targetNamespace = "urn:vim25", className = "vim25.InitiateFileTransferToGuestResponse")
    public String initiateFileTransferToGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "guestFilePath", targetNamespace = "urn:vim25")
        String guestFilePath,
        @WebParam(name = "fileAttributes", targetNamespace = "urn:vim25")
        GuestFileAttributes fileAttributes,
        @WebParam(name = "fileSize", targetNamespace = "urn:vim25")
        long fileSize,
        @WebParam(name = "overwrite", targetNamespace = "urn:vim25")
        boolean overwrite)
        throws FileFaultFaultMsg, GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param auth
     * @param vm
     * @param _this
     * @param spec
     * @return
     *     returns long
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     * @throws FileFaultFaultMsg
     */
    @WebMethod(operationName = "StartProgramInGuest", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "StartProgramInGuest", targetNamespace = "urn:vim25", className = "vim25.StartProgramInGuestRequestType")
    @ResponseWrapper(localName = "StartProgramInGuestResponse", targetNamespace = "urn:vim25", className = "vim25.StartProgramInGuestResponse")
    public long startProgramInGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "spec", targetNamespace = "urn:vim25")
        GuestProgramSpec spec)
        throws FileFaultFaultMsg, GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param auth
     * @param vm
     * @param _this
     * @param pids
     * @return
     *     returns java.util.List<vim25.GuestProcessInfo>
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "ListProcessesInGuest", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ListProcessesInGuest", targetNamespace = "urn:vim25", className = "vim25.ListProcessesInGuestRequestType")
    @ResponseWrapper(localName = "ListProcessesInGuestResponse", targetNamespace = "urn:vim25", className = "vim25.ListProcessesInGuestResponse")
    public List<GuestProcessInfo> listProcessesInGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "pids", targetNamespace = "urn:vim25")
        List<Long> pids)
        throws GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param auth
     * @param vm
     * @param pid
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "TerminateProcessInGuest", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "TerminateProcessInGuest", targetNamespace = "urn:vim25", className = "vim25.TerminateProcessInGuestRequestType")
    @ResponseWrapper(localName = "TerminateProcessInGuestResponse", targetNamespace = "urn:vim25", className = "vim25.TerminateProcessInGuestResponse")
    public void terminateProcessInGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "pid", targetNamespace = "urn:vim25")
        long pid)
        throws GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param names
     * @param auth
     * @param vm
     * @param _this
     * @return
     *     returns java.util.List<java.lang.String>
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "ReadEnvironmentVariableInGuest", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ReadEnvironmentVariableInGuest", targetNamespace = "urn:vim25", className = "vim25.ReadEnvironmentVariableInGuestRequestType")
    @ResponseWrapper(localName = "ReadEnvironmentVariableInGuestResponse", targetNamespace = "urn:vim25", className = "vim25.ReadEnvironmentVariableInGuestResponse")
    public List<String> readEnvironmentVariableInGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "names", targetNamespace = "urn:vim25")
        List<String> names)
        throws GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param auth
     * @param vm
     * @param keyName
     * @param isVolatile
     * @param _this
     * @param classType
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "CreateRegistryKeyInGuest", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "CreateRegistryKeyInGuest", targetNamespace = "urn:vim25", className = "vim25.CreateRegistryKeyInGuestRequestType")
    @ResponseWrapper(localName = "CreateRegistryKeyInGuestResponse", targetNamespace = "urn:vim25", className = "vim25.CreateRegistryKeyInGuestResponse")
    public void createRegistryKeyInGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "keyName", targetNamespace = "urn:vim25")
        GuestRegKeyNameSpec keyName,
        @WebParam(name = "isVolatile", targetNamespace = "urn:vim25")
        boolean isVolatile,
        @WebParam(name = "classType", targetNamespace = "urn:vim25")
        String classType)
        throws GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param auth
     * @param vm
     * @param keyName
     * @param _this
     * @param recursive
     * @param matchPattern
     * @return
     *     returns java.util.List<vim25.GuestRegKeyRecordSpec>
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "ListRegistryKeysInGuest", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ListRegistryKeysInGuest", targetNamespace = "urn:vim25", className = "vim25.ListRegistryKeysInGuestRequestType")
    @ResponseWrapper(localName = "ListRegistryKeysInGuestResponse", targetNamespace = "urn:vim25", className = "vim25.ListRegistryKeysInGuestResponse")
    public List<GuestRegKeyRecordSpec> listRegistryKeysInGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "keyName", targetNamespace = "urn:vim25")
        GuestRegKeyNameSpec keyName,
        @WebParam(name = "recursive", targetNamespace = "urn:vim25")
        boolean recursive,
        @WebParam(name = "matchPattern", targetNamespace = "urn:vim25")
        String matchPattern)
        throws GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param auth
     * @param vm
     * @param keyName
     * @param _this
     * @param recursive
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "DeleteRegistryKeyInGuest", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DeleteRegistryKeyInGuest", targetNamespace = "urn:vim25", className = "vim25.DeleteRegistryKeyInGuestRequestType")
    @ResponseWrapper(localName = "DeleteRegistryKeyInGuestResponse", targetNamespace = "urn:vim25", className = "vim25.DeleteRegistryKeyInGuestResponse")
    public void deleteRegistryKeyInGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "keyName", targetNamespace = "urn:vim25")
        GuestRegKeyNameSpec keyName,
        @WebParam(name = "recursive", targetNamespace = "urn:vim25")
        boolean recursive)
        throws GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param auth
     * @param vm
     * @param _this
     * @param value
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "SetRegistryValueInGuest", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "SetRegistryValueInGuest", targetNamespace = "urn:vim25", className = "vim25.SetRegistryValueInGuestRequestType")
    @ResponseWrapper(localName = "SetRegistryValueInGuestResponse", targetNamespace = "urn:vim25", className = "vim25.SetRegistryValueInGuestResponse")
    public void setRegistryValueInGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "value", targetNamespace = "urn:vim25")
        GuestRegValueSpec value)
        throws GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param auth
     * @param vm
     * @param keyName
     * @param _this
     * @param expandStrings
     * @param matchPattern
     * @return
     *     returns java.util.List<vim25.GuestRegValueSpec>
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "ListRegistryValuesInGuest", action = "urn:vim25/6.0")
    @WebResult(name = "returnval", targetNamespace = "urn:vim25")
    @RequestWrapper(localName = "ListRegistryValuesInGuest", targetNamespace = "urn:vim25", className = "vim25.ListRegistryValuesInGuestRequestType")
    @ResponseWrapper(localName = "ListRegistryValuesInGuestResponse", targetNamespace = "urn:vim25", className = "vim25.ListRegistryValuesInGuestResponse")
    public List<GuestRegValueSpec> listRegistryValuesInGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "keyName", targetNamespace = "urn:vim25")
        GuestRegKeyNameSpec keyName,
        @WebParam(name = "expandStrings", targetNamespace = "urn:vim25")
        boolean expandStrings,
        @WebParam(name = "matchPattern", targetNamespace = "urn:vim25")
        String matchPattern)
        throws GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

    /**
     * 
     * @param auth
     * @param valueName
     * @param vm
     * @param _this
     * @throws RuntimeFaultFaultMsg
     * @throws GuestOperationsFaultFaultMsg
     * @throws TaskInProgressFaultMsg
     * @throws InvalidStateFaultMsg
     */
    @WebMethod(operationName = "DeleteRegistryValueInGuest", action = "urn:vim25/6.0")
    @RequestWrapper(localName = "DeleteRegistryValueInGuest", targetNamespace = "urn:vim25", className = "vim25.DeleteRegistryValueInGuestRequestType")
    @ResponseWrapper(localName = "DeleteRegistryValueInGuestResponse", targetNamespace = "urn:vim25", className = "vim25.DeleteRegistryValueInGuestResponse")
    public void deleteRegistryValueInGuest(
        @WebParam(name = "_this", targetNamespace = "urn:vim25")
        ManagedObjectReference _this,
        @WebParam(name = "vm", targetNamespace = "urn:vim25")
        ManagedObjectReference vm,
        @WebParam(name = "auth", targetNamespace = "urn:vim25")
        GuestAuthentication auth,
        @WebParam(name = "valueName", targetNamespace = "urn:vim25")
        GuestRegValueNameSpec valueName)
        throws GuestOperationsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg
    ;

}
