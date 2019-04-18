package dz.nier.autre.bean;

import java.io.Serializable;

abstract public class BasicBean implements Serializable, Comparable<BasicBean> {
	private static final long serialVersionUID = 1L;
	protected long id;

	public BasicBean(long id) {
		this.id = id;
	}

	public long getId() {
		return this.id;
	}

	public BasicBean() {

	}

	public int compareTo(BasicBean arg0) {
		return ((Long) this.id).compareTo(arg0.getId());
	}

	public boolean equals(Object obj) {
		return this.compareTo((BasicBean) obj) == 0;
	}

	public String toString() {
		return "(id : " + this.id + "  |  ";
	}

	public String toSimpleString() {
		return "(id : " + this.id + "  |  ";
	}
}