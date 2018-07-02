package org.neotech.tmb.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author
 *
 */
public class TmRcrd implements Serializable {	

	private static final long serialVersionUID = 7049110239293791294L;
	
	protected Date tm;

	/**
	 * Default
	 */
	public TmRcrd() {
		super();
	}

	/**
	 * 
	 * @param tm
	 */
	public TmRcrd(Date tm) {
		super();
		this.tm = tm;
	}

	/* ***** Get & Set ***** */

	public Date getTm() {
		return tm;
	}

	public void setTm(Date tm) {
		this.tm = tm;
	}

	/* ***** Override ***** */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tm == null) ? 0 : tm.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TmRcrd other = (TmRcrd) obj;
		if (tm == null) {
			if (other.tm != null)
				return false;
		} else if (!tm.equals(other.tm))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TmRcrd [tm=" + tm + "]";
	}

}
