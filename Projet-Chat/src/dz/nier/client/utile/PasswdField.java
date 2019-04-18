package dz.nier.client.utile;

import javax.swing.JPasswordField;

public class PasswdField extends JPasswordField {
	public String getText() {
		return new String(this.getPassword());
	}
}
