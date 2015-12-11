package utils;

import java.awt.*;

/**
 * Created by Gvendurst on 11.12.2015.
 */
public class PicturePoint extends Point {
	private Facing facing;
	private int id = 0;

	public Facing getFacing() {
		return facing;
	}

	public void setFacing(Facing facing) {
		this.facing = facing;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PicturePoint(int x, int y, Facing facing) {
		super(x, y);
		this.facing = facing;
	}

	public PicturePoint(int x, int y, Facing facing, int id) {
		this(x, y, facing);
		this.id = id;
	}


}
