package cz.muni.fi.resources;

import java.util.ArrayList;
import java.util.List;
import org.opennebula.client.PoolElement;
import org.opennebula.client.acl.Acl;
import org.opennebula.client.host.Host;

/**
 * This class represents an OpenNebula Host
 *
 * @author Gabriela Podolnikova
 */
public class HostXml {

    private int id;

    private String name;

    private int state;

    private int clusterId;
    
    private Integer disk_usage;
    
    private Integer mem_usage;
    
    private Integer cpu_usage;

    private Integer max_disk;

    private Integer max_mem;

    private Integer max_cpu;

    private Integer free_disk;
    
    private Integer free_mem;

    private Integer free_cpu;

    private Integer used_disk;
    
    private Integer used_mem;
    
    private Integer used_cpu;
      
    private Integer runningVms;
    
    private Integer reservedCpu;
    
    private Integer reservedMemory;
    
    private ArrayList<Integer> dsIds;
    
    private ArrayList<String> pcis;
    
    private final Host host;
    
    private ArrayList<DatastoreXml> datastores;

    public HostXml(Host host) {
        this.host = host;
        host.info();
        this.init();
    }
    
     private void init() {
        id = Integer.parseInt(host.xpath("/HOST/ID"));
        name = host.xpath("/HOST/NAME");
        state = Integer.parseInt(host.xpath("/HOST/STATE"));
        clusterId = Integer.parseInt(host.xpath("/HOST/CLUSTER_ID")); ;
        setDisk_usage((Integer) Integer.parseInt(host.xpath("/HOST/HOST_SHARE/DISK_USAGE")));
        setMem_usage((Integer) Integer.parseInt(host.xpath("/HOST/HOST_SHARE/MEM_USAGE")));
        setCpu_usage((Integer) Integer.parseInt(host.xpath("/HOST/HOST_SHARE/CPU_USAGE")));
        setMax_disk((Integer) Integer.parseInt(host.xpath("/HOST/HOST_SHARE/MAX_DISK")));
        setMax_mem((Integer) Integer.parseInt(host.xpath("/HOST/HOST_SHARE/MAX_MEM")));
        setMax_cpu((Integer) Integer.parseInt(host.xpath("/HOST/HOST_SHARE/MAX_CPU")));
        setFree_disk((Integer) Integer.parseInt(host.xpath("/HOST/HOST_SHARE/FREE_DISK")));
        setFree_mem((Integer) Integer.parseInt(host.xpath("/HOST/HOST_SHARE/FREE_MEM")));
        setFree_cpu((Integer) Integer.parseInt(host.xpath("/HOST/HOST_SHARE/FREE_CPU")));
        setUsed_disk((Integer) Integer.parseInt(host.xpath("/HOST/HOST_SHARE/USED_DISK")));
        setUsed_mem((Integer) Integer.parseInt(host.xpath("/HOST/HOST_SHARE/USED_MEM")));
        setUsed_cpu((Integer) Integer.parseInt(host.xpath("/HOST/HOST_SHARE/USED_CPU")));
        setRunningVms((Integer) Integer.parseInt(host.xpath("/HOST/HOST_SHARE/RUNNING_VMS")));
        if (!host.xpath("/HOST/TEMPLATE/RESERVED_CPU").equals("")) {
            reservedCpu = Integer.parseInt(host.xpath("/HOST/TEMPLATE/RESERVED_CPU"));
        }
        if (!host.xpath("/HOST/TEMPLATE/RESERVED_MEM").equals("")) {
            reservedMemory= Integer.parseInt(host.xpath("/HOST/TEMPLATE/RESERVED_MEM"));
        }
        //pcis = host.xpath("/HOST/HOST_SHARE/PCI_DEVICES");
        if (!host.xpath("/HOST/HOST_SHARE/PCI_DEVICES").equals("")) {
            get_pcis("/HOST/HOST_SHARE/PCI_DEVICES");
        }
        if (!host.xpath("/HOST/HOST_SHARE/DATASTORES").equals("")) {
            get_ds("/HOST/HOST_SHARE/DATASTORES");
        }
        System.out.println("Pcis: " + pcis);
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getClusterId() {
        return clusterId;
    }

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }


    @Override
    public String toString() {
        return "Host{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", state=" + state +
                ", clusterId=" + clusterId +
                '}';
    }

    
    /**
     * Tests whether a VM can be hosted by the host.
     * 
     * @param vm virtual machine to be tested
     * @return true if VM can be hosted, false otherwise
     */
    public boolean testCapacity(VmXml vm) {
        System.out.println("testCapacity:" + max_cpu + " - " + cpu_usage + " = " + (max_cpu - cpu_usage) + " =? " + free_cpu);
        if (((max_cpu - cpu_usage) >= vm.getCpu().intValue()) && ((max_mem - mem_usage) >= vm.getMemory())) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Increases cpu and memory on current host.
     * @param vm virtual machine with information for increasing capacity
     */
    public void addCapacity(VmXml vm) {
         cpu_usage += vm.getCpu().intValue();
         mem_usage += vm.getMemory().intValue();
    }
    
    public void delCapacity(VmXml vm) {
         cpu_usage -= vm.getCpu().intValue();
         mem_usage -= vm.getMemory().intValue();
    }
    
    public void get_ds(String xpathExpr) {
        datastores = new ArrayList<>();
        dsIds = new ArrayList<>();
        int i = 1;
        String node = host.xpath(xpathExpr + "/DS["+i+"]");
        DatastoreXml ds;
        while (!node.equals("")) {
            ds = new DatastoreXml();
            Integer id_ds = Integer.parseInt(host.xpath(xpathExpr + "/DS["+i+"]" + "/ID"));
            Integer free_mb = Integer.parseInt(host.xpath(xpathExpr + "/DS["+i+"]" + "/FREE_MB"));
            Integer total_mb = Integer.parseInt(host.xpath(xpathExpr + "/DS["+i+"]" + "/TOTAL_MB"));
            Integer used_mb = Integer.parseInt(host.xpath(xpathExpr + "/DS["+i+"]" + "/USED_MB"));
            ds.setId(id_ds);
            ds.setFree_mb(free_mb);
            ds.setTotal_mb(total_mb);
            ds.setUsed_mb(used_mb);
            i++;
            node = host.xpath(xpathExpr + "/DS["+i+"]");
            datastores.add(ds);
            dsIds.add(ds.getId());
        }
    }
    
    public void get_pcis(String xpathExpr) {
        pcis = new ArrayList<>();
        System.out.println("Inside get pcis: " + xpathExpr);
        int i = 1;
        String node = host.xpath(xpathExpr + "/PCI["+i+"]");
        System.out.println("node: " + node);
        while (!node.equals("")) {
            String device_name = host.xpath(xpathExpr + "/PCI["+i+"]" + "/DEVICE_NAME");
            System.out.println("device name: " + device_name);
            i++;
            node = host.xpath(xpathExpr + "/PCI["+i+"]");
            pcis.add(device_name);
        }
    }
    
    public <T> List<T> getNodes(Class klazz, String xpathExpr, PoolElement el) {
        List<T> list = new ArrayList<>();
        int i = 0;
        String node = el.xpath(xpathExpr + "["+i+"]");
        while (!node.equals("")) {
            try {
                list.add((T) klazz.getConstructor().newInstance()); // If default constructor
            } catch (Exception e) {
                System.err.println("Could not load specified node: " + node + e);
            }
            i++;
            node = el.xpath(xpathExpr + "["+i+"]");
        }
        return list;
    }
    
    public boolean testDs(VmXml vm) {
        boolean fits = false;
        if (!dsIds.contains(vm.getDatastore_id())) {
            return false;
        } else {
            for (DatastoreXml ds: datastores) {
                ArrayList<Integer> diskSizes =  vm.getDiskSizes();
                for (Integer diskSize: diskSizes) {
                    if (ds.getFree_mb() > diskSize) {
                        fits = true;
                    }
                }
            }
        }
        return fits;
    }
    
    public void checkHost(UserXml user, ArrayList<Acl> acls, VmXml vm) {
        Integer uid = vm.getUid();
        Integer gid = vm.getGid();
    }
    
    public boolean checkVmReqs(VmXml vm) {
        return false;
    }
    
    /**
     * @return the disk_usage
     */
    public Integer getDisk_usage() {
        return disk_usage;
    }

    /**
     * @param disk_usage the disk_usage to set
     */
    public void setDisk_usage(Integer disk_usage) {
        this.disk_usage = disk_usage;
    }

    /**
     * @return the mem_usage
     */
    public Integer getMem_usage() {
        return mem_usage;
    }

    /**
     * @param mem_usage the mem_usage to set
     */
    public void setMem_usage(Integer mem_usage) {
        this.mem_usage = mem_usage;
    }

    /**
     * @return the cpu_usage
     */
    public Integer getCpu_usage() {
        return cpu_usage;
    }

    /**
     * @param cpu_usage the cpu_usage to set
     */
    public void setCpu_usage(Integer cpu_usage) {
        this.cpu_usage = cpu_usage;
    }

    /**
     * @return the max_disk
     */
    public Integer getMax_disk() {
        return max_disk;
    }

    /**
     * @param max_disk the max_disk to set
     */
    public void setMax_disk(Integer max_disk) {
        this.max_disk = max_disk;
    }

    /**
     * @return the max_mem
     */
    public Integer getMax_mem() {
        return max_mem;
    }

    /**
     * @param max_mem the max_mem to set
     */
    public void setMax_mem(Integer max_mem) {
        this.max_mem = max_mem;
    }

    /**
     * @return the max_cpu
     */
    public Integer getMax_cpu() {
        return max_cpu;
    }

    /**
     * @param max_cpu the max_cpu to set
     */
    public void setMax_cpu(Integer max_cpu) {
        this.max_cpu = max_cpu;
    }

    /**
     * @return the free_disk
     */
    public Integer getFree_disk() {
        return free_disk;
    }

    /**
     * @param free_disk the free_disk to set
     */
    public void setFree_disk(Integer free_disk) {
        this.free_disk = free_disk;
    }

    /**
     * @return the free_mem
     */
    public Integer getFree_mem() {
        return free_mem;
    }

    /**
     * @param free_mem the free_mem to set
     */
    public void setFree_mem(Integer free_mem) {
        this.free_mem = free_mem;
    }

    /**
     * @return the free_cpu
     */
    public Integer getFree_cpu() {
        return free_cpu;
    }

    /**
     * @param free_cpu the free_cpu to set
     */
    public void setFree_cpu(Integer free_cpu) {
        this.free_cpu = free_cpu;
    }

    /**
     * @return the used_disk
     */
    public Integer getUsed_disk() {
        return used_disk;
    }

    /**
     * @param used_disk the used_disk to set
     */
    public void setUsed_disk(Integer used_disk) {
        this.used_disk = used_disk;
    }

    /**
     * @return the used_mem
     */
    public Integer getUsed_mem() {
        return used_mem;
    }

    /**
     * @param used_mem the used_mem to set
     */
    public void setUsed_mem(Integer used_mem) {
        this.used_mem = used_mem;
    }

    /**
     * @return the used_cpu
     */
    public Integer getUsed_cpu() {
        return used_cpu;
    }

    /**
     * @param used_cpu the used_cpu to set
     */
    public void setUsed_cpu(Integer used_cpu) {
        this.used_cpu = used_cpu;
    }

    /**
     * @return the runningVms
     */
    public Integer getRunningVms() {
        return runningVms;
    }

    /**
     * @param runningVms the runningVms to set
     */
    public void setRunningVms(Integer runningVms) {
        this.runningVms = runningVms;
    }

    /**
     * @return the reservedCpu
     */
    public Integer getReservedCpu() {
        return reservedCpu;
    }

    /**
     * @param reservedCpu the reservedCpu to set
     */
    public void setReservedCpu(Integer reservedCpu) {
        this.reservedCpu = reservedCpu;
    }

    /**
     * @return the reservedMemory
     */
    public Integer getReservedMemory() {
        return reservedMemory;
    }

    /**
     * @param reservedMemory the reservedMemory to set
     */
    public void setReservedMemory(Integer reservedMemory) {
        this.reservedMemory = reservedMemory;
    }

    /**
     * @return the dsIds
     */
    public ArrayList<Integer> getDsIds() {
        return dsIds;
    }

    /**
     * @return the pcis
     */
    public ArrayList<String> getPcis() {
        return pcis;
    }

    /**
     * @return the host
     */
    public Host getHost() {
        return host;
    }

    /**
     * @return the datastores
     */
    public ArrayList<DatastoreXml> getDatastores() {
        return datastores;
    }

}
