 require 'rbvmomi'
 
 vim = RbVmomi::VIM.connect host: ARGV[0], user: ARGV[1], password: ARGV[2], ssl: 'false', insecure: 'true'
 @dc = vim.serviceInstance.find_datacenter(ARGV[3])
 folder = @dc.vmFolder
 vm_source = folder.findByIp("#{ARGV[4]}")
 @source_name = vm_source.name
 puts('#vmName:' + @source_name);
 