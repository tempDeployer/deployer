1. Move commons-compress-1.9-javadoc.jar , commons-compress-1.9.jar and jsch.jar to Apache installation lib directory

2. Install knife-vsphere plugin - It is required to install ChefDK and then navigate to C:/opscode/chef/embedded/bin . Execute gem install knife-vsphere command

3.Make sure you have following credentials in knife.rb
knife[:vsphere_host] = "svlsl-vmvc1.kana-test.com"
knife[:vsphere_user] = "username"
knife[:vsphere_pass] = "Password"
knife[:vsphere_dc] = "SC StandAlone 1"
knife[:vsphere_insecure] = true

4. Change path variable values according to local file structure in deployerEnvironment.txt and keep this file inside C:\ directory 





