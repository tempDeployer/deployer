package org.kana.rockhopper.vsphere;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import org.kana.rockhopper.ConfigurationUtil;

import com.vmware.vim25.VirtualMachineCloneSpec;
import com.vmware.vim25.VirtualMachineRelocateSpec;
import com.vmware.vim25.mo.Datacenter;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

/**
 * Service wrapper for interacting directly with the vSphere server.
 * 
 * @author nthomson
 */
public class VSphereService {
	private static VSphereService instance;

	private String endpointUrl;
	private String username;
	private String password;
	private String datacenterName;

	public VSphereService() {
		this.endpointUrl = "https://" + ConfigurationUtil.getKey("deployer.vsphere.host") + "/sdk";
		this.username = ConfigurationUtil.getKey("deployer.vsphere.user");
		this.password = ConfigurationUtil.getKey("deployer.vsphere.pass");
		this.datacenterName = ConfigurationUtil.getKey("deployer.vsphere.dc");
	}

	public static VSphereService getInstance() {
		if (instance == null) {
			instance = new VSphereService();
		}

		return instance;
	}

	private ServiceInstance createServiceInstance() throws VSphereException {
		try {
			return new ServiceInstance(new URL(this.endpointUrl), this.username, this.password, true);
		} catch (RemoteException | MalformedURLException e) {
			throw new VSphereException("Failed to create service instance", e);
		}
	}

	public String getVMNameByIPAddress(String ipAddress) throws VSphereException {
		ServiceInstance si = createServiceInstance();

		Folder rootFolder = si.getRootFolder();
		
		String vmName = null;

		ManagedEntity[] mes;
		try {
			mes = new InventoryNavigator(rootFolder).searchManagedEntities("VirtualMachine");

			if (mes == null || mes.length == 0) {
				return null;
			}

			

			for (ManagedEntity me : mes) {
				VirtualMachine vm = (VirtualMachine) me;

				if (ipAddress.equals(vm.getGuest().getIpAddress())) {
					vmName = vm.getName();
					break;
				}
			}

			
		} catch (RemoteException e) {
			throw new VSphereException("Failed to find VM " + ipAddress + " - " + e.getMessage(), e);
		} finally {
			si.getServerConnection().logout();
		}
		
		return vmName;
	}
	
	public void removeVM(String name) throws VSphereException {
		ServiceInstance si = createServiceInstance();

		String vmPath = datacenterName + "/vm/" + name;

		try {
			VirtualMachine vm = (VirtualMachine) si.getSearchIndex().findByInventoryPath(vmPath);
			
			if (vm == null) {
				throw new VSphereException("Cannot remove VM, does not exist");
			}
			
			System.out.println("Deleting VM "+name+"...");
			
			vm.destroy_Task();
		} catch (RemoteException e) {
			throw new VSphereException("Failed to delete VM " + name + " - " + e.getMessage(), e);
		} finally {
			si.getServerConnection().logout();
		}
	}

	public void cloneVM(String name, String cloneName) throws VSphereException {
		ServiceInstance si = createServiceInstance();

		String vmPath = datacenterName + "/vm/" + name;

		try {
			VirtualMachine vm = (VirtualMachine) si.getSearchIndex().findByInventoryPath(vmPath);
			Datacenter dc = (Datacenter) si.getSearchIndex().findByInventoryPath(datacenterName);

			if (vm == null || dc == null) {
				System.out.println("VirtualMachine or Datacenter path is NOT correct. Pls double check. ");
				return;
			}
			Folder vmFolder = dc.getVmFolder();

			VirtualMachineCloneSpec cloneSpec = new VirtualMachineCloneSpec();
			cloneSpec.setLocation(new VirtualMachineRelocateSpec());
			cloneSpec.setPowerOn(false);
			cloneSpec.setTemplate(false);

			Task task = vm.cloneVM_Task(vmFolder, cloneName, cloneSpec);
			System.out.println("Launching the VM clone task. It might take a while. Please wait for the result ...");

			String status = task.waitForTask();
			if (status == Task.SUCCESS) {
				System.out.println("Virtual Machine got cloned successfully.");
			} else {
				System.out.println("Failure -: Virtual Machine cannot be cloned");
			}
		} catch (RemoteException | InterruptedException e) {
			throw new VSphereException("Failed to clone VM " + name + " - " + e.getMessage(), e);
		} finally {
			si.getServerConnection().logout();
		}
	}
}
