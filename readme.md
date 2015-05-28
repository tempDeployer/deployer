Deployer Tool
=============

The following document describes how to set up the deployer tool.

Basic Development
-----------------

In order to get a basic development environment running the project should be imported in to Eclipse as a Maven project. You must update the contents of the deployer.properties file to the appropriate values for your system.

To run it right click on the following file:

`deployer.launch`

And select

`Run As -> deployer`

This will start a local Tomcat 7 server which will make the application available on port 8680 at the following URL:

`http://localhost:8680/deployer.html`

vSphere Setup
-------------

The knife-vsphere plugin is required for the deployer to interact with vSphere directly. Install ChefDK and then navigate to 

`<install location>/embedded/bin` 

Execute the following command 

`gem install knife-vsphere command`

Then Make sure you have following credentials in knife.rb

`knife[:vsphere_host] = "svlsl-vmvc1.kana-test.com"
knife[:vsphere_user] = "username"
knife[:vsphere_pass] = "Password"
knife[:vsphere_dc] = "SC StandAlone 1"
knife[:vsphere_insecure] = true`





