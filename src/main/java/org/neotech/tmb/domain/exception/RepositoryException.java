package org.neotech.tmb.domain.exception;

/**
 * 
 * @author
 *
 */
public class RepositoryException extends RuntimeException {
	
	private static final long serialVersionUID = 1811324008128700680L;

	public RepositoryException() {
		super();
	}

	public RepositoryException(
			String arg0,
			Throwable arg1,
			boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public RepositoryException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public RepositoryException(String arg0) {
		super(arg0);
	}

	public RepositoryException(Throwable arg0) {
		super(arg0);
	}

}
