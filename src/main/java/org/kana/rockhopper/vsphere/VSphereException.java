package org.kana.rockhopper.vsphere;

public class VSphereException extends Exception {

	private static final long serialVersionUID = -5045621255373210428L;

	public VSphereException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public VSphereException(String arg0) {
		super(arg0);
	}
}
